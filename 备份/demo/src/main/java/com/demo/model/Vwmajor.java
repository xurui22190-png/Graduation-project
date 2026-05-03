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
@ApiModel(value="Vwmajor对象", description="VIEW")
public class Vwmajor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专业ID")
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

    @ApiModelProperty(value = "院系名称")
    @TableField("cName")
    private String cname;

    @ApiModelProperty(value = "院系编码")
    @TableField("cCode")
    private String ccode;

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
