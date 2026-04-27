package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.ScoreinfoDto;
import com.demo.model.Scoreinfo;
import com.demo.model.Vwscores;
import com.demo.service.IScoreinfoService;
import com.demo.service.ScoreinfoService;
import com.demo.service.VwscoresService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/scoreinfo")
@Api(tags = "成绩管理")
public class ScoreinfoController {

    @Autowired
    private IScoreinfoService scoreinfoService;

    @Autowired
    private VwscoresService vwscoresService;

    @GetMapping("getlist")
    @ApiOperation("获取成绩分页列表")
    public ResponsePageResult<Vwscores> getlist(ScoreinfoDto query) {
        Page<Vwscores> pager = new Page<>();
        pager.setCurrent(query.getPageIndex());
        pager.setSize(query.getPageSize());

        LambdaQueryWrapper<Vwscores> wrapper = new LambdaQueryWrapper<>();

        wrapper.and(StringUtils.hasText(query.getQkey()), w ->
                w.like(Vwscores::getSno, query.getQkey())
                        .or()
                        .like(Vwscores::getSname, query.getQkey())
        );

        wrapper.eq(query.getSctermid() != null && query.getSctermid() > 0,
                Vwscores::getSctermid, query.getSctermid());

        wrapper.eq(query.getScclassid() != null && query.getScclassid() > 0,
                Vwscores::getScclassid, query.getScclassid());

        wrapper.eq(query.getSccourseid() != null && query.getSccourseid() > 0,
                Vwscores::getSccourseid, query.getSccourseid());

        wrapper.eq(query.getScteacherid() != null && query.getScteacherid() > 0,
                Vwscores::getScteacherid, query.getScteacherid());

        wrapper.eq(query.getScstatus() != null && query.getScstatus() >= 0,
                Vwscores::getScstatus, query.getScstatus());

        wrapper.orderByDesc(Vwscores::getScid);

        IPage<Vwscores> pageData = vwscoresService.page(pager, wrapper);
        return ResponsePageResult.PageResult(pageData);
    }

    @GetMapping("getbyid")
    @ApiOperation("根据ID获取成绩")
    public ResponseResult getbyid(@RequestParam Integer scid) {
        if (scid == null || scid <= 0) {
            return ResponseResult.Fail("成绩ID不能为空");
        }

        Scoreinfo data = scoreinfoService.getById(scid);
        if (data == null) {
            return ResponseResult.Fail("成绩不存在");
        }

        return ResponseResult.success("success", data);
    }

    @PostMapping("save")
    @ApiOperation("新增成绩")
    public ResponseResult save(@RequestBody Scoreinfo model) {
        if (model.getScstudentid() == null || model.getScstudentid() <= 0) {
            return ResponseResult.Fail("学生不能为空");
        }
        if (model.getSctermid() == null || model.getSctermid() <= 0) {
            return ResponseResult.Fail("学期不能为空");
        }
        if (model.getScclassid() == null || model.getScclassid() <= 0) {
            return ResponseResult.Fail("班级不能为空");
        }
        if (model.getSccourseid() == null || model.getSccourseid() <= 0) {
            return ResponseResult.Fail("课程不能为空");
        }

        if (model.getScscore() == null) {
            model.setScscore(java.math.BigDecimal.ZERO);
        }

        if (model.getScstatus() == null) {
            model.setScstatus(1);
        }

        if (model.getSccreatedate() == null) {
            model.setSccreatedate(LocalDateTime.now());
        }

        long count = scoreinfoService.count(
                new LambdaQueryWrapper<Scoreinfo>()
                        .eq(Scoreinfo::getScstudentid, model.getScstudentid())
                        .eq(Scoreinfo::getSctermid, model.getSctermid())
                        .eq(Scoreinfo::getScclassid, model.getScclassid())
                        .eq(Scoreinfo::getSccourseid, model.getSccourseid())
        );

        if (count > 0) {
            return ResponseResult.Fail("该学生在当前学期班级课程下的成绩已存在");
        }

        boolean res = scoreinfoService.save(model);
        return res ? ResponseResult.success("新增成功", null) : ResponseResult.Fail("新增失败");
    }

    @PostMapping("update")
    @ApiOperation("修改成绩")
    public ResponseResult update(@RequestBody Scoreinfo model) {
        if (model.getScid() == null || model.getScid() <= 0) {
            return ResponseResult.Fail("成绩ID不能为空");
        }
        if (model.getScstudentid() == null || model.getScstudentid() <= 0) {
            return ResponseResult.Fail("学生不能为空");
        }
        if (model.getSctermid() == null || model.getSctermid() <= 0) {
            return ResponseResult.Fail("学期不能为空");
        }
        if (model.getScclassid() == null || model.getScclassid() <= 0) {
            return ResponseResult.Fail("班级不能为空");
        }
        if (model.getSccourseid() == null || model.getSccourseid() <= 0) {
            return ResponseResult.Fail("课程不能为空");
        }

        Scoreinfo oldModel = scoreinfoService.getById(model.getScid());
        if (oldModel == null) {
            return ResponseResult.Fail("成绩记录不存在");
        }

        long count = scoreinfoService.count(
                new LambdaQueryWrapper<Scoreinfo>()
                        .ne(Scoreinfo::getScid, model.getScid())
                        .eq(Scoreinfo::getScstudentid, model.getScstudentid())
                        .eq(Scoreinfo::getSctermid, model.getSctermid())
                        .eq(Scoreinfo::getScclassid, model.getScclassid())
                        .eq(Scoreinfo::getSccourseid, model.getSccourseid())
        );

        if (count > 0) {
            return ResponseResult.Fail("该学生在当前学期班级课程下的成绩已存在");
        }

        if (model.getSccreatedate() == null) {
            model.setSccreatedate(oldModel.getSccreatedate());
        }

        boolean res = scoreinfoService.updateById(model);
        return res ? ResponseResult.success("修改成功", null) : ResponseResult.Fail("修改失败");
    }

    @PostMapping("delete")
    @ApiOperation("删除成绩")
    public ResponseResult delete(@RequestParam Integer scid) {
        if (scid == null || scid <= 0) {
            return ResponseResult.Fail("成绩ID不能为空");
        }

        boolean res = scoreinfoService.removeById(scid);
        return res ? ResponseResult.success("删除成功", null) : ResponseResult.Fail("删除失败");
    }


}