package com.dqj.security.filter;

import com.dqj.common.jwt.JwtHelper;
import com.dqj.common.result.ResponseUtil;
import com.dqj.common.result.Result;
import com.dqj.common.result.ResultCodeEnum;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 *  认证解析token过滤器
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

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
                return new UsernamePasswordAuthenticationToken(username,null,Collections.emptyList());
            }
        }
        return null;
    }
}
