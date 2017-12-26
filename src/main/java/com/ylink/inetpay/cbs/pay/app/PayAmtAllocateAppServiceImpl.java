package com.ylink.inetpay.cbs.pay.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayAmtAllocateService;
import com.ylink.inetpay.common.project.pay.app.PayAmtAllocateAppService;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
@Service("payAmtAllocateAppService")
public class PayAmtAllocateAppServiceImpl implements PayAmtAllocateAppService {
	@Autowired
	private PayAmtAllocateService payAmtAllocateService;
	@Override
	public PageData<PayAmtAllocateDto> findAll(
			PageData<PayAmtAllocateDto> pageData, PayAmtAllocateDto queryparam) {
		return payAmtAllocateService.findAll(pageData, queryparam);
	}

	@Override
	public PayAmtAllocateDto details(String id) {
		return payAmtAllocateService.details(id);
	}

	@Override
	public PageData<PayAmtAllocateDto> queryAllData(PageData<PayAmtAllocateDto> pageData,
			PayAmtAllocateDto queryparam) {
		return payAmtAllocateService.queryAll(pageData, queryparam);
	}

	@Override
	public PayAmtAllocateDto receiptDetail(String id) {
		return payAmtAllocateService.receiptDetail(id);
	}

	@Override
	public List<PayAmtAllocateDto> queryAllPayAmtAllocate(PayAmtAllocateDto queryParam) {
		 
		return payAmtAllocateService.queryAllPayAmtAllocate(queryParam);
	}

	@Override
	public List<PayAmtAllocateDto> listBatchNo(PayAmtAllocateDto queryParam) {
		
		return payAmtAllocateService.listBatchNo(queryParam);
	}

	@Override
	public List<PayAmtAllocateDto> findPayAmtAllocateByBatchNo(PayAmtAllocateDto queryParam) {
		return payAmtAllocateService.findPayAmtAllocateByBatchNo(queryParam);
	}

}
