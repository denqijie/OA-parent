package com.dqj.security.filter;

import com.alibaba.fastjson.JSON;
import com.dqj.common.jwt.JwtHelper;
import com.dqj.common.result.ResponseUtil;
import com.dqj.common.result.Result;
import com.dqj.common.result.ResultCodeEnum;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *  认证解析token过滤器
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    //构造注入
    public TokenAuthenticationFilter(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     *  过滤并解析token
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.info("uri:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        //判断请求头里面有没有token 有则是登录 没有则返回错误信息
        if(null != authentication) {
            //SecurityContext类似ServletContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
        }
    }

    /**
     *  获取判断token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //请求头里面是否有token
        String token = request.getHeader("token");
        if(!StringUtils.isEmpty(token)){
            String username = JwtHelper.getUsername(token);
            if(!StringUtils.isEmpty(username)){
                //通过username从redis获取权限数据
                String authString = (String) redisTemplate.opsForValue().get(username);
                //把redis获取字符串权限数据转换成集合类型 List<SimpleGrantedAuthority>
                if(!StringUtils.isEmpty(authString)){
                    List<Map> mapList = JSON.parseArray(authString, Map.class);
                    //创建集合用来存数据
                    List<SimpleGrantedAuthority> authList = new ArrayList<>();
                    for (Map map : mapList) {
                        authList.add(new SimpleGrantedAuthority((String) map.get("authority")));
                    }
                    return new UsernamePasswordAuthenticationToken(username,null, authList);
                }
                //数据为空则返回空的集合
                return new UsernamePasswordAuthenticationToken(username,null, new ArrayList<>());
            }
        }
        return null;
    }
}
