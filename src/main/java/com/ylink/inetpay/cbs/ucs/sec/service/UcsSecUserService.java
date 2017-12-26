package com.ylink.inetpay.cbs.ucs.sec.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;

public interface UcsSecUserService {

	/**
	 * 获取用户信息（不含角色）
	 * 
	 * @param id
	 * @return
	 */
	UcsSecUserDto get(String id);

	/**
	 * 获取登录用户及关联角色
	 * 
	 * @param id
	 * @return
	 * @author LS
	 * @date 2013-7-29
	 */
	UcsSecUserDto getWithRoles(String id);

	/**
	 * 获取登录用户及关联角色
	 * 
	 * @param loginName
	 * @return
	 * @author LS
	 * @date 2013-7-29
	 */
	UcsSecUserDto getWithRolesByLoginName(String loginName);

	/**
	 * 查询账户列表
	 * 
	 * @param pageData
	 * @param params
	 * @return
	 * @author LS
	 * @date 2013-8-1
	 */
	PageData<UcsSecUserDto> listWidthRolesPage(PageData<UcsSecUserDto> pageData, UcsSecUserDto params);

	/**
	 * 根据名称查询登录账号
	 * 
	 * @param name
	 * @return
	 * @author LS
	 * @date 2013-8-6
	 */
	public UcsSecUserDto getByLoginName(String name);

	/**
	 * 初始化系统管理员登录账号
	 * 
	 * @author LS
	 * @date 2013-8-6
	 */
	public void initUcsSecUserDto();

	/**
	 * 保存基本信息及角色信息
	 * 
	 * @param entity
	 * @author LS
	 * @date 2013-8-3
	 */
	public void saveWithRoles(UcsSecUserDto entity);

	/**
	 * 更新基本信息及角色信息
	 * 
	 * @param entity
	 * @author LS
	 * @date 2013-8-3
	 */
	public void updateWithRoles(UcsSecUserDto entity);

	/**
	 * 检查登录名是否存在, 存在则返回true，否则返回false
	 * 
	 * @param entity
	 * @return
	 * @author LS
	 * @date 2013-8-3
	 */
	boolean checkLoginName(String id, String loginName);

	/**
	 * 检查邮箱是否存在, 存在则返回true，否则返回false
	 * 
	 * @param entity
	 * @return
	 * @author LS
	 * @date 2013-8-3
	 */
	boolean checkEmail(String id, String email);

	/**
	 * 登录失败锁定时间
	 * 
	 * @param id
	 * @return
	 */
	void updateLockTime(String id);

	/**
	 * 更新非空字段
	 * 
	 * @description
	 * @param entity
	 * @author LS
	 * @date 2014-2-15
	 */
	void updateSelective(UcsSecUserDto entity);

	/**
	 * 删除用户
	 * 
	 * @param id
	 */
	void delete(String id);

	/**
	 * 启用用户
	 * 
	 * @param id
	 */
	void enableLoginAcccount(String id);

	/**
	 * 停用用户
	 * 
	 * @param id
	 */
	void disableLoginAcccount(String id);

	/**
	 * 修改密码
	 * 
	 * @description 
	 * @param id
	 * @param password  
	 * @author LS
	 * @date 2014-2-15
	 */
	void changepwd(String id, String password);
	
	/**
	 * 重置密码
	 * 
	 * @param id
	 * @param password
	 */
	void resetpwd(String id, String password);
	
	/**
	 * 获取用户列表
	 * @param params
	 * @return
	 */
	List<UcsSecUserDto> list(UcsSecUserDto params);
	
	/**
	 * 根据部门ID获取用户列表
	 * @param deptId
	 * @return
	 */
	List<UcsSecUserDto> listByDeptId(String deptId);

	/**
	 * 清空登录锁定信息，锁定时间和错误次数
	 */
	void clearLoginFailInfo();
}
