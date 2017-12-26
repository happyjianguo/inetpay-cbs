package com.ylink.inetpay.cbs.ucs.sec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecDepartmentDto;

@MybatisMapper("ucsSecDepartmentDtoMapper")
public interface UcsSecDepartmentDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UcsSecDepartmentDto record);

    int insertSelective(UcsSecDepartmentDto record);

    UcsSecDepartmentDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UcsSecDepartmentDto record);

    int updateByPrimaryKey(UcsSecDepartmentDto record);
    
    List<UcsSecDepartmentDto> getTopDepts();
    
    List<UcsSecDepartmentDto> getChildrenDepts(String parentId);
    
    List<UcsSecDepartmentDto> getChildrenDeptsByPids(@Param("pids")List<String> pids);
}