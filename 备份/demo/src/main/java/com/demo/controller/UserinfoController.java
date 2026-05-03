package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.UserinfoDto;
import com.demo.model.Userinfo;
import com.demo.service.UserinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/userinfo")
@Api(tags = "用户管理")
public class UserinfoController {

    @Autowired
    private UserinfoService userinfoService;

    @GetMapping("getlist")
    @ApiOperation("获取用户列表")
    public ResponsePageResult<Userinfo> getlist(UserinfoDto query) {
        Page<Userinfo> pager = new Page<>(query.getPageIndex(), query.getPageSize());

        LambdaQueryWrapper<Userinfo> wrapper = new LambdaQueryWrapper<>();

        if (query.getQkey() != null && !query.getQkey().trim().equals("")) {
            wrapper.and(w -> w.like(Userinfo::getUlog, query.getQkey())
                    .or()
                    .like(Userinfo::getUname, query.getQkey()));
        }

        if (query.getUsex() != null && !query.getUsex().trim().equals("")) {
            wrapper.eq(Userinfo::getUsex, query.getUsex());
        }

        if (query.getUrole() != null && query.getUrole() > 0) {
            wrapper.eq(Userinfo::getUrole, query.getUrole());
        }

        wrapper.orderByDesc(Userinfo::getUid);

        IPage<Userinfo> pageResult = userinfoService.page(pager, wrapper);
        return ResponsePageResult.PageResult(pageResult);
    }

    @GetMapping("getbyid")
    @ApiOperation("根据ID获取用户")
    public ResponseResult getbyid(Integer uId) {
        if (uId == null || uId <= 0) {
            return ResponseResult.Fail("用户ID不能为空");
        }

        Userinfo model = userinfoService.getById(uId);
        if (model == null) {
            return ResponseResult.Fail("用户不存在");
        }

        return ResponseResult.success("success", model);
    }

    @PostMapping("add")
    @ApiOperation("新增用户")
    public ResponseResult add(@RequestBody UserinfoDto dto) {

        if (dto == null) {
            return ResponseResult.Fail("提交数据不能为空");
        }

        if (dto.getUlog() == null || dto.getUlog().trim().equals("")) {
            return ResponseResult.Fail("账号不能为空");
        }

        if (dto.getUname() == null || dto.getUname().trim().equals("")) {
            return ResponseResult.Fail("姓名不能为空");
        }

        if (dto.getUrole() == null || dto.getUrole() <= 0) {
            return ResponseResult.Fail("角色不能为空");
        }

        // 用户管理里只允许新增管理员
        if (dto.getUrole() == 1 || dto.getUrole() == 2) {
            return ResponseResult.Fail("学生或教师账号请在对应管理模块中新增");
        }

        long existsCount = userinfoService.count(
                new LambdaQueryWrapper<Userinfo>()
                        .eq(Userinfo::getUlog, dto.getUlog())
        );
        if (existsCount > 0) {
            return ResponseResult.Fail("账号已存在");
        }

        Userinfo model = new Userinfo();
        BeanUtils.copyProperties(dto, model);

        if (model.getUpwd() == null || model.getUpwd().trim().equals("")) {
            model.setUpwd("123456");
        }

        model.setUcreatedate(LocalDateTime.now());

        boolean flag = userinfoService.save(model);
        return flag ? ResponseResult.success("新增成功", model) : ResponseResult.Fail("新增失败");
    }

    @PostMapping("update")
    @ApiOperation("修改用户")
    public ResponseResult update(@RequestBody UserinfoDto dto) {
        if (dto == null || dto.getUid() == null || dto.getUid() <= 0) {
            return ResponseResult.Fail("用户ID不能为空");
        }

        Userinfo oldModel = userinfoService.getById(dto.getUid());
        if (oldModel == null) {
            return ResponseResult.Fail("用户不存在");
        }

        if (dto.getUlog() == null || dto.getUlog().trim().equals("")) {
            return ResponseResult.Fail("账号不能为空");
        }

        if (dto.getUname() == null || dto.getUname().trim().equals("")) {
            return ResponseResult.Fail("姓名不能为空");
        }

        if (dto.getUrole() == null || dto.getUrole() <= 0) {
            return ResponseResult.Fail("角色不能为空");
        }

        long existsCount = userinfoService.count(
                new LambdaQueryWrapper<Userinfo>()
                        .eq(Userinfo::getUlog, dto.getUlog())
                        .ne(Userinfo::getUid, dto.getUid())
        );
        if (existsCount > 0) {
            return ResponseResult.Fail("账号已存在");
        }

        // 学生/教师账号不允许在用户管理里改角色
        if (oldModel.getUrole() != null && (oldModel.getUrole() == 1 || oldModel.getUrole() == 2)) {
            if (!oldModel.getUrole().equals(dto.getUrole())) {
                return ResponseResult.Fail("学生或教师账号角色不可在此修改");
            }
        }

        oldModel.setUlog(dto.getUlog());
        oldModel.setUname(dto.getUname());
        oldModel.setUsex(dto.getUsex());
        oldModel.setUphoto(dto.getUphoto());
        oldModel.setUrole(dto.getUrole());

        boolean flag = userinfoService.updateById(oldModel);
        return flag ? ResponseResult.success("修改成功", oldModel) : ResponseResult.Fail("修改失败");
    }

    @PostMapping("delete")
    @ApiOperation("删除用户")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return ResponseResult.Fail("请选择要删除的数据");
        }

        List<Userinfo> list = userinfoService.listByIds(ids);
        if (list == null || list.size() == 0) {
            return ResponseResult.Fail("用户不存在");
        }

        for (Userinfo item : list) {
            if (item.getUrole() != null && (item.getUrole() == 1 || item.getUrole() == 2)) {
                return ResponseResult.Fail("学生或教师账号不能在用户管理中删除，请到对应模块操作");
            }
        }

        boolean flag = userinfoService.removeByIds(ids);
        return flag ? ResponseResult.success("删除成功", null) : ResponseResult.Fail("删除失败");
    }

    @PostMapping("resetpwd")
    @ApiOperation("重置密码")
    public ResponseResult resetpwd(@RequestBody UserinfoDto dto) {

        Integer uId = dto.getUid();

        if (uId == null || uId <= 0) {
            return ResponseResult.Fail("用户ID不能为空");
        }

        Userinfo oldModel = userinfoService.getById(uId);
        if (oldModel == null) {
            return ResponseResult.Fail("用户不存在");
        }

        oldModel.setUpwd("123456");
        boolean flag = userinfoService.updateById(oldModel);

        return flag ? ResponseResult.success("密码已重置为123456", null)
                : ResponseResult.Fail("重置失败");
    }
}