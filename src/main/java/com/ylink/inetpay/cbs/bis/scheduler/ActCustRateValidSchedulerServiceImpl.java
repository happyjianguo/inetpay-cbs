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
 * @author LS
 * 客户利率生效
 */
public class ActCustRateValidSchedulerServiceImpl implements
		SchedulerService {

	private static final long serialVersionUID = 1L;
	
	private static Logger _log = LoggerFactory.getLogger(ActCustRateValidSchedulerServiceImpl.class);
	
	@Autowired
	private BisActCustRateAuditService bisActCustRateAuditService;
	
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	
	@Autowired
	ActCustRateAppService actCustRateAppService;
	@Autowired
	ActAccountDateAppService actAccountDateAppService;
	
	@Override
	public void execute() {
		try {
			//每天凌晨执行一次
			_log.info("定时任务执客户利率生效开始");
			String accountDate = actAccountDateAppService.getAccountDate();
			List<BisActCustRateAuditDto> items = bisActCustRateAuditService.findPassAndValidTime(DateUtils.changeToDate(accountDate));
			if(items != null && !items.isEmpty()){
				ActCustRateDto actCustRateDto = null;
				for(BisActCustRateAuditDto item : items){
					actCustRateDto = new ActCustRateDto();
					BeanUtils.copyProperties(item, actCustRateDto, new String[]{"id"} );
					try {
						actCustRateDto.setId(item.getRefId());
						actCustRateDto.setStatus(EActCustRateStatus.EFFECTIVE);
						actCustRateAppService.save(actCustRateDto);
						item.setStatus(EActCustRateStatus.EFFECTIVE);
						bisActCustRateAuditService.updateSelective(item);
					} catch (Exception e) {
						saveExceptionLog();
						_log.error("定时执行利息生效同步失败：" + ExceptionProcUtil.getExceptionDesc(e));
					}
				}
			}
		} catch (Exception e) {
			saveExceptionLog();
			_log.error("定时执行利息生效同步失败：" + ExceptionProcUtil.getExceptionDesc(e));
		}
	}
	
	private void saveExceptionLog(){
		BisExceptionLogDto bisExceptionLogDto = new BisExceptionLogDto();
		bisExceptionLogDto.setSystem(EBisSmsSystem.CBS);
		bisExceptionLogDto.setType(EBisExceptionLogType.BASE_SERVICE);
		bisExceptionLogDto.setNlevel(EBisExceptionLogNlevel.ERROR);
		bisExceptionLogDto.setContent("定时执行利息生效同步失败");
		bisExceptionLogDto.setAllpath(CbsConfig.getLogFullPath());
		bisExceptionLogService.saveLog(bisExceptionLogDto);
	}

}
