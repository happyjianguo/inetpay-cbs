package com.ylink.inetpay.cbs.chl.app;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.common.project.cbs.app.ChlBankAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
/** 银行表**/
@Service("chlBankAppService")
public class ChlBankAppServiceImpl implements ChlBankAppService {
	
	@Autowired
	private ChlBankService chlBankService;
	

	@Override
	public PageData<TbChlBank> findListPage(PageData<TbChlBank> pageDate,
			TbChlBank tbChlChannelDto) {
		return chlBankService.findListPage(pageDate, tbChlChannelDto);
	}
	
	@Override
	public PageData<TbChlBank> getInnerBanks(PageData<TbChlBank> pageDate,
			TbChlBank tbChlChannelDto) {
		return chlBankService.getInnerBanks(pageDate, tbChlChannelDto);
	}

	@Override
	public List<TbChlBank> findList(TbChlBank tbChlBankDto) throws CbsCheckedException{
		return chlBankService.findListPage(tbChlBankDto);
	}

	@Override
	public void updateBank(TbChlBank tbChlBankDto)
			throws CbsCheckedException {
	}

	@Override
	public List<TbChlBank> getBanks() throws CbsCheckedException {
		return chlBankService.getBanks();
	}

	@Override
	public List<TbChlBank> getBankType() {
		return chlBankService.getBankType();
	}

	@Override
	public TbChlBank getBankByBankType(String bankType) throws CbsCheckedException {
		return chlBankService.getBankByBankType(bankType);
	}

	@Override
	public List<TbChlBank> getChannels() {
		return chlBankService.getChannels();
	}

	@Override
	public List<TbChlBank> findListByChannelType(String channelType) {
		return chlBankService.findListByChannelType(channelType);
	}

	@Override
	public Map<String, TbChlBank> findChannelBankMap() {
		return chlBankService.findChannelBankMap();
	}
}
