package com.ylink.inetpay.cbs.mrs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.cbs.util.HttpSendUtil;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncFileRespVO;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncProductRespVO;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncRespVO;

@Service("syncCustomerService")
public class SyncCustomerServiceImpl implements SyncCustomerService {

	private static Logger log = LoggerFactory.getLogger(SyncCustomerServiceImpl.class);
	
	private static String NOTIFY_SUCCESS = "0000";
	@Autowired
	private BisSysParamService bisSysParamService;
	
	@Override
	public SyncRespVO syncPerson(String json) {
		String url = bisSysParamService.getValue(SHIEConfigConstant.SYNC_ECIF_URL);
		String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.SYNC_IDV_SERVICE);
		String retJson = "";
		SyncRespVO resp = null;
		try {
			retJson = HttpSendUtil.sendPostJsonStr(json, url+serviceUrl);
		} catch (Exception e) {
			log.error("查询失败：", e);
			resp = new SyncRespVO();
			resp.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.SYNC_FAIL));
			return resp;
		}
		resp = new Gson().fromJson(retJson, SyncRespVO.class);

		if(NOTIFY_SUCCESS.equals(resp.getResultCode())) {
			resp.setUserCheckVo(new UserCheckVO(true));
		} else {
			log.error("查询失败：", resp.getResultMsg());
			resp.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.SYNC_FAIL));
		}
		return resp;
	}

	@Override
	public SyncRespVO syncOrgan(String json) {
		String url = bisSysParamService.getValue(SHIEConfigConstant.SYNC_ECIF_URL);
		String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.SYNC_UNIT_SERVICE);
		String retJson = "";
		SyncRespVO resp = null;
		try {
			retJson = HttpSendUtil.sendPostJsonStr(json, url+serviceUrl);
		} catch (Exception e) {
			log.error("查询失败：", e);
			resp = new SyncRespVO();
			resp.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.SYNC_FAIL));
			return resp;
		}
		resp = new Gson().fromJson(retJson, SyncRespVO.class);

		if(NOTIFY_SUCCESS.equals(resp.getResultCode())) {
			resp.setUserCheckVo(new UserCheckVO(true));
		} else {
			log.info("查询失败：", resp.getResultMsg());
			resp.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.SYNC_FAIL));
		}
		return resp;
	}
	@Override
	public SyncProductRespVO syncProduct(String json) {
		String url = bisSysParamService.getValue(SHIEConfigConstant.SYNC_ECIF_URL);
		String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.SYNC_PRODUCT_SERVICE);
		// 发送请求
		String retJson = "";
		SyncProductRespVO resp = null;
		try {
			retJson = HttpSendUtil.sendPostJsonStr(json, url+serviceUrl);
		} catch (Exception e) {
			log.error("查询失败：", e);
			resp = new SyncProductRespVO();
			resp.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.SYNC_FAIL));
			return resp;
		}
		resp = new Gson().fromJson(retJson, SyncProductRespVO.class);
		//注：如果返回客户编码则表示成功。
		if(NOTIFY_SUCCESS.equals(resp.getResultCode())) {
			resp.setUserCheckVo(new UserCheckVO(true));
		} else {
			log.info("查询失败：", resp.getResultMsg());
			resp.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.SYNC_FAIL));
		}
		return resp;
	}

	@Override
	public SyncFileRespVO syncFile(String json) {
		String url = bisSysParamService.getValue(SHIEConfigConstant.SYNC_ECIF_URL);
		String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.SYNC_FILE_SERVICE);
		// 发送请求
		String retJson = "";
		SyncFileRespVO resp = null;
		try {
			retJson = HttpSendUtil.sendPostJsonStr(json, url+serviceUrl);
		} catch (Exception e) {
			log.error("查询失败：", e);
			resp = new SyncFileRespVO();
			resp.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.SYNC_FAIL));
			return resp;
		}
		resp = new Gson().fromJson(retJson, SyncFileRespVO.class);
		//注：如果返回客户编码则表示成功。
		if(NOTIFY_SUCCESS.equals(resp.getResultCode())) {
			resp.setUserCheckVo(new UserCheckVO(true));
		} else {
			log.info("查询失败：", resp.getResultMsg());
			resp.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.SYNC_FAIL));
		}
		return resp;
	}

	
}
