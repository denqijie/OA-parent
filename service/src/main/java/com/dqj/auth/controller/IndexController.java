package com.dqj.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dqj.auth.service.SysMenuService;
import com.dqj.auth.service.SysUserService;
import com.dqj.common.config.exception.MyException;
import com.dqj.common.jwt.JwtHelper;
import com.dqj.common.result.Result;
import com.dqj.common.utils.MD5;
import com.dqj.model.system.SysUser;
import com.dqj.vo.system.LoginVo;
import com.dqj.vo.system.RouterVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        //1.获取用户名
        String username_input = loginVo.getUsername();
        //2.根据用户名查询数据库
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username_input);
        SysUser sysUser = sysUserService.getOne(wrapper);
        //3.用户信息是否存在
        if(sysUser == null){
            throw new MyException(201,"用户不存在！");
        }
        //4.验证账户密码
        String password_db = sysUser.getPassword();
        String password_input = MD5.encrypt(loginVo.getPassword());
        if(!password_db.equals(password_input)){
            throw new MyException(201,"密码错误，请验证密码！");
        }
        //5.验证账户状态（是否被禁用）
        if(sysUser.getStatus().intValue() == 0){
            throw new MyException(201,"用户禁用，请联系管理员！");
        }
        //6.使用jwt根据用户id和用户username 生成token字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        //7.返回
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }

    /**
     *  获取用户信息
     */
    @GetMapping("/info")
    public Result info(HttpServletRequest request){
        //1.从请求头获取用户信息（获取请求头token字符串）
        String token = request.getHeader("token");
        //2.从token字符串中获取用户id或者用户username
        Long userId = JwtHelper.getUserId(token);
        //3.根据用户id查询用户信息
        SysUser sysUser = sysUserService.getById(userId);
        //4.获取用户可以操作的菜单列表（查询数据库构建动态路由结构，进行显示）
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);
        //5.获取用户可以操作按钮列表（菜单列表的下级列表）
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);
        //6.返回相应的数据
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        //返回用户可以操作菜单
        map.put("routers",routerList);
        //返回用户可以操作按钮
        map.put("buttons",permsList);
        return Result.ok(map);
    }

    /**
     *  退出登录
     */
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }

}
