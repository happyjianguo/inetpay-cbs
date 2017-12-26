package com.ylink.inetpay.cbs.chl.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.CbsChlFrontedSessionService;
import com.ylink.inetpay.common.project.cbs.app.CbsChlFrontedSessionAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlFrontendSession;

@Service("cbsChlFrontedSessionAppService")
public class CbsChlFrontedSessionAppServiceImpl implements CbsChlFrontedSessionAppService {

	private static Logger _loger = LoggerFactory.getLogger(CbsChlFrontedSessionAppServiceImpl.class);
	@Autowired
	CbsChlFrontedSessionService cbsChlFrontedSessionService;

	@Override
	public PageData<TbChlFrontendSession> queryAllData(PageData<TbChlFrontendSession> pageDate,
			TbChlFrontendSession TbChlFrontendSession) {
		return cbsChlFrontedSessionService.queryAllData(pageDate, TbChlFrontendSession);
	}

	@Override
	public TbChlFrontendSession selectByBusiId(String busiId) {
		_loger.info("查询订单信息:" + busiId);
		return cbsChlFrontedSessionService.selectByBusiId(busiId);
	}

}
