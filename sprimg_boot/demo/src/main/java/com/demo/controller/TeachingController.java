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
    @ApiOperation("获取授课列表（含数据权限隔离）")
    public ResponsePageResult getlist(TeachingDto query, HttpServletRequest request) {
        // 【修复问题1：数据越权】
        // 拦截请求，判断当前登录人的身份。如果是教师，强制将其 ID 塞入查询条件中
        try {
            Object uidObj = request.getAttribute("uid");
            if (uidObj != null) {
                Integer uid = Integer.parseInt(uidObj.toString());
                Teacherinfo teacher = teacherinfoService.lambdaQuery()
                        .eq(Teacherinfo::getTaccountid, uid)
                        .one();

                if (teacher != null) {
                    // 如果是老师，强制在分页查询条件中加上他的 teacherId
                    // 注意：如果你的 IDEA 在这里标红，请把 setTcteacherid 改成 setTcTeacherId (根据你的实体类大小写而定)
                    query.setTcTeacherId(teacher.getTid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    // 【修复问题2：404 Not Found】
    // 增加前端缺少的 updateWeights 接口
    @PostMapping("updateWeights")
    @ApiOperation("更新授课成绩权重")
    public ResponseResult updateWeights(@RequestBody Teaching model) {
        // 调试探照灯：在控制台打印一下前端传过来的数据到底长啥样
        System.out.println("收到前端权重数据：" + model);

        // 1. 强制校验 ID（500 报错最常见原因：没传 ID 导致更新全表失败）
        if (model.getTcid() == null) {
            return ResponseResult.Fail("后端接收失败：tcid 为空，请检查前端字段名");
        }

        try {
            // 2. 使用 MyBatis-Plus 的局部更新方法
            // 注意：这里用 updateById 比较稳妥
            boolean flag = teachingService.updateById(model);

            if(flag) {
                return ResponseResult.success("权重设置成功", null);
            } else {
                return ResponseResult.Fail("更新失败，请检查该记录是否存在");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 在 IDEA 控制台打印具体错误
            return ResponseResult.Fail("系统内部错误：" + e.getMessage());
        }
    }

    @PostMapping("delete")
    @ApiOperation("删除授课")
    public ResponseResult delete(@RequestParam Integer id) {
        return teachingService.delete(id);
    }

    @GetMapping("mylist")
    @ApiOperation("教师端获取我的授课列表（不分页）")
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