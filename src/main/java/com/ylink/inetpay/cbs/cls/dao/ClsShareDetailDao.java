package com.ylink.inetpay.cbs.cls.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsShareDetail;
import com.ylink.inetpay.common.project.clear.dto.ClsShareReport;

/**
 * 
 * 类说明：
 * 实现ClsShareDetail 的增删查改
 * 
 * <p>
 * 详细描述：
 *   
 * @author 1603254
 *   
 * CreateDate:  2016-5-17
 */
@MybatisMapper("clsShareDetailDao")
public interface ClsShareDetailDao {

	/**
	 * 方法说明： 
	 * 查询ClsShareDetail
	 * @param  ClsShareDetail				
	 * @return List 	查询的结果集
	 */	
	List<ClsShareDetail> queryClsShareDetail(ClsShareDetail clsShareDetail);


	/**
	 * @方法描述:  手续费分润明细报表表头
	 * @作者： 1603254
	 * @日期： 2016-6-2-上午10:50:33
	 * @param report
	 * @return 
	 * @返回类型： ReporHeadDto
	 */
	public ReporHeadDto  queryShareDetailSummary(ClsShareDetail detail);
	
	
}