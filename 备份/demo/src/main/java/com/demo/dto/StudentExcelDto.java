package com.demo.dto; // 注意改成你的包名

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class StudentExcelDto {
    @ExcelProperty("学生姓名")
    private String name;

    @ExcelProperty("登录账号")
    private String loginName;

    @ExcelProperty("初始密码")
    private String password;

    @ExcelProperty("班级ID")
    private Integer classId;

    // --- 以下为新增字段 ---
    @ExcelProperty("专业")
    private String majorName; // 仅做 Excel 展示，实际业务靠 classId 关联

    @ExcelProperty("学院")
    private String collegeName; // 仅做 Excel 展示

    @ExcelProperty("年级")
    private String grade; // 仅做 Excel 展示
    @ExcelProperty("性别") // 🔥 新增
    private String sex;
    @ExcelProperty("电话")
    private String phone;

    @ExcelProperty("身份证号")
    private String idCard;

    @ExcelProperty("家庭住址")
    private String address;
}