package com.ylink.inetpay.cbs.chl.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.channel.dto.TbChlRefundOrderDto;
public interface ChlRefundService {

	public TbChlRefundOrderDto details(String id);

	public PageData<TbChlRefundOrderDto> findListPage(PageData<TbChlRefundOrderDto> pageDate,
			TbChlRefundOrderDto refundOrderDto);
	
}
