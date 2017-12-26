package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;

public interface MrsBackupsService {
	/**
	 * @方法描述: 备份用户操作日志
	 * @作者： hinode
	 * @日期： 2016-9-12-下午6:44:17
	 * @param date 当前物理时间
	 * @return 
	 * @返回类型： boolean
	 */
	public boolean backupsOperationLog(Date date);
}
