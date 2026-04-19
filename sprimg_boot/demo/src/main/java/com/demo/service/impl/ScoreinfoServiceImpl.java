package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.ScoreinfoDto;
import com.demo.mapper.ScoreinfoMapper;
import com.demo.model.Scoreinfo;
import com.demo.model.Teacherinfo;
import com.demo.service.IScoreinfoService;
import com.demo.service.TeacherinfoService;
import com.demo.utils.CommUtil;
import com.demo.utils.UserToken;
import com.demo.vo.TeacherScoreVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class ScoreinfoServiceImpl
        extends ServiceImpl<ScoreinfoMapper, Scoreinfo>
        implements IScoreinfoService {

    @Resource
    private ScoreinfoMapper scoreinfoMapper;

    @Resource
    private TeacherinfoService teacherinfoService;

    private Integer getCurrentTeacherId(HttpServletRequest request) {
        UserToken userToken = CommUtil.getCurrentUser(request);
        if (userToken == null || userToken.getUrId() == null) {
            return null;
        }

        Teacherinfo teacherinfo = teacherinfoService.getOne(
                new QueryWrapper<Teacherinfo>().eq("tAccountId", userToken.getUrId())
        );

        if (teacherinfo == null) {
            return null;
        }

        return teacherinfo.getTid();
    }

    @Override
    public ResponsePageResult getTeacherScoreList(ScoreinfoDto obj, HttpServletRequest request) {
        Integer teacherid = getCurrentTeacherId(request);
        if (teacherid == null) {
            return ResponsePageResult.ResResult(400, "未找到当前教师信息", null);
        }

        if (obj.getPageIndex() <= 0) {
            obj.setPageIndex(1);
        }
        if (obj.getPageSize() <= 0) {
            obj.setPageSize(10);
        }

        Page<TeacherScoreVo> page = new Page<>(obj.getPageIndex(), obj.getPageSize());
        return ResponsePageResult.PageResult(scoreinfoMapper.getTeacherScoreList(page, obj, teacherid));
    }

    @Override
    public ResponseResult getTeacherTaskList(HttpServletRequest request) {
        Integer teacherid = getCurrentTeacherId(request);
        if (teacherid == null) {
            return ResponseResult.Fail("未找到当前教师信息");
        }
        return ResponseResult.success("获取成功", scoreinfoMapper.getTeacherTaskList(teacherid));
    }

    @Override
    public ResponseResult getTeacherScoreStudentList(Integer sctermid,
                                                     Integer scclassid,
                                                     Integer sccourseid,
                                                     HttpServletRequest request) {
        Integer teacherid = getCurrentTeacherId(request);
        if (teacherid == null) {
            return ResponseResult.Fail("未找到当前教师信息");
        }

        if (sctermid == null || sctermid <= 0 ||
                scclassid == null || scclassid <= 0 ||
                sccourseid == null || sccourseid <= 0) {
            return ResponseResult.Fail("学期、班级、课程不能为空");
        }

        Integer count = scoreinfoMapper.checkTeachingAuth(teacherid, sctermid, scclassid, sccourseid);
        if (count == null || count <= 0) {
            return ResponseResult.Fail("无权限查看该教学任务下的学生成绩");
        }

        return ResponseResult.success(
                "获取成功",
                scoreinfoMapper.getTeacherScoreStudentList(teacherid, sctermid, scclassid, sccourseid)
        );
    }
}