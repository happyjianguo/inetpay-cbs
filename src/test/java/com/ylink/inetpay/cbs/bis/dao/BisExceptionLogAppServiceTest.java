package com.ylink.inetpay.cbs.bis.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 异常日志监控测试类
 * @author haha
 *
 */
public class BisExceptionLogAppServiceTest extends UCBaseTest{
	@Autowired
	private BisExceptionLogAppService bisExceptionLogAppService;
	@Test
	public void listTest() throws CbsCheckedException{
		BisExceptionLogDto details = bisExceptionLogAppService.details("ac29c166-39c5-4313-899b-025bdfb6f898");
		PageData<BisExceptionLogDto> findListPage = bisExceptionLogAppService.findListPage(new PageData<BisExceptionLogDto>(), details);
		for (BisExceptionLogDto dto: findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	@Test
	public void saveLogTest() throws CbsCheckedException{
		BisExceptionLogDto	dto=new BisExceptionLogDto();
		dto.setSystem(EBisSmsSystem.CHANNEL);
		dto.setNlevel(EBisExceptionLogNlevel.ERROR);
		dto.setType(EBisExceptionLogType.CHANNEL_RECONCILIATION);
		dto.setContent("错误内容");
		dto.setAllpath("/log/day1");
		dto.setRequinfo("请求报文".getBytes());
		dto.setRespinfo("响应报文".getBytes());
		bisExceptionLogAppService.saveLog(dto);
	}
	@Test
	public void detailsTest() throws CbsCheckedException{
		BisExceptionLogDto details = bisExceptionLogAppService.details("15876115-a55a-4253-8ba9-b7c9be117fdf");
		System.out.println(details.getResponse());
		System.out.println(details.getRequest());
		System.out.println(details);
	}
}
