package com.blank.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.blank.constant.MessageConstant;
import com.blank.constant.RedisConstant;
import com.blank.entity.PageResult;
import com.blank.entity.QueryPageVo;
import com.blank.entity.Result;
import com.blank.pojo.Setmeal;
import com.blank.service.SetMealService;
import com.blank.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.UUID;

@RequestMapping("/setmeal")
@RestController
public class SetMealController {
    @Reference
    private SetMealService setMealService;
    @Resource
    private JedisPool jedisPool;
    /**
     * 上传图片
     * @param imgFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf - 1);
            //使用UUID随机产生文件名,防止文件名处分
            String fileName = UUID.randomUUID().toString()+suffix;
            //上传图片
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //图片上传成功
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        }catch (Exception e){
            e.printStackTrace();
            //图片上传失败
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加体检套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setMealService.add(setmeal,checkgroupIds);
        }catch (Exception e){
            //新增套餐失败
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageVo
     * @return
     */
    @RequestMapping("/findPage")
    private PageResult findPage(@RequestBody QueryPageVo queryPageVo){
        PageResult pageResult = setMealService.findPage(
                queryPageVo.getCurrentPage(),
                queryPageVo.getPageSize(),
                queryPageVo.getQueryString());
        return pageResult;
    }
}
