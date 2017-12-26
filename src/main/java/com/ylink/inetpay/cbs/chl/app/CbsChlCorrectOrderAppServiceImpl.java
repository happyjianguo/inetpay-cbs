package com.ylink.inetpay.cbs.chl.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.CbsChlCorrectOrderService;
import com.ylink.inetpay.common.project.cbs.app.CbsChlCorrectOrderAppService;
import com.ylink.inetpay.common.project.channel.dto.ChlCorrectOrderDto;

@Service("cbsChlCorrectOrderAppService")
public class CbsChlCorrectOrderAppServiceImpl implements CbsChlCorrectOrderAppService {

	private static Logger _loger = LoggerFactory.getLogger(CbsChlCorrectOrderAppServiceImpl.class);
	@Autowired
	CbsChlCorrectOrderService cbsChlCorrectOrderService;

	@Override
	public PageData<ChlCorrectOrderDto> queryAllData(PageData<ChlCorrectOrderDto> pageDate,
			ChlCorrectOrderDto chlCorrectOrderDto) {
		return cbsChlCorrectOrderService.queryAllData(pageDate, chlCorrectOrderDto);
	}

	@Override
	public ChlCorrectOrderDto selectById(String id) {
		_loger.info("查询冲正订单信息:" + id);
		return cbsChlCorrectOrderService.selectById(id);
	}

}
