package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.clear.dto.ClsAuditDto;

/**
 * 审核service
 * @author pst10
 *
 */
public interface ClsAuditService {

	/**
	 * 查询复核数据
	 * @param busId
	 * @return
	 */
	public List<ClsAuditDto> getByCond(String busId);
	
	/**
	 * 新增
	 * @param bisAuditDto
	 */
	public void insert(ClsAuditDto bisAuditDto);
	/**
	 * 判断审核人员是否已经复核
	 * 
	 * @param id
	 * @param loginName
	 * @return
	 */
	public boolean isAudit(String id, String loginName);
	/**
	 * 根据业务id获取操作记录
	 * @param id
	 * @param external 
	 * @return
	 */
	public List<ClsAuditDto> findListBybusId(String id,BISAuditType auditType);
	/**
	 * 获取审核信息列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<ClsAuditDto> findListPage(PageData<ClsAuditDto> pageData, ClsAuditDto queryParam);
	/**
	 * 获取审核详情
	 * @param id
	 * @return
	 */
	public ClsAuditDto getAudit(String id);
}
