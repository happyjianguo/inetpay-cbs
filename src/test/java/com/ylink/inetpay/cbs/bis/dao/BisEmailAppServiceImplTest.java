package com.ylink.inetpay.cbs.bis.dao;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.project.cbs.app.BisEmailAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public class BisEmailAppServiceImplTest extends UCBaseTest {
	@Autowired
	private BisEmailAppService bisEmailAppService; 
	@Test
	public void findListPageTest() throws CbsCheckedException{
		BisEmailDto details = bisEmailAppService.details("765fe72d-ca0f-4573-aa74-92acc27f6a0f");
		PageData<BisEmailDto> findListPage = bisEmailAppService.findListPage(new PageData<BisEmailDto>(),details);
		for (BisEmailDto dto : findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	
	@Test
	public void detailsTest() throws CbsCheckedException{
		BisEmailDto details = bisEmailAppService.details("765fe72d-ca0f-4573-aa74-92acc27f6a0f");
		System.out.println(details);
	}
	
	@Test
	public void sendEmailTest() throws CbsCheckedException{
		ArrayList<String> params = new ArrayList<>();
		params.add("yanggang");
		params.add("杨刚");
		params.add(UUID.randomUUID().toString().substring(0,4));
		//PAY_PASSWORD LOGIN_PASSWORD EMAIL
		//bisEmailAppService.sendEmail("1281904583@qq.com","CP20160503100009",EBisSmsSystem.CBS, params, EBisEmailTemplateCode.PAY_PASSWORD,true);
	}
}
