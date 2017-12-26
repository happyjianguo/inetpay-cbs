package com.ylink.inetpay.cbs.bis.scheduler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.inetpay.cbs.bis.cache.BisSysParamDtoCache;
import com.ylink.inetpay.cbs.bis.service.BisExceptionLogService;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.project.account.app.ActCustInterestAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;
import com.ylink.inetpay.common.project.channel.app.ChlChannelAppService;
import com.ylink.inetpay.common.project.clear.app.ClearCATAppService;
import com.ylink.inetpay.common.project.pay.app.PayPaymentAppService;

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
public class ClsFlushJobSchedulerServiceImpl implements
		SchedulerService {

	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(ClsFlushJobSchedulerServiceImpl.class);
	
	@Autowired
	private ClearCATAppService clearCATAppService;
	
	@Autowired
	private BisExceptionLogService bisExceptionLogService;

	@Autowired
	private ChlChannelAppService chlChannelAppService;

	@Autowired
	private BisSysParamDtoCache bisSysParamDtoCache;
    @Autowired
    private PayPaymentAppService payPaymentAppService;
    @Autowired
    private ActCustInterestAppService actCustInterestAppService;

	@Override
	public void execute() {
		try {
			BisSysParamDto sysParam = bisSysParamDtoCache.selectByKey(SystemParamConstants.UNLOCK_PRO_TIME);
			if(sysParam == null || StringUtils.isBlank(sysParam.getValue())){
				_log.error("未配置解锁处理中的间隔时间");
				return;
			}
			clearCATAppService.flashStatus();
			chlChannelAppService.updateProcessingJob(Integer.valueOf(sysParam.getValue()));
            payPaymentAppService.flashStatus();
            actCustInterestAppService.flashStatus();
            
		} catch (Exception e) {
			BisExceptionLogDto bisExceptionLogDto = new BisExceptionLogDto();
			bisExceptionLogDto.setSystem(EBisSmsSystem.CBS);
			bisExceptionLogDto.setType(EBisExceptionLogType.BASE_SERVICE);
			bisExceptionLogDto.setNlevel(EBisExceptionLogNlevel.ERROR);
			bisExceptionLogDto.setContent("解锁处理中的任务失败："+e.getMessage());
			bisExceptionLogDto.setAllpath(CbsConfig.getLogFullPath());
			bisExceptionLogService.saveLog(bisExceptionLogDto);
			_log.error("解锁处理中的任务失败：",e);
		}
	}

}
