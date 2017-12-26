package com.ylink.inetpay.cbs.bis.app;

import com.ylink.inetpay.cbs.bis.service.BisTradeTypeSettCycleService;
import com.ylink.inetpay.common.project.cbs.app.BisTradeTypeSettCycleAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeSettCycleDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pst25 on 2017/3/29.
 */
@Service("bisTradeTypeSettCycleAppService")
public class BisTradeTypeSettCycleAppServiceImpl implements BisTradeTypeSettCycleAppService {

    @Autowired
    BisTradeTypeSettCycleService bisTradeTypeSettCycleService;

    @Override
    public BisTradeTypeSettCycleDto findById(String id) throws CbsCheckedException {
        return bisTradeTypeSettCycleService.findById(id);
    }

    @Override
    public List<BisTradeTypeSettCycleDto> listByBusiCode(String busiCode) throws CbsCheckedException {
        return bisTradeTypeSettCycleService.listByBusiCode(busiCode);
    }

    @Override
    public int updateSelective(BisTradeTypeSettCycleDto bisTradeTypeSettCycle) throws CbsCheckedException {
        return bisTradeTypeSettCycleService.updateSelective(bisTradeTypeSettCycle);
    }

    @Override
    public void clearSettleDayAndSetLastSettleDayById(String id, String lastSettleDay) throws CbsCheckedException {
        bisTradeTypeSettCycleService.clearSettleDayAndSetLastSettleDayById(id,lastSettleDay);
    }

    @Override
    public void clearFeeSettleDayAndSetLastSettleDayById(String id, String lastSettleDay) throws CbsCheckedException {
        bisTradeTypeSettCycleService.clearFeeSettleDayAndSetLastSettleDayById(id,lastSettleDay);
    }

    @Override
    public BisTradeTypeSettCycleDto findByBusiCodeAndTradeTypeCode(String busiCode, String tradeTypeCode) throws CbsCheckedException {
        return bisTradeTypeSettCycleService.findByBusiCodeAndTradeTypeCode(busiCode,tradeTypeCode);
    }


}
