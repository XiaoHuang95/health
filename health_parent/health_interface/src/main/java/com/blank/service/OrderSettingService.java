package com.blank.service;

import com.blank.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    //添加设置信息
    void add(List<OrderSetting> orderSettingList);
    //根据日期查询预约设置数据
    List<Map> getOrderSettingByMonth(String date);
    //根据指定日期修改可预约人数
    void editNumberByDate(OrderSetting orderSetting);
}
