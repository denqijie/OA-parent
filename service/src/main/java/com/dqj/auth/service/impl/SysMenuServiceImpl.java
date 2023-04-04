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
import com.dqj.vo.system.MetaVo;
import com.dqj.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
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

    /**
     * 获取用户可以操作的菜单列表
     */
    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        //储存所有菜单数据
        List<SysMenu> sysMenuList = null;

        //1.判断当前用户是否管理员 如果是管理员可以查询所有菜单列表 userId=1表示管理员Id
        if(userId.longValue() == 1){
            //查询所有菜单列表
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
            //根据升序进行排列
            wrapper.orderByAsc(SysMenu::getSortValue);
            sysMenuList = baseMapper.selectList(wrapper);
        }else {
            //如果不是管理员 根据userId查询对应id操作的菜单列表
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        //2.把查询出来的数据列表 构成框架要求的路由数据结构
        //菜单工具类构建树形结构
        List<SysMenu> sysMenusTreeList = MenuHelper.buildTree(sysMenuList);
        //构成框架要求的路由数据结构
        List<RouterVo> routerVoList = this.buildRouter(sysMenusTreeList);
        //3.返回数据
        return routerVoList;
    }

    /**
     *  构成框架要求的路由数据结构
     */
    private List<RouterVo> buildRouter(List<SysMenu> sysMenusTreeList) {
        //创建list集合，用于存储最终数据
        List<RouterVo> routers = new ArrayList<>();
        //sysMenusTreeList 进行遍历
        for (SysMenu sysMenu : sysMenusTreeList) {
            RouterVo router = new RouterVo();
            router.setHidden(false);
            router.setAlwaysShow(false);
            //调用getRouterPath 获取路由地址
            router.setPath(getRouterPath(sysMenu));
            router.setComponent(sysMenu.getComponent());
            router.setMeta(new MetaVo(sysMenu.getName(), sysMenu.getIcon()));
            //下一层数据部分
            List<SysMenu> children = sysMenu.getChildren();
            if(sysMenu.getType().intValue() == 1){
                //加载下面隐藏路由 getComponent不为空则表示该路由是隐藏的
                List<SysMenu> hiddenMenuList = children.stream()
                        .filter(SysMenu -> !StringUtils.isEmpty(SysMenu.getComponent())) // getComponent过滤掉为空的数据
                        .collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    //hidden为true表示隐藏路由
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));
                    routers.add(hiddenRouter);
                }
            }else{
                //整理剩下不是隐藏路由的菜单
                if(!CollectionUtils.isEmpty(children)){
                    //判断
                    if(children.size() > 0){
                        router.setAlwaysShow(true);
                    }
                    //递归
                    router.setChildren(buildRouter(children));
                }
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取用户可以操作按钮列表（菜单列表的下级列表）
     */
    @Override
    public List<String> findUserPermsByUserId(Long userId) {
        //1.判断当前用户是否管理员 如果是管理员可以查询所有按钮列表 userId=1表示管理员Id
        List<SysMenu> sysMenuList = null;
        if(userId.longValue() == 1){
            //查询所有菜单列表
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysMenu::getStatus,1);
            sysMenuList = baseMapper.selectList(wrapper);
        }else{
            //2.多表关系查询：用户角色关系表，角色菜单关系表，菜单表
            sysMenuList = baseMapper.findMenuListByUserId(userId);
        }
        //3.从查询出来的数据里面 获取可以操作按钮值的list集合
        List<String> permsList = sysMenuList.stream()
                .filter(SysMenu -> SysMenu.getType() == 2)
                .map(SysMenu -> SysMenu.getPerms()) //perms权限标识
                .collect(Collectors.toList());
        //4.返回数据
        return permsList;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }
}
