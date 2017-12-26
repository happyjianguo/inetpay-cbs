package com.ylink.inetpay.cbs.bis.dao;

import java.util.Date;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

/**
 * 
 * 类说明：
 * 实现BisHistorySms 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-9-14
 */
@MybatisMapper("bisHistorySmsMapper")
public interface BisHistorySmsMapper {

	/**
	 * 方法说明： 
	 * 添加BisHistorySms
	 * @param  BisHistorySms				
	 */
	int insert(Date date);
	
 
	/**
	 * 方法说明： 
	 * 删除BisHistorySms
	 * @param  BisHistorySms的标识id				
	 */	
	int delete(Date date);
}