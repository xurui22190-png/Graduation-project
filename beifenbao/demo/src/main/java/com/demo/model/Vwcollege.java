package com.demo.model;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Vwcollege对象", description="VIEW")
public class Vwcollege implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学院ID")
    @TableField("CID")
    private Integer cid;

    @ApiModelProperty(value = "院系名称")
    @TableField("cName")
    private String cname;

    @ApiModelProperty(value = "院系编码")
    @TableField("cCode")
    private String ccode;

    @ApiModelProperty(value = "父级ID")
    @TableField("cParentId")
    private Integer cparentid;

    @ApiModelProperty(value = "院系名称")
    @TableField("cNamet")
    private String cnamet;

    @ApiModelProperty(value = "院系编码")
    @TableField("cCodet")
    private String ccodet;

    @ApiModelProperty(value = "父级ID")
    @TableField("cParentIdt")
    private Integer cparentidt;


}
