package com.blank.dao;

import com.blank.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    //添加体检套餐
    void add(Setmeal setmeal);
    //绑定体检套餐与检查组的多对多的关系
    void setSetMealAndCheckGroup(Map<String, Integer> map);
    //分页查询
    Page<Setmeal> selectByCondition(String queryString);
    //获取体检套餐列表
    List<Setmeal> findAll();
    //根据套餐id查询套餐信息
    Setmeal findById(int id);
}
