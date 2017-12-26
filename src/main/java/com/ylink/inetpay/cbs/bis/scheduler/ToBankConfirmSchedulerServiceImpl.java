package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.pay.app.PayRefundAppService;
import com.ylink.inetpay.common.project.pay.app.PayTransferAppService;
import com.ylink.inetpay.common.project.pay.app.PayWithdrawAppService;


public class ToBankConfirmSchedulerServiceImpl implements SchedulerService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(ToBankConfirmSchedulerServiceImpl.class);
	
	@Autowired
	PayWithdrawAppService payWithdrawAppService;
	
	@Autowired
	PayTransferAppService payTransferAppService;
	
	@Autowired
	PayRefundAppService payRefundAppService;

	@Override
	public void execute() {
        _log.info("执行到提现/转账/退款到中间户的定时任务");
		try {
			payWithdrawAppService.withdrawConfirm();

		} catch (Exception e) {
			_log.error("调用提现到中间户失败：" +ExceptionProcUtil.getExceptionDesc(e));
		}
        try{
            payTransferAppService.transferToBankConfirm();

        }catch(Exception e){
            _log.error("调用转账到中间户失败：" +ExceptionProcUtil.getExceptionDesc(e));
        }
        try{
            payRefundAppService.refundToBankConfirm();

        }catch(Exception e){
            _log.error("退款到中间户失败：" +ExceptionProcUtil.getExceptionDesc(e));
        }


	}

}
