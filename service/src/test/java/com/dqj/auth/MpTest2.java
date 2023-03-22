package com.dqj.auth;

import com.dqj.auth.service.SysRoleService;
import com.dqj.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
public class MpTest2 {

    //  注入
    @Autowired
    private SysRoleService sysRoleService;

    //  查询所有操作
    @Test
    public void getAll(){
        List<SysRole> sysRoles = sysRoleService.list();
        System.out.println(sysRoles);
    }

}
