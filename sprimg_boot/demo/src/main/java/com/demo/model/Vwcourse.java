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
@ApiModel(value="Vwcourse对象", description="VIEW")
public class Vwcourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程ID")
    @TableField("crId")
    private Integer crid;

    @ApiModelProperty(value = "课程代码")
    @TableField("crCode")
    private String crcode;

    @ApiModelProperty(value = "课程名称")
    @TableField("crName")
    private String crname;

    @ApiModelProperty(value = "隶属专业ID")
    @TableField("crMajorId")
    private Integer crmajorid;

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
