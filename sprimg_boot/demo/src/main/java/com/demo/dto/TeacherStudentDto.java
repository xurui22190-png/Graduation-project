package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "教师查看学生查询对象")
public class TeacherStudentDto extends QueryItemModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学期ID")
    private Integer tctermid;

    @ApiModelProperty(value = "课程ID")
    private Integer tccourseid;

    @ApiModelProperty(value = "班级ID")
    private Integer tcclassid;
}