package com.ylink.inetpay.cbs.cls.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.clear.dto.ClsAcctCheck;

/**
 * @类名称： ClsAcctCheckService
 * @类描述： 内部对账监控服务类接口
 * @创建人： 1603254
 * @创建时间： 2016-5-27 下午4:05:32
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-27 下午4:05:32
 * @操作原因： 
 * 
 */
public interface ClsAcctCheckService{

	
	/**
	 * @方法描述:  查询对账监控的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:06:40
	 * @param pageData
	 * @param book
	 * @return 
	 * @返回类型： PageData<ClsAcctBook>
	*/
	public PageData<ClsAcctCheck> findAcctCheck(PageData<ClsAcctCheck> pageData,
			ClsAcctCheck check);
	
	
	
	/**
	 * @方法描述:  重新执行失败的记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:07:19
	 * @param id
	 * @return 
	 * @返回类型： String
	*/
	public ClsAcctCheck findById(String id);
 
	
	/**
	 * @方法描述: 更新状态为未处理
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:33:25 
	 * @返回类型： void
	*/
	void updateDealStatusToUnProcess(String id);
}
