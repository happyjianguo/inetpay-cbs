package com.ylink.inetpay.cbs.cls.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.clear.dto.ClsAccessCheck;

public interface ClsMerCheckStatusService {

	
	/**
	 * @方法描述:  根据id查找
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午2:31:47
	 * @param id
	 * @return 
	 * @返回类型： ClsMerCheck
	*/
	public ClsAccessCheck queryById(String id);
	
	/**
	 * @方法描述: 根据条件查询
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午2:31:58
	 * @param pageData
	 * @param check
	 * @return 
	 * @返回类型： PageData<ClsMerCheck>
	*/
	public PageData<ClsAccessCheck> queryMerCheck( PageData<ClsAccessCheck> pageData,
			ClsAccessCheck check);
	
	/**
	 * @方法描述:  更新状态
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午2:53:14
	 * @param check 
	 * @返回类型： void
	*/
	public void updateMerCheck(ClsAccessCheck check);
	
}

