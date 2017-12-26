package com.ylink.inetpay.cbs.pay;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.pay.service.PayTransferService;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayTransferDto;

public class PayTransferServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	PayTransferService appService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------转账查询-------------------");
		PayTransferDto payTransferDto = new PayTransferDto();
		PageData<PayTransferDto> pageData = new PageData<PayTransferDto>();
		payTransferDto.setBusiId("12345678901234567890");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<PayTransferDto> list = appService.queryAllData(pageData,payTransferDto);
		for (PayTransferDto dto : list.getRows()) {
			System.out.println(dto.getBusiId());
			System.out.println(dto.getCreateTime());
		}
	}
	@Test
	public void detail() throws CbsCheckedException{
		_log.info("--------------转账明细查询-------------------");
		PayTransferDto payTransferDto = appService.selectByBusiId("12345678901234567890");
		if (payTransferDto != null) {
			System.out.println(payTransferDto.getBusiId());
			System.out.println(payTransferDto.getCreateTime());
			System.out.println(payTransferDto.getCompleteTime());
		}
	}
}
