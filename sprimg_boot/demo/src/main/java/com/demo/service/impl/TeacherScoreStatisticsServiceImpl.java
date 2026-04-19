package com.demo.service.impl;

import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeacherScoreStatisticsDto;
import com.demo.mapper.TeacherScoreStatisticsMapper;
import com.demo.service.ITeacherScoreStatisticsService;
import com.demo.vo.TeacherScoreStatisticsVo;
import com.demo.vo.TeacherScoreSummaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherScoreStatisticsServiceImpl implements ITeacherScoreStatisticsService {

    @Autowired
    private TeacherScoreStatisticsMapper teacherScoreStatisticsMapper;

    @Override
    public ResponsePageResult getList(TeacherScoreStatisticsDto dto, Integer teacherId) {
        if (dto.getPageIndex() < 1) {
            dto.setPageIndex(1);
        }
        if (dto.getPageSize() < 1) {
            dto.setPageSize(10);
        }

        dto.setPageStart((dto.getPageIndex() - 1) * dto.getPageSize());

        List<TeacherScoreStatisticsVo> list = teacherScoreStatisticsMapper.getList(dto, teacherId);
        Long total = teacherScoreStatisticsMapper.getCount(dto, teacherId);

        ResponsePageResult result = new ResponsePageResult();
        result.set_code(200);
        result.set_msg("success");
        result.setData(list);
        result.setTotalRecord(total == null ? 0 : total);
        result.setPageIndex(dto.getPageIndex());
        result.setTotalPage(total == null || dto.getPageSize() == 0 ? 0 : (total + dto.getPageSize() - 1) / dto.getPageSize());

        return result;
    }

    @Override
    public ResponseResult getSummary(TeacherScoreStatisticsDto dto, Integer teacherId) {
        TeacherScoreSummaryVo vo = teacherScoreStatisticsMapper.getSummary(dto, teacherId);
        if (vo == null) {
            vo = new TeacherScoreSummaryVo();
            vo.setTotalCount(0);
            vo.setAvgScore(0.0);
            vo.setMaxScore(0.0);
            vo.setMinScore(0.0);
            vo.setPassRate(0.0);
            vo.setGoodRate(0.0);
        }
        return ResponseResult.success("success", vo);
    }

    @Override
    public ResponseResult getChart(TeacherScoreStatisticsDto dto, Integer teacherId) {
        Map<String, Object> map = new HashMap<>();
        map.put("rangeData", teacherScoreStatisticsMapper.getRangeData(dto, teacherId));
        map.put("classAvgData", teacherScoreStatisticsMapper.getClassAvgData(dto, teacherId));
        map.put("trendData", teacherScoreStatisticsMapper.getTrendData(dto, teacherId));
        return ResponseResult.success("success", map);
    }

    @Override
    public ResponseResult getTermList(Integer teacherId) {
        return ResponseResult.success("success", teacherScoreStatisticsMapper.getTermList(teacherId));
    }

    @Override
    public ResponseResult getClassList(Integer teacherId) {
        return ResponseResult.success("success", teacherScoreStatisticsMapper.getClassList(teacherId));
    }

    @Override
    public ResponseResult getCourseList(Integer teacherId) {
        return ResponseResult.success("success", teacherScoreStatisticsMapper.getCourseList(teacherId));
    }
}