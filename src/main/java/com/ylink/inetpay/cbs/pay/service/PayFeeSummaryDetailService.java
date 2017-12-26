package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDetailDto;


/**
 * 手续费报表明细查询接口
 * @author pst08
 *
 */
public interface PayFeeSummaryDetailService {

	public PageData<PayFeeSummaryDetailDto> listPage(PageData<PayFeeSummaryDetailDto> pageData, PayFeeSummaryDetailDto queryParam);
	/**
	 * 
	 *方法描述：根据查询条件查询所有数据
	 * 创建人：pst10
	 * 创建时间：2017年5月16日 上午9:46:30
	 * @param queryParam
	 * @return
	 */
	public List<PayFeeSummaryDetailDto> queryAllBySearch(PayFeeSummaryDetailDto queryParam);
}
