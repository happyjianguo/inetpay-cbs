package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;

/**
 * 审核service
 * @author pst10
 *
 */
public interface BisAuditService {

	/**
	 * 查询复核数据
	 * @param busId
	 * @return
	 */
	public List<BisAuditDto> getByCond(String busId);
	
	/**
	 * 新增
	 * @param bisAuditDto
	 */
	public void insert(BisAuditDto bisAuditDto);
	/**
	 * 判断审核人员是否已经复核
	 * 
	 * @param id
	 * @param loginName
	 * @return
	 */
	public boolean isAudit(String id, String loginName,BISAuditType auditType);
	/**
	 * 根据当前登录用户查找被该用户审核过的业务记录
	 */
	public List<BisAuditDto> findBisAuditDtoByLoginName(String loginName,List<String> ids,BISAuditType auditType);
	/**
	 * 根据业务id获取操作记录
	 * @param id
	 * @param external 
	 * @return
	 */
	public List<BisAuditDto> findListBybusId(String id,BISAuditType auditType);
	/**
	 * 获取审核信息列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<BisAuditDto> findListPage(PageData<BisAuditDto> pageData, BisAuditDto queryParam);
	/**
	 * 获取审核详情
	 * @param id
	 * @return
	 */
	public BisAuditDto getAudit(String id);
	/**
	 * 是否存在审核记录
	 * @param id
	 * @param loginName
	 * @param auditType
	 * @return
	 */
	public boolean isExistAucit(String id, String loginName, BISAuditType auditType);
	/**
	 * 获取当前用户审核记录
	 * @param id
	 * @param loginName
	 * @param auditType
	 * @return
	 */
	public long getAucitNum(List<String> ids, String loginName, BISAuditStatus auditStatus,BISAuditType auditType);
	/**
	 * 获取冻结解冻审核详情
	 * @param id
	 * @param amountFrozen
	 * @param amountUnfreeze
	 * @return
	 */
	public List<BisAuditDto> findFrozenListBybusId(String id, BISAuditType amountFrozen);
	/**
	 * 获取当前用户退款复核记录
	 * @param busiIds
	 * @param busiRefund
	 * @param merRefund
	 * @param loginName
	 * @return
	 */
	public List<BisAuditDto> findRefundListBybusIds(List<String> busiIds, BISAuditType busiRefund,
			BISAuditType merRefund, String loginName);
}
