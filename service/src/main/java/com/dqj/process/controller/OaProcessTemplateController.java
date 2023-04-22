package com.dqj.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqj.common.result.Result;
import com.dqj.model.process.ProcessTemplate;
import com.dqj.process.service.OaProcessTemplateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 审批模板 前端控制器
 */
@RestController
@RequestMapping("/admin/process/processTemplate")
public class OaProcessTemplateController {

    @Autowired
    private OaProcessTemplateService templateService;

    /**
     *  分页查询审批模板
     */
    @ApiOperation("获取分页审批模板的数据")
    @GetMapping("/{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit){
        Page<ProcessTemplate> pageParam = new Page<>(page, limit);
        //分页查询审批模板 把审批类型对应的名称查询出来
        IPage<ProcessTemplate> pageModel = templateService.selectPageProcessTempate(pageParam);
        return Result.ok(pageModel);
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.list')")
    @ApiOperation(value = "获取")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        ProcessTemplate processTemplate = templateService.getById(id);
        return Result.ok(processTemplate);
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        templateService.save(processTemplate);
        return Result.ok();
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.templateSet')")
    @ApiOperation(value = "修改")
    @PutMapping("/update")
    public Result updateById(@RequestBody ProcessTemplate processTemplate) {
        templateService.updateById(processTemplate);
        return Result.ok();
    }

    //@PreAuthorize("hasAuthority('bnt.processTemplate.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        templateService.removeById(id);
        return Result.ok();
    }
}

