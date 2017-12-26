package com.ylink.inetpay.cbs.act;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.account.dto.ActBookDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActBookAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public class ActBookServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	CbsActBookAppService appService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------查询-------------------");
		ActBookDto actBookDto = new ActBookDto();
		PageData<ActBookDto> pageData = new PageData<ActBookDto>();
		actBookDto.setBookId("12345678901234567890");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<ActBookDto> list = appService.queryAllData(pageData, actBookDto);
		for (ActBookDto dto : list.getRows()) {
			System.out.println(dto.getBookId());
			System.out.println(dto.getCreateTime());
		}
	}
	@Test
	public void detail() throws CbsCheckedException{
		_log.info("--------------明细查询-------------------");
		ActBookDto actBookDto = appService.selectByBookId("12345678901234567890");
		if (actBookDto != null) {
			System.out.println(actBookDto.getPayId());
			System.out.println(actBookDto.getCreateTime());
			System.out.println(actBookDto.getBookId());
		}
	}
	@Test
	public void queryByPayId(){
		System.out.println("查找账务流水，根据支付流水号");
		System.out.println(appService.selectByPayId("20160720001000004755").getBookId());
	}
}
