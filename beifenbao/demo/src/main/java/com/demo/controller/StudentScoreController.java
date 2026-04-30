package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.utils.CommUtil;
import com.demo.common.ResponsePageResult;
import com.demo.dto.StudentScoreDto;
import com.demo.model.Studentinfo;
import com.demo.service.IStudentScoreService;
import com.demo.service.StudentinfoService;
import com.demo.utils.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/student/score")
public class StudentScoreController {

    @Autowired
    private IStudentScoreService studentScoreService;

    @Autowired
    private StudentinfoService studentinfoService;

    @GetMapping("/getlist")
    public ResponsePageResult getlist(StudentScoreDto dto, HttpServletRequest request) {
        UserToken userToken = CommUtil.getCurrentUser(request);

        Integer uid = userToken.getUrId();

        Studentinfo studentinfo = studentinfoService.getOne(
                new QueryWrapper<Studentinfo>().eq("sAccountId", uid)
        );

        if (studentinfo != null) {
            dto.setSid(studentinfo.getSid());
        } else {
            dto.setSid(-1);
        }

        return studentScoreService.getStudentScoreList(dto);
    }
}