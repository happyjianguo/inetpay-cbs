package com.ylink.inetpay.cbs.ucs.sec.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecPermissionDto;

@MybatisMapper("ucsSecPermissionDtoMapper")
public interface UcsSecPermissionDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UcsSecPermissionDto record);

    int insertSelective(UcsSecPermissionDto record);

    UcsSecPermissionDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UcsSecPermissionDto record);

    int updateByPrimaryKey(UcsSecPermissionDto record);

    /**
     * 列出所有权限
     *
     * @return
     */
    Set<UcsSecPermissionDto> listAll();

    /**
     * 根据角色表示获取权限
     *
     * @param roleTag
     * @return
     */
    Set<UcsSecPermissionDto> listByRoleTag(String roleTag);

    /**
     * 根据级别列出权限信息
     *
     * @param nlevel
     * @return
     * @description
     * @author LS
     * @date 2013-9-9
     */
    List<UcsSecPermissionDto> listPerms(@Param("nlevel") String nlevel,
                                        @Param("length") int length);

    /**
     * 根据角色列表获取权限信息
     *
     * @param roles
     * @return
     */
    Set<String> getPermsByRoles(@Param("roleIdList") Set<String> roles);

    /**
     * 获取角色列表的权限集合
     *
     * @param roles
     * @param nlevel
     * @param length
     * @return
     */
    List<UcsSecPermissionDto> getPermDtosByRoles(
            @Param("roleIdList") Set<String> roles,
            @Param("nlevel") String nlevel, @Param("length") int length);

    /**
     * 获取顶级菜单
     *
     * @return
     */
    List<UcsSecPermissionDto> getUserTopMenus(String userId);

    /**
     * 获取外部系统
     *
     * @return
     */
    List<UcsSecPermissionDto> getOuterTopMenus();

}