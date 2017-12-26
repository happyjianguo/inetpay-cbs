package com.ylink.inetpay.cbs.cls.dao;

//import com.ylink.inetpay.clear.domain.*;
import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsAcctCheck;

/**
 * 
 * 类说明：
 * 实现ClsAcctCheck 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-4-29
 */
@MybatisMapper("clsAcctCheckDao")
public interface ClsAcctCheckDao {
	
	/**
	 * 方法说明： 
	 * 根据日期查询TbClsAcctCheck
	 * @param  checkDate				
	 * @return List 	查询的结果集
	 */	
	List<ClsAcctCheck> queryClsAcctCheck(ClsAcctCheck check);

	 
	/**
	 * 方法说明： 
	 * 根据Id查询TbClsAcctCheck
	 * @param  checkDate				
	 * @return List 	查询的结果集
	 */	
	ClsAcctCheck queryClsAcctCheckById(String id);
	
	/**
	 * @方法描述:  根据对账日期查询对账任务
	 * @作者： hinode
	 * @日期： 2016-7-27-下午3:03:49
	 * @param checkDay
	 * @return 
	 * @返回类型： ClsAcctCheck
	 */
	ClsAcctCheck queryByCheckDay(String checkDay);
	
	/**
	 * @方法描述: 更新状态为未处理
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:33:25 
	 * @返回类型： void
	*/
	void updateDealStatusToUnProcess(String id);
	 
}