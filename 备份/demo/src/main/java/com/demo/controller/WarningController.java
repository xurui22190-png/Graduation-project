package com.demo.controller;

import com.demo.common.ResponseResult; // 根据你的实际路径修改
import com.demo.service.IWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 学业预警控制器
 */
@RestController
@RequestMapping("/api/warning") // 注意：如果你的前端配的是 /warning，这里请保持一致
public class WarningController {

    @Autowired
    private IWarningService warningService;

    // 🌟 1. 注入 Redis 模板
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 手动触发指定课程的预警扫描 (🌟已接入 Redis 接口防抖限流)
     * @param courseId 课程ID
     */
    @PostMapping("/scan/{courseId}")
    public ResponseResult scanCourse(@PathVariable Integer courseId) {
        // 定义 Redis 的防刷锁 Key，格式如 "scan_lock_4"
        String redisKey = "scan_lock_" + courseId;

        // 🌟 2. 去 Redis 里查，这个 Key 存不存在？
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            // 如果存在，说明 30 秒内已经有人（或自己）点过了，直接拦截，保护 Python 模型！
            return ResponseResult.Fail("AI 扫描任务正在后台拼命处理中，请勿频繁点击，请 30 秒后再试！");
        }

        try {
            // 3. 如果没被锁定，执行真正的扫描逻辑
            warningService.scanAndGenerateWarnings(courseId);

            // 🌟 4. 核心：扫描成功后，往 Redis 里塞入这个 Key，并设置 30 秒自动过期！
            // 这样接下来的 30 秒内，任何人再点这个课程的扫描，都会被上面的 if 拦截。
            redisTemplate.opsForValue().set(redisKey, "locked", 30, TimeUnit.SECONDS);

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