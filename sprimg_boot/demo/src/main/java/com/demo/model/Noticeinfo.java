package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("noticeinfo")
@ApiModel(value="Noticeinfo对象", description="公告信息表")
public class Noticeinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公告ID")
    @TableId(value = "nId", type = IdType.AUTO)
    private Integer nid;

    @ApiModelProperty(value = "公告标题")
    @TableField("nTitle")
    private String ntitle;

    @ApiModelProperty(value = "公告内容")
    @TableField("nContent")
    private String ncontent;

    @ApiModelProperty(value = "公告类型")
    @TableField("nType")
    private String ntype;

    @ApiModelProperty(value = "是否置顶：0否 1是")
    @TableField("nTop")
    private Integer ntop;

    @ApiModelProperty(value = "状态：0下线 1发布")
    @TableField("nState")
    private Integer nstate;

    @ApiModelProperty(value = "创建人用户ID")
    @TableField("nCreateUid")
    private Integer ncreateuid;

    @ApiModelProperty(value = "创建人姓名")
    @TableField("nCreateName")
    private String ncreatename;

    @ApiModelProperty(value = "创建时间")
    @TableField("nCreateTime")
    private Date ncreatetime;

    @ApiModelProperty(value = "更新时间")
    @TableField("nUpdateTime")
    private Date nupdatetime;

    @ApiModelProperty(value = "是否删除：0否 1是")
    @TableField("nIsDelete")
    private Integer nisdelete;
}