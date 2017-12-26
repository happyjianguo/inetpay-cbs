package com.ylink.inetpay.cbs.mrs.service;

import com.ylink.inetpay.common.project.portal.vo.sync.SyncFileRespVO;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncProductRespVO;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncRespVO;

/**
 * 同步客户信息
 * @author pst11
 */
public interface SyncCustomerService {

	/**
	 * 同步个人信息
	 * @param json 请求json串
	 * @return
	 */
	public SyncRespVO syncPerson(String json);
	
	/**
	 * 同步机构信息
	 * @param json 请求json串
	 * @return
	 */
	public SyncRespVO syncOrgan(String json);
	/**
	 * 同步产品信息
	 * @param json 请求json串
	 * @return
	 */
	public SyncProductRespVO syncProduct(String json);
	

	/**
	 * 同步文件信息
	 * @param json 请求json串
	 * @return
	 */
	public SyncFileRespVO syncFile(String json);
}
