package com.ylink.inetpay.cbs.mrs.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.redrock.ips.support.cache.redis.RedisCacheUtil;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.mrs.service.MrsOrganService;
import com.ylink.inetpay.cbs.mrs.service.MrsPersonService;
import com.ylink.inetpay.cbs.mrs.service.RedisLock;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.MrsAccountType;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.exception.PortalCode;
import com.ylink.inetpay.common.project.portal.vo.IndividualRequestVO;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchRequestVO;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchResponseVO;
import com.ylink.inetpay.common.project.portal.vo.UnitRequestVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.AccountSearchReqVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.AccountSearchRespVO;
import net.sf.json.JSONObject;
@Service("mrsSearchRestService")
public class MrsSearchRestServiceImpl implements MrsSearchRestService {

	private static Logger log = LoggerFactory.getLogger(MrsSearchRestServiceImpl.class);
	
	@Autowired
	private MrsPersonService mrsPersonService;
	@Autowired
	private MrsOrganService mrsOrganService;
	@Autowired
	private  BisExceptionLogAppService bisExceptionLogAppService;
	@Override
	public LoginMsgSearchResponseVO loginMsgSearch(String params) {
		log.info("--------进入登录信息查询方法：receive:"+params);
		LoginMsgSearchRequestVO reqVo = new LoginMsgSearchRequestVO();
    	LoginMsgSearchResponseVO respVo = new LoginMsgSearchResponseVO();
    	String lockKey = "";
    	try {
    		log.debug("-----------第一步：转换LoginMsgSearchRequestVO对象");
    		reqVo = toLoginMsgSearchRequestVO(params);
    		
    		// 解析参数封装成request对象
    		log.debug("-----------第二步，校验入参参数：");
    		String checkResult = checkLongMsgSearchParams(reqVo);
        	if(StringUtil.isNEmpty(checkResult)){
        		log.error(checkResult);
    			respVo.setMsgCode(PortalCode.CODE_9999);
    			respVo.setMsgInfo(checkResult);
    			return respVo;
        	}
        	//获取客户类型
        	String customerType = reqVo.getCustomerType();
        	if(MrsCustomerType.MCT_0.getValue().equals(customerType)) {
        		// 个人查询密码
        		lockKey = RedisLock.getPersonLockKey(reqVo.getCertType(), reqVo.getCertNo());
    			boolean flag = RedisCacheUtil.setNXWithWait(lockKey, System.currentTimeMillis()+"", MrsPassiveOpenAcntServiceImpl.TIME_OUT);
    			if(!flag) {
    				log.info("并发锁密码查询失败:[number={}]", reqVo.getCertNo());
    				respVo.setMsgCode(PortalCode.CODE_9999);
    				respVo.setMsgInfo("查询失败");
    				return respVo;
    			} 
				log.info("个人密码查询调用mrsPersonService.findLoginMsg方法");
        		respVo = mrsPersonService.findLoginMsg(reqVo);
        		
        	} else if(MrsCustomerType.MCT_1.getValue().equals(customerType)) {
        		// 机构查询密码
        		String socialCreditCode = reqVo.getSocialCreditCode();
        		String organizeCode = reqVo.getOrganizeCode();
        		String revenueCode = reqVo.getRevenueCode();
        		String businessLicence = reqVo.getBusinessLicence();
        		lockKey = RedisLock.getOrganLockKey(socialCreditCode, organizeCode, revenueCode, businessLicence,null);
        		boolean flag = RedisCacheUtil.setNXWithWait(lockKey, System.currentTimeMillis()+"", MrsPassiveOpenAcntServiceImpl.TIME_OUT);
    			if(!flag) {
    				log.info("并发锁密码查询失败:[number={}]", reqVo.getCertNo());
    				respVo.setMsgCode(PortalCode.CODE_9999);
    				respVo.setMsgInfo("查询失败");
    				return respVo;
    			} 
        		log.info("机构密码查询调用mrsPersonService.findLoginMsg方法");
        		respVo = mrsOrganService.findLoginMsg(reqVo);
        	} 
        	if(respVo == null) {
        		log.info("三要素信息[name={}]不存在   开始调用被动开户",reqVo.getCustomerName());
        		// 调用被动开户 
        		MrsAccountDto account = passiveOpenAcnt(reqVo);
        		respVo = new LoginMsgSearchResponseVO();
        		respVo.setCustId(account.getCustId());
        		respVo.setLoginPwd(account.getNoEncryptloginPwd());
    			respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_0);
        	}
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("查询成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("登录信息查询完成,返回json对象：{}",jsons.toString());
			return respVo;
    	} catch (CodeCheckedException e) {
			log.error("密码查询失败："+e.getMessage());
			respVo.setMsgCode(e.getCode());
			respVo.setMsgInfo(e.getMessage());
		}  catch (Exception e) {
    		log.error("密码查询失败:", e);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("密码查询失败");
			String customerType = reqVo.getCustomerType();
			String errorMsg = "";
			if(MrsCustomerType.MCT_0.getValue().equals(customerType)) {
        		// 个人查询密码
				errorMsg = String.format("密码查询失败,客户类型：%s,客户名称：%s,证件类型：%s,证件号码：%s",
						reqVo.getCustomerType(),reqVo.getCustomerName(),
						reqVo.getCertType(),reqVo.getCertNo());
			} else if (MrsCustomerType.MCT_1.getValue().equals(customerType)) {
				// 机构查询密码
				errorMsg = String.format("密码查询失败,客户类型：%s,客户名称：%s,社会统一信用代码：%s,组织机构代码：%s,税务登记号码：%s,营业执照编码：%s",
						reqVo.getCustomerType(), reqVo.getCustomerName(), 
						reqVo.getSocialCreditCode(),reqVo.getOrganizeCode(),
						reqVo.getRevenueCode(),reqVo.getBusinessLicence());
			} 
			saveErrorExcetpionLog(errorMsg);
    		return respVo;
    	} finally {
    		if(StringUtil.isNEmpty(lockKey)) {
    			try {
					RedisCacheUtil.evict(lockKey);
				} catch (Exception e) {
					log.error("锁删除异常:", e);
				}
    		}
    	}
    	log.info("一户通[custId={}]密码查询成功",respVo.getCustId());
		return respVo;
	}

