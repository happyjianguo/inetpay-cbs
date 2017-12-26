package com.ylink.inetpay.cbs.act.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.account.dto.ActBalanceDto;
import com.ylink.inetpay.common.project.account.dto.ActBillBalanceDto;

/**
 * @类名称： ActBalanceMapper
 * @类描述：  平衡报表的查询
 * @创建人： 1603254
 * @创建时间： 2016-7-1 下午4:02:17
 *
 * @修改人： 1603254
 * @操作时间： 2016-7-1 下午4:02:17
 * @操作原因： 
 * 
 */
@MybatisMapper("actBalanceMapper")
public interface ActBalanceMapper {
	
	/**
	 * @方法描述: 根据日期查询失算平衡
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午6:04:53
	 * @param acctDate
	 * @return 
	 * @返回类型： ActBalanceDto
	*/
	ActBalanceDto queryBalance(String acctDate);
	
	
	/**
	 * @方法描述:  记账分录试算平衡表 查询
	 * @作者： 1603254
	 * @日期： 2016-7-1-下午4:02:10
	 * @param acctDate
	 * @return 
	 * @返回类型： ActBillBalanceDto
	*/
	ActBillBalanceDto queryBillBalance(String acctDate);
	
}
