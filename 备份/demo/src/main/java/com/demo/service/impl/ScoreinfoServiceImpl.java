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
import com.demo.service.IDiagnosisService; // 💡 引入AI诊断服务
import com.demo.utils.CommUtil;
import com.demo.utils.UserToken;
import com.demo.vo.TeacherScoreVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

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
    // 在类开头的其他 @Resource 下方添加
    @Resource
    private com.demo.mapper.TeachingMapper teachingMapper;
    @Resource
    private IDiagnosisService diagnosisService; // 💡 注入你的AI知识点拆解引擎


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
    public ResponseResult getStudentScoreList(Integer studentId, String qkey, Integer termid) {
        com.demo.model.Studentinfo student = studentinfoMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.demo.model.Studentinfo>()
                        .eq("sAccountId", studentId)
                        .or().eq("sId", studentId)
        );

        if (student == null) {
            return ResponseResult.success("未找到该学生信息", new java.util.ArrayList<>());
        }

        Integer realSId = student.getSid();

        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.demo.model.Vwscores> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("sid", realSId);
        if (qkey != null && !qkey.isEmpty()) {
            wrapper.like("crName", qkey);
        }
        if (termid != null) {
            wrapper.eq("scTermId", termid);
        }

        List<Vwscores> list = vwscoresMapper.selectList(wrapper);

        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (com.demo.model.Vwscores vw : list) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("yall", vw.getYall());
            map.put("crname", vw.getCrname());
            map.put("scscore", vw.getScscore());
            map.put("tname", vw.getTname());
            map.put("scCourseId", vw.getSccourseid());
            result.add(map);
        }

        return ResponseResult.success("获取成功", result);
    }


    // =========================================================================================
    // 💡 以下为新增的毕设最强亮点：【一维成绩智能降维与多维逆向推演算法】
    // =========================================================================================

    /**
     * 核心算法：根据老师录入的总分，带约束地随机逆向推演出：平时分、测试分、期末分
     */
    private void reverseCalculateSubScores(Scoreinfo score, double weightReg, double weightTest, double weightExam) {
        if (score.getScscore() == null) return;

        double totalScore = score.getScscore().doubleValue();
        Random random = new Random();

        // 1. 模拟平时成绩：通常平时成绩比总分高，并且要在 0-100 范围内（浮动 -2 到 +8 分）
        double regular = totalScore + (random.nextDouble() * 10 - 2);
        regular = Math.min(100.0, Math.max(0.0, regular));

        // 2. 模拟测试成绩：围绕总分上下随机波动（浮动 -5 到 +5 分）
        double test = totalScore + (random.nextDouble() * 10 - 5);
        test = Math.min(100.0, Math.max(0.0, test));

        // 3. 终极闭环：通过代数方程反解出期末成绩，确保加权之和绝对等于老师给的总分！
        // 公式推导: 期末 = (总分 - 平时*平时权重 - 测试*测试权重) / 期末权重
        double exam = (totalScore - (regular * weightReg) - (test * weightTest)) / weightExam;

        // 极限兜底：如果反解出的期末成绩超出了真实物理边界（>100 或 <0），说明总分太极端，直接退化为全相等
        if (exam > 100.0 || exam < 0.0) {
            regular = totalScore;
            test = totalScore;
            exam = totalScore;
        }

        // 4. 精确处理：保留一位小数并塞入数据库实体中
        score.setScRegular(BigDecimal.valueOf(regular).setScale(1, RoundingMode.HALF_UP));
        score.setScTest(BigDecimal.valueOf(test).setScale(1, RoundingMode.HALF_UP));
        score.setScExam(BigDecimal.valueOf(exam).setScale(1, RoundingMode.HALF_UP));
    }


    /**
     * 【暴露给 Controller 的成绩保存入口】
     * 老师在前端列表批量敲完总分，或者Excel导入后，都是调用的这里。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveTeacherScores(List<Scoreinfo> scoreList) {
        if (scoreList == null || scoreList.isEmpty()) {
            return ResponseResult.Fail("提交的数据为空");
        }

        int successCount = 0;
        for (Scoreinfo score : scoreList) {
            if (score.getScscore() != null) {

                // --- 【修改点：动态获取真实权重】 ---
                double wReg = 0.3, wTest = 0.2, wExam = 0.5; // 设置默认兜底值

                // 根据当前成绩记录的课程和班级，去 teaching 表找该老师设置的权重
                com.demo.model.Teaching teaching = teachingMapper.selectOne(
                        new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.demo.model.Teaching>()
                                .eq("tcCourseId", score.getSccourseid())
                                .eq("tcClassId", score.getScclassid())
                                .last("LIMIT 1")
                );

                if (teaching != null && teaching.getWexam() != null) {
                    // 如果数据库里有老师设置的权重，就用老师设置的
                    wReg = teaching.getWregular();
                    wTest = teaching.getWtest();
                    wExam = teaching.getWexam();
                }
                // ------------------------------------

                // 1. 调用你已有的逆向算法（会自动填补平时、测试、期末字段）
                reverseCalculateSubScores(score, wReg, wTest, wExam);

                // 2. 保持你原有的更新/保存逻辑
                if (score.getScid() != null && score.getScid() > 0) {
                    this.updateById(score);
                } else {
                    // 查重：防止重复插入
                    Scoreinfo existScore = this.getOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Scoreinfo>()
                            .eq("scStudentId", score.getScstudentid())
                            .eq("scCourseId", score.getSccourseid())
                            .eq("scTermId", score.getSctermid()));
                    if (existScore != null) {
                        score.setScid(existScore.getScid());
                        this.updateById(score);
                    } else {
                        this.save(score);
                    }
                }

                // 3. 保持你原有的AI诊断联动
                try {
                    diagnosisService.generateSimulatedDetails(
                            score.getScstudentid(),
                            score.getSccourseid(),
                            score.getScscore().doubleValue()
                    );
                } catch (Exception e) {
                    // 打印日志但不中断主逻辑
                    System.err.println("细粒度拆解提示：" + e.getMessage());
                }
                successCount++;
            }
        }
        return ResponseResult.success("成功录入并实时拆解 " + successCount + " 名学生成绩！", null);
    }
}