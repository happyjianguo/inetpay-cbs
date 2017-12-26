package com.ylink.inetpay.cbs.ucs.sec.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecRoleDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface UcsSecRoleService {

	/**
	 * 初始化系统管理员角色
	 * 
	 * @param roleType
	 * @return
	 */
	public UcsSecRoleDto initRole(String loginName,String roleId,String roleName);

	/**
	 * 新增或更新角色
	 * 
	 * @description
	 * @param role
	 * @author LS
	 * @date 2013-9-5
	 */
	public void saveOrUpdate(UcsSecRoleDto role, List<String> permsIds);

	/**
	 * 删除角色
	 * 
	 * @description
	 * @param roleId
	 * @author LS
	 * @date 2013-9-5
	 */
	public void delete(String roleId) throws CbsCheckedException ;

	/**
	 * 获取角色及其关联权限
	 * 
	 * @param id
	 * @author LS
	 * @date 2013-7-30
	 */
	public UcsSecRoleDto getWidthPermissions(String id);

	/**
	 * 查询角色列表
	 * 
	 * @return
	 * @author LS
	 * @date 2013-8-3
	 */
	public List<UcsSecRoleDto> listRoles(UcsSecRoleDto securityRoleDto);

	/**
	 * 分页查询角色列表
	 * 
	 * @param pageData
	 * @param params
	 * @return
	 */
	public PageData<UcsSecRoleDto> listRolesPage(
			PageData<UcsSecRoleDto> pageData, UcsSecRoleDto params);
	
	/**
	 * 更新非空字段
	 * @param role
	 */
	public void updateSelective(UcsSecRoleDto role);
	/**
	 * 判断角色名是否存在
	 * @param roleName
	 * @return
	 */
	public boolean isExistRoleName(String roleName);
}
