package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.BisEcternalPaymentsService;
import com.ylink.inetpay.cbs.bis.service.BisExceptionLogService;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;

public class BisEcternalPaymentsSchedulerServiceImpl implements SchedulerService {

	/**
	 * 自动对外支付（对于批次导入的对外支付记录，通过定时任务执行支付）
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(BisEcternalPaymentsSchedulerServiceImpl.class);
	
	@Autowired
	BisEcternalPaymentsService bisEcternalPaymentsService;
	@Autowired
	BisExceptionLogService bisExceptionLogService;

	@Override
	public void execute() {
		try {
			_log.info("定时任务执行对外支付开始");
			bisEcternalPaymentsService.autoEctPay();
		} catch (Exception e) {
			_log.error("定时执行对外支付失败：" +ExceptionProcUtil.getExceptionDesc(e));
			saveExceptionLog();
		}
	}
	
	private void saveExceptionLog(){
		BisExceptionLogDto bisExceptionLogDto = new BisExceptionLogDto();
		bisExceptionLogDto.setSystem(EBisSmsSystem.CBS);
		bisExceptionLogDto.setType(EBisExceptionLogType.BASE_SERVICE);
		bisExceptionLogDto.setNlevel(EBisExceptionLogNlevel.ERROR);
		bisExceptionLogDto.setContent("定时执行对外支付失败");
		bisExceptionLogDto.setAllpath(CbsConfig.getLogFullPath());
		bisExceptionLogService.saveLog(bisExceptionLogDto);
	}
}
