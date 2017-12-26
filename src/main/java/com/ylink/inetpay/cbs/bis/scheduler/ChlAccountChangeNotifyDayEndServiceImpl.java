package com.ylink.inetpay.cbs.bis.scheduler;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ylink.eu.util.tools.DateUtil;
import com.ylink.eu.util.tools.enums.DateEnum;
import com.ylink.inetpay.cbs.bis.service.SchedulerService;
import com.ylink.inetpay.common.project.channel.app.ChlAccountChangeNotifyAppService;

/**
 * 动账通知查询（历史）
 * 
 * @author lhui
 *
 */
public class ChlAccountChangeNotifyDayEndServiceImpl implements SchedulerService {

	private static final long serialVersionUID = 1L;

	private static Logger _log = LoggerFactory.getLogger(ChlAccountChangeNotifyDayEndServiceImpl.class);

	@Autowired
	ChlAccountChangeNotifyAppService chlAccountChangeNotifyAppService;

	@Override
	public void execute() {
		_log.info("开始进行动账通知查询（历史）.");
		Date date = DateUtil.getBeforeDaysDate(1);
		String startDate = DateUtil.format(date, DateEnum.YYYYMMDD$);
		String endDate = startDate;
		chlAccountChangeNotifyAppService.accChgNotifyProcess(startDate, endDate);
		_log.info("动账通知查询结束（历史）.");
	}
}
