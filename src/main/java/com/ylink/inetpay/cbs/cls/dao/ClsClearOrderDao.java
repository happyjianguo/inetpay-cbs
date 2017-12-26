package com.ylink.inetpay.cbs.cls.dao;

//import com.ylink.inetpay.clear.domain.*;
import com.ylink.inetpay.common.project.clear.dto.*;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import java.util.List; 

/**
 * 
 * 类说明：
 * 实现ClsClearOrder 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-11-10
 */
 @MybatisMapper("clsClearOrderDao")
public interface ClsClearOrderDao {

	/**
	 * 方法说明： 
	 * 添加ClsClearOrder
	 * @param  ClsClearOrder				
	 */
	void addClsClearOrder(ClsClearOrder clsClearOrder);
	

	/**
	 * 方法说明： 
	 * 查询ClsClearOrder
	 * @param  ClsClearOrder				
	 * @return List 	查询的结果集
	 */	
	List<ClsClearOrder> queryClsClearOrder(ClsClearOrder clsClearOrder);

	/**
	 * 方法说明： 
	 * 删除ClsClearOrder
	 * @param  ClsClearOrder的标识id				
	 */	
	void deleteClsClearOrder(List<String> list);
	
	
	/**
	 * 方法说明： 
	 * 更新ClsClearOrder
	 * @param  ClsClearOrder				
	 * @return List 	查询的结果集
	 */		
	void updateClsClearOrder(ClsClearOrder clsClearOrder);
	
	/**
	 * 根据id查询订单详情
	 */
	ClsClearOrder queryById(String id);
}