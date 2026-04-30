package com.demo.service;

import java.util.List;
import java.util.Map;

/**
 * 学业预警服务接口
 */
public interface IWarningService {

    /**
     * 扫描指定课程的所有学生，调用大模型预测并生成预警记录
     * @param courseId 课程ID
     */
    void scanAndGenerateWarnings(Integer courseId);

    /**
     * 获取预警监控列表（包含学生姓名、课程名等信息）
     * @return 预警列表数据
     */
    List<Map<String, Object>> getWarningList();

    /**
     * 处理/消除预警状态
     * @param warningId 预警ID
     * @param status 要更新的状态(1-已谈话, 2-已消除)
     */
    void updateWarningStatus(Integer warningId, Integer status);

    // 发送专属学业预警公告
    void sendWarningNotice(Integer studentId, String title, String content);
}