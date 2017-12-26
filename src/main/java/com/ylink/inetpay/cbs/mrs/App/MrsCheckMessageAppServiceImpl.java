package com.ylink.inetpay.cbs.mrs.App;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shie.openapi.client.ShieClient;
import com.shie.openapi.dto.RequestDto;
import com.shie.openapi.dto.ResponseDto;
import com.shie.openapi.enmu.ShieEvnEnmu;
import com.ylink.eu.util.tools.Base64Util;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.cbs.mrs.service.MrsCheckMessageService;
import com.ylink.inetpay.cbs.mrs.service.MrsMerChannelIdService;
import com.ylink.inetpay.cbs.mrs.service.MrsMerPermissionService;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.core.constant.EReqType;
import com.ylink.inetpay.common.core.constant.messageCheck.EServiceType;
import com.ylink.inetpay.common.project.cbs.app.MrsCheckMessageAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsMerChannelIdDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsMerPermissionDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.respose.BaseRespPojo;
import com.ylink.inetpay.common.project.cbs.respose.CheckMessageRespPojo;
import com.ylink.inetpay.common.project.cbs.respose.CheckMessageTokenRespPojo;
import com.ylink.inetpay.common.project.cbs.respose.CheckMessageUserInfoRespPojo;
import com.ylink.inetpay.common.project.cbs.respose.MerChannelIdRespPojo;
import com.ylink.inetpay.common.project.cbs.respose.ReturnPhoneCheckMessageRespPojo;

import net.sf.json.JSONObject;

@Service("mrsCheckMessageAppService")
public class MrsCheckMessageAppServiceImpl implements MrsCheckMessageAppService {
	@Autowired
	private BisSysParamService bisSysParamService;
	@Autowired
	private MrsMerChannelIdService mrsMerChannelIdService;
	@Autowired
	private MrsMerPermissionService mrsMerPermissionService;
	@Autowired
	private MrsCheckMessageService mrsCheckMessageService;
	private static Logger _log = LoggerFactory.getLogger(MrsCheckMessageAppServiceImpl.class);

