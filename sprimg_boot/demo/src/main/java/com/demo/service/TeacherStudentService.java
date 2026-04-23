package com.demo.service;

import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeacherStudentDto;
import com.demo.dto.TeacherStudentScoreSaveDto;

import javax.servlet.http.HttpServletRequest;

public interface TeacherStudentService {

    ResponsePageResult getlist(TeacherStudentDto dto, HttpServletRequest request);

    ResponseResult getTaskList(HttpServletRequest request);

    ResponseResult getStudents(TeacherStudentDto dto, HttpServletRequest request);

    ResponsePageResult getCourseStudents(TeacherStudentDto dto, HttpServletRequest request);

    ResponseResult saveScores(TeacherStudentScoreSaveDto dto, HttpServletRequest request);
}