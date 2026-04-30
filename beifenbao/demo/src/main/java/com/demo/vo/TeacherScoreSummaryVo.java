package com.demo.vo;

import lombok.Data;

@Data
public class TeacherScoreSummaryVo {
    private Integer totalCount;
    private Double avgScore;
    private Double maxScore;
    private Double minScore;
    private Double passRate;
    private Double goodRate;
}