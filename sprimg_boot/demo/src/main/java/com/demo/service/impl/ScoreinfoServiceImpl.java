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
import com.demo.model.Vwscores;
import com.demo.service.IScoreinfoService;
import com.demo.service.TeacherinfoService;
import com.demo.utils.CommUtil;
import com.demo.utils.UserToken;
import com.demo.vo.TeacherScoreVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ScoreinfoServiceImpl
        extends ServiceImpl<ScoreinfoMapper, Scoreinfo>
        implements IScoreinfoService {

    @Resource
    private ScoreinfoMapper scoreinfoMapper;

    @Resource
    private TeacherinfoService teacherinfoService;

    @Resource
    private com.demo.mapper.StudentinfoMapper studentinfoMapper;

    @Resource
    private com.demo.mapper.VwscoresMapper vwscoresMapper;


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

    @Override
    public com.demo.common.ResponseResult getStudentScoreList(Integer studentId, String qkey, Integer termid) {
        // 1. 自动转换身份：解决前端传 41 (AccountID) 而数据库需要 12 (sId) 的问题
        com.demo.model.Studentinfo student = studentinfoMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.demo.model.Studentinfo>()
                        .eq("sAccountId", studentId)
                        .or().eq("sId", studentId)
        );

        if (student == null) {
            return com.demo.common.ResponseResult.success("未找到该学生信息", new java.util.ArrayList<>());
        }

        Integer realSId = student.getSid();

        // 2. 构造查询条件
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.demo.model.Vwscores> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("sid", realSId); // 注意：视图里的字段名是 sid
        if (qkey != null && !qkey.isEmpty()) {
            wrapper.like("crName", qkey);
        }
        if (termid != null) {
            wrapper.eq("scTermId", termid);
        }

        List<Vwscores> list = vwscoresMapper.selectList(wrapper);

        // 3. 封装返回给前端的字段（必须带上 scCourseId，解决400错误）
        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (com.demo.model.Vwscores vw : list) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("yall", vw.getYall());
            map.put("crname", vw.getCrname());
            map.put("scscore", vw.getScscore());
            map.put("tname", vw.getTname());
            // ⚠️ 关键字段：对应前端 row.scCourseId
            map.put("scCourseId", vw.getSccourseid());
            result.add(map);
        }

        return com.demo.common.ResponseResult.success("获取成功", result);
    }
}