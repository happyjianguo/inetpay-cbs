package com.ylink.inetpay.cbs.chl.app;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlBindBankCardService;
import com.ylink.inetpay.common.project.cbs.app.ChlUserBindBankAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlBindBankCard;
@Service("chlUserBindBankAppService")
public class ChlUserBindBankAppServiceImpl implements ChlUserBindBankAppService {
	@Autowired
	private ChlBindBankCardService chlBindBankCardService;
	
	private static Logger _log = LoggerFactory.getLogger(ChlUserBindBankAppServiceImpl.class);

	@Override
	public List<TbChlBindBankCard> findByChlBankProp(TbChlBindBankCard userBankDto) {
		return chlBindBankCardService.findByChlBankProp(userBankDto);
	}

	@Override
	public TbChlBindBankCard findById(String id){
		return chlBindBankCardService.findById(id);
	}
	
	@Override
	public void updateBankById(TbChlBindBankCard userBankDto) {
		
	}

	@Override
	public PageData<TbChlBindBankCard> pageData(PageData<TbChlBindBankCard> pageData, TbChlBindBankCard searchDto) {
		return chlBindBankCardService.pageData(pageData, searchDto);
	}

	@Override
	public List<TbChlBindBankCard> listBankCard(TbChlBindBankCard searchDto) {
		return chlBindBankCardService.listBankCard(searchDto);
	}
	
	
    
}
