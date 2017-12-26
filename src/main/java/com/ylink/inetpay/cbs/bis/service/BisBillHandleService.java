package com.ylink.inetpay.cbs.bis.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBillHandleDto;

/**
 * 手工记账经办
 * @author pst10
 *
 */
public interface BisBillHandleService {
	/**
	 * 查询所有分页
	 * @param params
	 * @return
	 */
	public PageData<BisBillHandleDto> getByCond(BisBillHandleDto bisBillHandleDto,PageData<BisBillHandleDto> pageData);
	
	/**
	 * 新增
	 * @param actBillHandleDto
	 */
	public void insert(BisBillHandleDto bisBillHandleDto);
	
	/**
	 * 修改
	 * @param actBillHandleDto
	 */
	public void update(BisBillHandleDto bisBillHandleDto);
	
	/**
	 * 单条详情
	 * @param id
	 * @return
	 */
	public BisBillHandleDto details(String id);
	
	/**
	 * 审核
	 * @param bisBillHandleDto
	 * @param bisAuditDto
	 * @return
	 */
	public boolean auditBill(BisBillHandleDto bisBillHandleDto,BisAuditDto bisAuditDto);
	
	
	/**
	 * 查询支付状态
	 * @param tradeId
	 * @return
	 */
	public BisBillHandleDto findByOrderAndPayAdjustType(String tradeId,String id);
}
