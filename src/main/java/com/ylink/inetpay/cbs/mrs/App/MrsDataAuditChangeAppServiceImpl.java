package com.ylink.inetpay.cbs.mrs.App;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.cbs.mrs.service.MrsDataAuditChangeService;
import com.ylink.inetpay.common.project.cbs.app.MrsDataAuditChangeAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataAuditChangeDto;

@Service("mrsDataAuditChangeAppService")
public class MrsDataAuditChangeAppServiceImpl implements MrsDataAuditChangeAppService {

	private static Logger log = LoggerFactory.getLogger(MrsDataAuditChangeAppServiceImpl.class);
	@Autowired
	private MrsDataAuditChangeService mrsDataAuditChangeService;
	
	@Override
	public MrsDataAuditChangeDto getMrsDataAuditChangeById(String id) {
		log.debug("MrsDataAuditChangeAppServiceImpl.getMrsDataAuditChangeById run....");
		return mrsDataAuditChangeService.getMrsDataAuditChangeById(id);
	}
	@Override
	public boolean checkAuditData(String refId) {
		log.debug("MrsDataAuditChangeAppServiceImpl.checkAuditData run....");
		return mrsDataAuditChangeService.checkAuditData(refId);
	}
	
}
