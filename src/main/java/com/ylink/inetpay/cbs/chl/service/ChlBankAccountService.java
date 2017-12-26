package com.ylink.inetpay.cbs.chl.service;

import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;

/**
 * 
 * 银行账户服务类
 * 
 * @author haha
 *
 */
public interface ChlBankAccountService {
	/**
	 * 银行账户列表（分页查询）
	 * 
	 * @param pageDate
	 * @param TbChlBankAccount
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<TbChlBankAccount> findListPage(PageData<TbChlBankAccount> pageDate,
			TbChlBankAccount tbChlbanAccountDto);
	
	
	public PageData<TbChlBankAccount> findBalancePage(PageData<TbChlBankAccount> pageDate,
			TbChlBankAccount tbChlbanAccountDto);


	public List<TbChlBankAccount> findList(TbChlBankAccount tbChlbanAccountDto);
	
	/**根据ID查找 **/
	public TbChlBankAccount findById(String id) ;
	/**
	 * 根据渠道编码查找
	 * @param channelCode
	 * @return
	 */
	public TbChlBankAccount findByChannelCode(String channelCode);
	
	/**
	 * 修改银行账户
	 * @param tbChlBankDto
	 * @throws CbsCheckedException
	 */
	public void updateBank(TbChlBankAccount tbChlbanAccountDto);

	/**
	 * 根据用户编号获取用户银行卡号列表
	 * @param userId
	 * @return
	 */
	public List<TbChlBankAccount> getUserBankAccList();

	/**
	 * 根据银行卡号获取银行卡号信息
	 * @param goldBankAccNo
	 * @return
	 */
	public TbChlBankAccount getChlBankAccount(String goldBankAccNo);

	/**
	 * 根据银行行号获取银行卡号列表
	 * @param goldBankCode
	 * @return
	 */
	public List<TbChlBankAccount> chlBankService(String outerAccountBankType);

	/**
	 * 根据出金人银行账号获取银行信息
	 * @param outerAcct
	 * @return
	 */
	public TbChlBankAccount findByBankAccNo(String outerAcct);
	
	/**
	 * 获取平台所有银行卡信息
	 */
	public List<TbChlBankAccount> getBankAccNoList();

	/**
	 * 获取系统银行类型
	 * @return
	 */
	public List<TbChlBankAccount> getBankTypes();

	/**
	 * 获取保交所银行
	 * @return
	 */
	public Map<String, TbChlBankAccount> findChannelBankMap();

}
