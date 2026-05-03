package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeacherStudentDto;
import com.demo.dto.TeacherStudentScoreSaveDto;
import com.demo.mapper.ScoreinfoMapper;
import com.demo.mapper.TeacherStudentMapper;
import com.demo.mapper.TeacherinfoMapper;
import com.demo.model.Scoreinfo;
import com.demo.model.Teacherinfo;
import com.demo.model.Vwteacherstudent;
import com.demo.service.IDiagnosisService;
import com.demo.service.TeacherStudentService;
import com.demo.utils.CommUtil;
import com.demo.utils.UserToken;
import com.demo.vo.TeacherCourseStudentVo;
import com.demo.vo.TeacherStudentScoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
@Service
public class TeacherStudentServiceImpl implements TeacherStudentService {

    @Autowired
    private TeacherStudentMapper teacherStudentMapper;

    @Autowired
    private TeacherinfoMapper teacherinfoMapper;

    @Autowired
    private IDiagnosisService diagnosisService;

    @Autowired
    private ScoreinfoMapper scoreinfoMapper;

    private Integer getCurrentTeacherId(HttpServletRequest request) {
        UserToken user = CommUtil.getCurrentUser(request);
        if (user == null || user.getUrId() == null) {
            return null;
        }

        QueryWrapper<Teacherinfo> wrapper = new QueryWrapper<>();
        wrapper.eq("tAccountId", user.getUrId());
        Teacherinfo teacherinfo = teacherinfoMapper.selectOne(wrapper);

        if (teacherinfo == null || teacherinfo.getTid() == null) {
            return null;
        }

        return teacherinfo.getTid();
    }

