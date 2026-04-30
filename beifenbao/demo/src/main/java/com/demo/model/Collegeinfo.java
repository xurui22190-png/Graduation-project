package com.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 院系信息表
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Collegeinfo对象", description="院系信息表")
public class Collegeinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学院ID")
    @TableId(value = "CID", type = IdType.AUTO)
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


}
