package com.ylink.inetpay.cbs.pay.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayExternalHandleService;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.project.pay.app.PayExternalHandleAppService;
import com.ylink.inetpay.common.project.pay.dto.PayExternalHandleDto;

@Service("payExternalHandleAppService")
public class PayExternalHandleAppServiceImpl implements PayExternalHandleAppService {
	@Autowired
	private PayExternalHandleService payExternalService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public PageData<PayExternalHandleDto> getByCond(PayExternalHandleDto payExternalDto, PageData<PayExternalHandleDto> pageData) {
		return payExternalService.getByCond(payExternalDto, pageData);
	}

	@Override
	public void insert(PayExternalHandleDto payExternalDto) {
		try {
			payExternalService.insert(payExternalDto);
		} catch (Exception e) {
			_log.error("新增对外经办异常："+e);
		}
	}

	@Override
	public void update(PayExternalHandleDto payExternalDto) {
		try {
			payExternalService.update(payExternalDto);
		} catch (Exception e) {
			_log.error("修改对外经办异常："+e);
		}
		
	}

	@Override
	public PayExternalHandleDto details(String id) {
		return payExternalService.details(id);
	}
	
}
