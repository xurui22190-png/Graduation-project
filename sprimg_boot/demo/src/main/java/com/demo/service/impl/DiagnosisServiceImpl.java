package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.mapper.KnowledgePointMapper;
import com.demo.mapper.ScoreDetailMapper;
import com.demo.mapper.ScoreinfoMapper;
import com.demo.mapper.StudentinfoMapper; // 💡 必须引入
import com.demo.model.KnowledgePoint;
import com.demo.model.ScoreDetail;
import com.demo.model.Scoreinfo;
import com.demo.model.Studentinfo; // 💡 必须引入
import com.demo.service.IDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class DiagnosisServiceImpl implements IDiagnosisService {

    @Autowired
    private KnowledgePointMapper knowledgePointMapper;

    @Autowired
    private ScoreDetailMapper scoreDetailMapper;

    @Autowired
    private ScoreinfoMapper scoreinfoMapper;

    @Autowired
    private StudentinfoMapper studentinfoMapper; // 用于转换账号ID和学生ID

    /**
     * 💡 私有辅助方法：将前端传来的任何ID（账号ID或学生ID）转换为真实的sid
     */
    private Integer getRealSid(Integer inputId) {
        Studentinfo student = studentinfoMapper.selectOne(
                new QueryWrapper<Studentinfo>().eq("sAccountId", inputId).or().eq("sid", inputId)
        );
        return (student != null) ? student.getSid() : inputId;
    }

    // ================== 1. 生成模拟成绩明细（当老师端未手动录入明细时调用） ==================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSimulatedDetails(Integer studentId, Integer courseId, Double totalScore) {
        Integer realSid = getRealSid(studentId);

        // 先清理旧的模拟数据
        scoreDetailMapper.delete(new QueryWrapper<ScoreDetail>()
                .eq("student_id", realSid).eq("course_id", courseId));

        // 查找该课程下由老师定义的知识点
        List<KnowledgePoint> points = knowledgePointMapper.selectList(
                new QueryWrapper<KnowledgePoint>().eq("course_id", courseId));

        if (points == null || points.isEmpty()) return;

        double baseRate = totalScore / 100.0;
        for (KnowledgePoint kp : points) {
            ScoreDetail detail = new ScoreDetail();
            detail.setStudentId(realSid);
            detail.setCourseId(courseId);
            detail.setPointId(kp.getId());
            detail.setMaxScore(100.0);

            // 围绕总分率进行小范围随机浮动，模拟真实表现
            double fluctuation = (Math.random() * 0.2) - 0.1;
            double actualRate = Math.min(1.0, Math.max(0.0, baseRate + fluctuation));

            detail.setActualScore(Math.round(actualRate * 100 * 10.0) / 10.0);
            scoreDetailMapper.insert(detail);
        }
    }

    // ================== 2. 获取雷达图数据（同步老师端的知识维度画像） ==================
    @Override
    public Map<String, Object> getRadarData(Integer studentId, Integer courseId) {
        Integer realSid = getRealSid(studentId);

        // 1. 尝试查询已有的成绩明细（老师评分生成的或系统生成的）
        List<ScoreDetail> details = scoreDetailMapper.selectList(
                new QueryWrapper<ScoreDetail>().eq("student_id", realSid).eq("course_id", courseId));

        // 2. 🚀【核心修复】：如果明细为空，但主表有总分，则自动激活“同步生成”逻辑
        if (details.isEmpty()) {
            Scoreinfo mainScore = scoreinfoMapper.selectOne(
                    new QueryWrapper<Scoreinfo>().eq("scStudentId", realSid).eq("scCourseId", courseId));

            if (mainScore != null && mainScore.getScscore() != null) {
                // 老师给了分，但还没明细，我们帮他生成一份
                generateSimulatedDetails(realSid, courseId, mainScore.getScscore().doubleValue());
                // 重新查一次
                details = scoreDetailMapper.selectList(
                        new QueryWrapper<ScoreDetail>().eq("student_id", realSid).eq("course_id", courseId));
            }
        }

        List<Map<String, Object>> indicator = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (ScoreDetail detail : details) {
            KnowledgePoint kp = knowledgePointMapper.selectById(detail.getPointId());
            if (kp != null) {
                Map<String, Object> ind = new HashMap<>();
                ind.put("name", kp.getPointName());
                ind.put("max", detail.getMaxScore());
                indicator.add(ind);
                data.add(detail.getActualScore());
            }
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("indicator", indicator);
        resultMap.put("data", data);
        return resultMap;
    }

    // ================== 3. 诊断弱项 ==================
    @Override
    public List<String> diagnoseWeakPoints(Integer studentId, Integer courseId) {
        Integer realSid = getRealSid(studentId);
        List<String> weakPoints = new ArrayList<>();

        List<ScoreDetail> details = scoreDetailMapper.selectList(
                new QueryWrapper<ScoreDetail>().eq("student_id", realSid).eq("course_id", courseId));

        for (ScoreDetail detail : details) {
            if (detail.getActualScore() / detail.getMaxScore() < 0.6) {
                KnowledgePoint kp = knowledgePointMapper.selectById(detail.getPointId());
                if (kp != null) weakPoints.add(kp.getPointName());
            }
        }
        return weakPoints;
    }

    // ================== 4. 接入 Python 生成 AI 诊断报告 ==================
    @Override
    public String generateAiReport(Integer studentId, Integer courseId) {
        Integer realSid = getRealSid(studentId);

        // 1. 调用上面的 getRadarData 确保拿到数据（含自动补全逻辑）
        Map<String, Object> radarData = getRadarData(realSid, courseId);
        List<Map<String, Object>> indicators = (List<Map<String, Object>>) radarData.get("indicator");
        List<Double> scores = (List<Double>) radarData.get("data");

        if (indicators == null || indicators.isEmpty()) {
            return "【AI 导师温馨提示】系统暂未检测到您在该课程下的知识点得分明细。请联系任课教师完成评分后再查看诊断。";
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("indicators", indicators);
            requestBody.put("scores", scores);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String pythonApiUrl = "http://127.0.0.1:8000/api/diagnose";
            ResponseEntity<Map> response = restTemplate.postForEntity(pythonApiUrl, entity, Map.class);

            Map<String, Object> body = response.getBody();
            if (body != null && (Integer) body.get("code") == 200) {
                return (String) body.get("data");
            }
            return "AI 导师正在思考中，请稍后再试。";
        } catch (Exception e) {
            return "【系统提示】AI 诊断模块暂时离线（Python 服务未启动）。根据您的成绩建议：请重点复习得分率较低的环节。";
        }
    }

    // ================== 5. 生成班级走势报告 ==================
    @Override
    public String generateClassAiReport(Integer classId, Integer courseId) {
        try {
            QueryWrapper<Scoreinfo> wrapper = new QueryWrapper<>();
            wrapper.eq("scClassId", classId).eq("scCourseId", courseId).isNotNull("scScore");
            List<Scoreinfo> scoreList = scoreinfoMapper.selectList(wrapper);

            if (scoreList == null || scoreList.isEmpty()) {
                return "该班级尚未录入任何成绩数据，无法进行走势预测。";
            }

            List<Double> scores = new ArrayList<>();
            for (Scoreinfo sc : scoreList) scores.add(sc.getScscore().doubleValue());

            List<KnowledgePoint> kpList = knowledgePointMapper.selectList(
                    new QueryWrapper<KnowledgePoint>().eq("course_id", courseId));
            List<String> kpNames = new ArrayList<>();
            for (KnowledgePoint kp : kpList) kpNames.add(kp.getPointName());

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> body = new HashMap<>();
            body.put("class_name", "当前选中班级");
            body.put("course_name", "当前选中课程");
            body.put("scores", scores);
            body.put("knowledge_points", kpNames);

            String pythonUrl = "http://127.0.0.1:8000/api/class_diagnose";
            ResponseEntity<Map> res = restTemplate.postForEntity(pythonUrl, new HttpEntity<>(body), Map.class);

            if (res.getBody() != null && (Integer) res.getBody().get("code") == 200) {
                return (String) res.getBody().get("data");
            }
            return "班级 AI 分析失败。";
        } catch (Exception e) {
            return "无法连接到班级分析引擎。";
        }
    }
}