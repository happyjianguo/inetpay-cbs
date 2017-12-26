package com.ylink.inetpay.cbs.bis.app;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisTransferHandleService;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.project.cbs.app.BisTransferHandleAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisTransferHandleDto;
@Service("bisTransferHandleAppService")
public class BisTransferHandleAppServiceImpl implements BisTransferHandleAppService{
	@Autowired
	BisTransferHandleService bisTransferHandleService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);
	@Override
	public PageData<BisTransferHandleDto> getByCond(BisTransferHandleDto bisTransferHandleDto, PageData<BisTransferHandleDto> pageData) {
		return bisTransferHandleService.getByCond(bisTransferHandleDto, pageData);
	}
	
	@Override
	public PageData<BisTransferHandleDto> getByConds(BisTransferHandleDto bisTransferHandleDto, PageData<BisTransferHandleDto> pageData,String loginName) {
		return bisTransferHandleService.getByConds(bisTransferHandleDto, pageData,loginName);
	}

	@Override
	public void insert(BisTransferHandleDto bisTransferHandleDto) {
		try {
			bisTransferHandleService.insert(bisTransferHandleDto);
		} catch (Exception e) {
			_log.error("新增头寸调拨经办异常："+e);
		}
	}

	@Override
	public void update(BisTransferHandleDto bisTransferHandleDto) {
		try {
			bisTransferHandleService.update(bisTransferHandleDto);
		} catch (Exception e) {
			_log.error("修改头寸调拨经办异常："+e);
		}
	}

	@Override
	public BisTransferHandleDto details(String id) {
		return bisTransferHandleService.details(id);
	}

	@Override
	public void updateAll(BisTransferHandleDto bisTransferHandleDto) {
		try {
			bisTransferHandleService.updateAll(bisTransferHandleDto);
		} catch (Exception e) {
			_log.error("修改头寸调拨经办异常："+e);
		}
		
	}

	@Override
	public BisTransferHandleDto getById(String id) {
		return bisTransferHandleService.getById(id);
	}

	@Override
	public boolean auditTransfer(BisTransferHandleDto bisTransferHandleDto, BisAuditDto bisAuditDto) {
		return bisTransferHandleService.auditTransfer(bisTransferHandleDto, bisAuditDto);
	}

	@Override
	public BisTransferHandleDto queryCashTransfer(String tradeId, String id) {
		return bisTransferHandleService.queryCashTransfer(tradeId,id);
	}
	}
