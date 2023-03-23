package com.dqj.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqj.auth.service.SysRoleService;
import com.dqj.common.result.Result;
import com.dqj.model.system.SysRole;
import com.dqj.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    //  注入Service
    @Autowired
    private SysRoleService sysRoleService;

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
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable long page, @PathVariable long limit, SysRoleQueryVo sysRoleQueryVo){
        //  1.创建Page对象，传递相关分页参数
        Page<SysRole> pageParam = new Page<>(page, limit);
        //  2.封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        //  org.springframework.util包下的StringUtils工具类方法isEmpty判断条件
        if(!StringUtils.isEmpty(roleName)){
            //  封装
            wrapper.like(SysRole::getRoleName, roleName);
        }
        //  3.调用service的方法实现
        IPage<SysRole> rolePages = sysRoleService.page(pageParam, wrapper);
        return Result.ok(rolePages);
    }
}
