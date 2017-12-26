package com.ylink.inetpay.cbs.act.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ylink.inetpay.common.project.cbs.vo.act.AccountBalanceQueryRespVo;
import com.ylink.inetpay.common.project.cbs.vo.pay.OrderDetailsQueryRespVo;

/**
 * 
 * @author pst11
 *
 */
@Path("account")
public interface ActSearchAccountService {

	/**
	 * 商户一户通账户余额查询
	 * 	前提：只查询资金账户生成的一户通
	 * 入参：一户通号、账户类型
	 * 出参：<一户通号,账户余额>
	 * 
	 */
	@POST
	@Path("balanceQuery")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountBalanceQueryRespVo balanceQuery(String params) throws Exception;
	
	@POST
	@Path("orderDetailsQuery")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	OrderDetailsQueryRespVo orderDetailsQuery(String params) throws Exception;
	
}
