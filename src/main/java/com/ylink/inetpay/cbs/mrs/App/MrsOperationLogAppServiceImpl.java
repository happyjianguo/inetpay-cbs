package com.ylink.inetpay.cbs.mrs.App;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.mrs.service.MrsOperationLogService;
import com.ylink.inetpay.common.project.cbs.app.MrsOperationLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOperationLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
@Service("mrsOperationLogAppService")
public class MrsOperationLogAppServiceImpl implements MrsOperationLogAppService {
	@Autowired
	private MrsOperationLogService mrsOperationLogService;
	@Override
	public PageData<MrsOperationLogDto> pageList(
			PageData<MrsOperationLogDto> pagDate,
			MrsOperationLogDto mrsOperationLogDto) throws CbsCheckedException {
		return mrsOperationLogService.pageList(pagDate, mrsOperationLogDto);
	}

	@Override
	public void add(MrsOperationLogDto mrsOperationLogDto)
			throws CbsCheckedException {
		mrsOperationLogService.add(mrsOperationLogDto);
	}

}
