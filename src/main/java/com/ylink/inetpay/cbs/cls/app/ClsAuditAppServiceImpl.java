package com.ylink.inetpay.cbs.cls.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.cls.service.ClsAuditService;
import com.ylink.inetpay.common.project.cbs.app.ClsAuditAppService;
import com.ylink.inetpay.common.project.clear.dto.ClsAuditDto;

@Service("clsAuditAppService")
public class ClsAuditAppServiceImpl implements ClsAuditAppService {
	@Autowired
	private ClsAuditService clsAuditService;
	private static Logger _log = LoggerFactory.getLogger(ClsAuditAppServiceImpl.class);

	@Override
	public List<ClsAuditDto> getByCond(String busId) {
		return clsAuditService.getByCond(busId);
	}

	@Override
	public void insert(ClsAuditDto bisAuditDto) {
		try {
			clsAuditService.insert(bisAuditDto);
		} catch (Exception e) {
			_log.error("新增复核异常：" + e);
		}

	}

	@Override
	public ClsAuditDto getAudit(String id) {
		return clsAuditService.getAudit(id);
	}

	@Override
	public PageData<ClsAuditDto> findListPage(PageData<ClsAuditDto> pageData, ClsAuditDto queryParam) {
		return clsAuditService.findListPage(pageData,queryParam);
	}

	@Override
	public boolean isAudit(String id, String loginName) {
		return clsAuditService.isAudit(id, loginName);
	}

}