	/**
	 * 调用被动开户
	 * @param reqVo
	 * @throws Exception 
	 * @throws CodeCheckedException 
	 */
	private MrsAccountDto passiveOpenAcnt(LoginMsgSearchRequestVO reqVo) throws CodeCheckedException, Exception {
		String customerType = reqVo.getCustomerType();
		MrsAccountDto accountDto = null;
		// 根据接入平台编号查询平台信息
		MrsPlatformDto mrsPlatformDto = mrsPersonService.checkPlatform(reqVo.getPlatformCode());
		if(MrsCustomerType.MCT_0.getValue().equals(customerType)) {
    		// 个人被动开户
			log.info("调用个人被动开户");
			IndividualRequestVO requestVo = new IndividualRequestVO();
			String accountType = StringUtil.isEmpty(reqVo.getAccountType()) ? MrsAccountType.MAT_1.getValue() : reqVo.getAccountType();
			requestVo.setAccountType(accountType);
			requestVo.setCredentialsNumber(reqVo.getCertNo());
			requestVo.setCredentialsType(reqVo.getCertType());
			requestVo.setCustomerName(reqVo.getCustomerName());
			requestVo.setPlatformCode(reqVo.getPlatformCode());
			accountDto = mrsPersonService.initOpenAcnt(requestVo,mrsPlatformDto);
    	} else if(MrsCustomerType.MCT_1.getValue().equals(customerType)) {
    		// 机构被动开户
			log.info("调用机构被动开户");
			UnitRequestVO requestVo = new UnitRequestVO();
			String accountType = StringUtil.isEmpty(reqVo.getAccountType()) ? MrsAccountType.MAT_1.getValue() : reqVo.getAccountType();
			requestVo.setAccountType(accountType);
			requestVo.setBusinessLicence(reqVo.getBusinessLicence());
			requestVo.setCustomerName(reqVo.getCustomerName());
			requestVo.setOrganizeCode(reqVo.getOrganizeCode());
			requestVo.setPlatformCode(reqVo.getPlatformCode());
			requestVo.setRevenueCode(reqVo.getRevenueCode());
			requestVo.setSocialCreditCode(reqVo.getSocialCreditCode());
			accountDto = mrsOrganService.initOpenAcnt(requestVo,mrsPlatformDto);
    	} 
		return accountDto;
	}

