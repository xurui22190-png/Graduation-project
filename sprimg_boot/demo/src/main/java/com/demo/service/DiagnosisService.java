package com.demo.service;

/**
 * 智能诊断与成绩分析服务接口
 */
public interface DiagnosisService {

    /**
     * 核心亮点：根据加权总分自动拆解并生成细粒度知识点成绩明细
     *
     * @param studentId 学生ID
     * @param courseId  课程ID
     * @param totalScore 该生这门课的总分
     */
    void generateSimulatedDetails(Integer studentId, Integer courseId, Double totalScore);

}