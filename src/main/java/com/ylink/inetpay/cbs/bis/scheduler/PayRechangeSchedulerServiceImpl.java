package com.ylink.inetpay.cbs.bis.scheduler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.project.pay.app.PayRechargeAppService;

/**
 * 超时未支付的充值订单关闭定时任务
 */
public class PayRechangeSchedulerServiceImpl implements SchedulerService {

    private static final long serialVersionUID = -4205266652191263605L;
    private static Logger _log = LoggerFactory.getLogger(PayRechangeSchedulerServiceImpl.class);

    @Resource(name="payRechargeAppService")
    private PayRechargeAppService payRechargeAppService;


    /**
     * 定时任务执行入口
     */
    @Override
    public void execute() {
        try {
            payRechargeAppService.closeRechangeNotPay();
        } catch (Exception e) {
            _log.error("订单查询失败：" ,e);
        }
    }
}
