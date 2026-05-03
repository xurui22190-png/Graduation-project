package com.demo.service.impl;

import com.demo.mapper.ScoreDetailMapper;
import com.demo.model.ScoreDetail;
import com.demo.service.IScoreDetailService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl; // 必须有这行实现类导入

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ScoreDetailServiceImpl extends ServiceImpl<ScoreDetailMapper, ScoreDetail> implements IScoreDetailService {

    @Resource
    private ScoreDetailMapper scoreDetailMapper;

    @Override
    public List<Map<String, Object>> getDetailsByCourse(Integer sid, Integer courseId) {
        return scoreDetailMapper.selectDetailsWithPointName(sid, courseId);
    }
}