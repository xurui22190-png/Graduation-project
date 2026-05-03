package com.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "学生端课程查看对象")
public class StudentCourseVo {

    @ApiModelProperty(value = "授课ID")
    private Integer tkid;

    @ApiModelProperty(value = "课程ID")
    private Integer crid;

    @ApiModelProperty(value = "课程编号")
    private String crcode;

    @ApiModelProperty(value = "课程名称")
    private String crname;

    @ApiModelProperty(value = "学分")
    private Double crcredit;

    @ApiModelProperty(value = "课时")
    private Integer crperiod;

    @ApiModelProperty(value = "课程类型")
    private String crtype;

    @ApiModelProperty(value = "考核方式")
    private String crexamtype;

    @ApiModelProperty(value = "教师ID")
    private Integer tid;

    @ApiModelProperty(value = "教师姓名")
    private String tname;

    @ApiModelProperty(value = "学期ID")
    private Integer ytid;

    @ApiModelProperty(value = "学期名称")
    private String ytname;

    @ApiModelProperty(value = "班级ID")
    private Integer cid;

    @ApiModelProperty(value = "班级名称")
    private String cname;
}