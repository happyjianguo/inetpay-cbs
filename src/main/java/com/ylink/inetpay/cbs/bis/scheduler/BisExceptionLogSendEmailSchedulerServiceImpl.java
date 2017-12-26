package com.ylink.inetpay.cbs.bis.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.BisActCustRateAuditService;
import com.ylink.inetpay.cbs.bis.service.BisExceptionLogService;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.common.core.constant.EActCustRateStatus;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.account.app.ActAccountDateAppService;
import com.ylink.inetpay.common.project.account.app.ActCustRateAppService;
import com.ylink.inetpay.common.project.account.dto.ActCustRateDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActCustRateAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;

/**
 * 异常监控日志发送邮件
 * @author pst18
 *
 */
public class BisExceptionLogSendEmailSchedulerServiceImpl implements
		SchedulerService {

	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(BisExceptionLogSendEmailSchedulerServiceImpl.class);
	
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	@Override
	public void execute() {
		try {
			_log.info("异常日志发送邮件开始");
			bisExceptionLogService.sendExceptionLog();
		} catch (Exception e) {
			saveExceptionLog();
			_log.error("异常日志发送邮件失败：" + ExceptionProcUtil.getExceptionDesc(e));
		}
	}
	
	private void saveExceptionLog(){
		BisExceptionLogDto bisExceptionLogDto = new BisExceptionLogDto();
		bisExceptionLogDto.setSystem(EBisSmsSystem.CBS);
		bisExceptionLogDto.setType(EBisExceptionLogType.BASE_SERVICE);
		bisExceptionLogDto.setNlevel(EBisExceptionLogNlevel.ERROR);
		bisExceptionLogDto.setContent("异常日志发送邮件失败");
		bisExceptionLogDto.setAllpath(CbsConfig.getLogFullPath());
		bisExceptionLogService.saveLog(bisExceptionLogDto);
	}

}
