package com.ylink.inetpay.cbs.cls.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.CLSReviewStatus;
import com.ylink.inetpay.common.project.clear.dto.ClsBankCheck;

public interface ClsBankCheckService {

	/**
	 * @方法描述:  查询资金
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:54:25
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： ClsBankCheck
	 */
	public PageData<ClsBankCheck> findBankCheck(PageData<ClsBankCheck> pageData,ClsBankCheck check);
	
	
	
	/**
	 * @方法描述:  
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午2:55:45
	 * @param id
	 * @return 
	 * @返回类型： String
	 */
	public ClsBankCheck findById(String id);
	
	
	public boolean isEqual(String keyId, String currentUserLoginName,
			CLSReviewStatus wait);
}
