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

    @ApiModelProperty(value = "授课任务学期ID")
    private Integer tctermid;

    @ApiModelProperty(value = "授课任务课程ID")
    private Integer tccourseid;

    @ApiModelProperty(value = "授课任务班级ID")
    private Integer tcclassid;

    @ApiModelProperty(value = "成绩录入学期ID")
    private Integer sctermid;

    @ApiModelProperty(value = "成绩录入课程ID")
    private Integer sccourseid;

    @ApiModelProperty(value = "成绩录入班级ID")
    private Integer scclassid;

    @ApiModelProperty(value = "录入状态")
    private Integer scstatus;

    public Integer getQueryTermId() {
        return sctermid != null ? sctermid : tctermid;
    }

    public Integer getQueryCourseId() {
        return sccourseid != null ? sccourseid : tccourseid;
    }

    public Integer getQueryClassId() {
        return scclassid != null ? scclassid : tcclassid;
    }
}