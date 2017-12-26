package com.ylink.inetpay.cbs.ucs.sec.service;

import java.util.List;
import java.util.Set;

import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecPermissionDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface UcsSecPermissionService {

	/**
	 * 获取所有的权限
	 * 
	 * @return
	 * @author LS
	 * @date 2013-7-31
	 */
	Set<UcsSecPermissionDto> listAll();

	/**
	 * 根据角色标识获取权限
	 * @return
     */
	Set<UcsSecPermissionDto> listByRoleTag(String roleTag);

	/**
	 * 获取权限层级导航
	 * 
	 * @description
	 * @return
	 * @author LS
	 * @date 2013-9-9
	 */
	List<UcsSecPermissionDto> listPermNavigate();
	
	/**
	 * 获取角色列表的权限集合
	 * @param roles
	 * @return
	 */
	List<UcsSecPermissionDto> getPermDtosByRoles(Set<String> roles);

	/**
	 * 根据角色列表获取权限信息
	 * 
	 * @param roles
	 * @return
	 */
	Set<String> getPermsByRoles(Set<String> roles);
	
	/**
	 * 获取顶级菜单
	 * @return
	 */
	List<UcsSecPermissionDto> getUserTopMenus(String userId);
	
	/**
	 * 获取外部系统列表
	 * @return
	 * @throws CbsCheckedException
	 */
	public List<UcsSecPermissionDto> getOuterTopMenus();
	
	/**
	 * 根据ID获取权限对象
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public UcsSecPermissionDto findById(String id);
	
	/**
	 * 更新
	 * @param ucsSecPermission
	 * @throws CbsCheckedException
	 */
	public void updateSelective(UcsSecPermissionDto ucsSecPermission);
}
