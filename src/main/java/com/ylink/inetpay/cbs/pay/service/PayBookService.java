package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayBookDto;

public interface PayBookService {
	/**
	 * 查询支付流水订单数据
	 * 
	 * @param pageDate
	 * @param PayBookDto
	 * @return
	 */
	PageData<PayBookDto> queryAllData(PageData<PayBookDto> pageDate,
			PayBookDto payBookDto);
	public List<PayBookDto> listPayBook(PayBookDto payBookDto) ;
	/**
	 * 根据平台业务订单号查询
	 * 
	 * @param busiId
	 * @return
	 */
	PayBookDto selectByPayId(String busiId);
	 /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayBookDto payBookDto);
    
    /**
	 * 查询支付流水订单数据
	 * 
	 * @param pageDate
	 * @param PayBookDto
	 * @return
	 */
	PageData<PayBookDto> queryAllDataForAccount(PageData<PayBookDto> pageDate,
			PayBookDto payBookDto);
}
