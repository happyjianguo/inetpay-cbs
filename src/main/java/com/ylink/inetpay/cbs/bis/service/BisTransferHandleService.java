package com.ylink.inetpay.cbs.bis.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTransferHandleDto;

/**
 * 
 * @author pst10
 *
 */
public interface BisTransferHandleService {
	/**
	 * 查询所有分页
	 * @param params
	 * @return
	 */
	public PageData<BisTransferHandleDto> getByCond(BisTransferHandleDto bisTransferHandleDto,PageData<BisTransferHandleDto> pageData);
	
	public PageData<BisTransferHandleDto> getByConds(BisTransferHandleDto bisTransferHandleDto, PageData<BisTransferHandleDto> pageData,String loginName);

	/**
	 * 新增
	 * @param clsCallAcctHandle
	 */
	public void insert(BisTransferHandleDto bisTransferHandleDto);
	
	/**
	 * 修改
	 * @param clsCallAcctHandle
	 */
	public void update(BisTransferHandleDto bisTransferHandleDto);
	
	/**
	 * 单条详情
	 * @param id
	 * @return
	 */
	public BisTransferHandleDto details(String id);
	
	/***
	 * 修改所有
	 * @param clsCallAcctHandle
	 */
	public void updateAll(BisTransferHandleDto bisTransferHandleDto);
	
	/**
	 * 单条详情加锁
	 * @param id
	 * @return
	 */
	public BisTransferHandleDto getById(String id);
	
	/**
	 * 审核
	 * @param bisTransferHandleDto
	 * @param bisAuditDto
	 * @return
	 */
	public boolean auditTransfer(BisTransferHandleDto bisTransferHandleDto,BisAuditDto bisAuditDto);
	
	
	/**
	 * 查询支付状态
	 * @param tradeId
	 * @return
	 */
	public BisTransferHandleDto queryCashTransfer(String tradeId,String id);
	/**
	 * 自动刷查询支付状态
	 */
	public void autoQueryUnDownStatus();
	
}
