package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.BisExceptionLogService;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.app.BisAccountFrozenAuditAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;

public class BisFrozenUnfreezeAccountSchedulerServiceImpl implements SchedulerService {

	/**
	 * 冻结解冻
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(BisFrozenUnfreezeAccountSchedulerServiceImpl.class);
	
	@Autowired
	BisAccountFrozenAuditAppService accountFrozenAuditAppService;
	@Autowired
	BisExceptionLogService bisExceptionLogService;

	@Override
	public void execute() {
		try {
			_log.info("定时任务执行冻结解冻资金开始");
			accountFrozenAuditAppService.taskFrozeAccount();
			accountFrozenAuditAppService.taskUnfreezeAccount();
		} catch (Exception e) {
			_log.error("冻结解冻资金失败：" +ExceptionProcUtil.getExceptionDesc(e));
			saveExceptionLog();
		}
	}
	
	private void saveExceptionLog(){
		BisExceptionLogDto bisExceptionLogDto = new BisExceptionLogDto();
		bisExceptionLogDto.setSystem(EBisSmsSystem.CBS);
		bisExceptionLogDto.setType(EBisExceptionLogType.BASE_SERVICE);
		bisExceptionLogDto.setNlevel(EBisExceptionLogNlevel.ERROR);
		bisExceptionLogDto.setContent("定时执行冻结解冻资金失败");
		bisExceptionLogDto.setAllpath(CbsConfig.getLogFullPath());
		bisExceptionLogService.saveLog(bisExceptionLogDto);
	}
}
