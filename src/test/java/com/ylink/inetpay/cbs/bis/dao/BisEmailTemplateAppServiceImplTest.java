package com.ylink.inetpay.cbs.bis.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.constant.ENormalDisabledStatus;
import com.ylink.inetpay.common.project.cbs.app.BisEmailTemplateAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 邮件模板测试类
 * @author haha
 *
 */
public class BisEmailTemplateAppServiceImplTest extends UCBaseTest{
	@Autowired
	private BisEmailTemplateAppService bisEmailTemplateAppService;
	@Test
	public void findListPageTest() throws CbsCheckedException{
		BisEmailTemplateDto details = bisEmailTemplateAppService.details("0001");
		PageData<BisEmailTemplateDto> findListPage = bisEmailTemplateAppService.findListPage(new PageData<BisEmailTemplateDto>(),details);
		for (BisEmailTemplateDto dto: findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	
	@Test
	public void detailsTest() throws CbsCheckedException{
		BisEmailTemplateDto details = bisEmailTemplateAppService.details("0001");
		System.out.println(details);
	}
	
	@Test
	public void updateTest() throws CbsCheckedException{
		BisEmailTemplateDto details = bisEmailTemplateAppService.details("0001");
		details.setReviser("yanggang");
		details.setStatus(ENormalDisabledStatus.DISABLE);
		details.setReviserName("杨刚");
		bisEmailTemplateAppService.update(details);
	}
	
	/**
	 * @throws CbsCheckedException
	 */
	@Test
	public void getEmailTemplateTest() throws CbsCheckedException{
		//PAY_PASSWORD LOGIN_PASSWORD EMAIL
		BisEmailTemplateDto emailTempla = bisEmailTemplateAppService.getEmailTempla(EBisEmailTemplateCode.PAY_PASSWORD);
		System.out.println(emailTempla);
	}
}
