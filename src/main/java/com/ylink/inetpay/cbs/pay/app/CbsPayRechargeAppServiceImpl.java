package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayRechargeService;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.app.CbsPayRechargeAppService;
import com.ylink.inetpay.common.project.pay.dto.PayRechargeDto;

@Service("cbsPayRechargeAppService")
public class CbsPayRechargeAppServiceImpl implements CbsPayRechargeAppService {
	@Autowired
	PayRechargeService payRechargeService;

	@Override
	public PageData<PayRechargeDto> queryAllData(
			PageData<PayRechargeDto> pageDate, PayRechargeDto payRechargeDto) {
		return payRechargeService.queryAllData(pageDate, payRechargeDto);
	}

	@Override
	public PayRechargeDto selectByBusiId(String busiId) {
		return payRechargeService.selectByBusiId(busiId);
	}
	@Override
	public ReporHeadDto reportSumData(PayRechargeDto payRechargeDto) {
		//所有总笔数和总金额
		ReporHeadDto reporAllDto = new ReporHeadDto();
		//所有成功的总笔数和总金额
		ReporHeadDto reporSuccDto = new ReporHeadDto();
		reporAllDto = payRechargeService.reportSumData(payRechargeDto);
		payRechargeDto.setOrderStatus(EOrderStatus.ORDER_STATUS_SUCCESS);
		reporSuccDto = payRechargeService.reportSumData(payRechargeDto);
		if(reporSuccDto!=null){
			reporAllDto.setSuccNum(reporSuccDto.getAllNum());
			reporAllDto.setSuccAmt(reporSuccDto.getAllAmt());
		}
		return reporAllDto;
	}
}
