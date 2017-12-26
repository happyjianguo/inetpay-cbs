package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsAccessCheck;

/**
 * 
 * 类说明：
 * 实现ClsMerCheck 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-4-29
 */
@MybatisMapper("clsMerCheckDao")
public interface ClsMerCheckDao {

	
	/**
	 * @方法描述:  根据id查找记录
	 * @作者： 1603254
	 * @日期： 2016-5-10-下午2:37:39
	 * @param id
	 * @return 
	 * @返回类型： ClsMerCheck
	*/
	ClsAccessCheck queryById(String id);
	
	/**
	 * @方法描述:  根据id查找记录
	 * @作者： 1603254
	 * @日期： 2016-5-10-下午2:37:39
	 * @param id
	 * @return 
	 * @返回类型： ClsMerCheck
	 */
	List<ClsAccessCheck> queryMerCheck(ClsAccessCheck check);
	
	 
	
	/**
	 * 方法说明： 
	 * 更新ClsMerCheck
	 * @param  ClsMerCheck				
	 * @return List 	查询的结果集
	 */		
	void updateMerCheck(ClsAccessCheck check);
	
}