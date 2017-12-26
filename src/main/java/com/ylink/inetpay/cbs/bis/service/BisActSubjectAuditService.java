package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActSubjectAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface BisActSubjectAuditService {

	/**
	 * 根据主键获取对象
	 * 
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public BisActSubjectAuditDto findById(String id);

	/**
	 * 分页查询
	 * 
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<BisActSubjectAuditDto> findPage(PageData<BisActSubjectAuditDto> pageData,
			BisActSubjectAuditDto queryParam);

	/**
	 * 变更
	 * 
	 * @throws CbsCheckedException
	 */
	public void changeActSubject(BisActSubjectAuditDto actSubjectAudit);

	/**
	 * 根据交易类型统计待审核的记录数，同一交易类型，每次只能有一条处于到审核状态
	 * 
	 * @param tradeType
	 * @return
	 * @throws CbsCheckedException
	 */
	public long countBySubjectNo(String subjectNo);

	/**
	 * 审核拒绝
	 * 
	 * @param auditor
	 * @param auditorName
	 * @param id
	 */
	public void auditPass(String auditor, String auditorName, String id);

	/**
	 * 批量审核通过
	 * 
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void batchAuditPass(String auditor, String auditorName, List<String> idList);

	/**
	 * 审核通过
	 * 
	 * @param auditor
	 * @param auditorName
	 * @param id
	 */
	public void auditReject(String auditor, String auditorName, String id);

	/**
	 * 批量审核拒绝
	 * 
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void batchAuditReject(String auditor, String auditorName, List<String> idList);

	/**
	 * 撤销
	 * 
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void cancel(String id);

	/**
	 * 撤销
	 * 
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void batchCancel(List<String> idList);
	/**
	 * 获取审核记录数
	 * @param ids
	 * @param auditReject
	 * @return
	 */
	public long getAuditNum(List<String> ids, BISAuditStatus auditReject);
}
