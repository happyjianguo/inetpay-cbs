package com.ylink.inetpay.cbs.chl.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.CbsChlAccChangeFileService;
import com.ylink.inetpay.common.project.cbs.app.CbsChlAccChangeFileAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlAccChangeFileRecord;

@Service("cbsChlAccChangeFileAppService")
public class CbsChlAccChangeFileAppServiceImpl implements CbsChlAccChangeFileAppService {

	private static Logger _loger = LoggerFactory.getLogger(CbsChlAccChangeFileAppServiceImpl.class);
	@Autowired
	CbsChlAccChangeFileService cbsChlAccChangeFileService;

	@Override
	public PageData<TbChlAccChangeFileRecord> queryAllData(PageData<TbChlAccChangeFileRecord> pageDate,
			TbChlAccChangeFileRecord TbChlAccChangeFileRecord) {
		return cbsChlAccChangeFileService.queryAllData(pageDate, TbChlAccChangeFileRecord);
	}

	@Override
	public TbChlAccChangeFileRecord selectByBusiId(String busiId) {
		_loger.info("查询订单信息:" + busiId);
		return cbsChlAccChangeFileService.selectByBusiId(busiId);
	}

}
