package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayRefundService;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.app.CbsPayRefundAppService;
import com.ylink.inetpay.common.project.pay.dto.PayRefundDto;

@Service("cbsPayRefundAppService")
public class CbsPayRefundAppServiceImpl implements CbsPayRefundAppService {
	@Autowired
	PayRefundService payRefundService;

	@Override
	public PageData<PayRefundDto> queryAllData(
			PageData<PayRefundDto> pageDate, PayRefundDto payRefundDto) {
		return payRefundService.queryAllData(pageDate, payRefundDto);
	}

	@Override
	public PayRefundDto selectByBusiId(String busiId) {
		return payRefundService.selectByBusiId(busiId);
	}
	@Override
	public ReporHeadDto reportSumData(PayRefundDto payRefundDto) {
		//所有总笔数和总金额
		ReporHeadDto reporAllDto = new ReporHeadDto();
		//所有成功的总笔数和总金额
		ReporHeadDto reporSuccDto = new ReporHeadDto();
		reporAllDto = payRefundService.reportSumData(payRefundDto);
		payRefundDto.setOrderStatus(EOrderStatus.ORDER_STATUS_SUCCESS);
		reporSuccDto = payRefundService.reportSumData(payRefundDto);
		if(reporSuccDto!=null){
			reporAllDto.setSuccNum(reporSuccDto.getAllNum());
			reporAllDto.setSuccAmt(reporSuccDto.getAllAmt());
		}
		return reporAllDto;
	}
}
