package com.blank.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.blank.dao.CheckGroupDao;
import com.blank.entity.PageResult;
import com.blank.pojo.CheckGroup;
import com.blank.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    //添加检查组合，同时需要设置检查组合和检查项的关联关系
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        //设置检查项和检查组的关系
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }
    //分页查询
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //设置分页条件
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }
    //根据id查询检查组信息
    @Override
    public CheckGroup findById(Integer id) {
        CheckGroup checkGroup = checkGroupDao.findById(id);
        return checkGroup;
    }

    //编辑检查组信息
    public void edit(CheckGroup checkGroup,Integer[] checkitemIds){
        //根据检查组id删除中间表数据(清理原有关系)
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //向中间表插入数据(建立新的关系)
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        //更新检查组的基本信息
        checkGroupDao.edit(checkGroup);
    }
    //删除检查组信息以及起关联的检查项信息
    @Override
    public boolean deleteById(Integer id) {
        int res = checkGroupDao.deleteAssociation(id);
        if (res>0){
            checkGroupDao.deleteById(id);
            return true;
        }
        return false;
    }
    //查询所有检查组信息
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    //设置检查项和检查组的关系
    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds) {
        if (checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId: checkitemIds){
                Map<String,Integer> map = new HashMap<>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
