package com.ylink.inetpay.cbs.bis.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.EBisBusinessType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBillingTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
/**
 * 个人计费模板服务类
 * @author haha
 *
 */
public interface BisBillingTemplateService {
	/**
	 * 个人计费模板管理列表
	 * @param pageDate
	 * @param bisBillingTemplateAppService
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<BisBillingTemplateDto> findListPage(PageData<BisBillingTemplateDto>  pageDate,BisBillingTemplateDto bisBillingTemplateDto);
	/**
	 * 个人计费模板修改
	 * @param bisBillingTemplateAppService
	 * @throws CbsCheckedException
	 */
	public void update(BisBillingTemplateDto bisBillingTemplateDto)throws CbsCheckedException;
	/**
	 * 个人计费模板详情
	 * @throws CbsCheckedException
	 */
	public BisBillingTemplateDto details(String id)throws CbsCheckedException;
	/**
	 * 根据交易类型查找计费模板并计费,单位为分
	 * @param businessType
	 * @return
	 */
	public Long compuOderFee(EBisBusinessType businessType,long orderAmt);
}
