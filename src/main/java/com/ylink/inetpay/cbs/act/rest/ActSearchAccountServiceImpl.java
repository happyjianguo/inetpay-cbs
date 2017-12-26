package com.ylink.inetpay.cbs.act.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.google.gson.Gson;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.act.service.ActAccountService;
import com.ylink.inetpay.cbs.chl.service.ChlBankService;
import com.ylink.inetpay.cbs.pay.service.PayAmtAllocateService;
import com.ylink.inetpay.cbs.pay.service.PayBookService;
import com.ylink.inetpay.common.core.constant.EAccountDrCr;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.EBusiType;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.constant.ETradeType;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.cbs.app.MrsAccountAppService;
import com.ylink.inetpay.common.project.cbs.constant.mrs.PayResultType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.PortalCode;
import com.ylink.inetpay.common.project.cbs.vo.act.AccountBalanceQueryReqVo;
import com.ylink.inetpay.common.project.cbs.vo.act.AccountBalanceQueryRespVo;
import com.ylink.inetpay.common.project.cbs.vo.pay.OrderDetailsQueryDetailRespVo;
import com.ylink.inetpay.common.project.cbs.vo.pay.OrderDetailsQueryReqVo;
import com.ylink.inetpay.common.project.cbs.vo.pay.OrderDetailsQueryRespVo;
import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;
import com.ylink.inetpay.common.project.pay.dto.PayBookDto;
import com.ylink.inetpay.common.project.pay.rest.outpay.MerOutPayBatchBizContent;
import com.ylink.inetpay.common.project.pay.shorturl.ShortUrlData;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchRequestVO;

@Service("actSearchAccountService")
public class ActSearchAccountServiceImpl implements ActSearchAccountService {
	
	private static Logger log = LoggerFactory.getLogger(ActSearchAccountServiceImpl.class);

	@Autowired
	private ActAccountService actAccountService;
	
	@Autowired
	private PayBookService payBookService;
	
	@Autowired
	private ChlBankService chlBankService;
	
	@Autowired
	private PayAmtAllocateService payAmtAllocateService;
	
	@Resource(name="mrsAccountAppService")
    private MrsAccountAppService mrsAccountAppService;
	
