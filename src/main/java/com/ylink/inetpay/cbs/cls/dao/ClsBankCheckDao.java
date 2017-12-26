package com.ylink.inetpay.cbs.cls.dao;

//import com.ylink.inetpay.clear.domain.*;
import com.ylink.inetpay.common.project.clear.dto.*;

import java.util.List; 

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

/**
 * 
 * 类说明：
 * 实现ClsBankCheck 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-4-29
 */
@MybatisMapper("clsBankCheckDao")
public interface ClsBankCheckDao {

	 
	/**
	 * 方法说明： 
	 * 查询ClsBankCheck
	 * @param  ClsBankCheck				
	 * @return List 	查询的结果集
	 */	
	List<ClsBankCheck> queryClsBankCheck(ClsBankCheck check);
	 
	
	/**
	 * @方法描述:  根据id查找对象
	 * @作者： 1603254
	 * @日期： 2016-5-4-下午2:21:08
	 * @return 
	 * @返回类型： ClsBankCheck
	*/
	ClsBankCheck queryClsBankCheckById(String id);
	
	 
}