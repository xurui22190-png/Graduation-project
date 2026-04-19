package com.demo.mapper;

import com.demo.dto.TeacherScoreStatisticsDto;
import com.demo.vo.TeacherScoreStatisticsVo;
import com.demo.vo.TeacherScoreSummaryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TeacherScoreStatisticsMapper {

    List<TeacherScoreStatisticsVo> getList(@Param("dto") TeacherScoreStatisticsDto dto,
                                           @Param("teacherId") Integer teacherId);

    Long getCount(@Param("dto") TeacherScoreStatisticsDto dto,
                  @Param("teacherId") Integer teacherId);

    TeacherScoreSummaryVo getSummary(@Param("dto") TeacherScoreStatisticsDto dto,
                                     @Param("teacherId") Integer teacherId);

    List<Map<String, Object>> getRangeData(@Param("dto") TeacherScoreStatisticsDto dto,
                                           @Param("teacherId") Integer teacherId);

    List<Map<String, Object>> getClassAvgData(@Param("dto") TeacherScoreStatisticsDto dto,
                                              @Param("teacherId") Integer teacherId);

    List<Map<String, Object>> getTrendData(@Param("dto") TeacherScoreStatisticsDto dto,
                                           @Param("teacherId") Integer teacherId);

    List<Map<String, Object>> getTermList(@Param("teacherId") Integer teacherId);

    List<Map<String, Object>> getClassList(@Param("teacherId") Integer teacherId);

    List<Map<String, Object>> getCourseList(@Param("teacherId") Integer teacherId);
}