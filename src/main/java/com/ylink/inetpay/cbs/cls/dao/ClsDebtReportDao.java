package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.clear.dto.ClsDebtReport;

/**
 * 
 * 类说明：
 * 实现ClsDebtReport 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-5-30
 */
@MybatisMapper("clsDebtReportDao")
public interface ClsDebtReportDao {

	
	/**
	 * 方法说明： 根据日期查询
	 * 更新ClsDebtReport
	 * @param  ClsDebtReport				
	 * @return List 	查询的结果集
	 */		
	List<ClsDebtReport> queryDebtReport(String  calDate);
}