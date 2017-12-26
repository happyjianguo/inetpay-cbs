package com.ylink.inetpay.cbs.mrs.service;


import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountAduitInfoDto;

public interface MrsAccountAduitInfoService {
	
	MrsAccountAduitInfoDto selectByPrimaryKey(String id); 
    
	void updateByPrimaryKey(MrsAccountAduitInfoDto record);
	
	PageData<MrsAccountAduitInfoDto> findListPage(PageData<MrsAccountAduitInfoDto> pagaData,MrsAccountAduitInfoDto infoDto);
	
	/**
	 * 根据审核信息主键查询审核主要信息表
	 */
	public PageData<MrsAccountAduitInfoDto> getByCont(PageData<MrsAccountAduitInfoDto> pageData,MrsAccountAduitInfoDto queryParam);
}
