package com.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.StudentinfoDto;
import com.demo.dto.StudentExcelDto;
import com.demo.model.Studentinfo;
import com.demo.model.Userinfo;
import com.demo.model.Vwstudent;
import com.demo.service.StudentinfoService;
import com.demo.service.UserinfoService;
import com.demo.service.VwstudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/studentinfo")
@Api(tags = "学生管理")
public class StudentinfoController {

    @Autowired
    private StudentinfoService studentinfoService;

    @Autowired
    private VwstudentService vwstudentService;

    @Autowired
    private UserinfoService userinfoService;

    @Autowired
    private StudentinfoService studentService;

    @GetMapping("getall")
    @ApiOperation("获取全部学生")
    public ResponseResult getall() {
        List<Studentinfo> list = studentinfoService.list(
                new LambdaQueryWrapper<Studentinfo>().orderByDesc(Studentinfo::getSid)
        );
        return ResponseResult.success("success", list);
    }

    @GetMapping("getlist")
    @ApiOperation("获取学生列表")
    public ResponsePageResult<Vwstudent> getlist(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            StudentinfoDto query) { // query 继续接收 qkey, sclassid 等其他字段

        // 使用前端传来的 page 和 limit
        Page<Vwstudent> pager = new Page<>(page, limit);

        LambdaQueryWrapper<Vwstudent> wrapper = new LambdaQueryWrapper<>();

        // ... 下面的查询条件逻辑保持不变 ...

        IPage<Vwstudent> pageResult = vwstudentService.page(pager, wrapper);
        return ResponsePageResult.PageResult(pageResult);
    }

    @GetMapping("getbyid")
    @ApiOperation("根据ID获取学生详情")
    public ResponseResult getbyid(Integer sid) {
        if (sid == null || sid <= 0) {
            return ResponseResult.Fail("参数sid不能为空");
        }

        Vwstudent model = vwstudentService.getById(sid);
        if (model == null) {
            return ResponseResult.Fail("学生不存在");
        }

        return ResponseResult.success("success", model);
    }

    @PostMapping("add")
    @ApiOperation("新增学生")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult add(@RequestBody StudentinfoDto dto) {

        if (!StringUtils.hasText(dto.getSno())) {
            return ResponseResult.Fail("学号不能为空");
        }
        if (!StringUtils.hasText(dto.getSname())) {
            return ResponseResult.Fail("姓名不能为空");
        }
        if (dto.getSclassid() == null || dto.getSclassid() <= 0) {
            return ResponseResult.Fail("请选择班级");
        }

        long existsNo = studentinfoService.count(
                new LambdaQueryWrapper<Studentinfo>()
                        .eq(Studentinfo::getSno, dto.getSno())
        );
        if (existsNo > 0) {
            return ResponseResult.Fail("学号已存在");
        }

        long existsUser = userinfoService.count(
                new LambdaQueryWrapper<Userinfo>()
                        .eq(Userinfo::getUlog, dto.getSno())
        );
        if (existsUser > 0) {
            return ResponseResult.Fail("该学号对应的用户账号已存在");
        }

        Userinfo user = new Userinfo();
        user.setUlog(dto.getSno());
        user.setUpwd("123456");
        user.setUname(dto.getSname());
        user.setUsex(dto.getSsex());
        user.setUrole(1);
        user.setUcreatedate(LocalDateTime.now());

        boolean userRes = userinfoService.save(user);
        if (!userRes || user.getUid() == null || user.getUid() <= 0) {
            throw new RuntimeException("创建用户失败");
        }

        Studentinfo model = new Studentinfo();
        BeanUtils.copyProperties(dto, model);
        model.setSaccountid(user.getUid());
        model.setScreatedate(LocalDateTime.now());

        boolean stuRes = studentinfoService.save(model);
        if (!stuRes) {
            throw new RuntimeException("新增学生失败");
        }

        return ResponseResult.success("新增成功", model);
    }

