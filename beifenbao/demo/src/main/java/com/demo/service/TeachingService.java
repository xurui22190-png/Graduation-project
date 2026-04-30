package com.demo.service;

import com.demo.common.ResponsePageResult;
import com.demo.common.ResponseResult;
import com.demo.dto.TeachingDto;
import com.demo.model.Teaching;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 授课安排表 服务类
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
public interface TeachingService extends IService<Teaching> {
    ResponsePageResult getlist(TeachingDto query);

    ResponseResult add(Teaching model);

    ResponseResult update(Teaching model);

    ResponseResult delete(Integer id);
}
