package com.dqj.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqj.model.process.ProcessTemplate;
import com.dqj.model.process.ProcessType;
import com.dqj.process.mapper.OaProcessTemplateMapper;
import com.dqj.process.service.OaProcessTemplateService;
import com.dqj.process.service.OaProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 审批模板 服务实现类
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {

    @Autowired
    private OaProcessTypeService oaProcessTypeService;

    /**
     *  分页查询审批模板 把审批类型对应的名称查询出来
     */
    @Override
    public IPage<ProcessTemplate> selectPageProcessTempate(Page<ProcessTemplate> pageParam) {
        //1.调用baseMapper方法实现分页查询
        Page<ProcessTemplate> processTemplatePage = baseMapper.selectPage(pageParam, null);
        //2.分页查询返回分页数据 从分页数据中获取列表list集合 getRecords()表示记录数
        List<ProcessTemplate> processTemplateList = processTemplatePage.getRecords();
        //3.遍历list集合 得到每个对象的审批类型id
        for (ProcessTemplate template : processTemplateList) {
            //得到每个对象的审批类型id
            Long processTypeId = template.getProcessTypeId();
            //根据审批类型id 查询获取对应名称
            LambdaQueryWrapper<ProcessType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessType::getId,processTypeId);
            ProcessType processType = oaProcessTypeService.getOne(wrapper);
            if(StringUtils.isEmpty(processType)){
                continue;
            }
            //完成最终封装processTypeName
            template.setProcessTypeName(processType.getName());
        }
        return processTemplatePage;
    }
}
