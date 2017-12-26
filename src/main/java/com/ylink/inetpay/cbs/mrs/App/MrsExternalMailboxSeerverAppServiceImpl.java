package com.ylink.inetpay.cbs.mrs.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsExternalMailboxServerService;
import com.ylink.inetpay.common.project.cbs.app.MrsExternalMailboxServerAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsExternalMailboxServerDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
@Service("mrsExternalMailboxServerAppService")
public class MrsExternalMailboxSeerverAppServiceImpl implements
		MrsExternalMailboxServerAppService {
	@Autowired
	private MrsExternalMailboxServerService mrsExternalMailboxServerService;
	@Override
	public PageData<MrsExternalMailboxServerDto> findListPage(
			PageData<MrsExternalMailboxServerDto> pageData,
			MrsExternalMailboxServerDto mrsExternalMailboxServerDto)
			throws CbsCheckedException {
		return mrsExternalMailboxServerService.findListPage(pageData, mrsExternalMailboxServerDto);
	}

	@Override
	public void insert(MrsExternalMailboxServerDto mrsExternalMailboxServerDto)
			throws CbsCheckedException {
		mrsExternalMailboxServerService.insert(mrsExternalMailboxServerDto);
	}

	@Override
	public void update(MrsExternalMailboxServerDto mrsExternalMailboxServerDto)
			throws CbsCheckedException {
		mrsExternalMailboxServerService.update(mrsExternalMailboxServerDto);
	}

	@Override
	public void delete(String id) throws CbsCheckedException {
		mrsExternalMailboxServerService.delete(id);
	}

	@Override
	public MrsExternalMailboxServerDto details(String id)
			throws CbsCheckedException {
		return mrsExternalMailboxServerService.details(id);
	}

	@Override
	public String getServerPath(String SUFFI) throws CbsCheckedException {
		return mrsExternalMailboxServerService.getServerPath(SUFFI);
	}

}
