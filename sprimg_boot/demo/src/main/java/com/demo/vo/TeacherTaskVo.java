package com.demo.vo;

import lombok.Data;

@Data
public class TeacherTaskVo {
    private Integer tcid;
    private Integer tctermid;
    private Integer tcclassid;
    private Integer tccourseid;
    private Integer tcteacherid;

    private String yall;
    private String cname;
    private String crname;
    private String tname;
}