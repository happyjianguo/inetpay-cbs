package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;

public interface MrsCertFileService {

	public void insert(MrsCertFileDto dto);
	/**
	 * 
	 *方法描述：根据一户通编号查询一户通附件信息
	 * 创建人：ydx
	 * 创建时间：2017年2月20日 下午10:23:19
	 * @param custId
	 * @return
	 */
	public List<MrsCertFileDto> findByCustId(String custId);
}
