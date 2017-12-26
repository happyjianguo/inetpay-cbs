package com.ylink.inetpay.cbs.pay.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayFeeSummaryDetailService;
import com.ylink.inetpay.common.project.cbs.app.PayFeeSummaryDetailAppService;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDetailDto;

@Service("payFeeSummaryDetailAppService")
public class PayFeeSummaryDetailAppServiceImpl implements PayFeeSummaryDetailAppService{

	private static Logger log = LoggerFactory.getLogger(PayFeeSummaryDetailAppServiceImpl.class);
	
	@Autowired
	private PayFeeSummaryDetailService payFeeSummaryDetailService;
	
	@Override
	public PageData<PayFeeSummaryDetailDto> listPage(PageData<PayFeeSummaryDetailDto> pageData,
			PayFeeSummaryDetailDto queryParam) {
		log.debug("payFeeSummaryDetailAppServiceImpl.listPage run....");
		return payFeeSummaryDetailService.listPage(pageData, queryParam);
	}

	@Override
	public List<PayFeeSummaryDetailDto> queryAllBySearch(PayFeeSummaryDetailDto queryParam) {
		return payFeeSummaryDetailService.queryAllBySearch(queryParam);
	}

}
