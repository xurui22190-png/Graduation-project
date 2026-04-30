package com.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mapper.KnowledgePointMapper;
import com.demo.model.KnowledgePoint;
import com.demo.service.IKnowledgePointService;
import org.springframework.stereotype.Service;

@Service
public class KnowledgePointServiceImpl extends ServiceImpl<KnowledgePointMapper, KnowledgePoint> implements IKnowledgePointService {
}