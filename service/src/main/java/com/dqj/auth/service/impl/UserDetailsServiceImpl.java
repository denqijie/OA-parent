package com.dqj.auth.service.impl;

import com.dqj.auth.service.SysUserService;
import com.dqj.model.system.SysUser;
import com.dqj.security.custom.CustomUser;
import com.dqj.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;

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
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
