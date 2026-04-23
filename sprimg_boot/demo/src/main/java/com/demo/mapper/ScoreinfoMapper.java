package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.dto.ScoreinfoDto;
import com.demo.model.Scoreinfo;
import com.demo.vo.TeacherScoreStudentVo;
import com.demo.vo.TeacherScoreVo;
import com.demo.vo.TeacherTaskVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreinfoMapper extends BaseMapper<Scoreinfo> {

    IPage<TeacherScoreVo> getTeacherScoreList(Page<TeacherScoreVo> page,
                                              @Param("obj") ScoreinfoDto obj,
                                              @Param("teacherid") Integer teacherid);

    List<TeacherTaskVo> getTeacherTaskList(@Param("teacherid") Integer teacherid);

    List<java.util.Map<String, Object>> getTeacherScoreStudentList(@Param("teacherid") Integer teacherid,
                                                                   @Param("sctermid") Integer sctermid,
                                                                   @Param("scclassid") Integer scclassid,
                                                                   @Param("sccourseid") Integer sccourseid);

    Integer checkTeachingAuth(@Param("teacherid") Integer teacherid,
                              @Param("sctermid") Integer sctermid,
                              @Param("scclassid") Integer scclassid,
                              @Param("sccourseid") Integer sccourseid);
}