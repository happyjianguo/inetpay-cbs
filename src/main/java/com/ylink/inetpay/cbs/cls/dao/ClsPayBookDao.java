package com.ylink.inetpay.cbs.cls.dao;

//import com.ylink.inetpay.clear.domain.*;
import com.ylink.inetpay.common.project.clear.dto.*;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import java.util.List; 

/**
 * 
 * 类说明：
 * 实现ClsPayBook 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-11-10
 */
 @MybatisMapper("clsPayBookDao")
public interface ClsPayBookDao {

	/**
	 * 方法说明： 
	 * 添加ClsPayBook
	 * @param  ClsPayBook				
	 */
	void addClsPayBook(ClsPayBook clsPayBook);
	

	/**
	 * 方法说明： 
	 * 查询ClsPayBook
	 * @param  ClsPayBook				
	 * @return List 	查询的结果集
	 */	
	List<ClsPayBook> queryClsPayBook(ClsPayBook clsPayBook);

	/**
	 * 方法说明： 
	 * 删除ClsPayBook
	 * @param  ClsPayBook的标识id				
	 */	
	void deleteClsPayBook(List<String> list);
	
	
	/**
	 * 方法说明： 
	 * 更新ClsPayBook
	 * @param  ClsPayBook				
	 * @return List 	查询的结果集
	 */		
	void updateClsPayBook(ClsPayBook clsPayBook);
}