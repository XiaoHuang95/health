package com.blank.service;

import com.blank.entity.PageResult;
import com.blank.pojo.Setmeal;

import java.util.List;

/**
 * 体检套餐服务接口
 */
public interface SetMealService {
    //添加体检套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);
    //分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    //获取套餐列表数据
    List<Setmeal> findAll();
    //根据id查询套餐
    Setmeal findById(int id);
}
