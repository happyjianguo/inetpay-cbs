package com.ylink.inetpay.cbs.act.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.account.dto.ActRuleDto;

public interface ActRuleService {
	/**
	 * 根据参数查询所有记账规则数据
	 * 
	 * @param pageDate
	 * @param ActRuleDto
	 * @return
	 */
	PageData<ActRuleDto> queryAllData(PageData<ActRuleDto> pageDate,
			ActRuleDto actRuleDto);
	
	ActRuleDto findById(String id);
    
	List<ActRuleDto> pageList();
}
