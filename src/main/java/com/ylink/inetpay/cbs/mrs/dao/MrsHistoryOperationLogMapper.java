package com.ylink.inetpay.cbs.mrs.dao;

import java.util.Date;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

/**
 * 
 * 类说明：
 * 实现MrsHistoryOperationLog 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-9-14
 */
@MybatisMapper("mrsHistoryOperationLogMapper")
public interface MrsHistoryOperationLogMapper {

	/**
	 * 方法说明： 
	 * 添加MrsHistoryOperationLog
	 * @param  MrsHistoryOperationLog				
	 */
	int insert(Date date);
	 
	/**
	 * 方法说明： 
	 * 删除MrsHistoryOperationLog
	 * @param  MrsHistoryOperationLog的标识id				
	 */	
	int delete(Date date);
}