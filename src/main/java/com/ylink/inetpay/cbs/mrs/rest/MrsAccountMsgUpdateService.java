package com.ylink.inetpay.cbs.mrs.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ylink.inetpay.common.project.portal.vo.AccountCloseMsgRespVo;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgRespVo;

@Path("account")
public interface MrsAccountMsgUpdateService {

	/**
	 * 个人账户变更接口
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("individualUpdate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountMsgRespVo individualUpdate(String params);

	/**
	 * 机构账户变更接口
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("unitUpdate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountMsgRespVo unitUpdate(String params);

	/**
	 * 业务权限变更接口
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("updateBusiInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountMsgRespVo updateBusi(String params) throws Exception;

	/**
	 * 产品账户变更接口
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("productUpdate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountMsgRespVo productUpdate(String params) throws Exception;

	/**
	 * 账户撤销校验接口
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("closeaccountverify")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountCloseMsgRespVo closeAccount(String params) throws Exception;

}
