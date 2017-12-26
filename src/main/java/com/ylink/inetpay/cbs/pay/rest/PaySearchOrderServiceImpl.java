package com.ylink.inetpay.cbs.pay.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.pay.service.PayAmtAllocateService;
import com.ylink.inetpay.cbs.util.DateUtils;
import com.ylink.inetpay.common.core.constant.ECurrenoy;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.exception.CodeUncheckException;
import com.ylink.inetpay.common.project.cbs.app.MrsOrganAppService;
import com.ylink.inetpay.common.project.cbs.constant.mrs.PayResultType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOrganDto;
import com.ylink.inetpay.common.project.cbs.exception.PortalCode;
import com.ylink.inetpay.common.project.cbs.vo.pay.OrderOutPayParamQueryDetailRespVo;
import com.ylink.inetpay.common.project.cbs.vo.pay.OrderOutPayParamQueryReqVo;
import com.ylink.inetpay.common.project.cbs.vo.pay.OrderOutPayParamQueryRespVo;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
import com.ylink.inetpay.common.project.pay.shorturl.ShortUrlData;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchRequestVO;

@Service("paySearchOrderService")
public class PaySearchOrderServiceImpl implements PaySearchOrderService {
	
	private static Logger log = LoggerFactory.getLogger(PaySearchOrderServiceImpl.class);

	@Autowired
	private PayAmtAllocateService payAmtAllocateService;
	
	@Resource(name="mrsOrganAppService")
    private MrsOrganAppService mrsOrganAppService;
	
	@Override
	public OrderOutPayParamQueryRespVo balanceQuery(String params) throws Exception {
		log.info("--------进入商户对外支付查询方法：receive:{}",params);
		ShortUrlData data;
		OrderOutPayParamQueryReqVo reqVo = new OrderOutPayParamQueryReqVo();
		OrderOutPayParamQueryRespVo respVo = new OrderOutPayParamQueryRespVo();
		
		try{
			log.debug("-----------第一步：转换OrderOutPayParamQueryReqVo对象");
			data = getObjectBean(params,ShortUrlData.class);
            if(data == null){
                log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("请求参数格式错误");
            }
            
            //如果不存在报文头  那么直接解析json
    		reqVo = getObjectBean(StringUtils.isBlank(data.getBizContent())?params:data.getBizContent(), OrderOutPayParamQueryReqVo.class);
    		if(reqVo == null){
				log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("转换对象为空、Gson对象转换失败");
			}
		}catch(Exception e){
			log.error("查询失败:", e);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("查询失败");
			return respVo;
		}
		
		log.debug("-----------第二步，校验入参数：");
		String checkResult = checkOrderSearchParams(reqVo);
    	if(StringUtil.isNEmpty(checkResult)){
    		log.error(checkResult);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(checkResult);
			return respVo;
    	}
    	
    	log.debug("-----------第三步，业务处理：");
    	PayAmtAllocateDto payAmtAllocate = new PayAmtAllocateDto();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	List<PayAmtAllocateDto> payAmtAllocateList = null;
    	try{
    		MrsOrganDto mrsOrgan=mrsOrganAppService.findByExtOrgId(reqVo.getComCode());
            if(mrsOrgan==null){
                log.error("根据商户号没有查询到商户信息！商户号：[{}]",reqVo.getComCode());
                respVo.setMsgCode(PortalCode.CODE_9999);
    			respVo.setMsgInfo("没有查询到商户信息");
    			return respVo;
            }
            
	    	payAmtAllocate.setMerCode(mrsOrgan.getCustId());
	    	payAmtAllocate.setStartCreateTime(sdf.parse(reqVo.getStartDate()+"000000"));
	    	payAmtAllocate.setEndCreateTime(sdf.parse(reqVo.getEndDate()+"235959"));
	    	payAmtAllocate.setBatchNo(reqVo.getBatchNo());
	    	payAmtAllocate.setMerOrderId(reqVo.getOrderId());
	    	payAmtAllocate.setPayeeBankCardNo(reqVo.getBankCardNo());
	    	payAmtAllocate.setPayeeBankCustName(reqVo.getBankCardName());
	    	
	    	payAmtAllocateList = payAmtAllocateService.findList(payAmtAllocate);
	    	
    	}catch(Exception e){
    		log.error("商户对外支付查询失败", e);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("内部查询失败");
			return respVo;
    	}
    	
    	respVo.setRtnNum(0);
    	List<OrderOutPayParamQueryDetailRespVo> details = new ArrayList<OrderOutPayParamQueryDetailRespVo>();
    	
    	if(null!=payAmtAllocateList && payAmtAllocateList.size()>0){
    		
    		OrderOutPayParamQueryDetailRespVo order = null;
    		// 总数
    		respVo.setRtnNum(payAmtAllocateList.size());
    		SimpleDateFormat sdfal = new SimpleDateFormat("yyyyMMddHHmmss");
    		PayResultType payResult = null;
    		for(PayAmtAllocateDto paa : payAmtAllocateList){
    			order = new OrderOutPayParamQueryDetailRespVo();
    			order.setOrderDate(DateUtils.dateToyyMMdd(paa.getCreateTime()));
    			order.setBatchNo(paa.getBatchNo());
    			order.setOrderId(paa.getMerOrderId());
    			order.setProductCode(paa.getOrderType());
    			order.setAccChanId(paa.getChannelCode());
    			order.setBankType(paa.getPayeeBankType());
    			order.setBankCardName(paa.getPayeeBankCustName());
    			order.setBankCardNo(paa.getPayeeBankCardNo());
    			order.setCurrenoy(ECurrenoy.RMB.getValue());
    			order.setPubPriv(paa.getPubPriv().getValue());
    			order.setCentralBkAccount(paa.getCentralBk());
    			order.setProvince(paa.getProvince());
    			order.setCity(paa.getCity());
    			order.setAmount(paa.getOrderAmt());
    			order.setFeeAmount(paa.getFee());
    			if(null!=paa.getCompleteTime()){
    				order.setCompleteTime(sdfal.format(paa.getCompleteTime()));
    			}
    			
    			// 转换结果
    			if(EOrderStatus.ORDER_STATUS_WAITING_AUDIT.equals(paa.getAllocateStatus())){
    				payResult = PayResultType.ORDER_STATUS_WAITING_AUDIT;
    			}
    			else if(EOrderStatus.ORDER_STATUS_WAITING.equals(paa.getAllocateStatus())
    					|| EOrderStatus.ORDER_STATUS_WORKING.equals(paa.getAllocateStatus())
    					|| EOrderStatus.ORDER_STATUS_CHANLWORKING.equals(paa.getAllocateStatus())){
    				payResult = PayResultType.ORDER_STATUS_WORKING;
    			}
    			else if(EOrderStatus.ORDER_STATUS_SUCCESS.equals(paa.getAllocateStatus())){
    				payResult = PayResultType.ORDER_STATUS_SUCCESS;
    			}
    			else if(EOrderStatus.ORDER_STATUS_FAIL.equals(paa.getAllocateStatus())){
    				payResult = PayResultType.ORDER_STATUS_FAIL;
    			}
    			else if(EOrderStatus.ORDER_STATUS_REFUND_SUCCESS.equals(paa.getAllocateStatus())){
    				payResult = PayResultType.ORDER_STATUS_REFUND_SUCCESS;
    			}
    			// 目前  EOrderStatus.ORDER_STATUS_CLOSE 状态库里认为不存在   如后续该状态去掉  同步去掉即可
    			// 其他情况暂返回失败处理
    			else{
    				payResult = PayResultType.ORDER_STATUS_FAIL;
    			}
    			order.setPayResult(payResult.getValue());
    			order.setPayResultMsg(payResult.getDisplayName());
    			
    			details.add(order);
    		}
    	}
    	respVo.setDetails(details);
    	
    	respVo.setMsgCode(PortalCode.CODE_0000);
		respVo.setMsgInfo("成功");
		
		return respVo;
	}
	
