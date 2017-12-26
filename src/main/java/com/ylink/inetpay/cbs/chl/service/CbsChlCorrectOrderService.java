package com.ylink.inetpay.cbs.chl.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.channel.dto.ChlCorrectOrderDto;

public interface CbsChlCorrectOrderService {
	/**
	 * 查询冲正订单数据
	 * @param pageDate
	 * @param chlCorrectOrderDto
	 * @return
	 */
	PageData<ChlCorrectOrderDto> queryAllData(PageData<ChlCorrectOrderDto> pageDate,
			ChlCorrectOrderDto chlCorrectOrderDto);

	/**
	 * 根据id查询冲正订单数据
	 * @param id
	 * @return
	 */
	ChlCorrectOrderDto selectById(String id);
}
