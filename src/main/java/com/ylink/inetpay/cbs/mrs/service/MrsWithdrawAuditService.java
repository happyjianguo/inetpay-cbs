package com.ylink.inetpay.cbs.mrs.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsWithdrawAduitDto;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.bank.AduitVo;

public interface MrsWithdrawAuditService {
	/**
	 * 查询所有信息
	 * @param pageDate
	 * @param mrsRechargeAduitDto
	 * @return
	 */
	PageData<MrsWithdrawAduitDto> queryAllData(PageData<MrsWithdrawAduitDto> pageDate,
			MrsWithdrawAduitDto mrsWithdrawAduitDto);
	
	/**
	 * 根据主键进行查询
	 * @param id
	 * @return
	 */
	public MrsWithdrawAduitDto findDtoById(String id);
	
	/**
	 * 审核
	 * @param vo
	 * @param name
	 */
	public RespCheckVO aduit(AduitVo vo,String name,String userId);
	/**
	 * 保存提现申请到审核表
	 */
	public RespCheckVO  saveWithdrawAudit(MrsWithdrawAduitDto mrsWithdrawAduitDto,MrsPortalReviewAduitDto mrsPortalReviewAduitDto);

	/**
	 * 获取序列号-用于充值提现审核申请生成流水
	 * 
	 * @return
	 */
	public String getMrsAuditOrderNosVal();
}
