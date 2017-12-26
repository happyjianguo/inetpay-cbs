package com.ylink.inetpay.cbs.bis.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
/**
 * 邮件模板服务类
 * @author haha
 *
 */
public interface BisEmailTemplateService {
	/**
	 * 获取邮件模板列表
	 * @param pageDate
	 * @param bisSmsTemplateDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<BisEmailTemplateDto> findListPage(PageData<BisEmailTemplateDto> pageDate,BisEmailTemplateDto bisEmailTemplateDto);
	/**
	 * 获取模板详情
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public BisEmailTemplateDto details(String id);
	/**
	 * 修改邮件模板
	 * @param bisSmsTemplateDto
	 * @throws CbsCheckedException
	 */
	public void update(BisEmailTemplateDto bisEmailTemplateDto);
	/**
	 * 根据业务类型（编码）获取邮件模板
	 * @return
	 * @throws CbsCheckedException
	 */
	public BisEmailTemplateDto getEmailTempla(EBisEmailTemplateCode templateCode);
}
