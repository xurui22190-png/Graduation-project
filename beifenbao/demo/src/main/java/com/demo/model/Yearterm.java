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
 * 学年学期表
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Yearterm对象", description="学年学期表")
public class Yearterm implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学期ID")
    @TableId(value = "yId", type = IdType.AUTO)
    private Integer yid;

    @ApiModelProperty(value = "学年")
    @TableField("yYear")
    private String yyear;

    @ApiModelProperty(value = "学期")
    @TableField("yTerm")
    private String yterm;

    @ApiModelProperty(value = "全称")
    @TableField("yAll")
    private String yall;


}
