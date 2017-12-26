package com.ylink.inetpay.cbs.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.helpers.DefaultHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ylink.inetpay.common.project.portal.vo.policy.PolicyJson;

  
public class PolicyArr extends DefaultHandler{   
	
	private static Logger log = LoggerFactory.getLogger(PolicyArr.class);
	
	/**
	 * JSONArray数据解析到List<PolicyJson>中
	 * @param parse
	 * @return
	 */
	public static List<PolicyJson> listObj(JSONArray parse) {
		PolicyJson policyJson;
		ArrayList<PolicyJson> policyJsonList = new ArrayList<PolicyJson>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date validateDate = null;
		Date expiryDate = null;
		Date registerStartDate = null;
		if (parse != null) {
			for (int i = 0; i < parse.size(); i++) {
				policyJson = new PolicyJson();
				JSONObject object = parse.getJSONObject(i);
				String validate = object.getString("validateDate");
				String expiry = object.getString("expiryDate");
				String registerStart = object.getString("registerStartDate");
				try {
					if (validate.isEmpty() || validate.equals("")) {
						validateDate = null;
					}else {
						validateDate = format.parse(validate);
					}
					if (expiry.isEmpty() || expiry.equals("")) {
						expiryDate = null;
					}else {
						expiryDate = format.parse(expiry);
					}
					if (registerStart.isEmpty() || registerStart.equals("")) {
						registerStartDate = null;
					}else {
						registerStartDate = format.parse(registerStart);
					}
				 } catch (Exception e) {
				    e.printStackTrace();
				 }
				policyJson.setPolicyNo(object.getString("policyNo"));
				policyJson.setHolderName(object.getString("holderName"));
				policyJson.setValidateDate(validateDate);
				policyJson.setExpiryDate(expiryDate);
				policyJson.setHolderCertiType(object.getString("holderCertiType"));
				policyJson.setHolderCertiCode(object.getString("holderCertiCode"));
				
				policyJson.setRegisterStartDate(registerStartDate);
				policyJson.setRegisterStatus(object.getString("registerStatus"));
				policyJson.setInsurerName(object.getString("insurerName"));
				policyJson.setPledgee(object.getString("pledgee"));
				policyJson.setRegisterNo(object.getString("registerNo"));
				policyJsonList.add(i, policyJson);
			}
		}else {
			// 数据为空
			log.info("查询结果为空:");
			return null;
		}
		return policyJsonList;
	}
}  
