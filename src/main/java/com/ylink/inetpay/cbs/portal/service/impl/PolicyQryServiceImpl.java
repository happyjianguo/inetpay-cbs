package com.ylink.inetpay.cbs.portal.service.impl;

import java.util.List;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.cbs.portal.service.PolicyQryService;
import com.ylink.inetpay.cbs.util.HttpSendUtil;
import com.ylink.inetpay.cbs.util.PolicyArr;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.customerpolicyqry.CustomerPolicyQryReqVO;
import com.ylink.inetpay.common.project.portal.vo.customerpolicyqry.CustomerPolicyQryRespVO;
import com.ylink.inetpay.common.project.portal.vo.customerpolicyqry.CustomerPolicyQryResultObj;
import com.ylink.inetpay.common.project.portal.vo.policy.PolicyJson;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PolicyQryReqVO;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PolicyQryRespVO;
import com.ylink.inetpay.common.project.portal.vo.policyqry.PolicyQryResultObj;

/**
 * @author pst11
 */
@Service("policyQryService")
public class PolicyQryServiceImpl implements PolicyQryService{

	private static Logger log = LoggerFactory.getLogger(PolicyQryServiceImpl.class);

	@Autowired
	private BisSysParamService bisSysParamService;
	
	@Override
	public PolicyQryResultObj doPolicyQry(PolicyQryReqVO reqVo){
		// http://172.16.25.33:10086/gscore-web/policy/queryForCustomer
		String url = bisSysParamService.getValue(SHIEConfigConstant.POLICY_QRY_URL);
		String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.POLICY_DETAIL_SERVICE);
//		String url = "http://jzuat.shie.com.cn";
//		String url = "http://172.j16.25.33:10086";
//		String serviceUrl = "/gscore-web/policy/queryForCustomer";
		
		String retJson = HttpSendUtil.sendPostJson(reqVo, url+serviceUrl);
		PolicyQryRespVO resp = new Gson().fromJson(retJson, PolicyQryRespVO.class);
		PolicyQryResultObj resultObj = null;
		if("0000".equals(resp.getResultCode())) {
			resultObj = resp.getResultObj();
			if(resultObj == null){
				// 数据为空
				log.info("查询结果为空:");
				return null;
			}
			resultObj.setUserCheckVo(new UserCheckVO(true));
		} else {
			log.error("查询失败：", resp.getResultMsg());
			resultObj = new PolicyQryResultObj();
			resultObj.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.POLICY_QRY_FAIL));
		}
		return resultObj;
	}
	
	@Override
	public CustomerPolicyQryResultObj doCustomerPolicyQry(CustomerPolicyQryReqVO reqVo){

		// http://172.16.25.33:10086/gscore-web/commonquery/queryPageInfo
//		String url = "http://jzuat.shie.com.cn";
//		String url = "http://172.16.25.33:10086";
//		String serviceUrl = "/gscore-web/commonquery/queryPageInfo";
		String url = bisSysParamService.getValue(SHIEConfigConstant.POLICY_QRY_URL);
		String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.POLICY_PAGE_SERVICE);
		String retJson = HttpSendUtil.sendPostJson(reqVo, url+serviceUrl);
		CustomerPolicyQryRespVO resp = new Gson().fromJson(retJson, CustomerPolicyQryRespVO.class);
		CustomerPolicyQryResultObj resultObj = null;
		if("0000".equals(resp.getResultCode())) {
			resultObj = resp.getResultObj();
			if(resultObj == null){
				// 数据为空
				log.info("查询结果为空:");
				return null;
			}
			resultObj.setUserCheckVo(new UserCheckVO(true));
		} else {
			log.error("保单信息(分页)查询失败：", resp.getResultMsg());
			resultObj = new CustomerPolicyQryResultObj();
			resultObj.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_POLICY_QRY_FAIL));
		}
		return resultObj;
	}

	@Override
	public List<PolicyJson> findPolicy(String policyNo, String orgCode) {
		String url = bisSysParamService.getValue(SHIEConfigConstant.POLICY_PLEDGE_URL);
		String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.POLICY_NO_SERVICE);
//		String url = "http://172.25.33.59:8081/iprs";
//		String serviceUrl = "/selectRegisterInfoByPolicy";
		/*Map<String, String> map = new HashMap();
		map.put("policyNo", policyNo);
		map.put("orgCode", orgCode);*/
//		String cs = "policyNo="+"170106435280306"+"&orgCode="+"000027";
		String cs = "policyNo="+policyNo+"&orgCode="+orgCode;
		String retJson = HttpSendUtil.sendPostXml(cs, url+serviceUrl);
		if (retJson.equals("<List/>")) {
			log.info("查到保单质押数据为空:"+retJson);
			return null;
		}
		
		JSONObject xmlJSONObj = XML.toJSONObject(retJson);
		String as = xmlJSONObj.toString();
		JSONArray parse = HttpSendUtil.parse(as);
		List<PolicyJson> listObj = PolicyArr.listObj(parse);
		
		return listObj;
	}

	@Override
	public List<PolicyJson> find3Element(String name, String certiType, String certiCode) {
//		try {
			String url = bisSysParamService.getValue(SHIEConfigConstant.POLICY_PLEDGE_URL);
			String serviceUrl = bisSysParamService.getValue(SHIEConfigConstant.POLICY_ELEMENT_SERVICE);
//			String url = "http://172.25.33.59:8081/iprs";
//			String serviceUrl = "/selectRegisterInfoByUser";
			
//			String cs = "name="+"刘粗"+"&certiType="+"01"+"&certiCode="+"110101198802050055";
			String cs = "name="+name+"&certiType="+certiType+"&certiCode="+certiCode;
			String retJson = HttpSendUtil.sendPostXml(cs, url+serviceUrl);
			if (retJson.equals("<List/>")) {
				log.info("查到保单质押数据为空:"+retJson);
				return null;
			}
			
			JSONObject xmlJSONObj = XML.toJSONObject(retJson);
			String as = xmlJSONObj.toString();
			JSONArray parse = HttpSendUtil.parse(as);
			List<PolicyJson> listObj = PolicyArr.listObj(parse);
//			PolicyElementRespVO resp = new Gson().fromJson(parse, PolicyElementRespVO.class);
//		} catch (Exception e) {
//			log.error("查询失败：",e);
//		}
		return listObj;
	}
	
  
}