	@Override
	public List<CheckMessageUserInfoRespPojo> UserMessageCheck(CheckMessageRespPojo checkMessage, String type,
			String custId) throws CbsCheckedException {
		/*// 判断用户是否拥有权限
		String serviceCode;
		if ("phone".equals(type)) {
			serviceCode = EServiceType.PHONE_CHECK.getValue();
		} else if ("photo".equals(type)) {
			serviceCode = EServiceType.USER_CHECK_PHOTO.getValue();
		} else {
			serviceCode = EServiceType.USER_CHECK.getValue();
		}
		if (!mrsMerPermissionService.getServiceByCode(serviceCode, custId)) {
			_log.info("用户信息认证，权限校验不通过！服务代码【" + serviceCode + "】");
			throw new CbsUncheckedException("", "没有权限，请联系管理员");
		}*/
		// 获取配置参数
		_log.info("获取配置参数");
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add(SystemParamConstants.MERCHANT_PRIVATE_KEY);
		paramNames.add(SystemParamConstants.MERCHANT_PW);
		paramNames.add(SystemParamConstants.USER_CHECK_EVN);
		paramNames.add(SystemParamConstants.CHANNEL_ID);
		List<BisSysParamDto> findByParamNames = bisSysParamService.findByParamNames(paramNames);
		HashMap<String, String> paramMap = new HashMap<>();
		for (BisSysParamDto paramDto : findByParamNames) {
			paramMap.put(paramDto.getKey(), paramDto.getValue());
		}
		// 商户私钥
		String privatekey = paramMap.get(SystemParamConstants.MERCHANT_PRIVATE_KEY);
		if (StringUtils.isBlank(privatekey)) {
			_log.error("用户信息认证获取系统私钥失败，请到系统参数中配置私钥【MERCHANT_PRIVATE_KEY】");
			throw new CbsUncheckedException("", "用户信息认证获取系统私钥失败，请到系统参数中配置私钥【MERCHANT_PRIVATE_KEY】");
		}
		// 商户秘钥（仅获取Token的业务报文需传）
		String merChantPw = paramMap.get(SystemParamConstants.MERCHANT_PW);
		if (StringUtils.isBlank(merChantPw)) {
			_log.error("用户信息认证获取商户秘钥失败，请到系统参数中配置商户秘钥【MERCHANT_PW】");
			throw new CbsUncheckedException("", "用户信息认证获取商户秘钥失败，请到系统参数中配置私钥【MERCHANT_PW】");
		}
		// 测试环境
		String evn = paramMap.get(SystemParamConstants.USER_CHECK_EVN);
		if (StringUtils.isBlank(evn)) {
			_log.error("用户信息认证获取环境失败，请到系统参数中配置环境【USER_CHECK_EVN】");
			throw new CbsUncheckedException("", "用户信息认证获取环境失败，请到系统参数中配置私钥【USER_CHECK_EVN】");
		}
		// 渠道id
		String channelId = paramMap.get(SystemParamConstants.CHANNEL_ID);
		if (StringUtils.isBlank(channelId)) {
			_log.error("用户信息认证获取系统渠道id失败，请到系统参数中配置渠道id【CHANNEL_ID】");
			throw new CbsUncheckedException("", "用户认证获取系统渠道id失败，请到系统参数中配置私钥【CHANNEL_ID】");
		}
		// 获取商户渠道id
		_log.info("用户信息认证获取商户渠道id");
		String accChanId = getMerChantChannelId(custId, channelId, merChantPw, evn, privatekey);
		if (StringUtils.isBlank(accChanId)) {
			_log.error("用户信息认证获取商户渠道id失败，一户通账号【" + custId + "】");
			throw new CbsUncheckedException("", "用户信息认证获取商户渠道id失败，一户通账号【" + custId + "】");
		}
		// 获取token值
		_log.info("用户信息认证获取token");
		String token = getToken(custId, privatekey, accChanId, evn, merChantPw);
		if (StringUtils.isBlank(token)) {
			_log.error("用户信息认证获取token失败，一户通账号【" + custId + "】");
			throw new CbsUncheckedException("", "用户信息认证获取token，一户通账号【" + custId + "】");
		}
		// 执行信息认证
		_log.info("执行信息认证");
		CheckMessageRespPojo pojo = doCheckMessage(checkMessage, token, privatekey, accChanId, evn, type,custId,merChantPw,0);
		if (pojo != null && pojo.getUserInfos() != null) {
			return pojo.getUserInfos();
		}
		_log.error("用户信息认证失败：返回结果为空");
		throw new CbsUncheckedException("", "用户信息认证失败：返回结果为空");
	}

