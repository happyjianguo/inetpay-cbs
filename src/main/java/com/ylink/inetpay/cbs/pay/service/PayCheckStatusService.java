package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayCheckFileDto;

/**
 * @类名称： PayCheckStatusAppService
 * @类描述： 支付系统对账监控任务执行
 * @创建人： 1603254
 * @创建时间： 2016-5-27 上午9:37:28
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-27 上午9:37:28
 * @操作原因： 
 * 
 */
public interface PayCheckStatusService {

	
	/**
	 * @方法描述:  查询监控状态  
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午9:47:16
	 * @param pageDate
	 * @param check
	 * @return 
	 * @返回类型： PageData<PayCheckFileDto>
	*/
	public PageData<PayCheckFileDto> finCheckStatus(PageData<PayCheckFileDto> pageData,
			PayCheckFileDto check);
	
	
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-27-上午10:52:49
	 * @param id
	 * @return 
	 * @返回类型： PayCheckFileDto
	*/
	public PayCheckFileDto findById(String id);
	
	
	/**
	 * @方法描述: 更新状态为未处理
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:48:55
	 * @param id 
	 * @返回类型： void
	*/
	public void updateDealStatusToUnProcess(String id);
}
