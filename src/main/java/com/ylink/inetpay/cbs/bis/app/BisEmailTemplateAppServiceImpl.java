package com.ylink.inetpay.cbs.bis.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.bis.service.BisEmailTemplateService;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.project.cbs.app.BisEmailTemplateAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
@Service("bisEmailTemplateAppService")
public class BisEmailTemplateAppServiceImpl implements
		BisEmailTemplateAppService {
	@Autowired
	private BisEmailTemplateService bisEmailTemplateService;
	@Override
	public PageData<BisEmailTemplateDto> findListPage(
			PageData<BisEmailTemplateDto> pageDate,
			BisEmailTemplateDto bisEmailTemplateDto) throws CbsCheckedException {
		return bisEmailTemplateService.findListPage(pageDate, bisEmailTemplateDto);
	}

	@Override
	public BisEmailTemplateDto details(String id) throws CbsCheckedException {
		return bisEmailTemplateService.details(id);
	}

	@Override
	public void update(BisEmailTemplateDto bisEmailTemplateDto)
			throws CbsCheckedException {
		bisEmailTemplateService.update(bisEmailTemplateDto);
	}

	@Override
	public BisEmailTemplateDto getEmailTempla(EBisEmailTemplateCode templateCode)
			throws CbsCheckedException {
		return bisEmailTemplateService.getEmailTempla(templateCode);
	}

}
