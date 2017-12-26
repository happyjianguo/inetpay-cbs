package com.ylink.inetpay.cbs.pay.service;


import java.util.Date;
import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EPayBusiType;
import com.ylink.inetpay.common.core.constant.EPayFeeState;
import com.ylink.inetpay.common.project.pay.dto.PayFeeConfigDto;

public interface PayFeeConfigService {

	/**
	 * 
	* @方法描述：新增 
	* @param record
	* @return long 
	* @作者： SEN_SHAO
	* @创建时间： 2017年4月18日 上午11:20:41
	 */
	long save(PayFeeConfigDto record);

	/**
	 * 
	* @方法描述：新增时的ajax校验 
	* @param merCode
	* @param busiType
	* @param effectiveDate
	* @return PayFeeConfigDto 
	* @作者： SEN_SHAO
	* @创建时间： 2017年4月18日 上午11:20:50
	 */
	PayFeeConfigDto findFeeConfig(String merCode, EPayBusiType busiType, String effectiveDate);

	/**
	 * 
	* @方法描述：分页列表 
	* @param pageData
	* @param queryParam
	* @return PageData<PayFeeConfigDto> 
	* @作者： SEN_SHAO
	* @创建时间： 2017年4月18日 上午11:21:06
	 */
	PageData<PayFeeConfigDto> findByCondition(PageData<PayFeeConfigDto> pageData, PayFeeConfigDto queryParam);

	/**
	 *
	* @方法描述： 通过id查找 
	* @param id
	* @return PayFeeConfigDto 
	* @作者： SEN_SHAO
	* @创建时间： 2017年4月18日 上午11:21:20
	 */
	PayFeeConfigDto findById(String id);

	/**
	 * 
	* @方法描述：更新手续费配置 
	* @param record
	* @return long 
	* @作者： SEN_SHAO
	* @创建时间： 2017年4月18日 上午11:21:41
	 */
	long update(PayFeeConfigDto record);

	/**
	 * 查询待生效列表
	 * @param pageData
	 * @param payFeeState
	 * @param effectiveDate
	 * @return
	 */
	List<PayFeeConfigDto> findByStatusPage(PageData<PayFeeConfigDto> pageData, EPayFeeState payFeeState,
			Date effectiveDate);

	/**
	 * 代付手续费生效
	 * @param id
	 * @param payFeeState
	 */
	void updateFeeStatus(String id, EPayFeeState payFeeState);

	/**
	 * 
	* @方法描述：更新时的ajax校验 
	* @param merCode
	* @param busiType
	* @param effectiveDate
	* @return List<PayFeeConfigDto> 
	* @作者： SEN_SHAO
	* @创建时间： 2017年4月18日 上午11:21:57
	 */
	List<PayFeeConfigDto> editAjaxcheck(String merCode, EPayBusiType busiType, String effectiveDate);


	PayFeeConfigDto queryByMerCodeBusiTypeEffectiveDate(String merCode, EPayBusiType busiType, String effectiveDate,
			EPayFeeState payFeeState);


	String getPlatTradeNoSeqNo(int length, String seqName);


}
