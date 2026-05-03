package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "学生端课程查询对象")
public class StudentCourseDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "学期ID")
    private Integer ytid;
}