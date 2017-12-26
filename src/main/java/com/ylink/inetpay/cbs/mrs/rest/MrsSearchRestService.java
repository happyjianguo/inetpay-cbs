package com.ylink.inetpay.cbs.mrs.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchResponseVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.AccountSearchRespVO;

/**
 * 
 * @author pst11
 *
 */
@Path("mrssearch")
public interface MrsSearchRestService {

	/**
	 * 只支持个人客户和机构客户的信息查询
	 * 	前提：只查询未生效的客户信息
	 * 	 规则：如果机构客户存在多条未生效的信息，则只返回最新的一个登陆客户的登陆信息
	 * 入参：一户通code、三要素、类型
	 * 出参：List<密码|一户通ID>
	 */
	@POST
	@Path("loginmsg")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	LoginMsgSearchResponseVO loginMsgSearch(String params) throws Exception;
	
	@POST
	@Path("account")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AccountSearchRespVO accountSearch(String params) throws Exception;
}
