package com.ylink.inetpay.cbs.bis.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisBillingTemplateService;
import com.ylink.inetpay.common.core.constant.EBisBusinessType;
import com.ylink.inetpay.common.project.cbs.app.BisBillingTemplateAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBillingTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisBillingTemplateAppService")
public class BisBillingTemplateAppServiceImpl implements
		BisBillingTemplateAppService {
	@Autowired
	private BisBillingTemplateService bisBillingTemplateService;
	@Override
	public PageData<BisBillingTemplateDto> findListPage(
			PageData<BisBillingTemplateDto> pageDate,
			BisBillingTemplateDto bisBillingTemplateDto)
			throws CbsCheckedException {
		return bisBillingTemplateService.findListPage(pageDate, bisBillingTemplateDto);
	}

	@Override
	public void update(BisBillingTemplateDto bisBillingTemplateDto)
			throws CbsCheckedException {
		bisBillingTemplateService.update(bisBillingTemplateDto);
	}

	@Override
	public BisBillingTemplateDto details(String id)
			throws CbsCheckedException {
		return bisBillingTemplateService.details(id);
	}

	@Override
	public long compuOderFee(EBisBusinessType businessType,long orderAmt)
			throws CbsCheckedException {
		Long amt= bisBillingTemplateService.compuOderFee(businessType,orderAmt);
		if(amt==null)
		{
			return 0;
		}
		return amt.longValue();
	}
}
