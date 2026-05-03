package com.demo.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TeacherStudentScoreVo {
    private Integer sid;
    private String sno;
    private String sname;
    private Integer sclassid;
    private String cname;

    private Integer scid;
    private BigDecimal scscore;
    private Integer scstatus;
}