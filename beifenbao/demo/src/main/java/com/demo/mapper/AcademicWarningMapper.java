package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.model.AcademicWarning;
import org.apache.ibatis.annotations.Mapper;

/**
 * 学业预警 Mapper 接口
 */
@Mapper
public interface AcademicWarningMapper extends BaseMapper<AcademicWarning> {
    // MyBatis-Plus 已经内置了 insert, selectList, updateById 等方法
    // 如果后续需要复杂的多表联查（比如查出学生姓名、课程名称），可以在这里写 @Select 注解
}