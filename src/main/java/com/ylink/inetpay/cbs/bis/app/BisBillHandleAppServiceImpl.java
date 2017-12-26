package com.ylink.inetpay.cbs.bis.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisBillHandleService;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.project.cbs.app.BisBillHandleAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBillHandleDto;

@Service("bisBillHandleAppService")
public class BisBillHandleAppServiceImpl implements BisBillHandleAppService {
	@Autowired
	BisBillHandleService actBillHandleService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);

	@Override
	public PageData<BisBillHandleDto> getByCond(BisBillHandleDto actBillHandleDto,
			PageData<BisBillHandleDto> pageData) {
		return actBillHandleService.getByCond(actBillHandleDto, pageData);
	}

	@Override
	public void insert(BisBillHandleDto actBillHandleDto) {
		try {
			actBillHandleService.insert(actBillHandleDto);
		} catch (Exception e) {
			_log.error("新增手工记账经办异常："+e);
		}
		
	}

	@Override
	public void update(BisBillHandleDto actBillHandleDto) {
		try {
			actBillHandleService.update(actBillHandleDto);
		} catch (Exception e) {
			_log.error("修改手工记账经办异常："+e);
		}
		
	}

	@Override
	public BisBillHandleDto details(String id) {
		return actBillHandleService.details(id);
	}

	@Override
	public boolean auditBill(BisBillHandleDto bisBillHandleDto, BisAuditDto bisAuditDto) {
		return actBillHandleService.auditBill(bisBillHandleDto, bisAuditDto);
	}

	@Override
	public BisBillHandleDto findByOrderAndPayAdjustType(String tradeId, String id) {
		return actBillHandleService.findByOrderAndPayAdjustType(tradeId, id);
	}

}
