package com.ylink.inetpay.cbs.pay.dao;


import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayNoticeDto;
@MybatisMapper("payNoticeDtoMapper")
public interface PayNoticeDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayNoticeDto record);

    int insertSelective(PayNoticeDto record);

    PayNoticeDto selectByPrimaryKey(String id);
    
    PayNoticeDto selectBybusiId(String busiId);

    int updateByPrimaryKeySelective(PayNoticeDto record);

    int updateByPrimaryKey(PayNoticeDto record);
}