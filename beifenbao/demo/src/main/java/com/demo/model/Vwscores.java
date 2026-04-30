package com.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@ApiModel(value="Vwscores对象", description="VIEW")
public class Vwscores implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "成绩ID")
    @TableField("scId")
    private Integer scid;

    @ApiModelProperty(value = "班级名称")
    @TableField("className")
    private String classname;

    @ApiModelProperty(value = "学生ID")
    @TableField("scStudentId")
    private Integer scstudentid;

    @ApiModelProperty(value = "学期ID")
    @TableField("scTermId")
    private Integer sctermid;

    @ApiModelProperty(value = "班级ID")
    @TableField("scClassId")
    private Integer scclassid;

    @ApiModelProperty(value = "课程ID")
    @TableField("scCourseId")
    private Integer sccourseid;

    @ApiModelProperty(value = "分数")
    @TableField("scScore")
    private BigDecimal scscore;

    @ApiModelProperty(value = "设置状态(0:未设置 1:已设置)")
    @TableField("scStatus")
    private Integer scstatus;

    @ApiModelProperty(value = "登记时间")
    @TableField("scCreateDate")
    private LocalDateTime sccreatedate;

    @ApiModelProperty(value = "登记老师ID")
    @TableField("scTeacherId")
    private Integer scteacherid;

    @ApiModelProperty(value = "学号")
    @TableField("sNo")
    private String sno;

    @ApiModelProperty(value = "姓名")
    @TableField("sName")
    private String sname;

    @ApiModelProperty(value = "性别")
    @TableField("sSex")
    private String ssex;

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

    @ApiModelProperty(value = "学年")
    @TableField("yYear")
    private String yyear;

    @ApiModelProperty(value = "学期")
    @TableField("yTerm")
    private String yterm;

    @ApiModelProperty(value = "全称")
    @TableField("yAll")
    private String yall;

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
