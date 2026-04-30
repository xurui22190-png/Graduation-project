package com.demo.controller;

import com.demo.model.Userinfo;
import com.demo.service.UserinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/test")
@RestController
public class Testcontroller {
    @GetMapping("/hello")
    public String HelloWorld() {
        return "Hello World sddsddsas123d!!!!";
    }
    @Autowired
    private UserinfoService userinfoService;
    @GetMapping("/list")
    public List<Userinfo> list(){
        return userinfoService.list();
    }
}
