package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayRedPDto;
/**
 * 红包发放服务类
 * @author yanggang
 * @2016-7-13 2016-7-13
 */
public interface PayRedService {
	/**
	 * 红包发放列表
	 * @param pageData
	 * @param queryparam
	 * @return
	 */
	public PageData<PayRedPDto> findAll(PageData<PayRedPDto> pageData,PayRedPDto queryparam);
	/**
	 * 红包发放详情
	 * @param id
	 * @return
	 */
	public PayRedPDto details(String id);
}
