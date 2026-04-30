package com.demo.utils;

import lombok.Data;

@Data
public class UserToken {
    private Integer urId = 0;
    private String urLog;
    private String urRole;
    private Integer urSign = 0;
}
