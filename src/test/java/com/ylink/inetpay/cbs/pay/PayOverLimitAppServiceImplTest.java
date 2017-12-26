package com.ylink.inetpay.cbs.pay;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.common.project.cbs.app.PayOverlimitAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayOverlimitDto;

/**
 * 超限记录测试类
 * @author haha
 *
 */
public class PayOverLimitAppServiceImplTest extends OtherBaseTest{
	@Autowired
	private PayOverlimitAppService payOverlimitAppService;
	@Test
	public void listTest() throws CbsCheckedException{
		PayOverlimitDto details = payOverlimitAppService.details("95efc3dc-4db5-4915-a3e3-6a58ed58df3e");
		PageData<PayOverlimitDto> overLimitList = payOverlimitAppService.singleOverLimitList(new PageData<PayOverlimitDto>(), details);
		for (PayOverlimitDto payOverlimit : overLimitList.getRows()) {
			System.out.println(payOverlimit);
		}
	}
	@Test
	public void detailsTest() throws CbsCheckedException{
		PayOverlimitDto details = payOverlimitAppService.details("95efc3dc-4db5-4915-a3e3-6a58ed58df3e");
		System.out.println(details);
	}
}
