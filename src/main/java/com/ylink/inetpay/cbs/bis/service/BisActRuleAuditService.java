package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EActRuleType;
import com.ylink.inetpay.common.core.constant.ETradeType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActRuleAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface BisActRuleAuditService {

	public BisActRuleAuditDto findById(String id);
	
	/**
	 * 分页查询
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<BisActRuleAuditDto> findPage(PageData<BisActRuleAuditDto> pageData,
			BisActRuleAuditDto queryParam);
	
	/**
	 * 变更记账规则
	 * @throws CbsCheckedException
	 */
	public void changeActRule(BisActRuleAuditDto actRuleAudit);
	
	/**
	 * 根据交易类型统计待审核的记录数，同一交易类型，每次只能有一条处于到审核状态
	 * @param tradeType
	 * @param id 
	 * @return
	 * @throws CbsCheckedException
	 */
	public long countWaitAuditByTradeTypeAndRuleType(ETradeType tradeType,EActRuleType ruleType);
	
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
	public void auditReject(String auditor,String auditorName,String id);
	
	/**
	 * 批量审核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void batchAuditReject(String auditor,String auditorName,List<String> idList);
	
	/**
	 * 撤销
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void cancel(String id);
	/**
	 * 批量撤销
	 * @param idList
	 */
	public void batchCancel(List<String> idList);
	/**
	 * 获取审核记录
	 * @param ids
	 * @param auditPass
	 * @param uneffective
	 * @return
	 */
	public long getAuditNum(List<String> ids, BISAuditStatus auditPass);
}