	private String checkOrderSearchParams(OrderOutPayParamQueryReqVo reqVo) {
		if(reqVo == null){
			return "请求对象为空";
		}
		if(StringUtil.isEmpty(reqVo.getComCode())) {
			return "商户代码为空";
		}
		if(StringUtil.isEmpty(reqVo.getStartDate())) {
			return "查询起始日期为空";
		}
		if(StringUtil.isEmpty(reqVo.getEndDate())) {
			return "查询结束日期为空";
		}
		
		if(reqVo.getComCode().length()>16) {
			return "商户代码长度过长";
		}
		if(reqVo.getStartDate().length()!=8) {
			return "查询起始日期格式YYYYMMDD";
		}
		if(reqVo.getStartDate().length()!=8) {
			return "查询起始日期格式YYYYMMDD";
		}
		
		if(null!=reqVo.getBatchNo() && reqVo.getBatchNo().length()>22) {
			return "机构的批次号长度过长";
		}
		if(null!=reqVo.getOrderId() && reqVo.getOrderId().length()>22) {
			return "商户代码长度过长";
		}
		if(null!=reqVo.getBankCardNo() && reqVo.getBankCardNo().length()>30) {
			return "收款银行卡号长度过长";
		}
		if(null!=reqVo.getBankCardName() && reqVo.getBankCardName().length()>50) {
			return "收款方户名长度过长";
		}
		
		return null;
	}
	
	/**
	 * 从json获取Bean 对象
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	private <T> T getObjectBean(String jsonString, Class<T> cls) {
        T t = null;
        Gson gson = new Gson();
        t = gson.fromJson(jsonString, cls);
        return t;
    }

	
	private LoginMsgSearchRequestVO toLoginMsgSearchRequestVO(String params) throws Exception {
		LoginMsgSearchRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params,LoginMsgSearchRequestVO.class);
			if(requestVo == null){
				log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("转换对象为空、Gson对象转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：",e);
			throw new Exception("Gson转换错误："+ e.toString());
		}
	}
}
