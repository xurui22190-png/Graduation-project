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

/**
 * <p>
 * 班级信息表
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("classinfo")
@ApiModel(value = "Classinfo对象", description = "班级信息表")
public class Classinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "班级ID")
    @TableId(value = "cId", type = IdType.AUTO)
    private Integer cid;

    @ApiModelProperty(value = "所属专业ID")
    @TableField("cMajorId")
    private Integer cmajorid;

    @ApiModelProperty(value = "年级")
    @TableField("cGrade")
    private String cgrade;

    @ApiModelProperty(value = "班级名称")
    @TableField("cName")
    private String cname;

    @ApiModelProperty(value = "班级编号")
    @TableField("cCode")
    private String ccode;
}