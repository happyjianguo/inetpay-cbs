package com.ylink.inetpay.cbs.ucs.sec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.ENormalDisabledStatus;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecRoleDto;

@MybatisMapper("ucsSecRoleDtoMapper")
public interface UcsSecRoleDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UcsSecRoleDto record);

    int insertSelective(UcsSecRoleDto record);

    UcsSecRoleDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UcsSecRoleDto record);

    int updateByPrimaryKey(UcsSecRoleDto record);
    
    /**
	 * 删除角色与权限的关联
	 * 
	 * @description 
	 * @param id  
	 * @author LS
	 * @date 2014-2-14
	 */
	void deletePermsByRoleId(String id);


	/**
	 * 保存角色（自定义ID）
	 * 
	 * @param entity
	 */
	void saveWithId(UcsSecRoleDto entity);
	
	/**
	 * 获取角色及其关联权限
	 * 
	 * @param roleId
	 * @author LS
	 * @date 2013-7-30
	 */
	public UcsSecRoleDto getWidthPermissions(String roleId);

	/**
	 * 给角色批量添加权限
	 * 
	 * @param roleId
	 * @param permIdList
	 */
	void addRolePermissions(@Param("roleId") String roleId,
			@Param("permIdList") List<String> permIdList);

	/**
	 * 查询角色列表
	 * 
	 * @return
	 * @author LS
	 * @date 2013-8-3
	 */
	public List<UcsSecRoleDto> listRoles(UcsSecRoleDto securityRoleDto);

	/**
	 * 根据角色ID获取用户ID列表
	 * 
	 * @description
	 * @param roleId
	 * @return
	 * @author LS
	 * @date 2013-9-5
	 */
	public List<String> getUserList(String roleId);
	/**
	 * 判断角色名称是否存在
	 * @param roleName
	 * @return
	 */
	List<UcsSecRoleDto> isExistRoleName(@Param("roleName")String roleName,@Param("status")ENormalDisabledStatus status);
}