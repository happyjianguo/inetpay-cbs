package com.ylink.inetpay.cbs.mrs.service;

import com.ylink.inetpay.common.project.cbs.respose.CheckMessageTokenRespPojo;
import com.ylink.inetpay.common.project.cbs.respose.ReturnPhoneCheckMessageRespPojo;

public interface MrsCheckMessageService {

	void removeToken(String custId);

	CheckMessageTokenRespPojo getToken(String custId, CheckMessageTokenRespPojo tokenBean);

	ReturnPhoneCheckMessageRespPojo getPhoneCheckResult(String transCode, ReturnPhoneCheckMessageRespPojo pojo);

	void removePhoneCheckResult(String phoneCheckTransCode);
	
}
