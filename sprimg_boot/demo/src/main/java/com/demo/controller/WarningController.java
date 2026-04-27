package com.demo.controller;

import com.demo.common.ResponseResult; // 根据你的实际路径修改
import com.demo.service.IWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学业预警控制器
 */
@RestController
@RequestMapping("/api/warning")
public class WarningController {

    @Autowired
    private IWarningService warningService;

    /**
     * 手动触发指定课程的预警扫描
     * @param courseId 课程ID
     */
    @PostMapping("/scan/{courseId}")
    public ResponseResult scanCourse(@PathVariable Integer courseId) {
        try {
            warningService.scanAndGenerateWarnings(courseId);
            return ResponseResult.success("AI学业预警扫描完成", null);
        } catch (Exception e) {
            return ResponseResult.Fail("扫描失败：" + e.getMessage());
        }
    }

    /**
     * 获取预警监控大盘列表
     */
    @GetMapping("/list")
    public ResponseResult getWarningList() {
        List<Map<String, Object>> list = warningService.getWarningList();
        return ResponseResult.success("获取预警列表成功", list);
    }

    /**
     * 教师处理预警（如：已面谈、消除预警）
     */
    @PutMapping("/handle/{warningId}/{status}")
    public ResponseResult handleWarning(@PathVariable Integer warningId, @PathVariable Integer status) {
        warningService.updateWarningStatus(warningId, status);
        return ResponseResult.success("预警状态更新成功", null);
    }
    /**
     * 发送专属学业预警公告给学生
     */
    @PostMapping("/sendNotice")
    public ResponseResult sendNotice(@RequestBody Map<String, Object> params) {
        try {
            // 提取前端传来的参数
            Integer studentId = (Integer) params.get("studentId");
            String title = (String) params.get("title");
            String content = (String) params.get("content");

            warningService.sendWarningNotice(studentId, title, content);
            return ResponseResult.success("发送成功", null);
        } catch (Exception e) {
            return ResponseResult.Fail("发送失败: " + e.getMessage());
        }
    }
}