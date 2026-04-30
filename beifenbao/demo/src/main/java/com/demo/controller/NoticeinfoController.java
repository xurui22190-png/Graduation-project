package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.NoticeinfoDto;
import com.demo.model.Noticeinfo;
import com.demo.service.NoticeinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/noticeinfo")
@Api(tags = "公告管理")
public class NoticeinfoController {

    @Autowired
    private NoticeinfoService noticeinfoService;

    @ApiOperation("分页查询公告")
    @GetMapping("/getlist")
    public ResponsePageResult getlist(NoticeinfoDto dto) {
        Page<Noticeinfo> page = new Page<>(dto.getPageIndex(), dto.getPageSize());

        QueryWrapper<Noticeinfo> qw = new QueryWrapper<>();
        qw.eq("nIsDelete", 0);

        if (dto.getQkey() != null && !"".equals(dto.getQkey().trim())) {
            qw.like("nTitle", dto.getQkey().trim());
        }

        if (dto.getNstate() != null && dto.getNstate() >= 0) {
            qw.eq("nState", dto.getNstate());
        }

        if (dto.getNtype() != null && !"".equals(dto.getNtype().trim())) {
            qw.eq("nType", dto.getNtype().trim());
        }

        qw.orderByDesc("nTop");
        qw.orderByDesc("nCreateTime");

        return ResponsePageResult.PageResult(noticeinfoService.page(page, qw));
    }

    @ApiOperation("根据ID获取公告")
    @GetMapping("/getbyid/{id}")
    public ResponseResult getbyid(@PathVariable Integer id) {
        Noticeinfo data = noticeinfoService.getById(id);
        if (data == null || data.getNisdelete() == 1) {
            return ResponseResult.Fail("公告不存在");
        }
        return ResponseResult.success("success", data);
    }

    @ApiOperation("新增公告")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody NoticeinfoDto dto) {
        if (dto.getNtitle() == null || "".equals(dto.getNtitle().trim())) {
            return ResponseResult.Fail("公告标题不能为空");
        }
        if (dto.getNcontent() == null || "".equals(dto.getNcontent().trim())) {
            return ResponseResult.Fail("公告内容不能为空");
        }

        Noticeinfo model = new Noticeinfo();
        BeanUtils.copyProperties(dto, model);

        if (model.getNtype() == null || "".equals(model.getNtype().trim())) {
            model.setNtype("普通公告");
        }
        if (model.getNtop() == null) {
            model.setNtop(0);
        }
        if (model.getNstate() == null) {
            model.setNstate(1);
        }

        model.setNisdelete(0);
        model.setNcreatetime(new Date());
        model.setNupdatetime(new Date());

        boolean flag = noticeinfoService.save(model);
        return flag ? ResponseResult.success("新增成功", null) : ResponseResult.Fail("新增失败");
    }

    @ApiOperation("修改公告")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody NoticeinfoDto dto) {
        if (dto.getNid() == null || dto.getNid() <= 0) {
            return ResponseResult.Fail("公告ID不能为空");
        }

        Noticeinfo old = noticeinfoService.getById(dto.getNid());
        if (old == null || old.getNisdelete() == 1) {
            return ResponseResult.Fail("公告不存在");
        }

        if (dto.getNtitle() == null || "".equals(dto.getNtitle().trim())) {
            return ResponseResult.Fail("公告标题不能为空");
        }
        if (dto.getNcontent() == null || "".equals(dto.getNcontent().trim())) {
            return ResponseResult.Fail("公告内容不能为空");
        }

        old.setNtitle(dto.getNtitle());
        old.setNcontent(dto.getNcontent());
        old.setNtype(dto.getNtype());
        old.setNtop(dto.getNtop() == null ? 0 : dto.getNtop());
        old.setNstate(dto.getNstate() == null ? 1 : dto.getNstate());
        old.setNupdatetime(new Date());

        boolean flag = noticeinfoService.updateById(old);
        return flag ? ResponseResult.success("修改成功", null) : ResponseResult.Fail("修改失败");
    }

    @ApiOperation("删除公告")
    @PostMapping("/delete/{id}")
    public ResponseResult delete(@PathVariable Integer id) {
        Noticeinfo old = noticeinfoService.getById(id);
        if (old == null || old.getNisdelete() == 1) {
            return ResponseResult.Fail("公告不存在");
        }

        old.setNisdelete(1);
        old.setNupdatetime(new Date());

        boolean flag = noticeinfoService.updateById(old);
        return flag ? ResponseResult.success("删除成功", null) : ResponseResult.Fail("删除失败");
    }

    @ApiOperation("修改公告状态")
    @PostMapping("/updatestate")
    public ResponseResult updatestate(@RequestBody NoticeinfoDto dto) {
        if (dto.getNid() == null) {
            return ResponseResult.Fail("公告ID不能为空");
        }

        Noticeinfo old = noticeinfoService.getById(dto.getNid());
        if (old == null || old.getNisdelete() == 1) {
            return ResponseResult.Fail("公告不存在");
        }

        old.setNstate(dto.getNstate() == null ? 0 : dto.getNstate());
        old.setNupdatetime(new Date());

        boolean flag = noticeinfoService.updateById(old);
        return flag ? ResponseResult.success("状态更新成功", null) : ResponseResult.Fail("状态更新失败");
    }

    @ApiOperation("修改置顶状态")
    @PostMapping("/updatetop")
    public ResponseResult updatetop(@RequestBody NoticeinfoDto dto) {
        if (dto.getNid() == null) {
            return ResponseResult.Fail("公告ID不能为空");
        }

        Noticeinfo old = noticeinfoService.getById(dto.getNid());
        if (old == null || old.getNisdelete() == 1) {
            return ResponseResult.Fail("公告不存在");
        }

        old.setNtop(dto.getNtop() == null ? 0 : dto.getNtop());
        old.setNupdatetime(new Date());

        boolean flag = noticeinfoService.updateById(old);
        return flag ? ResponseResult.success("置顶状态更新成功", null) : ResponseResult.Fail("置顶状态更新失败");
    }

    @ApiOperation("查看端分页查询公告")
    @GetMapping("/listquery")
    public ResponsePageResult listquery(NoticeinfoDto dto) {
        Page<Noticeinfo> page = new Page<>(dto.getPageIndex(), dto.getPageSize());

        QueryWrapper<Noticeinfo> qw = new QueryWrapper<>();
        qw.eq("nIsDelete", 0);
        qw.eq("nState", 1);

        if (dto.getQkey() != null && !"".equals(dto.getQkey().trim())) {
            qw.like("nTitle", dto.getQkey().trim());
        }

        if (dto.getNtype() != null && !"".equals(dto.getNtype().trim())) {
            qw.eq("nType", dto.getNtype().trim());
        }

        qw.orderByDesc("nTop");
        qw.orderByDesc("nCreateTime");

        return ResponsePageResult.PageResult(noticeinfoService.page(page, qw));
    }

    @ApiOperation("查看端根据ID获取公告详情")
    @GetMapping("/detail/{id}")
    public ResponseResult detail(@PathVariable Integer id) {
        Noticeinfo data = noticeinfoService.getById(id);
        if (data == null || data.getNisdelete() == 1 || data.getNstate() != 1) {
            return ResponseResult.Fail("公告不存在或已下线");
        }
        return ResponseResult.success("success", data);
    }
}