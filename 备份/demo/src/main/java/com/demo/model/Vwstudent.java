package com.demo.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vwstudent")
@ApiModel(value = "Vwstudent对象", description = "学生视图")
public class Vwstudent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID")
    @TableId("sid")
    private Integer sid;

    @ApiModelProperty(value = "学号")
    @TableField("sNo")
    private String sno;

    @ApiModelProperty(value = "姓名")
    @TableField("sName")
    private String sname;

    @ApiModelProperty(value = "性别")
    @TableField("sSex")
    private String ssex;

    @ApiModelProperty(value = "班级ID")
    @TableField("sClassId")
    private Integer sclassid;

    @ApiModelProperty(value = "电话")
    @TableField("sTel")
    private String stel;

    @ApiModelProperty(value = "身份证号")
    @TableField("sIdcard")
    private String sidcard;

    @ApiModelProperty(value = "家庭住址")
    @TableField("sAddress")
    private String saddress;

    @ApiModelProperty(value = "创建时间")
    @TableField("sCreateDate")
    private LocalDateTime screatedate;

    @ApiModelProperty(value = "关联账号ID")
    @TableField("sAccountId")
    private Integer saccountid;

    @ApiModelProperty(value = "所属专业ID")
    @TableField("cMajorId")
    private Integer cmajorid;

    @ApiModelProperty(value = "年级")
    @TableField("cGrade")
    private Integer cgrade;

    @ApiModelProperty(value = "班级名称")
    @TableField("className")
    private String className;

    @ApiModelProperty(value = "班级编号")
    @TableField("classCode")
    private String classCode;

    @ApiModelProperty(value = "专业名称")
    @TableField("mName")
    private String mName;

    @ApiModelProperty(value = "专业编码")
    @TableField("mCode")
    private String mCode;

    @ApiModelProperty(value = "学院ID")
    @TableField("mCollegeId")
    private Integer mcollegeid;

    @ApiModelProperty(value = "学院名称")
    @TableField("collegeName")
    private String collegeName;

    @ApiModelProperty(value = "学院编码")
    @TableField("collegeCode")
    private String collegeCode;
}