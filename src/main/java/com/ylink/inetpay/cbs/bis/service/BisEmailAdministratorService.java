package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.constant.bis.EMessageType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailAdministratorDto;

/**
 * 收件人配置
 * @author pst18
 *
 */
public interface BisEmailAdministratorService {
		
	public PageData<BisEmailAdministratorDto> pageList(PageData<BisEmailAdministratorDto> pageData, BisEmailAdministratorDto queryParam);

	public long save(BisEmailAdministratorDto dto);

	public BisEmailAdministratorDto view(String id);
	
	public long delete(String id);

	public long update(BisEmailAdministratorDto dto);
	/**
	 * 根据信息类型修改收件人配置
	 * @param messageType
	 * @param dtos
	 * @return
	 */
	public long updateByMessageType(EMessageType messageType,List<BisEmailAdministratorDto> dtos);
	/**
	 * 批量保存
	 * @param dtos
	 * @return
	 */
	public long batchSaveByMessageType(EMessageType messageType,List<BisEmailAdministratorDto> dtos);
	/**
	 * 根据信息类型删除收件人配置
	 * @param messageType
	 * @return
	 */
	public long deleteByMessageType(EMessageType messageType);
	
	public List<BisEmailAdministratorDto> viewByMessageType(EMessageType messageType);
}

