package com.ylink.inetpay.cbs.chl.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.exception.BusinessRuntimeException;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.CbsChannelRouteService;
import com.ylink.inetpay.common.project.cbs.app.CbsChannelRouteAppService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.ChlChannelRouteAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlChannelRoute;
import com.ylink.inetpay.common.project.channel.exception.ChannelCheckedException;
/** 渠道银行表**/
@Service("cbsChannelRouteAppService")
public class CbsChannelRouteAppServiceImpl implements CbsChannelRouteAppService {
	
	@Autowired
	private CbsChannelRouteService chlChannelRouteService;
	@Autowired
	private ChlChannelRouteAppService chlChannelRouteAppService;
	

	@Override
	public PageData<TbChlChannelRoute> findListPage(PageData<TbChlChannelRoute> pageDate,
			TbChlChannelRoute tbChlChannelDto) {
		return chlChannelRouteService.findListPage(pageDate, tbChlChannelDto);
	}

	@Override
	public List<TbChlChannelRoute> findList(TbChlChannelRoute TbChlChannelBankDto) throws CbsCheckedException{
		return chlChannelRouteService.findListPage(TbChlChannelBankDto);
	}
	
	@Override
	public TbChlChannelRoute findById(String id) throws CbsCheckedException{
		return chlChannelRouteService.details(id);
	}

	@Override
	public void updateBank(TbChlChannelRoute tbChlBankDto)throws CbsCheckedException {
		try {
			chlChannelRouteAppService.updateChannelRout(tbChlBankDto);
		} catch (ChannelCheckedException e) {
			throw new BusinessRuntimeException("修改银行路由失败："+e.getMessage());
		}
	}
	
	public void updateRouteOrder(List<TbChlChannelRoute> tbChlBankDto)throws CbsCheckedException{
		try {
			chlChannelRouteAppService.updateRouteOrder(tbChlBankDto);
		} catch (ChannelCheckedException e) {
			throw new BusinessRuntimeException("配置银行路由顺序失败:"+e.getMessage());
		}
	}

	@Override
	public List<TbChlChannelRoute> findRouteByPayOreder(Long payOrder) {
		return chlChannelRouteService.findRouteByPayOreder(payOrder);
	}
}
