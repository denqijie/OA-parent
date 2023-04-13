package com.dqj.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dqj.model.system.SysUser;

/**
 *  User业务层
 */
public interface SysUserService extends IService<SysUser> {
    /**
     *  更新状态
     */
    void updateStatus(Long id, Integer status);

    /**
     *  根据用户名进行查询
     */
    SysUser getUserByUserName(String username);
}
