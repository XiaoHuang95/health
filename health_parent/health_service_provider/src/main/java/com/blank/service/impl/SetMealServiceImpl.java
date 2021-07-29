package com.blank.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.blank.constant.RedisConstant;
import com.blank.dao.SetMealDao;
import com.blank.entity.PageResult;
import com.blank.pojo.Setmeal;
import com.blank.service.SetMealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;
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
