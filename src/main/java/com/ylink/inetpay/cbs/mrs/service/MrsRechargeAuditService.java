package com.ylink.inetpay.cbs.mrs.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsRechargeAduitDto;

public interface MrsRechargeAuditService {
	/**
	 * 查询所有信息
	 * @param pageDate
	 * @param mrsRechargeAduitDto
	 * @return
	 */
	PageData<MrsRechargeAduitDto> queryAllData(PageData<MrsRechargeAduitDto> pageDate,
			MrsRechargeAduitDto mrsRechargeAduitDto);
	
	public MrsRechargeAduitDto selectByPrimaryKey(String id) ;
	
	public void saveRechargeAudit(MrsRechargeAduitDto RechargeAduitDto,MrsPortalReviewAduitDto mrsPortalReviewAduitDto);
	
	public void updateRechargeAudit(MrsRechargeAduitDto RechargeAduitDto,MrsPortalReviewAduitDto mrsPortalReviewAduitDto);
	
	public void deleteByPrimaryKey(String id);

	public int insertSelective(MrsRechargeAduitDto dto) ;

	public int updateByPrimaryKeySelective(MrsRechargeAduitDto mrsAduitInfoDto) ;
}
