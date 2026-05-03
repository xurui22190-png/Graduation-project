package com.demo.service;

import java.util.List;
import java.util.Map;

public interface IDiagnosisService {
    /**
     * 核心亮点：激活本地 AI 引擎生成精简诊断报告
     * @param intent 毕业意向 (job:就业, grad:考研, civil:考公)
     */
    String generateAiReport(Integer studentId, Integer courseId, String intent);

    /**
     * 诊断弱项：返回得分率低于60%的知识点名称
     */
    List<String> diagnoseWeakPoints(Integer studentId, Integer courseId);

    /**
     * 获取用于前端 Echarts 雷达图的数据
     */
    Map<String, Object> getRadarData(Integer studentId, Integer courseId);

    /**
     * 根据总分，自动拆解模拟知识点得分
     */
    void generateSimulatedDetails(Integer studentId, Integer courseId, Double totalScore);

    // 占位方法
    String generateClassAiReport(Integer classId, Integer courseId);
    Map<String, Object> getClassAnalysisData(Integer classId, Integer courseId);
}