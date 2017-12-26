package com.ylink.inetpay.cbs.ucs.sec.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.ucs.sec.dao.UcsSecUserDtoMapper;
import com.ylink.inetpay.common.core.constant.EUcsSecUserStatus;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.core.util.PasswordUtil;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecRoleDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;

@Transactional(value=CbsConstants.TX_MANAGER_UCS)
@Service("ucsSecUserService")
public class UcsSecUserServiceImpl implements UcsSecUserService {

	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);

	@Autowired
	private UcsSecRoleService roleService;

	@Autowired
	private UcsSecUserDtoMapper ucsSecUserDtoMapper;

	/**
	 * 
	 * @param name
	 * @return
	 * @author LS
	 * @date 2013-8-6
	 */
	@Override
	public UcsSecUserDto getByLoginName(String name) {
		return ucsSecUserDtoMapper.getByLoginName(name);
	}

	/**
	 * 初始化系统管理员账户
	 * 
	 * @author LS
	 * @date 2013-7-29
	 */
	@Override
	public void initUcsSecUserDto() {
		initSysUser(CbsConstants.ROOT1_LOGIN_NAME,"管理员1",CbsConstants.SUPER_ROLE1,"管理员角色1");
        initSysUser(CbsConstants.ROOT2_LOGIN_NAME,"管理员2",CbsConstants.SUPER_ROLE2,"管理员角色2");
        _log.info("成功初始化系统账户！");
	}

	private void initSysUser(String loginName,String realName,String roleId,String roleName) {
		UcsSecUserDto root = ucsSecUserDtoMapper.getByLoginName(loginName);
		if (root == null) {
			root = new UcsSecUserDto();
			root.setLoginName(loginName);
			root.setRealName(realName);
			root.setPassword(PasswordUtil.encryptPlainByMd5WithSalt(CbsConstants.DEFAULT_PASSWORD));
			root.setMobile("1");
			root.setEmail("1@1.com");
			root.setStatus(EUcsSecUserStatus.NORMAL);
			root.setCreater(loginName);
			root.setCreaterName(realName);
			root.setCreateTime(new Date());
			UcsSecRoleDto superRole = roleService.initRole(loginName,roleId,roleName);
			root.addRole(superRole);
			try {
				// 保存账户
				ucsSecUserDtoMapper.insert(root);
				root.setId(root.getIdentity());
				// 更新权限
				updateRole(root);
			} catch (Exception e) {
				String msg = "初始化系统账户异常：" + e.getMessage();
				_log.error("初始化系统账户异常：{}",ExceptionProcUtil.getExceptionDesc(e));
				throw new CbsUncheckedException(ECbsErrorCode.SYS_ERROR.getValue(),msg);
			}
		} else {
			//每次启动就给超级管理员角色设置所有的权限
			roleService.initRole(loginName,roleId,roleName);
		}
	}

	/**
	 * 更新用户角色关联信息
	 * 
	 * @param account
	 * @author LS
	 * @date 2013-8-6
	 */
	private void updateRole(UcsSecUserDto account) {
		if (account != null) {
			if (account.getRoles() != null && !account.getRoles().isEmpty()) {
				// 先删除所有的角色，再添加角色
				ucsSecUserDtoMapper.deleteUserRoles(account.getId());
				List<String> roleIdList = new ArrayList<String>();
				for (UcsSecRoleDto role : account.getRoles()) {
					roleIdList.add(role.getId());
				}
				// 批量插入
				ucsSecUserDtoMapper.addUserRoles(account.getId(), roleIdList);
			} else {
				// 删除所有角色
				ucsSecUserDtoMapper.deleteUserRoles(account.getId());
			}
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @author LS
	 * @date 2013-8-6
	 */
	@Override
	public UcsSecUserDto getWithRoles(String id) {
		return ucsSecUserDtoMapper.getWithRoles(id);
	}

	/**
	 * 
	 * @param loginName
	 * @return
	 * @author LS
	 * @date 2013-8-6
	 */
	@Override
	public UcsSecUserDto getWithRolesByLoginName(String loginName) {
		return ucsSecUserDtoMapper.getWithRolesByLoginName(loginName);
	}

	/**
	 * 
	 * @param pageData
	 * @param params
	 * @param orderKey
	 * @param asc
	 * @return
	 * @author LS
	 * @date 2013-8-6
	 */
	@Override
	public PageData<UcsSecUserDto> listWidthRolesPage(
			PageData<UcsSecUserDto> pageData,
			UcsSecUserDto params) {
		List<UcsSecUserDto> list = ucsSecUserDtoMapper.listWidthRolesPage(params, 
				pageData.getBeginIndex(),pageData.getEndIndex());
		long total = ucsSecUserDtoMapper.listWidthRolesPageCount(params);
		
		pageData.setRows(list);
		pageData.setTotal(total);

		return pageData;
	}

	@Override
	public List<UcsSecUserDto> list(UcsSecUserDto params) {
		return ucsSecUserDtoMapper.listWidthRoles(params);
	}

	/**
	 * 
	 * @param entity
	 * @author LS
	 * @date 2013-8-6
	 */
	@Override
	public void saveWithRoles(UcsSecUserDto entity) {
		entity.setCreateTime(new Date());
		ucsSecUserDtoMapper.insert(entity);
		entity.setId(entity.getIdentity());
		updateRole(entity);
	}

	/**
	 * 
	 * @param entity
	 * @author LS
	 * @date 2013-8-6
	 */
	@Override
	public void updateWithRoles(UcsSecUserDto entity) {
		ucsSecUserDtoMapper.updateByPrimaryKeySelective(entity);
		updateRole(entity);
	}

	/**
	 * 
	 * @param id
	 * @param loginName
	 * @return
	 * @author LS
	 * @date 2013-8-6
	 */
	@Override
	public boolean checkLoginName(String id, String loginName) {
		int count = ucsSecUserDtoMapper.checkLoginName(id, loginName);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param id
	 * @param email
	 * @return
	 * @author LS
	 * @date 2013-8-6
	 */
	@Override
	public boolean checkEmail(String id, String email) {
		int count = ucsSecUserDtoMapper.checkEmail(id, email);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void updateLockTime(String id) {
		ucsSecUserDtoMapper.updateLockTime(id, new Date());
	}

	@Override
	public void delete(String id) {
		ucsSecUserDtoMapper.updateStatus(id, EUcsSecUserStatus.DELETE);
	}

	@Override
	public void enableLoginAcccount(String id) {
		ucsSecUserDtoMapper.updateStatus(id,EUcsSecUserStatus.NORMAL);
	}

	@Override
	public void disableLoginAcccount(String id) {
		ucsSecUserDtoMapper.updateStatus(id,EUcsSecUserStatus.DISABLED);
	}

	@Override
	public void changepwd(String id, String password) {
		UcsSecUserDto entity = new UcsSecUserDto();
		entity.setId(id);
		entity.setPassword(password);
		entity.setLastUpdatePasswordTime(new Date());
		ucsSecUserDtoMapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public void resetpwd(String id, String password) {
		UcsSecUserDto entity = get(id);
		entity.setId(id);
		entity.setPassword(password);
		entity.setLastUpdatePasswordTime(null);
		ucsSecUserDtoMapper.updateByPrimaryKey(entity);
	}

	@Override
	public UcsSecUserDto get(String id) {
		return ucsSecUserDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void updateSelective(UcsSecUserDto entity) {
		entity.setUpdateTime(new Date());
		ucsSecUserDtoMapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<UcsSecUserDto> listByDeptId(String deptId) {
		return ucsSecUserDtoMapper.listByDeptId(deptId);
	}

	@Override
	public void clearLoginFailInfo() {
		ucsSecUserDtoMapper.clearLoginFailInfo();
	}
}
