package com.dqj.auth.service.impl;

import com.dqj.auth.mapper.SysUserMapper;
import com.dqj.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqj.model.system.SysUser;
import org.springframework.stereotype.Service;

/**
 * 用户表 服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    /**
     *  更新状态
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        //根据userid查询用户对象
        SysUser sysUser = baseMapper.selectById(id);
        //设置修改状态
        sysUser.setStatus(status);
        //调佣方法进行修改
        baseMapper.updateById(sysUser);
    }
}
