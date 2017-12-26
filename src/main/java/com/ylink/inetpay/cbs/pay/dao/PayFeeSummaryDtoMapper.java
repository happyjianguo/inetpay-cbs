package com.ylink.inetpay.cbs.pay.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.pay.dto.PayFeeSummaryDto;


@MybatisMapper("payFeeSummaryDtoMapper")
public interface PayFeeSummaryDtoMapper {

	 /**
     * 根据查询条件查询手续费
     * @param queryParam
     * @return
     */
    List<PayFeeSummaryDto> list(PayFeeSummaryDto queryParam);
    
    PayFeeSummaryDto selectedById(String busiId);
    
    /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayFeeSummaryDto queryParam);
    /**
     * 获取手续费总汇
     * @param queryParam
     * @return
     */
	List<PayFeeSummaryDto> queryList(PayFeeSummaryDto queryParam);
	/**
	 * 手续总汇检查详情
	 * @param id
	 * @return
	 */
	PayFeeSummaryDto detail(String id);
	/**
	 * 业务收支明细查询
	 * @param queryParam
	 * @return
	 */
	List<PayFeeSummaryDto> queryAll(PayFeeSummaryDto queryParam);
	/**
	 * 业务收入支出合计
	 * @param queryParam
	 * @return
	 */
	PayFeeSummaryDto findSumPayAmount(PayFeeSummaryDto queryParam);
}
