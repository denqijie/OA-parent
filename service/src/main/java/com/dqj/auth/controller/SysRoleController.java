package com.dqj.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqj.auth.service.SysRoleService;
import com.dqj.common.result.Result;
import com.dqj.model.system.SysRole;
import com.dqj.vo.system.AssginRoleVo;
import com.dqj.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 *  角色管理CRUD接口
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    //  注入Service
    @Autowired
    private SysRoleService sysRoleService;

    /**
     *  根据用户获取角色数据
     */
    @ApiOperation("根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId){
        Map<String,Object> map = sysRoleService.findRoleDataByUserId(userId);
        return Result.ok(map);
    }

    /**
     *  根据用户分配角色
     */
    @ApiOperation("根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }

    /**
     *  根据id查询角色
     */
    @ApiOperation("根据ID查询角色")
    @GetMapping("/find/{id}")
    public Result find(@PathVariable Long id){
        //  根据id查询数据
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    /**
     *  查询所有角色 返回统一数据结果
     */
    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result findALL(){
        //  调用service方法实现查询所有角色操作
        List<SysRole> sysRoles = sysRoleService.list();
        return Result.ok(sysRoles);
    }

    /**
     *  条件分页查询
     *  page 当前页/limit 每页显示记录数
     *  SysRoleQueryVo 条件对象(roleName)
     */
    @ApiOperation("条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page, @PathVariable Long limit, SysRoleQueryVo sysRoleQueryVo){
        //  1.创建Page对象，传递相关分页参数
        Page<SysRole> pageParam = new Page<>(page, limit);
        //  2.封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        //  org.springframework.util包下的StringUtils工具类方法isEmpty判断条件
        if(!StringUtils.isEmpty(roleName)){
            //  封装 分页搜索框的查询条件
            wrapper.like(SysRole::getRoleName, roleName);
        }
        //  3.调用service的方法实现
        IPage<SysRole> rolePages = sysRoleService.page(pageParam, wrapper);
        return Result.ok(rolePages);
    }

    /**
     *  添加角色
     */
    @ApiOperation("添加角色") // api文档中文注释
    @PostMapping("/save")
    public Result save(@RequestBody SysRole sysRole){   //  @RequestBody通过请求体传递 以JSON格式封装数据
        //  调用service方法实现
        boolean is_success = sysRoleService.save(sysRole);
        //  判断条件
        if(is_success){
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     *  修改角色
     */
    @ApiOperation("修改角色") // api文档中文注释
    @PutMapping("/update")
    public Result update(@RequestBody SysRole sysRole){   //  @RequestBody通过请求体传递 以JSON格式封装数据
        //  调用service方法实现
        boolean is_success = sysRoleService.updateById(sysRole);
        //  判断条件
        if(is_success){
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     *  根据id刪除
     */
    @ApiOperation("根据ID刪除角色")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        //  调用service方法实现
        boolean is_success = sysRoleService.removeById(id);
        //  判断条件
        if(is_success){
            return Result.ok();
        }
        return Result.fail();
    }

    /**
     *  批量刪除
     */
    @ApiOperation("批量刪除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){ //  @RequestBody通过请求体传递 以JSON格式封装数据
        //  list集合转成JSON数组格式
        boolean is_success = sysRoleService.removeByIds(idList);
        //  判断条件
        if(is_success){
            return Result.ok();
        }
        return Result.fail();
    }

}
