package com.demo.service.impl;

import com.demo.model.Yearterm;
import com.demo.mapper.YeartermMapper;
import com.demo.service.YeartermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 学年学期表 服务实现类
 * </p>
 */
@Service
public class YeartermServiceImpl extends ServiceImpl<YeartermMapper, Yearterm> implements YeartermService {

    @Override
    @Cacheable(value = "yearterm", key = "'all'")
    public List<Yearterm> list() {
        System.out.println("⚠️ Redis 缓存未命中，正在查询 MySQL (学期表)...");
        return super.list();
    }

    @Override
    @CacheEvict(value = "yearterm", key = "'all'")
    public boolean save(Yearterm entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(value = "yearterm", key = "'all'")
    public boolean updateById(Yearterm entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(value = "yearterm", key = "'all'")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }
}