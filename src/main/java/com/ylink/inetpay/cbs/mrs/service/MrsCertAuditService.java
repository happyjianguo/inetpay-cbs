package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import com.ylink.inetpay.common.core.constant.MrsCertAuditStatus;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertAuditDto;

public interface MrsCertAuditService {

	public void add(MrsCertAuditDto dto);
	
	public List<MrsCertAuditDto> findByCustId(String custId);
	
	MrsCertAuditDto findOneByCustId(String custId, MrsCertAuditStatus auditStatus);
}
