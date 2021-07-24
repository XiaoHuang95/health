package com.blank.service;

import com.blank.entity.PageResult;
import com.blank.pojo.CheckItem;

/**
 * 检查项服务接口
 */
public interface CheckItemService {
    //添加
    public void add(CheckItem checkItem);
    //分页查询
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);
    //删除
    void deleteById(Integer id);
    //修改检查项信息
    void edit(CheckItem checkItem);
    //根据id查询检查项信息
    CheckItem findById(Integer id);
}
