package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.dao.MrsCertAuditDtoMapper;
import com.ylink.inetpay.common.core.constant.MrsCertAuditStatus;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertAuditDto;

@Service("mrsCertAuditService")
public class MrsCertAuditServiceImpl implements MrsCertAuditService {

	@Autowired
	private MrsCertAuditDtoMapper mrsCertAuditDtoMapper;
	
	@Override
	public void add(MrsCertAuditDto dto) {
		mrsCertAuditDtoMapper.insert(dto);
	}

	@Override
	public List<MrsCertAuditDto> findByCustId(String custId) {
		return mrsCertAuditDtoMapper.findByCustId(custId);
	}

	@Override
	public MrsCertAuditDto findOneByCustId(String custId, MrsCertAuditStatus auditStatus) {
		return mrsCertAuditDtoMapper.findOneByCustId(custId, auditStatus.getValue());
	}

}
