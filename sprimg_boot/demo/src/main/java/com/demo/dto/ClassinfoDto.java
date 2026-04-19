package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "班级查询对象")
public class ClassinfoDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "班级ID")
    private Integer cid;

    @ApiModelProperty(value = "专业ID")
    private Integer cmajorid;

    @ApiModelProperty(value = "年级")
    private String cgrade;

    @ApiModelProperty(value = "班级名称")
    private String cname;

    @ApiModelProperty(value = "班级编号")
    private String ccode;
}