	/**
	 * 手机认证
	 */
	@Override
	public boolean phoneCheck(CheckMessageRespPojo checkMessageRespPojo, String custId) throws CbsCheckedException {
		/*// 权限认证
		String serviceCode = EServiceType.PHONE_CHECK.getValue();
		if (!mrsMerPermissionService.getServiceByCode(serviceCode, custId)) {
			_log.info("手机号认证，权限校验不通过！服务代码【" + serviceCode + "】");
			throw new CbsUncheckedException("", "没有权限，请联系管理员");
		}*/
		// 获取配置参数
		_log.info("获取配置参数");
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add(SystemParamConstants.MERCHANT_PRIVATE_KEY);
		paramNames.add(SystemParamConstants.MERCHANT_PW);
		paramNames.add(SystemParamConstants.USER_CHECK_EVN);
		paramNames.add(SystemParamConstants.CHANNEL_ID);
		List<BisSysParamDto> findByParamNames = bisSysParamService.findByParamNames(paramNames);
		HashMap<String, String> paramMap = new HashMap<>();
		for (BisSysParamDto paramDto : findByParamNames) {
			paramMap.put(paramDto.getKey(), paramDto.getValue());
		}
		// 商户私钥
		String privatekey = paramMap.get(SystemParamConstants.MERCHANT_PRIVATE_KEY);
		if (StringUtils.isBlank(privatekey)) {
			_log.error("手机号认证获取系统私钥失败，请到系统参数中配置私钥【MERCHANT_PRIVATE_KEY】");
			throw new CbsUncheckedException("", "手机号认证获取系统私钥失败，请到系统参数中配置私钥【MERCHANT_PRIVATE_KEY】");
		}
		// 商户秘钥（仅获取Token的业务报文需传）
		String merChantPw = paramMap.get(SystemParamConstants.MERCHANT_PW);
		if (StringUtils.isBlank(merChantPw)) {
			_log.error("手机号认证获取商户秘钥失败，请到系统参数中配置商户秘钥【MERCHANT_PW】");
			throw new CbsUncheckedException("", "手机号认证获取商户秘钥失败，请到系统参数中配置私钥【MERCHANT_PW】");
		}
		// 测试环境
		String evn = paramMap.get(SystemParamConstants.USER_CHECK_EVN);
		if (StringUtils.isBlank(evn)) {
			_log.error("手机号认证获取环境失败，请到系统参数中配置环境【USER_CHECK_EVN】");
			throw new CbsUncheckedException("", "手机号认证获取环境失败，请到系统参数中配置私钥【USER_CHECK_EVN】");
		}
		// 渠道id
		String channelId = paramMap.get(SystemParamConstants.CHANNEL_ID);
		if (StringUtils.isBlank(channelId)) {
			_log.error("手机号认证获取系统渠道id失败，请到系统参数中配置渠道id【CHANNEL_ID】");
			throw new CbsUncheckedException("", "手机号认证获取系统渠道id失败，请到系统参数中配置私钥【CHANNEL_ID】");
		}
		// 获取商户渠道id
		_log.info("获取商户渠道id");
		String accChanId = getMerChantChannelId(custId, channelId, merChantPw, evn, privatekey);
		if (StringUtils.isBlank(accChanId)) {
			_log.error("手机号认证获取商户渠道id失败，一户通账号【" + custId + "】");
			throw new CbsUncheckedException("", "手机号认证获取商户渠道id失败，一户通账号【" + custId + "】");
		}
		// 获取token值
		_log.info("获取token");
		String token = getToken(custId, privatekey, accChanId, evn, merChantPw);
		if (StringUtils.isBlank(token)) {
			_log.error("手机号认证获取token失败，一户通账号【" + custId + "】");
			throw new CbsUncheckedException("", "手机号认证获取token，一户通账号【" + custId + "】");
		}
		// 执行手机认证
		_log.info("执行手机认证");
		return doPhoneCheck(privatekey, token, accChanId, evn, checkMessageRespPojo,custId,merChantPw,0);
	}

