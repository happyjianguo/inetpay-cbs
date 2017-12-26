package com.ylink.inetpay.cbs.chl.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.chl.service.ChlPaymentService;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.channel.app.ChlPaymentAppService;
import com.ylink.inetpay.common.project.channel.dto.TbChlPayOrderDto;
@Service("chlPaymentAppService")
public class ChlPaymentAppServiceImpl implements ChlPaymentAppService {

	@Autowired
	private ChlPaymentService chlPaymentService;
	
	/**查看支付订单详情**/
	@Override
	public TbChlPayOrderDto details(String id) throws CbsCheckedException {
		return chlPaymentService.details(id);
	}

	/**分页查询支付订单信息**/
	@Override
	public PageData<TbChlPayOrderDto> findListPage(PageData<TbChlPayOrderDto> pageDate, TbChlPayOrderDto payOrderDto)
			throws CbsCheckedException {
		return chlPaymentService.findListPage(pageDate, payOrderDto);
	}
	

}
