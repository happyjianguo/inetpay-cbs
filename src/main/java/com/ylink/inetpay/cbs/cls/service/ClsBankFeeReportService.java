package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.clear.dto.ClsBankFeeRepDto;
import com.ylink.inetpay.common.project.clear.dto.ClsChannelBill;

/**
 * @类名称： ClsShareReportService
 * @类描述：渠道手续费接口服务类
 * @创建人： yuqingjun
 * @创建时间： 2016-12-25 上午10:35:02
 * 
 */
public interface ClsBankFeeReportService {

	/**
	 * @方法描述: 渠道手续费汇总报表
	 * @作者： yuqingjun
	 * @日期： 2016-12-26
	 * @param pageData
	 * @return 
	 */
	public List<ClsBankFeeRepDto> queryAllBankFee(ClsBankFeeRepDto report);
	
	/**
	 * @方法描述: 渠道手续费汇总报表
	 * @作者： yuqingjun
	 * @日期： 2016-12-26
	 * @param pageData
	 * @return 
	 */
	public PageData<ClsBankFeeRepDto> queryBankFee(PageData<ClsBankFeeRepDto> pageData,
			ClsBankFeeRepDto report);
	/**
	 * @方法描述: 渠道手续费明细报表
	 * @作者： yuqingjun
	 * @日期： 2016-12-26
	 * @param pageData
	 * @return 
	 */
	public PageData<ClsChannelBill> queryBankFeeDetail(PageData<ClsChannelBill> pageData,
			ClsChannelBill report);

	/**
	 * 渠道手续费明细报表导出
	 * @param report
	 * @return
	 */
	public List<ClsChannelBill> queryAllBankFeeDetail(ClsChannelBill report);
 
}

