package com.ylink.inetpay.cbs.pay.service;

import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.common.project.pay.dto.PayOverlimitDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 支付限额超限记录服务类
 * @author haha
 *
 */
public interface PayOverlimitService {
	/**
	 * 单笔/累积超限查询,根据不同的业务类型查询。
	 * @param pageDate
	 * @param payBwlistDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<PayOverlimitDto> singleOverLimitList(PageData<PayOverlimitDto> pageDate,
			PayOverlimitDto payOverlimitDto);
	/**
	 * 单笔/累积超限查询,根据不同的业务类型查询。
	 * @param pageDate
	 * @param payBwlistDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<PayOverlimitDto> totalOverLimitList(PageData<PayOverlimitDto> pageDate,
			PayOverlimitDto payOverlimitDto);
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public PayOverlimitDto details(String id);
}
