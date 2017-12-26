package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitContentDtoWithBLOBs;

public interface MrsAduitContentService {

	/**
	 * 根据custID查询审核信息内容表
	 */
	List<MrsAduitContentDtoWithBLOBs> selectByAuditId(String id);
	
	void saveMrsAduitContentDto(MrsAduitContentDtoWithBLOBs dto);
	
	void deleteByAduitId(String id);
}
