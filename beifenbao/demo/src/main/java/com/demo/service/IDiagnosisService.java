package com.demo.service;  // 第一行必须是这个

import java.util.List;

public interface IDiagnosisService {
    /**
     * 根据总分，自动生成该课程各知识点的模拟得分（用于毕设展示）
     */
    void generateSimulatedDetails(Integer studentId, Integer courseId, Double totalScore);

    /**
     * 诊断弱项：返回得分率低于60%的知识点名称列表
     */
    List<String> diagnoseWeakPoints(Integer studentId, Integer courseId);
    /**
     * 获取用于前端 Echarts 雷达图的数据
     */
    java.util.Map<String, Object> getRadarData(Integer studentId, Integer courseId);

    // ================== 第四个方法：接入本地 Python AI 引擎生成诊断报告 ==================
    String generateAiReport(Integer studentId, Integer courseId);

    String generateClassAiReport(Integer classId, Integer courseId);
}