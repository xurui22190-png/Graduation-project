package com.demo.vo;

import lombok.Data;

@Data
public class ScoreStatsListVo {

    private Integer crid;
    private String crname;

    private Integer cid;
    private String cname;

    private Integer totalnum;
    private Double avgscore;
    private Double maxscore;
    private Double minscore;
    private Double passrate;
    private Double excellentrate;
}