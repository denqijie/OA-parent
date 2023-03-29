package com.dqj.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqj.auth.service.SysUserService;
import com.dqj.common.result.Result;
import com.dqj.model.system.SysUser;
import com.dqj.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 *  用户管理CRUD接口
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    //  注入service
    @Autowired
    private SysUserService sysUserService;

    /**
     *  根据用户id更改用户的状态
     */
    @ApiOperation("更新状态")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status){
        sysUserService.updateStatus(id,status);
        return Result.ok();
    }

    /**
     *  用户条件分页查询
     *  page 表示当前页/ limit 表示每页显示记录数
     *  SysUserQueryVo 条件对象(username)
     */
    @ApiOperation("用户条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageQueryUser(@PathVariable Long page, @PathVariable Long limit, SysUserQueryVo sysUserQueryVo){
        //  1.创建Page对象，传递相关分页参数
        Page<SysUser> pageParam = new Page<>(page,limit);
        //  2.封装条件，判断条件，进行封装
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String username = sysUserQueryVo.getKeyword(); //关键字
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd(); //结束时间
        //  分页搜索框的查询条件
        if(!StringUtils.isEmpty(username)){
            //like表示模糊查询
            wrapper.like(SysUser::getUsername,username);
        }
        if(!StringUtils.isEmpty(createTimeBegin)){
            //ge表示大于等于 >
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)){
            //le表示小于等于 <
            wrapper.le(SysUser::getCreateTime,createTimeEnd);
        }
        //  3.调用service的方法实现
        IPage<SysUser> userPages = sysUserService.page(pageParam, wrapper);
        return Result.ok(userPages);
    }

    /**
     *  根据id获取用户
     */
    @ApiOperation("根据id获取用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id){
        //  调用service方法
        SysUser sysUser = sysUserService.getById(id);
        return Result.ok(sysUser);
    }

    /**
     *  保存用户
     */
    @ApiOperation("保存用户")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser sysUser){ //  @RequestBody通过请求体传递 以JSON格式封装数据
        sysUserService.save(sysUser);
        return Result.ok();
    }

    /**
     *  更新用户
     */
    @ApiOperation("更新用户")
    @PutMapping("/update")
    public Result update(@RequestBody SysUser sysUser){ //  @RequestBody通过请求体传递 以JSON格式封装数据
        sysUserService.updateById(sysUser);
        return Result.ok();
    }

    /**
     *  刪除用户
     */
    @ApiOperation("刪除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        sysUserService.removeById(id);
        return Result.ok();
    }
}

