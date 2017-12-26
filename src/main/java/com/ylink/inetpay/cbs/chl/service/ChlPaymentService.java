package com.ylink.inetpay.cbs.chl.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.channel.dto.TbChlPayOrderDto;
public interface ChlPaymentService {

	public TbChlPayOrderDto details(String id);

	public PageData<TbChlPayOrderDto> findListPage(PageData<TbChlPayOrderDto> pageDate, TbChlPayOrderDto payOrderDto);
	

}
