package com.ylink.inetpay.cbs.bis.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 监控日志服务类
 * @author haha
 *
 */
public interface BisExceptionLogService {
	/**
	 * 分页查询监控日志
	 * @param pageDate
	 * @param bisExceptionLogDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<BisExceptionLogDto> findListPage(PageData<BisExceptionLogDto> pageDate,BisExceptionLogDto bisExceptionLogDto);
	/**
	 * 保存监控日志
	 * @param bisExceptionLogDto
	 * @throws CbsCheckedException
	 */
	public void saveLog(BisExceptionLogDto bisExceptionLogDto);
	/**
	 * 监控日志详情
	 * @param id
	 * @return
	 * @throws CbsCheckedException
	 */
	public BisExceptionLogDto details(String id);
	/**
	 * 定时任务调用，发送一场日志给管理员
	 */
	public void sendExceptionLog();
}
