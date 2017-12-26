package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisAccessorService;
import com.ylink.inetpay.cbs.bis.service.BisTradeTypeParamService;
import com.ylink.inetpay.common.project.cbs.app.BisAccessorAppService;
import com.ylink.inetpay.common.project.cbs.app.BisTradeTypeParamAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccessorDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTradeTypeParamDto;

@Service("bisTradeTypeParamAppService")
public class BisTradeTypeParamAppServiceImpl implements BisTradeTypeParamAppService  {
	 @Autowired 
	 private BisTradeTypeParamService bisTradeTypeParamService;

	@Override
	public PageData<BisTradeTypeParamDto> pageList(PageData<BisTradeTypeParamDto> pageData, BisTradeTypeParamDto queryParam) {
	 
		return bisTradeTypeParamService.pageList(pageData,queryParam);
	}

	@Override
	public void insertBisTradeTypeParam(BisTradeTypeParamDto bisTradeTypeParamDto) {
		bisTradeTypeParamService.insertBisTradeTypeParam(bisTradeTypeParamDto);
	}

	@Override
	public BisTradeTypeParamDto detail(String viewId) {
		 
		return bisTradeTypeParamService.detail(viewId);
	}

	@Override
	public void update(BisTradeTypeParamDto bisTradeTypeParamDto) {
		  bisTradeTypeParamService.update(bisTradeTypeParamDto);
		
	}

	@Override
	public List<BisTradeTypeParamDto> findAllTradeType() {
		return bisTradeTypeParamService.findAllTradeType();
	}
}
