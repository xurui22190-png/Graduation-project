package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Userinfo对象", description="用户信息表")
public class Userinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

    @ApiModelProperty(value = "账号")
    @TableField("uLog")
    private String ulog;

    @ApiModelProperty(value = "密码")
    @TableField("uPwd")
    private String upwd;

    @ApiModelProperty(value = "姓名")
    @TableField("uName")
    private String uname;

    @ApiModelProperty(value = "性别")
    @TableField("uSex")
    private String usex;

    @ApiModelProperty(value = "头像")
    @TableField("uPhoto")
    private String uphoto;

    @ApiModelProperty(value = "角色(1:学生 2:教师 3:管理员)")
    @TableField("uRole")
    private Integer urole;

    @ApiModelProperty(value = "注册时间")
    @TableField("uCreateDate")
    private LocalDateTime ucreatedate;


}
