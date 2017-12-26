package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisAuditService;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecUserServiceImpl;
import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.BISAuditType;
import com.ylink.inetpay.common.project.cbs.app.BisAuditAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisAuditDto;

@Service("bisAuditAppService")
public class BisAuditAppServiceImpl implements BisAuditAppService {
	@Autowired
	private BisAuditService bisAuditService;
	private static Logger _log = LoggerFactory.getLogger(UcsSecUserServiceImpl.class);

	@Override
	public List<BisAuditDto> getByCond(String busId) {
		return bisAuditService.getByCond(busId);
	}

	@Override
	public void insert(BisAuditDto bisAuditDto) {
		try {
			bisAuditService.insert(bisAuditDto);
		} catch (Exception e) {
			_log.error("新增复核异常：" + e);
		}

	}

	@Override
	public BisAuditDto getAudit(String id) {
		return bisAuditService.getAudit(id);
	}

	@Override
	public PageData<BisAuditDto> findListPage(PageData<BisAuditDto> pageData, BisAuditDto queryParam) {
		return bisAuditService.findListPage(pageData,queryParam);
	}

	@Override
	public boolean isAudit(String id, String loginName,BISAuditType auditType) {
		return bisAuditService.isAudit(id, loginName,auditType);
	}
	
	@Override
	public List<BisAuditDto> findBisAuditDtoByLoginName(String loginName,List<String> ids,BISAuditType auditType){
		return bisAuditService.findBisAuditDtoByLoginName(loginName,ids,auditType);
	}

	@Override
	public boolean isExistAucit(String id, String loginName, BISAuditType auditType) {
		return bisAuditService.isExistAucit(id,loginName,auditType);
	}

	@Override
	public long getAucitNum(List<String> ids, String loginName, BISAuditStatus auditStatus,BISAuditType auditType) {
		return bisAuditService.getAucitNum(ids, loginName, auditStatus,auditType);
	}

}
