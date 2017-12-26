package com.ylink.inetpay.cbs.ucs.sec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.ucs.sec.dao.UcsSecRoleDtoMapper;
import com.ylink.inetpay.common.core.constant.ENormalDisabledStatus;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecPermissionDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecRoleDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

@Transactional(value=CbsConstants.TX_MANAGER_UCS)
@Service("ucsSecRoleService")
public class UcsSecRoleServiceImpl implements UcsSecRoleService {

	@Autowired
	private UcsSecPermissionService permissionService;

	@Autowired
	private UcsSecRoleDtoMapper ucsSecRoleDtoMapper;

	/**
	 * 初始化系统超级管理员角色
	 * 
	 * @return
	 * @author LS
	 * @date 2013-7-29
	 */
	@Override
	public UcsSecRoleDto initRole(String loginName,String roleId,String roleName) {
		UcsSecRoleDto role = ucsSecRoleDtoMapper.selectByPrimaryKey(roleId);
		if (role == null) {
			role = new UcsSecRoleDto();
			role.setId(roleId);
			role.setRoleName(roleName);
			role.setStatus(ENormalDisabledStatus.NORMAL);
			role.setCreater(loginName);
			role.setCreateTime(new Date());
			role.setRemark("系统自动创建");
			ucsSecRoleDtoMapper.saveWithId(role);
		}
		if (role != null) {
			// 设置相应权限给该角色
			Set<UcsSecPermissionDto> perms = permissionService.listByRoleTag(roleId);
			if (perms != null) {
				role.setPermissions(perms);
				updatePermission(role);
			}
		}
		return role;
	}
	
	/**
	 * 给角色设置权限
	 * 
	 * @param role
	 * @author LS
	 * @date 2013-8-6
	 */
	private void updatePermission(UcsSecRoleDto role) {
		if (role != null && role.getPermissions() != null
				&& !role.getPermissions().isEmpty()) {
			ucsSecRoleDtoMapper.deletePermsByRoleId(role.getId());
			List<String> permIdList = new ArrayList<String>();
			for (UcsSecPermissionDto perm : role.getPermissions()) {
				permIdList.add(perm.getId());
			}
			if(!permIdList.isEmpty()) {
				ucsSecRoleDtoMapper.addRolePermissions(role.getId(), permIdList);
			}
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @author LS
	 * @date 2013-7-30
	 */
	@Override
	public UcsSecRoleDto getWidthPermissions(String id) {
		return ucsSecRoleDtoMapper.getWidthPermissions(id);
	}

	@Override
	public List<UcsSecRoleDto> listRoles(UcsSecRoleDto role) {
		return ucsSecRoleDtoMapper.listRoles(role);
	}

	@Override
	public void delete(String roleId) throws CbsCheckedException {
		List<String> userIdList = ucsSecRoleDtoMapper.getUserList(roleId);
		if (userIdList != null && userIdList.size() > 0) {
			throw new CbsCheckedException(ECbsErrorCode.SYS_ERROR.getValue(),"有用户关联了该角色，不能删除");
		}
		//删除角色与权限的关联
		ucsSecRoleDtoMapper.deletePermsByRoleId(roleId);
		//删除角色
		ucsSecRoleDtoMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public void saveOrUpdate(UcsSecRoleDto role, List<String> permsIds) {
		if (StringUtils.isNotBlank(role.getId())) {
			ucsSecRoleDtoMapper.updateByPrimaryKeySelective(role);
		} else {
			role.setStatus(ENormalDisabledStatus.NORMAL);
			role.setCreateTime(new Date());
			ucsSecRoleDtoMapper.insert(role);
			role.setId(role.getIdentity());
		}
		// 更新权限
		ucsSecRoleDtoMapper.deletePermsByRoleId(role.getId());
		if (permsIds != null && !permsIds.isEmpty()) {
			ucsSecRoleDtoMapper.addRolePermissions(role.getId(), permsIds);
		}
	}

	@Override
	public PageData<UcsSecRoleDto> listRolesPage(
			PageData<UcsSecRoleDto> pageData, UcsSecRoleDto params) {
		
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<UcsSecRoleDto> list = ucsSecRoleDtoMapper.listRoles(params);
		
		Page<UcsSecRoleDto> page = (Page<UcsSecRoleDto>)list;
		
		pageData.setRows(list);
		pageData.setTotal(page.getTotal());
		return pageData;
	}

	@Override
	public void updateSelective(UcsSecRoleDto role) {
		ucsSecRoleDtoMapper.updateByPrimaryKeySelective(role);
	}

	@Override
	public boolean isExistRoleName(String roleName) {
		List<UcsSecRoleDto> role = ucsSecRoleDtoMapper.isExistRoleName(roleName,ENormalDisabledStatus.NORMAL);
		return role!=null && role.isEmpty();
	}
}
