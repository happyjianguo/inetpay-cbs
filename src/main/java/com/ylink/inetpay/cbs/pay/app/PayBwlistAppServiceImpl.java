package com.ylink.inetpay.cbs.pay.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.pay.service.PayBwlistService;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.project.cbs.app.PayBwlistAppService;
import com.ylink.inetpay.common.project.pay.dto.PayBwlistDto;
import com.ylink.inetpay.common.project.pay.exception.PayCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
@Service("payBwlistAppService")
public class PayBwlistAppServiceImpl implements PayBwlistAppService {
	@Autowired
	private PayBwlistService payBwlistService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public PageData<PayBwlistDto> findListPage(PageData<PayBwlistDto> pageDate,
			PayBwlistDto payBwlistDto) throws CbsCheckedException {
		return payBwlistService.findListPage(pageDate, payBwlistDto);
	}

	@Override
	public void add(PayBwlistDto payBwlistDto)  throws CbsCheckedException {
		try {
		payBwlistService.add(payBwlistDto);
		} catch (Exception e) {
			_log.error("新增黑白名单："+ECbsErrorCode.ACCOUNT_DATE_ERROR.getDisplayName());
			throw new CbsCheckedException(ECbsErrorCode.PAY_SYS_ERROR.getValue(), ECbsErrorCode.PAY_SYS_ERROR.getDisplayName());
		}
	}

	@Override
	public void delete(String id,String custId)  throws CbsCheckedException {
		try {
		payBwlistService.delete(id,custId);
		} catch (Exception e) {
			_log.error("删除黑白名单："+ECbsErrorCode.ACCOUNT_DATE_ERROR.getDisplayName());
			throw new CbsCheckedException(ECbsErrorCode.PAY_SYS_ERROR.getValue(), ECbsErrorCode.PAY_SYS_ERROR.getDisplayName());
		}
	}

	@Override
	public PayBwlistDto details(String id){
		return payBwlistService.details(id);
	}
}
