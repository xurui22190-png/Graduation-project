package com.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.dto.TeacherStudentDto;
import com.demo.model.Vwteacherstudent;
import org.apache.ibatis.annotations.Param;

public interface TeacherStudentMapper {

    IPage<Vwteacherstudent> getlist(Page<Vwteacherstudent> page,
                                    @Param("dto") TeacherStudentDto dto,
                                    @Param("teacherId") Integer teacherId);
}