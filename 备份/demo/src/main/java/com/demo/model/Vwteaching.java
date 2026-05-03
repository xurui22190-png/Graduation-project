package com.demo.model;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Vwteaching对象", description="VIEW")
public class Vwteaching implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "授课ID")
    @TableField("tcId")
    private Integer tcid;

    @ApiModelProperty(value = "学期ID")
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

    @ApiModelProperty(value = "学年")
    @TableField("yYear")
    private String yyear;

    @ApiModelProperty(value = "学期")
    @TableField("yTerm")
    private String yterm;

    @ApiModelProperty(value = "全称")
    @TableField("yAll")
    private String yall;

    @ApiModelProperty(value = "所属专业ID")
    @TableField("cMajorId")
    private Integer cmajorid;

    @ApiModelProperty(value = "年级")
    @TableField("cGrade")
    private Integer cgrade;

    @ApiModelProperty(value = "班级名称")
    @TableField("className")
    private String classname;

    @ApiModelProperty(value = "班级编号")
    @TableField("classCode")
    private String classcode;

    @ApiModelProperty(value = "课程代码")
    @TableField("crCode")
    private String crcode;

    @ApiModelProperty(value = "课程名称")
    @TableField("crName")
    private String crname;

    @ApiModelProperty(value = "工号")
    @TableField("tNo")
    private String tno;

    @ApiModelProperty(value = "姓名")
    @TableField("tName")
    private String tname;

    @ApiModelProperty(value = "性别")
    @TableField("tSex")
    private String tsex;

    @ApiModelProperty(value = "电话")
    @TableField("tTel")
    private String ttel;

    @ApiModelProperty(value = "教育程度")
    @TableField("tEduLevel")
    private String tedulevel;

    @ApiModelProperty(value = "毕业院校")
    @TableField("tSchool")
    private String tschool;

    @ApiModelProperty(value = "现居地址")
    @TableField("tAddress")
    private String taddress;


}
