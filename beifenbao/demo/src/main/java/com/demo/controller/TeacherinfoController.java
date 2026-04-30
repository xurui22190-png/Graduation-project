package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeacherinfoDto;
import com.demo.model.Teacherinfo;
import com.demo.model.Userinfo;
import com.demo.service.ITeacherinfoService;
import com.demo.service.TeacherinfoService;
import com.demo.service.UserinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/teacherinfo")
@Api(tags = "教师管理")
public class TeacherinfoController {

    @Autowired
    private ITeacherinfoService teacherinfoService;

    @Autowired
    private UserinfoService userinfoService;

    @GetMapping("getall")
    @ApiOperation("获取全部教师")
    public ResponseResult getall() {
        return ResponseResult.success("success", teacherinfoService.list());
    }

    @GetMapping("getlist")
    @ApiOperation("获取教师列表")
    public ResponsePageResult<Teacherinfo> getlist(TeacherinfoDto query) {
        Page<Teacherinfo> pager = new Page<>();
        pager.setCurrent(query.getPageIndex());
        pager.setSize(query.getPageSize());

        LambdaQueryWrapper<Teacherinfo> wrapper = new LambdaQueryWrapper<>();

        if (query.getQkey() != null && !query.getQkey().trim().equals("")) {
            wrapper.and(w -> w.like(Teacherinfo::getTno, query.getQkey())
                    .or()
                    .like(Teacherinfo::getTname, query.getQkey())
                    .or()
                    .like(Teacherinfo::getTtel, query.getQkey())
                    .or()
                    .like(Teacherinfo::getTschool, query.getQkey()));
        }

        if (query.getTsex() != null && !query.getTsex().trim().equals("")) {
            wrapper.eq(Teacherinfo::getTsex, query.getTsex());
        }

        if (query.getTedulevel() != null && !query.getTedulevel().trim().equals("")) {
            wrapper.eq(Teacherinfo::getTedulevel, query.getTedulevel());
        }

        if (query.getTcollegeid() != null && query.getTcollegeid() > 0) {
            wrapper.eq(Teacherinfo::getTcollegeid, query.getTcollegeid());
        }

        wrapper.orderByDesc(Teacherinfo::getTid);

        IPage<Teacherinfo> pageData = teacherinfoService.page(pager, wrapper);
        return ResponsePageResult.PageResult(pageData);
    }

    @GetMapping("getbyid")
    @ApiOperation("根据ID获取教师详情")
    public ResponseResult getbyid(Integer tid) {
        if (tid == null || tid <= 0) {
            return ResponseResult.Fail("参数错误");
        }

        Teacherinfo model = teacherinfoService.getById(tid);
        if (model == null) {
            return ResponseResult.Fail("教师不存在");
        }

        return ResponseResult.success("success", model);
    }

    @PostMapping("save")
    @ApiOperation("新增教师")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult save(@RequestBody Teacherinfo model) {
        try {
            if (model == null) {
                return ResponseResult.Fail("提交数据不能为空");
            }

            if (model.getTno() == null || model.getTno().trim().equals("")) {
                return ResponseResult.Fail("教师工号不能为空");
            }

            if (model.getTname() == null || model.getTname().trim().equals("")) {
                return ResponseResult.Fail("教师姓名不能为空");
            }

            long tnoCount = teacherinfoService.count(
                    new LambdaQueryWrapper<Teacherinfo>()
                            .eq(Teacherinfo::getTno, model.getTno())
            );
            if (tnoCount > 0) {
                return ResponseResult.Fail("教师工号已存在");
            }

            long ulogCount = userinfoService.count(
                    new LambdaQueryWrapper<Userinfo>()
                            .eq(Userinfo::getUlog, model.getTno())
            );
            if (ulogCount > 0) {
                return ResponseResult.Fail("该工号对应的用户账号已存在");
            }

            Userinfo user = new Userinfo();
            user.setUlog(model.getTno());
            user.setUpwd("123456");
            user.setUname(model.getTname());
            user.setUsex(model.getTsex());
            user.setUrole(2);
            user.setUcreatedate(LocalDateTime.now());

            boolean userRes = userinfoService.save(user);
            if (!userRes) {
                throw new RuntimeException("新增用户失败");
            }

            if (user.getUid() == null || user.getUid() <= 0) {
                throw new RuntimeException("用户主键回填失败，请检查 Userinfo 实体的@TableId(type = IdType.AUTO)");
            }

            model.setTaccountid(user.getUid());

            boolean teacherRes = teacherinfoService.save(model);
            if (!teacherRes) {
                throw new RuntimeException("新增教师失败");
            }

            return ResponseResult.success("新增成功", model);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.Fail("保存失败：" + e.getMessage());
        }
    }

