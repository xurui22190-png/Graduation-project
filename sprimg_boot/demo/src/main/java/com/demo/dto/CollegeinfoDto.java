package com.demo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@ApiModel(value="院系对象")
public class CollegeinfoDto extends QueryItemModel implements Serializable {


    @ApiModelProperty(value = "学院ID")
    private Integer cid;

    @ApiModelProperty(value = "院系名称")
    private String cname;

    @ApiModelProperty(value = "院系编码")
    private String ccode;

    @ApiModelProperty(value = "父级ID")
    private Integer cparentid;


}
