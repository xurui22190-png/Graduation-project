package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "学生查询对象")
public class StudentinfoDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "学生ID")
    private Integer sid;

    @ApiModelProperty(value = "学号")
    private String sno;

    @ApiModelProperty(value = "姓名/学号关键字")
    private String qkey;

    @ApiModelProperty(value = "姓名")
    private String sname;

    @ApiModelProperty(value = "性别")
    private String ssex;

    @ApiModelProperty(value = "班级ID")
    private Integer sclassid;

    @ApiModelProperty(value = "电话")
    private String stel;

    @ApiModelProperty(value = "身份证号")
    private String sidcard;

    @ApiModelProperty(value = "家庭住址")
    private String saddress;

    @ApiModelProperty(value = "账号ID")
    private Integer saccountid;
}