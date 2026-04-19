package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "教师查看学生视图对象")
public class Vwteacherstudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID")
    private Integer sid;

    @ApiModelProperty(value = "学号")
    private String sno;

    @ApiModelProperty(value = "姓名")
    private String sname;

    @ApiModelProperty(value = "性别")
    private String ssex;

    @ApiModelProperty(value = "班级ID")
    private Integer sclassid;

    @ApiModelProperty(value = "班级名称")
    private String className;

    @ApiModelProperty(value = "课程ID")
    private Integer courseid;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "学期ID")
    private Integer termid;

    @ApiModelProperty(value = "学期名称")
    private String termName;
}