/**
 * 版权所有(C) 2016 深圳市雁联计算系统有限公司
 * 创建:lyg 2016-9-2
 */

package com.ylink.inetpay.cbs.bis.cache;

import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailTemplateDto;

/** 
 * @author lyg
 * @date 2016-9-2
 * @description：TODO
 */

public interface BisEmailTemplateDtoCache {

	BisEmailTemplateDto updateByEmailTempla(BisEmailTemplateDto record);
	    /**
	     * 根据编号获取邮件模板
	     * @param templateCode
	     * @return
	     */
	BisEmailTemplateDto getEmailTempla(EBisEmailTemplateCode templateCode);
}
