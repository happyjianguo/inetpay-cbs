/**===========================================
 *        Copyright (C) 2016 YLINK
 *           All rights reserved
 *
 *  @项目名： inetpay-clear
 *  @文件名： NoticeUtil.java
 *  @版本信息： V1.0.0 
 *  @作者： hinode
 *  @日期： 2016-7-6-下午9:35:01
 * 
 ============================================*/

package com.ylink.inetpay.cbs.chl.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.EmessType;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.app.BisSmsAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * @类名称： NoticeUtil
 * @类描述：
 * @创建人： hinode
 * @创建时间： 2016-7-6 下午9:35:01
 */
@Service("noticeService")
public class NoticeServiceImpl {
	protected static final Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);

	@Autowired
	private  BisSmsAppService smsAppService;
	@Autowired
	@Qualifier("smsTaskExecutor")
	private  ThreadPoolTaskExecutor smsTaskExecutor;
	@Autowired
	private  BisExceptionLogAppService bisExceptionLogAppService;

 
	/**
	 * @方法描述:   发送异常消息给运维人员
	 * @作者： 1603254
	 * @日期： 2016-7-18-下午8:23:14
	 * @param level  异常日志异常等级
	 * @param model  异常日志类型
	 * @param errorMsg 异常日志信息
	 * @param tranNo   交易号
	 * @返回类型： void
	*/
	public  void sendErrorMsg(final EBisExceptionLogNlevel level,final EBisExceptionLogType model,final String errorMsg,final String tranNo) {
		smsTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info(model.getDisplayName() + "异常消息发送开始");
				List<String> list = new ArrayList<String>();
				list.add(errorMsg);
				try {
					logger.info("给运维人员发短信");
					smsAppService.sendSms(null, EBisSmsSystem.CBS, list, EBisTemplateCode.CBS_CRON,
							EmessType.MESSAGE_NOTIFICATION);
					logger.info("记录异常日志");
					bisExceptionLogAppService.saveLog(createExcetpion(level,model,errorMsg,tranNo));
					logger.info(model.getDisplayName() + "异常消息发送结束");
				} catch (CbsCheckedException e) {
					logger.error("调用公共服务异常", e);
				} catch (Exception e) {
					logger.error("异常消息通知异常", e);
				}
			}
		});
	}
	
	private  BisExceptionLogDto createExcetpion(EBisExceptionLogNlevel level,EBisExceptionLogType model,String errorMsg,final String tranNo){
		BisExceptionLogDto	dto=new BisExceptionLogDto();
		dto.setSystem(EBisSmsSystem.CBS);
		dto.setNlevel(level);
		dto.setType(model);
		dto.setContent(errorMsg);
		dto.setAllpath(CbsConfig.getLogFullPath());
		dto.setTranNo(tranNo);
		return dto;
	}
}
