package com.ylink.inetpay.cbs.act;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.cbs.app.CbsActSubjectAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public class ActSubjectServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	CbsActSubjectAppService appService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------查询-------------------");
//		ActSubjectDto actSubjectDto = new ActSubjectDto();
//		actSubjectDto.setSubjectNo("1");
//		List<ActSubjectDto> list = appService.queryAllDataByTree(actSubjectDto);
//		for (ActSubjectDto dto : list) {
//			System.out.println(dto.getSubjectNo());
//			System.out.println(dto.getSubjectName());
//		}
	}
	
}
