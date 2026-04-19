package com.demo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TeacherScoreVo {

    private Integer scid;
    private Integer scstudentid;
    private Integer sctermid;
    private Integer scclassid;
    private Integer sccourseid;
    private Integer scteacherid;

    private BigDecimal scscore;
    private Integer scstatus;
    private Date sccreatedate;

    private String sno;
    private String sname;
    private String cname;
    private String crname;
    private String yall;
    private String tname;
}