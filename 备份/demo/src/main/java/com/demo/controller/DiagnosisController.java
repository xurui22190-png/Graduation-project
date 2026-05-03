package com.demo.controller; // ⚠️注意：如果你的包名不是这个，请保留你原本的第一行

import com.demo.common.ResponseResult; // ⚠️注意：如果你的Result类不叫这个，请根据你的项目修改
import com.demo.service.IDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diagnosis") // 确保这里带了 /api
public class DiagnosisController {

    @Autowired
    private IDiagnosisService diagnosisService;

    /**
     * 1. 供 Vue 前端 Echarts 调用的接口：获取学情雷达图数据
     */
    @GetMapping("/getRadarData")
    public ResponseResult getRadarData(@RequestParam Integer studentId, @RequestParam Integer courseId) {
        Map<String, Object> radarData = diagnosisService.getRadarData(studentId, courseId);

        List<?> dataList = (List<?>) radarData.get("data");
        if (dataList == null || dataList.isEmpty()) {
            return ResponseResult.Fail("暂无学情分析数据");
        }

        return ResponseResult.success("获取雷达图数据成功", radarData);
    }

    /**
     * 2. 供 Vue 调用的接口：获取个人 AI 深度诊断报告
     */
    @GetMapping("/getAiReport")
    public ResponseResult getAiReport(
            @RequestParam Integer studentId,
            @RequestParam Integer courseId,
            @RequestParam(defaultValue = "job") String intent
    ) {
        String report = diagnosisService.generateAiReport(studentId, courseId, intent);

        // 🌟 修复点：第一个参数传"success"，第二个参数传真正的report
        return ResponseResult.success("success", report);
    }

    /**
     * 3. 供 Vue 调用的接口：获取班级 AI 走势预测报告
     */
    @GetMapping("/getClassAiReport")
    public ResponseResult getClassAiReport(@RequestParam Integer classId, @RequestParam Integer courseId) {
        String aiReport = diagnosisService.generateClassAiReport(classId, courseId);
        return ResponseResult.success("班级AI报告生成成功", aiReport);
    }
    @GetMapping("/classAnalysis")
    public ResponseResult getClassAnalysis(@RequestParam Integer classId, @RequestParam Integer courseId) {
        try {
            Map<String, Object> data = diagnosisService.getClassAnalysisData(classId, courseId);
            // 💡 修复点：调用你 ResponseResult.java 中定义的两个参数的方法
            return ResponseResult.success("查询成功", data);
        } catch (Exception e) {
            return ResponseResult.Fail("分析失败：" + e.getMessage());
        }
    }


}