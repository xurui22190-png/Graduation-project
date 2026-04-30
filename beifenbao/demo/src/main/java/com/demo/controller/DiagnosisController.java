package com.demo.controller; // ⚠️注意：如果你的包名不是这个，请保留你原本的第一行

import com.demo.common.ResponseResult; // ⚠️注意：如果你的Result类不叫这个，请根据你的项目修改
import com.demo.service.IDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseResult getAiReport(@RequestParam Integer studentId, @RequestParam Integer courseId) {
        String aiReport = diagnosisService.generateAiReport(studentId, courseId);
        return ResponseResult.success("个人AI报告生成成功", aiReport);
    }

    /**
     * 3. 供 Vue 调用的接口：获取班级 AI 走势预测报告
     */
    @GetMapping("/getClassAiReport")
    public ResponseResult getClassAiReport(@RequestParam Integer classId, @RequestParam Integer courseId) {
        String aiReport = diagnosisService.generateClassAiReport(classId, courseId);
        return ResponseResult.success("班级AI报告生成成功", aiReport);
    }
}