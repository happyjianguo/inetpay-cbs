package com.ylink.inetpay.cbs.mrs.service;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;

public interface MrsPlatformService {

	public MrsPlatformDto findByPlatform(String platformCode);
	
}
