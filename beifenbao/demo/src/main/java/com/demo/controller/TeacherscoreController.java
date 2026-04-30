package com.demo.controller;

import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.ScoreinfoDto;
import com.demo.service.IScoreinfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/teacherscore")
public class TeacherscoreController {

    @Resource
    private IScoreinfoService scoreinfoService;

    /**
     * 教师成绩分页查询
     */
    @GetMapping("/getlist")
    public ResponsePageResult getlist(ScoreinfoDto obj, HttpServletRequest request) {
        return scoreinfoService.getTeacherScoreList(obj, request);
    }

    /**
     * 获取当前教师授课任务列表
     */
    @GetMapping("/gettasklist")
    public ResponseResult gettasklist(HttpServletRequest request) {
        return scoreinfoService.getTeacherTaskList(request);
    }

    /**
     * 获取某教学任务下的学生成绩录入列表
     */
    @GetMapping("/getstudents")
    public ResponseResult getstudents(@RequestParam Integer sctermid,
                                      @RequestParam Integer scclassid,
                                      @RequestParam Integer sccourseid,
                                      HttpServletRequest request) {
        return scoreinfoService.getTeacherScoreStudentList(sctermid, scclassid, sccourseid, request);
    }
}