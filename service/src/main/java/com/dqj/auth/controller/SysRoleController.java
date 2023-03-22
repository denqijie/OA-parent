package com.dqj.auth.controller;

import com.dqj.auth.service.SysRoleService;
import com.dqj.common.result.Result;
import com.dqj.model.system.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    //  注入Service
    @Autowired
    private SysRoleService sysRoleService;

    //  查询所有角色 返回统一数据结果
    @GetMapping("/findAll")
    public Result findALL(){
        //  调用service方法实现查询所有角色操作
        List<SysRole> sysRoles = sysRoleService.list();
        return Result.ok(sysRoles);
    }
}
