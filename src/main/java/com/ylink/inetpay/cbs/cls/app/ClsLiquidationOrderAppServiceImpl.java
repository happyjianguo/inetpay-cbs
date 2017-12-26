package com.ylink.inetpay.cbs.cls.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsLiquidationOrderService;
import com.ylink.inetpay.common.project.cbs.app.ClsLiquidationOrderAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsLiquidationOrder;

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
@Service("clsLiquidationOrderAppService")
public class ClsLiquidationOrderAppServiceImpl implements ClsLiquidationOrderAppService{
	@Autowired
	private ClsLiquidationOrderService clsLiquidationOrderService;
	/**
	 * 查询清分任务监控
	 */
	public PageData<ClsLiquidationOrder> findClsLiquidationOrder(PageData<ClsLiquidationOrder> pageData,
			ClsLiquidationOrder clsLiquidationOrderder) {
		return clsLiquidationOrderService.findLiquidationOrder(pageData, clsLiquidationOrderder);
	}
	
	/**
	 * 根据id查询详情
	 */
	public ClsLiquidationOrder findById(String id) {
		return clsLiquidationOrderService.findById(id);
	}
}
