package com.demo.service;

import com.demo.common.ResponsePageResult;
import com.demo.dto.TeacherStudentDto;

import javax.servlet.http.HttpServletRequest;

public interface TeacherStudentService {
    ResponsePageResult getlist(TeacherStudentDto dto, HttpServletRequest request);
}