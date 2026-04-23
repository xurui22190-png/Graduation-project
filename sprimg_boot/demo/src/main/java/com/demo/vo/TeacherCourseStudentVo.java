package com.demo.vo;

import lombok.Data;

@Data
public class TeacherCourseStudentVo {
    private Integer sid;
    private String sno;
    private String sname;
    private String ssex;
    private Integer sclassid;
    private String cname;
    private Integer cmajorid;
    private String mname;
    private String stel;
}