package com.ylink.inetpay.cbs.mrs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ylink.inetpay.common.project.cbs.respose.CheckMessageTokenRespPojo;
import com.ylink.inetpay.common.project.cbs.respose.ReturnPhoneCheckMessageRespPojo;

@Service("mrsCheckMessageService")
public class MrsCheckMessageServiceImpl implements MrsCheckMessageService{
	private static Logger _log = LoggerFactory.getLogger(MrsCheckMessageServiceImpl.class);
	// 将token从缓存中移除
	@CacheEvict(value = "mpay", key = "'mrsCheckMessageAppService[key-'+#custId", allEntries = true)
	public void removeToken(String custId) {
		_log.info("token移除缓存");
	}

	// 将token放入缓存中
	@Cacheable(value = "mpay", key = "'mrsCheckMessageAppService[key-'+#custId")
	public CheckMessageTokenRespPojo getToken(String custId, CheckMessageTokenRespPojo token) {
		_log.info("token存入缓存【"+custId+"】");
		return token;
	}

	// 将手机认证信息从缓存中移除
	@CacheEvict(value = "mpay", key = "'mrsCheckMessageAppService[key-'+#phoneCheckTransCode", allEntries = true)
	public void removePhoneCheckResult(String phoneCheckTransCode) {
		_log.info("移除手机认证信息缓存");
	}
	
	// 将手机认证信息放入缓存中
	@Cacheable(value = "mpay", key = "'mrsCheckMessageAppService[key-'+#phoneCheckTransCode")
	public ReturnPhoneCheckMessageRespPojo getPhoneCheckResult(String phoneCheckTransCode,
			ReturnPhoneCheckMessageRespPojo pojo) {
		_log.info("手机认证信息存入缓存【"+phoneCheckTransCode+"】");
		return pojo;
	}
}
