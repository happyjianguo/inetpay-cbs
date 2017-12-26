package com.ylink.inetpay.cbs.cls;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.common.project.cbs.app.ClsReviewCountAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsReviewVo;

public class ClsReviewCountAppServiceTest extends OtherBaseTest {

	@Autowired
	private ClsReviewCountAppService clsReviewCountAppService;
	
	@Test
	public void test(){
		ClsReviewVo c=clsReviewCountAppService.findRecord();
		System.out.println(c.toString());
	}
}
