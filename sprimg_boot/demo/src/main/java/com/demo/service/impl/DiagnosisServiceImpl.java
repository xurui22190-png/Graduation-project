package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.dto.ScoreWeaknessReportDto;
import com.demo.dto.StudentPlanningReportDto;
import com.demo.mapper.ClassinfoMapper;
import com.demo.mapper.CourseinfoMapper;
import com.demo.mapper.KnowledgePointMapper;
import com.demo.mapper.ScoreDetailMapper;
import com.demo.mapper.ScoreinfoMapper;
import com.demo.model.Classinfo;
import com.demo.mapper.StudentinfoMapper;
import com.demo.model.Courseinfo;
import com.demo.model.KnowledgePoint;
import com.demo.model.ScoreDetail;
import com.demo.model.Scoreinfo;
import com.demo.model.Studentinfo;
import com.demo.service.IDiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

@Service
public class DiagnosisServiceImpl implements IDiagnosisService {

    @Autowired
    private KnowledgePointMapper knowledgePointMapper;

    @Autowired
    private ScoreDetailMapper scoreDetailMapper;

    @Autowired
    private StudentinfoMapper studentinfoMapper;

    @Autowired
    private CourseinfoMapper courseinfoMapper;

    @Autowired
    private ClassinfoMapper classinfoMapper;

    @Autowired
    private ScoreinfoMapper scoreinfoMapper;

    @Value("${ai.gateway.url:http://127.0.0.1:8000/api}")
    private String aiGatewayUrl;

    private final RestTemplate restTemplate;

