package com.demo.dto;

import com.demo.common.QueryItemModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TeachingDto extends QueryItemModel {

    private Integer tcTermId;
    private Integer tcClassId;
    private Integer tcCourseId;
    private Integer tcTeacherId;
    private String qkey;
}