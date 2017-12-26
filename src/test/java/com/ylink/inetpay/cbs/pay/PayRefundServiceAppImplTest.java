package com.ylink.inetpay.cbs.pay;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.cbs.app.CbsPayRefundAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayRefundDto;

public class PayRefundServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	CbsPayRefundAppService appService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------退款查询-------------------");
		PayRefundDto payRefundDto = new PayRefundDto();
		PageData<PayRefundDto> pageData = new PageData<PayRefundDto>();
		payRefundDto.setBusiId("12345678901234567890");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<PayRefundDto> list = appService.queryAllData(pageData, payRefundDto);
		for (PayRefundDto dto : list.getRows()) {
			System.out.println(dto.getBusiId());
			System.out.println(dto.getCreateTime());
		}
	}
	@Test
	public void detail() throws CbsCheckedException{
		_log.info("--------------退款明细查询-------------------");
		PayRefundDto payRefundDto = appService.selectByBusiId("12345678901234567890");
		if (payRefundDto != null) {
			System.out.println(payRefundDto.getBusiId());
			System.out.println(payRefundDto.getCreateTime());
			System.out.println(payRefundDto.getCompleteTime());
		}
	}
}
