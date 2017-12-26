package com.ylink.inetpay.cbs.bis.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ylink.inetpay.common.project.portal.vo.BankCodeResultVO;
import com.ylink.inetpay.common.project.portal.vo.BankResultVO;

@Path("bank")
public interface BisBankCodeRestService {

	/***
	 * 央行联行号查询
	 * @param params
	 * @return
	 */
	@POST
	@Path("bankcode")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankCodeResultVO getBankCode(String params);
	
	/**
	 * 银行行别查询
	 * @param params
	 * @return
	 */
	@POST
	@Path("bankList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	BankResultVO getBankList(String params);

	
}
