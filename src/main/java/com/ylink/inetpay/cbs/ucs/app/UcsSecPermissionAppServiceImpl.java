package com.ylink.inetpay.cbs.ucs.app;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecPermissionService;
import com.ylink.inetpay.common.project.cbs.app.UcsSecPermissionAppService;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecPermissionDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("ucsSecPermissionAppService")
public class UcsSecPermissionAppServiceImpl implements
		UcsSecPermissionAppService {
	
	@Autowired
	UcsSecPermissionService ucsSecPermissionService;

	@Override
	public Set<String> getPermsByRoles(Set<String> roles)
			throws CbsCheckedException {
		if(roles == null || roles.isEmpty())return null;
		return ucsSecPermissionService.getPermsByRoles(roles);
	}

	@Override
	public Set<UcsSecPermissionDto> listAll() throws CbsCheckedException {
		return ucsSecPermissionService.listAll();
	}

	@Override
	public List<UcsSecPermissionDto> listPermNavigate()
			throws CbsCheckedException {
		return ucsSecPermissionService.listPermNavigate();
	}

	@Override
	public List<UcsSecPermissionDto> getPermDtosByRoles(Set<String> roles)
			throws CbsCheckedException {
		return ucsSecPermissionService.getPermDtosByRoles(roles);
	}

	@Override
	public List<UcsSecPermissionDto> getUserTopMenus(String userId) throws CbsCheckedException {
		return ucsSecPermissionService.getUserTopMenus(userId);
	}

	@Override
	public List<UcsSecPermissionDto> getOuterTopMenus()
			throws CbsCheckedException {
		return ucsSecPermissionService.getOuterTopMenus();
	}

	@Override
	public UcsSecPermissionDto findById(String id) throws CbsCheckedException {
		return ucsSecPermissionService.findById(id);
	}

	@Override
	public void updateSelective(UcsSecPermissionDto ucsSecPermission)
			throws CbsCheckedException {
		ucsSecPermissionService.updateSelective(ucsSecPermission);
	}

}
