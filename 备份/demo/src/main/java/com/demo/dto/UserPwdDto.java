package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="修改密码", description="")
public class UserPwdDto implements Serializable {


    @ApiModelProperty(value = "新密码")
    private String upwd;

    @ApiModelProperty(value = "原密码")
    private String uoldpwd;


}