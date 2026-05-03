package com.demo.mapper;

import com.demo.dto.ScoreStatsDto;
import com.demo.vo.ScoreRangeVo;
import com.demo.vo.ScoreStatsListVo;
import com.demo.vo.ScoreSummaryVo;
import com.demo.vo.ScoreTrendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScoreStatsMapper {

    ScoreSummaryVo getSummary(@Param("dto") ScoreStatsDto dto);

    List<ScoreRangeVo> getRangeList(@Param("dto") ScoreStatsDto dto);

    List<ScoreStatsListVo> getList(@Param("dto") ScoreStatsDto dto);
    List<ScoreTrendVo> getTrendList(@Param("dto") ScoreStatsDto dto);
}