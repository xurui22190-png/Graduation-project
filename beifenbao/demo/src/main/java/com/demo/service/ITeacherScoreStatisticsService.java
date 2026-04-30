package com.demo.service;

import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeacherScoreStatisticsDto;

public interface ITeacherScoreStatisticsService {
    ResponsePageResult getList(TeacherScoreStatisticsDto dto, Integer teacherId);
    ResponseResult getSummary(TeacherScoreStatisticsDto dto, Integer teacherId);
    ResponseResult getChart(TeacherScoreStatisticsDto dto, Integer teacherId);
    ResponseResult getTermList(Integer teacherId);
    ResponseResult getClassList(Integer teacherId);
    ResponseResult getCourseList(Integer teacherId);
}