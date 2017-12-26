package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsEmailMapperDto;

@MybatisMapper("mrsEmailMapperDtoMapper")
public interface MrsEmailMapperDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsEmailMapperDto record);

    int insertSelective(MrsEmailMapperDto record);

    MrsEmailMapperDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsEmailMapperDto record);

    int updateByPrimaryKey(MrsEmailMapperDto record);
    
    MrsEmailMapperDto findBySuffix(String suffix);
}