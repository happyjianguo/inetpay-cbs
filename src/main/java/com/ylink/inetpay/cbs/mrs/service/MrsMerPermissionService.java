package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsMerPermissionDto;

public interface MrsMerPermissionService {
	/**
	 * 根据服务代码获取服务权限信息
	 * @param custId
	 * @return
	 */
	public Boolean getServiceByCode(String serviceCode,String custId);
	/**
	 * 新增服务权限
	 * @param dto
	 * @return
	 */
	public int saveService(MrsMerPermissionDto dto);
	/**
	 * 修改服务权限信息
	 * @param dto
	 * @return
	 */
	public int updateService(MrsMerPermissionDto dto);
	/**
	 * 删除服务权限信息
	 * @param serviceCode
	 * @return
	 */
	public int deleteService(String serviceCode,String custId);
	/**
	 * 批量保存服务权限信息
	 * @param merPerssionList
	 * @return
	 */
	public int batchSaveService(List<MrsMerPermissionDto> dtos,String custId);
}
