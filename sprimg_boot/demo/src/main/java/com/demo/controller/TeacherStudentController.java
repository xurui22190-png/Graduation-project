package com.demo.controller;

import com.demo.common.ResponsePageResult;
import com.demo.dto.TeacherStudentDto;
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
}