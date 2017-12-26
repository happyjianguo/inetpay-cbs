package com.ylink.inetpay.cbs.pay.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayBookService;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.app.CbsPayBookAppService;
import com.ylink.inetpay.common.project.pay.dto.PayBookDto;

@Service("cbsPayBookAppService")
public class CbsPayBookAppServiceImpl implements CbsPayBookAppService {
	@Autowired
	PayBookService payBookService;

	@Override 
	public PageData<PayBookDto> queryAllData(
			PageData<PayBookDto> pageDate, PayBookDto payBookDto) {
		return payBookService.queryAllData(pageDate, payBookDto);
	}
	@Override 
	public List<PayBookDto> listPayBook(PayBookDto payBookDto) {
		return payBookService.listPayBook(payBookDto);
	}
	@Override
	public PayBookDto selectByPayId(String payId) {
		return payBookService.selectByPayId(payId);
	}
	@Override
	public ReporHeadDto reportSumData(PayBookDto payBookDto) {
		//所有总笔数和总金额
		ReporHeadDto reporAllDto = new ReporHeadDto();
		//所有成功的总笔数和总金额
		ReporHeadDto reporSuccDto = new ReporHeadDto();
		reporAllDto = payBookService.reportSumData(payBookDto);
		payBookDto.setOrderStatus(EOrderStatus.ORDER_STATUS_SUCCESS);
		reporSuccDto = payBookService.reportSumData(payBookDto);
		if(reporSuccDto!=null){
			reporAllDto.setSuccNum(reporSuccDto.getAllNum());
			reporAllDto.setSuccAmt(reporSuccDto.getAllAmt());
		}
		return reporAllDto;
	}
}
