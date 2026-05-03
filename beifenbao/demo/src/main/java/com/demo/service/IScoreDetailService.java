package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.model.ScoreDetail;

import java.util.List;
import java.util.Map;

public interface IScoreDetailService extends IService<ScoreDetail> {
    List<Map<String, Object>> getDetailsByCourse(Integer sid, Integer courseId);
}
