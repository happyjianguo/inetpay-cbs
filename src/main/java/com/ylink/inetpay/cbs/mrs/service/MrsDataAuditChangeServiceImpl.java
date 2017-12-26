package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsDataAuditChangeDtoMapper;
import com.ylink.inetpay.common.core.constant.EAuditChangeType;
import com.ylink.inetpay.common.core.constant.EAuditStatus;
import com.ylink.inetpay.common.core.constant.EAuditUserType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataAuditChangeDto;

@Service("mrsDataAuditChangeService")
public class MrsDataAuditChangeServiceImpl implements MrsDataAuditChangeService {

	@Autowired
	private MrsDataAuditChangeDtoMapper mrsDataAuditChangeDtoMapper;

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void save(String refId, EAuditChangeType changeType, String jsonStr, EAuditStatus auditStatus,
			String createUser, EAuditUserType auditType) {
		MrsDataAuditChangeDto baseDataAuditChangeDto = new MrsDataAuditChangeDto();

		baseDataAuditChangeDto.setId(baseDataAuditChangeDto.getIdentity()); // 主键ID
		baseDataAuditChangeDto.setRefId(refId); // 关联ID
		baseDataAuditChangeDto.setChangeType(changeType); // 审核类型
		baseDataAuditChangeDto.setChangeContent(jsonStr); // 变更内容
		baseDataAuditChangeDto.setAuditStatus(auditStatus);// 复核核状态
		baseDataAuditChangeDto.setCreateUser(createUser); // 创建用户
		baseDataAuditChangeDto.setCreateTime(new Date()); // 创建时间
		baseDataAuditChangeDto.setUserType(auditType); // 审核用户类型

		mrsDataAuditChangeDtoMapper.insert(baseDataAuditChangeDto);

	}

	@Override
	public MrsDataAuditChangeDto getMrsDataAuditChangeById(String id) {
		return mrsDataAuditChangeDtoMapper.selectByPrimaryKey(id);
	}
	@Override
	public boolean checkAuditData(String refId) {
		MrsDataAuditChangeDto mrsDataAuditChangeDto = mrsDataAuditChangeDtoMapper.selectByRefId(refId);
		if(mrsDataAuditChangeDto==null){
			return true;
		}else{
			return false;
		}
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateBaseDataAuditChange(MrsDataAuditChangeDto baseDataAuditChangeDto) {
		mrsDataAuditChangeDtoMapper.updateByPrimaryKeySelective(baseDataAuditChangeDto);
	}
}
