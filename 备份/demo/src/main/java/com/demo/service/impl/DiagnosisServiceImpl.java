package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.mapper.*;
import com.demo.model.*;
import com.demo.service.IDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import java.util.*;

@Service
public class DiagnosisServiceImpl implements IDiagnosisService {

    @Autowired
    private KnowledgePointMapper knowledgePointMapper;
    @Autowired
    private ScoreDetailMapper scoreDetailMapper;
    @Autowired
    private StudentinfoMapper studentinfoMapper;

    private final RestTemplate restTemplate;
    private final String AI_SERVICE_URL = "http://localhost:8000/api/ai/career_advice";

    public DiagnosisServiceImpl() {
        // 设置超长超时，因为本地 Ollama 生成较慢
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(90000); // 90秒，确保 Qwen 能说完
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public String generateAiReport(Integer studentId, Integer courseId, String intent) {
        try {
            // 1. 获取学生信息 (用于 Prompt，但不作为 AI 联想的依据)
            Studentinfo student = studentinfoMapper.selectById(studentId);
            String name = (student != null) ? student.getSname() : "该学生";

            // 2. 获取真实的薄弱知识点 (由规则引擎计算)
            List<String> weakList = diagnoseWeakPoints(studentId, courseId);
            String weaknessDesc = weakList.isEmpty() ? "各项知识点掌握均衡，表现优异。" : String.join("、", weakList);

            // 3. 构造发送给 Python 的数据 (添加“去名人化”指令)
            Map<String, Object> pythonBody = new HashMap<>();
            pythonBody.put("student_name", name);
            pythonBody.put("intent", intent); // 🌟 接收前端传来的真实意向
            pythonBody.put("weaknesses", weaknessDesc);

            // 补充 6 维分数上下文 (暂时模拟，可改为动态计算)
            Map<String, Integer> scoreMap = new HashMap<>();
            scoreMap.put("核心力", 85); scoreMap.put("实践力", 90); scoreMap.put("逻辑力", 60);
            pythonBody.put("scores", scoreMap);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(pythonBody, headers);

            // 4. 发送请求
            ResponseEntity<Map> response = restTemplate.postForEntity(AI_SERVICE_URL, entity, Map.class);

            if (response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Object code = body.get("code");
                // 兼容数字和字符串类型的 200
                if (code != null && String.valueOf(code).equals("200")) {
                    String result = (String) body.get("data");
                    System.out.println("AI 建议: " + result);
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "本地 AI 导师正在阅卷，请稍后刷新重试。";
        }
        return "AI 诊断解析失败。";
    }

    @Override
    public List<String> diagnoseWeakPoints(Integer studentId, Integer courseId) {
        List<String> weakPoints = new ArrayList<>();
        QueryWrapper<ScoreDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId).eq("course_id", courseId);
        List<ScoreDetail> details = scoreDetailMapper.selectList(wrapper);
        for (ScoreDetail d : details) {
            if (d.getActualScore() != null && d.getMaxScore() != null && (d.getActualScore() / d.getMaxScore() < 0.6)) {
                KnowledgePoint kp = knowledgePointMapper.selectById(d.getPointId());
                if (kp != null) weakPoints.add(kp.getPointName());
            }
        }
        return weakPoints;
    }

    @Override
    @Transactional
    public void generateSimulatedDetails(Integer studentId, Integer courseId, Double totalScore) {
        QueryWrapper<ScoreDetail> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("student_id", studentId).eq("course_id", courseId);
        scoreDetailMapper.delete(deleteWrapper);
        QueryWrapper<KnowledgePoint> kpWrapper = new QueryWrapper<>();
        kpWrapper.eq("courseId", courseId);
        List<KnowledgePoint> kpList = knowledgePointMapper.selectList(kpWrapper);
        if (kpList == null || kpList.isEmpty()) return;
        Random random = new Random();
        double base = totalScore / 100.0;
        for (KnowledgePoint kp : kpList) {
            ScoreDetail detail = new ScoreDetail();
            detail.setStudentId(studentId);
            detail.setCourseId(courseId);
            detail.setPointId(kp.getId());
            detail.setMaxScore(100.0);
            double score = (base * 100) + (random.nextDouble() * 30 - 15);
            detail.setActualScore(Math.max(0, Math.min(100, Math.round(score * 10) / 10.0)));
            detail.setCreateTime(new Date());
            scoreDetailMapper.insert(detail);
        }
    }

    @Override
    public Map<String, Object> getRadarData(Integer studentId, Integer courseId) {
        Map<String, Object> res = new HashMap<>();
        List<String> indicators = new ArrayList<>();
        List<Double> scores = new ArrayList<>();
        QueryWrapper<ScoreDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId).eq("course_id", courseId);
        List<ScoreDetail> list = scoreDetailMapper.selectList(wrapper);
        for (ScoreDetail d : list) {
            KnowledgePoint kp = knowledgePointMapper.selectById(d.getPointId());
            if (kp != null) {
                indicators.add(kp.getPointName());
                scores.add(d.getActualScore());
            }
        }
        res.put("indicators", indicators);
        res.put("data", scores);
        return res;
    }

    @Override public String generateClassAiReport(Integer classId, Integer courseId) { return "班级报告生成中..."; }
    @Override public Map<String, Object> getClassAnalysisData(Integer classId, Integer courseId) { return new HashMap<>(); }
}