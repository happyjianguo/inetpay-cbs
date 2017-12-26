package com.ylink.inetpay.cbs.cls.app;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsLiquidationJobService;
import com.ylink.inetpay.common.project.cbs.app.ClsLiquidationJobAppService;
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
@Service("clsLiquidationJobAppService")
public class ClsLiquidationJobAppServiceImpl  implements ClsLiquidationJobAppService{
	@Autowired
	private ClsLiquidationJobService clsLiquidationJobService;
	/**
	 * 查询清算任务监控
	 */
	public PageData<ClsLiquidationJob> findLiquidationJob(PageData<ClsLiquidationJob> pageData,
			ClsLiquidationJob liquiDation) {
		return clsLiquidationJobService.queryClsLiquidationJob(pageData, liquiDation);
	}

}
