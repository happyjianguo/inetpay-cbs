/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-2
 */

package com.ylink.inetpay.cbs.bis.cache;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailTemplateDto;

/** 
 * @author lyg
 * @date 2016-9-2
 * @description：TODO
 */

public class BisEmailTemplateDtoCacheTest extends UCBaseTest {

	@Autowired
	BisEmailTemplateDtoCache bisEmailTemplateDtoCache;
	@Test
	public void test()
	{
		BisEmailTemplateDto dto=bisEmailTemplateDtoCache.getEmailTempla(EBisEmailTemplateCode.EMAIL);
		dto=bisEmailTemplateDtoCache.getEmailTempla(EBisEmailTemplateCode.EMAIL);
		dto.setUpdateTime(new Date());
		bisEmailTemplateDtoCache.updateByEmailTempla(dto);
		dto=bisEmailTemplateDtoCache.getEmailTempla(EBisEmailTemplateCode.EMAIL);
		
	}
}