    @PostMapping("edit")
    @ApiOperation("修改学生")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult edit(@RequestBody StudentinfoDto dto) {

        if (dto.getSid() == null || dto.getSid() <= 0) {
            return ResponseResult.Fail("学生ID不能为空");
        }

        Studentinfo oldModel = studentinfoService.getById(dto.getSid());
        if (oldModel == null) {
            return ResponseResult.Fail("学生不存在");
        }

        if (!StringUtils.hasText(dto.getSno())) {
            return ResponseResult.Fail("学号不能为空");
        }
        if (!StringUtils.hasText(dto.getSname())) {
            return ResponseResult.Fail("姓名不能为空");
        }
        if (dto.getSclassid() == null || dto.getSclassid() <= 0) {
            return ResponseResult.Fail("请选择班级");
        }

        long existsNo = studentinfoService.count(
                new LambdaQueryWrapper<Studentinfo>()
                        .eq(Studentinfo::getSno, dto.getSno())
                        .ne(Studentinfo::getSid, dto.getSid())
        );
        if (existsNo > 0) {
            return ResponseResult.Fail("学号已存在");
        }

        LambdaQueryWrapper<Userinfo> userWrapper = new LambdaQueryWrapper<Userinfo>()
                .eq(Userinfo::getUlog, dto.getSno());

        if (oldModel.getSaccountid() != null && oldModel.getSaccountid() > 0) {
            userWrapper.ne(Userinfo::getUid, oldModel.getSaccountid());
        }

        long existsUser = userinfoService.count(userWrapper);
        if (existsUser > 0) {
            return ResponseResult.Fail("该学号对应的其他用户账号已存在");
        }

        BeanUtils.copyProperties(dto, oldModel);

        Userinfo user = null;
        if (oldModel.getSaccountid() != null && oldModel.getSaccountid() > 0) {
            user = userinfoService.getById(oldModel.getSaccountid());
        }

        if (user == null) {
            user = new Userinfo();
            user.setUlog(dto.getSno());
            user.setUpwd("123456");
            user.setUname(dto.getSname());
            user.setUsex(dto.getSsex());
            user.setUrole(1);
            user.setUcreatedate(LocalDateTime.now());

            boolean userRes = userinfoService.save(user);
            if (!userRes || user.getUid() == null || user.getUid() <= 0) {
                throw new RuntimeException("补建用户失败");
            }

            oldModel.setSaccountid(user.getUid());
        } else {
            user.setUlog(dto.getSno());
            user.setUname(dto.getSname());
            user.setUsex(dto.getSsex());
            user.setUrole(1);

            boolean userRes = userinfoService.updateById(user);
            if (!userRes) {
                throw new RuntimeException("同步用户信息失败");
            }
        }

        boolean stuRes = studentinfoService.updateById(oldModel);
        if (!stuRes) {
            throw new RuntimeException("修改学生失败");
        }

        return ResponseResult.success("修改成功", oldModel);
    }

    @PostMapping("delete")
    @ApiOperation("删除学生")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return ResponseResult.Fail("请选择要删除的数据");
        }

        List<Studentinfo> studentList = studentinfoService.listByIds(ids);
        if (studentList == null || studentList.size() == 0) {
            return ResponseResult.Fail("未找到要删除的学生数据");
        }

        List<Integer> userIds = new ArrayList<>();
        for (Studentinfo item : studentList) {
            if (item.getSaccountid() != null && item.getSaccountid() > 0) {
                userIds.add(item.getSaccountid());
            }
        }

        boolean stuRes = studentinfoService.removeByIds(ids);
        if (!stuRes) {
            throw new RuntimeException("删除学生失败");
        }

        if (userIds.size() > 0) {
            userinfoService.removeByIds(userIds);
        }

        return ResponseResult.success("删除成功", null);
    }

        @PostMapping("/import")
        public ResponseResult upload(@RequestParam("file") MultipartFile file) throws IOException {
            // 使用 EasyExcel 同步读取数据
            List<StudentExcelDto> list = EasyExcel.read(file.getInputStream())
                    .head(StudentExcelDto.class)
                    .sheet()
                    .doReadSync();

            studentService.importStudents(list);

            // 调用你 ResponseResult.java 里的 success(String msg, Object data) 方法
            return ResponseResult.success("成功导入 " + list.size() + " 名学生", null);
        }


}