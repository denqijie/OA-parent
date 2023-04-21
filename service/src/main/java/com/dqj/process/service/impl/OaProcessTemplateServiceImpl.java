package com.dqj.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqj.model.process.ProcessTemplate;
import com.dqj.process.mapper.OaProcessTemplateMapper;
import com.dqj.process.service.OaProcessTemplateService;
import org.springframework.stereotype.Service;

/**
 * 审批模板 服务实现类
 */
@Service
public class OaProcessTemplateServiceImpl extends ServiceImpl<OaProcessTemplateMapper, ProcessTemplate> implements OaProcessTemplateService {

}
