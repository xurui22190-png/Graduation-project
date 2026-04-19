package com.demo.service;

import com.demo.common.ResponsePageResult;
import com.demo.dto.ScoreStatsDto;
import com.demo.vo.ScoreRangeVo;
import com.demo.vo.ScoreStatsListVo;
import com.demo.vo.ScoreSummaryVo;
import com.demo.vo.ScoreTrendVo;

import java.util.List;

public interface IScoreStatsService {

    ScoreSummaryVo getSummary(ScoreStatsDto dto);

    List<ScoreRangeVo> getRangeList(ScoreStatsDto dto);

    ResponsePageResult<ScoreStatsListVo> getList(ScoreStatsDto dto);

    List<ScoreTrendVo> getTrendList(ScoreStatsDto dto);
}