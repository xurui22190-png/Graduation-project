package com.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.dto.StudentCourseDto;
import com.demo.vo.StudentCourseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudentCourseMapper {

    IPage<StudentCourseVo> getStudentCourseList(Page<StudentCourseVo> page,
                                                @Param("query") StudentCourseDto query,
                                                @Param("uid") Integer uid);
}