package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfSubAcctItemDto;
@MybatisMapper("mrsConfSubAcctItemDtoMapper")
public interface MrsConfSubAcctItemDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsConfSubAcctItemDto record);

    int insertSelective(MrsConfSubAcctItemDto record);

    MrsConfSubAcctItemDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsConfSubAcctItemDto record);

    int updateByPrimaryKey(MrsConfSubAcctItemDto record);
}