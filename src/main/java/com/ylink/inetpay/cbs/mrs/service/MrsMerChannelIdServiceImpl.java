package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsMerChannelIdDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsMerChannelIdDto;

@Service("mrsMerChannelIdService")
public class MrsMerChannelIdServiceImpl implements MrsMerChannelIdService{

	private static Logger _log = LoggerFactory.getLogger(MrsMerChannelIdServiceImpl.class);
	@Autowired
	private MrsMerChannelIdDtoMapper mrsMerChannelIdDtoMapper;
	@Override
	public String getMerChannelIdByCustId(String custId) {
		return mrsMerChannelIdDtoMapper.getMerChannelIdByCustId(custId);
	}
	@Override
	public int saveMerChannelId(MrsMerChannelIdDto dto) {
		dto.setCreateTime(new Date());
		dto.setId(UUID.randomUUID().toString());
		return mrsMerChannelIdDtoMapper.insert(dto);
	}
	@Override
	public int updateMerChannelId(MrsMerChannelIdDto dto) {
		dto.setUpdateTime(new Date());
		return mrsMerChannelIdDtoMapper.updateMerChannelId(dto);
	}
}
