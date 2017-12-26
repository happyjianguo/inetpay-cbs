package com.ylink.inetpay.cbs.bis.scheduler;

import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.project.pay.app.PayOutPayAppService;
import com.ylink.inetpay.common.project.pay.app.PayRefundAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class MerOutPayAduitSchedulerServiceImpl implements SchedulerService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(MerOutPayAduitSchedulerServiceImpl.class);
	
	@Autowired
    PayOutPayAppService payOutPayAppService;
    @Autowired
    PayRefundAppService payRefundAppService;
	

	@Override
	public void execute() {
        try{
            payOutPayAppService.callCBSToAduit();
        }catch(Exception e){
            _log.error("商户对外支付调用cbs生成审核记录失败！" ,e);
        }

        try{
            payRefundAppService.sendCBSToMerRefundAudit();
        }catch(Exception e){
            _log.error("商户退款调用cbs生成审核记录失败！" ,e);
        }


    }

}
