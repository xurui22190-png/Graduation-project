package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "教师查询对象")
public class TeacherinfoDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "教师ID")
    private Integer tid;

    @ApiModelProperty(value = "工号")
    private String tno;

    @ApiModelProperty(value = "姓名")
    private String tname;

    @ApiModelProperty(value = "性别")
    private String tsex;

    @ApiModelProperty(value = "电话")
    private String ttel;

    @ApiModelProperty(value = "教育程度")
    private String tedulevel;

    @ApiModelProperty(value = "毕业院校")
    private String tschool;

    @ApiModelProperty(value = "现居地址")
    private String taddress;

    @ApiModelProperty(value = "关联账号ID")
    private Integer taccountid;

    @ApiModelProperty(value = "所属院系ID")
    private Integer tcollegeid;
}