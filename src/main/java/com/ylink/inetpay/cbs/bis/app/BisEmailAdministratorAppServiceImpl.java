package com.ylink.inetpay.cbs.bis.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisEmailAdministratorService;
import com.ylink.inetpay.common.project.cbs.app.BisEmailAdministratorAppService;
import com.ylink.inetpay.common.project.cbs.constant.bis.EMessageType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailAdministratorDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

@Service("bisEmailAdministratorAppService")
public class BisEmailAdministratorAppServiceImpl implements BisEmailAdministratorAppService {
	@Autowired
	private BisEmailAdministratorService bisEmailAdministratorService;
	@Override
	public PageData<BisEmailAdministratorDto> pageList(PageData<BisEmailAdministratorDto> pageData,
			BisEmailAdministratorDto queryParam) {
		return bisEmailAdministratorService.pageList(pageData, queryParam);
	}

	@Override
	public long save(BisEmailAdministratorDto dto) throws CbsCheckedException {
		return bisEmailAdministratorService.save(dto);
	}

	@Override
	public BisEmailAdministratorDto view(String id) {
		return bisEmailAdministratorService.view(id);
	}

	@Override
	public long delete(String id) throws CbsCheckedException {
		return bisEmailAdministratorService.delete(id);
	}

	@Override
	public long update(BisEmailAdministratorDto dto) throws CbsCheckedException {
		return bisEmailAdministratorService.update(dto);
	}

	@Override
	public long updateByMessageType(EMessageType messageType, List<BisEmailAdministratorDto> dtos)
			throws CbsCheckedException {
		return bisEmailAdministratorService.updateByMessageType(messageType, dtos);
	}

	@Override
	public long batchSaveByMessageType(EMessageType messageType, List<BisEmailAdministratorDto> dtos)
			throws CbsCheckedException {
		return bisEmailAdministratorService.batchSaveByMessageType(messageType, dtos);
	}

	@Override
	public long deleteByMessageType(EMessageType messageType) throws CbsCheckedException {
		return bisEmailAdministratorService.deleteByMessageType(messageType);
	}

	@Override
	public List<BisEmailAdministratorDto> viewByMessageType(EMessageType messageType) {
		return bisEmailAdministratorService.viewByMessageType(messageType);
	}
}
