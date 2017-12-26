package com.ylink.inetpay.cbs.mrs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsPlatformDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;

@Service("mrsPlatformService")
public class MrsPlatformServiceImpl implements MrsPlatformService {

	@Autowired
	private MrsPlatformDtoMapper mrsPlatformDtoMapper;
	
	@Override
	public MrsPlatformDto findByPlatform(String platformCode) {
		
		return mrsPlatformDtoMapper.findByPlatformCode(platformCode, null);
	}

}