	public boolean doPhoneCheck(String privatekey, String token, String accChanId, String evn,
			CheckMessageRespPojo pojo,String custId,String merChantPw,long failNum) {
		// 服务代码
		String serviceCode = EServiceType.PHONE_CHECK.getValue();
		RequestDto requestDto = new RequestDto(serviceCode, accChanId, token);
		// 构建业务报文
		Map<String, Object> bizContent = new HashMap<String, Object>();
		bizContent.put("reqType", EReqType.SINGLE.getValue());
		bizContent.put("transCode", pojo.getTransCode());
		bizContent.put("userInfos", pojo.getUserInfos());
		// ShieClient参数为connectTimeout，readTimeout的毫秒数
		ShieClient shieClient = new ShieClient(15000, 15000);
		ResponseDto responseDto = null;
			_log.info("手机认证发送报文：" + JSON.toJSONString(requestDto) + JSON.toJSONString(bizContent));
			try {
//				responseDto = shieClient.send(requestDto, bizContent, evn, privatekey);
			} catch (Exception e) {
				_log.error("手机认证失败：{}",e);
				throw new CbsUncheckedException("", "手机认证失败,通讯异常");
			}
			_log.info("手机认证返回报文：" + JSON.toJSONString(responseDto));
			if (responseDto != null && !StringUtils.isBlank(responseDto.getResponseCode())) {
				if("0000".equals(responseDto.getResponseCode())){
					return true;
				}else{
					if("9102".equals(responseDto.getResponseCode())){
						_log.error("用户信息认证失败：" + responseDto.getResponseDesc());
						if(failNum<3){
							failNum++;
							//如果是token失效造成的，重新发送
							// 重新获取token，存储到缓存中去
							mrsCheckMessageService.removeToken(custId);
							CheckMessageTokenRespPojo tokenBean = getTokenDo(privatekey, accChanId, evn, merChantPw, custId);
							if (tokenBean != null && !StringUtils.isBlank(tokenBean.getToken())) {
								mrsCheckMessageService.getToken(custId, tokenBean);
								return doPhoneCheck(privatekey, tokenBean.getToken(), accChanId, evn, pojo,custId,merChantPw,failNum);
							} else {
								_log.error("获取token失败");
								throw new CbsUncheckedException("", "获取token失败");
							}
						}else {
							_log.error("手机认证由于token失效，造成失败3次，不予重发");
							throw new CbsUncheckedException("", "手机认证失败");
						}
					}
					if("9104".equals(responseDto.getResponseCode())){
						_log.error("手机认证失败：" + responseDto.getResponseDesc());
						throw new CbsUncheckedException("", "您没有该服务权限，请到柜台签约该服务");
					}
					_log.error("手机认证失败：" + responseDto.getResponseDesc());
					throw new CbsUncheckedException("",responseDto.getResponseDesc() );
				}
			}
			_log.info("手机认证失败：" + responseDto.getResponseDesc());
			throw new CbsUncheckedException("", "手机认证失败" + responseDto.getResponseDesc());

	}

	// 获取商户渠道id
	public String getMerChantChannelId(String custId, String channelId, String merChantPw, String evn,
			String merChantPrivateKey) {
		_log.info("获取商户渠道id");
		String merchanChannelId = mrsMerChannelIdService.getMerChannelIdByCustId(custId);
		// 如果商户渠道id为空,重新发起豹纹查询
		if (StringUtils.isBlank(merchanChannelId)) {
			_log.info("系统参数中没有设置商户渠道id，重新获取商户渠道id");
			merchanChannelId = sendJsonGetMerChanneId(custId, channelId, merChantPw, evn, merChantPrivateKey);
		}
		return merchanChannelId;
	}

