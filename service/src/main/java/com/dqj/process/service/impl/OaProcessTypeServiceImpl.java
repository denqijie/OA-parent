package com.dqj.process.service.impl;

import com.dqj.model.process.ProcessType;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqj.process.mapper.OaProcessTypeMapper;
import com.dqj.process.service.OaProcessTypeService;
import org.springframework.stereotype.Service;

/**
 * 审批类型 服务实现类
 */
@Service
public class OaProcessTypeServiceImpl extends ServiceImpl<OaProcessTypeMapper, ProcessType> implements OaProcessTypeService {

}
