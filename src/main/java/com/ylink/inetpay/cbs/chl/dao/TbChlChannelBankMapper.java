package com.ylink.inetpay.cbs.chl.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.channel.dto.TbChlChannelBank;
@MybatisMapper("tbChlChannelBankMapper")
public interface TbChlChannelBankMapper {
   
    int deleteByPrimaryKey(String id);

    int insert(TbChlChannelBank record);

    int insertSelective(TbChlChannelBank record);

    TbChlChannelBank selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TbChlChannelBank record);

    int updateByPrimaryKey(TbChlChannelBank record);
    /** 分页查询**/
    List<TbChlChannelBank> findListPage(TbChlChannelBank record);
}