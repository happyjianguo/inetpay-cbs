package com.ylink.inetpay.cbs.cls.app;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClearJobService;
import com.ylink.inetpay.cbs.cls.service.ClsClearOrderService;
import com.ylink.inetpay.cbs.cls.service.ClsLiquidationJobService;
import com.ylink.inetpay.common.project.cbs.app.ClearJobAppService;
import com.ylink.inetpay.common.project.cbs.app.ClsClearOrderAppService;
import com.ylink.inetpay.common.project.cbs.app.ClsLiquidationJobAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsClearJob;
import com.ylink.inetpay.common.project.clear.dto.ClsClearOrder;
import com.ylink.inetpay.common.project.clear.dto.ClsLiquidationJob;

/**
 * @类名称： ClsMerchantSettleAppServiceImpl
 * @类描述： 
 * @创建人： 1603254
 * @创建时间： 2016-5-25 上午11:46:02
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-25 上午11:46:02
 * @操作原因： 
 * 
 */
@Service("clsClearOrderAppService")
public class ClsClearOrderAppServiceImpl  implements ClsClearOrderAppService{
	@Autowired
	private ClsClearOrderService clsClearOrderService;
	
	/**
	 * 查询清分订单
	 */
	public PageData<ClsClearOrder> findClsClearOrder(PageData<ClsClearOrder> pageData,
			ClsClearOrder clsClearOrder) {
		return clsClearOrderService.queryClsClearOrder(pageData, clsClearOrder);
	}
	
	/**
	 * 根据id查询订单详情
	 */
	public ClsClearOrder details(String id) {
		return clsClearOrderService.deails(id);
	}

}
