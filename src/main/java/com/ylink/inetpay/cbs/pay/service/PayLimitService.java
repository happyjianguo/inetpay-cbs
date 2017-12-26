package com.ylink.inetpay.cbs.pay.service;

import java.util.List;

import com.ylink.inetpay.common.core.constant.EPayLimitBusinessType;
import com.ylink.inetpay.common.project.pay.dto.PayLimitDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
/**
 * 好运通会员支付限额服务类
 * @author haha
 *
 */
public interface PayLimitService {
	/**
	 * 查询会员支付限额
	 * @return
	 */
	public List<PayLimitDto> getPayLimits(String custId);
	/**
	 * 修改会员支付限额
	 * @throws CbsCheckedException
	 */
	public void updatePayList(List<PayLimitDto> payLimits);
	public PayLimitDto findByCustIdAndBusiType(String custId,EPayLimitBusinessType busiType);
}
