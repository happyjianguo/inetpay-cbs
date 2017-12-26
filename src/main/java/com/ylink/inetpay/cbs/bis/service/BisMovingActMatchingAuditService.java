package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisMovingActMatchingAuditDto;

/**
 * 动帐匹配复核列表
 * 
 * @author yc
 * 
 */
public interface BisMovingActMatchingAuditService  {
	/**
	 * 分页查询
	 * @param pageData 
	 * @param queryParam
	 * @return
	 */
	PageData<BisMovingActMatchingAuditDto> queryAll(PageData<BisMovingActMatchingAuditDto> pageData, BisMovingActMatchingAuditDto queryParam);
	/***
	 * 根据id查询
	 * @param id
	 * @return
	 */
	BisMovingActMatchingAuditDto detail(String id);
	/**
	 * 修改
	 * @param queryParam
	 */
	void update(BisMovingActMatchingAuditDto queryParam);
	
	List<BisMovingActMatchingAuditDto> findAllByBusIdAndStatus(String id);
	/**
	 * 动账匹配复核
	 * @param dto
	 * @return
	 */
	long movingAccountMatchingAudit(BisMovingActMatchingAuditDto dto);
	
	void save(BisMovingActMatchingAuditDto dto);

 
}
