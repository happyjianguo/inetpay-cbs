package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsMerPermissionDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsMerPermissionDto;

@Service("mrsMerPermissionService")
public class MrsMerPermissionServiceImpl implements MrsMerPermissionService{
	@Autowired
	private MrsMerPermissionDtoMapper mrsMerPermissionDtoMapper;
	private static Logger _log = LoggerFactory.getLogger(MrsMerPermissionServiceImpl.class);

	@Override
	public Boolean getServiceByCode(String serviceCode,String custId) {
		if(mrsMerPermissionDtoMapper.getServiceByCode(serviceCode,custId)>0){
			return true;
		}
		return false;
	}

	@Override
	public int saveService(MrsMerPermissionDto dto) {
		dto.setId(UUID.randomUUID().toString());
		dto.setCreateTime(new Date());
		return mrsMerPermissionDtoMapper.insert(dto);
	}

	@Override
	public int updateService(MrsMerPermissionDto dto) {
		dto.setUpdateTime(new Date());
		return mrsMerPermissionDtoMapper.updateService(dto);
	}

	@Override
	public int deleteService(String serviceCode,String custId) {
		return mrsMerPermissionDtoMapper.deleteService(serviceCode,custId);
	}

	@Override
	public int batchSaveService(List<MrsMerPermissionDto> dtos,String custId) {
		for (MrsMerPermissionDto mrsMerPermissionDto : dtos) {
			mrsMerPermissionDto.setId(UUID.randomUUID().toString());
			mrsMerPermissionDto.setCreateTime(new Date());
			mrsMerPermissionDto.setCustId(custId);
		}
		return mrsMerPermissionDtoMapper.batchSaveService(dtos);
	}
}
