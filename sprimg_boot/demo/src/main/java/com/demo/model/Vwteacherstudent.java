package com.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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

    @ApiModelProperty(value = "班级ID")
    private Integer cid;

    @ApiModelProperty(value = "班级名称")
    private String cname;

    @ApiModelProperty(value = "课程ID")
    private Integer crid;

    @ApiModelProperty(value = "课程名称")
    private String crname;

    @ApiModelProperty(value = "学期ID")
    private Integer yid;

    @ApiModelProperty(value = "学期名称")
    private String yall;

    @ApiModelProperty(value = "教师ID")
    private Integer tid;

    @ApiModelProperty(value = "教师名称")
    private String tname;

    @ApiModelProperty(value = "成绩ID")
    private Integer scid;

    @ApiModelProperty(value = "成绩")
    private Double scscore;

    @ApiModelProperty(value = "成绩状态")
    private Integer scstatus;

    @ApiModelProperty(value = "登记时间")
    private Date sccreatedate;
}