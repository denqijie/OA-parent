package com.dqj.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dqj.model.system.SysMenu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 菜单表 Mapper 接口
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 根据userId查询对应id操作的菜单列表
     * 多表关系查询：用户角色关系表，角色菜单关系表，菜单表
     */
    List<SysMenu> findMenuListByUserId(@Param("userId") Long userId);
}
