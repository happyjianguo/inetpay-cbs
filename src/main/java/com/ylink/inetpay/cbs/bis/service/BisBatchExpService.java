package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EBatchBusiType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccountFrozenAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBatchExp;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEcternalPayments;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashfund;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface BisBatchExpService {

	/**
	 * 获取分页列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<BisBatchExp> findPageList(PageData<BisBatchExp> pageData,BisBatchExp queryParam);
	/**
	 * 保存批量保证金导入数据
	 * @param batchDtos
	 * @return
	 * @throws CbsCheckedException
	 */
	public long batchCashFundExp(List<BisSetCashfund> batchDtos,BisBatchExp bisBatchExp)throws CbsCheckedException;
	/**
	 * 保存批量资金冻结
	 * @param batchDtos
	 * @return
	 * @throws CbsCheckedException
	 */
	public long batchAccountFrozenExp(List<BisAccountFrozenAuditDto> batchDtos,BisBatchExp bisBatchExp)throws CbsCheckedException;
	/**
	 * 保存批量客户利率
	 * @param batchDtos
	 * @return
	 * @throws CbsCheckedException
	 */
	public long batchCustRateExp(List<BisActCustRateAuditDto> batchDtos,BisBatchExp bisBatchExp)throws CbsCheckedException;
	/**
	 * 保存批量对外支付
	 * @param batchDtos
	 * @return
	 * @throws CbsCheckedException
	 */
	public long batchEcternalPayExp(List<BisEcternalPayments> batchDtos,BisBatchExp bisBatchExp)throws CbsCheckedException;
	/**
	 * 根据批次号获取批次明细列表
	 * @param batchNo
	 * @return
	 */
	public BisBatchExp findListByBatchNo(String batchNo);
	/**
	 * 批次导入复核
	 * @param batchNo
	 * @param auditStatus
	 * @param batchBusiType
	 * @return
	 */
	public long batchExpAudit(String batchNo, BISAuditStatus auditStatus,EBatchBusiType busiType,String loginName,String realName,String checkReason);
	/**
	 * 修改成功/失败笔数
	 * @param successNum
	 * @param failNum
	 * @param batchNo
	 */
	public void updateBatchExpNum(long successNum, long failNum, String batchNo);
	/**
	 * 获取批次记录详情
	 * @param id
	 * @return
	 */
	public BisBatchExp batchExpView(String id);
	/**
	 * 获取批次号
	 * @return
	 */
	public String getBatchNo();
	/**
	 * 判断文件是否已经导入
	 * @param expFileName
	 * @param busiType
	 * @return
	 */
	public boolean isExistFile(String expFileName, EBatchBusiType busiType);
}
