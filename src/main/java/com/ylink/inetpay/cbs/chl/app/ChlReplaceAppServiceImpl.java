package com.ylink.inetpay.cbs.chl.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlReplaceService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.ChlReplaceAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlTransferOrder;
@Service("chlReplaceAppService")
public class ChlReplaceAppServiceImpl implements ChlReplaceAppService {

	@Autowired
	private ChlReplaceService chlReplaceService;
	
	/**根据Id查询代付详情**/
	@Override
	public TbChlTransferOrder details(String id) throws CbsCheckedException {
		return chlReplaceService.details(id);
	}

	/**分页查询代付订单列表**/
	@Override
	public PageData<TbChlTransferOrder> findListPage(PageData<TbChlTransferOrder> pageDate,
			TbChlTransferOrder tbDirectPayDto) throws CbsCheckedException {
		return chlReplaceService.findListPage(pageDate, tbDirectPayDto);
	}
	

}
