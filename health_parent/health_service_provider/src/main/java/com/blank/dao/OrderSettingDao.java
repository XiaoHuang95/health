package com.blank.dao;

import com.blank.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    //判断日期是否存在
    long findCountByOrderDate(Date orderDate);
    //编辑预约信息
    void editNumberByOrderDate(OrderSetting orderSetting);
    //添加预约信息
    void add(OrderSetting orderSetting);
    //根据日期查询预约设置数据
    List<OrderSetting> getOrderSettingByMonth(Map date);
}
