package com.blank.dao;

import com.blank.pojo.CheckGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    //添加检查项
    void add(CheckGroup checkGroup);
    //设置检查组和检查项的关系
    void setCheckGroupAndCheckItem(Map<String, Integer> map);
    //分页查询
    Page<CheckGroup> selectByCondition(String queryString);
    //根据id查询
    CheckGroup findById(Integer id);
    //编辑检查组信息
    void edit(CheckGroup checkGroup);
    //清除与中间表的原有关系
    int deleteAssociation(Integer id);
    //删除检查组信息以及其关联的检查项信息
    void deleteById(Integer id);
    //查询所有检查组的信息
    List<CheckGroup> findAll();
}
