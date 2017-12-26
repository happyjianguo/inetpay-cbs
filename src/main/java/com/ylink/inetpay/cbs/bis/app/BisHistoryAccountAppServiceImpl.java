package com.ylink.inetpay.cbs.bis.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisHistoryAccountService;
import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;
import com.ylink.inetpay.common.project.cbs.app.BisHistoryAccountAppService;

@Service("bisHistoryAccountAppService")
public class BisHistoryAccountAppServiceImpl implements BisHistoryAccountAppService {
	@Autowired
	private BisHistoryAccountService actHistoryAccountService;

	@Override
	public PageData<ActHistoryAccountDto> getList(PageData<ActHistoryAccountDto> pageDate,
			ActHistoryAccountDto actHistoryAccountDto) {
		return actHistoryAccountService.getList(pageDate, actHistoryAccountDto);
	}

	@Override
	public ActHistoryAccountDto selectByAccountId(String accountId) {
		return actHistoryAccountService.selectByAccountId(accountId);
	}

	@Override
	public ActHistoryAccountDto selectByAccountIdAndAccountDate(String id, String accountDate) {
		return actHistoryAccountService.selectByAccountIdAndAccountDate(id,accountDate);
	}

}
