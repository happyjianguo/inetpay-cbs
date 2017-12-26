package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayRedPDto;

@MybatisMapper("payRedPDtoMapper")
public interface PayRedPDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(PayRedPDto record);

	int insertSelective(PayRedPDto record);

	PayRedPDto selectByPrimaryKey(String id);
	/**
	 * 红包发放列表
	 * @param queryparam
	 * @return
	 */
	List<PayRedPDto> list(PayRedPDto queryparam);
	
}