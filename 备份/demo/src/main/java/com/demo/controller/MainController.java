package com.demo.controller;

import com.demo.common.ResponseResult;
import com.demo.dto.UserinfoDto;
import com.demo.model.Userinfo;
import com.demo.service.UserinfoService;
import com.demo.utils.CommUtil;
import com.demo.utils.UserToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/main")
@Api(tags = "系统基本操作")
public class MainController {

    @Autowired
    private UserinfoService userinfoService;

    @PutMapping("setpwd")
    @ApiOperation("修改密码")
    public ResponseResult SetPassword(HttpServletRequest request, @RequestBody com.demo.dto.UserPwdDto dto) {
        UserToken utk = CommUtil.getCurrentUser(request);
        if (utk == null) {
            return ResponseResult.Fail("登录已失效，请重新登录");
        }

        Userinfo model = userinfoService.getById(utk.getUrId());
        if (model == null) {
            return ResponseResult.Fail("用户不存在");
        }

        if (dto == null) {
            return ResponseResult.Fail("请求参数不能为空");
        }

        if (dto.getUoldpwd() == null || "".equals(dto.getUoldpwd().trim())) {
            return ResponseResult.Fail("原密码不能为空");
        }

        if (dto.getUpwd() == null || "".equals(dto.getUpwd().trim())) {
            return ResponseResult.Fail("新密码不能为空");
        }

        if (!dto.getUoldpwd().equals(model.getUpwd())) {
            return ResponseResult.Fail("原密码不对");
        }

        if (dto.getUoldpwd().equals(dto.getUpwd())) {
            return ResponseResult.Fail("新密码不能和原密码一样");
        }

        model.setUpwd(dto.getUpwd());
        boolean bl = userinfoService.updateById(model);

        if (bl) {
            return ResponseResult.success("密码修改成功", null);
        } else {
            return ResponseResult.Fail("密码修改失败");
        }
    }

    @PutMapping("setphoto")
    @ApiOperation("设置头像")
    public ResponseResult Setphoto(HttpServletRequest request, @RequestBody UserinfoDto dto) {
        UserToken utk = CommUtil.getCurrentUser(request);
        if (utk == null) {
            return ResponseResult.Fail("登录已失效，请重新登录");
        }

        Userinfo model = userinfoService.getById(utk.getUrId());
        if (model == null) {
            return ResponseResult.Fail("用户不存在");
        }

        if (dto == null) {
            return ResponseResult.Fail("请求参数不能为空");
        }

        if (dto.getUphoto() == null || "".equals(dto.getUphoto().trim())) {
            return ResponseResult.Fail("头像不能为空");
        }

        model.setUphoto(dto.getUphoto());

        boolean bl = userinfoService.updateById(model);

        if (bl) {
            return ResponseResult.success("头像设置成功", model.getUphoto());
        } else {
            return ResponseResult.Fail("头像设置失败");
        }
    }
}