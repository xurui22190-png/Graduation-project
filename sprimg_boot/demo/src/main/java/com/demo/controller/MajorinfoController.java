package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.MajorinfoDto;
import com.demo.model.Majorinfo;
import com.demo.service.MajorinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majorinfo")
@Api(tags = "专业信息")
public class MajorinfoController {

    @Autowired
    private MajorinfoService majorinfoService;

    @GetMapping("/getall")
    @ApiOperation("获取全部专业")
    public ResponseResult getall() {
        LambdaQueryWrapper<Majorinfo> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Majorinfo::getMid);
        List<Majorinfo> list = majorinfoService.list(lqw);
        return ResponseResult.success("获取成功", list);
    }

    @GetMapping("/getlist")
    @ApiOperation("获取专业列表")
    public ResponsePageResult<Majorinfo> getlist(MajorinfoDto query) {
        Page<Majorinfo> pager = new Page<>();
        pager.setCurrent(query.getPageIndex());
        pager.setSize(query.getPageSize());

        LambdaQueryWrapper<Majorinfo> lqw = new LambdaQueryWrapper<>();

        if (query.getMcollegeid() != null && query.getMcollegeid() > 0) {
            lqw.eq(Majorinfo::getMcollegeid, query.getMcollegeid());
        }

        if (query.getQkey() != null && !"".equals(query.getQkey().trim())) {
            lqw.and(wrapper -> wrapper.like(Majorinfo::getMname, query.getQkey().trim())
                    .or()
                    .like(Majorinfo::getMcode, query.getQkey().trim()));
        }

        lqw.orderByAsc(Majorinfo::getMid);

        IPage<Majorinfo> result = majorinfoService.page(pager, lqw);
        return ResponsePageResult.PageResult(result);
    }

    @GetMapping("/getmodel/{mid}")
    @ApiOperation("获取单个专业")
    public ResponseResult getmodel(@PathVariable Integer mid) {
        Majorinfo model = majorinfoService.getById(mid);
        if (model == null) {
            return ResponseResult.Fail("专业不存在");
        }
        return ResponseResult.success("获取成功", model);
    }

    @PostMapping("/add")
    @ApiOperation("新增专业")
    public ResponseResult add(@RequestBody Majorinfo model) {

        if (model.getMname() == null || "".equals(model.getMname().trim())) {
            return ResponseResult.Fail("专业名称不能为空");
        }
        if (model.getMcode() == null || "".equals(model.getMcode().trim())) {
            return ResponseResult.Fail("专业代码不能为空");
        }
        if (model.getMcollegeid() == null || model.getMcollegeid() <= 0) {
            return ResponseResult.Fail("所属院系不能为空");
        }

        LambdaQueryWrapper<Majorinfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Majorinfo::getMcode, model.getMcode().trim());
        long count = majorinfoService.count(lqw);
        if (count > 0) {
            return ResponseResult.Fail("专业代码已存在");
        }

        model.setMname(model.getMname().trim());
        model.setMcode(model.getMcode().trim());

        boolean flag = majorinfoService.save(model);
        if (flag) {
            return ResponseResult.success("新增成功", null);
        }
        return ResponseResult.Fail("新增失败");
    }

    @PostMapping("/update")
    @ApiOperation("修改专业")
    public ResponseResult update(@RequestBody Majorinfo model) {

        if (model.getMid() == null || model.getMid() <= 0) {
            return ResponseResult.Fail("专业ID不能为空");
        }
        if (model.getMname() == null || "".equals(model.getMname().trim())) {
            return ResponseResult.Fail("专业名称不能为空");
        }
        if (model.getMcode() == null || "".equals(model.getMcode().trim())) {
            return ResponseResult.Fail("专业代码不能为空");
        }
        if (model.getMcollegeid() == null || model.getMcollegeid() <= 0) {
            return ResponseResult.Fail("所属院系不能为空");
        }

        Majorinfo oldModel = majorinfoService.getById(model.getMid());
        if (oldModel == null) {
            return ResponseResult.Fail("专业不存在");
        }

        LambdaQueryWrapper<Majorinfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Majorinfo::getMcode, model.getMcode().trim());
        lqw.ne(Majorinfo::getMid, model.getMid());
        long count = majorinfoService.count(lqw);
        if (count > 0) {
            return ResponseResult.Fail("专业代码已存在");
        }

        oldModel.setMname(model.getMname().trim());
        oldModel.setMcode(model.getMcode().trim());
        oldModel.setMcollegeid(model.getMcollegeid());

        boolean flag = majorinfoService.updateById(oldModel);
        if (flag) {
            return ResponseResult.success("修改成功", null);
        }
        return ResponseResult.Fail("修改失败");
    }

    @DeleteMapping("/delete/{mid}")
    @ApiOperation("删除专业")
    public ResponseResult delete(@PathVariable Integer mid) {

        if (mid == null || mid <= 0) {
            return ResponseResult.Fail("参数错误");
        }

        Majorinfo model = majorinfoService.getById(mid);
        if (model == null) {
            return ResponseResult.Fail("专业不存在");
        }

        boolean flag = majorinfoService.removeById(mid);
        if (flag) {
            return ResponseResult.success("删除成功", null);
        }
        return ResponseResult.Fail("删除失败");
    }
}