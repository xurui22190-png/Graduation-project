package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.mapper.AcademicWarningMapper;
import com.demo.mapper.NoticeinfoMapper;
import com.demo.mapper.ScoreinfoMapper;
import com.demo.mapper.StudentinfoMapper;
import com.demo.model.AcademicWarning;
import com.demo.model.Noticeinfo;
import com.demo.model.Scoreinfo;
import com.demo.model.Studentinfo;
import com.demo.service.IWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class WarningServiceImpl implements IWarningService {

    @Autowired
    private AcademicWarningMapper warningMapper;

    @Autowired
    private NoticeinfoMapper noticeinfoMapper;

    @Autowired
    private ScoreinfoMapper scoreinfoMapper;

    @Autowired
    private StudentinfoMapper studentinfoMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scanAndGenerateWarnings(Integer courseId) {
        QueryWrapper<Scoreinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("scCourseId", courseId).isNotNull("scScore");
        List<Scoreinfo> scores = scoreinfoMapper.selectList(queryWrapper);

        for (Scoreinfo score : scores) {
            double finalScore = score.getScscore().doubleValue();
            Random random = new Random(score.getScid());

            double homeworkAvg = getScoreOrDefault(
                    score.getScRegular(),
                    Math.min(100, Math.max(0, finalScore + (random.nextDouble() * 20 - 10)))
            );
            double attendanceRate = Math.min(1.0, Math.max(0.0, (finalScore / 100.0) + (random.nextDouble() * 0.3)));
            double midtermScore = getScoreOrDefault(
                    score.getScTest(),
                    Math.min(100, Math.max(0, finalScore + (random.nextDouble() * 30 - 15)))
            );

            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("homework_avg", homeworkAvg);
            reqBody.put("attendance_rate", attendanceRate);
            reqBody.put("midterm_score", midtermScore);

            RiskResult riskResult = predictRisk(reqBody, homeworkAvg, attendanceRate, midtermScore);
            saveWarningIfNeeded(score.getScstudentid(), courseId, riskResult.probability, riskResult.riskLevel);
        }
    }

    private double getScoreOrDefault(BigDecimal value, double defaultValue) {
        return value == null ? defaultValue : value.doubleValue();
    }

    private RiskResult predictRisk(Map<String, Object> reqBody, double homeworkAvg, double attendanceRate, double midtermScore) {
        try {
            String pythonUrl = "http://127.0.0.1:8000/api/ai/predict_risk";
            ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl, reqBody, Map.class);
            Map<String, Object> result = response.getBody();

            if (isSuccessResult(result)) {
                Double probability = parseProbability(result);
                if (probability == null) {
                    probability = calculateRiskProbability(homeworkAvg, attendanceRate, midtermScore);
                }

                String riskLevel = (String) result.get("risk_level");
                if (riskLevel == null || riskLevel.trim().isEmpty()) {
                    riskLevel = getRiskLevel(probability);
                }

                return new RiskResult(probability, riskLevel);
            }

            System.err.println("Python预警接口返回异常，使用本地算法兜底：" + result);
        } catch (Exception e) {
            System.err.println("调用 Python AI 预警模型失败，使用本地风险算法兜底：" + e.getMessage());
        }

        double probability = calculateRiskProbability(homeworkAvg, attendanceRate, midtermScore);
        return new RiskResult(probability, getRiskLevel(probability));
    }

    private boolean isSuccessResult(Map<String, Object> result) {
        return result != null && "200".equals(String.valueOf(result.get("code")));
    }

    private Double parseProbability(Map<String, Object> result) {
        Object value = result.get("probability");
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        if (value == null) {
            value = result.get("fail_probability");
        }
        if (value == null) {
            return null;
        }

        try {
            String text = String.valueOf(value).trim();
            if (text.endsWith("%")) {
                return Double.parseDouble(text.substring(0, text.length() - 1)) / 100.0;
            }
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private double calculateRiskProbability(double homeworkAvg, double attendanceRate, double midtermScore) {
        double baseScore = homeworkAvg * 0.3 + attendanceRate * 100 * 0.2 + midtermScore * 0.5;
        double exponent = (baseScore - 55) * 0.2;
        if (exponent > 100) {
            return 0.0;
        }
        if (exponent < -100) {
            return 1.0;
        }
        return 1.0 / (1.0 + Math.exp(exponent));
    }

    private String getRiskLevel(double probability) {
        if (probability > 0.7) {
            return "高危预警";
        }
        if (probability > 0.4) {
            return "中风险";
        }
        return "低风险";
    }

    private void saveWarningIfNeeded(Integer studentId, Integer courseId, double probability, String riskLevel) {
        if (probability <= 0.1) {
            return;
        }

        QueryWrapper<AcademicWarning> existWrapper = new QueryWrapper<>();
        existWrapper.eq("wstudentid", studentId)
                .eq("wcourseid", courseId)
                .in("wstatus", Arrays.asList(0, 1));

        if (warningMapper.selectCount(existWrapper) > 0) {
            return;
        }

        AcademicWarning warning = new AcademicWarning();
        warning.setWstudentid(studentId);
        warning.setWcourseid(courseId);
        warning.setWprobability(probability);
        warning.setWrisklevel(riskLevel);
        warning.setWreason("系统综合评估该生平时表现、阶段测试和课程成绩，计算出综合学业风险指数为 "
                + Math.round(probability * 100) + "%，建议辅导员或导师重点关注并及时干预。");
        warning.setWstatus(0);
        warning.setWcreatedate(new Date());
        warningMapper.insert(warning);
    }

    @Override
    public List<Map<String, Object>> getWarningList() {
        List<AcademicWarning> warnings = warningMapper.selectList(
                new QueryWrapper<AcademicWarning>().orderByDesc("wcreatedate")
        );
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (AcademicWarning w : warnings) {
            Map<String, Object> map = new HashMap<>();
            map.put("wid", w.getWid());
            map.put("wstudentid", w.getWstudentid());
            map.put("studentId", w.getWstudentid());
            map.put("riskLevel", w.getWrisklevel());
            map.put("probability", w.getWprobability());
            map.put("reason", w.getWreason());
            map.put("status", w.getWstatus());
            map.put("createDate", w.getWcreatedate());

            Studentinfo student = studentinfoMapper.selectById(w.getWstudentid());
            if (student != null) {
                map.put("studentName", student.getSname());
                map.put("studentNo", student.getSno());
            }

            map.put("courseId", w.getWcourseid());
            resultList.add(map);
        }
        return resultList;
    }

    @Override
    public void updateWarningStatus(Integer warningId, Integer status) {
        AcademicWarning warning = new AcademicWarning();
        warning.setWid(warningId);
        warning.setWstatus(status);
        warningMapper.updateById(warning);
    }

    @Override
    public void sendWarningNotice(Integer studentId, String title, String content) {
        Noticeinfo notice = new Noticeinfo();
        notice.setNtitle("【专属推送】" + title);
        notice.setNcontent(content);
        notice.setNcreatetime(new Date());
        notice.setNtype("学业预警");
        notice.setNstate(1);
        notice.setNtop(1);
        notice.setNisdelete(0);
        notice.setNcreatename("AI学业指导中心");
        notice.setNcreateuid(0);
        noticeinfoMapper.insert(notice);
    }

    private static class RiskResult {
        private final double probability;
        private final String riskLevel;

        private RiskResult(double probability, String riskLevel) {
            this.probability = probability;
            this.riskLevel = riskLevel;
        }
    }
}
