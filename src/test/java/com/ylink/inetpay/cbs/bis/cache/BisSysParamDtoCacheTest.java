/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-2
 */

package com.ylink.inetpay.cbs.bis.cache;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.UCBaseTest;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;

/** 
 * @author lyg
 * @date 2016-9-2
 * @description：TODO
 */

public class BisSysParamDtoCacheTest extends UCBaseTest {

	@Autowired
	BisSysParamDtoCache bisSysParamDtoCache;
	@Test
	public void test()
	{
		BisSysParamDto dto=bisSysParamDtoCache.selectByKey("CHL_FTP_PORT");
		dto.setUpdaterName("admin");
		bisSysParamDtoCache.updateById(dto);
		dto=bisSysParamDtoCache.selectByKey("CHL_FTP_PORT");
		System.out.println(dto);
		dto=bisSysParamDtoCache.selectById("0034");
		System.out.println(dto);
		
	}
}