	private String checkLongMsgSearchParams(LoginMsgSearchRequestVO reqVo) {
		if(reqVo == null){
			return "请求对象为空";
		}
		if(StringUtil.isNEmpty(reqVo.getAccountType())) {
			// 如果一户通类型非空  校验传入的值是否正确
			if(MrsAccountType.getEnum(reqVo.getAccountType()) == null) {
				return "一户通类型错误";
			}
		}
		if(StringUtil.isEmpty(reqVo.getCustomerName())) {
			return "名称为空";
		}
		if (StringUtil.isNEmpty(reqVo.getCustomerName())
				&& reqVo.getCustomerName().length() > 85) {
			return "名称超长";
		}
		if(StringUtil.isEmpty(reqVo.getPlatformCode())) {
			return "接入平台为空";
		}
		if (StringUtil.isNEmpty(reqVo.getPlatformCode())
				&& reqVo.getPlatformCode().length() > 16) {
			return "接入平台超长";
		}
		String customerType = reqVo.getCustomerType();
		if(StringUtil.isEmpty(customerType)){
			return "客户类型为空";
		}
		if(StringUtil.isNEmpty(customerType) && MrsCustomerType.getEnum(customerType) == null) {
			return "客户类型不正确";
		}
		if(MrsCustomerType.MCT_0.getValue().equals(customerType)) {
			// 个人客户
			// 三要素和一户通账号不能同时为空  
			if(StringUtil.isEmpty(reqVo.getCertType()) ||  StringUtil.isEmpty(reqVo.getCertNo()) ){
				return "三要素不全";	
			}
			if (StringUtil.isNEmpty(reqVo.getCertType())
					&& reqVo.getCertType().length() > 2) {
				return "证件类型超长";
			}
			if (StringUtil.isNEmpty(reqVo.getCertNo())
					&& reqVo.getCertNo().length() > 32) {
				return "证件号码超长";
			}
		} else if (MrsCustomerType.MCT_1.getValue().equals(customerType)){
			// 机构客户
			if(!(StringUtil.isNEmpty(reqVo.getBusinessLicence()) || StringUtil.isNEmpty(reqVo.getOrganizeCode()) || 
					StringUtil.isNEmpty(reqVo.getRevenueCode()) || StringUtil.isNEmpty(reqVo.getSocialCreditCode())) ){
				return "机构证件号码都为空";
			}
			if (StringUtil.isNEmpty(reqVo.getBusinessLicence())
					&& reqVo.getBusinessLicence().length() > 32) {
				return "营业执照编码超长";
			}
			if (StringUtil.isNEmpty(reqVo.getOrganizeCode())
					&& reqVo.getOrganizeCode().length() > 32) {
				return "组织机构代码超长";
			}
			if (StringUtil.isNEmpty(reqVo.getRevenueCode())
					&& reqVo.getRevenueCode().length() > 32) {
				return "税务登记号码超长";
			}
			if (StringUtil.isNEmpty(reqVo.getSocialCreditCode())
					&& reqVo.getSocialCreditCode().length() > 32) {
				return "社会统一信用代码超长";
			}
		}
		
		
		return null;
	}
	
