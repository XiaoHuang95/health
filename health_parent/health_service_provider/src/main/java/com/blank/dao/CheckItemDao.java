package com.blank.dao;

import com.blank.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * 持久层Dao
 */
public interface CheckItemDao {
    //添加
    public void add(CheckItem checkItem);
    //带条件的分页查询
    public Page<CheckItem> selectByCondition(String queryString);
    //查找当前检查项是否被引用
    public long findCountByCheckItemId(Integer checkItemId);
    //删除
    public void deleteById(Integer id);
    //编辑检查项信息
    void edit(CheckItem checkItem);
    //根据id查询检查项
    CheckItem findById(Integer id);
    //查询所有检查项
    List<CheckItem> findAll();
}
