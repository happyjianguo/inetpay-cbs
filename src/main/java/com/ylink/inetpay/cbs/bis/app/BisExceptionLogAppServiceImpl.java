package com.ylink.inetpay.cbs.bis.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.cbs.bis.service.BisExceptionLogService;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
@Service("bisExceptionLogAppService")
public class BisExceptionLogAppServiceImpl implements BisExceptionLogAppService  {
	@Autowired
	private BisExceptionLogService bisExceptionLogService;
	
	@Autowired
	@Qualifier("logTaskExecutor")
	private TaskExecutor taskExecutor;
	
	@Override
	public BisExceptionLogDto details(String id) throws CbsCheckedException {
		return bisExceptionLogService.details(id);
	}

	@Override
	public PageData<BisExceptionLogDto> findListPage(
			PageData<BisExceptionLogDto> pageDate, BisExceptionLogDto bisExceptionLogDto)
			throws CbsCheckedException {
		return bisExceptionLogService.findListPage(pageDate, bisExceptionLogDto);
	}

	/**
	 * 开启新的线程保存日志信息，不影响主线程的业务执行和性能
	 */
	@Override
	public void saveLog(final BisExceptionLogDto bisExceptionLogDto) throws CbsCheckedException {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				bisExceptionLogService.saveLog(bisExceptionLogDto);	
			}
		});
	}

}
