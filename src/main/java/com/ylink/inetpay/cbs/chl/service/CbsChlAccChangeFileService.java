package com.ylink.inetpay.cbs.chl.service;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.project.channel.dto.TbChlAccChangeFileRecord;

public interface CbsChlAccChangeFileService {
	/**
	 * 查询银行前置会话数据
	 * 
	 * @param pageDate
	 * @param TbChlAccChangeFileRecord
	 * @return
	 */
	PageData<TbChlAccChangeFileRecord> queryAllData(PageData<TbChlAccChangeFileRecord> pageDate,
			TbChlAccChangeFileRecord TbChlAccChangeFileRecord);

	/**
	 * 根据银行前置会话ID查询
	 * 
	 * @param id
	 * @return
	 */
	TbChlAccChangeFileRecord selectByBusiId(String id);
}
