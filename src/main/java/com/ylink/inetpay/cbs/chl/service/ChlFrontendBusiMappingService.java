package com.ylink.inetpay.cbs.chl.service;

import com.ylink.inetpay.common.core.constant.BusinessCodeEnum;
import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.constant.ECustType;

public interface ChlFrontendBusiMappingService {
	/**
	 * 检查某个业务的支持模式
	 * @param custType 客户类型
	 * @param channelCode 渠道编码
	 * @param businessCode 业务功能号
	 * @return true-线上，false-线下
	 */
	public boolean checkBusiMode(ECustType custType,EChlChannelCode channelCode,BusinessCodeEnum businessCode);
}
