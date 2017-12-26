package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsNotifyDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;

public interface MrsNotifyService {

	/**
	 * 同步个人信息
	 * @throws CbsCheckedException 
	 */
	public void syncPersonInfo(MrsNotifyDto dto) throws CbsCheckedException;
	
	/**
	 * 同步机构信息
	 * @throws CbsCheckedException 
	 */
	public void syncOrganInfo(MrsNotifyDto dto) throws CbsCheckedException;
	/**
	 * 同步产品信息
	 * @throws CbsCheckedException 
	 */
	public void syncProductInfo(MrsNotifyDto dto) throws CbsCheckedException;
	/**
	 * 同步文件信息
	 * @throws CbsCheckedException 
	 */
	public void syncFileInfo(MrsNotifyDto dto) throws CbsCheckedException;
	
	public List<MrsNotifyDto> findByStatusAndMaxNotifyNum(List<String> statusList, Integer maxNotifyNum); 
}
