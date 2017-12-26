package com.ylink.inetpay.cbs.bis.dao;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisProductDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisProductTradeTypePojoDto;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

@MybatisMapper("bisProductDtoMapper")
public interface BisProductDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisProductDto record);

    int insertSelective(BisProductDto record);

    BisProductDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisProductDto record);

    int updateByPrimaryKey(BisProductDto record);
    /**
     * 获取产品列表
     * @param queryParam
     * @return
     */
	List<BisProductTradeTypePojoDto> list(BisProductTradeTypePojoDto queryParam);
	/**
	 * 根据主键id获取产品对象
	 * @param id
	 * @return
	 */
	BisProductTradeTypePojoDto findProductTradeById(String id);
	/**
	 * 根据产品code获取产品信息
	 * @param productCode
	 * @return
	 */
	BisProductDto findByProductCode(String productCode);
}