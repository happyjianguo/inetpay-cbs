package com.ylink.inetpay.cbs.mrs.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOperationLogDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

/**
 * 会员操作日志服务类
 * @author haha
 *
 */
public interface MrsOperationLogService {
	/**
	 * 分页查询会员用户操作日志
	 * @param pagDate
	 * @param mrsOperationLogDto
	 * @return
	 * @throws CbsCheckedException
	 */
	public PageData<MrsOperationLogDto> pageList(PageData<MrsOperationLogDto> pagDate,MrsOperationLogDto mrsOperationLogDto);
	/**
	 * 保存会员用户操作日志
	 * @param mrsOperationLogDto
	 * @throws CbsCheckedException
	 */
	public void add(MrsOperationLogDto mrsOperationLogDto);
}
