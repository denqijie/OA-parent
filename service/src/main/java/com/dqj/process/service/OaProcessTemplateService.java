package com.dqj.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqj.model.process.ProcessTemplate;

/**
 * 审批模板 服务类
 */
public interface OaProcessTemplateService extends IService<ProcessTemplate> {
    /**
     * 分页查询审批模板 把审批类型对应的名称查询出来
     */
    IPage<ProcessTemplate> selectPageProcessTempate(Page<ProcessTemplate> pageParam);
}
