package com.ylink.inetpay.cbs.chl.app;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlActBillService;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;
import com.ylink.inetpay.common.project.channel.app.ChlActBillAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

  /**
   * 渠道出入账明细报表
   * @author yc
   *
   */
@Service("chlActBillAppService")
public class ChlActBillAppServiceImpl implements ChlActBillAppService {
	 @Autowired
	 private  ChlActBillService chlActBillService;

	@Override
	public List<TbChlBankAccount> findTbChlBankAccount() {
		 
		return chlActBillService.findTbChlBankAccount();
	}

	@Override
	public List<ActAccountDto> findActAccountDto(String custId) {
	 
		return chlActBillService.findActAccountDto(custId);
	}

	@Override
	public PageData<ActBillDto> findActBillDto(PageData<ActBillDto> pageData, ActBillDto queryParam, List<ActAccountDto> actAccountDtoList) {
		 
		return chlActBillService.findActBillDto(pageData,queryParam,actAccountDtoList);
	}

	@Override
	public String getCashAmount(List<ActAccountDto> actAccountDtoList, ActBillDto queryParam) {
		queryParam.setCashAmount(queryParam.getEarlyTime());
		return chlActBillService.getCashAmount(actAccountDtoList,queryParam);
	}
	
	/**
	 * 获取期末金额
	 */
	@Override
	public String getSemesterTimeCashAmount(List<ActAccountDto> actAccountDtoList, ActBillDto queryParam) {
		queryParam.setCashAmount(queryParam.getSemesterTime());
		return chlActBillService.getCashAmount(actAccountDtoList,queryParam);
	}

	@Override
	public List<ActBillDto> queryAll(ActBillDto queryParam,List<ActAccountDto> actAccountDtoList) {
		// TODO Auto-generated method stub
		return chlActBillService.queryAll(queryParam,actAccountDtoList);
	}
 
	
	 
}
