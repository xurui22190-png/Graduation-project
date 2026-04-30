package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeachingDto;
import com.demo.model.Teaching;
import com.demo.model.Teacherinfo;
import com.demo.model.Vwteaching;
import com.demo.service.TeacherinfoService;
import com.demo.service.TeachingService;
import com.demo.service.VwteachingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/teaching")
@Api(tags = "授课管理")
public class TeachingController {

    @Autowired
    private TeachingService teachingService;

    @Autowired
    private TeacherinfoService teacherinfoService;

    @Autowired
    private VwteachingService vwteachingService;

    @GetMapping("getlist")
    @ApiOperation("获取授课列表")
    public ResponsePageResult getlist(TeachingDto query) {
        return teachingService.getlist(query);
    }

    @PostMapping("add")
    @ApiOperation("新增授课")
    public ResponseResult add(@RequestBody Teaching model) {
        return teachingService.add(model);
    }

    @PostMapping("update")
    @ApiOperation("修改授课")
    public ResponseResult update(@RequestBody Teaching model) {
        return teachingService.update(model);
    }

    @PostMapping("delete")
    @ApiOperation("删除授课")
    public ResponseResult delete(@RequestParam Integer id) {
        return teachingService.delete(id);
    }

    @GetMapping("mylist")
    @ApiOperation("教师端获取我的授课列表")
    public ResponseResult mylist(HttpServletRequest request) {
        try {
            Object uidObj = request.getAttribute("uid");
            if (uidObj == null) {
                return ResponseResult.Fail("未获取到当前登录用户");
            }

            Integer uid = Integer.parseInt(uidObj.toString());

            Teacherinfo teacher = teacherinfoService.lambdaQuery()
                    .eq(Teacherinfo::getTaccountid, uid)
                    .one();

            if (teacher == null) {
                return ResponseResult.Fail("当前账号未绑定教师信息");
            }

            List<Vwteaching> list = vwteachingService.list(
                    new LambdaQueryWrapper<Vwteaching>()
                            .eq(Vwteaching::getTcteacherid, teacher.getTid())
                            .orderByDesc(Vwteaching::getTcid)
            );

            return ResponseResult.success("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.Fail("查询失败：" + e.getMessage());
        }
    }
}