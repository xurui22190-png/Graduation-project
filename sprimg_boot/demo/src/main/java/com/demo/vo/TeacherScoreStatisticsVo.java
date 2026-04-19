package com.demo.vo;

import lombok.Data;

@Data
public class TeacherScoreStatisticsVo {
    private Integer scid;
    private Integer sid;
    private String sno;
    private String sname;
    private String ssex;
    private Integer sclassid;
    private String classname;
    private String classcode;
    private String yyear;
    private String yterm;
    private String yall;
    private Integer sccourseid;
    private String crcode;
    private String crname;
    private Double scscore;
    private Integer scstatus;
    private String screatedate;
}