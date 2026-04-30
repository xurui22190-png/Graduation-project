package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.CourseinfoDto;
import com.demo.model.Courseinfo;
import com.demo.service.CourseinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * <p>
 * 课程信息表 前端控制器
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
@RestController
@RequestMapping("/api/courseinfo")
@Api(tags = "课程管理")
public class CourseinfoController {

    @Autowired
    private CourseinfoService courseinfoService;

    @GetMapping("getlist")
    @ApiOperation("获取课程分页列表")
    public ResponsePageResult<Courseinfo> getlist(CourseinfoDto query) {
        Page<Courseinfo> pager = new Page<>();
        pager.setCurrent(query.getPageIndex());
        pager.setSize(query.getPageSize());

        LambdaQueryWrapper<Courseinfo> wrapper = new LambdaQueryWrapper<>();

        if (query.getQkey() != null && !"".equals(query.getQkey().trim())) {
            wrapper.and(w -> w
                    .like(Courseinfo::getCrcode, query.getQkey().trim())
                    .or()
                    .like(Courseinfo::getCrname, query.getQkey().trim()));
        }

        if (query.getCrmajorid() != null && query.getCrmajorid() > 0) {
            wrapper.eq(Courseinfo::getCrmajorid, query.getCrmajorid());
        }

        wrapper.orderByDesc(Courseinfo::getCrid);

        Page<Courseinfo> pagedata = courseinfoService.page(pager, wrapper);
        return ResponsePageResult.PageResult(pagedata);
    }

    @GetMapping("getmodel")
    @ApiOperation("获取课程详情")
    public ResponseResult getmodel(Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.Fail("参数错误");
        }

        Courseinfo model = courseinfoService.getById(id);
        if (model == null) {
            return ResponseResult.Fail("课程不存在");
        }

        return ResponseResult.success("success", model);
    }

    @PostMapping("add")
    @ApiOperation("新增课程")
    public ResponseResult add(@RequestBody Courseinfo model) {
        if (model == null) {
            return ResponseResult.Fail("提交数据不能为空");
        }
        if (model.getCrcode() == null || "".equals(model.getCrcode().trim())) {
            return ResponseResult.Fail("课程代码不能为空");
        }
        if (model.getCrname() == null || "".equals(model.getCrname().trim())) {
            return ResponseResult.Fail("课程名称不能为空");
        }
        if (model.getCrmajorid() == null || model.getCrmajorid() <= 0) {
            return ResponseResult.Fail("请选择所属专业");
        }

        LambdaQueryWrapper<Courseinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Courseinfo::getCrcode, model.getCrcode().trim());
        Courseinfo oldModel = courseinfoService.getOne(wrapper);

        if (oldModel != null) {
            return ResponseResult.Fail("课程代码已存在");
        }

        model.setCrcode(model.getCrcode().trim());
        model.setCrname(model.getCrname().trim());

        boolean res = courseinfoService.save(model);
        if (res) {
            return ResponseResult.success("新增成功", model);
        }
        return ResponseResult.Fail("新增失败");
    }

    @PostMapping("update")
    @ApiOperation("修改课程")
    public ResponseResult update(@RequestBody Courseinfo model) {
        if (model == null || model.getCrid() == null || model.getCrid() <= 0) {
            return ResponseResult.Fail("参数错误");
        }
        if (model.getCrcode() == null || "".equals(model.getCrcode().trim())) {
            return ResponseResult.Fail("课程代码不能为空");
        }
        if (model.getCrname() == null || "".equals(model.getCrname().trim())) {
            return ResponseResult.Fail("课程名称不能为空");
        }
        if (model.getCrmajorid() == null || model.getCrmajorid() <= 0) {
            return ResponseResult.Fail("请选择所属专业");
        }

        Courseinfo data = courseinfoService.getById(model.getCrid());
        if (data == null) {
            return ResponseResult.Fail("课程不存在");
        }

        LambdaQueryWrapper<Courseinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Courseinfo::getCrcode, model.getCrcode().trim());
        Courseinfo oldModel = courseinfoService.getOne(wrapper);

        if (oldModel != null && !Objects.equals(oldModel.getCrid(), model.getCrid())) {
            return ResponseResult.Fail("课程代码已存在");
        }

        model.setCrcode(model.getCrcode().trim());
        model.setCrname(model.getCrname().trim());

        boolean res = courseinfoService.updateById(model);
        if (res) {
            return ResponseResult.success("修改成功", model);
        }
        return ResponseResult.Fail("修改失败");
    }

    @GetMapping("getall")
    @ApiOperation("获取全部课程")
    public ResponseResult getall() {
        return ResponseResult.success("success", courseinfoService.list());
    }

    @GetMapping("delete")
    @ApiOperation("删除课程")
    public ResponseResult delete(Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.Fail("参数错误");
        }

        Courseinfo model = courseinfoService.getById(id);
        if (model == null) {
            return ResponseResult.Fail("课程不存在");
        }

        boolean res = courseinfoService.removeById(id);
        if (res) {
            return ResponseResult.success("删除成功", null);
        }
        return ResponseResult.Fail("删除失败");
    }
}