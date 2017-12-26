package com.ylink.inetpay.cbs.mrs.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ylink.inetpay.common.project.portal.vo.IndividualResponseVO;
import com.ylink.inetpay.common.project.portal.vo.ProductResponseVO;
import com.ylink.inetpay.common.project.portal.vo.UnitResponseVO;

@Path("passiveopenacnt")
public interface MrsPassiveOpenAcntService {
	/**
	 * 个人被动开户
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("individual")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	IndividualResponseVO individualCust(String params);
	
	/**
	 * 机构被动开户(juzai)
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("unit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	UnitResponseVO unit(String params);


	/**
	 * 机构被动开户(资管)
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("unitAsset")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	UnitResponseVO unitAsset(String params);

	/**
	 * 产品被动开户
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("product")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	ProductResponseVO product(String params);
}
