package com.demo.controller;

import com.demo.utils.CommUtil;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeacherScoreStatisticsDto;
import com.demo.model.Teacherinfo;
import com.demo.utils.UserToken;
import com.demo.service.ITeacherScoreStatisticsService;
import com.demo.service.ITeacherinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/teacher/scorestatistics")
public class TeacherScoreStatisticsController {

    @Autowired
    private ITeacherScoreStatisticsService teacherScoreStatisticsService;

    @Autowired
    private ITeacherinfoService teacherinfoService;

    private Integer getCurrentTeacherId(HttpServletRequest request) {
        UserToken user = CommUtil.getCurrentUser(request);
        if (user == null) {
            return null;
        }

        Integer uid = user.getUrId();

        Teacherinfo teacher = teacherinfoService.lambdaQuery()
                .eq(Teacherinfo::getTaccountid, uid)
                .last("limit 1")
                .one();

        return teacher == null ? null : teacher.getTid();
    }

    @GetMapping("/getlist")
    public ResponsePageResult getList(TeacherScoreStatisticsDto dto, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        if (teacherId == null) {
            return ResponsePageResult.ResResult(500, "未获取到当前教师信息", null);
        }
        return teacherScoreStatisticsService.getList(dto, teacherId);
    }

    @GetMapping("/summary")
    public ResponseResult getSummary(TeacherScoreStatisticsDto dto, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        if (teacherId == null) {
            return ResponseResult.Fail("未获取到当前教师信息");
        }
        return teacherScoreStatisticsService.getSummary(dto, teacherId);
    }

    @GetMapping("/chart")
    public ResponseResult getChart(TeacherScoreStatisticsDto dto, HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        if (teacherId == null) {
            return ResponseResult.Fail("未获取到当前教师信息");
        }
        return teacherScoreStatisticsService.getChart(dto, teacherId);
    }

    @GetMapping("/termlist")
    public ResponseResult getTermList(HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        if (teacherId == null) {
            return ResponseResult.Fail("未获取到当前教师信息");
        }
        return teacherScoreStatisticsService.getTermList(teacherId);
    }

    @GetMapping("/classlist")
    public ResponseResult getClassList(HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        if (teacherId == null) {
            return ResponseResult.Fail("未获取到当前教师信息");
        }
        return teacherScoreStatisticsService.getClassList(teacherId);
    }

    @GetMapping("/courselist")
    public ResponseResult getCourseList(HttpServletRequest request) {
        Integer teacherId = getCurrentTeacherId(request);
        if (teacherId == null) {
            return ResponseResult.Fail("未获取到当前教师信息");
        }
        return teacherScoreStatisticsService.getCourseList(teacherId);
    }
}