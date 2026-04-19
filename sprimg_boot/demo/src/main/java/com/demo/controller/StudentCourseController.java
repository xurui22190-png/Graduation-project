package com.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.dto.StudentCourseDto;
import com.demo.mapper.StudentCourseMapper;
import com.demo.vo.StudentCourseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/student/course")
public class StudentCourseController {

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @GetMapping("/getlist")
    public ResponsePageResult getlist(StudentCourseDto query, HttpServletRequest request) {
        System.out.println("当前登录用户uid = " + request.getAttribute("uid"));

        int pageIndex = query.getPageIndex() <= 0 ? 1 : query.getPageIndex();
        int pageSize = query.getPageSize() <= 0 ? 10 : query.getPageSize();

        Page<StudentCourseVo> pager = new Page<>(pageIndex, pageSize);

        Integer uid = (Integer) request.getAttribute("uid");

        if (uid == null) {
            return ResponsePageResult.PageResult(new Page<StudentCourseVo>(pageIndex, pageSize));
        }

        IPage<StudentCourseVo> pagedata = studentCourseMapper.getStudentCourseList(pager, query, uid);
        return ResponsePageResult.PageResult(pagedata);
    }
}