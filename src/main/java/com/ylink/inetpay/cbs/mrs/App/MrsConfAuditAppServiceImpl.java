package com.ylink.inetpay.cbs.mrs.App;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsConfAuditService;
import com.ylink.inetpay.common.project.cbs.app.MrsConfAuditAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditItemDto;

/**
 * 审核配置
 * @author pst23
 *
 */
@Service("mrsConfAuditAppService")
public class MrsConfAuditAppServiceImpl implements MrsConfAuditAppService {
	@Autowired
	MrsConfAuditService mrsConfAuditService;
	
	/**
     * 根据主键查询
     */
	@Override
	public MrsConfAuditDto selectByPrimaryKey(String id) {
		return mrsConfAuditService.selectByPrimaryKey(id);
	}
	/**
	 * 根据发起端，业务类型查询审核配置信息
	 */
	@Override
	public MrsConfAuditDto findByBusiTypeAndSendType(String busiType, String sendPort) {
		return mrsConfAuditService.findByBusiTypeAndSendType(busiType, sendPort);
	}
	/**
     * 根据参数查询所有数据
     */
	@Override
	public PageData<MrsConfAuditDto> quaryAllData(PageData<MrsConfAuditDto> pageData, MrsConfAuditDto mrsConfAuditDto) {
		return mrsConfAuditService.quaryAllData(pageData, mrsConfAuditDto);
	}
	/**
	 * 根据审核配置信息主键查询审核配置明细信息
	 * 明细表AuditId关联信息表Id
	 */
	@Override
	public List<MrsConfAuditItemDto> selectByAuditId(String auditId) {
		return mrsConfAuditService.selectByAuditId(auditId);
	}
	/**
     * 更新审核信息(包括审核配置表和审核配置明细表)
     */
	@Override
	public void updateAuditAndAuditItem(MrsConfAuditDto mrsConfAuditDto, MrsConfAuditItemDto mrsConfAuditItemDto,
			List<String> secUserIds,List<String> secUserLoginNames) {
		mrsConfAuditService.updateAuditAndAuditItem(mrsConfAuditDto, mrsConfAuditItemDto, secUserIds, secUserLoginNames);
	}
	/**
     * 添加审核配置信息
     */
	@Override
	public void insertSelective(MrsConfAuditDto mrsConfAuditDto,MrsConfAuditItemDto mrsConfAuditItemDto,List<String> secUserIds,List<String> secUserLoginNames) {
		mrsConfAuditService.insertSelective(mrsConfAuditDto, mrsConfAuditItemDto, secUserIds, secUserLoginNames);
	}

}
