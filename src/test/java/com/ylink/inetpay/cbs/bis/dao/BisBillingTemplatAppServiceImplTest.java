package com.ylink.inetpay.cbs.bis.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EBisBusinessType;
import com.ylink.inetpay.common.project.cbs.app.BisBillingTemplateAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBillingTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public class BisBillingTemplatAppServiceImplTest extends UCBaseTest {
	@Autowired
	private BisBillingTemplateAppService bisBillingTemplateAppService;

	@Test
	public void listTest() throws CbsCheckedException {
		BisBillingTemplateDto details = bisBillingTemplateAppService
				.details("000100000000002");
		details.setEndUpdateTime(new Date());
		//details.setStartUpdateTime(new Date());
		PageData<BisBillingTemplateDto> findListPage = bisBillingTemplateAppService.findListPage(new PageData<BisBillingTemplateDto>(), details);
		for (BisBillingTemplateDto dto : findListPage.getRows()) {
			System.out.println(dto);
		}
	}

	@Test
	public void updateTest() throws CbsCheckedException {
		BisBillingTemplateDto details = bisBillingTemplateAppService
				.details("000100000000002");
		details.setReviser("yangqiang");
		details.setReviserName("杨强");
		details.setBusinessType(EBisBusinessType.TRANSFER_ACCOUNTS);
		bisBillingTemplateAppService.update(details);
	}
	
	@Test
	public void compOrderFee() throws CbsCheckedException{
		Long fee = bisBillingTemplateAppService.compuOderFee(EBisBusinessType.TRANSFER_ACCOUNTS, 10000L);
		System.out.println(fee+"------------------------------------------------");
	}
}
