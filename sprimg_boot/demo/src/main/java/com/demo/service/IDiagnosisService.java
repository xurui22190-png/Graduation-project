package com.demo.service;

import com.demo.dto.ScoreWeaknessReportDto;
import com.demo.dto.StudentPlanningReportDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

public interface IDiagnosisService {
    /**
     * 核心亮点：激活本地 AI 引擎生成精简诊断报告
     * @param intent 毕业意向 (job:就业, grad:考研, civil:考公)
     */
    String generateAiReport(Integer studentId, Integer courseId, String intent);

    /**
     * 流式生成单门课程 AI 诊断报告，供前端 EventSource 实时接收。
     */
    SseEmitter streamAiReport(Integer studentId, Integer courseId, String intent);

    /**
     * 根据学生六维画像和成绩列表，生成未来规划分析
     */
    String generatePlanningReport(StudentPlanningReportDto dto);

    /**
     * 只根据学生所有成绩，生成弱项诊断
     */
    String generateScoreWeaknessReport(ScoreWeaknessReportDto dto);

    /**
     * 诊断弱项：返回得分率低于60%的知识点名称
     */
    List<String> diagnoseWeakPoints(Integer studentId, Integer courseId);

    /**
     * 根据单门课程知识点雷达数据，生成学情分析
     */
    String generateCourseAnalysis(Integer studentId, Integer courseId);

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
