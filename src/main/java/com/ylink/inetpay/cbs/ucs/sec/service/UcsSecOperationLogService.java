package com.ylink.inetpay.cbs.ucs.sec.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecOperationLogDto;
public interface UcsSecOperationLogService {
	/**
	 * 操作日志查询
	 * @param pageDate
	 * @param uscSecOperationLogDto
	 * @return
	 */
	PageData<UcsSecOperationLogDto> list(PageData<UcsSecOperationLogDto> pageDate,UcsSecOperationLogDto uscSecOperationLogDto);
	/**
	 * 保存操作日志
	 * @param ucsSecOperationLogDto
	 * @return
	 */
	void insert(UcsSecOperationLogDto ucsSecOperationLogDto);
}
