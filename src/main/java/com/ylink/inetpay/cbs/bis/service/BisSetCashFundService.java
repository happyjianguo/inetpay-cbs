package com.ylink.inetpay.cbs.bis.service;

import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.AuditTypeEnum;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EPocessStatusEnum;
import com.ylink.inetpay.common.core.constant.EffectiveStatusEnum;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashFundOperDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashfund;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface BisSetCashFundService {
	/**
	 * 查询保证金审核列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<BisSetCashfund> findCheckStatus(PageData<BisSetCashfund> pageData,
			BisSetCashfund queryParam);
	/**
	 * 根据用户编号获取保证金设置审核详情
	 * @param id
	 * @return
	 */
	public BisSetCashfund getView(String id);
	/**
	 * 批量审核
	 * @param ids
	 * @param auditReason 
	 * @return
	 * @throws CbsCheckedException
	 */
	List<String> batchAudit(List<String> ids, UcsSecUserDto userDto, AuditTypeEnum auditType, String auditReason)
			throws CbsCheckedException;
	/**
	 * 批量设置保证金
	 * @param ids
	 * @param userDto
	 * @param remarks 
	 * @param custNames 
	 * @param custIds 
	 * @throws CbsCheckedException
	 */
	public List<String> batchSetCashFund(List<String> balanceAccountIds,List<String> cashfundAccountIds,
			UcsSecUserDto userDto, Long setAmount, String effectiveDate, String remarks) throws CbsCheckedException;
	/**
	 * 定时刷新保证金设置记录
	 */
	public void flushSetCashFund();
	/**
	 * 获取审核记录数
	 * @param ids
	 * @param auditPass
	 * @param waitEffect
	 * @return
	 */
	public long getAuditNum(List<String> ids, BISAuditStatus auditPass, EffectiveStatusEnum waitEffect);
	/**
	 * 批量终止跑批任务
	 * @param ids
	 * @param stop
	 */
	public void stopSetCashFund(List<String> ids, EPocessStatusEnum stop);
	/**
	 * 根据业务主键获取所有操作纪律
	 * @param id
	 * @return
	 */
	public List<BisSetCashFundOperDto> getOperList(String id);
	/**
	 * 查询保证金设置未知状态
	 */
	public void queryUndownStatus();
	/**
	 * 根据批次号获取保证金map
	 */
	public Map<String, BisSetCashfund> findCashFundMapByBatchNo(String batchNo);
	/**
	 * 根据查询条件获取保证金list
	 * @param queryParam
	 * @return
	 */
	public List<BisSetCashfund> list(BisSetCashfund queryParam);
	/**
	 * 根据科目编号查询是否存在待复核或者复核通过 未生效的记录
	 * @param accountIds
	 * @return
	 */
	public Map<String,BisSetCashfund> findExistWaitAuditMap(List<String> accountIds);
	/**
	 * 根据批次号停止与批次相同的记录
	 * @param batchNo
	 * @param stop
	 * @return
	 */
	public long stopSetCashFundByBatchNo(String batchNo, EPocessStatusEnum stop);
	

}
