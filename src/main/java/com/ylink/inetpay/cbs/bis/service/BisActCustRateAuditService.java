package com.ylink.inetpay.cbs.bis.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EActCustRateStatus;
import com.ylink.inetpay.common.core.constant.EAutoManual;
import com.ylink.inetpay.common.core.dto.SuccessFailDealingDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface BisActCustRateAuditService {

	public void updateSelective(BisActCustRateAuditDto bisActCustRateAudit);
	
	public BisActCustRateAuditDto findById(String id);
	
	/**
	 * 分页查询
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<BisActCustRateAuditDto> findPage(PageData<BisActCustRateAuditDto> pageData,
			BisActCustRateAuditDto queryParam);
	
	/**
	 * @throws CbsCheckedException
	 */
	public void save(BisActCustRateAuditDto actCustRateAudit);
	
	/**
	 * 审核拒绝
	 * @param auditor
	 * @param auditorName
	 * @param id
	 */
	public void auditPass(String auditor,String auditorName,String id,String reason);
	
	/**
	 * 批量审核通过
	 * @param auditor
	 * @param auditorName
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void batchAuditPass(String auditor,String auditorName,List<String> idList,String reason);
	
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
	public void cancel(String auditor,String auditorName,String id,String reason);
	/**
	 * 批量撤销
	 * @param idList
	 */
	public void batchCancel(String auditor,String auditorName,List<String> idList,String reason);
	
	public long countWaitAuditWithAccountId(String accountId);
	
	public List<BisActCustRateAuditDto> findWaitAuditWithAccountIds(List<String> accountIds);
	
	long countWaitAuditWithBankCardNo(String bankCardNo);
	public void saveOrUpateCustRate(List<String> accountIds, BigDecimal rate, Date validTime,
			String operator,String operatorName);
	List<BisActCustRateAuditDto> findPassAndValidTime(Date date);
	/**
	 * 获取审核记录数
	 * @param ids
	 * @param auditPass
	 * @param uneffective
	 * @return
	 */
	public long getAuditNum(List<String> ids, BISAuditStatus auditPass, EActCustRateStatus uneffective);
	/**
	 * 客户利率生效
	 * @param auto
	 * @param accountDate
	 * @param id
	 * @return
	 */
	public SuccessFailDealingDto custRateEffective(EAutoManual auto, String accountDate, String id);
	/**
	 * 根据批次号获取客户利率map
	 * @param batchNo
	 * @return
	 */
	public Map<String, BisActCustRateAuditDto> findCustRateMapByBatchNo(String batchNo);
	/**
	 * 根据查询条件获取客户利率列表
	 * @param queryParam
	 * @return
	 */
	public List<BisActCustRateAuditDto> list(BisActCustRateAuditDto queryParam);
	/**
	 * 根据账户id获取复核通过未生效 或者待复核的记录
	 * @param accountIds
	 * @return
	 */
	public List<BisActCustRateAuditDto> findWaitAudit(ArrayList<String> accountIds);
	/**
	 * 根据银行卡号查询复核通过未生效或者待复核的记录
	 * @param bankAccNos
	 * @return
	 */
	public List<BisActCustRateAuditDto> findWaitAuditByBankAccNos(ArrayList<String> bankAccNos);
	/**
	 * 根据账户获取待复核或者复核通过为生效的客户利率记录
	 * @param accountIds
	 * @return
	 */
	public Map<String,BisActCustRateAuditDto> findExistWaitAuditMap(@Param("accountIds")List<String> accountIds);
	/**
	 * 客户利率生效
	 * @param batchNo
	 */
	public void custRateEffective(String batchNo);
}

