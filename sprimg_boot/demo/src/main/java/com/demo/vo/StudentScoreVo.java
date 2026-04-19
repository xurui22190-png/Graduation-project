package com.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "学生成绩显示对象")
public class StudentScoreVo implements Serializable {

    @ApiModelProperty(value = "成绩ID")
    private Integer scid;

    @ApiModelProperty(value = "学生ID")
    private Integer sid;

    @ApiModelProperty(value = "学号")
    private String sno;

    @ApiModelProperty(value = "姓名")
    private String sname;

    @ApiModelProperty(value = "学期ID")
    private Integer sctermid;

    @ApiModelProperty(value = "学年")
    private String yyear;

    @ApiModelProperty(value = "学期")
    private String yterm;

    @ApiModelProperty(value = "学期全称")
    private String yall;

    @ApiModelProperty(value = "课程ID")
    private Integer sccourseid;

    @ApiModelProperty(value = "课程编号")
    private String crcode;

    @ApiModelProperty(value = "课程名称")
    private String crname;

    @ApiModelProperty(value = "成绩")
    private Double scscore;

    @ApiModelProperty(value = "状态")
    private Integer scstatus;

    @ApiModelProperty(value = "教师姓名")
    private String tname;
}