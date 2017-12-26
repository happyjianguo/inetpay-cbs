package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;

/**
 * 业务收入支出报表
 * @author pst08
 *
 */
public interface PayBusinessIncomeService {
	/**
	 * 分页业务收入支出
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	PageData<PayFeeSummaryDto> queryAll(PageData<PayFeeSummaryDto> pageData, PayFeeSummaryDto queryParam);
	/**
	 * 业务收入支出合计
	 * @param queryParam
	 * @return
	 */
	PayFeeSummaryDto findSumPayAmount(PayFeeSummaryDto queryParam);
	/**
	 * 业务收入支出查询
	 * @param queryParam
	 * @return
	 */
	List<PayFeeSummaryDto> findAll(PayFeeSummaryDto queryParam);

}
