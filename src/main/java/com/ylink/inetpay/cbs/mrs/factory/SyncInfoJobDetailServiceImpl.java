package com.ylink.inetpay.cbs.mrs.factory;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.shie.constant.SHIEConfigConstant;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.mrs.service.MrsNotifyService;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.MrsNotifyStatus;
import com.ylink.inetpay.common.core.constant.MrsNotifyType;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsNotifyDto;

/**
 * 信息同步定时任务
 * @author haojiao
 */
public class SyncInfoJobDetailServiceImpl implements SchedulerService{

	private static final long serialVersionUID = 8442351611225256476L;

	private static Logger log = LoggerFactory.getLogger(SyncInfoJobDetailServiceImpl.class);
	
	@Autowired
	private MrsNotifyService mrsNotifyService;
	@Autowired
	private  BisExceptionLogAppService bisExceptionLogAppService;
	@Override
	public void execute() {
		log.info("开始执行信息同步定时任务");
		List<String> statusList = new ArrayList<String>();
		statusList.add(MrsNotifyStatus.NO_NOTIFY.getValue());
		statusList.add(MrsNotifyStatus.NOTIFY_FAIL.getValue());
		Integer maxNotifyNum = SHIEConfigConstant.MAX_NOTIFY_NUM ; 
		List<MrsNotifyDto> notifyList = mrsNotifyService.findByStatusAndMaxNotifyNum(statusList, maxNotifyNum);
		for (MrsNotifyDto dto : notifyList) {
			try{
				String type = dto.getBusiType();
				if(MrsNotifyType.PERSON.getValue().equals(type)) {
					mrsNotifyService.syncPersonInfo(dto);
				} else if(MrsNotifyType.ORGAN.getValue().equals(type)){
					mrsNotifyService.syncOrganInfo(dto);
				} else if(MrsNotifyType.PRODUCT.getValue().equals(type)){
					mrsNotifyService.syncProductInfo(dto);
				} else if(MrsNotifyType.FILE.getValue().equals(type)){
					if(MrsNotifyStatus.NOTIFY_FAIL.getValue().equals(dto.getStatus())){
						mrsNotifyService.syncFileInfo(dto);
					}
				}
			} catch (Exception e) {
				log.error("信息同步失败[id = "+dto.getId()+", custId = "+dto.getCustId()+"]:",e);
				saveErrorExcetpionLog(String.format("信息同步失败,id = %s,custId = %s",
						dto.getId(),dto.getCustId()));
			}
		}
	}
	/**
	 * 记录异常日志
	 * @param errorMsg
	 */
	private void saveErrorExcetpionLog(String errorMsg){
		BisExceptionLogDto	dto=new BisExceptionLogDto();
		dto.setSystem(EBisSmsSystem.CBS);
		dto.setNlevel(EBisExceptionLogNlevel.ERROR);
		dto.setType(EBisExceptionLogType.MRS_SYNC_ECIF);
		dto.setContent(errorMsg);
		dto.setAllpath(CbsConfig.getLogFullPath());
		try {
			bisExceptionLogAppService.saveLog(dto);
		} catch (Exception e) {
			log.error("同步信息到ECIF的系统,记录异常日志失败！");
		}
	}
}
