package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ylink.inetpay.cbs.bis.service.SchedulerManagerImpl;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsHistoryOperationLogMapper;

@Service("mrsBackupsService")
public class MrsBackupsServiceImpl implements MrsBackupsService {

	@Autowired
	private MrsHistoryOperationLogMapper operationLogMapper;

	private static Logger logger = LoggerFactory.getLogger(SchedulerManagerImpl.class);

	/**
	 * @方法描述: 备份用户操作日志
	 * @作者： hinode
	 * @日期： 2016-9-12-下午6:44:17
	 * @param date 当前物理时间
	 * @return 
	 * @返回类型： boolean
	 */
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public boolean backupsOperationLog(Date date){
		logger.info("开始备份用户操作日志");
		int add=operationLogMapper.insert(date);
		int delete=operationLogMapper.delete(date);
		logger.info("历史表添加记录条数：{}，原表删除记录条数：{}",new Object[]{add,delete});
		return true;
	}
}
