package com.demo.controller;

import com.demo.common.ResponseResult; // 1. 确保导入路径正确
import com.demo.service.IScoreDetailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scoreDetail")
public class ScoreDetailController {

    @Resource
    private IScoreDetailService scoreDetailService;

    /**
     * 获取学生单门课的知识点掌握明细
     */
    @GetMapping("/getCourseDetail")
    public ResponseResult getCourseDetail(@RequestParam Integer sid, @RequestParam Integer courseId) {
        // 调用 Service 获取数据
        List<Map<String, Object>> details = scoreDetailService.getDetailsByCourse(sid, courseId);

        if (details == null || details.isEmpty()) {
            // 2. 根据你的 ResponseResult.java，失败方法是 Fail()
            return ResponseResult.Fail("该课程暂无细分数据");
        }

        // 3. 根据你的 ResponseResult.java，成功带数据的方法是 success(String msg, Object data)
        return ResponseResult.success("查询成功", details);
    }
}