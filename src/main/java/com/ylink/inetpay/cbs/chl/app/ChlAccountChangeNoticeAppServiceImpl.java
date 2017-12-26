package com.ylink.inetpay.cbs.chl.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlAccountChangeNotice;
import com.ylink.inetpay.common.project.channel.app.ChlAccountChangeNoticeAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlAccountChangeNotice;
@Service("chlAccountChangeNoticeAppService")
public class ChlAccountChangeNoticeAppServiceImpl implements ChlAccountChangeNoticeAppService {
	
	@Autowired
	ChlAccountChangeNotice chlAccountChangeNotice;
	public PageData<TbChlAccountChangeNotice> findAll(PageData<TbChlAccountChangeNotice> pageData,
			TbChlAccountChangeNotice tbChlAccountChangeNotice) {
		return chlAccountChangeNotice.findAll(pageData, tbChlAccountChangeNotice);
	}
	@Override
	public TbChlAccountChangeNotice detail(String id) {
		
		return chlAccountChangeNotice.detail(id);
	}
}
