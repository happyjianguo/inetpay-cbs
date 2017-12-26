package com.ylink.inetpay.cbs.bis.service;

import com.ylink.inetpay.common.core.constant.EBisEmailChectStatus;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
/**
 * 邮件验证服务类
 * @author haha
 *
 */
public interface BisCheckEmailService {
	
	
	/**
	 * 修改同类型邮件状态
	 * @param operType
	 * @param checkStatus
	 * @param custId
	 */
	public void updateEmailStatus(EBisEmailTemplateCode operType,
			EBisEmailChectStatus checkStatus, String custId);

	/**
	 * 邮件验证
	 * @param checkMessage
	 * @return
	 * @throws CbsCheckedException 
	 * @throws PortalCheckedException 
	 */
	public MrsLoginUserDto shieCheckEmail(String checkMessage, String id, EBisEmailTemplateCode template) ;
	
}
