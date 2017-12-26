package com.ylink.inetpay.cbs.bis.dao;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeParamDto;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

@MybatisMapper("bisTradeTypeParamDtoMapper")
public interface BisTradeTypeParamDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisTradeTypeParamDto record);

    int insertSelective(BisTradeTypeParamDto record);

    BisTradeTypeParamDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisTradeTypeParamDto record);

    int updateByPrimaryKey(BisTradeTypeParamDto record);

	List<BisTradeTypeParamDto> list(BisTradeTypeParamDto queryParam);
	/**
	 * 根据交易类型获取交易类型参数
	 * @param tradeType
	 * @return
	 */
	BisTradeTypeParamDto findByTradeTypeCode(String tradeType);
}