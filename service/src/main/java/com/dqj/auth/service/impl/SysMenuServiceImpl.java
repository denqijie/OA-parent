package com.dqj.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dqj.auth.service.SysRoleMenuService;
import com.dqj.auth.util.MenuHelper;
import com.dqj.common.config.exception.MyException;
import com.dqj.model.system.SysMenu;
import com.dqj.auth.mapper.SysMenuMapper;
import com.dqj.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqj.model.system.SysRoleMenu;
import com.dqj.vo.system.AssginMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单表 服务实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

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

    /**
     *  查询所有菜单和角色分配菜单
     */
    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {

        //1.查询所有菜单 添加条件 status = 1
        LambdaQueryWrapper<SysMenu> sysMenuWrapper = new LambdaQueryWrapper<>();
        sysMenuWrapper.eq(SysMenu::getStatus,1);
        List<SysMenu> sysMenuList = baseMapper.selectList(sysMenuWrapper);

        //2.根据角色id roleId进行查询 角色菜单关系表里面角色id对应的所有菜单id
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuWrapper.eq(SysRoleMenu::getRoleId,roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(sysRoleMenuWrapper);

        //3.根据菜单id，获取对应的菜单对象（用菜单id和所有菜单集合里面id进行比较，如果相同进行封装）
        //steam流写法
        List<Long> menuIdList = sysRoleMenuList.stream().map(SysRoleMenu -> SysRoleMenu.getMenuId()).collect(Collectors.toList());
        sysMenuList.stream().forEach(SysMenu -> {
            if(menuIdList.contains(SysMenu.getId())){
                SysMenu.setSelect(true);
            }else{
                SysMenu.setSelect(false);
            }
        });

        //4.返回规定树形显示格式菜单列表
        List<SysMenu> tree = MenuHelper.buildTree(sysMenuList);
        return tree;
    }

    /**
     *  角色分配菜单
     */
    @Override
    public void doAssign(AssginMenuVo assginMenuVo) {

        //1.根据角色id 刪除菜单角色表 分配数据
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId,assginMenuVo.getRoleId());
        sysRoleMenuService.remove(wrapper);

        //2.从参数里面获取角色新分配菜单id列表，进行遍历，把每个id数据添加菜单角色表
        List<Long> menuIdList = assginMenuVo.getMenuIdList();
        for (Long menuId : menuIdList) {
            if(StringUtils.isEmpty(menuId)){
                continue;
            }
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(assginMenuVo.getRoleId());
            sysRoleMenuService.save(sysRoleMenu);
        }
    }
}
