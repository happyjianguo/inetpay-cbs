package com.ylink.inetpay.cbs.mrs.service;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsMerChannelIdDto;

public interface MrsMerChannelIdService {
	/**
	 * 根据一户通编号获取商户渠道id
	 * @param custId
	 * @return
	 */
	public String getMerChannelIdByCustId(String custId);
	/**
	 * 新增商户渠道id
	 * @param dto
	 * @return
	 */
	public int saveMerChannelId(MrsMerChannelIdDto dto);
	/**
	 * 修改商户渠道id
	 * @param dto
	 * @return
	 */
	public int updateMerChannelId(MrsMerChannelIdDto dto);
}
