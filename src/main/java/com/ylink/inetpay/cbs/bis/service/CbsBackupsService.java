/**===========================================
 *        Copyright (C) 2016 YLINK
 *           All rights reserved
 *
 *  @项目名： inetpay-cbs
 *  @文件名： CbsBackupsService.java
 *  @版本信息： V1.0.0 
 *  @作者： hinode
 *  @日期： 2016-9-13-上午10:57:42
 * 
 ============================================*/

package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;

/**
 * @类名称： CbsBackupsService
 * @类描述： 
 * @创建人： hinode
 * @创建时间： 2016-9-13 上午10:57:42
 */
public interface CbsBackupsService {
	/**
	 * @方法描述: 备份短信发送记录
	 * @作者： hinode
	 * @日期： 2016-9-12-下午6:44:17
	 * @param date 当前物理时间
	 * @return 
	 * @返回类型： boolean
	 */
	public boolean backupsSms(Date date);
}
