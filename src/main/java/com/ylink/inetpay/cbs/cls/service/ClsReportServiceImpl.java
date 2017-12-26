package com.ylink.inetpay.cbs.cls.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.cls.dao.ClsReserveReportDao;
import com.ylink.inetpay.common.project.clear.dto.ClsReserveReport;

/**
 * @类名称： ClsReportService
 * @类描述： 备付金报表查询
 * @创建人： 1603254
 * @创建时间： 2016-5-31 上午9:17:29
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-31 上午9:17:29
 * @操作原因： 
 * 
 */
@Service("clsReportService")
public class ClsReportServiceImpl implements ClsReportService{

	@Autowired
	private ClsReserveReportDao reserveReportDao;
	
	/**
	 * @方法描述:  查询备付金信息
	 * @作者： 1603254
	 * @日期： 2016-5-31-上午9:18:45
	 * @return 
	 * @返回类型： List<ClsReserveReport>
	*/
	public List<ClsReserveReport> queryReserveReport(String calDate){
		return reserveReportDao.queryReserveReport(calDate);
	}
}
