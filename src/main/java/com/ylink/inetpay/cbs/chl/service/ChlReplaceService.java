package com.ylink.inetpay.cbs.chl.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.channel.dto.TbChlTransferOrder;
public interface ChlReplaceService {

	public TbChlTransferOrder details(String id);

	public PageData<TbChlTransferOrder> findListPage(PageData<TbChlTransferOrder> pageDate,
			TbChlTransferOrder tbDirectPayDto);
	

}
