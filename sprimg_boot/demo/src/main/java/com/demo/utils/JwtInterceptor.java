package com.demo.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 放行OPTIONS预检测请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从请求头获取token
        String token = request.getHeader("Authorization");

        // 处理Bearer前缀
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Token不存在情况处理
        if (token == null || token.trim().isEmpty()) {
            response.sendError(401, "未提供有效的token");
            return false;
        }

        // Token无效处理
        if (!JwtUtil.validateToken(token)) {
            response.sendError(401, "无效Token");
            return false;
        }

        // Token过期处理
        if (JwtUtil.isTokenExpired(token)) {
            response.sendError(401, "Token已过期");
            return false;
        }

        // 解析用户信息
        UserToken userToken = JwtUtil.getUserFromToken(token);

        // 保留原有写法，避免影响旧功能
        request.setAttribute("thisuser", userToken);

        // 新增 uid，给新接口使用
        request.setAttribute("uid", userToken.getUrId());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object haneler, Exception ex) throws Exception {
    }
}