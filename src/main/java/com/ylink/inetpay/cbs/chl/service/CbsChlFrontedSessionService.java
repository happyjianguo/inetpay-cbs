package com.ylink.inetpay.cbs.chl.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.channel.dto.TbChlFrontendSession;

public interface CbsChlFrontedSessionService {
	/**
	 * 查询银行前置会话数据
	 * 
	 * @param pageDate
	 * @param TbChlFrontendSession
	 * @return
	 */
	PageData<TbChlFrontendSession> queryAllData(PageData<TbChlFrontendSession> pageDate,
			TbChlFrontendSession TbChlFrontendSession);

	/**
	 * 根据银行前置会话ID查询
	 * 
	 * @param id
	 * @return
	 */
	TbChlFrontendSession selectByBusiId(String id);
}
