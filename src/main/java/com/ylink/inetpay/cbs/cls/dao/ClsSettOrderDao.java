package com.ylink.inetpay.cbs.cls.dao;

//import com.ylink.inetpay.clear.domain.*;
import com.ylink.inetpay.common.project.clear.dto.*;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import java.util.List; 

/**
 * 
 * 类说明：
 * 实现ClsSettOrder 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-11-10
 */
 @MybatisMapper("clsSettOrderDao")
public interface ClsSettOrderDao {

	/**
	 * 方法说明： 
	 * 添加ClsSettOrder
	 * @param  ClsSettOrder				
	 */
	void addClsSettOrder(ClsSettOrder clsSettOrder);
	

	/**
	 * 方法说明： 
	 * 查询ClsSettOrder
	 * @param merCodes 
	 * @param  ClsSettOrder				
	 * @return List 	查询的结果集
	 */	
	List<ClsSettOrder> queryClsSettOrder(@Param(value ="clsSettOrder")ClsSettOrder clsSettOrder, @Param(value ="merCodesList")List<String> merCodesList);

	/**
	 * 方法说明： 
	 * 删除ClsSettOrder
	 * @param  ClsSettOrder的标识id				
	 */	
	void deleteClsSettOrder(List<String> list);
	
	
	/**
	 * 方法说明： 
	 * 更新ClsSettOrder
	 * @param  ClsSettOrder				
	 * @return List 	查询的结果集
	 */		
	void updateClsSettOrder(ClsSettOrder clsSettOrder);
	/**
	 * 查询结算订单详情
	 */
	ClsSettOrder queryById(String id);
}