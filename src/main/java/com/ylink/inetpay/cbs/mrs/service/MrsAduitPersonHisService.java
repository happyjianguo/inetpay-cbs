package com.ylink.inetpay.cbs.mrs.service;


import java.util.List;

import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonHisDto;

public interface MrsAduitPersonHisService {
	
	/**
	 * 审核信息表
	 * @param pageData
	 * @param hisDto
	 * @return
	 */
	PageData <MrsAduitPersonHisDto> findListHisPage(PageData <MrsAduitPersonHisDto> pageData,MrsAduitPersonHisDto hisDto);
    
	MrsAduitPersonHisDto selectByPrimaryKey(String id);
    
	MrsAduitPersonDto selectPersonByKey(String id);
	
	MrsAduitPersonDto selectByAduitUserName(String aduitUserName,String aduitId);
	/**
     * 候选人审核表
     * @param pageData
     * @param perDto
     * @return
     */
	PageData <MrsAduitPersonDto> findListPerPage(PageData <MrsAduitPersonDto> pageData,MrsAduitPersonDto perDto);
    
	List<MrsAduitPersonHisDto> findListHisAll(MrsAduitPersonHisDto hisDto);
	
	void save(MrsAduitPersonDto record);
    
	void deleteByPrimaryKey(String id);

	void updateByPrimaryKey(MrsAduitPersonDto record);
	
	/**
	 * 根据当前登录用户ID查询审核人信息表的审核信息主键
	 */
	public List<String> getByCurrentUserId(String id);
	
	List<MrsAduitPersonDto> findByAduitId(String aduitId);
}
