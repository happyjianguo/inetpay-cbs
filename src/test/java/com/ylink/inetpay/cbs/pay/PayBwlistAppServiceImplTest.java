package com.ylink.inetpay.cbs.pay;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.common.core.constant.EPayBwType;
import com.ylink.inetpay.common.core.constant.EPayLimitBusinessType;
import com.ylink.inetpay.common.project.cbs.app.PayBwlistAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayBwlistDto;
import com.ylink.inetpay.common.project.pay.exception.PayCheckedException;

/**
 * 黑白名单测试类
 * @author haha
 *
 */
public class PayBwlistAppServiceImplTest extends OtherBaseTest{
	@Autowired
	private PayBwlistAppService payBwlistAppService;
	@Test
	public void findListPageTest() throws CbsCheckedException{
		PayBwlistDto details = payBwlistAppService.details("95efc3dc-4db5-4915-a3e3-6a58ed58df3e");
		PageData<PayBwlistDto> findListPage = payBwlistAppService.findListPage(new PageData<PayBwlistDto>(), details);
		for (PayBwlistDto bwListDto : findListPage.getRows()) {
			System.out.println(bwListDto);
		}
	}
	@Test
	public void detailsTest() throws CbsCheckedException{
		PayBwlistDto details = payBwlistAppService.details("591da095-aa63-465f-b6c0-eccb74bb4c91");
		System.out.println(details);
	}
	@Test
	public void addTest() throws CbsCheckedException, PayCheckedException{
		PayBwlistDto payBwlistDto = new PayBwlistDto();
		payBwlistDto.setCustId("0000000011111113");
		payBwlistDto.setBwType(EPayBwType.WHITE_LIST);
		payBwlistDto.setCustName("杨刚");
	//	payBwlistDto.setBusinessType(EPayLimitBusinessType.LUQIAO_FEE);
		payBwlistDto.setCreater("yanggang");
		payBwlistDto.setCreaterName("杨刚");
		payBwlistAppService.add(payBwlistDto);
	}
	@Test
	public void deleteTest() throws CbsCheckedException, PayCheckedException{
		//payBwlistAppService.delete("ccd9858d-0fe5-4a85-9d20-7ff0e40ef2c1");
	}
}
