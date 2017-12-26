package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisAccessorService;
import com.ylink.inetpay.common.project.cbs.app.BisAccessorAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAccessorDto;

@Service("bisAccessorAppService")
public class BisAccessorAppServiceImpl implements BisAccessorAppService {
	@Autowired
	private BisAccessorService bisAccessorService;
	@Override
	public PageData<BisAccessorDto> pageList(PageData<BisAccessorDto> pageData, BisAccessorDto queryParam) {
		 
		return bisAccessorService.pageList(pageData, queryParam);
	}
	@Override
	public void saveAccessor(BisAccessorDto queryParam) {
		bisAccessorService.saveAccessor(queryParam);
		
	}
	@Override
	public BisAccessorDto details(String viewId) {
		 
		return bisAccessorService.details(viewId);
	}
	@Override
	public void deleteBisAccessorDto(String viewId) {
		bisAccessorService.deleteBisAccessorDto(viewId);
		
	}
	@Override
	public void updateAccessorDto(BisAccessorDto accessorDto) {
		bisAccessorService.updateAccessorDto(accessorDto);
		
	}
	@Override
	public List<String> queryBisAccessorDtoCustId() {
		 
		return bisAccessorService.queryBisAccessorDtoCustId();
	}
	
	@Override
	public List<BisAccessorDto> list() {
		return bisAccessorService.list();
	}
	
}
