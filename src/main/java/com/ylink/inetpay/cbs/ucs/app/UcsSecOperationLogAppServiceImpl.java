package com.ylink.inetpay.cbs.ucs.app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import com.ylink.inetpay.cbs.ucs.sec.service.UcsSecOperationLogService;
import com.ylink.inetpay.common.project.cbs.app.UcsSecOperationLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecOperationLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
@Service("ucsSecOpertionLogAppService")
public class UcsSecOperationLogAppServiceImpl implements UcsSecOperationLogAppService {
	@Autowired
	UcsSecOperationLogService ucsSecOperationLogService;
	@Override
	public PageData<UcsSecOperationLogDto> list(
			PageData<UcsSecOperationLogDto> pageDate,
			UcsSecOperationLogDto ucsSecOperationLogDto) throws CbsCheckedException {
		return ucsSecOperationLogService.list(pageDate, ucsSecOperationLogDto);
	}
	@Override
	public void insert(UcsSecOperationLogDto ucsSecOperationLogDto) throws CbsCheckedException {
		ucsSecOperationLogService.insert(ucsSecOperationLogDto);
	}
}
