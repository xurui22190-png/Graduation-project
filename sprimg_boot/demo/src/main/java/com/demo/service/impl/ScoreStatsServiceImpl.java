package com.demo.service.impl;

import com.demo.common.ResponsePageResult;
import com.demo.dto.ScoreStatsDto;
import com.demo.mapper.ScoreStatsMapper;
import com.demo.service.IScoreStatsService;
import com.demo.vo.ScoreRangeVo;
import com.demo.vo.ScoreStatsListVo;
import com.demo.vo.ScoreSummaryVo;
import com.demo.vo.ScoreTrendVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class ScoreStatsServiceImpl implements IScoreStatsService {

    @Resource
    private ScoreStatsMapper scoreStatsMapper;

    @Override
    public ScoreSummaryVo getSummary(ScoreStatsDto dto) {
        return scoreStatsMapper.getSummary(dto);
    }

    @Override
    public List<ScoreRangeVo> getRangeList(ScoreStatsDto dto) {
        return scoreStatsMapper.getRangeList(dto);
    }

    @Override
    public List<ScoreTrendVo> getTrendList(ScoreStatsDto dto) {
        return scoreStatsMapper.getTrendList(dto);
    }

    @Override
    public ResponsePageResult<ScoreStatsListVo> getList(ScoreStatsDto dto) {
        List<ScoreStatsListVo> allList = scoreStatsMapper.getList(dto);
        if (allList == null) {
            allList = Collections.emptyList();
        }

        int pageIndex = dto.getPageIndex() < 1 ? 1 : dto.getPageIndex();
        int pageSize = dto.getPageSize() < 1 ? 10 : dto.getPageSize();

        int total = allList.size();
        int fromIndex = (pageIndex - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<ScoreStatsListVo> pageList;
        if (fromIndex >= total) {
            pageList = Collections.emptyList();
        } else {
            pageList = allList.subList(fromIndex, toIndex);
        }

        ResponsePageResult<ScoreStatsListVo> result = new ResponsePageResult<>();
        result.set_code(200);
        result.set_msg("success");
        result.setData(pageList);
        result.setTotalRecord(total);
        result.setPageIndex(pageIndex);
        result.setTotalPage((long) Math.ceil(total * 1.0 / pageSize));

        return result;
    }
}