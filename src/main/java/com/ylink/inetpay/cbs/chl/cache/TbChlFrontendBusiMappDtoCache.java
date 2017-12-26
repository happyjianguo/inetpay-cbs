/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.chl.cache;

import com.ylink.inetpay.common.project.channel.dto.TbChlFrontendBusiMapping;


public interface TbChlFrontendBusiMappDtoCache {
	
	
	public TbChlFrontendBusiMapping selectByParam(TbChlFrontendBusiMapping record);
	
	
}
