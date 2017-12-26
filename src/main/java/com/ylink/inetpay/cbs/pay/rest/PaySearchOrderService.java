package com.ylink.inetpay.cbs.pay.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ylink.inetpay.common.project.cbs.vo.pay.OrderOutPayParamQueryRespVo;

/**
 * 
 * @author pst11
 *
 */
@Path("order")
public interface PaySearchOrderService {

	/**
	 * 商户对外支付查询
	 * 入参：
	 * 出参：<>
	 * 
	 */
	@POST
	@Path("outPayParamQuery")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	OrderOutPayParamQueryRespVo balanceQuery(String params) throws Exception;
	
}
