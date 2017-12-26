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
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.pay.app.PayAccChangeNotifyAppService;

/**
 * @类名称： CATLiquiSchedulerServiceImpl
 * @类描述： 巨灾业务清算
 * @创建人： hinode
 * @创建时间： 2016年11月20日 下午8:19:01
 *
 * @修改人： hinode
 * @操作时间： 2016年11月20日 下午8:19:01
 * @操作原因： 
 *
 */
public class PayOffPayChangeDealSchedulerServiceImpl implements
		SchedulerService {

	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(PayOffPayChangeDealSchedulerServiceImpl.class);
	
	@Autowired
	private PayAccChangeNotifyAppService payAccChangeNotifyAppService;
	
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	
	@Override
	public void execute() {
		try {
			payAccChangeNotifyAppService.dealPayAccChange();
		} catch (Exception e) {
			BisExceptionLogDto bisExceptionLogDto = new BisExceptionLogDto();
			bisExceptionLogDto.setSystem(EBisSmsSystem.CBS);
			bisExceptionLogDto.setType(EBisExceptionLogType.BASE_SERVICE);
			bisExceptionLogDto.setNlevel(EBisExceptionLogNlevel.ERROR);
			bisExceptionLogDto.setContent("线下缴费动账通知处理失败");
			bisExceptionLogDto.setAllpath(CbsConfig.getLogFullPath());
			bisExceptionLogService.saveLog(bisExceptionLogDto);
			_log.error("动账通知处理失败" ,e);
		}
	}

}
