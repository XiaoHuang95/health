package com.blank.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.blank.constant.MessageConstant;
import com.blank.entity.Result;
import com.blank.pojo.Setmeal;
import com.blank.service.SetMealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetMealService setMealService;

    //获取所有套餐信息
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> setmealList = setMealService.findAll();
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmealList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }
}
