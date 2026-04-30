package com.demo.vo;

import lombok.Data;

@Data
public class ScoreSummaryVo {

    private Integer totalcount;      // 成绩总数
    private Integer studentcount;    // 学生人数
    private Double avgscore;         // 平均分
    private Double maxscore;         // 最高分
    private Double minscore;         // 最低分
    private Double passrate;         // 及格率
    private Double excellentrate;    // 优秀率
}