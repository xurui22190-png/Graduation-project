package com.demo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryItemModel {
    @ApiModelProperty(value = "关键词")
    private String qkey;

    @ApiModelProperty(value = "页码")
    private int pageIndex = 1;

    @ApiModelProperty(value = "页数据量")
    private int pageSize = 20;
}
