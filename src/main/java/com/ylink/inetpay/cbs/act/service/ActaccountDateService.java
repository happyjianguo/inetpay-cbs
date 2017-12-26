package com.ylink.inetpay.cbs.act.service;

import java.util.List;

import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.account.dto.ActaccountDateDto;

public interface ActaccountDateService {

	public ActaccountDateDto getActAccountDateDto();
	/**
	 *获取账务日期
	 * @return
	 */
	public String getAccountDate();
	ActaccountDateDto selectByPrimaryKey(String id);
	
	 
}
