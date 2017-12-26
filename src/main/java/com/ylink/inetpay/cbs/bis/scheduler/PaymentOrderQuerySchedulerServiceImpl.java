package com.ylink.inetpay.cbs.bis.scheduler;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.project.pay.app.PayOutPayAppService;
import com.ylink.inetpay.common.project.pay.app.PayPaymentAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * 交易中订单查询接口
 * Created by pst12 on 2016/11/20.
 */
public class PaymentOrderQuerySchedulerServiceImpl implements SchedulerService {
    private static final long serialVersionUID = 5330254339141944958L;

    private static Logger _log = LoggerFactory.getLogger(PaymentOrderQuerySchedulerServiceImpl.class);

    @Resource(name="payPaymentAppService")
    private PayPaymentAppService payPaymentAppService;

    @Autowired
    private PayOutPayAppService payOutPayAppService;

    /**
     * 定时任务执行入口
     */
    @Override
    public void execute() {
        try {
            payPaymentAppService.paymentOrderQueryTask();
        } catch (Exception e) {
            _log.error("订单查询失败：" ,e);
        }

        try{
            _log.debug("对外支付订单查询开始。。");
            payOutPayAppService.queryCashTransferByTask();
            _log.debug("对外支付订单查询结束。。");
        }catch(Exception e){
            _log.error("对外支付订单查询失败：" ,e);
        }
    }
}
