package com.ylink.inetpay.cbs.act.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.act.service.ActRuleService;
import com.ylink.inetpay.common.project.account.app.ActRuleAppService;
import com.ylink.inetpay.common.project.account.dto.ActRuleDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActRuleAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("cbsActRuleAppService")
public class ActRuleAppServiceImpl implements CbsActRuleAppService {
	@Autowired
	ActRuleService actRuleService;

	@Autowired
	ActRuleAppService actRuleAppService;
	
	@Override
	public PageData<ActRuleDto> queryAllData(PageData<ActRuleDto> pageDate,
			ActRuleDto actRuleDto) {
		return actRuleService.queryAllData(pageDate, actRuleDto);
	}

	@Override
	public ActRuleDto findById(String id) throws CbsCheckedException {
		return actRuleService.findById(id);
	}

	@Override
	public List<ActRuleDto> pageList() {
		return actRuleService.pageList();
	}
}
