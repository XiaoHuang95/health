package com.blank.service;

import com.blank.entity.PageResult;
import com.blank.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    //新增检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);
    //分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    //根据id查询
    CheckGroup findById(Integer id);
    //根据检查组合id查询对应的所有检查项id
    List<Integer> findCheckItemIdsByCheckGroupId(Integer checkGroupId);
    //编辑检查组信息
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);
    //删除检查组信息
    boolean deleteById(Integer id);
    //查询所有检查组信息
    List<CheckGroup> findAll();
}
