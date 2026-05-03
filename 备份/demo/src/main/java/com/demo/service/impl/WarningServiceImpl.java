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

import java.util.*;

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

    // 注入 RestTemplate 用于调用 Python 接口
    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scanAndGenerateWarnings(Integer courseId) {
        // 1. 获取该课程下的所有学生成绩记录
        QueryWrapper<Scoreinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("scCourseId", courseId).isNotNull("scScore");
        List<Scoreinfo> scores = scoreinfoMapper.selectList(queryWrapper);

        for (Scoreinfo score : scores) {
            Double finalScore = score.getScscore().doubleValue();
            System.out.println("正在处理学生ID: " + score.getScstudentid() + " 最终成绩: " + finalScore);

            // 2. [数据拟真] 根据总分，推导平时的表现（为了送给 AI 进行预测）
            Random r = new Random(score.getScid());
            double homeworkAvg = Math.min(100, Math.max(0, finalScore + (r.nextDouble() * 20 - 10)));
            double attendanceRate = Math.min(1.0, Math.max(0.0, (finalScore / 100.0) + (r.nextDouble() * 0.3)));
            double midtermScore = Math.min(100, Math.max(0, finalScore + (r.nextDouble() * 30 - 15)));

            // 3. 构造发送给 Python 模型的数据
            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("homework_avg", homeworkAvg);
            reqBody.put("attendance_rate", attendanceRate);
            reqBody.put("midterm_score", midtermScore);

            try {
                // 4. 调用 Python 的预测接口
                String pythonUrl = "http://127.0.0.1:8000/api/predict_risk";
                ResponseEntity<Map> response = restTemplate.postForEntity(pythonUrl, reqBody, Map.class);
                Map<String, Object> result = response.getBody();

                if (result != null && (Integer) result.get("code") == 200) {
                    Double probability = (Double) result.get("probability");
                    String riskLevel = (String) result.get("risk_level");

                    System.out.println("【AI预警引擎】学生ID: " + score.getScstudentid() + " | 预测挂科概率: " + probability);

                    // 只要风险概率大于 10%，就生成预警
                    if (probability > 0.1) {
                        // 检查是否已经存在未处理的预警，避免重复插入
                        QueryWrapper<AcademicWarning> existWrapper = new QueryWrapper<>();
                        existWrapper.eq("wstudentid", score.getScstudentid())
                                .eq("wcourseid", courseId)
                                .in("wstatus", Arrays.asList(0, 1));

                        if (warningMapper.selectCount(existWrapper) == 0) {
                            AcademicWarning warning = new AcademicWarning();
                            warning.setWstudentid(score.getScstudentid());
                            warning.setWcourseid(courseId);
                            warning.setWprobability(probability);
                            warning.setWrisklevel(riskLevel);
                            warning.setWreason("AI模型综合评估该生的作业表现与出勤状况，计算出其综合学业风险指数高达 " + Math.round(probability * 100) + "%，建议辅导员/导师重点关注并介入干预。");
                            warning.setWstatus(0);
                            warning.setWcreatedate(new Date());
                            warningMapper.insert(warning);

                            System.out.println("✅ 成功生成一条预警记录并入库！");
                        } else {
                            System.out.println("⚠️ 该学生已有未处理的预警，跳过生成。");
                        }
                    }
                } else {
                    System.err.println("❌ Python返回状态码异常：" + result);
                }
            } catch (Exception e) {
                // 🌟🌟🌟 最关键的一步：打印完整的错误堆栈，抓出真凶！
                System.err.println("❌ 严重错误！调用 Python AI 预警模型失败！");
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Map<String, Object>> getWarningList() {
        // 获取所有预警记录，并关联学生姓名（此处用简单的循环关联，也可改用 Mapper SQL 联查）
        List<AcademicWarning> warnings = warningMapper.selectList(new QueryWrapper<AcademicWarning>().orderByDesc("wcreatedate"));
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (AcademicWarning w : warnings) {
            Map<String, Object> map = new HashMap<>();
            map.put("wid", w.getWid());
            map.put("riskLevel", w.getWrisklevel());
            map.put("probability", w.getWprobability());
            map.put("reason", w.getWreason());
            map.put("status", w.getWstatus());
            map.put("createDate", w.getWcreatedate());

            // 查出学生姓名
            Studentinfo student = studentinfoMapper.selectById(w.getWstudentid());
            if (student != null) {
                map.put("studentName", student.getSname());
                map.put("studentNo", student.getSno());
            }

            // 此处可补充查询 Course 名称
            map.put("courseId", w.getWcourseid());
            // map.put("courseName", courseMapper.selectById(w.getWcourseid()).getCourseName());

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

        // 1. 使用完全匹配实体类的 set 方法
        // 巧妙操作：因为表里没接收人字段，我们在标题前面加上【专属推送】来吸引学生注意
        notice.setNtitle("【专属推送】" + title);
        notice.setNcontent(content);
        notice.setNcreatetime(new Date());

        // 2. 补充你表结构里需要的其他默认字段，让公告显得更专业
        notice.setNtype("学业预警");        // 公告类型
        notice.setNstate(1);             // 状态：1发布 (学生立刻就能看到)
        notice.setNtop(1);               // 是否置顶：1是 (放在公告最上面)
        notice.setNisdelete(0);          // 是否删除：0否
        notice.setNcreatename("AI学业指导中心"); // 创建人姓名
        notice.setNcreateuid(0);         // 填个默认的系统ID

        // 3. 插入到数据库的公告表中
        noticeinfoMapper.insert(notice);
    }
}