	/**
	 * 获取商户渠道id，发送报文
	 * 
	 * @param custId
	 * @param channel
	 * @param serviceCode
	 * @param merChantPw
	 * @param evn
	 * @param merChantPrivateKey
	 * @return
	 */
	public String sendJsonGetMerChanneId(String custId, String channel, String merChantPw, String evn,
			String merChantPrivateKey) {
		/*// 权限认证
		String serviceCode = EServiceType.MER_CHANNEL_ID.getValue();
		if (!mrsMerPermissionService.getServiceByCode(serviceCode, custId)) {
			_log.info("获取商户渠道id，权限校验不通过！服务代码【" + serviceCode + "】");
			throw new CbsUncheckedException("", "没有权限，请联系管理员");
		}*/
		String serviceCode = EServiceType.MER_CHANNEL_ID.getValue();
		RequestDto requestDto = new RequestDto();
		requestDto.setServiceCode(serviceCode);
		requestDto.setChannelId(channel);
		requestDto.setOneAccountAcc(custId);
		// 构建业务报文
		Map<String, Object> bizContent = new HashMap<String, Object>();
		bizContent.put("accSert", merChantPw);
		// ShieClient参数为connectTimeout，readTimeout的毫秒数
		ShieClient shieClient = new ShieClient(15000, 15000);
		ResponseDto responseDto = null;
		try {
			_log.info("获取渠道id发送报文：" + JSON.toJSONString(bizContent) + JSON.toJSONString(requestDto));
			_log.info("征信请求参数evn:"+evn+",url:"+ShieEvnEnmu.get(evn));
//			responseDto = shieClient.send(requestDto, bizContent, evn, merChantPrivateKey);
			_log.info("获取渠道id返回报文：" + JSON.toJSONString(responseDto));
		} catch (Exception e) {
			_log.error("获取渠道id失败:()", e);
			throw new CbsUncheckedException("", "获取渠道id失败");
		}
		MerChannelIdRespPojo merChannelIdBean = new MerChannelIdRespPojo();
		if (responseDto != null && !StringUtils.isBlank(responseDto.getResponseCode())
				&& "0000".equals(responseDto.getResponseCode())) {
			JSONObject jsonObject = JSONObject.fromObject(responseDto.getBizContent());
			Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("serviceList", MrsMerPermissionDto.class);
			merChannelIdBean = (MerChannelIdRespPojo) JSONObject.toBean(jsonObject, MerChannelIdRespPojo.class,
					classMap);
			if (merChannelIdBean != null && !StringUtils.isBlank(merChannelIdBean.getAccChanId())) {
				// 保存渠道信息
				MrsMerChannelIdDto dto = new MrsMerChannelIdDto();
				dto.setCustId(custId);
				dto.setMerChannelId(merChannelIdBean.getAccChanId());
				mrsMerChannelIdService.saveMerChannelId(dto);
				List<MrsMerPermissionDto> merPerssionList = merChannelIdBean.getServiceList();
				// 可以做成批量新增
				if (merPerssionList != null && !merPerssionList.isEmpty()) {
					mrsMerPermissionService.batchSaveService(merPerssionList, custId);
				}
				return merChannelIdBean.getAccChanId();
			} else {
				_log.error("获取渠道id失败");
				throw new CbsUncheckedException("", "获取渠道id失败");
			}
		} else {
			_log.error("获取渠道id失败：" + responseDto.getResponseDesc());
			throw new CbsUncheckedException("", "获取渠道id失败：" + responseDto.getResponseDesc());
		}
	}

	/**
	 * 获取缓存中的手机认证数据
	 */
	@Override
	public ReturnPhoneCheckMessageRespPojo queryPhoneCheckResult(String phoneCheckTransCode, String custId)
			throws CbsCheckedException {
		ReturnPhoneCheckMessageRespPojo pojo = mrsCheckMessageService.getPhoneCheckResult(phoneCheckTransCode, null);
		//如果获取到缓存数据，移除缓存
		if(pojo!=null){
			mrsCheckMessageService.removePhoneCheckResult(phoneCheckTransCode);
		}
		return pojo;
	}

	// 获取token，默认先从缓存中获取
	public String getToken(String custId, String merChantPrivateKey, String accChanId, String evn, String merChantPw) {
		String token = "";
		// 从缓存中获取token，如果不存在则是第一次获取token，需要重新获取token
		CheckMessageTokenRespPojo tokenBean = mrsCheckMessageService.getToken(custId, null);
		if (tokenBean == null || StringUtils.isBlank(tokenBean.getToken())) {
			// 移除缓存
			mrsCheckMessageService.removeToken(custId);
			tokenBean = getTokenDo(merChantPrivateKey, accChanId, evn, merChantPw, custId);
		} else {
			// 判断token是否过期
			long effectiveTime = (new Date().getTime() - tokenBean.getCreateTime().getTime()) / 1000;
			if (tokenBean.getExpiresIn().compareTo(effectiveTime + "") <= 0) {// 时间传输消耗，不考虑等于
				// 移除缓存
				mrsCheckMessageService.removeToken(custId);
				tokenBean = getTokenDo(merChantPrivateKey, accChanId, evn, merChantPw, custId);
			}
		}
		if (tokenBean == null || StringUtils.isBlank(tokenBean.getToken())) {
			_log.error("获取token失败:token为空");
			throw new CbsUncheckedException("", "获取token失败，token为空");
		} else {
			// 保存token到缓存中
			mrsCheckMessageService.getToken(custId, tokenBean);
			token = tokenBean.getToken();
			
			
		}
		return token;
	}

