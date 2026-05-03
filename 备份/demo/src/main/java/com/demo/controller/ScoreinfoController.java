package com.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.ScoreExcelDto;
import com.demo.dto.ScoreinfoDto;
import com.demo.mapper.StudentinfoMapper;
import com.demo.model.Scoreinfo;
import com.demo.model.Studentinfo;
import com.demo.model.Vwscores;
import com.demo.service.IScoreinfoService;
import com.demo.service.ScoreinfoService;
import com.demo.service.VwscoresService;
import com.demo.service.impl.ScoreinfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/scoreinfo")
@Api(tags = "成绩管理")
public class ScoreinfoController {

    @Autowired
    private IScoreinfoService scoreinfoService;

    @Autowired
    private VwscoresService vwscoresService;
    @Autowired
    private StudentinfoMapper studentinfoMapper;


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
    // 在 Controller 里，确保它是这样调用 Service 的：
    @PostMapping("saveScoreList")
    @ApiOperation("教师端批量保存并智能拆解成绩")
    public ResponseResult saveScoreList(@RequestBody List<Scoreinfo> list) {
        if (list == null || list.isEmpty()) {
            return ResponseResult.Fail("提交的成绩数据为空");
        }
        // 直接调用接口里的方法，优雅且规范！
        return scoreinfoService.saveTeacherScores(list);
    }
    @PostMapping("importScores")
    @ApiOperation("Excel批量导入成绩并触发AI拆解")
    public ResponseResult importScores(@RequestParam("file") MultipartFile file,
                                       @RequestParam("sctermid") Integer sctermid,
                                       @RequestParam("scclassid") Integer scclassid,
                                       @RequestParam("sccourseid") Integer sccourseid) {
        if (file.isEmpty()) {
            return ResponseResult.Fail("上传的文件为空");
        }

        try {
            // 1. 使用 EasyExcel 同步读取 Excel 数据
            List<ScoreExcelDto> excelDataList = EasyExcel.read(file.getInputStream())
                    .head(ScoreExcelDto.class)
                    .sheet()
                    .doReadSync();

            if (excelDataList == null || excelDataList.isEmpty()) {
                return ResponseResult.Fail("Excel中没有读取到数据");
            }

            List<Scoreinfo> scoreinfoList = new ArrayList<>();

            // 2. 遍历 Excel 数据，转换为我们数据库需要的 Scoreinfo 实体
            for (ScoreExcelDto excelData : excelDataList) {
                if (excelData.getSno() == null || excelData.getScscore() == null) {
                    continue; // 跳过空行
                }

                // 通过学号查询真实的 sid (这里需要用到你的 studentinfoMapper，如果没有注入请在Controller顶层注入)
                // 假设你的学生表 Mapper 是 studentinfoMapper，实体是 Studentinfo
                Studentinfo student = studentinfoMapper.selectOne(
                        new LambdaQueryWrapper<Studentinfo>().eq(Studentinfo::getSno, excelData.getSno())
                );

                if (student != null) {
                    Scoreinfo score = new Scoreinfo();
                    score.setScstudentid(student.getSid()); // 真实的系统学生ID
                    score.setSctermid(sctermid);
                    score.setScclassid(scclassid);
                    score.setSccourseid(sccourseid);
                    score.setScscore(java.math.BigDecimal.valueOf(excelData.getScscore()));
                    score.setScstatus(1); // 标记为已录入
                    score.setSccreatedate(LocalDateTime.now());

                    scoreinfoList.add(score);
                }
            }

            // 3. 🚀 见证奇迹的时刻：把组装好的列表，交给我们的魔法引擎去逆向推演和 AI 拆解！
            return ((ScoreinfoServiceImpl) scoreinfoService).saveTeacherScores(scoreinfoList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.Fail("Excel导入失败，请检查模板格式：" + e.getMessage());
        }
    }


}