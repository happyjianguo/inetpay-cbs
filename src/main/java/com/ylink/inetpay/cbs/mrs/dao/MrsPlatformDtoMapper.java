package com.ylink.inetpay.cbs.mrs.dao;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;

@MybatisMapper("mrsPlatformDtoMapper")
public interface MrsPlatformDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsPlatformDto record);

    int insertSelective(MrsPlatformDto record);

    MrsPlatformDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsPlatformDto record);

    int updateByPrimaryKey(MrsPlatformDto record);
    
    /**
     * 根据接入平台编号和状态查询接入平台信息
     * @param code
     * @return
     */
    MrsPlatformDto findByPlatformCode(@Param("platformCode")String platformCode, @Param("status")String status);
}