package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="公告查询对象")
public class NoticeinfoDto extends QueryItemModel implements Serializable {

    @ApiModelProperty(value = "公告ID")
    private Integer nid;

    @ApiModelProperty(value = "公告标题")
    private String ntitle;

    @ApiModelProperty(value = "公告内容")
    private String ncontent;

    @ApiModelProperty(value = "公告类型")
    private String ntype;

    @ApiModelProperty(value = "是否置顶：0否 1是")
    private Integer ntop;

    @ApiModelProperty(value = "状态：0下线 1发布")
    private Integer nstate;

    @ApiModelProperty(value = "创建人用户ID")
    private Integer ncreateuid;

    @ApiModelProperty(value = "创建人姓名")
    private String ncreatename;
}