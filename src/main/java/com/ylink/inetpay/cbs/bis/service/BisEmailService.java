package com.ylink.inetpay.cbs.bis.service;

import java.util.List;
import java.util.Map;

import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
/**
 * 邮件发送记录服务类
 * @author haha
 *
 */
public interface BisEmailService {
	/**
	 * 获取邮件发送记录列表
	 * @param pageDate
	 * @param bisSmsDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<BisEmailDto> findListPage(PageData<BisEmailDto> pageDate,BisEmailDto bisEmailDto);
	/**
	 * 获取邮件发送记录详情
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public BisEmailDto details(String id);
	/**
	 * 定时刷新邮件的状态，对非实时发送的短信进行发送操作。
	 * @throws CbsCheckedException
	 */
	public void flushEmail();
	/**
	 * 邮件发送接口
	 * @param email 邮箱
	 * @param emailSystem 邮件子系统
	 * @param params 邮件内容参数
	 * @param emailTemplateCode 邮件模板编码
	 * @param isCheck 是否需要验证
	 * @throws CbsCheckedException 异常
	 */
	public void sendEmail(String email,String id, EBisSmsSystem emailSystem,
			List<String> params, EBisEmailTemplateCode emailTemplateCode,
			String  checkMessage);
	
	/**
	 * 邮件发送接口
	 * @param email
	 * @param id
	 * @param emailSystem
	 * @param template
	 * @param params
	 * @throws CbsCheckedException 
	 * @throws PortalCheckedException 
	 */
	public UserCheckVO shieSendEmail(String email, String id, EBisSmsSystem emailSystem, EBisEmailTemplateCode template,
			Map<String, Object> params,String custId) ;
	/**
	 * 异常信息发送邮件
	 * @param email
	 * @param id
	 * @param emailSystem
	 * @param template
	 * @param params
	 * @param custId
	 * @return
	 */
	public UserCheckVO sendExceptionLogEmail(String email,EBisSmsSystem emailSystem, EBisEmailTemplateCode template,
			Map<String, Object> params);
}
