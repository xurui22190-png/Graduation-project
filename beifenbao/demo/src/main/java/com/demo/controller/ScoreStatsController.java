package com.demo.controller;

import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.ScoreStatsDto;
import com.demo.service.IScoreStatsService;
import com.demo.vo.ScoreRangeVo;
import com.demo.vo.ScoreStatsListVo;
import com.demo.vo.ScoreSummaryVo;
import com.demo.vo.ScoreTrendVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/scorestats")
public class ScoreStatsController {

    @Resource
    private IScoreStatsService scoreStatsService;

    @GetMapping("/summary")
    public ResponseResult getSummary(ScoreStatsDto dto) {
        ScoreSummaryVo data = scoreStatsService.getSummary(dto);
        return ResponseResult.success("success", data);
    }

    @GetMapping("/range")
    public ResponseResult getRange(ScoreStatsDto dto) {
        List<ScoreRangeVo> data = scoreStatsService.getRangeList(dto);
        return ResponseResult.success("success", data);
    }

    @GetMapping("/list")
    public ResponsePageResult<ScoreStatsListVo> getList(ScoreStatsDto dto) {
        return scoreStatsService.getList(dto);
    }

    @GetMapping("/trend")
    public ResponseResult getTrend(ScoreStatsDto dto) {
        List<ScoreTrendVo> data = scoreStatsService.getTrendList(dto);
        return ResponseResult.success("success", data);
    }
}