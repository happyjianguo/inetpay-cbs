package com.ylink.inetpay.cbs.act;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.account.dto.ActRuleDto;
import com.ylink.inetpay.common.project.cbs.app.CbsActRuleAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public class ActRuleServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	CbsActRuleAppService appService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------查询-------------------");
		ActRuleDto actRuleDto = new ActRuleDto();
		PageData<ActRuleDto> pageData = new PageData<ActRuleDto>();
		actRuleDto.setDrSubjectNo("111");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<ActRuleDto> list = appService.queryAllData(pageData, actRuleDto);
		for (ActRuleDto dto : list.getRows()) {
			System.out.println(dto.getBusiType());
			System.out.println(dto.getDrSubjectNo());
		}
	}
	
}
