package com.demo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.dto.StudentScoreDto;
import com.demo.mapper.StudentScoreMapper;
import com.demo.service.IStudentScoreService;
import com.demo.vo.StudentScoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentScoreServiceImpl implements IStudentScoreService {

    @Autowired
    private StudentScoreMapper studentScoreMapper;

    @Override
    public ResponsePageResult getStudentScoreList(StudentScoreDto dto) {
        long pageIndex = dto.getPageIndex() <= 0 ? 1L : dto.getPageIndex();
        long pageSize = dto.getPageSize() <= 0 ? 10L : dto.getPageSize();
        Page<StudentScoreVo> page = new Page<>(pageIndex, pageSize);
        IPage<StudentScoreVo> result = studentScoreMapper.getStudentScoreList(page, dto);
        return ResponsePageResult.PageResult(result);
    }
}