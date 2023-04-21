package com.dqj.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dqj.model.process.ProcessType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审批类型 Mapper 接口
 */
@Mapper
public interface OaProcessTypeMapper extends BaseMapper<ProcessType> {

}
