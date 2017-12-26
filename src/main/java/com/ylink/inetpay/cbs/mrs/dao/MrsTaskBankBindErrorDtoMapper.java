package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsTaskBankBindErrorDto;
@MybatisMapper("mrsTaskBankBindErrorDtoMapper")
public interface MrsTaskBankBindErrorDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsTaskBankBindErrorDto record);

    int insertSelective(MrsTaskBankBindErrorDto record);

    MrsTaskBankBindErrorDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsTaskBankBindErrorDto record);

    int updateByPrimaryKey(MrsTaskBankBindErrorDto record);
}