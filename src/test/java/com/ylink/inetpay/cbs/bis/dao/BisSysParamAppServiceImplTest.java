package com.ylink.inetpay.cbs.bis.dao;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.cbs.bis.service.BisActInterestDateAuditService;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.app.BisSysParamAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
public class BisSysParamAppServiceImplTest extends UCBaseTest{
	protected static final Logger logger = LoggerFactory.getLogger(BisSysParamAppServiceImplTest.class);
	@Autowired
	private BisSysParamAppService bisSysParamAppService;
	@Autowired
	private BisActInterestDateAuditService bisActInterestDateAuditService;
	@Test
	public void listTest() throws CbsCheckedException{
		BisSysParamDto bisSysParamDto = new BisSysParamDto();
		//bisSysParamDto.setCreateTime(new Date());
		bisSysParamDto.setGroupName("清结算参数");
		bisSysParamDto.setId("0001");
		bisSysParamDto.setKey(SystemParamConstants.PAY_LIMIT);
		bisSysParamDto.setRemark("支付限额");
		bisSysParamDto.setUpdater("yanggang");
		bisSysParamDto.setUpdaterName("杨刚");
		bisSysParamDto.setValue("100");
		//bisSysParamDto.setEndCreateTime(new Date());
		//bisSysParamDto.setStartCreateTime(new Date());
		PageData<BisSysParamDto> findListPage = bisSysParamAppService.findListPage(new PageData<BisSysParamDto>(), bisSysParamDto);
		for (BisSysParamDto dto : findListPage.getRows()) {
			System.out.println(dto);
		}
	}
	
	@Test
	public void updateTest() throws CbsCheckedException{
		BisSysParamDto bisSysParamDto = new BisSysParamDto();
		bisSysParamDto.setCreateTime(new Date());
		bisSysParamDto.setGroupName("清结算参数");
		bisSysParamDto.setId("0001");
		bisSysParamDto.setKey(SystemParamConstants.PAY_LIMIT);
		bisSysParamDto.setRemark("支付限额");
		bisSysParamDto.setUpdater("yanggang");
		bisSysParamDto.setUpdaterName("杨刚");
		bisSysParamDto.setValue("100");
		bisSysParamAppService.updateSelective(bisSysParamDto);
	}
	/*@Test
	public void getValueTest() throws CbsCheckedException{
		String value = bisSysParamAppService.getValue(SystemParamConstants.PAY_LIMIT);
		System.out.println(value);
	}*/
	/**
	 * 测试用户下一结息日
	 */
	@Test
	 public void nextSettleDayTest(){
		try {
			bisActInterestDateAuditService.auditPass("yanggang", "yanggnag", "7fddb739b48547269ff8110e1165e765");
			 Thread.sleep(1000000000);
		} catch (Exception e) {
			logger.error("系统异常，异常原因：{}",ExceptionProcUtil.getExceptionDesc(e));
		}
	 }
}
