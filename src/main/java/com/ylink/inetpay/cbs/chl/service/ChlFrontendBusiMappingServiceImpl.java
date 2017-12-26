package com.ylink.inetpay.cbs.chl.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.chl.cache.TbChlFrontendBusiMappDtoCache;
import com.ylink.inetpay.common.core.constant.BusinessCodeEnum;
import com.ylink.inetpay.common.core.constant.ECHLBusiMode;
import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.constant.ECustType;
import com.ylink.inetpay.common.project.channel.dto.TbChlFrontendBusiMapping;

@Service("chlFrontendBusiMappingService")
public class ChlFrontendBusiMappingServiceImpl implements ChlFrontendBusiMappingService {

	@Resource
	private TbChlFrontendBusiMappDtoCache tbChlFrontendBusiMappDtoCache;
	
	@Override
	public boolean checkBusiMode(ECustType custType, EChlChannelCode channelCode, BusinessCodeEnum businessCode) {
		TbChlFrontendBusiMapping record = new TbChlFrontendBusiMapping();
		record.setCustType(custType.getValue());
		record.setChannelCode(channelCode.getValue());
		record.setBusiCode(businessCode.getValue());
		record = tbChlFrontendBusiMappDtoCache.selectByParam(record);
		return record != null && ECHLBusiMode.ON_LINE.getValue().equals(record.getBusiMode()) ? true: false;
	}

}
