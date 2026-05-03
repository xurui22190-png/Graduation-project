package com.demo.utils;

import javax.servlet.http.HttpServletRequest;

public class CommUtil {

    //取得当前用户
    public static UserToken getCurrentUser( HttpServletRequest request ) {
        UserToken utk = ( UserToken) request.getAttribute("thisuser");
        return utk;
    }
}
