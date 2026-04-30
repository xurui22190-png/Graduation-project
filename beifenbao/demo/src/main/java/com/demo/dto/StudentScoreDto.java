package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "学生成绩查询对象")
public class StudentScoreDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "学生ID")
    private Integer sid;

    @ApiModelProperty(value = "学期ID")
    private Integer termid;
}