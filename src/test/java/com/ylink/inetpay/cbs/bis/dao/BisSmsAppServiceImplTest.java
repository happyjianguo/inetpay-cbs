package com.ylink.inetpay.cbs.bis.dao;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.EmessType;
import com.ylink.inetpay.common.project.cbs.app.BisSmsAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 短信发送记录测试类
 * @author haha
 *
 */
public class BisSmsAppServiceImplTest extends UCBaseTest{
	@Autowired
	private BisSmsAppService bisSmsAppService;
	@Test
	public void findListPageTest() throws CbsCheckedException{
		BisSmsDto details = bisSmsAppService.details("e778fe59-d15b-4c08-9abe-ce794b0c809c");
		PageData<BisSmsDto> findListPage = bisSmsAppService.findListPage(new PageData<BisSmsDto>(),details);
		for (BisSmsDto dto : findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	
	@Test
	public void details() throws CbsCheckedException{
		BisSmsDto details = bisSmsAppService.details("e778fe59-d15b-4c08-9abe-ce794b0c809c");
		System.out.println(details);
	}
	
	@Test
	public void flushSms() throws CbsCheckedException{
//		bisSmsAppService.flushSms();
	}
	
	@Test
	public void sendSms() throws CbsCheckedException{
		String uuid=UUID.randomUUID().toString().substring(0,4);
		ArrayList<String> params = new ArrayList<>();
		params.add("yanggang");
		params.add("杨刚");
		params.add(uuid);
		//PAY_PASSWORD LOGIN_PASSWORD UPDATE_TEL SETTLEMENT_FAIL
//		bisSmsAppService.sendSms("15915432082", EBisSmsSystem.CBS,params,EBisTemplateCode.UPDATE_TEL,EmessType.MESSAGE_NOTIFICATION);
	}
	
}
