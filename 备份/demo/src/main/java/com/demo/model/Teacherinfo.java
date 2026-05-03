package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("teacherinfo")
@ApiModel(value="Teacherinfo对象", description="教师信息表")
public class Teacherinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "教师ID")
    @TableId(value = "tId", type = IdType.AUTO)
    private Integer tid;

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

    @ApiModelProperty(value = "关联账号ID")
    @TableField("tAccountId")
    private Integer taccountid;

    @ApiModelProperty(value = "所属院系ID")
    @TableField("tCollegeId")
    private Integer tcollegeid;

    @ApiModelProperty(value = "院系名称")
    @TableField(exist = false)
    private String collegename;

    @ApiModelProperty(value = "院系编码")
    @TableField(exist = false)
    private String collegecode;

    @ApiModelProperty(value = "父级ID")
    @TableField(exist = false)
    private Integer cparentid;

    @ApiModelProperty(value = "父级名称")
    @TableField(exist = false)
    private String cnamet;

    @ApiModelProperty(value = "父级编码")
    @TableField(exist = false)
    private String ccodet;

    @ApiModelProperty(value = "父级父ID")
    @TableField(exist = false)
    private Integer cparentidt;
}