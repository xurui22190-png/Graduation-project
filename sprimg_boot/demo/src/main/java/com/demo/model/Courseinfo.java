package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 课程信息表
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Courseinfo对象", description="课程信息表")
public class Courseinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程ID")
    @TableId(value = "crId", type = IdType.AUTO)
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

    @ApiModelProperty(value = "学分")
    @TableField("crCredit")
    private BigDecimal crcredit;

    @ApiModelProperty(value = "总学时")
    @TableField("crPeriod")
    private Integer crperiod;

    @ApiModelProperty(value = "课程类型")
    @TableField("crType")
    private String crtype;

    @ApiModelProperty(value = "考核方式")
    @TableField("crExamType")
    private String crexamtype;

    @ApiModelProperty(value = "备注")
    @TableField("crRemark")
    private String crremark;

    @ApiModelProperty(value = "状态：1启用 0停用")
    @TableField("crState")
    private Integer crstate;

    @ApiModelProperty(value = "创建时间")
    @TableField("createTime")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "更新时间")
    @TableField("updateTime")
    private LocalDateTime updatetime;

}