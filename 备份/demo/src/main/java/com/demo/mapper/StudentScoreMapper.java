package com.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.dto.StudentScoreDto;
import com.demo.vo.StudentScoreVo;
import org.apache.ibatis.annotations.Param;

public interface StudentScoreMapper {
    IPage<StudentScoreVo> getStudentScoreList(IPage<StudentScoreVo> page, @Param("dto") StudentScoreDto dto);
}