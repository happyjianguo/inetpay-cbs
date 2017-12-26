/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-2
 */

package com.ylink.inetpay.cbs.bis.cache;

import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsTemplateDto;

/** 
 * @author lyg
 * @date 2016-9-2
 * @description：TODO
 */

public interface BisSmsTemplateDtoCache {

	public BisSmsTemplateDto getSmsTempla(EBisTemplateCode templateCode);
	public BisSmsTemplateDto updateBySmsTempla(BisSmsTemplateDto record);
}
