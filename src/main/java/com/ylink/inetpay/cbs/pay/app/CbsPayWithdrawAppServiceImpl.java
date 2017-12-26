package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayWithdrawService;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.app.CbsPayWithdrawAppService;
import com.ylink.inetpay.common.project.pay.dto.PayWithdrawDto;

@Service("cbsPayWithdrawAppService")
public class CbsPayWithdrawAppServiceImpl implements CbsPayWithdrawAppService {
	@Autowired
	PayWithdrawService payWithdrawService;

	@Override
	public PageData<PayWithdrawDto> queryAllData(
			PageData<PayWithdrawDto> pageDate, PayWithdrawDto payWithdrawDto) {
		return payWithdrawService.queryAllData(pageDate, payWithdrawDto);
	}

	@Override
	public PayWithdrawDto selectByBusiId(String busiId) {
		return payWithdrawService.selectByBusiId(busiId);
	}
	@Override
	public ReporHeadDto reportSumData(PayWithdrawDto payWithdrawDto) {
		//所有总笔数和总金额
		ReporHeadDto reporAllDto = new ReporHeadDto();
		//所有成功的总笔数和总金额
		ReporHeadDto reporSuccDto = new ReporHeadDto();
		reporAllDto = payWithdrawService.reportSumData(payWithdrawDto);
		payWithdrawDto.setOrderStatus(EOrderStatus.ORDER_STATUS_SUCCESS);
		reporSuccDto = payWithdrawService.reportSumData(payWithdrawDto);
		if(reporSuccDto!=null){
			reporAllDto.setSuccNum(reporSuccDto.getAllNum());
			reporAllDto.setSuccAmt(reporSuccDto.getAllAmt());
		}
		return reporAllDto;
	}
}
