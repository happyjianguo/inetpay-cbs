package com.ylink.inetpay.cbs.mrs.service;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;

public interface MrsPortalReviewAduitService {
	
	/**
	 * 根据业务编号和类型查找
	 * @param busiNo
	 * @param busiType
	 * @return
	 */
	public MrsPortalReviewAduitDto findByBusiNoAndType(String busiNo,String busiType);
	/**
	 * 根据业务编号和类型查找(待审核)
	 * @param busiNo
	 * @param busiType
	 * @return
	 */
	public MrsPortalReviewAduitDto findByBusiNoAndTypeWait(String busiNo,String busiType);

}
