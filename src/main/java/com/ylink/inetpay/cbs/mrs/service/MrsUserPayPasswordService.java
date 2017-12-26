package com.ylink.inetpay.cbs.mrs.service;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordDto;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.safe.EditPayPwdVo;

public interface MrsUserPayPasswordService {
	
	public MrsUserPayPasswordDto findByCustId(String custId);
	
	/**
	 * 初始化密码
	 * @param Ip
	 * @param name
	 * @param record
	 */
	public RespCheckVO startPwd(String Ip,String name,MrsUserPayPasswordDto record);
	
	/**
	 * 修改密码
	 * @param custId
	 * @param vo
	 */
	public RespCheckVO editPayPwd(String name,String Ip,String custId, EditPayPwdVo vo);
	
	/**
	 * 重置密码
	 * @param record
	 */
	public RespCheckVO resetPwd(String Ip,String name,MrsUserPayPasswordDto record);

}
