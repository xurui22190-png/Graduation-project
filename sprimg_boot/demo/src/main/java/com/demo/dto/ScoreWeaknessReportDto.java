package com.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScoreWeaknessReportDto {
    private String studentName;
    private String studentNo;
    private Double avgScore;
    private Double passRate;
    private Integer courseCount;
    private Double maxScore;
    private Double minScore;
    private List<CourseScore> courses = new ArrayList<>();

    @Data
    public static class CourseScore {
        private String term;
        private String courseName;
        private Double score;
        private Double regularScore;
        private Double testScore;
        private Double examScore;
    }
}