    @Override
    public ResponsePageResult getlist(TeacherStudentDto dto, HttpServletRequest request) {
        try {
            Integer teacherId = getCurrentTeacherId(request);
            if (teacherId == null) {
                return ResponsePageResult.ResResult(401, "未获取到当前教师信息", null);
            }

            long pageIndex = dto.getPageIndex() < 1 ? 1 : dto.getPageIndex();
            long pageSize = dto.getPageSize() < 1 ? 10 : dto.getPageSize();

            Page<Vwteacherstudent> page = new Page<>(pageIndex, pageSize);
            IPage<Vwteacherstudent> pageResult = teacherStudentMapper.getlist(page, dto, teacherId);

            return ResponsePageResult.PageResult(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponsePageResult.ResResult(500, "查询失败：" + e.getMessage(), new Page<>());
        }
    }

    @Override
    public ResponseResult getTaskList(HttpServletRequest request) {
        try {
            Integer teacherId = getCurrentTeacherId(request);
            if (teacherId == null) {
                return ResponseResult.Fail("未获取到当前教师信息");
            }

            List<Map<String, Object>> list = teacherStudentMapper.getTaskList(teacherId);
            if (list == null) {
                list = Collections.emptyList();
            }

            return ResponseResult.success("success", list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.Fail("获取授课任务失败：" + e.getMessage());
        }
    }

    @Override
    public ResponseResult getStudents(TeacherStudentDto dto, HttpServletRequest request) {
        try {
            Integer teacherId = getCurrentTeacherId(request);
            if (teacherId == null) {
                return ResponseResult.Fail("未获取到当前教师信息");
            }

            if (dto.getTctermid() == null || dto.getTcclassid() == null || dto.getTccourseid() == null) {
                return ResponseResult.Fail("参数不完整");
            }

            List<TeacherStudentScoreVo> list = teacherStudentMapper.getStudents(dto, teacherId);

            System.out.println("===== mapper raw list start =====");
            if (list == null) {
                System.out.println("list is null");
            } else {
                System.out.println("list size = " + list.size());
                for (int i = 0; i < list.size(); i++) {
                    System.out.println("item[" + i + "] = " + list.get(i));
                }
            }
            System.out.println("===== mapper raw list end =====");

            if (list == null) {
                list = Collections.emptyList();
            } else {
                list = list.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            System.out.println("===== filtered list =====");
            System.out.println("filtered size = " + list.size());
            for (int i = 0; i < list.size(); i++) {
                System.out.println("filtered item[" + i + "] = " + list.get(i));
            }

            return ResponseResult.success("success", list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.Fail("获取学生成绩列表失败：" + e.getMessage());
        }
    }

    @Override
    public ResponsePageResult getCourseStudents(TeacherStudentDto dto, HttpServletRequest request) {
        try {
            Integer teacherId = getCurrentTeacherId(request);
            if (teacherId == null) {
                return ResponsePageResult.ResResult(401, "未获取到当前教师信息", null);
            }

            long pageIndex = dto.getPageIndex() < 1 ? 1 : dto.getPageIndex();
            long pageSize = dto.getPageSize() < 1 ? 10 : dto.getPageSize();

            Page<TeacherCourseStudentVo> page = new Page<>(pageIndex, pageSize);
            IPage<TeacherCourseStudentVo> pageResult =
                    teacherStudentMapper.getCourseStudents(page, dto, teacherId);

            return ResponsePageResult.PageResult(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponsePageResult.ResResult(500, "查询失败：" + e.getMessage(), null);
        }
    }

    @Override
    @Transactional // 建议加上事务注解，保证数据一致性
    public ResponseResult saveScores(TeacherStudentScoreSaveDto dto, HttpServletRequest request) {
        try {
            Integer teacherId = getCurrentTeacherId(request);
            if (teacherId == null) {
                return ResponseResult.Fail("未获取到当前教师信息");
            }

            if (dto == null
                    || dto.getTctermid() == null
                    || dto.getTcclassid() == null
                    || dto.getTccourseid() == null) {
                return ResponseResult.Fail("参数不完整");
            }

            if (dto.getScoreList() == null || dto.getScoreList().isEmpty()) {
                return ResponseResult.Fail("没有可保存的数据");
            }

            for (TeacherStudentScoreSaveDto.ScoreItem item : dto.getScoreList()) {
                if (item == null || item.getSid() == null) {
                    continue;
                }

                Scoreinfo scoreinfo = null;

                // 1. 优先按 scid 查
                if (item.getScid() != null) {
                    scoreinfo = scoreinfoMapper.selectById(item.getScid());
                }

                // 2. 如果没找到，再按 学生+学期+班级+课程 查
                if (scoreinfo == null) {
                    QueryWrapper<Scoreinfo> wrapper = new QueryWrapper<>();
                    wrapper.eq("scStudentId", item.getSid())
                            .eq("scTermId", dto.getTctermid())
                            .eq("scClassId", dto.getTcclassid())
                            .eq("scCourseId", dto.getTccourseid());
                    scoreinfo = scoreinfoMapper.selectOne(wrapper);
                }

                Double scoreValue = item.getScscore() == null ? null : item.getScscore();
                Integer statusValue = item.getScstatus() == null ? 0 : item.getScstatus();

                BigDecimal scoreDecimal = scoreValue == null
                        ? BigDecimal.ZERO
                        : BigDecimal.valueOf(scoreValue);

                // 3. 没有记录就新增
                if (scoreinfo == null) {
                    scoreinfo = new Scoreinfo();
                    scoreinfo.setScstudentid(item.getSid());
                    scoreinfo.setSctermid(dto.getTctermid());
                    scoreinfo.setScclassid(dto.getTcclassid());
                    scoreinfo.setSccourseid(dto.getTccourseid());
                    scoreinfo.setScscore(scoreDecimal);
                    scoreinfo.setScstatus(statusValue);
                    scoreinfo.setSccreatedate(LocalDateTime.now());
                    scoreinfo.setScteacherid(teacherId);

                    scoreinfoMapper.insert(scoreinfo);
                } else {
                    // 4. 已有记录就更新
                    scoreinfo.setScscore(scoreDecimal);
                    scoreinfo.setScstatus(statusValue);

                    if (scoreinfo.getSccreatedate() == null) {
                        scoreinfo.setSccreatedate(LocalDateTime.now());
                    }
                    if (scoreinfo.getScteacherid() == null) {
                        scoreinfo.setScteacherid(teacherId);
                    }

                    scoreinfoMapper.updateById(scoreinfo);
                }

                // ================= 毕设核心亮点：智能拦截与知识点明细自动生成 =================
                // 只有当有成绩时才生成
                if (scoreValue != null && scoreValue > 0) {
                    try {
                        // 调用诊断引擎，自动将总分拆解为知识点得分，存入 score_detail 表
                        diagnosisService.generateSimulatedDetails(
                                item.getSid(), // 学生ID
                                dto.getTccourseid(), // 课程ID
                                scoreValue // 将总分传入
                        );
                        System.out.println("====== 🚀学情引擎：已成功为学生 " + item.getSid() + " 生成课程 " + dto.getTccourseid() + " 的明细！======");
                    } catch (Exception e) {
                        // 加上 try-catch 防止生成失败影响老师正常录入总分
                        System.err.println("====== ❌学情引擎警告：生成明细失败：" + e.getMessage() + " ======");
                    }
                }
                // ==============================================================================
            }

            return ResponseResult.success("保存成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.Fail("保存失败：" + e.getMessage());
        }
    }
}