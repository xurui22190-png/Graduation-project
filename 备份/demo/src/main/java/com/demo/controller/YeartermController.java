package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.common.ResponseResult;
import com.demo.model.Yearterm;
import com.demo.service.YeartermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/yearterm")
@Api(tags = "学期管理")
public class YeartermController {

    @Autowired
    private YeartermService yeartermService;

    @GetMapping("getall")
    @ApiOperation("获取全部学期")
    public ResponseResult getall() {
        LambdaQueryWrapper<Yearterm> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Yearterm::getYid);
        return ResponseResult.success("success", yeartermService.list(wrapper));
    }
}