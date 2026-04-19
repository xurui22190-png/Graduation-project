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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.google.common.base.Predicates.or;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author xyh
 * @since 2026-03-11
 */
@RestController
@RequestMapping("/api/userinfo00")
@Api(tags = "用户管理")
public class Userinfo00Controller {
    @Autowired
    private UserinfoService userinfoService;

    @GetMapping("getlist")
    @ApiOperation("获取用户信息")
    public ResponsePageResult<Userinfo> getlist(UserinfoDto query) {
        Page<Userinfo> pager = new Page(); // 3.x引用extension.plugins
        pager.setCurrent(query.getPageIndex());
        pager.setSize(query.getPageSize());

        LambdaQueryWrapper<Userinfo> lqw = new LambdaQueryWrapper<>();
        if (query.getQkey() != null) {
            lqw.like(Userinfo::getUname, query.getQkey());
            lqw.or();
            lqw.like(Userinfo::getUlog, query.getQkey());
        }
        lqw.orderByDesc(Userinfo::getUid);
       IPage<Userinfo> result = userinfoService.page(pager, lqw);
//        IPage<Userinfo> result = userinfoService.pageZDY(pager,lqw);
        return ResponsePageResult.PageResult(result);
    }

    @PostMapping("add")
    @ApiOperation("添加用用户")
    public ResponseResult Add(@RequestBody Userinfo userinfo) {
        LambdaQueryWrapper<Userinfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Userinfo::getUlog, userinfo.getUlog());
        List<Userinfo> ListEx = userinfoService.list(lqw);
        if (ListEx.size() > 0) {
            return ResponseResult.Fail("账号已存在");
        }
            boolean bl = userinfoService.save(userinfo);
            return ResponseResult.ResResult(bl);
    }

    @PutMapping("update")
    @ApiOperation("更新用户信息")
    public ResponseResult Update(@RequestBody Userinfo userinfo){
        LambdaQueryWrapper<Userinfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Userinfo::getUlog, userinfo.getUlog());
        lqw.ne(Userinfo::getUid, userinfo.getUid()); // 如uid!=1
        List<Userinfo> listEx = userinfoService.list(lqw);

        if (listEx.size() > 0) {
            return ResponseResult.Fail("账号已存在，请更换");
        }



        boolean  bl = userinfoService.saveOrUpdate(userinfo);
        return  ResponseResult.ResResult(bl);
    }

    @DeleteMapping("delet")
    @ApiOperation("删除用户信息")
    public ResponseResult Delete( Integer id){
        boolean  bl = userinfoService.removeById(id);
      return  ResponseResult.ResResult(bl);
    }

}
