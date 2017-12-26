package com.ylink.inetpay.cbs.chl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.OtherBaseTest;
import com.ylink.inetpay.common.project.cbs.app.ChlParamAppService;

/**
 * 渠道参数测试类
 * @author haha
 *
 */
public class ChlParamsAppServiceImplTest extends OtherBaseTest{
	@Autowired
	private ChlParamAppService chlParamAppService;
	/*@Test
	public void findListPageTest() throws CbsCheckedException{
		List<TbChlBankDto> params = chlParamAppService.getParams("000001");
		PageData<TbChlParamDto> findListPage = chlParamAppService.findListPage(new PageData<TbChlParamDto>(), new TbChlParamDto());
		System.out.println(findListPage.getRows().size());
		for (TbChlParamDto dto : findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	
	@Test
	public void getParamsTest() throws CbsCheckedException{
		List<TbChlBankDto> params = chlParamAppService.getParams("000001");
		System.out.println(params);
	}*/
}