	/**
	 * 获取token，发送报文
	 * 
	 * @param merChantPrivateKey
	 * @param accChanId
	 * @param evn
	 * @param merChantPw
	 * @return
	 */
	private CheckMessageTokenRespPojo getTokenDo(String merChantPrivateKey, String accChanId, String evn,
			String merChantPw, String custId) {
		/*// 权限认证
		String serviceCode = EServiceType.TOKEN.getValue();
		if (!mrsMerPermissionService.getServiceByCode(serviceCode, custId)) {
			_log.info("获取token，权限校验不通过！服务代码【" + serviceCode + "】");
			throw new CbsUncheckedException("", "没有权限，请联系管理员");
		}*/
		String serviceCode = EServiceType.TOKEN.getValue();
		RequestDto requestDto = new RequestDto(serviceCode, accChanId);
		// 构建业务报文
		Map<String, Object> bizContent = new HashMap<String, Object>();
		bizContent.put("accSert", merChantPw);
		// ShieClient参数为connectTimeout，readTimeout的毫秒数
		ShieClient shieClient = new ShieClient(15000, 15000);
		ResponseDto responseDto = null;
		try {
			_log.info("获取token发送报文：" + JSON.toJSONString(requestDto) + JSON.toJSONString(bizContent));
//			responseDto = shieClient.send(requestDto, bizContent, evn, merChantPrivateKey);
			_log.info("获取token返回报文：" + JSON.toJSONString(responseDto));
		} catch (Exception e) {
			_log.error("获取token异常()", e);
			throw new CbsUncheckedException("", "获取token异常");
		}
		CheckMessageTokenRespPojo tokenBean = new CheckMessageTokenRespPojo();
		if (responseDto != null && !StringUtils.isBlank(responseDto.getResponseCode())
				&& "0000".equals(responseDto.getResponseCode())) {
			JSONObject jsonObject = JSONObject.fromObject(responseDto.getBizContent());
			tokenBean = (CheckMessageTokenRespPojo) JSONObject.toBean(jsonObject, CheckMessageTokenRespPojo.class);
			return tokenBean;
		}
		_log.error("获取token异常：" + responseDto.getResponseDesc());
		throw new CbsUncheckedException("", "获取token异常：" + responseDto.getResponseDesc());
	}

	/**
	 * 判断是否因为token失效造成的失败，业务需要重新发送
	 */
	public boolean isAbate(BaseRespPojo pojo) {
		if ("9102".equals(pojo.getResponseCode()) || "Token无效".equals(pojo.getResponseDesc())) {
			return true;
		}
		return false;
	}

