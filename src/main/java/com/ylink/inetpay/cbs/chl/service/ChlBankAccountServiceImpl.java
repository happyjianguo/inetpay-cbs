package com.ylink.inetpay.cbs.chl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.chl.cache.TbChlBankAccountDtoCache;
import com.ylink.inetpay.cbs.chl.dao.TbChlBankAccountMapper;
import com.ylink.inetpay.common.core.constant.EChlReturnCode;
import com.ylink.inetpay.common.project.channel.app.AccountInfoQueryAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;
import com.ylink.inetpay.common.project.channel.dto.response.AccountRespPojo;
@Service("chlBankAccountService")
public class ChlBankAccountServiceImpl implements ChlBankAccountService {
	
	@Autowired
	private TbChlBankAccountMapper chlBankAccountMapper;
	@Autowired
	private TbChlBankAccountDtoCache tbChlBankAccountDtoCache;
	/** 余额查询**/
	@Autowired
	private AccountInfoQueryAppService accountInfoQueryAppService;
	

	@Override
	public PageData<TbChlBankAccount> findListPage(PageData<TbChlBankAccount> pageDate,
			TbChlBankAccount tbChlChannelDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlBankAccount> findListPage=chlBankAccountMapper.findListPage(tbChlChannelDto);
		Page<TbChlBankAccount> page=(Page<TbChlBankAccount>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(findListPage);
		return pageDate;
	}
	
	@Override
	public PageData<TbChlBankAccount> findBalancePage(PageData<TbChlBankAccount> pageDate,
			TbChlBankAccount tbChlChannelDto) {
		PageHelper.startPage(pageDate.getPageNumber(),pageDate.getPageSize());
		List<TbChlBankAccount> findListPage=chlBankAccountMapper.findListPage(tbChlChannelDto);
		List<TbChlBankAccount> accountList = reduceBankAccount(findListPage);
		for (TbChlBankAccount bank :accountList) {
				AccountRespPojo accountResp = accountInfoQueryAppService.queryAccBalance(bank.getCustId());
				if (accountResp.getRetrunCode() == EChlReturnCode.SUCCESS) {
					bank.setCurrentBalance(accountResp.getCurrentBalance());
					bank.setUseBalance(accountResp.getUseBalance());
				}else {
					bank.setCurrentBalance("0");
					bank.setUseBalance("0");
					//throw new BusinessRuntimeException("调渠道接口获取银行余额失败");
				}
		}
		Page<TbChlBankAccount> page=(Page<TbChlBankAccount>) findListPage;
		pageDate.setTotal(page.getTotal());
		pageDate.setRows(accountList);
		return pageDate;
	}

	@Override
	public List<TbChlBankAccount> findList(TbChlBankAccount tbChlBankDto){
		return chlBankAccountMapper.findListPage(tbChlBankDto);
	}

	/**根据ID查找 **/
	public TbChlBankAccount findById(String id) {
		return chlBankAccountMapper.selectByPrimaryKey(id);
	}
	
	/**根据渠道编码查找银行账户 **/
	public TbChlBankAccount findByChannelCode(String channelCode){
		return tbChlBankAccountDtoCache.findByChannelCode(channelCode);
		
	}
	@Override
	public void updateBank(TbChlBankAccount tbChlBankDto) {
			
	}

	@Override
	public List<TbChlBankAccount> getUserBankAccList() {
		return chlBankAccountMapper.getUserBankAccList();
	}

	@Override
	public TbChlBankAccount getChlBankAccount(String goldBankAccNo) {
		return chlBankAccountMapper.getChlBankAccount(goldBankAccNo);
	}

	@Override
	public List<TbChlBankAccount> chlBankService(String outerAccountBankType) {
		return tbChlBankAccountDtoCache.chlBankService(outerAccountBankType);
	}

	@Override
	public TbChlBankAccount findByBankAccNo(String outerAcct) {
		return tbChlBankAccountDtoCache.findByBankAccNo(outerAcct);
	}

	@Override
	public List<TbChlBankAccount> getBankAccNoList() {
		return chlBankAccountMapper.getBankAccNoList();
	}

	@Override
	public List<TbChlBankAccount> getBankTypes() {
		return chlBankAccountMapper.getBankTypes();
	}
	
	/**
	 * 筛选银行名称
	 * @param list
	 * @return
	 */
	public List<TbChlBankAccount> reduceBankAccount(List<TbChlBankAccount> list){
		List<TbChlBankAccount> acctList = new ArrayList<TbChlBankAccount>();
	    for(TbChlBankAccount bank:list){
	    	if(!bank.getBankType().equals("002")&&!bank.getBankType().equals("003")){
	    		acctList.add(bank);
	    	}
	    }
	    return acctList;
	}

	@Override
	public Map<String, TbChlBankAccount> findChannelBankMap() {
		List<TbChlBankAccount> channelBankList = chlBankAccountMapper.getBatchExpBankAccount();
		HashMap<String, TbChlBankAccount> channelBankMap = new HashMap<String ,TbChlBankAccount>();
		if(channelBankList!=null && !channelBankList.isEmpty()){
			for (TbChlBankAccount dto : channelBankList) {
				channelBankMap.put(dto.getBankAccNo(), dto);
			}
		}
		return channelBankMap;
	}
}
