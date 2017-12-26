package com.ylink.inetpay.cbs.act.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.act.dao.ActBalanceMapper;
import com.ylink.inetpay.common.project.account.dto.ActBalanceDto;
import com.ylink.inetpay.common.project.account.dto.ActBillBalanceDto;

/**
 * @类名称： ActBalanceService
 * @类描述：试算平衡
 * @创建人： 1603254
 * @创建时间： 2016-5-30 下午6:01:13
 *
 * @修改人： 1603254
 * @操作时间： 2016-5-30 下午6:01:13
 * @操作原因： 
 * 
 */
@Service("actBalanceService")
public class ActBalanceServiceImpl implements ActBalanceService {

	@Autowired
	private ActBalanceMapper balanceMapper;
	
	/**
	 * @方法描述: 查询试算平衡
	 * @作者： 1603254
	 * @日期： 2016-5-30-下午6:01:56
	 * @param acctDate
	 * @return 
	 * @返回类型： ActBalanceDto
	*/
	public ActBalanceDto queryBalance(String acctDate){
		return balanceMapper.queryBalance(acctDate);
	}
	
	
	/**
	 * @方法描述:  记账分录试算平衡表 查询
	 * @作者： 1603254
	 * @日期： 2016-7-1-下午3:59:52
	 * @param acctDate
	 * @return 
	 * @返回类型： ActBillBalanceDto
	*/
	public ActBillBalanceDto queryBillBalance(String acctDate){
		return balanceMapper.queryBillBalance(acctDate);
	}
	
}
