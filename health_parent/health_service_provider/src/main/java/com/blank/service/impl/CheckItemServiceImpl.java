package com.blank.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.blank.dao.CheckItemDao;
import com.blank.entity.PageResult;
import com.blank.pojo.CheckItem;
import com.blank.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项服务
 */
@Service(interfaceClass = CheckItemService.class) //interfaceClass：指定服务提供方实现的 interface 的类
@Transactional
public class CheckItemServiceImpl implements CheckItemService{

    @Autowired
    private CheckItemDao checkItemDao;
    //新增检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //分页查询
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //删除
    @Override
    public void deleteById(Integer id) throws RuntimeException {
        //查询当前检查项是否和检查组相关联
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0 ){
            //当前查询项被引用不能删除
            throw new RuntimeException("当前检查项被引用，不能删除");
        }
        //可以删除
        checkItemDao.deleteById(id);
    }

    //编辑检查项信息
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }
    //根据id查询检查项
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }
    //查询所有检查项
    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> checkItemList = checkItemDao.findAll();
        return checkItemList;
    }
}
