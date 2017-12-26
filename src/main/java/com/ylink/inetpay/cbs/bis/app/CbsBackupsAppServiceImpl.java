/**===========================================
 *        Copyright (C) 2016 YLINK
 *           All rights reserved
 *
 *  @项目名： inetpay-cbs
 *  @文件名： CbsBackupsAppServiceImpl.java
 *  @版本信息： V1.0.0 
 *  @作者： hinode
 *  @日期： 2016-9-12-下午6:03:39
 * 
 ============================================*/

package com.ylink.inetpay.cbs.bis.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.bis.service.CbsBackupsService;
import com.ylink.inetpay.cbs.mrs.service.MrsBackupsService;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.project.cbs.app.CbsBackupsAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * @类名称： CbsBackupsAppServiceImpl
 * @类描述：
 * @创建人： hinode
 * @创建时间： 2016-9-12 下午6:03:39
 */
@Service("cbsBackupsAppService")
public class CbsBackupsAppServiceImpl implements CbsBackupsAppService {
	@Autowired
	private CbsBackupsService cbsBackupsService;
	@Autowired
	private MrsBackupsService mrsBackupsService;
	private static Logger logger = LoggerFactory.getLogger(CbsBackupsAppServiceImpl.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ylink.inetpay.common.project.cbs.app.CbsBackupsAppService#backups
	 * (java.util.Date)
	 */
	@Override
	public boolean backups(Date date,Integer days) throws CbsCheckedException {
		String dateStr=DateUtils.changeDateToStr(date);
		String beforeDateStr=DateUtils.getSpecifiedDayBefore(dateStr, days);
		Date beforeDate=DateUtils.changeToDate(beforeDateStr);
		logger.info("当前物理日期：{}，开始对小于：{}日期的数据进行迁移",new Object[]{dateStr,beforeDateStr});
		boolean result1 = mrsBackupsService.backupsOperationLog(beforeDate);
		boolean result2 = cbsBackupsService.backupsSms(beforeDate);
		return result1 && result2;
	}

}
