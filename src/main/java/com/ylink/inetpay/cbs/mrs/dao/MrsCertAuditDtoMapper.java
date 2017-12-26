package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertAuditDto;

@MybatisMapper("mrsCertAuditDtoMapper")
public interface MrsCertAuditDtoMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(MrsCertAuditDto record);

    int insertSelective(MrsCertAuditDto record);

    MrsCertAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsCertAuditDto record);

    int updateByPrimaryKey(MrsCertAuditDto record);

	List<MrsCertAuditDto> findByCustId(@Param("custId")String custId);
	
	MrsCertAuditDto findOneByCustId(@Param("custId")String custId, @Param("status")String status);
}