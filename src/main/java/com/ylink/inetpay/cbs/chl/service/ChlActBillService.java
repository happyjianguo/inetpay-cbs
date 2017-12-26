package com.ylink.inetpay.cbs.chl.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.account.dto.ActHistoryAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannelBank;
/**
 *  
 * @author haha
 *
 */
public interface ChlActBillService {
	/**
	 * 获取渠道
	 * @return
	 */
	List<TbChlBankAccount> findTbChlBankAccount();
	
	/**
	 * 查询账户表
	 * @param custId
	 * @return
	 */
	List<ActAccountDto> findActAccountDto(String custId);
	/**
	 * 渠道账户
	 * @param pageData
	 * @param queryParam
	 * @param actAccountDtoList 
	 * @return
	 */
	PageData<ActBillDto> findActBillDto(PageData<ActBillDto> pageData, ActBillDto queryParam, List<ActAccountDto> actAccountDtoList);
	/**
	 * 获取账户金额
	 * @param actAccountDtoList
	 * @param queryParam
	 * @return
	 */
	String getCashAmount(List<ActAccountDto> actAccountDtoList, ActBillDto queryParam);
	/**
	 * 获取数据导出
	 * @param queryParam
	 * @param actAccountDtoList 
	 * @return
	 */
	List<ActBillDto> queryAll(ActBillDto queryParam, List<ActAccountDto> actAccountDtoList);
	 
	 
}
