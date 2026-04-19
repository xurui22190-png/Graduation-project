package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeachingDto;
import com.demo.mapper.TeachingMapper;
import com.demo.mapper.VwteachingMapper;
import com.demo.model.Teaching;
import com.demo.model.Vwteaching;
import com.demo.service.TeachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TeachingServiceImpl extends ServiceImpl<TeachingMapper, Teaching> implements TeachingService {

    @Autowired
    private VwteachingMapper vwteachingMapper;

    @Override
    public ResponsePageResult getlist(TeachingDto query) {
        Page<Vwteaching> pager = new Page<>();
        pager.setCurrent(query.getPageIndex());
        pager.setSize(query.getPageSize());

        LambdaQueryWrapper<Vwteaching> wrapper = new LambdaQueryWrapper<>();

        if (query.getTcTermId() != null && query.getTcTermId() > 0) {
            wrapper.eq(Vwteaching::getTctermid, query.getTcTermId());
        }

        if (query.getTcClassId() != null && query.getTcClassId() > 0) {
            wrapper.eq(Vwteaching::getTcclassid, query.getTcClassId());
        }

        if (query.getTcCourseId() != null && query.getTcCourseId() > 0) {
            wrapper.eq(Vwteaching::getTccourseid, query.getTcCourseId());
        }

        if (query.getTcTeacherId() != null && query.getTcTeacherId() > 0) {
            wrapper.eq(Vwteaching::getTcteacherid, query.getTcTeacherId());
        }

        if (StringUtils.hasText(query.getQkey())) {
            wrapper.and(w -> w
                    .like(Vwteaching::getClassname, query.getQkey())
                    .or()
                    .like(Vwteaching::getCrname, query.getQkey())
                    .or()
                    .like(Vwteaching::getTname, query.getQkey())
            );
        }

        wrapper.orderByDesc(Vwteaching::getTcid);

        IPage<Vwteaching> pageData = vwteachingMapper.selectPage(pager, wrapper);
        return ResponsePageResult.PageResult(pageData);
    }

    @Override
    public ResponseResult add(Teaching model) {
        if (model.getTctermid() == null || model.getTcclassid() == null ||
                model.getTccourseid() == null || model.getTcteacherid() == null) {
            return ResponseResult.Fail("参数不完整");
        }

        LambdaQueryWrapper<Teaching> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teaching::getTctermid, model.getTctermid())
                .eq(Teaching::getTcclassid, model.getTcclassid())
                .eq(Teaching::getTccourseid, model.getTccourseid())
                .eq(Teaching::getTcteacherid, model.getTcteacherid());

        Long count = this.count(wrapper);
        if (count > 0) {
            return ResponseResult.Fail("该授课记录已存在");
        }

        boolean flag = this.save(model);
        return flag ? ResponseResult.success("新增成功", null) : ResponseResult.Fail("新增失败");
    }

    @Override
    public ResponseResult update(Teaching model) {
        if (model.getTcid() == null || model.getTcid() <= 0) {
            return ResponseResult.Fail("授课ID不能为空");
        }

        LambdaQueryWrapper<Teaching> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teaching::getTctermid, model.getTctermid())
                .eq(Teaching::getTcclassid, model.getTcclassid())
                .eq(Teaching::getTccourseid, model.getTccourseid())
                .eq(Teaching::getTcteacherid, model.getTcteacherid())
                .ne(Teaching::getTcid, model.getTcid());

        Long count = this.count(wrapper);
        if (count > 0) {
            return ResponseResult.Fail("修改后的授课记录已存在");
        }

        boolean flag = this.updateById(model);
        return flag ? ResponseResult.success("修改成功", null) : ResponseResult.Fail("修改失败");
    }

    @Override
    public ResponseResult delete(Integer id) {
        if (id == null || id <= 0) {
            return ResponseResult.Fail("授课ID不能为空");
        }

        boolean flag = this.removeById(id);
        return flag ? ResponseResult.success("删除成功", null) : ResponseResult.Fail("删除失败");
    }
}