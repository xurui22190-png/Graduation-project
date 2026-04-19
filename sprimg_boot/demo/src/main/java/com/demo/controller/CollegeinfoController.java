package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.CollegeinfoDto;
import com.demo.model.Collegeinfo;
import com.demo.service.CollegeinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collegeinfo")
@Api(tags = "院系信息")
public class CollegeinfoController {

    @Autowired
    private CollegeinfoService collegeinfoService;

    @GetMapping("/getlist")
    @ApiOperation("获取院系列表")
    public ResponsePageResult<Collegeinfo> getlist(CollegeinfoDto query) {
        Page<Collegeinfo> pager = new Page<>();
        pager.setCurrent(query.getPageIndex());
        pager.setSize(query.getPageSize());

        LambdaQueryWrapper<Collegeinfo> lqw = new LambdaQueryWrapper<>();

        Integer cparentid = query.getCparentid();
        if (cparentid == null) {
            cparentid = 0;
        }

        lqw.eq(Collegeinfo::getCparentid, cparentid);

        if (query.getQkey() != null && !"".equals(query.getQkey().trim())) {
            lqw.and(wrapper -> wrapper.like(Collegeinfo::getCname, query.getQkey().trim())
                    .or()
                    .like(Collegeinfo::getCcode, query.getQkey().trim()));
        }

        lqw.orderByAsc(Collegeinfo::getCid);

        IPage<Collegeinfo> result = collegeinfoService.page(pager, lqw);
        return ResponsePageResult.PageResult(result);
    }

    @PostMapping("/add")
    @ApiOperation("新增院系")
    public ResponseResult add(@RequestBody Collegeinfo model) {

        if (model.getCname() == null || "".equals(model.getCname().trim())) {
            return ResponseResult.Fail("院系名称不能为空");
        }
        if (model.getCcode() == null || "".equals(model.getCcode().trim())) {
            return ResponseResult.Fail("院系代码不能为空");
        }
        if (model.getCparentid() == null) {
            model.setCparentid(0);
        }

        LambdaQueryWrapper<Collegeinfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Collegeinfo::getCcode, model.getCcode().trim());
        long count = collegeinfoService.count(lqw);
        if (count > 0) {
            return ResponseResult.Fail("院系代码已存在");
        }

        model.setCname(model.getCname().trim());
        model.setCcode(model.getCcode().trim());

        boolean flag = collegeinfoService.save(model);
        if (flag) {
            return ResponseResult.success("新增成功", null);
        }
        return ResponseResult.Fail("新增失败");
    }

    @PostMapping("/update")
    @ApiOperation("修改院系")
    public ResponseResult update(@RequestBody Collegeinfo model) {

        if (model.getCid() == null || model.getCid() <= 0) {
            return ResponseResult.Fail("院系ID不能为空");
        }
        if (model.getCname() == null || "".equals(model.getCname().trim())) {
            return ResponseResult.Fail("院系名称不能为空");
        }
        if (model.getCcode() == null || "".equals(model.getCcode().trim())) {
            return ResponseResult.Fail("院系代码不能为空");
        }
        if (model.getCparentid() == null) {
            model.setCparentid(0);
        }

        Collegeinfo oldModel = collegeinfoService.getById(model.getCid());
        if (oldModel == null) {
            return ResponseResult.Fail("院系不存在");
        }

        LambdaQueryWrapper<Collegeinfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Collegeinfo::getCcode, model.getCcode().trim());
        lqw.ne(Collegeinfo::getCid, model.getCid());
        long count = collegeinfoService.count(lqw);
        if (count > 0) {
            return ResponseResult.Fail("院系代码已存在");
        }

        oldModel.setCname(model.getCname().trim());
        oldModel.setCcode(model.getCcode().trim());
        oldModel.setCparentid(model.getCparentid());

        boolean flag = collegeinfoService.updateById(oldModel);
        if (flag) {
            return ResponseResult.success("修改成功", null);
        }
        return ResponseResult.Fail("修改失败");
    }

    @DeleteMapping("/delete/{cid}")
    @ApiOperation("删除院系")
    public ResponseResult delete(@PathVariable Integer cid) {

        if (cid == null || cid <= 0) {
            return ResponseResult.Fail("参数错误");
        }

        Collegeinfo model = collegeinfoService.getById(cid);
        if (model == null) {
            return ResponseResult.Fail("院系不存在");
        }

        LambdaQueryWrapper<Collegeinfo> childLqw = new LambdaQueryWrapper<>();
        childLqw.eq(Collegeinfo::getCparentid, cid);
        long childCount = collegeinfoService.count(childLqw);
        if (childCount > 0) {
            return ResponseResult.Fail("请先删除下级院系");
        }

        boolean flag = collegeinfoService.removeById(cid);
        if (flag) {
            return ResponseResult.success("删除成功", null);
        }
        return ResponseResult.Fail("删除失败");
    }

    @GetMapping("/getmodel/{cid}")
    @ApiOperation("获取单个院系")
    public ResponseResult getmodel(@PathVariable Integer cid) {
        Collegeinfo model = collegeinfoService.getById(cid);
        if (model == null) {
            return ResponseResult.Fail("院系不存在");
        }
        return ResponseResult.success("获取成功", model);
    }
    @GetMapping("getall")
    @ApiOperation("获取全部院系")
    public ResponseResult getall() {
        LambdaQueryWrapper<Collegeinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Collegeinfo::getCid);
        List<Collegeinfo> list = collegeinfoService.list(wrapper);
        return ResponseResult.success("success", list);
    }
}