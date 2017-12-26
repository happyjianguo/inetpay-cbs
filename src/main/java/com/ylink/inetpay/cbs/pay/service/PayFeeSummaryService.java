package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;

public interface PayFeeSummaryService {

	/**
	 * 分页查询手续费信息
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<PayFeeSummaryDto> listPage(PageData<PayFeeSummaryDto> pageData, PayFeeSummaryDto queryParam);
	
	PayFeeSummaryDto selectedById(String busiId);
	
	/**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayFeeSummaryDto queryParam);

    /**
     * 
    * @方法描述：查询汇总报表 
    * @param queryParam
    * @return List<ClsChannelBill> 
    * @作者： SEN_SHAO
    * @创建时间： 2017年4月24日 下午3:21:00
     */
	public List<PayFeeSummaryDto> queryAll(PayFeeSummaryDto queryParam);
}
