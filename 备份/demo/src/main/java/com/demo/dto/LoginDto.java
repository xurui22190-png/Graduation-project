package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="登录对象", description="")
public class LoginDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "账号")
    private String ulog;

    @ApiModelProperty(value = "密码")
    private String upwd;

    @ApiModelProperty(value = "角色(1:学生 2:教师 3:管理员)")
    private Integer urole;
}