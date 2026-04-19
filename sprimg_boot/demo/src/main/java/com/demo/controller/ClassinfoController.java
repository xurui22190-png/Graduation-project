package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.ClassinfoDto;
import com.demo.model.Classinfo;
import com.demo.model.Vwclass;
import com.demo.service.ClassinfoService;
import com.demo.service.VwclassService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classinfo")
@Api(tags = "班级管理")
public class ClassinfoController {

    @Autowired
    private ClassinfoService classinfoService;

    @Autowired
    private VwclassService vwclassService;

    @GetMapping("getlist")
    @ApiOperation("获取班级分页列表")
    public ResponsePageResult<Vwclass> getlist(ClassinfoDto query) {

        Page<Vwclass> pager = new Page<>();
        pager.setCurrent(query.getPageIndex() <= 0 ? 1 : query.getPageIndex());
        pager.setSize(query.getPageSize() <= 0 ? 10 : query.getPageSize());

        LambdaQueryWrapper<Vwclass> wrapper = new LambdaQueryWrapper<>();

        if (query.getCmajorid() != null && query.getCmajorid() > 0) {
            wrapper.eq(Vwclass::getCmajorid, query.getCmajorid());
        }

        if (StringUtils.hasText(query.getCgrade())) {
            wrapper.like(Vwclass::getCgrade, query.getCgrade());
        }

        if (StringUtils.hasText(query.getQkey())) {
            wrapper.and(w -> w.like(Vwclass::getCname, query.getQkey())
                    .or()
                    .like(Vwclass::getCcode, query.getQkey())
                    .or()
                    .like(Vwclass::getMname, query.getQkey())
                    .or()
                    .like(Vwclass::getCollegename, query.getQkey()));
        }

        wrapper.orderByDesc(Vwclass::getCid);

        IPage<Vwclass> pageResult = vwclassService.page(pager, wrapper);
        return ResponsePageResult.PageResult(pageResult);
    }

    @GetMapping("getall")
    @ApiOperation("获取全部班级")
    public ResponseResult getall() {
        List<Vwclass> list = vwclassService.list(
                new LambdaQueryWrapper<Vwclass>().orderByDesc(Vwclass::getCid)
        );
        return ResponseResult.success("success", list);
    }

    @GetMapping("getmodel")
    @ApiOperation("获取班级详情")
    public ResponseResult getmodel(Integer id) {
        if (id == null) {
            return ResponseResult.Fail("班级ID不能为空");
        }

        Classinfo model = classinfoService.getById(id);
        if (model == null) {
            return ResponseResult.Fail("班级不存在");
        }

        return ResponseResult.success("success", model);
    }

    @PostMapping("add")
    @ApiOperation("新增班级")
    public ResponseResult add(@RequestBody ClassinfoDto dto) {
        if (dto.getCmajorid() == null || dto.getCmajorid() <= 0) {
            return ResponseResult.Fail("请选择所属专业");
        }
        if (!StringUtils.hasText(dto.getCgrade())) {
            return ResponseResult.Fail("请输入年级");
        }
        if (!StringUtils.hasText(dto.getCname())) {
            return ResponseResult.Fail("请输入班级名称");
        }
        if (!StringUtils.hasText(dto.getCcode())) {
            return ResponseResult.Fail("请输入班级编号");
        }

        LambdaQueryWrapper<Classinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Classinfo::getCcode, dto.getCcode());
        long count = classinfoService.count(wrapper);
        if (count > 0) {
            return ResponseResult.Fail("班级编号已存在");
        }

        Classinfo model = new Classinfo();
        model.setCmajorid(dto.getCmajorid());
        model.setCgrade(dto.getCgrade());
        model.setCname(dto.getCname());
        model.setCcode(dto.getCcode());

        boolean flag = classinfoService.save(model);
        if (flag) {
            return ResponseResult.success("新增成功", model);
        }
        return ResponseResult.Fail("新增失败");
    }

    @PostMapping("update")
    @ApiOperation("修改班级")
    public ResponseResult update(@RequestBody ClassinfoDto dto) {
        if (dto.getCid() == null || dto.getCid() <= 0) {
            return ResponseResult.Fail("班级ID不能为空");
        }
        if (dto.getCmajorid() == null || dto.getCmajorid() <= 0) {
            return ResponseResult.Fail("请选择所属专业");
        }
        if (!StringUtils.hasText(dto.getCgrade())) {
            return ResponseResult.Fail("请输入年级");
        }
        if (!StringUtils.hasText(dto.getCname())) {
            return ResponseResult.Fail("请输入班级名称");
        }
        if (!StringUtils.hasText(dto.getCcode())) {
            return ResponseResult.Fail("请输入班级编号");
        }

        Classinfo oldModel = classinfoService.getById(dto.getCid());
        if (oldModel == null) {
            return ResponseResult.Fail("班级不存在");
        }

        LambdaQueryWrapper<Classinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Classinfo::getCcode, dto.getCcode())
                .ne(Classinfo::getCid, dto.getCid());

        long count = classinfoService.count(wrapper);
        if (count > 0) {
            return ResponseResult.Fail("班级编号已存在");
        }

        Classinfo model = new Classinfo();
        model.setCid(dto.getCid());
        model.setCmajorid(dto.getCmajorid());
        model.setCgrade(dto.getCgrade());
        model.setCname(dto.getCname());
        model.setCcode(dto.getCcode());

        boolean flag = classinfoService.updateById(model);
        if (flag) {
            return ResponseResult.success("修改成功", model);
        }
        return ResponseResult.Fail("修改失败");
    }

    @PostMapping("delete")
    @ApiOperation("删除班级")
    public ResponseResult delete(@RequestParam Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.Fail("班级ID不能为空");
        }

        Classinfo model = classinfoService.getById(id);
        if (model == null) {
            return ResponseResult.Fail("班级不存在");
        }

        boolean flag = classinfoService.removeById(id);
        if (flag) {
            return ResponseResult.success("删除成功", null);
        }
        return ResponseResult.Fail("删除失败");
    }
}