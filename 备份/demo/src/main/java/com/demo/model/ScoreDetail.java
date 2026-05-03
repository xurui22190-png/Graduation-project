package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("score_detail")
public class ScoreDetail {

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 学生ID
    private Integer studentId;

    // 课程ID
    private Integer courseId;

    // 知识点ID
    private Integer pointId;

    // 该知识点满分 (注意这里改成了 Double)
    private Double maxScore;

    // 实际得分 (注意这里改成了 Double)
    private Double actualScore;

    // 创建时间
    private Date createTime;
}