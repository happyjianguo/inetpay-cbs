package com.ylink.inetpay.cbs.bis.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.ENormalDisabledStatus;
import com.ylink.inetpay.common.project.cbs.app.BisSmsTemplateAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 短线模板测试类
 * @author haha
 *
 */
public class BisSmsTemplateAppServiceImplTest extends UCBaseTest{
	@Autowired
	private BisSmsTemplateAppService bisSmsTemplateAppService;
	@Test
	public void findListPageTest() throws CbsCheckedException{
		BisSmsTemplateDto details = bisSmsTemplateAppService.details("0001");
		PageData<BisSmsTemplateDto> findListPage = bisSmsTemplateAppService.findListPage(new PageData<BisSmsTemplateDto>(),details);
		for (BisSmsTemplateDto dto : findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	
	@Test
	public void detailsTest() throws CbsCheckedException{
		BisSmsTemplateDto details = bisSmsTemplateAppService.details("0001");
		System.out.println(details);
	}
	
	@Test
	public void updateTest() throws CbsCheckedException{
		BisSmsTemplateDto details = bisSmsTemplateAppService.details("0001");
		details.setReviser("yanggang");
		details.setStatus(ENormalDisabledStatus.DISABLE);
		details.setReviserName("杨刚");
		bisSmsTemplateAppService.update(details);
	}
	
	@Test
	public void getSmsTemplaTest() throws CbsCheckedException{
//		BisSmsTemplateDto smsTempla = bisSmsTemplateAppService.getSmsTempla(EBisTemplateCode.PAY_PASSWORD);
//		System.out.println(smsTempla);
	}
}
