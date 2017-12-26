package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisMovingActMatchingAuditService;
import com.ylink.inetpay.common.project.cbs.app.BisMovingActMatchingAuditAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisMovingActMatchingAuditDto;


@Service("bisMovingActMatchingAuditAppService")
public class BisMovingActMatchingAuditAppServiceImpl implements BisMovingActMatchingAuditAppService {
	 	@Autowired
	 	private BisMovingActMatchingAuditService bisMovingActMatchingAuditService;

		@Override
		public PageData<BisMovingActMatchingAuditDto> queryAll(PageData<BisMovingActMatchingAuditDto> pageData,BisMovingActMatchingAuditDto queryParam) {
			return bisMovingActMatchingAuditService.queryAll(pageData,queryParam);
		}

		@Override
		public BisMovingActMatchingAuditDto detail(String id) {
			return bisMovingActMatchingAuditService.detail(id);
		}

		@Override
		public void update(BisMovingActMatchingAuditDto queryParam) {
			bisMovingActMatchingAuditService.update(queryParam);
		}

		@Override
		public List<BisMovingActMatchingAuditDto> findAllByBusIdAndStatus(String id) {
			
			return bisMovingActMatchingAuditService.findAllByBusIdAndStatus(id);
		}

		@Override
		public long movingAccountMatchingAudit(BisMovingActMatchingAuditDto dto) {
			return bisMovingActMatchingAuditService.movingAccountMatchingAudit(dto);
		}

		@Override
		public void save(BisMovingActMatchingAuditDto dto) {
			bisMovingActMatchingAuditService.save(dto);
			
		}
}
