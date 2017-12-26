package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditItemDto;

/**
 * 审核配置
 * @author pst23
 *
 */
public interface MrsConfAuditService {
	
	/**
     * 根据主键查询
     */
	MrsConfAuditDto selectByPrimaryKey(String id);
	/**
     * 根据主键更新信息
     */
	void updateByPrimaryKeySelective(MrsConfAuditDto mrsConfAuditDto);
	/**
	 * 根据发起端，业务类型查询审核配置信息
	 */
	MrsConfAuditDto findByBusiTypeAndSendType(String busiType, String sendPort);
	/**
     * 根据参数查询所有数据
     */
	PageData<MrsConfAuditDto> quaryAllData(PageData<MrsConfAuditDto> pageData,MrsConfAuditDto mrsConfAuditDto);
	/**
	 * 根据审核配置信息主键查询审核配置明细信息
	 */
    List<MrsConfAuditItemDto> selectByAuditId(String auditId);
    /**
     * 更新审核信息(包括审核配置表和审核配置明细表)
     * @return 
     */
    void updateAuditAndAuditItem(MrsConfAuditDto mrsConfAuditDto,MrsConfAuditItemDto mrsConfAuditItemDto,List<String> secUserIds,List<String> secUserLoginNames);
    /**
     * 添加审核配置信息
     */
    void insertSelective(MrsConfAuditDto mrsConfAuditDto,MrsConfAuditItemDto mrsConfAuditItemDto,List<String> secUserIds,List<String> secUserLoginNames);
}
