package com.demo.controller;

import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeacherStudentDto;
import com.demo.dto.TeacherStudentScoreSaveDto;
import com.demo.service.TeacherStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/teacherstudent")
@Api(tags = "教师查看学生接口")
public class TeacherStudentController {

    @Autowired
    private TeacherStudentService teacherStudentService;

    @GetMapping("/getlist")
    @ApiOperation("教师查看自己授课下的学生列表")
    public ResponsePageResult getlist(TeacherStudentDto dto, HttpServletRequest request) {
        return teacherStudentService.getlist(dto, request);
    }

    @GetMapping("/gettasklist")
    @ApiOperation("获取当前教师授课任务列表")
    public ResponseResult getTaskList(HttpServletRequest request) {
        return teacherStudentService.getTaskList(request);
    }

    @GetMapping("/getstudents")
    @ApiOperation("成绩录入时获取学生列表")
    public ResponseResult getStudents(TeacherStudentDto dto, HttpServletRequest request) {
        return teacherStudentService.getStudents(dto, request);
    }
    @PostMapping("/savescores")
    @ApiOperation("保存学生成绩")
    public ResponseResult saveScores(@RequestBody TeacherStudentScoreSaveDto dto, HttpServletRequest request) {
        return teacherStudentService.saveScores(dto, request);
    }
    @GetMapping("/getcoursestudents")
    @ApiOperation("查看当前教师某门课下的学生列表")
    public ResponsePageResult getCourseStudents(TeacherStudentDto dto, HttpServletRequest request) {
        return teacherStudentService.getCourseStudents(dto, request);
    }
}