package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.account.dto.ActRuleDto;

@MybatisMapper("actRuleDtoMapper")
public interface ActRuleDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(ActRuleDto record);

	int insertSelective(ActRuleDto record);

	ActRuleDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(ActRuleDto record);

	int updateByPrimaryKey(ActRuleDto record);

	/**
	 * 根据参数查询所有记账规则数据
	 * 
	 * @param ActRuleDto
	 * @return
	 */
	List<ActRuleDto> queryAllData(ActRuleDto actRuleDto);
    
	List<ActRuleDto> pageList();
}