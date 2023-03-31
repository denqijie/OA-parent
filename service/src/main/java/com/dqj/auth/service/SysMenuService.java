package com.dqj.auth.service;

import com.dqj.model.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
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
}
