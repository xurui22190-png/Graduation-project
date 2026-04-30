package com.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.common.ResponseResult;
import com.demo.dto.LoginDto;
import com.demo.model.Studentinfo;
import com.demo.model.Teacherinfo;
import com.demo.model.Userinfo;
import com.demo.service.StudentinfoService;
import com.demo.service.TeacherinfoService;
import com.demo.service.UserinfoService;
import com.demo.utils.JwtUtil;
import com.demo.utils.UserToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pub")
@Api(tags ="登录Login")
public class PubController {

    private final UserinfoService userinfoService;
    private final StudentinfoService studentinfoService;
    private final TeacherinfoService teacherinfoService;

    public PubController(UserinfoService userinfoService,
                         StudentinfoService studentinfoService,
                         TeacherinfoService teacherinfoService) {
        this.userinfoService = userinfoService;
        this.studentinfoService = studentinfoService;
        this.teacherinfoService = teacherinfoService;
    }

    @PostMapping("login")
    @ApiOperation("用户登录")
    public ResponseResult Login(@RequestBody LoginDto nsfo) {

        if (nsfo == null || nsfo.getUlog() == null || nsfo.getUpwd() == null || nsfo.getUrole() == null) {
            return ResponseResult.Fail("登录参数不完整");
        }

        LambdaQueryWrapper<Userinfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Userinfo::getUlog, nsfo.getUlog());
        lqw.eq(Userinfo::getUpwd, nsfo.getUpwd());
        lqw.eq(Userinfo::getUrole, nsfo.getUrole());

        List<Userinfo> list = userinfoService.list(lqw);

        if (list == null || list.isEmpty()) {
            return ResponseResult.Fail("账号、密码或身份错误");
        }

        Userinfo loginUser = list.get(0);

        UserToken utk = new UserToken();
        utk.setUrId(loginUser.getUid());
        utk.setUrLog(loginUser.getUlog());
        utk.setUrRole(String.valueOf(loginUser.getUrole()));
        utk.setUrSign(GetSignID(loginUser.getUid(), loginUser.getUrole()));

        String token = JwtUtil.generateToken(utk);

        if (token == null || token.trim().isEmpty()) {
            return ResponseResult.Fail("登录失败，请重试");
        }

        Map<String, Object> mapOut = new HashMap<>();
        mapOut.put("userInfo", loginUser);
        mapOut.put("token", token);

        return ResponseResult.success("登录成功", mapOut);
    }

    private Integer GetSignID(Integer uid, Integer urole) {

        if (urole == null) {
            return 0;
        }

        if (urole == 1) {
            LambdaQueryWrapper<Studentinfo> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Studentinfo::getSaccountid, uid);
            List<Studentinfo> listU = studentinfoService.list(lqw);

            if (listU == null || listU.isEmpty()) {
                return 0;
            }
            return listU.get(0).getSid();

        } else if (urole == 2) {
            LambdaQueryWrapper<Teacherinfo> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Teacherinfo::getTaccountid, uid);
            List<Teacherinfo> listU = teacherinfoService.list(lqw);

            if (listU == null || listU.isEmpty()) {
                return 0;
            }
            return listU.get(0).getTid();
        }

        return 0;
    }
}