	@Override
	public AccountBalanceQueryRespVo balanceQuery(String params) throws Exception {
		log.info("--------进入商户一户通账户余额查询方法：receive:{}",params);
		ShortUrlData data;
		AccountBalanceQueryReqVo reqVo = new AccountBalanceQueryReqVo();
		AccountBalanceQueryRespVo respVo = new AccountBalanceQueryRespVo();
		
		try{
			log.info("-----------第一步：转换AccountBalanceQueryReqVO对象");
			data = getObjectBean(params,ShortUrlData.class);
            if(data == null){
                log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("请求参数格式错误");
            }
            
            //如果不存在报文头  那么直接解析json
    		reqVo = getObjectBean(StringUtils.isBlank(data.getBizContent())?params:data.getBizContent(), AccountBalanceQueryReqVo.class);
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
		
		log.info("-----------第二步，校验入参参数：");
		String checkResult = checkAccountSearchParams(reqVo);
    	if(StringUtil.isNEmpty(checkResult)){
    		log.error(checkResult);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(checkResult);
			return respVo;
    	}
    	
    	log.info("-----------第三步，业务处理：");
    	// 余额查询 指定业务类型  04 消费备付金
    	List<ActAccountDto> accountList = actAccountService.findByCustIdSubBusiType(reqVo.getCustId(), reqVo.getAccountType(), 
    			EActBusiRefSubBusiType.BALANCE_ACCOUNT.getValue());
    	
    	if(null==accountList || accountList.size()==0){
    		log.error("商户一户通账户余额查询失败，未查询到账户信息");
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("未查询到账户信息");
			return respVo;
    	}
    	
    	// 代付金额
    	// 对外支付 处理中金额
    	log.info("-----------第四步，业务处理，获取处理中订单金额：");
    	Long payingAmt = getBalanceQueryPayingAmt(reqVo);
    	
    	log.info("-----------第四步，业务处理，获取处理中订单金额为："+payingAmt);
    	
    	ActAccountDto account = accountList.get(0);
    	respVo.setCustId(reqVo.getCustId());
    	respVo.setAccountType(reqVo.getAccountType());
    	respVo.setAccountName(account.getCustName());
    	respVo.setBalance(account.getCashAmount());
    	respVo.setWaitPayAmt(payingAmt);
    	respVo.setCurrenoy(account.getCurrenoy().getValue());
		
    	respVo.setMsgCode(PortalCode.CODE_0000);
		respVo.setMsgInfo("成功");
		
		log.info("-----------商户一户通账户余额查询方法处理成功");
		
		return respVo;
	}
	
	/**
	 * 计算代付金额
	 * 对外支付订单 处理中金额
	 * @param reqVo
	 * @return
	 */
	private Long getBalanceQueryPayingAmt(AccountBalanceQueryReqVo reqVo){
		Long payingAmt = 0l;
		PayAmtAllocateDto payAmtAllocate = new PayAmtAllocateDto();
		List<EOrderStatus> queryOrders = new ArrayList<EOrderStatus>();
    	payAmtAllocate.setPayerCustId(reqVo.getCustId());
    	queryOrders.add(EOrderStatus.ORDER_STATUS_WORKING);
    	queryOrders.add(EOrderStatus.ORDER_STATUS_CHANLWORKING);
    	payAmtAllocate.setQueryOrders(queryOrders);;
    	List<PayAmtAllocateDto> payAmtAllocateList = payAmtAllocateService.findList(payAmtAllocate);
    	if(null!=payAmtAllocateList && !payAmtAllocateList.isEmpty()){
    		for(PayAmtAllocateDto dto : payAmtAllocateList){
    			payingAmt += dto.getOrderAmt();
    		}
    	}
    	return payingAmt;
	}
	
	
	@Override
	public OrderDetailsQueryRespVo orderDetailsQuery(String params) throws Exception {
		log.info("--------进入一户通账户交易明细查询方法：receive:{}",params);
		ShortUrlData data;
		OrderDetailsQueryReqVo reqVo = new OrderDetailsQueryReqVo();
		OrderDetailsQueryRespVo respVo = new OrderDetailsQueryRespVo();
		
		try{
			log.debug("-----------第一步：转换OrderDetailsQueryReqVo对象");
			data = getObjectBean(params,ShortUrlData.class);
            if(data == null){
                log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("请求参数格式错误");
            }
            
            //如果不存在报文头  那么直接解析json
    		reqVo = getObjectBean(StringUtils.isBlank(data.getBizContent())?params:data.getBizContent(), OrderDetailsQueryReqVo.class);
    		if(reqVo == null){
				log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("请求参数格式错误");
			}
		}catch(Exception e){
			log.error("查询失败:", e);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("查询失败");
			return respVo;
		}
		
		log.debug("-----------第二步，校验入参参数：");
		String checkResult = checkOrderDetailSearchParams(reqVo);
    	if(StringUtil.isNEmpty(checkResult)){
    		log.error(checkResult);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(checkResult);
			return respVo;
    	}
    	
    	log.debug("-----------第三步，业务处理：");
    	// 
    	PayBookDto payBookDto = new PayBookDto();
    	PageData<PayBookDto> pageDate = new PageData<PayBookDto>();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	
    	try {
			MrsAccountDto mrsAccount = mrsAccountAppService.findByExOrgNo(reqVo.getComCode());
			if(mrsAccount==null){
			    log.error("根据商户号没有查询到商户信息！商户号：[{}]",reqVo.getComCode());
			    respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("没有查询到商户信息");
				return respVo;
			}
			
			payBookDto.setPayerCustId(mrsAccount.getCustId());
			payBookDto.setStartTime(sdf.parse(reqVo.getStartDate()+"000000"));
			payBookDto.setEndTime(sdf.parse(reqVo.getEndDate()+"235959"));
			int pageNo = 1;
			int pageSize = 1000;
			try {
				pageNo = Integer.valueOf(reqVo.getPageNo());
				pageSize = Integer.valueOf(reqVo.getPageSize());
			} catch (Exception e) {
				 log.error("页数页码输入错误",e);
			     respVo.setMsgCode(PortalCode.CODE_9999);
				 respVo.setMsgInfo("页数页码输入数字");
				 return respVo;
			}
			pageDate.setPageNumber(pageNo);
			if(null==reqVo.getPageSize() || pageSize>1000 || pageSize<1){
				pageDate.setPageSize(1000);
			}else{
				pageDate.setPageSize(pageSize);
			}
			
			// 开始查询数据
			pageDate = payBookService.queryAllDataForAccount(pageDate, payBookDto);
		} catch (Exception e) {
			log.error("一户通账户交易明细查询失败！商户号：" + reqVo.getComCode(), e);
			
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("查询失败");
			return respVo;
		}
    	
    	respVo.setRtNum(0);
    	
    	// 返回值处理
    	List<OrderDetailsQueryDetailRespVo> details = new ArrayList<OrderDetailsQueryDetailRespVo>();
    	OrderDetailsQueryDetailRespVo detail = null;
    	SimpleDateFormat sdfal = new SimpleDateFormat("yyyyMMddHHmmss");
    	List<PayBookDto> payBookList = pageDate.getRows();
    	if(null!=payBookList && payBookList.size()>0){
    		respVo.setRtNum(payBookList.size());
    		
    		// 获取银行信息
    		// 去银行类型
    		ArrayList<String> bankTypes = new ArrayList<String>();
    		for(PayBookDto book : payBookList){
    			if(StringUtil.isNEmpty(book.getBankType()) && !bankTypes.contains(book.getBankType())){
    				bankTypes.add(book.getBankType());
    			}
    		}
    		// 根据银行类型获取银行信息
    		List<TbChlBank> bankList = new ArrayList<TbChlBank>();
    		if(bankTypes.size()>0){
    			bankList = chlBankService.getBankByBankTypes(bankTypes);
    		}
    		
    		PayResultType payResult = null;
    		
    		for(PayBookDto book : payBookList){
    			detail = new OrderDetailsQueryDetailRespVo();
    			detail.setOrderNo(book.getMerOrderId());
    			detail.setOrderTime(sdfal.format(book.getCreateTime()));
    			detail.setAmount(book.getOrderAmt());
    			detail.setBusiType(book.getBusiType().getValue());
    			detail.setBankCardName(book.getPayeeBankCustName());
    			detail.setBankCardNo(book.getPayeeBankNo());
    			detail.setOptional(book.getRemark());
    			
    			// 借贷标志  DR-借  CR-贷
    			detail.setDrCrFlag(""); // 初始置空
    			// ==入金 为 贷  出金 为 借
    			// ==入金
    			// ===充值  ：ACCOUNT_RECHARGE("01","余额充值"), 
    			//       CREDIT_ACCOUNT_RECHARGE("22","余额银保充值"),    
    			//       WX_ZFB_ACCOUNT_RECHARGE("29","微信支付宝余额充值"),
    			if(EBusiType.RECHARGE.equals(book.getBusiType())
    					&& (ETradeType.ACCOUNT_RECHARGE.equals(book.getTradeType())
    							|| ETradeType.CREDIT_ACCOUNT_RECHARGE.equals(book.getTradeType())
    							|| ETradeType.WX_ZFB_ACCOUNT_RECHARGE.equals(book.getTradeType())  )){
    				detail.setDrCrFlag(EAccountDrCr.CR.getValue());
    			}
    			// ===对外支付 冲正      MER_OUTPAY_CHANGE("36","商户对外支付动帐"),     MER_OUT_PAY_CORRECT("41","商户对外支付到银行冲正"),
    			if(EBusiType.MER_OUT_PAY.equals(book.getBusiType())
    					&& (ETradeType.MER_OUTPAY_CHANGE.equals(book.getTradeType())
    							|| ETradeType.MER_OUT_PAY_CORRECT.equals(book.getTradeType())  )){
    				detail.setDrCrFlag(EAccountDrCr.CR.getValue());
    			}
    			// ===动帐通知  退票退款
    			if(EBusiType.ACC_CHANGE.equals(book.getBusiType())
    					&& (ETradeType.MER_OUTPAY_CHANGE.equals(book.getTradeType())  )){
    				detail.setDrCrFlag(EAccountDrCr.CR.getValue());
    			}
    			
    			// ==出金
    			// ===充值 的冲正      LINE_RECHARGE_CORRECT("60","线下充值冲正"),    ACCOUNT_RECHARGE_REFUND("62","余额充值退款"),
    		    //  	CREDIT_ACCOUNT_RECHARGE_REFUND("63","余额银保充值退款"),
    			//  	WX_ZFB_ACCOUNT_RECHARGE_REFUND("64","微信支付宝余额充值退款"),
    			if(EBusiType.RECHARGE.equals(book.getBusiType())
    					&& (ETradeType.LINE_RECHARGE_CORRECT.equals(book.getTradeType())
    							|| ETradeType.ACCOUNT_RECHARGE_REFUND.equals(book.getTradeType())
    							|| ETradeType.CREDIT_ACCOUNT_RECHARGE_REFUND.equals(book.getTradeType())  
    							|| ETradeType.WX_ZFB_ACCOUNT_RECHARGE_REFUND.equals(book.getTradeType())  )){
    				detail.setDrCrFlag(EAccountDrCr.DR.getValue());
    			}
    			// ===对外支付	MER_OUT_PAY("23","商户对外支付"),
    			if(EBusiType.MER_OUT_PAY.equals(book.getBusiType())
    					&& (ETradeType.MER_OUT_PAY.equals(book.getTradeType())  )){
    				detail.setDrCrFlag(EAccountDrCr.DR.getValue());
    			}
    			
    			// 交易银行  付款方银行
    			if(bankList.size()>0){
	    			for(TbChlBank bank : bankList){
	    				if(bank.getBankType().equals(book.getBankType())){
	    					detail.setTranBank(bank.getBankName());
	    				}
	    			}
    			}
    			
    			// 转换结果
    			if(EOrderStatus.ORDER_STATUS_WAITING_AUDIT.equals(book.getOrderStatus())
    					|| EOrderStatus.ORDER_STATUS_WAITING.equals(book.getOrderStatus())
    					|| EOrderStatus.ORDER_STATUS_WORKING.equals(book.getOrderStatus())
    					|| EOrderStatus.ORDER_STATUS_CHANLWORKING.equals(book.getOrderStatus())){
    				payResult = PayResultType.ORDER_STATUS_WORKING;
    			}
    			else if(EOrderStatus.ORDER_STATUS_SUCCESS.equals(book.getOrderStatus())){
    				payResult = PayResultType.ORDER_STATUS_SUCCESS;
    			}
    			else if(EOrderStatus.ORDER_STATUS_FAIL.equals(book.getOrderStatus())){
    				payResult = PayResultType.ORDER_STATUS_FAIL;
    				detail.setFailReason(book.getFailReason());
    			}
    			else if(EOrderStatus.ORDER_STATUS_REFUND_SUCCESS.equals(book.getOrderStatus())){
    				payResult = PayResultType.ORDER_STATUS_REFUND_SUCCESS;
    			}
    			// 目前  EOrderStatus.ORDER_STATUS_CLOSE 状态库里认为不存在   如后续该状态去掉  同步去掉即可
    			// 其他情况暂返回失败处理
    			else{
    				payResult = PayResultType.ORDER_STATUS_FAIL;
    				detail.setFailReason(book.getFailReason());
    			}
    			detail.setOrderStatus(payResult.getValue());
    			
    			if(null!=book.getCompleteTime()){
    				detail.setCompleteTime(sdfal.format(book.getCompleteTime()));
    			}
    			
    			details.add(detail);
    		}
    	}
    	
    	respVo.setDetails(details);
    	
    	// 是否存在下一页
    	if(pageDate.getTotal()<=pageDate.getPageSize() || pageDate.getPageNumber()*pageDate.getPageSize() >= pageDate.getTotal()){
			respVo.setHasNextPage("N");
		}else{
			respVo.setHasNextPage("Y");
		}
    	
    	respVo.setMsgCode(PortalCode.CODE_0000);
		respVo.setMsgInfo("成功");
		
		return respVo;
	}



	private String checkAccountSearchParams(AccountBalanceQueryReqVo reqVo) {
		if(reqVo == null){
			return "请求对象为空";
		}
		if(StringUtil.isEmpty(reqVo.getCustId())) {
			return "一户通账号为空";
		}
		if(StringUtil.isEmpty(reqVo.getAccountType())) {
			return "账户业务类型为空";
		}
		if(reqVo.getCustId().length()>12) {
			return "一户通账号长度过长";
		}
		if(reqVo.getAccountType().length()>4) {
			return "账户业务类型长度过长";
		}
		
		return null;
	}
	
	private String checkOrderDetailSearchParams(OrderDetailsQueryReqVo reqVo) {
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
		if(StringUtil.isEmpty(reqVo.getPageNo())) {
			return "查询页号必填初始可为1";
		}
		
		if(reqVo.getComCode().length()>16) {
			return "商户代码长度过长";
		}
		if(reqVo.getStartDate().length()!=8) {
			return "查询起始日期格式YYYYMMDD";
		}
		if(reqVo.getEndDate().length()!=8) {
			return "查询结束日期格式YYYYMMDD";
		}
		int pageNo = -1;
		try {
			pageNo = Integer.valueOf(reqVo.getPageNo());
		} catch (NumberFormatException e) {}
		if(pageNo<1) {
			return "查询页号必填初始可为1";
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
