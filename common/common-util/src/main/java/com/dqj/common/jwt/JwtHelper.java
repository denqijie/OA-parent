package com.dqj.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;
import java.util.Date;

/**
 *  JWT工具类
 */
public class JwtHelper {

    //token有效时长
    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    //签名
    private static String tokenSignKey = "123456";

    /**
     *  根据用户id和用户名称生成token字符串
     */
    public static String createToken(Long userId, String username) {
        String token = Jwts.builder()
                .setSubject("AUTH-USER")    //分类
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))  //设置token有效时长
                //主体部分
                .claim("userId", userId)    //设置userId
                .claim("username", username)    //设置username
                //签名部分
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)   //根据密钥进行加密
                .compressWith(CompressionCodecs.GZIP)   //生成字符串进行压缩
                .compact();
        return token;
    }

    /**
     *  从token中获取用户id
     */
    public static Long getUserId(String token) {
        try {
            if (StringUtils.isEmpty(token)) return null;

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            Integer userId = (Integer) claims.get("userId");
            return userId.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  从token中获取用户名
     */
    public static String getUsername(String token) {
        try {
            if (StringUtils.isEmpty(token)) return "";

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
