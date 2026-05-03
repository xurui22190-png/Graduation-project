package com.demo.service.impl;

import com.demo.model.Collegeinfo;
import com.demo.mapper.CollegeinfoMapper;
import com.demo.service.CollegeinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 院系信息表 服务实现类
 * </p>
 */
@Service
public class CollegeinfoServiceImpl extends ServiceImpl<CollegeinfoMapper, Collegeinfo> implements CollegeinfoService {

    @Override
//    @Cacheable(value = "college", key = "'all'")
    public List<Collegeinfo> list() {
        System.out.println("⚠️ Redis 缓存未命中，正在查询 MySQL (院系表)...");
        return super.list();
    }

    @Override
//    @CacheEvict(value = "college", key = "'all'")
    public boolean save(Collegeinfo entity) {
        return super.save(entity);
    }

    @Override
//    @CacheEvict(value = "college", key = "'all'")
    public boolean updateById(Collegeinfo entity) {
        return super.updateById(entity);
    }

    @Override
//    @CacheEvict(value = "college", key = "'all'")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }
}