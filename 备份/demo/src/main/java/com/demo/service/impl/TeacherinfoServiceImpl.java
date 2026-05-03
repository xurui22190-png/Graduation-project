package com.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mapper.TeacherinfoMapper;
import com.demo.model.Teacherinfo;
import com.demo.service.ITeacherinfoService;
import com.demo.service.TeacherinfoService;
import org.springframework.stereotype.Service;

@Service
public class TeacherinfoServiceImpl
        extends ServiceImpl<TeacherinfoMapper, Teacherinfo>
        implements TeacherinfoService, ITeacherinfoService {
}