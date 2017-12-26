package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayExternalHandleDto;

/**
 * 对外支付经办
 * @author pst10
 *
 */
public interface PayExternalHandleService {
	/**
	 * 查询所有分页
	 * @param params
	 * @return
	 */
	public PageData<PayExternalHandleDto> getByCond(PayExternalHandleDto payExternalDto,PageData<PayExternalHandleDto> pageData);

	/**
	 * 新增
	 * @param payExternalDto
	 */
	public void insert(PayExternalHandleDto payExternalDto);
	
	
	
	/**
	 * 修改
	 * @param payExternalDto
	 */
	public void update(PayExternalHandleDto payExternalDto);
	

	/**
	 * 查询单条
	 * @param id
	 * @return
	 */
	public PayExternalHandleDto details(String id);
}
