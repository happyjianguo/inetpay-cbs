package com.ylink.inetpay.cbs.mrs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsEmailMapperDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsEmailMapperDto;

@Service("mrsEmailMapperService")
public class MrsEmailMapperServiceImpl implements MrsEmailMapperService {

	@Autowired
	private MrsEmailMapperDtoMapper mrsEmailMapperDtoMapper;
	
	@Override
	public String findBySuffix(String suffix) {
		MrsEmailMapperDto dto = mrsEmailMapperDtoMapper.findBySuffix(suffix);
		if(dto == null){
			return null;
		}
		return dto.getEmailUrl();
	}

}
