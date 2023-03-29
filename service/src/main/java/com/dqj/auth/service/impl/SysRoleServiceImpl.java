package com.dqj.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqj.auth.mapper.SysRoleMapper;
import com.dqj.auth.service.SysRoleService;
import com.dqj.auth.service.SysUserRoleService;
import com.dqj.model.system.SysRole;
import com.dqj.model.system.SysUserRole;
import com.dqj.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     *  查询所有角色和当前用户所属角色
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        //1.查询所有角色 返回list集合
        List<SysRole> allRolesList = baseMapper.selectList(null);
        //2.根据userid查询用户关系角色表 查询到userid对应所有角色id
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,userId); //封装条件 根据用户id查询信息
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper); //查询当前用户分配的角色信息
          // 通俗写法
//        List<Long> roleId = new ArrayList<>();
//        for (SysUserRole sysUserRole : existUserRoleList) {
//            Long Id = sysUserRole.getRoleId();
//            roleId.add(Id);
//        }
        //  lambda写法 遍历用户对象获取所有的角色id
        List<Long> existRoleIdList  = existUserRoleList.stream().map(sysUserRole -> sysUserRole.getRoleId()).collect(Collectors.toList());
        //3.根据查询所有角色id 找到对应的角色信息 根据角色id到所有的角色list集合进行比较
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole sysRole : allRolesList) {
            //  比较 contains表示包含
            if(existRoleIdList.contains(sysRole.getId())){
                assignRoleList.add(sysRole);
            }
        }
        //4.把得到两部分数据封装到map集合中返回
        Map<String,Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList",assignRoleList);
        roleMap.put("allRolesList",allRolesList);
        return roleMap;
    }

    /**
     *  为用户分配角色
     * @param assginRoleVo
     */
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //把用户之前分配角色数据刪除 用户角色关系表根据userId刪除
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);
        //重新进行分配
        List<Long> roleIdList = assginRoleVo.getRoleIdList(); //角色id列表
        for (Long roleIds : roleIdList) {
            //  判断是否为空，为空跳出当前循环，否则添加数据
            if(StringUtils.isEmpty(roleIds)){
                continue;
            }
            //  数据
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleIds);
            sysUserRoleService.save(sysUserRole);
        }
    }
}
