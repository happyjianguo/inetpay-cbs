package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPayOnlineDto;
@MybatisMapper("mrsPayOnlineDtoMapper")
public interface MrsPayOnlineDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsPayOnlineDto record);

    int insertSelective(MrsPayOnlineDto record);

    MrsPayOnlineDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsPayOnlineDto record);

    int updateByPrimaryKey(MrsPayOnlineDto record);
}