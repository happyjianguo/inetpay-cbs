package com.ylink.inetpay.cbs.bis.service;

import com.ylink.inetpay.cbs.bis.dao.BisTradeTypeSettCycleDtoMapper;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeSettCycleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pst25 on 2017/3/29.
 */
@Service("bisTradeTypeSettCycleService")
public class BisTradeTypeSettCycleServiceImpl implements BisTradeTypeSettCycleService {

    @Autowired
    BisTradeTypeSettCycleDtoMapper bisTradeTypeSettCycleDtoMapper;

    @Override
    public BisTradeTypeSettCycleDto findById(String id) {
        return bisTradeTypeSettCycleDtoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateSelective(BisTradeTypeSettCycleDto bisTradeTypeSettCycle) {
        return bisTradeTypeSettCycleDtoMapper.updateByPrimaryKeySelective(bisTradeTypeSettCycle);
    }

    @Override
    public List<BisTradeTypeSettCycleDto> listByBusiCode(String busiCode) {
        return bisTradeTypeSettCycleDtoMapper.listByBusiCode(busiCode);
    }

    @Override
    public void clearSettleDayAndSetLastSettleDayById(String id, String lastSettleDay) {
        bisTradeTypeSettCycleDtoMapper.clearSettleDayAndSetLastSettleDayById(id,lastSettleDay);
    }

    @Override
    public void clearFeeSettleDayAndSetLastSettleDayById(String id, String lastSettleDay) {
        bisTradeTypeSettCycleDtoMapper.clearFeeSettleDayAndSetLastSettleDayById(id,lastSettleDay);
    }

    @Override
    public BisTradeTypeSettCycleDto findByBusiCodeAndTradeTypeCode(String busiCode, String tradeTypeCode) {
        return bisTradeTypeSettCycleDtoMapper.findByBusiCodeAndTradeTypeCode(busiCode,tradeTypeCode);
    }

	@Override
	public List<BisTradeTypeSettCycleDto> findProductByBusinessCode(String businessCode) {
		return bisTradeTypeSettCycleDtoMapper.listByBusiCode(businessCode);
	}
}
