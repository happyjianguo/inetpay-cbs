package com.ylink.inetpay.cbs.bis.service;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
/**
 * 短息模板服务类
 * @author haha
 *
 */
public interface BisSmsTemplateService {
	/**
	 * 获取短信模板列表
	 * @param pageDate
	 * @param bisSmsTemplateDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<BisSmsTemplateDto> findListPage(PageData<BisSmsTemplateDto> pageDate,BisSmsTemplateDto bisSmsTemplateDto);
	/**
	 * 获取模板详情
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public BisSmsTemplateDto details(String id);
	/**
	 * 修改短信模板
	 * @param bisSmsTemplateDto
	 * @throws CbsCheckedException
	 */
	public void update(BisSmsTemplateDto bisSmsTemplateDto);
	/**
	 * 根据业务类型（编码）获取短信模板
	 * @return
	 * @throws CbsCheckedException
	 */
	public BisSmsTemplateDto getSmsTempla(EBisTemplateCode templateCode);
}
