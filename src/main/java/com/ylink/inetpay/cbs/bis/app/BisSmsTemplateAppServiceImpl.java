package com.ylink.inetpay.cbs.bis.app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.bis.service.BisSmsTemplateService;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.project.cbs.app.BisSmsTemplateAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsTemplateDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
@Service("bisSmsTemplateAppService")
public class BisSmsTemplateAppServiceImpl implements BisSmsTemplateAppService {
	@Autowired
	private BisSmsTemplateService bisSmsTemplateService;
	@Override
	public PageData<BisSmsTemplateDto> findListPage(
			PageData<BisSmsTemplateDto> pageDate,
			BisSmsTemplateDto bisSmsTemplateDto) throws CbsCheckedException {
		return bisSmsTemplateService.findListPage(pageDate, bisSmsTemplateDto);
	}

	@Override
	public BisSmsTemplateDto details(String id) throws CbsCheckedException {
		return bisSmsTemplateService.details(id);
	}

	@Override
	public void update(BisSmsTemplateDto bisSmsTemplateDto)
			throws CbsCheckedException {
		bisSmsTemplateService.update(bisSmsTemplateDto);		
	}

	@Override
	public BisSmsTemplateDto getSmsTempla(EBisTemplateCode templateCode)
			throws CbsCheckedException {
		return bisSmsTemplateService.getSmsTempla(templateCode);
	}

}
