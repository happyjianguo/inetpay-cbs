package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayBwlistDto;
@MybatisMapper("payBwlistDtoMapper")
public interface PayBwlistDtoMapper {
   // int deleteByPrimaryKey(String id);

   // int insert(PayBwlistDto record);

    //int insertSelective(PayBwlistDto record);
	//管理平台使用查询口，不影响交易，不加缓存
    PayBwlistDto selectByPrimaryKey(String id);

   // int updateByPrimaryKeySelective(PayBwlistDto record);

   // int updateByPrimaryKey(PayBwlistDto record);
    //管理平台使用查询口，不影响交易，不加缓存
	List<PayBwlistDto> list(PayBwlistDto payBwlistDto);
}