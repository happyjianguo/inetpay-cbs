package com.ylink.inetpay.cbs.ucs.sec.dao;

import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecOperationLogService;
import com.ylink.inetpay.common.core.constant.EUcsSecOperationLogType;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecOperationLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public class UcsSecOperationLogDtoMapperTest extends UCBaseTest {
	@Autowired
	UcsSecOperationLogDtoMapper ucsSecOperationLogDtoMapper;
	@Autowired
	UcsSecOperationLogService ucsSecOperationLogService;
	@Test
	public void selectTest() throws CbsCheckedException{
		PageData<UcsSecOperationLogDto> pageData = new PageData<UcsSecOperationLogDto>();
		pageData.setPageSize(10);
		pageData.setPageNumber(1);
		UcsSecOperationLogDto ucsSecOperationLogDto = new UcsSecOperationLogDto();
		ucsSecOperationLogDto.setEndTime(new Date());
		ucsSecOperationLogDto.setLoginName("yanggang");
		ucsSecOperationLogDto.setOperType(EUcsSecOperationLogType.LOGIN);
		PageData<UcsSecOperationLogDto> pageDate=ucsSecOperationLogService.list(pageData, ucsSecOperationLogDto);
		for (UcsSecOperationLogDto ucsSecOperationLogDto2 : pageDate.getRows()) {
			System.out.println(ucsSecOperationLogDto2);
		}
	}
	@Test
	public void insertTest() throws CbsCheckedException{
		UcsSecOperationLogDto ucsSecOperationLogDto = new UcsSecOperationLogDto();
		ucsSecOperationLogDto.setDescription("登录失败");
		//ucsSecOperationLogDto.setdName("yanggang");
		ucsSecOperationLogDto.setIp("192.168.0.0");
		ucsSecOperationLogDto.setLoginName("yanggang");
		//ucsSecOperationLogDto.setLogType("1");
		ucsSecOperationLogDto.setOperType(EUcsSecOperationLogType.LOGIN);
		ucsSecOperationLogDto.setRealName("杨刚");
		ucsSecOperationLogService.insert(ucsSecOperationLogDto);
	}
}
