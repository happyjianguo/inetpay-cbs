package com.ylink.inetpay.cbs.act.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.account.dto.ActCheckFileDto;

/**
 * @类名称： ActCheckStatusService
 * @类描述： 记账对账监控任务管理
 * @创建人： 1603254
 * @创建时间： 2016-5-27 上午11:18:27
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-27 上午11:18:27
 * @操作原因： 
 * 
 */
public interface ActCheckStatusService {
	
	/**
	 * @方法描述: 查询记账对账监控状态
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午10:09:01
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： PageData<ActCheckFileDto>
	*/
	public PageData<ActCheckFileDto> findCheckStatus(PageData<ActCheckFileDto> pageData,
			ActCheckFileDto check);
	
	
	/**
	 * @方法描述: 根据id查询记录
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午10:01:06
	 * @param id 
	 * @返回类型： void
	*/
	public ActCheckFileDto findById(String id);
	
	
	/**
	 * @方法描述:  更新处理状态为成功
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:20:36
	 * @param actCheckFile 
	 * @返回类型： void
	*/
	void updateDealStatus(ActCheckFileDto actCheckFile);
}
