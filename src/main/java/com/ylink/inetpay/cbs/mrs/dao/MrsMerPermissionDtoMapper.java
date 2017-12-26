package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsMerPermissionDto;
@MybatisMapper("mrsMerPermissionDtoMapper")
public interface MrsMerPermissionDtoMapper {
    int insert(MrsMerPermissionDto record);

    int insertSelective(MrsMerPermissionDto record);
    /**
     * 根据服务代码和一户通账号查询服务权限信息数量
     * @param serviceCode
     * @return
     */
	int getServiceByCode(@Param("serviceCode")String serviceCode,@Param("custId")String custId);
	/**
	 * 修改服务权限
	 * @param dto
	 * @return
	 */
	int updateService(MrsMerPermissionDto dto);
	/**
	 * 删除服务权限
	 * @param serviceCode
	 * @return
	 */
	int deleteService(@Param("serviceCode")String serviceCode,@Param("custId")String custId);
	/**
	 * 批量保存服务权限
	 * @param dtos
	 * @return
	 */
	int batchSaveService(@Param("dtos")List<MrsMerPermissionDto> dtos);
}