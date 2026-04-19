package com.demo.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("vwclass")
@ApiModel(value="Vwclass对象", description="VIEW")
public class Vwclass implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "班级ID")
    @TableField("cId")
    private Integer cid;

    @ApiModelProperty(value = "所属专业ID")
    @TableField("cMajorId")
    private Integer cmajorid;

    @ApiModelProperty(value = "年级")
    @TableField("cGrade")
    private Integer cgrade;

    @ApiModelProperty(value = "班级名称")
    @TableField("cName")
    private String cname;

    @ApiModelProperty(value = "班级编号")
    @TableField("cCode")
    private String ccode;

    @ApiModelProperty(value = "专业名称")
    @TableField("mName")
    private String mname;

    @ApiModelProperty(value = "专业编码")
    @TableField("mCode")
    private String mcode;

    @ApiModelProperty(value = "院系ID")
    @TableField("mCollegeId")
    private Integer mcollegeid;

    @ApiModelProperty(value = "院系名称")
    @TableField("collegeName")
    private String collegename;

    @ApiModelProperty(value = "院系编码")
    @TableField("collegeCode")
    private String collegecode;

    @ApiModelProperty(value = "父级ID")
    @TableField("cParentId")
    private Integer cparentid;

    @ApiModelProperty(value = "院系名称")
    @TableField("cNamet")
    private String cnamet;

    @ApiModelProperty(value = "院系编码")
    @TableField("cCodet")
    private String ccodet;

    @ApiModelProperty(value = "父级ID")
    @TableField("cParentIdt")
    private Integer cparentidt;
}