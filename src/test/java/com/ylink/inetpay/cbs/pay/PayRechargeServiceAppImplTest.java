package com.ylink.inetpay.cbs.pay;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.cbs.app.CbsPayRechargeAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayRechargeDto;

public class PayRechargeServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	CbsPayRechargeAppService payRechargeAppService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------充值查询-------------------");
		PayRechargeDto payRechargeDto = new PayRechargeDto();
		PageData<PayRechargeDto> pageData = new PageData<PayRechargeDto>();
		payRechargeDto.setBusiId("1");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<PayRechargeDto> list = payRechargeAppService.queryAllData(pageData, payRechargeDto);
		for (PayRechargeDto dto : list.getRows()) {
			System.out.println(dto.getBusiId());
			System.out.println(dto.getCreateTime());
		}
	}
	@Test
	public void detail() throws CbsCheckedException{
		_log.info("--------------充值明细查询-------------------");
		PayRechargeDto payRechargeDto = payRechargeAppService.selectByBusiId("12345678901234567890");
		if (payRechargeDto != null) {
			System.out.println(payRechargeDto.getBusiId());
			System.out.println(payRechargeDto.getCreateTime());
			System.out.println(payRechargeDto.getCompleteTime());
		}
	}
}
