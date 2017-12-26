package com.ylink.inetpay.cbs.pay;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.cbs.pay.service.PayBookService;
import com.ylink.inetpay.cbs.ucs.init.InitRootUser;
import com.ylink.inetpay.common.project.cbs.app.CbsPayBookAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayBookDto;

public class PayBookServiceAppImplTest extends OtherBaseTest{
	private static Logger _log = LoggerFactory.getLogger(InitRootUser.class);
	@Autowired
	CbsPayBookAppService appService;
	@Autowired
	PayBookService payBookService;
	@Test
	public void listTest() throws CbsCheckedException{
		_log.info("--------------支付流水查询-------------------");
		PayBookDto payBookDto = new PayBookDto();
		PageData<PayBookDto> pageData = new PageData<PayBookDto>();
		payBookDto.setBusiId("12345678901234567890");
		pageData.setPageNumber(1);
		pageData.setPageSize(5);
		PageData<PayBookDto> list = appService.queryAllData(pageData, payBookDto);
		for (PayBookDto dto : list.getRows()) {
			System.out.println(dto.getBusiId());
			System.out.println(dto.getCreateTime());
		}
	}
	@Test
	public void detail() throws CbsCheckedException{
		_log.info("--------------支付流水明细查询-------------------");
		PayBookDto payBookDto = appService.selectByPayId("12345678901234567890");
		if (payBookDto != null) {
			System.out.println(payBookDto.getBusiId());
			System.out.println(payBookDto.getCreateTime());
			System.out.println(payBookDto.getCompleteTime());
		}
	}
	
	@Test
	public  void test(){
		PageData<PayBookDto> pageData=new PageData<PayBookDto>();
		pageData.setPageSize(15);
		pageData.setPageNumber(2);
		payBookService.queryAllData(pageData, null);
	}
}
