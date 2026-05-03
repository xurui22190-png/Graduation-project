package com.demo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ScoreExcelDto {

    @ExcelProperty("学号")
    private String sno;

    @ExcelProperty("姓名")
    private String sname;

    @ExcelProperty("总分")
    private Double scscore;
}