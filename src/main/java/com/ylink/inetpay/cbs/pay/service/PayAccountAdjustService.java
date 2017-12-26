package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ExportDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.pay.dto.PayAccountAdjustDto;
import com.ylink.inetpay.common.project.pay.exception.PayCheckedException;

/**
 * 账户调账服务类 
 * @author lyg
 *
 */
public interface PayAccountAdjustService {
	 /**
     * 添加调账记录
     * @param payAccountAdjustDto
     * @throws PayCheckedException
     */
    public void addNotes(PayAccountAdjustDto payAccountAdjustDto);
    /**
     * 调账审核
     * @param payAccountAdjustDto
     * @throws PayCheckedException
     */
    public void auditPass(PayAccountAdjustDto payAccountAdjustDto);
    /**
     * 获取审核列表
     * @param payAccountAdjustDto
     * @return
     * @throws CbsCheckedException
     */
    public PageData<PayAccountAdjustDto> auditPageList(PayAccountAdjustDto payAccountAdjustDto,PageData<PayAccountAdjustDto> pageData);
    /**
     * 待审核记录详情
     * @param payAccountAdjustDto
     * @throws CbsCheckedException
     */
    public PayAccountAdjustDto auditDetails(String id);
    
	/**
	 * @方法描述: 查询调账的记录条数(调账审核)
	 * @作者： 1603254
	 * @日期： 2016-6-1-上午11:43:56
	 * @return 
	 * @返回类型： Integer
	*/
	public Integer queryAdjustCountByAdjust();
	
	/**
	 * @方法描述: 查询调账的记录条数(补录审核)
	 * @作者： 1603254
	 * @日期： 2016-6-1-上午11:43:56
	 * @return 
	 * @返回类型： Integer
	 */
	public Integer queryAdjustCountByRecover();
	/**
	 * 根据条件获取导出统计数据
	 * @param queryParam
	 * @return
	 */
	public ExportDto export(PayAccountAdjustDto queryParam);
	/**
	 * 获取账户调账列表
	 * @param pageData
	 * @param queryParam
	 * @return
	 */
	public PageData<PayAccountAdjustDto> list(PageData<PayAccountAdjustDto> pageData,PayAccountAdjustDto queryParam);
	
	/***
	 * 查询单条数据详情
	 * @param id
	 * @return
	 */
	public PayAccountAdjustDto getById(String id);
}
