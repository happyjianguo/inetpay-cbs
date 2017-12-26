package com.ylink.inetpay.cbs.chl.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.exception.BusinessRuntimeException;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.CbsChannelBankService;
import com.ylink.inetpay.common.project.cbs.app.CbsChannelBankAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.ChlChannelBankAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannelBank;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;
/** 渠道银行表**/
@Service("cbsChannelBankAppService")
public class CbsChannelBankAppServiceImpl implements CbsChannelBankAppService {
	
	@Autowired
	private CbsChannelBankService chlBankService;
	@Autowired
	private ChlChannelBankAppService chlChannelBankSer;
	

	@Override
	public PageData<TbChlChannelBank> findListPage(PageData<TbChlChannelBank> pageDate,
			TbChlChannelBank tbChlChannelDto) {
		return chlBankService.findListPage(pageDate, tbChlChannelDto);
	}

	@Override
	public List<TbChlChannelBank> findList(TbChlChannelBank TbChlChannelBankDto) throws CbsCheckedException{
		return chlBankService.findListPage(TbChlChannelBankDto);
	}

	@Override
	public void updateBank(TbChlChannelBank tbChlBankDto) throws CbsCheckedException {
		try {
			chlChannelBankSer.updateByPrimaryKeySelective(tbChlBankDto);
		} catch (ChannelCheckedException e) {
			throw new BusinessRuntimeException("更新渠道银行信息失败"+e.getMessage());
		}
	}

	@Override
	public void addChannelBank(TbChlChannelBank tbChlBankDto) throws CbsCheckedException {
		try {
			chlChannelBankSer.addChannelBank(tbChlBankDto);
		} catch (ChannelCheckedException e) {
			throw new BusinessRuntimeException("新增渠道银行信息失败"+e.getMessage());
		}
	}
	
	@Override
	public void delById(String id) throws CbsCheckedException{
		try {
			chlChannelBankSer.delById(id);
		} catch (ChannelCheckedException e) {
			throw new BusinessRuntimeException("删除渠道银行信息失败"+e.getMessage());
		}
	}
	
	@Override
	public TbChlChannelBank findById(String id) throws CbsCheckedException{
		return chlBankService.findById(id);
	}
}
