package com.ylink.inetpay.cbs.bis.service;

import java.util.ArrayList;
import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisActInterestDateAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface BisActInterestDateAuditService {

public BisActInterestDateAuditDto findById(String id);
	
	/**
	 * 分页查询
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<BisActInterestDateAuditDto> findPage(PageData<BisActInterestDateAuditDto> pageData,
			BisActInterestDateAuditDto queryParam);
	
	/**
	 * @throws CbsCheckedException
	 */
	public void save(BisActInterestDateAuditDto actCustRateAudit);
	
	/**
	 * 审核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param id
	 */
	public void auditPass(String auditor,String auditorName,String id);
	
	/**
	 * 批量审核通过
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void batchAuditPass(String auditor,String auditorName,List<String> idList);
	
	/**
	 * 审核通过
	 * @param auditor
	 * @param auditorName
	 * @param id
	 */
	public void auditReject(String auditor,String auditorName,String reason,String id);
	
	/**
	 * 批量审核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void batchAuditReject(String auditor,String auditorName,String reason,List<String> idList);
	
	/**
	 * 撤销
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void cancel(String loginName,String realName,String id,String reason);
	/**
	 * 批量撤销
	 * @param idList
	 */
	public void batchCancel(String loginName,String realName,List<String> idList,String reason);
	
	public long countWaitAuditWithAccountId(String accountId);
	/**
	 * 根据账户编号获取待复核记录
	 * @param accountIds
	 * @return
	 */
	public List<BisActInterestDateAuditDto> findWaitAudit(ArrayList<String> accountIds);
}
