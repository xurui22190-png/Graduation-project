package com.demo.service;

import com.demo.common.ResponsePageResult;
import com.demo.dto.StudentScoreDto;

public interface IStudentScoreService {
    ResponsePageResult getStudentScoreList(StudentScoreDto dto);
}