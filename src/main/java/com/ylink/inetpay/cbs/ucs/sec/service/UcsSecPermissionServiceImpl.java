package com.ylink.inetpay.cbs.ucs.sec.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.ucs.sec.dao.UcsSecPermissionDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecPermissionDto;

@Service("ucsSecPermissionService")
public class UcsSecPermissionServiceImpl implements UcsSecPermissionService {

	@Autowired
	UcsSecPermissionDtoMapper ucsSecPermissionDtoMapper;

	@Override
	public Set<UcsSecPermissionDto> listAll() {
		return ucsSecPermissionDtoMapper.listAll();
	}

	@Override
	public Set<UcsSecPermissionDto> listByRoleTag(String roleTag) {
		return ucsSecPermissionDtoMapper.listByRoleTag(roleTag);
	}

	@Override
	public List<UcsSecPermissionDto> listPermNavigate() {
		List<UcsSecPermissionDto> topPerms = ucsSecPermissionDtoMapper.listPerms(null, 2);
		if (topPerms != null) {
			for (UcsSecPermissionDto perm : topPerms) {
				getChildrenPerms(perm,null);
			}
		}
		return topPerms;
	}

	private void getChildrenPerms(UcsSecPermissionDto parentPerm,Set<String> roles) {
		List<UcsSecPermissionDto> perms = null;
		int length = parentPerm.getNlevel().length() + 2;
		if(roles == null || roles.isEmpty()) {
			perms = ucsSecPermissionDtoMapper.listPerms(parentPerm.getNlevel(), length);
		} else {
			perms = ucsSecPermissionDtoMapper.getPermDtosByRoles(roles, parentPerm.getNlevel(), length);
		}
		
		if (perms == null || perms.isEmpty()) {
			return;
		}
		parentPerm.setChildren(perms);
		for (UcsSecPermissionDto perm : perms) {
			getChildrenPerms(perm,roles);
		}
	}

	@Override
	public Set<String> getPermsByRoles(Set<String> roles) {
		return ucsSecPermissionDtoMapper.getPermsByRoles(roles);
	}

	@Override
	public List<UcsSecPermissionDto> getPermDtosByRoles(Set<String> roles) {
		if(roles == null || roles.isEmpty())return listPermNavigate();
		
		List<UcsSecPermissionDto> topPerms = ucsSecPermissionDtoMapper.getPermDtosByRoles(roles,null, 2);
		if (topPerms != null) {
			for (UcsSecPermissionDto perm : topPerms) {
				getChildrenPerms(perm,roles);
			}
		}
		return topPerms;
	}

	@Override
	public List<UcsSecPermissionDto> getUserTopMenus(String userId) {
		return ucsSecPermissionDtoMapper.getUserTopMenus(userId);
	}

	@Override
	public List<UcsSecPermissionDto> getOuterTopMenus() {
		return ucsSecPermissionDtoMapper.getOuterTopMenus();
	}

	@Override
	public UcsSecPermissionDto findById(String id) {
		return ucsSecPermissionDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateSelective(UcsSecPermissionDto ucsSecPermission) {
		ucsSecPermissionDtoMapper.updateByPrimaryKeySelective(ucsSecPermission);
	}
}