    public DiagnosisServiceImpl() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(90000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Override
    public String generateAiReport(Integer studentId, Integer courseId, String intent) {
        Studentinfo student = studentinfoMapper.selectById(studentId);
        Courseinfo course = courseinfoMapper.selectById(courseId);

        String studentName = safeText(student == null ? null : student.getSname(), "该生");
        String studentNo = safeText(student == null ? null : student.getSno(), "");
        String courseName = safeText(course == null ? null : course.getCrname(), "当前课程");

        List<PointAnalysis> points = buildPointAnalyses(studentId, courseId);
        String fallbackReport = buildLocalProfessionalReport(studentName, courseName, intent, points);

        if (points.isEmpty()) {
            return fallbackReport;
        }

        try {
            Map<String, Object> requestBody = new LinkedHashMap<>();
            String normalizedIntent = normalizeIntent(intent);
            requestBody.put("student_name", studentName);
            requestBody.put("student_no", studentNo);
            requestBody.put("course_name", courseName);
            requestBody.put("intent", normalizedIntent);
            requestBody.put("intent_label", intentLabel(normalizedIntent));
            requestBody.put("intent_focus", intentFocus(normalizedIntent));
            requestBody.put("overall_rate", roundOne(averageRate(points)));
            requestBody.put("weaknesses", buildWeaknessDesc(points));
            requestBody.put("strengths", buildStrengthDesc(points));
            requestBody.put("scores", buildScoreMap(points));
            requestBody.put("details", buildDetailList(points));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiGatewayUrl + "/career_advice",
                    new HttpEntity<>(requestBody, headers),
                    Map.class
            );

            if (response.getBody() != null
                    && "200".equals(String.valueOf(response.getBody().get("code")))) {
                Object data = response.getBody().get("data");
                String aiReport = data == null ? "" : data.toString();
                return isUsableAiReport(aiReport) ? cleanReport(aiReport) : fallbackReport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fallbackReport;
    }

    @Override
    public SseEmitter streamAiReport(Integer studentId, Integer courseId, String intent) {
        SseEmitter emitter = new SseEmitter(60000L);
        new Thread(() -> {
            Studentinfo student = studentinfoMapper.selectById(studentId);
            Courseinfo course = courseinfoMapper.selectById(courseId);

            String studentName = safeText(student == null ? null : student.getSname(), "该生");
            String studentNo = safeText(student == null ? null : student.getSno(), "");
            String courseName = safeText(course == null ? null : course.getCrname(), "当前课程");
            List<PointAnalysis> points = buildPointAnalyses(studentId, courseId);
            String fallbackReport = buildLocalProfessionalReport(studentName, courseName, intent, points);

            if (points.isEmpty()) {
                sendSseEvent(emitter, "diagnosis", fallbackReport);
                sendSseEvent(emitter, "done", "true");
                emitter.complete();
                return;
            }

            try {
                Map<String, Object> requestBody = new LinkedHashMap<>();
                String normalizedIntent = normalizeIntent(intent);
                requestBody.put("student_name", studentName);
                requestBody.put("student_no", studentNo);
                requestBody.put("course_name", courseName);
                requestBody.put("intent", normalizedIntent);
                requestBody.put("intent_label", intentLabel(normalizedIntent));
                requestBody.put("intent_focus", intentFocus(normalizedIntent));
                requestBody.put("overall_rate", roundOne(averageRate(points)));
                requestBody.put("weaknesses", buildWeaknessDesc(points));
                requestBody.put("strengths", buildStrengthDesc(points));
                requestBody.put("scores", buildScoreMap(points));
                requestBody.put("details", buildDetailList(points));

                boolean streamed = streamPythonAiReport(emitter, aiGatewayUrl + "/career_advice_stream", toJson(requestBody));
                if (!streamed) {
                    sendSseEvent(emitter, "diagnosis", fallbackReport);
                }
                sendSseEvent(emitter, "done", "true");
                emitter.complete();
            } catch (Exception e) {
                try {
                    sendSseEvent(emitter, "diagnosis", fallbackReport);
                    sendSseEvent(emitter, "done", "true");
                    emitter.complete();
                } catch (Exception ignored) {
                    emitter.completeWithError(e);
                }
            }
        }, "ai-report-sse").start();

        return emitter;
    }

    @Override
    public String generatePlanningReport(StudentPlanningReportDto dto) {
        if (dto == null) {
            return "【规划概览】暂无可分析的学生画像数据。";
        }

        String normalizedIntent = normalizeIntent(dto.getIntent());
        String studentName = safeText(dto.getStudentName(), "该生");
        String studentNo = safeText(dto.getStudentNo(), "");

        Map<String, Double> dimensions = dto.getDimensions() == null
                ? new LinkedHashMap<String, Double>()
                : dto.getDimensions();
        StudentPlanningReportDto.ScoreSummary summary = dto.getScoreSummary() == null
                ? new StudentPlanningReportDto.ScoreSummary()
                : dto.getScoreSummary();
        List<StudentPlanningReportDto.CourseScore> courses = dto.getCourses() == null
                ? new ArrayList<StudentPlanningReportDto.CourseScore>()
                : dto.getCourses();

        String fallbackReport = buildPlanningFallbackReport(studentName, normalizedIntent, dimensions, summary, courses);

        try {
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("student_name", studentName);
            requestBody.put("student_no", studentNo);
            requestBody.put("intent", normalizedIntent);
            requestBody.put("intent_label", intentLabel(normalizedIntent));
            requestBody.put("intent_focus", intentFocus(normalizedIntent));
            requestBody.put("dimensions", dimensions);
            requestBody.put("score_summary", buildPlanningSummaryMap(summary));
            requestBody.put("courses", buildPlanningCourseList(courses));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiGatewayUrl + "/planning_report",
                    new HttpEntity<>(requestBody, headers),
                    Map.class
            );

            if (response.getBody() != null
                    && "200".equals(String.valueOf(response.getBody().get("code")))) {
                Object data = response.getBody().get("data");
                String aiReport = data == null ? "" : data.toString();
                return isUsablePlanningReport(aiReport) ? cleanReport(aiReport) : fallbackReport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fallbackReport;
    }

    @Override
    public String generateScoreWeaknessReport(ScoreWeaknessReportDto dto) {
        if (dto == null || dto.getCourses() == null || dto.getCourses().isEmpty()) {
            return "【成绩概览】暂无可用于弱项诊断的成绩数据。";
        }

        String fallbackReport = buildScoreWeaknessFallbackReport(dto);

        try {
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("student_name", safeText(dto.getStudentName(), "该生"));
            requestBody.put("student_no", safeText(dto.getStudentNo(), ""));
            requestBody.put("avg_score", safeDouble(dto.getAvgScore()));
            requestBody.put("pass_rate", safeDouble(dto.getPassRate()));
            requestBody.put("course_count", dto.getCourseCount() == null ? dto.getCourses().size() : dto.getCourseCount());
            requestBody.put("max_score", safeDouble(dto.getMaxScore()));
            requestBody.put("min_score", safeDouble(dto.getMinScore()));
            requestBody.put("courses", buildScoreWeaknessCourseList(dto.getCourses()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiGatewayUrl + "/score_weakness",
                    new HttpEntity<>(requestBody, headers),
                    Map.class
            );

            if (response.getBody() != null
                    && "200".equals(String.valueOf(response.getBody().get("code")))) {
                Object data = response.getBody().get("data");
                String aiReport = data == null ? "" : data.toString();
                return isUsableScoreWeaknessReport(aiReport) ? cleanReport(aiReport) : fallbackReport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fallbackReport;
    }

    @Override
    public List<String> diagnoseWeakPoints(Integer studentId, Integer courseId) {
        List<String> weakPoints = new ArrayList<>();
        for (PointAnalysis point : buildPointAnalyses(studentId, courseId)) {
            if (point.rate < 60.0) {
                weakPoints.add(point.name);
            }
        }
        return weakPoints;
    }

    @Override
    public String generateCourseAnalysis(Integer studentId, Integer courseId) {
        Studentinfo student = studentinfoMapper.selectById(studentId);
        Courseinfo course = courseinfoMapper.selectById(courseId);

        String studentName = safeText(student == null ? null : student.getSname(), "该生");
        String courseName = safeText(course == null ? null : course.getCrname(), "当前课程");
        List<PointAnalysis> points = buildPointAnalyses(studentId, courseId);
        String fallbackReport = buildCourseAnalysisReport(studentName, courseName, points);

        if (points.isEmpty()) {
            return fallbackReport;
        }

        try {
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("student_name", studentName);
            requestBody.put("course_name", courseName);
            requestBody.put("indicators", buildPointNames(points));
            requestBody.put("scores", buildPointRates(points));
            requestBody.put("details", buildDetailList(points));
            requestBody.put("overall_rate", roundOne(averageRate(points)));
            requestBody.put("weaknesses", buildWeaknessDesc(points));
            requestBody.put("strengths", buildStrengthDesc(points));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiGatewayUrl + "/diagnose",
                    new HttpEntity<>(requestBody, headers),
                    Map.class
            );

            if (response.getBody() != null
                    && "200".equals(String.valueOf(response.getBody().get("code")))) {
                Object data = response.getBody().get("data");
                String aiReport = data == null ? "" : data.toString();
                return isUsableCourseAnalysis(aiReport) ? cleanReport(aiReport) : fallbackReport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fallbackReport;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSimulatedDetails(Integer studentId, Integer courseId, Double totalScore) {
        QueryWrapper<ScoreDetail> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("student_id", studentId).eq("course_id", courseId);
        scoreDetailMapper.delete(deleteWrapper);

        List<KnowledgePoint> kpList = knowledgePointMapper.selectList(
                new QueryWrapper<KnowledgePoint>().lambda().eq(KnowledgePoint::getCourseId, courseId)
        );

        if (kpList == null || kpList.isEmpty()) {
            return;
        }

        double baseRate = totalScore == null ? 0.0 : totalScore / 100.0;
        Random random = new Random();
        List<ScoreDetail> detailList = new ArrayList<>();
        double simulatedTotal = 0.0;

        for (KnowledgePoint kp : kpList) {
            double kpWeight = 1.0 / kpList.size();
            double fluctuation = Math.max(-0.15, Math.min(0.15, random.nextGaussian() * 0.05));
            double actualRate = Math.max(0.0, Math.min(1.0, baseRate + fluctuation));
            double maxScore = roundOne(100.0 * kpWeight);
            double pointScore = roundOne(actualRate * 100.0 * kpWeight);

            simulatedTotal += pointScore;

            ScoreDetail detail = new ScoreDetail();
            detail.setStudentId(studentId);
            detail.setCourseId(courseId);
            detail.setPointId(kp.getId());
            detail.setMaxScore(maxScore);
            detail.setActualScore(pointScore);
            detail.setCreateTime(new Date());
            detailList.add(detail);
        }

        double residual = roundOne((totalScore == null ? 0.0 : totalScore) - simulatedTotal);
        if (residual != 0.0 && !detailList.isEmpty()) {
            for (ScoreDetail detail : detailList) {
                double adjusted = detail.getActualScore() + residual;
                if (adjusted >= 0 && adjusted <= detail.getMaxScore()) {
                    detail.setActualScore(roundOne(adjusted));
                    break;
                }
            }
        }

        for (ScoreDetail detail : detailList) {
            scoreDetailMapper.insert(detail);
        }
    }

    @Override
    public Map<String, Object> getRadarData(Integer studentId, Integer courseId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> indicators = new ArrayList<>();
        List<Double> scores = new ArrayList<>();

        for (PointAnalysis point : buildPointAnalyses(studentId, courseId)) {
            Map<String, Object> indicator = new HashMap<>();
            indicator.put("name", point.name);
            indicator.put("max", 100);
            indicator.put("actualScore", point.actualScore);
            indicator.put("maxScore", point.maxScore);
            indicators.add(indicator);
            scores.add(point.rate);
        }

        result.put("indicators", indicators);
        result.put("data", scores);
        return result;
    }

    @Override
    public String generateClassAiReport(Integer classId, Integer courseId) {
        ClassAnalysisSummary summary = buildClassAnalysisSummary(classId, courseId);
        String fallbackReport = buildClassFallbackReport(summary);

        if (summary.pointStats.isEmpty() && summary.studentCount == 0) {
            return fallbackReport;
        }

        try {
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("class_name", summary.className);
            requestBody.put("course_name", summary.courseName);
            requestBody.put("student_count", summary.studentCount);
            requestBody.put("avg_score", summary.avgScore);
            requestBody.put("max_score", summary.maxScore);
            requestBody.put("min_score", summary.minScore);
            requestBody.put("pass_rate", summary.passRate);
            requestBody.put("excellent_rate", summary.excellentRate);
            requestBody.put("standard_deviation", summary.standardDeviation);
            requestBody.put("score_ranges", summary.scoreRanges);
            requestBody.put("weakest_point", summary.weakestPointText);
            requestBody.put("strongest_point", summary.strongestPointText);
            requestBody.put("point_stats", buildClassPointStatList(summary.pointStats));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiGatewayUrl + "/class_report",
                    new HttpEntity<>(requestBody, headers),
                    Map.class
            );

            if (response.getBody() != null
                    && "200".equals(String.valueOf(response.getBody().get("code")))) {
                Object data = response.getBody().get("data");
                String aiReport = data == null ? "" : data.toString();
                return isUsableClassReport(aiReport) ? cleanReport(aiReport) : fallbackReport;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fallbackReport;
    }

    @Override
    public Map<String, Object> getClassAnalysisData(Integer classId, Integer courseId) {
        ClassAnalysisSummary summary = buildClassAnalysisSummary(classId, courseId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("className", summary.className);
        result.put("courseName", summary.courseName);
        result.put("studentCount", summary.studentCount);
        result.put("avgScore", summary.avgScore);
        result.put("maxScore", summary.maxScore);
        result.put("minScore", summary.minScore);
        result.put("passRate", summary.passRate);
        result.put("excellentRate", summary.excellentRate);
        result.put("standardDeviation", summary.standardDeviation);
        result.put("weakestPoint", summary.weakestPointText);
        result.put("strongestPoint", summary.strongestPointText);
        result.put("scoreRanges", summary.scoreRanges);
        result.put("pointStats", buildClassPointStatList(summary.pointStats));
        return result;
    }

    private ClassAnalysisSummary buildClassAnalysisSummary(Integer classId, Integer courseId) {
        ClassAnalysisSummary summary = new ClassAnalysisSummary();

        Classinfo classInfo = classId == null ? null : classinfoMapper.selectById(classId);
        Courseinfo course = courseId == null ? null : courseinfoMapper.selectById(courseId);
        summary.className = safeText(classInfo == null ? null : classInfo.getCname(), "当前班级");
        summary.courseName = safeText(course == null ? null : course.getCrname(), "当前课程");
        summary.scoreRanges = buildEmptyScoreRanges();

        if (classId == null || courseId == null) {
            summary.weakestPointText = "请选择班级课程";
            summary.strongestPointText = "请选择班级课程";
            return summary;
        }

        List<Scoreinfo> scores = scoreinfoMapper.selectList(
                new QueryWrapper<Scoreinfo>()
                        .eq("scClassId", classId)
                        .eq("scCourseId", courseId)
                        .eq("scStatus", 1)
                        .isNotNull("scScore")
        );

        List<Integer> studentIds = new ArrayList<>();
        double total = 0.0;
        double max = 0.0;
        double min = 0.0;
        int passCount = 0;
        int excellentCount = 0;

        for (Scoreinfo score : scores) {
            if (score.getScstudentid() != null) {
                studentIds.add(score.getScstudentid());
            }

            double value = score.getScscore() == null ? 0.0 : score.getScscore().doubleValue();
            if (summary.studentCount == 0) {
                max = value;
                min = value;
            } else {
                max = Math.max(max, value);
                min = Math.min(min, value);
            }

            total += value;
            summary.studentCount++;
            if (value >= 60.0) {
                passCount++;
            }
            if (value >= 85.0) {
                excellentCount++;
            }
            String rangeName = scoreRangeName(value);
            summary.scoreRanges.put(rangeName, summary.scoreRanges.get(rangeName) + 1);
        }

        if (summary.studentCount > 0) {
            summary.avgScore = roundOne(total / summary.studentCount);
            summary.maxScore = roundOne(max);
            summary.minScore = roundOne(min);
            summary.passRate = roundOne(passCount * 100.0 / summary.studentCount);
            summary.excellentRate = roundOne(excellentCount * 100.0 / summary.studentCount);

            double varianceTotal = 0.0;
            for (Scoreinfo score : scores) {
                double value = score.getScscore() == null ? 0.0 : score.getScscore().doubleValue();
                varianceTotal += Math.pow(value - summary.avgScore, 2);
            }
            summary.standardDeviation = roundOne(Math.sqrt(varianceTotal / summary.studentCount));
        }

        summary.pointStats = buildClassPointStats(studentIds, courseId);
        if (summary.pointStats.isEmpty()) {
            summary.weakestPointText = "暂无知识点明细";
            summary.strongestPointText = "暂无知识点明细";
        } else {
            summary.weakestPointText = summary.pointStats.get(0).name + "("
                    + formatRate(summary.pointStats.get(0).avgRate) + "%)";
            ClassPointStat strongest = summary.pointStats.get(summary.pointStats.size() - 1);
            summary.strongestPointText = strongest.name + "(" + formatRate(strongest.avgRate) + "%)";
        }

        return summary;
    }

    private List<ClassPointStat> buildClassPointStats(List<Integer> studentIds, Integer courseId) {
        List<ClassPointStat> result = new ArrayList<>();
        if (studentIds == null || studentIds.isEmpty() || courseId == null) {
            return result;
        }

        List<ScoreDetail> details = scoreDetailMapper.selectList(
                new QueryWrapper<ScoreDetail>()
                        .eq("course_id", courseId)
                        .in("student_id", studentIds)
        );

        Map<Integer, ClassPointStat> statMap = new LinkedHashMap<>();
        for (ScoreDetail detail : details) {
            if (detail.getPointId() == null) {
                continue;
            }

            ClassPointStat stat = statMap.get(detail.getPointId());
            if (stat == null) {
                KnowledgePoint kp = knowledgePointMapper.selectById(detail.getPointId());
                stat = new ClassPointStat(safeText(kp == null ? null : kp.getPointName(), "未命名知识点"));
                statMap.put(detail.getPointId(), stat);
            }

            stat.studentCount++;
            stat.totalActual += detail.getActualScore() == null ? 0.0 : detail.getActualScore();
            stat.totalMax += detail.getMaxScore() == null ? 0.0 : detail.getMaxScore();
        }

        for (ClassPointStat stat : statMap.values()) {
            stat.avgActualScore = stat.studentCount == 0 ? 0.0 : roundOne(stat.totalActual / stat.studentCount);
            stat.avgMaxScore = stat.studentCount == 0 ? 0.0 : roundOne(stat.totalMax / stat.studentCount);
            double rate = stat.totalMax > 0 ? stat.totalActual / stat.totalMax * 100.0 : 0.0;
            stat.avgRate = roundOne(Math.max(0.0, Math.min(100.0, rate)));
            result.add(stat);
        }

        result.sort(Comparator.comparingDouble(point -> point.avgRate));
        return result;
    }

    private Map<String, Integer> buildEmptyScoreRanges() {
        Map<String, Integer> ranges = new LinkedHashMap<>();
        ranges.put("不及格", 0);
        ranges.put("60-69", 0);
        ranges.put("70-79", 0);
        ranges.put("80-89", 0);
        ranges.put("90-100", 0);
        return ranges;
    }

    private String scoreRangeName(double score) {
        if (score < 60.0) {
            return "不及格";
        }
        if (score < 70.0) {
            return "60-69";
        }
        if (score < 80.0) {
            return "70-79";
        }
        if (score < 90.0) {
            return "80-89";
        }
        return "90-100";
    }

    private List<Map<String, Object>> buildClassPointStatList(List<ClassPointStat> stats) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ClassPointStat stat : stats) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", stat.name);
            item.put("avgRate", stat.avgRate);
            item.put("avgActualScore", stat.avgActualScore);
            item.put("avgMaxScore", stat.avgMaxScore);
            item.put("studentCount", stat.studentCount);
            item.put("level", levelText(stat.avgRate));
            list.add(item);
        }
        return list;
    }

    private boolean streamPythonAiReport(SseEmitter emitter, String url, String jsonBody) {
        boolean hasChunk = false;
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(90000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "text/event-stream");

            try (OutputStream output = connection.getOutputStream()) {
                output.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }

            int status = connection.getResponseCode();
            InputStream stream = status >= 200 && status < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            if (status < 200 || status >= 300 || stream == null) {
                return false;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("data:")) {
                        continue;
                    }

                    String payload = line.substring(5).trim();
                    if (payload.isEmpty()) {
                        continue;
                    }

                    String chunk = parseJsonStringPayload(payload);
                    if (chunk == null || chunk.isEmpty()) {
                        continue;
                    }

                    if (!isAiErrorChunk(chunk)) {
                        hasChunk = true;
                    }
                    sendSseEvent(emitter, "diagnosis", chunk);
                }
            }
        } catch (Exception e) {
            return hasChunk;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return hasChunk;
    }

    private void sendSseEvent(SseEmitter emitter, String eventName, String data) {
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data == null ? "" : data));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isAiErrorChunk(String text) {
        return text.contains("连接本地 AI 失败")
                || text.contains("AI 服务异常")
                || text.toLowerCase(Locale.ROOT).contains("error");
    }

    private String parseJsonStringPayload(String payload) {
        String text = payload.trim();
        if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
            return unescapeJsonString(text.substring(1, text.length() - 1));
        }
        return text;
    }

    private String unescapeJsonString(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c != '\\' || i + 1 >= value.length()) {
                builder.append(c);
                continue;
            }

            char next = value.charAt(++i);
            switch (next) {
                case '"':
                    builder.append('"');
                    break;
                case '\\':
                    builder.append('\\');
                    break;
                case '/':
                    builder.append('/');
                    break;
                case 'b':
                    builder.append('\b');
                    break;
                case 'f':
                    builder.append('\f');
                    break;
                case 'n':
                    builder.append('\n');
                    break;
                case 'r':
                    builder.append('\r');
                    break;
                case 't':
                    builder.append('\t');
                    break;
                case 'u':
                    if (i + 4 < value.length()) {
                        String hex = value.substring(i + 1, i + 5);
                        try {
                            builder.append((char) Integer.parseInt(hex, 16));
                            i += 4;
                        } catch (NumberFormatException e) {
                            builder.append("\\u").append(hex);
                            i += 4;
                        }
                    } else {
                        builder.append("\\u");
                    }
                    break;
                default:
                    builder.append(next);
                    break;
            }
        }
        return builder.toString();
    }

    private String toJson(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + escapeJson((String) value) + "\"";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value instanceof Map) {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            boolean first = true;
            for (Object entryObject : ((Map<?, ?>) value).entrySet()) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entryObject;
                if (!first) {
                    builder.append(",");
                }
                builder.append(toJson(String.valueOf(entry.getKey())));
                builder.append(":");
                builder.append(toJson(entry.getValue()));
                first = false;
            }
            builder.append("}");
            return builder.toString();
        }
        if (value instanceof Iterable) {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            boolean first = true;
            for (Object item : (Iterable<?>) value) {
                if (!first) {
                    builder.append(",");
                }
                builder.append(toJson(item));
                first = false;
            }
            builder.append("]");
            return builder.toString();
        }
        return "\"" + escapeJson(String.valueOf(value)) + "\"";
    }

    private String escapeJson(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        builder.append(String.format(Locale.ROOT, "\\u%04x", (int) c));
                    } else {
                        builder.append(c);
                    }
                    break;
            }
        }
        return builder.toString();
    }

    private String buildClassFallbackReport(ClassAnalysisSummary summary) {
        if (summary.studentCount == 0 && summary.pointStats.isEmpty()) {
            return "【班级概览】" + summary.className + "《" + summary.courseName + "》暂无可用于 AI 教研分析的成绩数据。"
                    + "\n【短板判断】当前未发现已录入成绩或知识点明细，请先完成成绩录入并触发知识点拆解。"
                    + "\n【教学建议】建议先核对班级、课程筛选条件，再重新保存或导入该课程成绩，确保 scoreinfo 与 score_detail 均有记录。"
                    + "\n【跟踪建议】数据补齐后再生成班级报告，用于识别集体薄弱知识点和分层辅导对象。";
        }

        List<ClassPointStat> weakPoints = filterClassPointsBelow(summary.pointStats, 60.0);
        List<ClassPointStat> riskPoints = filterClassPointsBetween(summary.pointStats, 60.0, 75.0);
        List<ClassPointStat> strongPoints = filterClassPointsAtLeast(summary.pointStats, 85.0);

        StringBuilder report = new StringBuilder();
        report.append("【班级概览】")
                .append(summary.className)
                .append("在《")
                .append(summary.courseName)
                .append("》中已统计")
                .append(summary.studentCount)
                .append("名学生，平均分")
                .append(formatRate(summary.avgScore))
                .append("，及格率")
                .append(formatRate(summary.passRate))
                .append("%，优秀率")
                .append(formatRate(summary.excellentRate))
                .append("%，标准差")
                .append(formatRate(summary.standardDeviation))
                .append("。");

        if (!weakPoints.isEmpty()) {
            report.append("\n【集体短板】低于 60% 的知识点集中在")
                    .append(joinClassPointNames(weakPoints, 4))
                    .append("，说明这些内容需要作为下一轮复习课的优先讲解对象。");
        } else if (!riskPoints.isEmpty()) {
            report.append("\n【集体短板】暂无整体不及格知识点，但")
                    .append(joinClassPointNames(riskPoints, 4))
                    .append("处于临界区，后续测验中容易拉低班级均分。");
        } else if (!summary.pointStats.isEmpty()) {
            report.append("\n【集体短板】知识点整体达到基础要求，当前最弱项为")
                    .append(summary.weakestPointText)
                    .append("，更适合做巩固提升而非大面积补缺。");
        } else {
            report.append("\n【集体短板】当前缺少知识点明细，暂时只能根据总分判断班级整体水平。");
        }

        if (strongPoints.isEmpty()) {
            report.append("\n【优势表现】暂无特别突出的高掌握知识点，说明班级学习成果还需要通过专项训练形成稳定优势。");
        } else {
            report.append("\n【优势表现】")
                    .append(joinClassPointNames(strongPoints, 3))
                    .append("掌握较好，可作为综合题训练和同伴互助讲解的基础。");
        }

        report.append("\n【教学建议】");
        if (summary.standardDeviation >= 12.0) {
            report.append("班级内部分化较明显，建议采用分层任务：低分学生先补基础概念和例题，中高分学生完成综合迁移题，课堂讲评时重点复盘共性错因。");
        } else if (!weakPoints.isEmpty() || !riskPoints.isEmpty()) {
            report.append("建议围绕")
                    .append(summary.weakestPointText)
                    .append("设计 15 到 20 分钟微专题，先讲核心概念，再安排同类题即时训练，最后收集错误样例做二次讲评。");
        } else {
            report.append("班级整体较稳定，建议增加综合应用题和限时训练，把知识点掌握转化为完整解题能力，同时保留少量基础题防止遗忘。");
        }

        report.append("\n【跟踪建议】下一次作业或小测重点观察")
                .append(summary.weakestPointText)
                .append("的班级平均掌握率是否提升到 75% 以上；若低分段人数仍未下降，应安排一次针对性答疑或课后辅导。");

        return report.toString();
    }

    private List<ClassPointStat> filterClassPointsBelow(List<ClassPointStat> points, double maxExclusive) {
        List<ClassPointStat> result = new ArrayList<>();
        for (ClassPointStat point : points) {
            if (point.avgRate < maxExclusive) {
                result.add(point);
            }
        }
        result.sort(Comparator.comparingDouble(point -> point.avgRate));
        return result;
    }

    private List<ClassPointStat> filterClassPointsBetween(List<ClassPointStat> points,
                                                          double minInclusive,
                                                          double maxExclusive) {
        List<ClassPointStat> result = new ArrayList<>();
        for (ClassPointStat point : points) {
            if (point.avgRate >= minInclusive && point.avgRate < maxExclusive) {
                result.add(point);
            }
        }
        result.sort(Comparator.comparingDouble(point -> point.avgRate));
        return result;
    }

    private List<ClassPointStat> filterClassPointsAtLeast(List<ClassPointStat> points, double minInclusive) {
        List<ClassPointStat> result = new ArrayList<>();
        for (ClassPointStat point : points) {
            if (point.avgRate >= minInclusive) {
                result.add(point);
            }
        }
        result.sort((left, right) -> Double.compare(right.avgRate, left.avgRate));
        return result;
    }

    private String joinClassPointNames(List<ClassPointStat> points, int limit) {
        if (points.isEmpty()) {
            return "暂无";
        }

        StringBuilder builder = new StringBuilder();
        int count = Math.min(limit, points.size());
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                builder.append("、");
            }
            ClassPointStat point = points.get(i);
            builder.append(point.name)
                    .append("(")
                    .append(formatRate(point.avgRate))
                    .append("%)");
        }
        return builder.toString();
    }

    private String buildPlanningFallbackReport(String studentName,
                                               String intent,
                                               Map<String, Double> dimensions,
                                               StudentPlanningReportDto.ScoreSummary summary,
                                               List<StudentPlanningReportDto.CourseScore> courses) {
        List<Map.Entry<String, Double>> sortedDimensions = new ArrayList<>(dimensions.entrySet());
        sortedDimensions.sort((left, right) -> Double.compare(safeDouble(right.getValue()), safeDouble(left.getValue())));

        String strongest = sortedDimensions.isEmpty()
                ? "暂无明显优势维度"
                : sortedDimensions.get(0).getKey() + "(" + formatRate(safeDouble(sortedDimensions.get(0).getValue())) + ")";
        String weakest = sortedDimensions.isEmpty()
                ? "暂无明显短板维度"
                : sortedDimensions.get(sortedDimensions.size() - 1).getKey() + "(" + formatRate(safeDouble(sortedDimensions.get(sortedDimensions.size() - 1).getValue())) + ")";

        double avgScore = safeDouble(summary.getAvgScore());
        double passRate = safeDouble(summary.getPassRate());
        int courseCount = summary.getCourseCount() == null ? courses.size() : summary.getCourseCount();

        StringBuilder report = new StringBuilder();
        report.append("【规划概览】")
                .append(studentName)
                .append("当前按")
                .append(intentLabel(intent))
                .append("进行未来规划分析。六维画像中优势更偏向")
                .append(strongest)
                .append("，相对短板在")
                .append(weakest)
                .append("；成绩侧平均分为")
                .append(formatRate(avgScore))
                .append("分，及格率为")
                .append(formatRate(passRate))
                .append("%，已纳入分析课程数为")
                .append(courseCount)
                .append("门。");

        report.append("\n【路径判断】");
        if ("grad".equals(intent)) {
            report.append("如果以考研为主，应优先判断理论基础和持续刷题耐力是否稳定。当前更适合围绕高分课程总结知识框架，同时针对低分课程补足推导能力与综合题迁移。");
        } else if ("civil".equals(intent)) {
            report.append("如果以考公为主，应优先判断基础稳定性、限时准确率和规范表达。当前更适合用均衡提分策略巩固公共基础类能力，而不是把精力过度集中在少数难课。");
        } else {
            report.append("如果以就业为主，应优先判断实践转化能力、项目表达和岗位匹配度。当前更适合把优势维度对应到作品集、项目经历和实习方向上，而不是只看单门课程高低。");
        }

        report.append("\n【行动建议】")
                .append(planningActionPlan(intent, strongest, weakest, summary));

        report.append("\n【跟踪建议】")
                .append(planningTrackingPlan(intent, summary));

        return report.toString();
    }

    private Map<String, Object> buildPlanningSummaryMap(StudentPlanningReportDto.ScoreSummary summary) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("avg_score", safeDouble(summary.getAvgScore()));
        map.put("pass_rate", safeDouble(summary.getPassRate()));
        map.put("course_count", summary.getCourseCount() == null ? 0 : summary.getCourseCount());
        map.put("max_score", safeDouble(summary.getMaxScore()));
        map.put("min_score", safeDouble(summary.getMinScore()));
        map.put("strong_courses", summary.getStrongCourses() == null ? new ArrayList<String>() : summary.getStrongCourses());
        map.put("weak_courses", summary.getWeakCourses() == null ? new ArrayList<String>() : summary.getWeakCourses());
        return map;
    }

    private List<Map<String, Object>> buildPlanningCourseList(List<StudentPlanningReportDto.CourseScore> courses) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StudentPlanningReportDto.CourseScore course : courses) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("term", safeText(course.getTerm(), ""));
            item.put("course_name", safeText(course.getCourseName(), "未命名课程"));
            item.put("score", safeDouble(course.getScore()));
            item.put("regular_score", safeDouble(course.getRegularScore()));
            item.put("test_score", safeDouble(course.getTestScore()));
            item.put("exam_score", safeDouble(course.getExamScore()));
            list.add(item);
        }
        return list;
    }

    private List<Map<String, Object>> buildScoreWeaknessCourseList(List<ScoreWeaknessReportDto.CourseScore> courses) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ScoreWeaknessReportDto.CourseScore course : courses) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("term", safeText(course.getTerm(), ""));
            item.put("course_name", safeText(course.getCourseName(), "未命名课程"));
            item.put("score", safeDouble(course.getScore()));
            item.put("regular_score", safeDouble(course.getRegularScore()));
            item.put("test_score", safeDouble(course.getTestScore()));
            item.put("exam_score", safeDouble(course.getExamScore()));
            list.add(item);
        }
        return list;
    }

    private String buildScoreWeaknessFallbackReport(ScoreWeaknessReportDto dto) {
        List<ScoreWeaknessReportDto.CourseScore> courses = new ArrayList<>(dto.getCourses());
        courses.sort(Comparator.comparingDouble(course -> safeDouble(course.getScore())));

        String weakCourses = joinWeakCourseNames(courses, Math.min(3, courses.size()));
        double avgScore = safeDouble(dto.getAvgScore());
        double passRate = safeDouble(dto.getPassRate());
        double minScore = safeDouble(dto.getMinScore());

        StringBuilder report = new StringBuilder();
        report.append("【成绩概览】当前共纳入")
                .append(dto.getCourseCount() == null ? courses.size() : dto.getCourseCount())
                .append("门课程，平均分为")
                .append(formatRate(avgScore))
                .append("分，及格率为")
                .append(formatRate(passRate))
                .append("%，最低分为")
                .append(formatRate(minScore))
                .append("分。");

        report.append("\n【弱项判断】从所有成绩看，当前最需要关注的课程是")
                .append(weakCourses)
                .append("。这些课程拉低整体均分，也更可能暴露基础理解、练习强度或考试发挥方面的问题。");

        report.append("\n【改进建议】建议先按最低分课程建立复习清单：每门课整理 3 类错因，包括概念不清、题型不熟和考试失误；随后每周固定完成一次错题二刷。");

        report.append("\n【跟踪建议】后续重点观察低分课程是否能提升到 75 分以上，同时保持高分课程稳定，避免只补短板导致优势课程下滑。");

        return report.toString();
    }

    private String buildCourseAnalysisReport(String studentName,
                                             String courseName,
                                             List<PointAnalysis> points) {
        if (points.isEmpty()) {
            return "【学情概览】" + studentName + "在《" + courseName + "》暂无可用于单科分析的知识点明细。"
                    + "\n【原因判断】当前只有课程总成绩，缺少 score_detail 中的知识点拆解数据。"
                    + "\n【学习建议】请先由任课教师完成成绩组成或知识点拆解后，再查看该课程的雷达图与学情分析。";
        }

        double averageRate = roundOne(averageRate(points));
        List<PointAnalysis> weakPoints = filterBelow(points, 60.0);
        List<PointAnalysis> riskPoints = filterBetween(points, 60.0, 75.0);
        List<PointAnalysis> strongPoints = filterAtLeast(points, 85.0);
        List<PointAnalysis> sortedByRate = new ArrayList<>(points);
        sortedByRate.sort(Comparator.comparingDouble(point -> point.rate));
        PointAnalysis firstPriority = sortedByRate.get(0);

        StringBuilder report = new StringBuilder();
        report.append("【学情概览】")
                .append(studentName)
                .append("在《")
                .append(courseName)
                .append("》的知识点平均掌握率为")
                .append(formatRate(averageRate))
                .append("%，整体处于")
                .append(levelText(averageRate))
                .append("水平。");

        if (strongPoints.isEmpty()) {
            report.append("\n【优势表现】当前暂无特别突出的高掌握知识点，说明该课程还没有形成稳定优势项。");
        } else {
            report.append("\n【优势表现】")
                    .append(joinPointNames(strongPoints, 3))
                    .append("掌握较好，可作为后续综合题、应用题和复盘讲解的支撑点。");
        }

        if (!weakPoints.isEmpty()) {
            report.append("\n【薄弱诊断】主要短板集中在")
                    .append(joinPointNames(weakPoints, 4))
                    .append("，这些知识点低于 60%，会影响后续章节理解和综合题得分。");
        } else if (!riskPoints.isEmpty()) {
            report.append("\n【薄弱诊断】暂无明显不及格知识点，但")
                    .append(joinPointNames(riskPoints, 4))
                    .append("处于临界区，后续测验中容易出现波动。");
        } else {
            report.append("\n【薄弱诊断】各知识点均达到基础要求，主要提升空间在知识迁移、答题稳定性和综合应用。");
        }

        report.append("\n【学习建议】优先围绕“")
                .append(firstPriority.name)
                .append("”进行专项复习：先回看概念与例题，再完成 3 到 5 道同类练习，最后用错题复盘确认是否真正掌握。");

        report.append("\n【跟踪建议】建议在下一次作业或小测中重点观察“")
                .append(firstPriority.name)
                .append("”的得分变化，若两周后仍低于 75%，需要增加一次针对性答疑或二刷训练。");

        return report.toString();
    }

    private List<PointAnalysis> buildPointAnalyses(Integer studentId, Integer courseId) {
        QueryWrapper<ScoreDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId).eq("course_id", courseId);

        List<ScoreDetail> details = scoreDetailMapper.selectList(wrapper);
        List<PointAnalysis> points = new ArrayList<>();

        for (ScoreDetail detail : details) {
            KnowledgePoint kp = knowledgePointMapper.selectById(detail.getPointId());
            if (kp == null) {
                continue;
            }

            double actualScore = detail.getActualScore() == null ? 0.0 : detail.getActualScore();
            double maxScore = detail.getMaxScore() == null ? 0.0 : detail.getMaxScore();
            double rate = maxScore > 0 ? actualScore / maxScore * 100.0 : actualScore;
            rate = Math.max(0.0, Math.min(100.0, rate));

            points.add(new PointAnalysis(
                    safeText(kp.getPointName(), "未命名知识点"),
                    roundOne(actualScore),
                    roundOne(maxScore),
                    roundOne(rate)
            ));
        }

        points.sort(Comparator.comparing(point -> point.name));
        return points;
    }

    private String buildLocalProfessionalReport(String studentName,
                                                String courseName,
                                                String intent,
                                                List<PointAnalysis> points) {
        String normalizedIntent = normalizeIntent(intent);
        if (points.isEmpty()) {
            return "【学情概览】" + studentName + "在《" + courseName + "》暂无可用于 AI 诊断的知识点明细。"
                    + "\n【原因判断】当前只可能有总成绩，缺少 score_detail 中的知识点拆解数据，无法形成可靠的薄弱项判断。"
                    + "\n【目标口径】当前选择为" + intentLabel(normalizedIntent) + "，系统需要知识点明细后才能给出该方向的匹配判断。"
                    + "\n【改进建议】请先录入或重新保存该课程成绩，让系统生成知识点得分明细后再生成报告。";
        }

        double averageRate = roundOne(averageRate(points));
        List<PointAnalysis> weakPoints = filterBelow(points, 60.0);
        List<PointAnalysis> riskPoints = filterBetween(points, 60.0, 75.0);
        List<PointAnalysis> strongPoints = filterAtLeast(points, 85.0);
        List<PointAnalysis> sortedByRate = new ArrayList<>(points);
        sortedByRate.sort(Comparator.comparingDouble(point -> point.rate));

        StringBuilder report = new StringBuilder();
        report.append("【学情概览】")
                .append(studentName)
                .append("在《")
                .append(courseName)
                .append("》的知识点平均掌握率为")
                .append(formatRate(averageRate))
                .append("%，整体处于")
                .append(levelText(averageRate))
                .append("水平。本次按")
                .append(intentLabel(normalizedIntent))
                .append("分析，重点关注")
                .append(intentFocus(normalizedIntent))
                .append("。");

        if (strongPoints.isEmpty()) {
            report.append("\n【优势表现】目前暂无特别突出的高掌握知识点，说明学习表现相对均衡，但拔高项还不明显。");
        } else {
            report.append("\n【优势表现】")
                    .append(joinPointNames(strongPoints, 3))
                    .append("掌握较好，可作为后续综合题和应用题训练的基础。");
        }

        if (!weakPoints.isEmpty()) {
            report.append("\n【薄弱诊断】核心短板集中在")
                    .append(joinPointNames(weakPoints, 4))
                    .append("，这些维度低于 60%，需要优先补齐概念理解和基础题熟练度。");
        } else if (!riskPoints.isEmpty()) {
            report.append("\n【薄弱诊断】没有明显不及格知识点，但")
                    .append(joinPointNames(riskPoints, 4))
                    .append("处于临界区，后续测验中存在波动风险。");
        } else {
            report.append("\n【薄弱诊断】各知识点均达到基础要求，主要问题不在补缺，而在综合迁移和稳定性提升。");
        }

        PointAnalysis firstPriority = sortedByRate.get(0);
        report.append("\n【改进建议】")
                .append(intentActionPlan(normalizedIntent, firstPriority.name));

        report.append("\n【跟踪建议】")
                .append(intentTrackingPlan(normalizedIntent));

        return report.toString();
    }

    private Map<String, Double> buildScoreMap(List<PointAnalysis> points) {
        Map<String, Double> scoreMap = new LinkedHashMap<>();
        for (PointAnalysis point : points) {
            scoreMap.put(point.name, point.rate);
        }
        return scoreMap;
    }

    private List<String> buildPointNames(List<PointAnalysis> points) {
        List<String> names = new ArrayList<>();
        for (PointAnalysis point : points) {
            names.add(point.name);
        }
        return names;
    }

    private List<Double> buildPointRates(List<PointAnalysis> points) {
        List<Double> rates = new ArrayList<>();
        for (PointAnalysis point : points) {
            rates.add(point.rate);
        }
        return rates;
    }

    private List<Map<String, Object>> buildDetailList(List<PointAnalysis> points) {
        List<Map<String, Object>> details = new ArrayList<>();
        for (PointAnalysis point : points) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", point.name);
            item.put("actualScore", point.actualScore);
            item.put("maxScore", point.maxScore);
            item.put("rate", point.rate);
            item.put("level", levelText(point.rate));
            details.add(item);
        }
        return details;
    }

    private String buildWeaknessDesc(List<PointAnalysis> points) {
        List<PointAnalysis> weakPoints = filterBelow(points, 60.0);
        if (!weakPoints.isEmpty()) {
            return joinPointNames(weakPoints, 5);
        }

        List<PointAnalysis> sorted = new ArrayList<>(points);
        sorted.sort(Comparator.comparingDouble(point -> point.rate));
        return "暂无不及格知识点，最低掌握项为" + joinPointNames(sorted, Math.min(3, sorted.size()));
    }

    private String buildStrengthDesc(List<PointAnalysis> points) {
        List<PointAnalysis> strongPoints = filterAtLeast(points, 85.0);
        if (strongPoints.isEmpty()) {
            return "暂无明显高掌握优势项";
        }
        return joinPointNames(strongPoints, 5);
    }

    private double averageRate(List<PointAnalysis> points) {
        if (points.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (PointAnalysis point : points) {
            total += point.rate;
        }
        return total / points.size();
    }

    private List<PointAnalysis> filterBelow(List<PointAnalysis> points, double maxExclusive) {
        List<PointAnalysis> result = new ArrayList<>();
        for (PointAnalysis point : points) {
            if (point.rate < maxExclusive) {
                result.add(point);
            }
        }
        result.sort(Comparator.comparingDouble(point -> point.rate));
        return result;
    }

    private List<PointAnalysis> filterBetween(List<PointAnalysis> points,
                                              double minInclusive,
                                              double maxExclusive) {
        List<PointAnalysis> result = new ArrayList<>();
        for (PointAnalysis point : points) {
            if (point.rate >= minInclusive && point.rate < maxExclusive) {
                result.add(point);
            }
        }
        result.sort(Comparator.comparingDouble(point -> point.rate));
        return result;
    }

    private List<PointAnalysis> filterAtLeast(List<PointAnalysis> points, double minInclusive) {
        List<PointAnalysis> result = new ArrayList<>();
        for (PointAnalysis point : points) {
            if (point.rate >= minInclusive) {
                result.add(point);
            }
        }
        result.sort((left, right) -> Double.compare(right.rate, left.rate));
        return result;
    }

    private String joinPointNames(List<PointAnalysis> points, int limit) {
        if (points.isEmpty()) {
            return "暂无";
        }

        StringBuilder builder = new StringBuilder();
        int count = Math.min(limit, points.size());
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                builder.append("、");
            }
            PointAnalysis point = points.get(i);
            builder.append(point.name)
                    .append("(")
                    .append(formatRate(point.rate))
                    .append("%)");
        }
        return builder.toString();
    }

    private String levelText(double rate) {
        if (rate >= 90) {
            return "优秀";
        }
        if (rate >= 80) {
            return "良好";
        }
        if (rate >= 60) {
            return "基础达标";
        }
        return "薄弱";
    }

    private String normalizeIntent(String intent) {
        if ("grad".equalsIgnoreCase(intent)) {
            return "grad";
        }
        if ("civil".equalsIgnoreCase(intent)) {
            return "civil";
        }
        return "job";
    }

    private String intentLabel(String intent) {
        if ("grad".equals(intent)) {
            return "考研深造导向";
        }
        if ("civil".equals(intent)) {
            return "考公考编导向";
        }
        return "就业导向";
    }

    private String intentFocus(String intent) {
        if ("grad".equals(intent)) {
            return "理论基础、逻辑推导、综合题迁移和持续深造潜力";
        }
        if ("civil".equals(intent)) {
            return "基础稳定性、限时准确率、材料理解和规范表达";
        }
        return "项目实践、工程实现、问题拆解和成果展示能力";
    }

    private String intentActionPlan(String intent, String priorityPoint) {
        if ("grad".equals(intent)) {
            return "建议先围绕“" + priorityPoint + "”建立概念框架，再做推导题和综合题训练；每次练习后记录错因类型，重点区分概念不清、步骤跳跃和计算失误。";
        }
        if ("civil".equals(intent)) {
            return "建议先围绕“" + priorityPoint + "”补齐基础题正确率，再进行 20 分钟限时训练；答题后用一句话归纳解题依据，训练稳定输出和规范表达。";
        }
        return "建议先围绕“" + priorityPoint + "”设计一个小型项目任务，把概念复习转化为接口、页面或算法模块实现，并沉淀代码截图、说明文档和问题复盘。";
    }

    private String intentTrackingPlan(String intent) {
        if ("grad".equals(intent)) {
            return "后续两周重点跟踪薄弱知识点的综合题得分率，若仍低于 75%，应增加专题讲解和错题二刷。";
        }
        if ("civil".equals(intent)) {
            return "后续两周重点跟踪基础题正确率和限时完成率，若波动明显，应减少难题投入，先稳定基础分。";
        }
        return "后续两周重点跟踪薄弱知识点在项目任务中的完成质量，检查是否能独立说明实现思路、关键问题和改进方案。";
    }

    private boolean isUsableAiReport(String report) {
        if (report == null) {
            return false;
        }

        String text = report.trim();
        String lower = text.toLowerCase(Locale.ROOT);

        if (text.length() < 80) {
            return false;
        }

        return !text.contains("连接本地 AI 失败")
                && !text.contains("AI 服务异常")
                && !text.contains("状态码")
                && !text.contains("解析失败")
                && !lower.contains("error")
                && (text.contains("学情")
                || text.contains("薄弱")
                || text.contains("建议")
                || text.contains("掌握"));
    }

    private boolean isUsablePlanningReport(String report) {
        if (report == null) {
            return false;
        }

        String text = report.trim();
        String lower = text.toLowerCase(Locale.ROOT);

        if (text.length() < 80) {
            return false;
        }

        return !text.contains("连接本地 AI 失败")
                && !text.contains("AI 服务异常")
                && !text.contains("状态码")
                && !lower.contains("error")
                && (text.contains("规划")
                || text.contains("路径")
                || text.contains("建议")
                || text.contains("画像"));
    }

    private boolean isUsableCourseAnalysis(String report) {
        if (report == null) {
            return false;
        }

        String text = report.trim();
        String lower = text.toLowerCase(Locale.ROOT);

        if (text.length() < 80) {
            return false;
        }

        return !text.contains("连接本地 AI 失败")
                && !text.contains("AI 服务异常")
                && !text.contains("状态码")
                && !lower.contains("error")
                && (text.contains("学情")
                || text.contains("优势")
                || text.contains("薄弱")
                || text.contains("建议"));
    }

    private boolean isUsableScoreWeaknessReport(String report) {
        if (report == null) {
            return false;
        }

        String text = report.trim();
        String lower = text.toLowerCase(Locale.ROOT);

        if (text.length() < 80) {
            return false;
        }

        return !text.contains("连接本地 AI 失败")
                && !text.contains("AI 服务异常")
                && !text.contains("状态码")
                && !lower.contains("error")
                && (text.contains("成绩")
                || text.contains("弱项")
                || text.contains("课程")
                || text.contains("建议"));
    }

    private boolean isUsableClassReport(String report) {
        if (report == null) {
            return false;
        }

        String text = report.trim();
        String lower = text.toLowerCase(Locale.ROOT);

        if (text.length() < 80) {
            return false;
        }

        return !text.contains("连接本地 AI 失败")
                && !text.contains("AI 服务异常")
                && !text.contains("状态码")
                && !lower.contains("error")
                && (text.contains("班级")
                || text.contains("集体")
                || text.contains("短板")
                || text.contains("教学建议"));
    }

    private String cleanReport(String report) {
        return report == null ? "" : report.trim();
    }

    private String safeText(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }

    private double roundOne(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private double safeDouble(Double value) {
        return value == null ? 0.0 : value;
    }

    private String formatRate(double value) {
        return String.format(Locale.ROOT, "%.1f", value);
    }

    private String joinWeakCourseNames(List<ScoreWeaknessReportDto.CourseScore> courses, int limit) {
        if (courses.isEmpty()) {
            return "暂无";
        }

        StringBuilder builder = new StringBuilder();
        int count = Math.min(limit, courses.size());
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                builder.append("、");
            }
            ScoreWeaknessReportDto.CourseScore course = courses.get(i);
            builder.append(safeText(course.getCourseName(), "未命名课程"))
                    .append("(")
                    .append(formatRate(safeDouble(course.getScore())))
                    .append("分)");
        }
        return builder.toString();
    }

    private String planningActionPlan(String intent,
                                      String strongest,
                                      String weakest,
                                      StudentPlanningReportDto.ScoreSummary summary) {
        if ("grad".equals(intent)) {
            return "建议把优势维度 " + strongest + " 对应到考研专业课和数学逻辑训练上，同时针对 " + weakest + " 建立每周错题归因表；优先复盘低分课程，形成专题笔记和二刷计划。";
        }
        if ("civil".equals(intent)) {
            return "建议围绕 " + weakest + " 做基础稳定训练，每周安排 3 次限时刷题并记录正确率；同时用 " + strongest + " 支撑材料理解和结构化表达，逐步把平均分和完成率拉稳。";
        }
        return "建议把 " + strongest + " 转化为项目方向或实习卖点，再围绕 " + weakest + " 补齐岗位短板；优先从成绩较高课程中提炼作品案例，从较低课程中梳理要补的工程能力。";
    }

    private String planningTrackingPlan(String intent, StudentPlanningReportDto.ScoreSummary summary) {
        double avgScore = safeDouble(summary.getAvgScore());
        if ("grad".equals(intent)) {
            return avgScore >= 80
                    ? "后续重点跟踪综合题得分率和复习连续性，每两周检查一次低分课程是否完成二刷。"
                    : "后续先把平均分抬到 80 分附近，再逐步增加综合题和跨章节训练，避免一开始投入过多拔高题。";
        }
        if ("civil".equals(intent)) {
            return "后续重点跟踪限时完成率、基础题正确率和表达规范度，每周至少复盘一次错题类型分布。";
        }
        return "后续重点跟踪项目输出、面试表达和课程能力转化效果，每两周检查一次是否新增了可展示成果。";
    }

    private static class PointAnalysis {
        private final String name;
        private final double actualScore;
        private final double maxScore;
        private final double rate;

        private PointAnalysis(String name, double actualScore, double maxScore, double rate) {
            this.name = name;
            this.actualScore = actualScore;
            this.maxScore = maxScore;
            this.rate = rate;
        }
    }

    private static class ClassAnalysisSummary {
        private String className = "当前班级";
        private String courseName = "当前课程";
        private int studentCount = 0;
        private double avgScore = 0.0;
        private double maxScore = 0.0;
        private double minScore = 0.0;
        private double passRate = 0.0;
        private double excellentRate = 0.0;
        private double standardDeviation = 0.0;
        private String weakestPointText = "暂无知识点明细";
        private String strongestPointText = "暂无知识点明细";
        private Map<String, Integer> scoreRanges = new LinkedHashMap<>();
        private List<ClassPointStat> pointStats = new ArrayList<>();
    }

    private static class ClassPointStat {
        private final String name;
        private int studentCount = 0;
        private double totalActual = 0.0;
        private double totalMax = 0.0;
        private double avgActualScore = 0.0;
        private double avgMaxScore = 0.0;
        private double avgRate = 0.0;

        private ClassPointStat(String name) {
            this.name = name;
        }
    }
}
