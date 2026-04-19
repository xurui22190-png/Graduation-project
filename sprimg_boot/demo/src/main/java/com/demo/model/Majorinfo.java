package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 专业信息表
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Majorinfo对象", description="专业信息表")
public class Majorinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专业ID")
    @TableId(value = "mid", type = IdType.AUTO)
    private Integer mid;

    @ApiModelProperty(value = "专业名称")
    @TableField("mName")
    private String mname;

    @ApiModelProperty(value = "专业编码")
    @TableField("mCode")
    private String mcode;

    @ApiModelProperty(value = "院系ID")
    @TableField("mCollegeId")
    private Integer mcollegeid;


}
