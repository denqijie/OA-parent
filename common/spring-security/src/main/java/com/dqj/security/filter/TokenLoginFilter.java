package com.dqj.security.filter;

import com.dqj.common.jwt.JwtHelper;
import com.dqj.common.result.ResponseUtil;
import com.dqj.common.result.Result;
import com.dqj.common.result.ResultCodeEnum;
import com.dqj.security.custom.CustomUser;
import com.dqj.vo.system.LoginVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *  自定义：
 *      登录过滤器，继承UsernamePasswordAuthenticationFilter
 *      对用户名密码进行登录校验
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * 构造方法
     * 认证管理器AuthenticationManager
     */
    public TokenLoginFilter(AuthenticationManager authenticationManager) {
        //设置认证管理器
        this.setAuthenticationManager(authenticationManager);
        //设置提交方式
        this.setPostOnly(false);
        //指定登录接口及提交方式 可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/admin/system/index/login", "POST"));
    }

    /**
     * 登录认证
     * 获取输入的用户名和密码 调用方法认证
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            //获取用户信息
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            //封装对象
            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            //调用方法
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  认证成功调用方法
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        //获取当前用户
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        //生成token
        String token = JwtHelper.createToken(customUser.getSysUser().getId(), customUser.getSysUser().getUsername());
        //返回数据
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        ResponseUtil.out(response,Result.ok(map));
    }

    /**
     *  认证失败调用方法
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response,Result.build(null,ResultCodeEnum.LOGIN_ERROR));
    }
}
