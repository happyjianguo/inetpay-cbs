package com.ylink.inetpay.cbs.bis.dao;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAcctTypeDto;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

@MybatisMapper("bisAcctTypeDtoMapper")
public interface BisAcctTypeDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisAcctTypeDto record);

    int insertSelective(BisAcctTypeDto record);

    BisAcctTypeDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisAcctTypeDto record);

    int updateByPrimaryKey(BisAcctTypeDto record);
}