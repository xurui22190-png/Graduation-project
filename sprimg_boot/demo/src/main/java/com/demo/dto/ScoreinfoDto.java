package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "成绩查询对象")
public class ScoreinfoDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "成绩ID")
    private Integer scid;

    @ApiModelProperty(value = "学生ID")
    private Integer scstudentid;

    @ApiModelProperty(value = "学期ID")
    private Integer sctermid;

    @ApiModelProperty(value = "班级ID")
    private Integer scclassid;

    @ApiModelProperty(value = "课程ID")
    private Integer sccourseid;

    @ApiModelProperty(value = "教师ID")
    private Integer scteacherid;

    @ApiModelProperty(value = "分数")
    private Double scscore;

    @ApiModelProperty(value = "状态")
    private Integer scstatus;

    @ApiModelProperty(value = "关键字：学生姓名/学号")
    private String qkey;
}