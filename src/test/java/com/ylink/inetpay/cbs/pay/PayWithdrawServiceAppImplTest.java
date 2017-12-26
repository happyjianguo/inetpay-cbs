package com.ylink.inetpay.cbs.pay;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.pay.service.PayWithdrawService;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayWithdrawDto;

public class PayWithdrawServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	PayWithdrawService appService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------提现查询-------------------");
		PayWithdrawDto payWithdrawDto = new PayWithdrawDto();
		PageData<PayWithdrawDto> pageData = new PageData<PayWithdrawDto>();
		payWithdrawDto.setBusiId("12345678901234567890");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<PayWithdrawDto> list = appService.queryAllData(pageData, payWithdrawDto);
		for (PayWithdrawDto dto : list.getRows()) {
			System.out.println(dto.getBusiId());
			System.out.println(dto.getCreateTime());
		}
	}
	@Test
	public void detail() throws CbsCheckedException{
		_log.info("--------------提现明细查询-------------------");
		PayWithdrawDto payWithdrawDto = appService.selectByBusiId("12345678901234567890");
		if (payWithdrawDto != null) {
			System.out.println(payWithdrawDto.getBusiId());
			System.out.println(payWithdrawDto.getCreateTime());
			System.out.println(payWithdrawDto.getCompleteTime());
		}
	}
}
