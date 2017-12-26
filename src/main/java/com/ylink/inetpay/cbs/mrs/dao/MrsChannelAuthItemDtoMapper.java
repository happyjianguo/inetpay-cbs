package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsChannelAuthItemDto;
@MybatisMapper("mrsChannelAuthItemDtoMapper")
public interface MrsChannelAuthItemDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsChannelAuthItemDto record);

    int insertSelective(MrsChannelAuthItemDto record);

    MrsChannelAuthItemDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsChannelAuthItemDto record);

    int updateByPrimaryKey(MrsChannelAuthItemDto record);
}