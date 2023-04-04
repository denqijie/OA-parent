package com.dqj.auth.service;

import com.dqj.model.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqj.vo.system.AssginMenuVo;
import com.dqj.vo.system.RouterVo;

import java.util.List;

/**
 * 菜单表 服务类
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     *  菜单列表
     */
    List<SysMenu> findNodes();

    /**
     *  刪除菜单
     */
    void removeMenuById(Long id);

    /**
     *  查询所有菜单和角色分配菜单
     */
    List<SysMenu> findMenuByRoleId(Long id);

    /**
     *  角色分配菜单
     */
    void doAssign(AssginMenuVo assginMenuVo);

    /**
     *  获取用户可以操作的菜单列表
     */
    List<RouterVo> findUserMenuListByUserId(Long userId);

    /**
     *  获取用户可以操作按钮列表（菜单列表的下级列表）
     */
    List<String> findUserPermsByUserId(Long userId);
}
