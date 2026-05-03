package com.demo.dto;

import com.demo.common.QueryItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 课程信息查询对象
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="课程查询对象")
public class CourseinfoDto extends QueryItemModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程ID")
    private Integer crid;

    @ApiModelProperty(value = "课程代码")
    private String crcode;

    @ApiModelProperty(value = "课程名称")
    private String crname;

    @ApiModelProperty(value = "隶属专业ID")
    private Integer crmajorid;

    @ApiModelProperty(value = "关键字")
    private String qkey;

}