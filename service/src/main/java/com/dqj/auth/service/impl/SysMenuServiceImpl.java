package com.dqj.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dqj.auth.util.MenuHelper;
import com.dqj.common.config.exception.MyException;
import com.dqj.model.system.SysMenu;
import com.dqj.auth.mapper.SysMenuMapper;
import com.dqj.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 菜单表 服务实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    /**
     *  菜单列表
     */
    @Override
    public List<SysMenu> findNodes() {
        //1.查询所有菜单数据
        List<SysMenu> sysMenuList = baseMapper.selectList(null);
        //2.构建成树形的结构
        List<SysMenu> resultList = MenuHelper.buildTree(sysMenuList);
        return resultList;
    }

    /**
     *  刪除菜单
     */
    @Override
    public void removeMenuById(Long id) {
        //判断当前菜单是否有子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId,id);
        Integer selectCount = baseMapper.selectCount(wrapper);
        //判断返回结果，大于0则表示含有下级菜单
        if(selectCount > 0){
            throw new MyException(201,"含有下级菜单，不能刪除！");
        }
        baseMapper.deleteById(id);
    }
}
