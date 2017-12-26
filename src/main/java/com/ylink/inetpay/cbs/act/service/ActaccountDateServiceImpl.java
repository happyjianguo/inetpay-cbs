package com.ylink.inetpay.cbs.act.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.act.cache.ActaccountDateDtoCache;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.account.dto.ActaccountDateDto;

@Service("actaccountDateService")
public class ActaccountDateServiceImpl implements ActaccountDateService {

	@Autowired
	ActaccountDateDtoCache actaccountDateDtoCache;

	@Override
	public ActaccountDateDto getActAccountDateDto() {
		return actaccountDateDtoCache.selectByPrimaryKey(SystemParamConstants.ACCOUNT_DATE_KEY);
	}

	@Override
	public String getAccountDate() {
		ActaccountDateDto  actaccountDateDto=actaccountDateDtoCache.selectByPrimaryKey(SystemParamConstants.ACCOUNT_DATE_KEY);
		return actaccountDateDto.getCurAccountDate();
	}

	@Override
	public ActaccountDateDto selectByPrimaryKey(String id) {
		return actaccountDateDtoCache.selectByPrimaryKey(id);
	}

	 


}
