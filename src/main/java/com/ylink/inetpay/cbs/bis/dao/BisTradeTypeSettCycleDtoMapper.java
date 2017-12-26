package com.ylink.inetpay.cbs.bis.dao;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeSettCycleDto;
import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import java.util.List;

@MybatisMapper("bisTradeTypeSettCycleDtoMapper")
public interface BisTradeTypeSettCycleDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisTradeTypeSettCycleDto record);

    int insertSelective(BisTradeTypeSettCycleDto record);

    BisTradeTypeSettCycleDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisTradeTypeSettCycleDto record);

    int updateByPrimaryKey(BisTradeTypeSettCycleDto record);

    List<BisTradeTypeSettCycleDto> listByBusiCode(String busiCode);

    void clearSettleDayAndSetLastSettleDayById(@Param("id") String id, @Param("lastSettleDay")String lastSettleDay);

    void clearFeeSettleDayAndSetLastSettleDayById(@Param("id") String id, @Param("lastSettleDay")String lastSettleDay);

    BisTradeTypeSettCycleDto findByBusiCodeAndTradeTypeCode(@Param("busiCode") String busiCode, @Param("tradeTypeCode") String tradeTypeCode);
    /**
     * 根据产品code获取交易结算dto
     * @param productCode
     * @return
     */
	BisTradeTypeSettCycleDto findByProductCode(String productCode);
}