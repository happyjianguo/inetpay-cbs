package com.ylink.inetpay.cbs.mrs.rest;

import java.net.URLDecoder;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shie.openapi.encrypt.RSAUtils;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.cbs.mrs.service.MrsCheckMessageService;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.project.cbs.respose.CheckMessageUserInfoRespPojo;
import com.ylink.inetpay.common.project.cbs.respose.ReturnPhoneCheckMessageRespPojo;
import com.ylink.inetpay.common.project.cbs.respose.ReturnRespPojo;

import net.sf.json.JSONObject;

@Service("mrsReturnPhoneMessageCheckService")
public class MrsReturnPhoneMessageCheckServiceImpl implements MrsReturnPhoneMessageCheckService{
	
	private static Logger _log = LoggerFactory.getLogger(MrsReturnPhoneMessageCheckServiceImpl.class);
	@Autowired
	private BisSysParamService bisSysParamService;
	@Autowired
	private MrsCheckMessageService mrsCheckMessageService;
	@Override
	public String individualCust(String params) {
		_log.info("手机认证推送报文{}",params);
		if(StringUtils.isBlank(params)){
			return "fail";
		}
		//获取私钥
		String privatekey = bisSysParamService.getValue(SystemParamConstants.MERCHANT_PRIVATE_KEY);
		//获取环境
		//String evn=bisSysParamService.getValue(SystemParamConstants.USER_CHECK_EVN);
		//商户公钥解密
		try {
			JSONObject jsonObject = JSONObject.fromObject(params);
			ReturnRespPojo ReturnRespPojoBean = (ReturnRespPojo) JSONObject.toBean(jsonObject, ReturnRespPojo.class);
			if(ReturnRespPojoBean!=null && !StringUtils.isBlank(ReturnRespPojoBean.getResult())){
				//解密
				params=ReturnRespPojoBean.getResult();
				params = RSAUtils.privateDecrypt(privatekey, params);
				params = URLDecoder.decode(params, "utf-8");
				//获取推送系统公钥
				/*String publicKey=ShieEvnEnmu.get(evn).getPublicKey();
				boolean verifyResult = RSAUtils.publicKeyVerify(publicKey, ReturnRespPojoBean.getSign(), params);
				if(!verifyResult){
					_log.info("推送对象解析失败：{}","验签不通过");
					return "fail";
				}*/
				//转换推送结果
				jsonObject = JSONObject.fromObject(params);
				HashMap<String, Class> classMap = new HashMap<String, Class>();
				classMap.put("userInfos", CheckMessageUserInfoRespPojo.class);
				ReturnPhoneCheckMessageRespPojo pojo  = (ReturnPhoneCheckMessageRespPojo) JSONObject.toBean(jsonObject, ReturnPhoneCheckMessageRespPojo.class,classMap);
				if(pojo!=null){
					mrsCheckMessageService.getPhoneCheckResult(pojo.getTransCode(), pojo);
					_log.info("推送对象解析成功{}",JSON.toJSONString(pojo));
				}else{
					_log.error("接收手机认证推送结果异常：{}","返回结果为空");
					return "fail";
				}
			}else{
				_log.error("接收手机认证推送结果异常：{}","返回结果为空");
				return "fail";
			}
		} catch (Exception e) {
			_log.error("接收手机认证推送结果异常：{}",e);
			return "fail";
		}
		return "success";
	}
}
