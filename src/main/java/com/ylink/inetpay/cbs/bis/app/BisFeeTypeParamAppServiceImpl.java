package com.ylink.inetpay.cbs.bis.app;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisFeeTypeParamService;
import com.ylink.inetpay.common.project.cbs.app.BisFeeTypeParamAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisFeeTypeParamDto;
 

@Service("bisFeeTypeParamAppService")
public class BisFeeTypeParamAppServiceImpl implements  BisFeeTypeParamAppService {
	@Autowired
	private BisFeeTypeParamService bisFeeTypeParamService;
	
	@Override
	public PageData<BisFeeTypeParamDto> pageList(PageData<BisFeeTypeParamDto> pageData, BisFeeTypeParamDto queryParam) {
		 
		return bisFeeTypeParamService.pageList(pageData,queryParam);
	}

	@Override
	public void saveBisFeeTypeParam(BisFeeTypeParamDto bisFeeTypeParamDto) {
		  bisFeeTypeParamService.saveBisFeeTypeParam(bisFeeTypeParamDto);
		
	}

	@Override
	public BisFeeTypeParamDto details(String viewId) {
		 
		return   bisFeeTypeParamService.details(viewId);
	}

	@Override
	public void updateAccessorDto(BisFeeTypeParamDto feeTypeParamDto) {
		 
		 bisFeeTypeParamService.updateAccessorDto(feeTypeParamDto);
	}
	
	
}
