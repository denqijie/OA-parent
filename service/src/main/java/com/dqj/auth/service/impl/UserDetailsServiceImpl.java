package com.dqj.auth.service.impl;

import com.dqj.auth.service.SysMenuService;
import com.dqj.auth.service.SysUserService;
import com.dqj.model.system.SysUser;
import com.dqj.security.custom.CustomUser;
import com.dqj.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名进行查询
        SysUser sysUser = sysUserService.getUserByUserName(username);
        if (null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        if (sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("此账户已停用，请联系管理员！");
        }
        //根据用户id查询用户权限数据
        List<String> userPermsList = sysMenuService.findUserPermsByUserId(sysUser.getId());
        //创建List集合 用于封装最终权限数据
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        for (String perms : userPermsList) {
            authList.add(new SimpleGrantedAuthority(perms.trim()));
        }
        return new CustomUser(sysUser, authList);
    }
}
