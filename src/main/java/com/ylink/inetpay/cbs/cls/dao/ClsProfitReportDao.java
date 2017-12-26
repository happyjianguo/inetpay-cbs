package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsProfitReport;

@MybatisMapper("clsProfitReportDao")
public interface ClsProfitReportDao {

	/**
	 * @方法描述:  查询利润报表
	 * @作者： 1603254
	 * @日期： 2016-6-2-下午3:30:23
	 * @param acctDate
	 * @return 
	 * @返回类型： List<ClsProfitReport>
	*/
	public List<ClsProfitReport> queryProfitReport(String acctDate);
	
	
}
