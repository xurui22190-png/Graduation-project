package com.demo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TeacherStudentScoreSaveDto implements Serializable {

    private Integer tctermid;
    private Integer tcclassid;
    private Integer tccourseid;

    private List<ScoreItem> scoreList;

    @Data
    public static class ScoreItem implements Serializable {
        private Integer scid;
        private Integer sid;
        private Double scscore;
        private Integer scstatus;
    }
}