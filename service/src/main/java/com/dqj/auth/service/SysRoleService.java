package com.dqj.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dqj.model.system.SysRole;
import com.dqj.vo.system.AssginRoleVo;
import java.util.Map;

public interface SysRoleService extends IService<SysRole> {
    /**
     *  查询所有角色和当前用户所属角色
     * @param userId
     * @return
     */
    Map<String,Object> findRoleDataByUserId(Long userId);

    /**
     *  为用户分配角色
     * @param assginRoleVo
     */
    void doAssign(AssginRoleVo assginRoleVo);
}
