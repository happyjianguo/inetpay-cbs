package com.ylink.inetpay.cbs.portal.service;

import java.util.List;

import com.ylink.inetpay.common.project.portal.vo.customerpolicyqry.CustomerPolicyQryReqVO;
import com.ylink.inetpay.common.project.portal.vo.customerpolicyqry.CustomerPolicyQryResultObj;
import com.ylink.inetpay.common.project.portal.vo.policy.PolicyJson;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PolicyQryReqVO;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PolicyQryResultObj;

public interface PolicyQryService {

	PolicyQryResultObj doPolicyQry(PolicyQryReqVO reqVo) ;

	CustomerPolicyQryResultObj doCustomerPolicyQry(CustomerPolicyQryReqVO reqVo) ;
	
	List<PolicyJson> findPolicy(String policyNo, String orgCode);
	
	List<PolicyJson> find3Element(String name, String certiType, String certiCode);

}
