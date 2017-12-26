package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.CLsMerchantSettleVo;
import com.ylink.inetpay.common.project.clear.dto.ClsClearOrder;
import com.ylink.inetpay.common.project.clear.dto.ClsMerSett;
import com.ylink.inetpay.common.project.clear.dto.ClsPayBill;

/**
 * @类名称： ClsMerSettleService
 * @类描述： 清分订单接口服务类
 * @创建人： 1603254
 * @创建时间： 2016-5-25 下午2:35:48
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 下午2:35:48
 * @操作原因： 
 * 
 */
public interface ClsClearOrderService {

	/**
	 * @方法描述: 分页查询清分订单
	 * @作者： 1603254
	 * @日期： 2016-5-25-下午2:37:07
	 * @param pageDate
	 * @param settle
	 * @return 
	 * @返回类型： PageData<ClsClearOrder>
	*/
	public PageData<ClsClearOrder> queryClsClearOrder(PageData<ClsClearOrder> pageDate,
			ClsClearOrder clsClearOrder);
	
	/**
	 * 根据id查询清分订单详情
	 */
	public ClsClearOrder deails(String id);
}
