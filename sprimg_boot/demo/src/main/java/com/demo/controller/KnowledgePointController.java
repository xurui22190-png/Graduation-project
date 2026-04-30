package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.common.ResponseResult;
import com.demo.model.KnowledgePoint;
import com.demo.service.IKnowledgePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgePointController {

    @Autowired
    private IKnowledgePointService knowledgePointService;

    /**
     * 1. 获取指定课程的知识点列表
     * @param courseId 课程ID
     */
    @GetMapping("/list/{courseId}")
    public ResponseResult getListByCourseId(@PathVariable Integer courseId) {
        LambdaQueryWrapper<KnowledgePoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgePoint::getCourseId, courseId);

        List<KnowledgePoint> list = knowledgePointService.list(wrapper);

        // 🌟 适配你的 ResponseResult: success(String _msg, Object _data)
        return ResponseResult.success("查询成功", list);
    }

    /**
     * 2. 新增知识点 (包含查重逻辑)
     */
    @PostMapping("/save")
    public ResponseResult saveKnowledgePoint(@RequestBody KnowledgePoint knowledgePoint) {
        if (knowledgePoint.getCourseId() == null || knowledgePoint.getPointName() == null || knowledgePoint.getPointName().trim().isEmpty()) {
            // 🌟 适配你的 ResponseResult: Fail(String _msg)
            return ResponseResult.Fail("参数不完整，知识点名称不能为空");
        }

        // 查重：同一门课程下，不能有同名的知识点
        LambdaQueryWrapper<KnowledgePoint> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgePoint::getCourseId, knowledgePoint.getCourseId())
                .eq(KnowledgePoint::getPointName, knowledgePoint.getPointName().trim());

        if (knowledgePointService.count(wrapper) > 0) {
            return ResponseResult.Fail("该课程已存在同名知识点，请勿重复添加");
        }

        // 保存到数据库
        boolean success = knowledgePointService.save(knowledgePoint);
        if (success) {
            // 🌟 适配你的 ResponseResult: success(String _msg, Object _data)
            return ResponseResult.success("添加成功", null);
        } else {
            return ResponseResult.Fail("添加失败，系统异常");
        }
    }

    /**
     * 3. 删除知识点
     * @param id 知识点的主键ID
     */
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteKnowledgePoint(@PathVariable Integer id) {
        boolean success = knowledgePointService.removeById(id);
        if (success) {
            return ResponseResult.success("删除成功", null);
        } else {
            return ResponseResult.Fail("删除失败，该知识点可能已被移除");
        }
    }
}