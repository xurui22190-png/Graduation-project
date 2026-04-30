package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.ScoreinfoDto;
import com.demo.model.Scoreinfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IScoreinfoService extends IService<Scoreinfo> {

    ResponsePageResult getTeacherScoreList(ScoreinfoDto obj, HttpServletRequest request);

    ResponseResult getTeacherTaskList(HttpServletRequest request);

    ResponseResult getTeacherScoreStudentList(Integer sctermid,
                                              Integer scclassid,
                                              Integer sccourseid,
                                              HttpServletRequest request);
    ResponseResult saveTeacherScores(List<Scoreinfo> scoreList);
    ResponseResult getStudentScoreList(Integer studentId, String qkey, Integer termid);
}