    @PutMapping("update")
    @ApiOperation("修改教师")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult update(@RequestBody Teacherinfo model) {
        if (model == null || model.getTid() == null || model.getTid() <= 0) {
            return ResponseResult.Fail("参数错误");
        }

        Teacherinfo oldModel = teacherinfoService.getById(model.getTid());
        if (oldModel == null) {
            return ResponseResult.Fail("教师不存在");
        }

        if (model.getTno() == null || model.getTno().trim().equals("")) {
            return ResponseResult.Fail("教师工号不能为空");
        }

        if (model.getTname() == null || model.getTname().trim().equals("")) {
            return ResponseResult.Fail("教师姓名不能为空");
        }

        long tnoCount = teacherinfoService.count(
                new LambdaQueryWrapper<Teacherinfo>()
                        .eq(Teacherinfo::getTno, model.getTno())
                        .ne(Teacherinfo::getTid, model.getTid())
        );
        if (tnoCount > 0) {
            return ResponseResult.Fail("教师工号已存在");
        }

        Integer oldAccountId = oldModel.getTaccountid() == null ? 0 : oldModel.getTaccountid();

        long ulogCount = userinfoService.count(
                new LambdaQueryWrapper<Userinfo>()
                        .eq(Userinfo::getUlog, model.getTno())
                        .ne(Userinfo::getUid, oldAccountId)
        );
        if (ulogCount > 0) {
            return ResponseResult.Fail("该工号对应的其他用户账号已存在");
        }

        BeanUtils.copyProperties(model, oldModel);

        Userinfo user = null;
        if (oldModel.getTaccountid() != null && oldModel.getTaccountid() > 0) {
            user = userinfoService.getById(oldModel.getTaccountid());
        }

        if (user == null) {
            user = new Userinfo();
            user.setUlog(oldModel.getTno());
            user.setUpwd("123456");
            user.setUname(oldModel.getTname());
            user.setUsex(oldModel.getTsex());
            user.setUrole(2);
            user.setUcreatedate(LocalDateTime.now());

            boolean userRes = userinfoService.save(user);
            if (!userRes) {
                throw new RuntimeException("补建教师用户失败");
            }

            oldModel.setTaccountid(user.getUid());
        } else {
            user.setUlog(oldModel.getTno());
            user.setUname(oldModel.getTname());
            user.setUsex(oldModel.getTsex());
            user.setUrole(2);

            boolean userRes = userinfoService.updateById(user);
            if (!userRes) {
                throw new RuntimeException("同步用户信息失败");
            }
        }

        boolean teacherRes = teacherinfoService.updateById(oldModel);
        if (!teacherRes) {
            throw new RuntimeException("修改教师失败");
        }

        return ResponseResult.success("修改成功", oldModel);
    }

    @DeleteMapping("delete")
    @ApiOperation("删除教师")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete(Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.Fail("参数错误");
        }

        Teacherinfo model = teacherinfoService.getById(id);
        if (model == null) {
            return ResponseResult.Fail("教师不存在");
        }

        boolean teacherRes = teacherinfoService.removeById(id);
        if (!teacherRes) {
            throw new RuntimeException("删除教师失败");
        }

        if (model.getTaccountid() != null && model.getTaccountid() > 0) {
            userinfoService.removeById(model.getTaccountid());
        }

        return ResponseResult.success("删除成功", null);
    }
}