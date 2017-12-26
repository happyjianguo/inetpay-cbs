package com.ylink.inetpay.cbs.bis.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ylink.inetpay.common.project.portal.vo.AreaCityResultVO;

@Path("area")
public interface BisAreaCityRestService {

	@POST
	@Path("cityList")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AreaCityResultVO getBankCode(String params);
	
}
