package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 专业信息表
 * </p>
 *
 * @author xyh
 * @since 2026-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "专业对象")
public class MajorinfoDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "专业ID")
    private Integer mid;

    @ApiModelProperty(value = "专业名称")
    private String mname;

    @ApiModelProperty(value = "专业编码")
    private String mcode;

    @ApiModelProperty(value = "所属院系ID")
    private Integer mcollegeid;
}