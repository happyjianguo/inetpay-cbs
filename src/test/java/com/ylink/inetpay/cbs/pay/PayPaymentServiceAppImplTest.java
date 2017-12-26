package com.ylink.inetpay.cbs.pay;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.cbs.app.CbsPayPaymentAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayPaymentDto;

public class PayPaymentServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	CbsPayPaymentAppService appService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------支付查询-------------------");
		PayPaymentDto payPaymentDto = new PayPaymentDto();
		PageData<PayPaymentDto> pageData = new PageData<PayPaymentDto>();
		payPaymentDto.setBusiId("12345678901234567890");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<PayPaymentDto> list = appService.queryAllData(pageData, payPaymentDto);
		for (PayPaymentDto dto : list.getRows()) {
			System.out.println(dto.getBusiId());
			System.out.println(dto.getCreateTime());
		}
	}
	@Test
	public void detail() throws CbsCheckedException{
		_log.info("--------------支付明细查询-------------------");
		PayPaymentDto payPaymentDto = appService.selectByBusiId("12345678901234567890");
		if (payPaymentDto != null) {
			System.out.println(payPaymentDto.getBusiId());
			System.out.println(payPaymentDto.getCreateTime());
			System.out.println(payPaymentDto.getCompleteTime());
		}
	}
	
//	@Test
//	public void reportSumData() throws CbsCheckedException{
//		_log.info("--------------查询-------------------");
//		ReporHeadDto reporHeadDto = appService.reportSumData();
//		if (reporHeadDto != null) {
//			System.out.println(reporHeadDto.getAllNum());
//			System.out.println(reporHeadDto.getAllAmt());
//			System.out.println(reporHeadDto.getSuccNum());
//			System.out.println(reporHeadDto.getSuccAmt());
//		}
//	}
}
