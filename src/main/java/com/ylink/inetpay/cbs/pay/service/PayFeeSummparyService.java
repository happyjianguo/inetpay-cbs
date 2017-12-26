package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccessorDto;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;

public interface PayFeeSummparyService {
	/**
	 * 获取手续费汇总列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	PageData<PayFeeSummaryDto> pageList(PageData<PayFeeSummaryDto> pageData, PayFeeSummaryDto queryParam);
	/**
	 * 手续费总汇监控详情
	 * @param id
	 * @return
	 */
	PayFeeSummaryDto detail(String id);
 
	 
}
