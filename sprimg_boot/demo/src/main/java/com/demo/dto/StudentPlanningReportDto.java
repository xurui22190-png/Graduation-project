package com.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class StudentPlanningReportDto {
    private Integer studentId;
    private String studentName;
    private String studentNo;
    private String intent;
    private Map<String, Double> dimensions = new LinkedHashMap<>();
    private ScoreSummary scoreSummary = new ScoreSummary();
    private List<CourseScore> courses = new ArrayList<>();

    @Data
    public static class ScoreSummary {
        private Double avgScore;
        private Double passRate;
        private Integer courseCount;
        private Double maxScore;
        private Double minScore;
        private List<String> strongCourses = new ArrayList<>();
        private List<String> weakCourses = new ArrayList<>();
    }

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
