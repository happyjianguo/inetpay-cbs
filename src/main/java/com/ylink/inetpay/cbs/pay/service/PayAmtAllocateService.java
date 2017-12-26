package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
/**
 * 资金划拨服务类
 * @author yanggang
 * @2016-7-13 2016-7-13
 */
public interface PayAmtAllocateService {
	/**
	 * 资金划拨列表
	 * @param pageData
	 * @param queryparam
	 * @return
	 */
	public PageData<PayAmtAllocateDto> findAll(PageData<PayAmtAllocateDto> pageData,PayAmtAllocateDto queryparam);
	/**
	 * 资金划拨详情
	 * @param id
	 * @return
	 */
	public PayAmtAllocateDto details(String id);
	
	/**
	 * 订单查询
	 * @param pageData
	 * @param queryparam
	 * @return
	 */
	public List<PayAmtAllocateDto> findList(PayAmtAllocateDto payAmtAllocate);
	 /**
     * 收款查询
     * @param pageDate
     * @param payAmtAllocateDto
     * @return
     */
    PageData<PayAmtAllocateDto> queryAll(PageData<PayAmtAllocateDto> pageData,
    		PayAmtAllocateDto payAmtAllocateDto);
    /**
	 * 收款详情
	 * @param id
	 * @return
	 */
	public PayAmtAllocateDto receiptDetail(String id);
	/**
	 * 获取头寸调拨
	 * @param queryParam
	 * @return
	 */
	public List<PayAmtAllocateDto> queryAllPayAmtAllocate(PayAmtAllocateDto queryParam);
	
	/**
	 * 根据批次号查询
	 * @param queryParam
	 * @return
	 */
	public List<PayAmtAllocateDto> listBatchNo(PayAmtAllocateDto queryParam);
	/**
	 * 根据批次号查询批次商户对外支付订单明细
	 * @param batchNo
	 * @return
	 */
	public List<PayAmtAllocateDto> findPayAmtAllocateByBatchNo(PayAmtAllocateDto queryParam);
}
