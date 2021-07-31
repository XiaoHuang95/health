package com.blank.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.blank.constant.RedisConstant;
import com.blank.dao.SetMealDao;
import com.blank.entity.PageResult;
import com.blank.pojo.Setmeal;
import com.blank.service.SetMealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;

    @Value("${out_put_path}")
    private String outPutPath;
    //添加体检套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //新增体检套餐
        setMealDao.add(setmeal);
        if (checkgroupIds != null && checkgroupIds.length >0){
            //绑定体检套餐与检查组的多对多的关系
            setSetMealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
        //将图片保存到redis
        savePic2Redis(setmeal.getImg());
        //新增套餐后需要重新生成静态页面
        this.generateMobileStaticHtml();
    }
    //生成静态页面
    private void generateMobileStaticHtml() {
        //准备模板文件中所需要的数据
        List<Setmeal> setmealList = this.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面(多个)
        generateMobileSetmealDetailHtml(setmealList);
    }

    //生成套餐详情静态页面(多个)
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList){
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("setmeal",this.findById(setmeal.getId()));
            this.generateHtml("mobile_setmeal_detail.ftl",
                    "setmeal_detail_"+setmeal.getId()+".html",dataMap);
        }
    }

    //生成套餐列表静态页面
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("setmealList",setmealList);
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
    }

    //生成页面的方法
    public void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
        System.out.println("======");
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            //加载模板文件
            Template template = configuration.getTemplate(templateName);
            //生成数据
            File docFile = new File(outPutPath + "\\"+htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            //输出文件
            template.process(dataMap,out);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (null != out){
                    out.flush();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    //分页查询
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setMealDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }
    //获取套餐列表数据
    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    //根据id查询套餐信息
    @Override
    public Setmeal findById(int id) {
        return setMealDao.findById(id);
    }

    //将图片名称保存到redis
    private void savePic2Redis(String img) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }

    //绑定体检套餐与检查组的多对多的关系
    private void setSetMealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkGroupId : checkgroupIds){
            Map<String,Integer> map = new HashMap<>();
            map.put("setmeal_id",id);
            map.put("checkgroup_id",checkGroupId);
            setMealDao.setSetMealAndCheckGroup(map);
        }
    }
}
