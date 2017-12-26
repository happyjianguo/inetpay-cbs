package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import com.ylink.inetpay.common.project.clear.dto.ClsDebtReport;

/**
 * @类名称： ClsDebtReportService
 * @类描述： 资产负债
 * @创建人： 1603254
 * @创建时间： 2016-5-30 下午6:54:32
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-30 下午6:54:32
 * @操作原因： 
 * 
 */
public interface ClsDebtReportService {

	
	/**
	 * 方法说明： 根据日期查询
	 * 更新ClsDebtReport
	 * @param  ClsDebtReport				
	 * @return List 	查询的结果集
	 */		
	public List<ClsDebtReport> queryDebtReport(String  calDate);
	
}
