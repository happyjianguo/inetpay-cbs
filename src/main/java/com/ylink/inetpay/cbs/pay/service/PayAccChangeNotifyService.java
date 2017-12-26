package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayAccChangeNotifyDto;


public interface PayAccChangeNotifyService {

	public PageData<PayAccChangeNotifyDto> findAll(PageData<PayAccChangeNotifyDto> pageData,PayAccChangeNotifyDto queryparam);
	
	/**
	 * 划拨详情
	 * @param id
	 * @return
	 */
	public PayAccChangeNotifyDto details(String id);
	/**
	 * 保单退保
	 * @param warrantyNo
	 * @param accChangeOrderId
	 */
	public void movingAccountMatching(String streamNo, String accChangeBusiId);
	/**
	 * 根据id查数据
	 * @param id
	 * @return
	 */
	public PayAccChangeNotifyDto findPayAccChangeNotifyDtoById(String id);
}
