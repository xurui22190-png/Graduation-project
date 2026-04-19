package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "成绩统计查询对象")
public class ScoreStatsDto extends QueryItemModel {

    @ApiModelProperty(value = "学期ID")
    private Integer yid;

    @ApiModelProperty(value = "院系ID")
    private Integer collegeid;

    @ApiModelProperty(value = "专业ID")
    private Integer majorid;

    @ApiModelProperty(value = "班级ID")
    private Integer cid;

    @ApiModelProperty(value = "课程ID")
    private Integer crid;
}