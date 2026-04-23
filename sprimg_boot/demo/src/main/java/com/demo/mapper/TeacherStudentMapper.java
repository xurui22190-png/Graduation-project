package com.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.dto.TeacherStudentDto;
import com.demo.model.Vwteacherstudent;
import com.demo.vo.TeacherStudentScoreVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import com.demo.vo.TeacherStudentScoreVo;    import com.demo.vo.TeacherCourseStudentVo;

public interface TeacherStudentMapper {

    IPage<Vwteacherstudent> getlist(Page<Vwteacherstudent> page,
                                    @Param("dto") TeacherStudentDto dto,
                                    @Param("teacherId") Integer teacherId);

    List<Map<String, Object>> getTaskList(@Param("teacherId") Integer teacherId);


    IPage<TeacherCourseStudentVo> getCourseStudents(Page<TeacherCourseStudentVo> page,
                                                    @Param("dto") TeacherStudentDto dto,
                                                    @Param("teacherId") Integer teacherId);

    List<TeacherStudentScoreVo> getStudents(@Param("dto") TeacherStudentDto dto,
                                            @Param("teacherId") Integer teacherId);
}