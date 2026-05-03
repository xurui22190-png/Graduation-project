package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mapper.StudentinfoMapper;
import com.demo.mapper.UserinfoMapper;
import com.demo.dto.StudentExcelDto;
import com.demo.model.Studentinfo;
import com.demo.model.Userinfo;
import com.demo.service.StudentinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentinfoServiceImpl
        extends ServiceImpl<StudentinfoMapper, Studentinfo>
        implements StudentinfoService {

    // 注入 Userinfo 的 Mapper，用于在导入学生前先创建登录账号
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private StudentinfoMapper studentinfoMapper;

    /**
     * 批量导入学生数据
     * 添加 @Transactional 保证事务一致性：如果插入档案报错，账号也会跟着回滚，防止产生脏数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importStudents(List<StudentExcelDto> list) {
        for (StudentExcelDto dto : list) {
            // 1. 处理 userinfo (账号、学号、性别)
            Userinfo user = userinfoMapper.selectOne(
                    new LambdaQueryWrapper<Userinfo>().eq(Userinfo::getUlog, dto.getLoginName())
            );

            if (user == null) {
                user = new Userinfo();
                user.setUlog(dto.getLoginName());
                user.setUname(dto.getName());
                user.setUsex(dto.getSex());
                user.setUpwd(StringUtils.hasText(dto.getPassword()) ? dto.getPassword() : "123456");
                user.setUrole(1);
                userinfoMapper.insert(user);
            } else {
                user.setUname(dto.getName());
                user.setUsex(dto.getSex());
                if (StringUtils.hasText(dto.getPassword())) user.setUpwd(dto.getPassword());
                userinfoMapper.updateById(user);
            }

            // 2. 处理 studentinfo (档案、班级关联)
            Studentinfo student = baseMapper.selectOne(
                    new LambdaQueryWrapper<Studentinfo>().eq(Studentinfo::getSaccountid, user.getUid())
            );

            if (student == null) {
                student = new Studentinfo();
                student.setSaccountid(user.getUid());
                student.setSname(dto.getName());
                student.setSno(dto.getLoginName()); // 存入学号
                student.setSsex(dto.getSex());      // 存入性别
                student.setSclassid(dto.getClassId());
                student.setStel(dto.getPhone());
                student.setSidcard(dto.getIdCard());
                student.setSaddress(dto.getAddress());
                baseMapper.insert(student);
            } else {
                student.setSname(dto.getName());
                student.setSno(dto.getLoginName());
                student.setSsex(dto.getSex());
                student.setSclassid(dto.getClassId());
                student.setStel(dto.getPhone());
                student.setSidcard(dto.getIdCard());
                student.setSaddress(dto.getAddress());
                baseMapper.updateById(student);
            }
        }
    }
}