package com.ylink.inetpay.cbs.mrs.service;

import com.ylink.inetpay.common.core.constant.EAuditChangeType;
import com.ylink.inetpay.common.core.constant.EAuditStatus;
import com.ylink.inetpay.common.core.constant.EAuditUserType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataAuditChangeDto;

/**
 * 审核Service接口
 * 
 * @author pst01
 *
 */
public interface MrsDataAuditChangeService {
	/**
	 * 添加一条数据审核信息
	 * 
	 * @param refId
	 *            关联ID
	 * @param changeType
	 *            审核类型
	 * @param jsonStr
	 *            变更内容
	 * @param auditStatus
	 *            审核状态
	 * @param createUser
	 *            创建用户
	 * @param auditType
	 *            审核用户类型
	 */
	public void save(String refId, EAuditChangeType changeType, String jsonStr, EAuditStatus auditStatus,
			String createUser, EAuditUserType auditType);
	/**
	 * 根据主键ID查审核信息
	 * 
	 * @param id
	 *            主键ID
	 */
	public MrsDataAuditChangeDto getMrsDataAuditChangeById(String id);
	/**
	 * 检查是否有待审核数据
	 * @param refId
	 * @return
	 */
	public boolean checkAuditData(String refId);
	/**
	 * 更新审核信息
	 * 
	 * @param baseDataAuditChangeDto
	 */
	public void updateBaseDataAuditChange(MrsDataAuditChangeDto baseDataAuditChangeDto);
}
