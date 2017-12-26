package com.ylink.inetpay.cbs.pay.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayTransferService;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.app.CbsPayTransferAppService;
import com.ylink.inetpay.common.project.pay.dto.PayTransferDto;

@Service("cbsPayTransferAppService")
public class CbsPayTransferAppServiceImpl implements CbsPayTransferAppService {
	@Autowired
	PayTransferService payTransferService;

	@Override
	public PageData<PayTransferDto> queryAllData(
			PageData<PayTransferDto> pageDate, PayTransferDto payTransferDto) {
		return payTransferService.queryAllData(pageDate, payTransferDto);
	}

	@Override
	public PayTransferDto selectByBusiId(String busiId) {
		return payTransferService.selectByBusiId(busiId);
	}
	@Override
	public ReporHeadDto reportSumData(PayTransferDto payTransferDto) {
		//所有总笔数和总金额
		ReporHeadDto reporAllDto = new ReporHeadDto();
		//所有成功的总笔数和总金额
		ReporHeadDto reporSuccDto = new ReporHeadDto();
		reporAllDto = payTransferService.reportSumData(payTransferDto);
		payTransferDto.setOrderStatus(EOrderStatus.ORDER_STATUS_SUCCESS);
		reporSuccDto = payTransferService.reportSumData(payTransferDto);
		if(reporSuccDto!=null){
			reporAllDto.setSuccNum(reporSuccDto.getAllNum());
			reporAllDto.setSuccAmt(reporSuccDto.getAllAmt());
		}
		return reporAllDto;
	}
}
