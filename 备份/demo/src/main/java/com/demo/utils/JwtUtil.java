package com.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    private static String secret="mysecretKey1234567890";
    private static Long expire=3600000L;
    private static Long refreshExpire=2592000000L;
    public final static String CONTEXT_USER_ID="contextUserId";
    public final static String contextUrole="contextUrole";
    public final static String contextUsignID="contUsigiID";


    /**
     * 构建一个JWT令牌 生成Token
     */
    public static String generateToken(UserToken _user) {

        return Jwts.builder()
                .setSubject(_user.getUrLog()) //设置用户名为JWT的核心主题
                .claim(CONTEXT_USER_ID, _user.getUrId()) //自定义claim,存入用户ID
                .claim(contextUrole, _user.getUrRole()) //同上，存入用户角色
                .claim(contextUsignID, _user.getUrSign()) //同上，存入用户身份标识(1:学生/2:教师)
                .setIssuedAt(new Date()) //令牌签发时间为当前时间
                .setExpiration(new Date(System.currentTimeMillis() + expire)) //令牌过期时间
                .signWith(SignatureAlgorithm.HS512, secret) //使用HS512算法和密钥对令牌进行签名
                .compact(); //最终生成JWT字符串
    }

    /**
     * 验证Token是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token); // 使用密钥解析token
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 从Token中获取用户
     */
    public static UserToken getUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret) //设置签名密钥
                .parseClaimsJws(token) //解析token
                .getBody(); //提取claims部分

        UserToken userToken = new UserToken();
        userToken.setUrLog(claims.getSubject());
        userToken.setUrId(Integer.parseInt(claims.get(CONTEXT_USER_ID).toString()));
        userToken.setUrRole(claims.get(contextUrole).toString());
//        userToken.setUrId(Integer.parseInt(claims.get(contextUsignID).toString()));
        return userToken;
    }
    /**
     * 检查Token是否过期
     */
    public static boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(secret) //设置签名密钥
                .parseClaimsJws(token) //解析token
                .getBody() //提取claims部分
                .getExpiration(); //提取过期时间
        return expiration.before(new Date());
    }

}
