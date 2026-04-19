package com.demo.dto;

import com.demo.common.QueryItemModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TeacherScoreStatisticsDto extends QueryItemModel {
    private Integer tctermid;
    private Integer tcclassid;
    private Integer tccourseid;

    private Integer pageStart;
}