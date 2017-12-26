package com.ylink.inetpay.cbs.chl.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlRefundService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.ChlRefundAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlRefundOrderDto;
@Service("chlRefundAppService")
public class ChlRefundAppServiceImpl implements ChlRefundAppService {

	@Autowired
	private ChlRefundService chlRefundService;
	
	/**根据ID查看退款详情**/
	@Override
	public TbChlRefundOrderDto details(String id) throws CbsCheckedException {
		return chlRefundService.details(id);
	}

	/**分页查询退款信息**/
	@Override
	public PageData<TbChlRefundOrderDto> findListPage(PageData<TbChlRefundOrderDto> pageDate,
			TbChlRefundOrderDto refundOrderDto) throws CbsCheckedException {
		return chlRefundService.findListPage(pageDate, refundOrderDto);
	}
	
}
