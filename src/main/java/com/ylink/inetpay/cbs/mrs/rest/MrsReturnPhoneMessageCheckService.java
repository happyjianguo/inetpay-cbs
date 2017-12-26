package com.ylink.inetpay.cbs.mrs.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("return")
public interface MrsReturnPhoneMessageCheckService {
	/**
	 * 返回手机信息认证
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("phoneMessageCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	String individualCust(String params);
}