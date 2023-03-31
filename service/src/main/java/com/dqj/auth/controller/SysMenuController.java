package com.dqj.auth.controller;

import com.dqj.auth.service.SysMenuService;
import com.dqj.common.result.Result;
import com.dqj.model.system.SysMenu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 菜单表 前端控制器
 */
@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    /**
     *  菜单列表
     */
    @ApiOperation("菜单列表")
    @GetMapping("/findNodes")
    public Result findNodes(){
        List<SysMenu> sysMenus = sysMenuService.findNodes();
        return Result.ok(sysMenus);
    }

    /**
     *  新增菜单
     */
    @ApiOperation("新增菜单")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu){
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    /**
     *  修改菜单
     */
    @ApiOperation("修改菜单")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysMenu sysMenu){
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    /**
     *  刪除菜单
     */
    @ApiOperation("刪除菜单")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }
}

