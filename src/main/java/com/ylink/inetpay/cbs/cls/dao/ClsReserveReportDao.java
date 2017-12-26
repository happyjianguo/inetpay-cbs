package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsReserveReport;

/**
 * @类名称： ClsReserveReportDao
 * @类描述： 备付金报表操作
 * @创建人： 1603254
 * @创建时间： 2016-5-30 下午8:02:18
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-30 下午8:02:18
 * @操作原因： 
 * 
 */
@MybatisMapper("clsReserveReportDao")
public interface ClsReserveReportDao {
	
	/**
	 * @方法描述:  查询
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午8:02:15
	 * @param calDate
	 * @return 
	 * @返回类型： List<ClsReserveReport>
	*/
	public List<ClsReserveReport> queryReserveReport(String calDate);
}
