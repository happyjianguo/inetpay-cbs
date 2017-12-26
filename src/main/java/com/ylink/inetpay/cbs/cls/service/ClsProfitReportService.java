package com.ylink.inetpay.cbs.cls.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.clear.dto.ClsProfitReport;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;

/**
 * @类名称： ClsProfitReportService
 * @类描述： 利润报表
 * @创建人： 1603254
 * @创建时间： 2016-6-2 下午3:35:28
 *
 * @修改人： 1603254
 * @操作时间： 2016-6-2 下午3:35:28
 * @操作原因： 
 * 
 */
public interface ClsProfitReportService {

	
	/**
	 * @方法描述: 利润报表 查询
	 * @作者： 1603254
	 * @日期： 2016-6-2-下午3:36:50
	 * @param pageData
	 * @param report
	 * @return 
	 * @返回类型： PageData<ClsProfitReport>
	*/
	public PageData<ClsProfitReport> queryProfitReport(PageData<ClsProfitReport> pageData,
			ClsProfitReport report);
}
