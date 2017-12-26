package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsCertFileDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;

@Service("mrsCertFileService")
public class MrsCertFileServiceImpl implements MrsCertFileService {
	
	@Autowired
	private MrsCertFileDtoMapper mrsCertFileDtoMapper;

	@Override
	public void insert(MrsCertFileDto dto) {
		
		
	}

	@Override
	public List<MrsCertFileDto> findByCustId(String custId) {
		if(StringUtils.isBlank(custId)){
			return null;
		}
		return mrsCertFileDtoMapper.findByCustId(custId);
	}

	

}
