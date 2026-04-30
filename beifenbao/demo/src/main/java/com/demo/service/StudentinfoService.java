package com.demo.service;

import com.demo.model.StudentExcelDto;
import com.demo.model.Studentinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 学生信息表 服务类
 * </p>
 *
 * @author xyh
 * @since 2026-03-19
 */
public interface StudentinfoService extends IService<Studentinfo> {


    void importStudents(List<StudentExcelDto> list);
}
