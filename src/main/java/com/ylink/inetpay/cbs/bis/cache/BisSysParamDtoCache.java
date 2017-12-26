/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-5
 */

package com.ylink.inetpay.cbs.bis.cache;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;

/** 
 * @author lyg
 * @date 2016-9-5
 * @description：TODO
 */

public interface BisSysParamDtoCache {

	BisSysParamDto updateById(BisSysParamDto record);
	
	BisSysParamDto selectById(String id);
	BisSysParamDto selectByKey(String key);
}
