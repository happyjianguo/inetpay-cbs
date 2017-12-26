package com.ylink.inetpay.cbs.chl.app;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.chl.service.ChlFrontendBusiMappingService;
import com.ylink.inetpay.common.core.constant.BusinessCodeEnum;
import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.constant.ECustType;
import com.ylink.inetpay.common.project.cbs.app.ChlFrontendBusiAppService;

@Service("chlFrontendBusiAppService")
public class ChlFrontendBusiAppServiceImpl implements ChlFrontendBusiAppService {

	@Resource
	private ChlFrontendBusiMappingService chlFrontendBusiMappingService;
	/**检查某个业务的支持模式
	 * 
	 * true-线上，false-线下
	 *  **/
	@Override
	public boolean checkBusiMode(ECustType custType, EChlChannelCode channelCode, BusinessCodeEnum businessCode) {
		return chlFrontendBusiMappingService.checkBusiMode(custType, channelCode, businessCode);
	}

}
