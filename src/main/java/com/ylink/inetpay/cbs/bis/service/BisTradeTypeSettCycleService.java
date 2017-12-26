package com.ylink.inetpay.cbs.bis.service;

import java.util.List;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeSettCycleDto;

/**
 * Created by pst25 on 2017/3/29.
 */
public interface BisTradeTypeSettCycleService {

    BisTradeTypeSettCycleDto findById(String id);

    int updateSelective(BisTradeTypeSettCycleDto bisTradeTypeSettCycle);

    List<BisTradeTypeSettCycleDto> listByBusiCode(String busiCode);

    void clearSettleDayAndSetLastSettleDayById(String id, String lastSettleDay);

    void clearFeeSettleDayAndSetLastSettleDayById(String id, String lastSettleDay);

    BisTradeTypeSettCycleDto findByBusiCodeAndTradeTypeCode(String busiCode, String tradeTypeCode);
    /**
     * 根据业务代码获取结算参数
     * @param businessCode
     * @return
     */
	List<BisTradeTypeSettCycleDto> findProductByBusinessCode(String businessCode);
}
