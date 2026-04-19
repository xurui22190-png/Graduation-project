package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.dto.TeacherStudentDto;
import com.demo.mapper.TeacherStudentMapper;
import com.demo.mapper.TeacherinfoMapper;
import com.demo.model.Teacherinfo;
import com.demo.utils.UserToken;
import com.demo.model.Vwteacherstudent;
import com.demo.service.TeacherStudentService;
import com.demo.utils.CommUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TeacherStudentServiceImpl implements TeacherStudentService {

    @Autowired
    private TeacherStudentMapper teacherStudentMapper;

    @Autowired
    private TeacherinfoMapper teacherinfoMapper;

    @Override
    public ResponsePageResult getlist(TeacherStudentDto dto, HttpServletRequest request) {
        try {
            UserToken user = CommUtil.getCurrentUser(request);
            if (user == null || user.getUrId() == null) {
                return ResponsePageResult.ResResult(401, "未获取到当前登录用户", null);
            }

            QueryWrapper<Teacherinfo> wrapper = new QueryWrapper<>();
            wrapper.eq("tAccountId", user.getUrId());
            Teacherinfo teacherinfo = teacherinfoMapper.selectOne(wrapper);

            if (teacherinfo == null || teacherinfo.getTid() == null) {
                return ResponsePageResult.ResResult(400, "当前用户未绑定教师信息", null);
            }

            long pageIndex = dto.getPageIndex() < 1 ? 1 : dto.getPageIndex();
            long pageSize = dto.getPageSize() < 1 ? 10 : dto.getPageSize();

            Page<Vwteacherstudent> page = new Page<>(pageIndex, pageSize);
            IPage<Vwteacherstudent> pageResult = teacherStudentMapper.getlist(page, dto, teacherinfo.getTid());

            return ResponsePageResult.PageResult(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponsePageResult.ResResult(500, "查询失败：" + e.getMessage(), new Page<>());
        }
    }
}