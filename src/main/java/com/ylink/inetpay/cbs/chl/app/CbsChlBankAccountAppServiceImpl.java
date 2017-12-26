package com.ylink.inetpay.cbs.chl.app;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlBankAccountService;
import com.ylink.inetpay.common.project.cbs.app.CbsChlBankAccountAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.ChlBankAccountAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlBankAccount;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;
@Service("cbsChlBankAccountAppService")
public class CbsChlBankAccountAppServiceImpl implements CbsChlBankAccountAppService {
	
	@Autowired
	private ChlBankAccountService chlBankService;
	@Autowired
	private ChlBankAccountAppService chlBankAccountAppService;
	

	@Override
	public PageData<TbChlBankAccount> findListPage(PageData<TbChlBankAccount> pageDate,
			TbChlBankAccount tbChlChannelDto) {
		return chlBankService.findListPage(pageDate, tbChlChannelDto);
	}
	
	@Override
	public PageData<TbChlBankAccount> findBalancePage(PageData<TbChlBankAccount> pageDate,
			TbChlBankAccount tbChlChannelDto) throws CbsCheckedException{
		return chlBankService.findBalancePage(pageDate, tbChlChannelDto);
	}

	@Override
	public List<TbChlBankAccount> findList(TbChlBankAccount tbChlBankDto) throws CbsCheckedException{
		return chlBankService.findList(tbChlBankDto);
	}
	
	
	public TbChlBankAccount findByChannelCode(String channelCode) throws CbsCheckedException{
		return chlBankService.findByChannelCode(channelCode);
	}

	/**根据ID查找 **/
	public TbChlBankAccount findById(String id) throws CbsCheckedException{
		return chlBankService.findById(id);
	}
	
	@Override
	public void updateBank(TbChlBankAccount tbChlBankDto)
			throws CbsCheckedException {
		try {
			chlBankAccountAppService.updateBank(tbChlBankDto);
		} catch (ChannelCheckedException e) {
			throw new CbsCheckedException("99","更新银行账户信息失败");
		}
	}

	@Override
	public List<TbChlBankAccount> getUserBankAccList() {
		return chlBankService.getUserBankAccList();
	}

	@Override
	public TbChlBankAccount getChlBankAccount(String goldBankAccNo) {
		return chlBankService.getChlBankAccount(goldBankAccNo);
	}

	@Override
	public List<TbChlBankAccount> getGoldBankAccNo(String outerAccountBankType) {
		return chlBankService.chlBankService(outerAccountBankType);
	}

	@Override
	public List<TbChlBankAccount> getBankAccNoList() {
		return chlBankService.getBankAccNoList();
	}

	@Override
	public List<TbChlBankAccount> getBankTypes() {
		return chlBankService.getBankTypes();
	}

	@Override
	public Map<String, TbChlBankAccount> findChannelBankMap() {
		return chlBankService.findChannelBankMap();
	}
}
