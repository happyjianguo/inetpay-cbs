package com.ylink.inetpay.cbs.bis.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisMsgNotificationService;
import com.ylink.inetpay.common.project.cbs.app.BisMsgNotificationAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisMessageNotificationDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisMsgNotificationAppService")
public class BisMsgNotificationServiceImpl implements BisMsgNotificationAppService {

	@Autowired
	private BisMsgNotificationService bisMsgNotificationService;
	@Autowired
	@Qualifier("logTaskExecutor")
	private TaskExecutor taskExecutor;
	@Override
	public PageData<BisMessageNotificationDto> findListPage(PageData<BisMessageNotificationDto> pageDate,
			BisMessageNotificationDto bisMessageNotificationDto) throws CbsCheckedException {
		return bisMsgNotificationService.findListPage(pageDate, bisMessageNotificationDto);
	}

	@Override
	public void saveNotification(BisMessageNotificationDto bisMessageNotificationDto) throws CbsCheckedException {
		taskExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				bisMsgNotificationService.saveNotification(bisMessageNotificationDto);
			}
		});
	}

	@Override
	public BisMessageNotificationDto details(String id) throws CbsCheckedException {
		return bisMsgNotificationService.details(id);
	}

	

}
