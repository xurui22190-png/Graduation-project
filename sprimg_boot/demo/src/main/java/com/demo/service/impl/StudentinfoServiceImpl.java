package com.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mapper.StudentinfoMapper;
import com.demo.model.Studentinfo;
import com.demo.service.StudentinfoService;
import org.springframework.stereotype.Service;

@Service
public class StudentinfoServiceImpl
        extends ServiceImpl<StudentinfoMapper, Studentinfo>
        implements StudentinfoService {
}