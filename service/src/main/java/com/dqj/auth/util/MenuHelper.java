package com.dqj.auth.util;

import com.dqj.model.system.SysMenu;
import java.util.ArrayList;
import java.util.List;

/**
 *  构建成树形的结构
 */
public class MenuHelper {

    /**
     *  使用递归方法建菜单
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        //创建list集合，用于最终数据
        List<SysMenu> trees = new ArrayList<>();
        //把所有菜单数据进行遍历
        for (SysMenu sysMenu : sysMenuList) {
            //递归入口进入    parentId=0是入口
            if(sysMenu.getParentId().longValue() == 0){
                trees.add(getChildren(sysMenu,sysMenuList));
            }
        }
        return trees;
    }

    /**
     *  递归逻辑
     */
    public static SysMenu getChildren(SysMenu sysMenu,List<SysMenu> sysMenuList){
        //存放相关数据
        sysMenu.setChildren(new ArrayList<SysMenu>());
        //遍历所有菜单的数据，判断id和parentId的对应关系
        for (SysMenu menus : sysMenuList) {
            if(sysMenu.getId().longValue() == menus.getParentId().longValue()){
                //如果当前列表的下级列表为空，初始化值重新创建list集合
                if(sysMenu.getChildren() == null){
                    sysMenu.setChildren(new ArrayList<>());
                }
                // 递归循环
                //getChildren(menus,sysMenuList);
                //sysMenu.getChildren().add(menus);
                sysMenu.getChildren().add(getChildren(menus,sysMenuList));
            }
        }
        return sysMenu;
    }
}
