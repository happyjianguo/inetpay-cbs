package com.ylink.inetpay.cbs.ucs.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecRoleService;
import com.ylink.inetpay.common.project.cbs.app.UcsSecRoleAppService;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecRoleDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("ucsSecRoleAppService")
public class UcsSecRoleAppServiceImpl implements UcsSecRoleAppService {

	@Autowired
	private UcsSecRoleService ucsSecRoleService;

	@Override
	public void saveOrUpdate(UcsSecRoleDto role, List<String> permsIds)
			throws CbsCheckedException {
		ucsSecRoleService.saveOrUpdate(role, permsIds);
	}

	@Override
	public void delete(String roleId) throws CbsCheckedException {
		ucsSecRoleService.delete(roleId);
	}

	@Override
	public UcsSecRoleDto getWidthPermissions(String id)
			throws CbsCheckedException {
		return ucsSecRoleService.getWidthPermissions(id);
	}

	@Override
	public List<UcsSecRoleDto> listRoles(UcsSecRoleDto securityRoleDto)
			throws CbsCheckedException {
		return ucsSecRoleService.listRoles(securityRoleDto);
	}

	@Override
	public PageData<UcsSecRoleDto> listRolesPage(
			PageData<UcsSecRoleDto> pageData, UcsSecRoleDto params)
			throws CbsCheckedException {
		return ucsSecRoleService.listRolesPage(pageData, params);
	}

	@Override
	public void updateSelective(UcsSecRoleDto role) throws CbsCheckedException {
		ucsSecRoleService.updateSelective(role);
	}

	@Override
	public boolean isExistRoleName(String roleName) {
		return ucsSecRoleService.isExistRoleName(roleName);
	}
}
