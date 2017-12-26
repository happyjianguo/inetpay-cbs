package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsChannelAuthDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsChannelAuthDtoKey;
@MybatisMapper("mrsChannelAuthDtoMapper")
public interface MrsChannelAuthDtoMapper {
    int deleteByPrimaryKey(MrsChannelAuthDtoKey key);

    int insert(MrsChannelAuthDto record);

    int insertSelective(MrsChannelAuthDto record);

    MrsChannelAuthDto selectByPrimaryKey(MrsChannelAuthDtoKey key);

    int updateByPrimaryKeySelective(MrsChannelAuthDto record);

    int updateByPrimaryKey(MrsChannelAuthDto record);
}