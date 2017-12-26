package com.ylink.inetpay.cbs.bis.service;

import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ResultCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEcternalPayments;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
public interface BisEcternalPaymentsService {
	/**
	 * 保存对外支付信息
	 * @param bankCode
	 * @throws CbsCheckedException
	 */
	public void saveEcternalPayments(BisEcternalPayments bisEcternalPayments) throws CbsCheckedException;
	/**
	 * 修改对外支付信息
	 * @param bankCode
	 * @throws CbsCheckedException
	 */
	public void updateEcternalPaymentsById(BisEcternalPayments bisEcternalPayments) throws CbsCheckedException;
	/**
	 * 删除对外支付信息
	 * @param id
	 * @throws CbsCheckedException
	 */
	public void deleteEcternalPaymentsId(String id) throws CbsCheckedException;
	/**
	 * 对外支付列表
	 * @param pageDate
	 * @param bisEcternalPayments
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<BisEcternalPayments> findListPage(PageData<BisEcternalPayments> pageDate,BisEcternalPayments bisEcternalPayments)throws CbsCheckedException;
	/**
	 * 审核
	 * @param auditType
	 * @param id
	 * @param userDto 
	 * @param reason 
	 * @throws CbsCheckedException
	 */
	public void audit(String auditType, String id, UcsSecUserDto userDto, String reason)throws CbsCheckedException;
	/**
	 * 根据id获取对外支付信息(详情)
	 * @param id
	 * @return
	 */
	public BisEcternalPayments selectOneBeanById(String id);
	/**
	 * 根据id获取对外支付信息
	 * @param id
	 * @return
	 */
	public BisEcternalPayments getOldDto(String id);
	/**
	 * 根据编号id获取支付状态
	 * @param id
	 */
	public void queryPayStatus(String id);
	/**
	 * 保存保存对商户对外支付订单
	 * @param bisEcternalPayments
	 * @return
	 */
	public ResultCode saveMerChantEcternalPayments(PayAmtAllocateDto payAmtAllocateDto);
	/**
	 * 自从刷新支付状态
	 */
	public void autoQueryUnDownStatus();
	/**
	 * 自动对外支付
	 */
	public void autoEctPay();
	/**
	 * 根据批次号获取对外支付map
	 * @param batchNo
	 * @return
	 */
	public Map<String, BisEcternalPayments> findEcternalPaymentsMapByBatchNo(String batchNo);
	/**
	 * 根据查询条件获取冻结列表
	 * @param queryParam
	 * @return
	 */
	public List<BisEcternalPayments> list(BisEcternalPayments queryParam);
}
