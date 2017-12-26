package com.ylink.inetpay.cbs.cls.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.clear.dto.ClsAccessCheck;

public interface ClsAccessCheckService {
	
	/**
	 * 根据参数查询所有数据
	 * 
	 * @param pageData
	 * @param ClsAccessCheck
	 * @return
	 */
	PageData<ClsAccessCheck> queryAllData(PageData<ClsAccessCheck> pageData,
			ClsAccessCheck clsAccessCheck);
	/**
	 * 根据主键查询详情
	 * 
	 * @param id
	 * @return
	 */
	ClsAccessCheck selectByPrimaryKey(String id);
	
	/**
	 * 下载对账单
	 * 
	 * @param clsAccessCheck
	 * @return
	 */
	byte[] download(ClsAccessCheck clsAccessCheck);
}