	@Override
	public AccountSearchRespVO accountSearch(String params) throws Exception {
		log.info("--------进入账户查询方法：receive:"+params);
		AccountSearchReqVO reqVo = null;
		AccountSearchRespVO respVo = new AccountSearchRespVO();
    	try {
    		log.debug("-----------第一步：转换AccountSearchReqVO对象");
    		reqVo = toAccountSearchRequestVO(params);
    		
    		// 解析参数封装成request对象
    		log.debug("-----------第二步，校验入参参数：");
    		String checkResult = checkAccountSearchParams(reqVo);
        	if(StringUtil.isNEmpty(checkResult)){
        		log.error(checkResult);
    			respVo.setMsgCode(PortalCode.CODE_9999);
    			respVo.setMsgInfo(checkResult);
    			return respVo;
        	}
        	
        	String customerType = reqVo.getCustomerType();
        	if(MrsCustomerType.MCT_0.getValue().equals(customerType)) {
        		// 个人查询密码
				log.info("个人密码查询调用mrsPersonService.findAccountExist方法");
        		respVo = mrsPersonService.findAccountExist(reqVo);
        		
        	} else if(MrsCustomerType.MCT_1.getValue().equals(customerType)) {
        		// 机构查询密码
        		log.info("机构密码查询调用mrsPersonService.findAccountExist方法");
        		respVo = mrsOrganService.findAccountExist(reqVo);
        	} 
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("查询成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("账户查询完成,返回json对象：{}",jsons.toString());
			return respVo;
    	} catch (CodeCheckedException e) {
			log.error("账户查询失败："+e.getMessage());
			respVo.setMsgCode(e.getCode());
			respVo.setMsgInfo(e.getMessage());
		} catch (Exception e) {
    		log.error("账户查询失败:", e);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("账户查询失败");
			String customerType = reqVo.getCustomerType();
			String errorMsg = "";
			if(MrsCustomerType.MCT_0.getValue().equals(customerType)) {
        		// 个人查询账户
				errorMsg = String.format("账户查询失败,客户类型：%s,客户名称：%s,证件类型：%s,证件号码：%s",
						reqVo.getCustomerType(),reqVo.getCustomerName(),
						reqVo.getCertType(),reqVo.getCertNo());
			} else if (MrsCustomerType.MCT_1.getValue().equals(customerType)) {
				// 机构查询账户
				errorMsg = String.format("账户查询失败,客户类型：%s,客户名称：%s,社会统一信用代码：%s,组织机构代码：%s,税务登记号码：%s,营业执照编码：%s",
						reqVo.getCustomerType(), reqVo.getCustomerName(), 
						reqVo.getSocialCreditCode(),reqVo.getOrganizeCode(),
						reqVo.getRevenueCode(),reqVo.getBusinessLicence());
			} 
			saveErrorExcetpionLog(errorMsg);
    	}
		return respVo;
	}

	private String checkAccountSearchParams(AccountSearchReqVO reqVo) {
		if(reqVo == null){
			return "请求对象为空";
		}
		if(StringUtil.isEmpty(reqVo.getCustomerName())) {
			return "名称为空";
		}
		if(StringUtil.isEmpty(reqVo.getPlatformCode())) {
			return "接入平台为空";
		}
		String customerType = reqVo.getCustomerType();
		if(StringUtil.isEmpty(customerType)){
			return "客户类型为空";
		}
		if(!MrsCustomerType.MCT_0.getValue().equals(customerType) && !MrsCustomerType.MCT_1.getValue().equals(customerType)) {
			return "客户类型不正确";
		}
		if(MrsCustomerType.MCT_0.getValue().equals(customerType)) {
			// 个人客户
			// 三要素和一户通账号不能同时为空  
			if(StringUtil.isEmpty(reqVo.getCertType()) || StringUtil.isEmpty(reqVo.getCertNo()) ){
				return "三要素不全";	
			}
		} else if (MrsCustomerType.MCT_1.getValue().equals(customerType)){
			// 机构客户
			if(!(StringUtil.isNEmpty(reqVo.getBusinessLicence()) || StringUtil.isNEmpty(reqVo.getOrganizeCode()) || 
					StringUtil.isNEmpty(reqVo.getRevenueCode()) || StringUtil.isNEmpty(reqVo.getSocialCreditCode())) ){
				return "机构证件号码都为空";
			}
		}
		return null;
	}

	private AccountSearchReqVO toAccountSearchRequestVO(String params) throws Exception {
		AccountSearchReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params,AccountSearchReqVO.class);
			if(requestVo == null){
				log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("转换对象为空、Gson对象转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：",e);
			throw e;
		}
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
	/**
	 * 记录异常日志
	 * @param errorMsg
	 */
	private void saveErrorExcetpionLog(String errorMsg){
		BisExceptionLogDto	dto=new BisExceptionLogDto();
		dto.setSystem(EBisSmsSystem.CBS);
		dto.setNlevel(EBisExceptionLogNlevel.ERROR);
		dto.setType(EBisExceptionLogType.MRS_REST_CUST);
		dto.setContent(errorMsg);
		dto.setAllpath(CbsConfig.getLogFullPath());
		try {
			bisExceptionLogAppService.saveLog(dto);
		} catch (Exception e) {
			log.error("会员密码查询,记录异常日志失败！");
		}
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
}
