package com.ylink.inetpay.cbs.cls.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsExceptionOrder;

/**
 * 
 * 类说明：
 * 实现ClsExceptionOrder 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-4-29
 */
@MybatisMapper("clsExceptionOrderDao")
public interface ClsExceptionOrderDao {

	/**
	 * @方法描述:根据异常记录id查询异常记录
	 * @作者： 1603254
	 * @日期： 2016-5-9-下午5:02:04
	 * @param id 
	 * @返回类型： void
	*/
	ClsExceptionOrder queryExceptionOrderById(String id);
}