package com.ylink.inetpay.cbs.ucs.sec.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecOperationLogDto;

@MybatisMapper("ucsSecOperationLogDtoMapper")
public interface UcsSecOperationLogDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(UcsSecOperationLogDto record);

    int insertSelective(UcsSecOperationLogDto record);

    UcsSecOperationLogDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UcsSecOperationLogDto record);

    int updateByPrimaryKey(UcsSecOperationLogDto record);
    
    List<UcsSecOperationLogDto> list(UcsSecOperationLogDto ucsSecOperationLogDto);
}