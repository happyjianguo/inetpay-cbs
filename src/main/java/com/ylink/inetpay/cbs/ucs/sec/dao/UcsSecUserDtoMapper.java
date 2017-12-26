package com.ylink.inetpay.cbs.ucs.sec.dao;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EUcsSecUserStatus;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;

@MybatisMapper("ucsSecUserDtoMapper")
public interface UcsSecUserDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UcsSecUserDto record);

    int insertSelective(UcsSecUserDto record);

    UcsSecUserDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UcsSecUserDto record);

    int updateByPrimaryKey(UcsSecUserDto record);
    
    /**
	 * 通过登录名获取账户对象
	 * 
	 * @param loginName
	 * @return
	 * @author LS
	 * @date 2013-8-1
	 */
    UcsSecUserDto getByLoginName(String loginName);

	/**
	 * 检查登录名是否存在
	 * 
	 * @param id
	 * @param loginName
	 * @return
	 * @author LS
	 * @date 2013-8-6
	 */
	int checkLoginName(@Param("id") String id,@Param("loginName") String loginName);

	/**
	 * 检查邮箱是否存在
	 * 
	 * @param id
	 * @param email
	 * @return
	 * @author LS
	 * @date 2013-8-6
	 */
	int checkEmail(@Param("id") String id, @Param("email") String email);

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
	 * @param entity
	 * @return
	 * @author LS
	 * @date 2013-8-1
	 */
	List<UcsSecUserDto> listWidthRoles(@Param("queryParam")UcsSecUserDto queryParam);

	/**
	 * 分页
	 * 
	 * @param queryParam
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	List<UcsSecUserDto> listWidthRolesPage(@Param("queryParam")UcsSecUserDto queryParam , 
			@Param("beginIndex")int beginIndex,@Param("endIndex")int endIndex);
	
	/**
	 * 分页总条数
	 * 
	 * @param queryParam
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	long listWidthRolesPageCount(@Param("queryParam")UcsSecUserDto queryParam);

	/**
	 * 批量添加用户角色信息
	 * @param loginAccountId
	 * @param roleIdList
	 */
	void addUserRoles(@Param("userId")String userId, @Param("roleIdList")List<String> roleIdList);

	/**
	 * 删除用户所有角色
	 * 
	 * @param loginAccountId
	 * @author LS
	 * @date 2013-7-29
	 */
	void deleteUserRoles(@Param("userId")String userId);

	/**
	 * 最后一次登录失败锁定时间
	 * 
	 * @param loginAccount
	 * @author LS
	 * @date 2013-7-30
	 */
	void updateLockTime(@Param("userId") String userId, @Param("lockDate") Date lockDate);

	/**
	 * 更新用户状态
	 * 
	 * @param loginAccountId
	 * @param status
	 */
	void updateStatus(@Param("userId") String userId,	@Param("status")EUcsSecUserStatus status);
	
	/**
	 * 根据部门ID获取用户列表
	 * 
	 * @param deptId
	 * @return
	 */
	public List<UcsSecUserDto> listByDeptId(String deptId);
	/**
	 * 获取所有状态正常的管理员
	 * @param EUcsSecUserStatus
	 * @return
	 */
	public List<UcsSecUserDto> allNomalUser(@Param("status")EUcsSecUserStatus nomal);
	
	/**
	 * 清空登录锁定信息，锁定时间和错误次数
	 */
	void clearLoginFailInfo();
}