package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Teaching对象", description="授课安排表")
public class Teaching implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "授课ID")
    @TableId(value = "tcId", type = IdType.AUTO)
    private Integer tcid; // 保持你原来的字段名

    @ApiModelProperty(value = "学期")
    @TableField("tcTermId")
    private Integer tctermid;

    @ApiModelProperty(value = "班级ID")
    @TableField("tcClassId")
    private Integer tcclassid;

    @ApiModelProperty(value = "课程ID")
    @TableField("tcCourseId")
    private Integer tccourseid;

    @ApiModelProperty(value = "教师ID")
    @TableField("tcTeacherId")
    private Integer tcteacherid;

    // 👇👇👇 新增下面这三个权重字段，必须跟数据库字段对应
    @ApiModelProperty(value = "平时成绩权重")
    @TableField("w_regular")
    private Double wregular;

    @ApiModelProperty(value = "测试成绩权重")
    @TableField("w_test")
    private Double wtest;

    @ApiModelProperty(value = "考试成绩权重")
    @TableField("w_exam")
    private Double wexam;
}
