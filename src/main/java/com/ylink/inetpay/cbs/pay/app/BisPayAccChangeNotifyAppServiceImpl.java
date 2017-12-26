package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayAccChangeNotifyService;
import com.ylink.inetpay.common.project.cbs.BisPayAccChangeNotifyAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayAccChangeNotifyDto;

@Service("bisPayAccChangeNotifyAppService")
public class BisPayAccChangeNotifyAppServiceImpl implements BisPayAccChangeNotifyAppService {
	@Autowired
	private PayAccChangeNotifyService payAccChangeNotifyService;
	@Override
	public PageData<PayAccChangeNotifyDto> findAll(PageData<PayAccChangeNotifyDto> pageData,
			PayAccChangeNotifyDto queryparam) {		
		return payAccChangeNotifyService.findAll(pageData, queryparam);
	}
	@Override
	public PayAccChangeNotifyDto details(String id) {
		
		return payAccChangeNotifyService.details(id);
	}
	@Override
	public void movingAccountMatching(String streamNo, String accChangeBusiId) throws CbsCheckedException {
		payAccChangeNotifyService.movingAccountMatching(streamNo,accChangeBusiId);
	}
	@Override
	public PayAccChangeNotifyDto findPayAccChangeNotifyDtoById(String id) {
		return payAccChangeNotifyService.findPayAccChangeNotifyDtoById(id);
	}	
}
