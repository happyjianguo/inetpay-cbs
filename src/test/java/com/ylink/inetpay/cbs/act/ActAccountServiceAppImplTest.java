package com.ylink.inetpay.cbs.act;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActAccountAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public class ActAccountServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	CbsActAccountAppService appService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------查询-------------------");
		ActAccountDto actAccountDto = new ActAccountDto();
		PageData<ActAccountDto> pageData = new PageData<ActAccountDto>();
		actAccountDto.setAccountId("112233");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<ActAccountDto> list = appService.queryAllData(pageData, actAccountDto);
		for (ActAccountDto dto : list.getRows()) {
			System.out.println(dto.getAccountId());
			System.out.println(dto.getCreateTime());
		}
	}
	@Test
	public void detail() throws CbsCheckedException{
		_log.info("--------------明细查询-------------------");
		ActAccountDto actAccountDto = appService.selectByAccountId("112233");
		if (actAccountDto != null) {
			System.out.println(actAccountDto.getAccountId());
			System.out.println(actAccountDto.getCreateTime());
			System.out.println(actAccountDto.getCashAmount());
		}
	}
	@Test
	public void getUserAccounts()throws CbsCheckedException{
		List<ActAccountDto> userAccounts = appService.getUserAccounts("2");
		System.out.println(userAccounts);
	}
	@Test
	public void pageList()throws CbsCheckedException{
		ActAccountDto actAccountDto = appService.selectByAccountId("112233");
		PageData<ActAccountDto> pageList = appService.pageList(actAccountDto, new PageData<ActAccountDto>());
		for (ActAccountDto dto : pageList.getRows()) {
			System.out.println(dto);
		}
	}
}
