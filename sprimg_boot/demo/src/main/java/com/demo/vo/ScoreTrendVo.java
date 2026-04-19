package com.demo.vo;

import lombok.Data;

@Data
public class ScoreTrendVo {

    private Integer yid;
    private String termname;

    private Double avgscore;
    private Double passrate;
    private Double excellentrate;
}