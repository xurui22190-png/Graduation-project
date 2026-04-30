package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author xyh
 * @since 2026-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="用户对象", description="")
public class UserinfoDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "账号")
    private String ulog;

    @ApiModelProperty(value = "姓名")
    private String uname;

    @ApiModelProperty(value = "性别")
    private String usex;


    @ApiModelProperty(value = "角色(1:学生 2:教师 3:管理员)")
    private Integer urole;

    @ApiModelProperty(value = "头像")
    private String uphoto;

    @ApiModelProperty(value = "注册时间")
    private LocalDateTime ucreatedate;
}
