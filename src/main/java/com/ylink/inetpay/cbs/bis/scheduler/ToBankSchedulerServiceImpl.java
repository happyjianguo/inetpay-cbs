package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.pay.app.PayRefundAppService;
import com.ylink.inetpay.common.project.pay.app.PayTransferAppService;
import com.ylink.inetpay.common.project.pay.app.PayWithdrawAppService;


public class ToBankSchedulerServiceImpl implements SchedulerService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(ToBankSchedulerServiceImpl.class);
	
	@Autowired
	PayWithdrawAppService payWithdrawAppService;
	
	@Autowired
	PayTransferAppService payTransferAppService;
	
	@Autowired
	PayRefundAppService payRefundAppService;

	@Override
	public void execute() {
		try {
			payWithdrawAppService.withdrawToBank();

		} catch (Exception e) {
			_log.error("调用提现银行失败：" +ExceptionProcUtil.getExceptionDesc(e));
		}
        try{
            payTransferAppService.transferToBank();

        }catch(Exception e){
            _log.error("调用转账到银行失败：" +ExceptionProcUtil.getExceptionDesc(e));
        }
        try{
            payRefundAppService.refundToBank();

        }catch(Exception e){
            _log.error("调用退款到银行失败：" +ExceptionProcUtil.getExceptionDesc(e));
        }
	}

}
