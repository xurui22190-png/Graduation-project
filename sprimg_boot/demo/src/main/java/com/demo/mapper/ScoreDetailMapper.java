package com.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.model.ScoreDetail;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface ScoreDetailMapper extends BaseMapper<ScoreDetail> {

    /**
     * 修复后的 SQL：将 camelCase 改为 snake_case
     * 1. actualScore -> actual_score
     * 2. maxScore -> max_score
     * 3. pointId -> point_id
     * 4. studentId -> student_id
     * 5. courseId -> course_id
     */
    @Select("SELECT sd.actual_score as score, " +
            "sd.max_score as max, " +
            "kp.point_name as point, " +
            "(sd.actual_score / sd.max_score * 100) as rate " +
            "FROM score_detail sd " +
            "LEFT JOIN knowledge_point kp ON sd.point_id = kp.id " +
            "WHERE sd.student_id = #{sid} AND sd.course_id = #{courseId}")
    List<Map<String, Object>> selectDetailsWithPointName(Integer sid, Integer courseId);
}