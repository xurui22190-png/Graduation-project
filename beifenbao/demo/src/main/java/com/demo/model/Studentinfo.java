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
 * 学生信息表
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Studentinfo对象", description="学生信息表")
public class Studentinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学生ID")
    @TableId(value = "sid", type = IdType.AUTO)
    private Integer sid;

    @ApiModelProperty(value = "学号")
    @TableField("sNo")
    private String sno;

    @ApiModelProperty(value = "姓名")
    @TableField("sName")
    private String sname;

    @ApiModelProperty(value = "性别")
    @TableField("sSex")
    private String ssex;

    @ApiModelProperty(value = "班级")
    @TableField("sClassId")
    private Integer sclassid;

    @ApiModelProperty(value = "电话")
    @TableField("sTel")
    private String stel;

    @ApiModelProperty(value = "身份证号")
    @TableField("sIdcard")
    private String sidcard;

    @ApiModelProperty(value = "家庭住址")
    @TableField("sAddress")
    private String saddress;

    @ApiModelProperty(value = "创建时间")
    @TableField("sCreateDate")
    private LocalDateTime screatedate;

    @ApiModelProperty(value = "关联账号ID")
    @TableField("sAccountId")
    private Integer saccountid;


}
