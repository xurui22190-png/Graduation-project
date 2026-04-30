package com.demo.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 成绩信息表
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Scoreinfo对象", description="成绩信息表")
public class Scoreinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "成绩ID")
    @TableId(value = "scId", type = IdType.AUTO)
    private Integer scid;

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


}