	/*
	 * 执行用户信息认证
	 */
	public CheckMessageRespPojo doCheckMessage(CheckMessageRespPojo checkMessage, String token, String privatekey,
			String accChanId, String evn, String type,String custId,String merChantPw,long failNum) {
		String serviceCode = EServiceType.USER_CHECK.getValue();
		if ("photo".equals(type)) {
			serviceCode = EServiceType.USER_CHECK_PHOTO.getValue();
		}else if("badmsg".equals(type)){
			serviceCode = EServiceType.QUERYBADINFO.getValue();
		}
		RequestDto requestDto = new RequestDto(serviceCode, accChanId, token);
		// 构建业务报文
		Map<String, Object> bizContent = new HashMap<String, Object>();
		bizContent.put("reqType", checkMessage.getReqType());
		bizContent.put("transCode", checkMessage.getTransCode());
		bizContent.put("userInfos", checkMessage.getUserInfos());
		// ShieClient参数为connectTimeout，readTimeout的毫秒数
		ShieClient shieClient = new ShieClient(60000, 60000);
		ResponseDto responseDto = null;
		_log.info("认证报文：" + JSON.toJSONString(bizContent));
		CheckMessageRespPojo checkMessageBean = new CheckMessageRespPojo();
		try {
			_log.info("用户认证发送报文：" + JSON.toJSONString(requestDto) + JSON.toJSONString(bizContent));
//			responseDto = shieClient.send(requestDto, bizContent, evn, privatekey);
			_log.info("用户认证返回报文：" + JSON.toJSONString(responseDto));
		} catch (Exception e) {
			_log.error("用户信息认证失败：{}", e);
			throw new CbsUncheckedException("", "用户信息认证失败");
		}
		if (responseDto != null && !StringUtils.isBlank(responseDto.getResponseCode())) {
			if ("0000".equals(responseDto.getResponseCode())) {
				JSONObject jsonObject = JSONObject.fromObject(responseDto.getBizContent());
				Map<String, Class> classMap = new HashMap<String, Class>();
				classMap.put("userInfos", CheckMessageUserInfoRespPojo.class);
				checkMessageBean = (CheckMessageRespPojo) JSONObject.toBean(jsonObject, CheckMessageRespPojo.class,
						classMap);
				if (checkMessageBean != null) {
					if (!"0".equals(checkMessageBean.getReturnStatus())) {
						_log.error("用户信息认证失败：" + checkMessageBean.getReturnValue());
						throw new CbsUncheckedException("", "用户信息认证失败:" + checkMessageBean.getReturnValue());
					}
					if ("photo".equals(type) && checkMessageBean.getUserInfos() != null) {
						// base64解码
						for (CheckMessageUserInfoRespPojo pojo : checkMessageBean.getUserInfos()) {
							String checkPhoto = pojo.getCheckPhoto();
							if (!StringUtils.isBlank(checkPhoto)) {
								pojo.setPhoto(Base64Util.decodeString(checkPhoto));
							}
						}
					}
					return checkMessageBean;
				}
			} else {
				
				if("9102".equals(responseDto.getResponseCode())){
					_log.error("用户信息认证失败：" + responseDto.getResponseDesc());
					if(failNum<3){
						failNum++;
						//如果是token失效造成的，重新发送
						// 重新获取token，存储到缓存中去
						mrsCheckMessageService.removeToken(custId);
						CheckMessageTokenRespPojo tokenBean = getTokenDo(privatekey, accChanId, evn, merChantPw, custId);
						if (tokenBean != null && !StringUtils.isBlank(tokenBean.getToken())) {
							mrsCheckMessageService.getToken(custId, tokenBean);
							return doCheckMessage(checkMessage, tokenBean.getToken(), privatekey, accChanId, evn, type,custId,merChantPw,failNum);
						} else {
							_log.error("获取token失败");
							throw new CbsUncheckedException("", "获取token失败");
						}
					}else{
						mrsCheckMessageService.removeToken(custId);
						_log.error("信息认证由于token失效，造成失败3次，不予重发");
						throw new CbsUncheckedException("", "信息认证失败");
					}
				}
				if("9104".equals(responseDto.getResponseCode())){
					_log.error("用户信息认证失败：" + responseDto.getResponseDesc());
					throw new CbsUncheckedException("", "您没有该服务权限，请到柜台签约该服务");
				}
				_log.error("用户信息认证失败：" + responseDto.getResponseDesc());
				throw new CbsUncheckedException("", "用户信息认证失败:" + responseDto.getResponseDesc());
			}
		}
		_log.error("用户信息认证失败：通讯异常");
		throw new CbsUncheckedException("", "用户信息认证失败，请稍后重试");
	}
}
