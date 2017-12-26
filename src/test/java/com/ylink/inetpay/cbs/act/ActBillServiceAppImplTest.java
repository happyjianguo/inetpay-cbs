package com.ylink.inetpay.cbs.act;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActBillAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public class ActBillServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	CbsActBillAppService appService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------查询-------------------");
		ActBillDto actBillDto = new ActBillDto();
		PageData<ActBillDto> pageData = new PageData<ActBillDto>();
		actBillDto.setBillId("12345678901234567890");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<ActBillDto> list = appService.queryAllData(pageData, actBillDto);
		for (ActBillDto dto : list.getRows()) {
			System.out.println(dto.getBillId());
			System.out.println(dto.getCreateTime());
		}
	}
	@Test
	public void detail() throws CbsCheckedException{
		_log.info("--------------明细查询-------------------");
		ActBillDto actBillDto = appService.selectByBillId("12345678901234567890");
		if (actBillDto != null) {
			System.out.println(actBillDto.getBillId());
			System.out.println(actBillDto.getCreateTime());
			System.out.println(actBillDto.getBookId());
		}
	}
}
