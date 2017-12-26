/**===========================================
 *        Copyright (C) 2016 YLINK
 *           All rights reserved
 *
 *  @项目名： inetpay-cbs
 *  @文件名： CbsBackupsServiceImpl.java
 *  @版本信息： V1.0.0 
 *  @作者： hinode
 *  @日期： 2016-9-13-上午11:01:18
 * 
 ============================================*/

package com.ylink.inetpay.cbs.bis.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.bis.dao.BisHistorySmsMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;

/**
 * @类名称： CbsBackupsServiceImpl
 * @类描述：
 * @创建人： hinode
 * @创建时间： 2016-9-13 上午11:01:18
 */
@Service("cbsBackupsService")
public class CbsBackupsServiceImpl implements CbsBackupsService {
 
	@Autowired
	private BisHistorySmsMapper historySmsMapper;

	private static Logger logger = LoggerFactory.getLogger(CbsBackupsServiceImpl.class);

	/**
	 * @方法描述: 备份短信发送记录
	 * @作者： hinode
	 * @日期： 2016-9-12-下午6:44:17
	 * @param date 当前物理时间
	 * @return 
	 * @返回类型： boolean
	 */
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_BIS)
	public boolean backupsSms(Date date){
		logger.info("开始备份短信发送记录");
		int add=historySmsMapper.insert(date);
		int delete=historySmsMapper.delete(date);
		logger.info("历史表添加记录条数：{}，原表删除记录条数：{}",new Object[]{add,delete});
		return true;
	}
}
