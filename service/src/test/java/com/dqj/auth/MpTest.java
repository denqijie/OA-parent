package com.dqj.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dqj.auth.mapper.SysRoleMapper;
import com.dqj.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MpTest {

    //  注入
    @Autowired
    private SysRoleMapper sysRoleMapper;

    //  查询所有操作
    @Test
    public void getAll(){
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        System.out.println(sysRoles);
    }

    //  添加操作
    @Test
    public void add(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("角色管理员");
        sysRole.setRoleCode("role");
        sysRole.setDescription("角色管理员");
        sysRoleMapper.insert(sysRole);
    }

    //  修改操作
    @Test
    public void update(){
        SysRole sysRole = sysRoleMapper.selectById(9);
        sysRole.setDescription("3");
        sysRoleMapper.updateById(sysRole);
    }

    //  刪除操作(逻辑刪除)
    @Test
    public void IsDelete(){
        sysRoleMapper.deleteById(9);
    }

    //  批量刪除（逻辑刪除）
    @Test
    public void DeleteBatchIds(){
        sysRoleMapper.deleteBatchIds(Arrays.asList(1,2));
    }

    //  条件查询
    @Test
    public void Query(){
        //  创建QueryWapper对象，调用方法封装条件
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name","总经理");
        //  调用MP方法实现查询操作
        List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);
        System.out.println(sysRoles);
    }

    // lambda条件查询
    @Test
    public void LambdaQuery(){
        //  创建LLambdaQueryWrapper对象，调用方法封装条件
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getRoleName,"总经理");
        //  调用MP方法实现条件查询操作
        List<SysRole> sysRoles = sysRoleMapper.selectList(lambdaQueryWrapper);
        System.out.println(sysRoles);
    }
}
