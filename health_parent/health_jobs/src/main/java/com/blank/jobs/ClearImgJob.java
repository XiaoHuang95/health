package com.blank.jobs;

import com.blank.constant.RedisConstant;
import com.blank.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 自定义Job，实现定时清理垃圾图片
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //根据redis中保存的两个set集合进行差值运算，获得垃圾图片的集合
        Set<String> diffSet = jedisPool.getResource().
                sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (diffSet != null){
            for (String picName : diffSet){
                //删除七牛云上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                //删除redis上的图片
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES);
            }
        }
    }
}
