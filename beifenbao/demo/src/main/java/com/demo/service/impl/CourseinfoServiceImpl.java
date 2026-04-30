package com.demo.service.impl;

import com.demo.model.Courseinfo;
import com.demo.mapper.CourseinfoMapper;
import com.demo.service.CourseinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 课程信息表 服务实现类
 * </p>
 */
@Service
public class CourseinfoServiceImpl extends ServiceImpl<CourseinfoMapper, Courseinfo> implements CourseinfoService {

    @Override
//    @Cacheable(value = "course", key = "'all'")
    public List<Courseinfo> list() {
        System.out.println("⚠️ Redis 缓存未命中，正在查询 MySQL (课程表)...");
        return super.list();
    }

    @Override
//    @CacheEvict(value = "course", key = "'all'")
    public boolean save(Courseinfo entity) {
        return super.save(entity);
    }

    @Override
//    @CacheEvict(value = "course", key = "'all'")
    public boolean updateById(Courseinfo entity) {
        return super.updateById(entity);
    }

    @Override
//    @CacheEvict(value = "course", key = "'all'")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }
}