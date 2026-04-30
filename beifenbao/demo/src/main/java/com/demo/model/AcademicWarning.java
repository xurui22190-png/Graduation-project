package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 学业预警实体类
 */
@Data
@TableName("academic_warning")
public class AcademicWarning {

    @TableId(type = IdType.AUTO)
    private Integer wid;            // 主键

    private Integer wstudentid;     // 学生ID

    private Integer wcourseid;      // 课程ID

    private String wrisklevel;      // 风险等级：高风险、中风险、低风险

    private Double wprobability;    // 不及格预测概率 (0.0-1.0)

    private String wreason;         // 预警原因（例如：出勤率不足，平时分偏低）

    private Integer wstatus;        // 状态：0-未处理, 1-已谈话, 2-已消除

    private Date wcreatedate;       // 预警生成时间
}