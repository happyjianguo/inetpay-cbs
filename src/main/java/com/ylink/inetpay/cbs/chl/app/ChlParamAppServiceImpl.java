package com.ylink.inetpay.cbs.chl.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlParamService;
import com.ylink.inetpay.common.project.cbs.app.ChlParamAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.dto.TbChlParamDto;
@Service("chlParamAppService")
public class ChlParamAppServiceImpl implements ChlParamAppService {
	@Autowired
	private ChlParamService chlParamService;
	@Override
	public PageData<TbChlParamDto> findListPage(PageData<TbChlParamDto> pageDate,
			TbChlParamDto chlParamDto) throws CbsCheckedException {
		return chlParamService.findListPage(pageDate, chlParamDto);
	}

	
	@Override
	public TbChlParamDto details(String channelParamId) {
		return chlParamService.details(channelParamId);
	}
	
	@Override
	public TbChlParamDto selectByChannelCodeAndParam(String channelCode,String paramCode){
		return chlParamService.selectByChannelCodeAndParam(channelCode, paramCode);
	}
	
	@Override
	public void updateChannelParam(TbChlParamDto tbChlParamDto)
			throws CbsCheckedException {
		chlParamService.updateChannelParam(tbChlParamDto);
	}


	@Override
	public List<TbChlParamDto> listByChannelCodeAndParam(List<String> channelCodeList, List<String> paramCodeList) {
		return chlParamService.listByChannelCodeAndParam(channelCodeList, paramCodeList);
	}
}
