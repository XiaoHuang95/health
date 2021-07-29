package com.blank.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.blank.dao.OrderSettingDao;
import com.blank.pojo.OrderSetting;
import com.blank.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    //添加设置信息
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (orderSettingList != null && orderSettingList.size() > 0){
            for (OrderSetting orderSetting : orderSettingList){
                //检查该时间是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                //存在执行更新操作
                if (count >0){
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //不存在 执行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }
    //根据日期查询预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //2021-2
        String dateBegin = date + "-1";  //2021-2-1
        String dateEnd = date + "-31"; //2021-2-31
        Map map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList){
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate()); //获得日期几号
            orderSettingMap.put("number",orderSetting.getNumber());//可以预约人数
            orderSettingMap.put("reservations",orderSetting.getReservations()); //以预约人数
            data.add(orderSettingMap);
        }
        return data;
    }
    //根据指定日期修改可预约人数
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count >0 ){
            //当前日期已经进行了预约设置，进行修改操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //当前日期没有进行预约设置，进行添加操作
            orderSettingDao.add(orderSetting);
        }

    }
}
