package com.ylink.inetpay.cbs.mrs.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ylink.inetpay.common.project.portal.vo.AssignShieNoResponseVO;

/**
 * 账户平台配号接口:会员号配号 专用于各业务平台生成配号，根据一套三要素进行配号
 * 
 * @author yuqingjun
 *
 */
@Path("cust")
public interface MrsAssignShieNoService {

	/**
	 * 根据客户名称、证件类别、证件号码查询系统是否存在历史配号数据，若存在，返回客户号。
	 * 若系统不存在“历史”配号数据，系统根据规则生成新的客户号，并记录客户号与客户名称、
	 * 证件类别、证件号码、客户类型的关联关系。将客户号写入数据库，返回新生成客户号。
	 *
	 * @param 传入JsonParams
	 * @return 返回Json结果
	 * @throws Exception
	 */
	@POST
	@Path("assignShieNo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	AssignShieNoResponseVO assignShieNo(String params);
}
