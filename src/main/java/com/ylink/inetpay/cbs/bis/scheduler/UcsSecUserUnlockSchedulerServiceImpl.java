package com.ylink.inetpay.cbs.bis.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.service.BisExceptionLogService;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserService;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;

/**
 * 统一运营系统用户解锁处理
 * @author LS
 *
 */
public class UcsSecUserUnlockSchedulerServiceImpl implements
		SchedulerService {

	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserUnlockSchedulerServiceImpl.class);
	
	@Autowired
	UcsSecUserService ucsSecUserService;
	
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	
	@Override
	public void execute() {
		try {
			ucsSecUserService.clearLoginFailInfo();
		} catch (Exception e) {
			BisExceptionLogDto bisExceptionLogDto = new BisExceptionLogDto();
			bisExceptionLogDto.setSystem(EBisSmsSystem.CBS);
			bisExceptionLogDto.setType(EBisExceptionLogType.BASE_SERVICE);
			bisExceptionLogDto.setNlevel(EBisExceptionLogNlevel.ERROR);
			bisExceptionLogDto.setContent("统一运营系统用户解锁处理失败：" +e.getMessage());
			bisExceptionLogDto.setAllpath(CbsConfig.getLogFullPath());
			bisExceptionLogService.saveLog(bisExceptionLogDto);
			_log.error("统一运营系统用户解锁处理失败：" + ExceptionProcUtil.getExceptionDesc(e));
		}
	}

}
