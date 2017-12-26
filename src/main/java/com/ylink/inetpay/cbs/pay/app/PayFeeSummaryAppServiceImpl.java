package com.ylink.inetpay.cbs.pay.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayFeeSummaryService;
import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.cbs.app.PayFeeSummaryAppService;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;

@Service("payFeeSummaryAppService")
public class PayFeeSummaryAppServiceImpl implements PayFeeSummaryAppService {

	private static Logger log = LoggerFactory.getLogger(PayFeeSummaryAppServiceImpl.class);
	
	@Autowired
	private PayFeeSummaryService payFeeSummaryService;
	@Override
	public PageData<PayFeeSummaryDto> listPage(PageData<PayFeeSummaryDto> pageData, PayFeeSummaryDto queryParam) {
		log.debug("payFeeSummaryAppServiceImpl.listPage run....");
		return payFeeSummaryService.listPage(pageData, queryParam);
	}

	@Override
	public PayFeeSummaryDto selectByBusyId(String busiId) {
		log.debug("payFeeSummaryAppServiceImpl.selectedById run....");
		return payFeeSummaryService.selectedById(busiId);
	}

	@Override
	public ReporHeadDto reportSumData(PayFeeSummaryDto queryParam) {
		//所有总笔数和总金额
				ReporHeadDto reporAllDto = new ReporHeadDto();
				//所有成功的总笔数和总金额
				ReporHeadDto reporSuccDto = new ReporHeadDto();
				reporAllDto = payFeeSummaryService.reportSumData(queryParam);
				/*if(PayFeeSummaryDto.getOrderStatus()!=null){
					if(EOrderStatus.ORDER_STATUS_SUCCESS.getValue().equals(PayFeeSummaryDto.getOrderStatus().getValue())){
						reporAllDto.setSuccNum(reporAllDto.getAllNum());
						reporAllDto.setSuccAmt(reporAllDto.getAllAmt());
					}else{
						reporAllDto.setSuccNum(0l);
						reporAllDto.setSuccAmt(0l);
					}
				}else{
					queryParam.set(EOrderStatus.ORDER_STATUS_SUCCESS);
					reporSuccDto = payPaymentService.reportSumData(payPaymentDto);
					if(reporSuccDto!=null){
						reporAllDto.setSuccNum(reporSuccDto.getAllNum());
						reporAllDto.setSuccAmt(reporSuccDto.getAllAmt());
					}
				}*/
				if(reporSuccDto!=null){
					reporAllDto.setSuccNum(reporSuccDto.getAllNum());
					reporAllDto.setSuccAmt(reporSuccDto.getAllAmt());
				}
				return reporAllDto;
	}

	@Override
	public List<PayFeeSummaryDto> queryAll(PayFeeSummaryDto queryParam) {
		return payFeeSummaryService.queryAll(queryParam);
	}

}
