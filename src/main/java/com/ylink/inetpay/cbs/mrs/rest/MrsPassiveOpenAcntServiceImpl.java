package com.ylink.inetpay.cbs.mrs.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.redrock.ips.support.cache.redis.RedisCacheUtil;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.mrs.Constants;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.cbs.mrs.service.MrsDataParamService;
import com.ylink.inetpay.cbs.mrs.service.MrsOrganService;
import com.ylink.inetpay.cbs.mrs.service.MrsPersonService;
import com.ylink.inetpay.cbs.mrs.service.MrsProductService;
import com.ylink.inetpay.cbs.mrs.service.RedisLock;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.MrsAccountType;
import com.ylink.inetpay.common.core.constant.MrsBusinessSortCode;
import com.ylink.inetpay.common.core.constant.MrsBusinessSortSubCode;
import com.ylink.inetpay.common.core.constant.MrsCredentialsType;
import com.ylink.inetpay.common.core.constant.MrsDataParamType;
import com.ylink.inetpay.common.core.constant.MrsEducationCode;
import com.ylink.inetpay.common.core.constant.MrsProductType;
import com.ylink.inetpay.common.core.constant.MrsSexCode;
import com.ylink.inetpay.common.core.constant.RegexEnum;
import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataParamDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.exception.PortalCode;
import com.ylink.inetpay.common.project.portal.vo.IndividualRequestVO;
import com.ylink.inetpay.common.project.portal.vo.IndividualResponseVO;
import com.ylink.inetpay.common.project.portal.vo.ProductRequestVO;
import com.ylink.inetpay.common.project.portal.vo.ProductResponseVO;
import com.ylink.inetpay.common.project.portal.vo.UnitAssetRequestVo;
import com.ylink.inetpay.common.project.portal.vo.UnitRequestVO;
import com.ylink.inetpay.common.project.portal.vo.UnitResponseVO;
import net.sf.json.JSONObject;
@Service("mrsPassiveOpenAcntService")
public class MrsPassiveOpenAcntServiceImpl implements MrsPassiveOpenAcntService{
	
	private static Logger log = LoggerFactory.getLogger(MrsPassiveOpenAcntServiceImpl.class);
	
	public static final long TIME_OUT = 60L;
	@Autowired
	private MrsPersonService mrsPersonService;
	@Autowired
	private MrsOrganService mrsOrganService;
	@Autowired
	private MrsProductService mrsProductService;
	@Autowired
	private  BisExceptionLogAppService bisExceptionLogAppService;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private MrsDataParamService mrsDataParamService;
	@Override
	public IndividualResponseVO individualCust(String params) {
    	IndividualRequestVO individualReqVo = null;
    	IndividualResponseVO respVo = new IndividualResponseVO();
    	String lockKey = "";
    	try {
	    	log.info("--------进入个人被动开户方法：receive:"+params);
			log.debug("-----------第一步：转换IndividualRequestVO对象");
			individualReqVo = toIndividualsRequestVO(params);
			
			// 解析参数封装成request对象
			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkIndividualParams(individualReqVo);
	    	if(checkResult != null){
	    		log.error("参数["+params+"]校验失败:"+checkResult);
	    		respVo.setMsgCode(PortalCode.CODE_9999);
	    		respVo.setMsgInfo(checkResult);
				return respVo;
	    	}
	    	// 根据接入平台编号查询平台信息
			MrsPlatformDto mrsPlatformDto = mrsPersonService.checkPlatform(individualReqVo.getPlatformCode());
	    	lockKey = RedisLock.getPersonLockKey(individualReqVo.getCredentialsType(), individualReqVo.getCredentialsNumber());
	    	boolean flag = RedisCacheUtil.setNXWithWait(lockKey, System.currentTimeMillis()+"",TIME_OUT);
	    	if(flag) {
	    		// 根据三要素查询客户信息表
	    		List<MrsAccountDto> actPersonList = mrsAccountService.findByPerson3Element(individualReqVo.getCustomerName(), 
		    			individualReqVo.getCredentialsType(), individualReqVo.getCredentialsNumber());
		    	// 客户信息
		    	MrsAccountDto accountDto = null;
		    	if(actPersonList == null || actPersonList.size()==0){
		    		log.info("系统不存在客户,新开一户通账户!");
		    		accountDto = mrsPersonService.initOpenAcnt(individualReqVo,mrsPlatformDto);
			    	respVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
		    	} else {
		    		// 客户存在判断是否为强制开户
		    		if(Constants.FORCE_1.equals(individualReqVo.getIsForce())) {
		    			log.info("系统存在客户...强制开户");
		    			accountDto = mrsPersonService.forceOpenAcnt(individualReqVo,mrsPlatformDto);
		    			respVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
		    		} else {
		    			log.info("系统存在客户...");
		    			accountDto = mrsPersonService.updateOpenAcnt(individualReqVo, actPersonList,mrsPlatformDto);
		    			//respVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
		    		}
		    	}
		    	String accountCode = accountDto.getCustId();
		    	respVo.setAccountCode(accountCode);
		    	respVo.setMsgCode(PortalCode.CODE_0000);
		    	respVo.setMsgInfo("开户成功");
		    	JSONObject jsons = JSONObject.fromObject(respVo);
				log.info("个人被动开户完成,返回json对象：{}",jsons.toString());
	    		return respVo;
	    	} else {
	    		log.info("并发锁被动开户失败：证件号码[number={}]",individualReqVo.getCredentialsNumber());
	    		respVo.setMsgCode(PortalCode.CODE_9999);
	    		respVo.setMsgInfo("开户失败");
	    		return respVo;
	    	}
		} catch (CodeCheckedException e) {
			log.error("开户失败："+e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("客户姓名：%s,证件类型：%s,证件号码：%s,个人客户被动开户失败:%s",
					individualReqVo.getCustomerName(), 
	    			individualReqVo.getCredentialsType(), 
	    			individualReqVo.getCredentialsNumber(),
	    			e.getMessage()));
		} catch (Exception e) {
			log.error("开户失败："+ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("开户失败");
			saveErrorExcetpionLog(String.format("客户姓名：%s,证件类型：%s,证件号码：%s,个人客户被动开户失败:%s",
					individualReqVo.getCustomerName(), 
	    			individualReqVo.getCredentialsType(), 
	    			individualReqVo.getCredentialsNumber(),
	    			e.getMessage()));
		} finally {
    		if(StringUtil.isNEmpty(lockKey)) {
    			try {
					RedisCacheUtil.evict(lockKey);
				} catch (Exception e) {
					log.error("锁删除异常:", e);
				}
    		}
    	}
		return respVo;
	}
	
	@Override
	public UnitResponseVO unit(String params) {
		log.info("--------进入巨灾被动开户方法：receive:"+params);
    	UnitRequestVO unitReqVo = new UnitRequestVO();
    	UnitResponseVO unitRespVo = new UnitResponseVO();
    	String lockKey = "";
		log.debug("-----------第一步：转换UnitRequestVO对象");
		try {
			unitReqVo = toUnitRequestVO(params);
			// 解析参数封装成request对象
			log.debug("-----------第二步，校验入参参数");
			String checkResult = checkUnitParams(unitReqVo);
			if(checkResult != null){
				log.error("参数["+params+"]校验失败:"+checkResult);
				unitRespVo.setMsgCode(PortalCode.CODE_9999);
				unitRespVo.setMsgInfo(checkResult);
				return unitRespVo;
			}
			// 根据接入平台编号查询平台信息
			MrsPlatformDto mrsPlatformDto = mrsPersonService.checkPlatform(unitReqVo.getPlatformCode());
			String customerName = unitReqVo.getCustomerName();
			String socialCreditCode = unitReqVo.getSocialCreditCode();
			String organizeCode = unitReqVo.getOrganizeCode();
			String revenueCode = unitReqVo.getRevenueCode();
			String businessLicence = unitReqVo.getBusinessLicence();
			lockKey = RedisLock.getOrganLockKey(socialCreditCode, organizeCode, revenueCode, businessLicence,null);
	    	boolean flag = RedisCacheUtil.setNXWithWait(lockKey, System.currentTimeMillis()+"", TIME_OUT);
			if (flag) {
				// 根据三要素获取机构客户信息
				List<MrsAccountDto> organList = mrsAccountService.findByOrgan3Element(customerName, socialCreditCode,
						organizeCode, revenueCode, businessLicence, null);
				// 判断客户信息是否存在
				List<MrsAccountDto> existOrgan = organIsExist(organList, socialCreditCode, organizeCode, revenueCode,
						businessLicence);
				MrsAccountDto accountDto = null;
				if (CollectionUtil.isEmpty(existOrgan)) {
					// 不存在创建
					log.info("系统不存在客户,新开一户通账户!");
					accountDto = mrsOrganService.initOpenAcnt(unitReqVo, mrsPlatformDto);
					unitRespVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
				} else {
					log.info("系统存在客户!");// 存在，更新客户信息
					accountDto = mrsOrganService.updateOpenAcnt(unitReqVo, organList, mrsPlatformDto);
					//unitRespVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
				}
				unitRespVo.setAccountCode(accountDto.getCustId());
				unitRespVo.setMsgCode(PortalCode.CODE_0000);
				unitRespVo.setMsgInfo("开户成功");
				JSONObject jsons = JSONObject.fromObject(unitRespVo);
				log.info("巨灾机构被动开户完成,返回json对象：{}",jsons.toString());
				return unitRespVo;
			} else {
	    		log.info("并发锁被动开户失败：证件号码[name={}]",unitReqVo.getCustomerName());
	    		unitRespVo.setMsgCode(PortalCode.CODE_9999);
	    		unitRespVo.setMsgInfo("开户失败");
	    		return unitRespVo;
	    	}
		} catch (CodeCheckedException e) {
			log.error("开户失败："+e.getMessage());
			unitRespVo.setMsgCode(PortalCode.CODE_9999);
			unitRespVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("客户姓名：%s,社会统一信用代码：%s,组织机构代码：%s,税务登记号码：%s,营业执照编码：%s,机构客户被动开户失败:%s",
					unitReqVo.getCustomerName(), unitReqVo.getSocialCreditCode(), 
					unitReqVo.getOrganizeCode(), unitReqVo.getRevenueCode(), 
					unitReqVo.getBusinessLicence(),
	    			e.getMessage()));
		} catch (Exception e) {
			log.error("开户失败：", e);
			unitRespVo.setMsgCode(PortalCode.CODE_9999);
			unitRespVo.setMsgInfo("开户失败");
			saveErrorExcetpionLog(String.format("客户姓名：%s,社会统一信用代码：%s,组织机构代码：%s,税务登记号码：%s,营业执照编码：%s,机构客户被动开户失败:%s",
					unitReqVo.getCustomerName(), unitReqVo.getSocialCreditCode(), 
					unitReqVo.getOrganizeCode(), unitReqVo.getRevenueCode(), 
					unitReqVo.getBusinessLicence(),
	    			e.getMessage()));
		} finally {
    		if(StringUtil.isNEmpty(lockKey)) {
    			try {
					RedisCacheUtil.evict(lockKey);
				} catch (Exception e) {
					log.error("锁删除异常:", e);
				}
    		}
    	}
		return unitRespVo;
	}


	@Override
	public UnitResponseVO unitAsset(String params) {
		
		log.info("--------进入资管机构被动开户方法：receive:"+params);
		UnitAssetRequestVo unitAssetReqVo = new UnitAssetRequestVo();
    	UnitResponseVO unitRespVo = new UnitResponseVO();
    	String lockKey = "";
		log.debug("-----------第一步：转换UnitAssetRequestVo对象");
		try {
			unitAssetReqVo = toUnitAssetRequestVO(params);
			// 解析参数封装成request对象
			log.debug("-----------第二步，校验入参参数");
			String checkResult = checkAssetUnitParams(unitAssetReqVo);
			if(checkResult != null){
				
				log.error("参数["+params+"]校验失败:"+checkResult);
				unitRespVo.setMsgCode(PortalCode.CODE_9999);
				unitRespVo.setMsgInfo(checkResult);
				return unitRespVo;
			}
			//转换证件类型对应值
    		changeReqValue(unitAssetReqVo);
			// 根据接入平台编号查询平台信息
			MrsPlatformDto mrsPlatformDto = mrsPersonService.checkPlatform(unitAssetReqVo.getPlatformCode());
	    	String customerName = unitAssetReqVo.getCustomerName();
			String socialCreditCode = unitAssetReqVo.getSocialCreditCode();
			String organizeCode = unitAssetReqVo.getOrganizeCode();
			String revenueCode = unitAssetReqVo.getRevenueCode();
			String businessLicence = unitAssetReqVo.getBusinessLicence();
			String organOtherCode= unitAssetReqVo.getOrganOtherCode();
			lockKey = RedisLock.getOrganLockKey(socialCreditCode, organizeCode, revenueCode, businessLicence,organOtherCode);
	    	boolean flag = RedisCacheUtil.setNXWithWait(lockKey, System.currentTimeMillis()+"", TIME_OUT);
	    	if(flag) {
	    		// 根据三要素查询客户信息表
	    		List<MrsAccountDto> organList = mrsAccountService.findByOrgan3Element(customerName, 
	    				unitAssetReqVo.getSocialCreditCode(), unitAssetReqVo.getOrganizeCode(), 
	    				unitAssetReqVo.getRevenueCode(),unitAssetReqVo.getBusinessLicence(),
	    				unitAssetReqVo.getOrganOtherCode());
		    	// 客户信息
		    	MrsAccountDto accountDto = null;
		    	if(organList == null || organList.size()==0){
		    		log.info("系统不存在客户,新开一户通账户!");
		    		accountDto = mrsOrganService.initOpenAssetAcnt(unitAssetReqVo,mrsPlatformDto);
					unitRespVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
		    	} else {
		    		// 客户存在判断是否为强制开户
		    		if(Constants.FORCE_1.equals(unitAssetReqVo.getIsForce())) {
		    			log.info("系统存在客户...强制开户");
		    			accountDto = mrsOrganService.forceOpenAcnt(unitAssetReqVo,mrsPlatformDto);
		    			unitRespVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
		    		} else {
		    			log.info("系统存在客户...");
		    			accountDto = mrsOrganService.updateOpenAcntAsset(unitAssetReqVo, organList,mrsPlatformDto);
		    			//unitRespVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
		    		}
		    	}
				unitRespVo.setAccountCode(accountDto.getCustId());
				unitRespVo.setMsgCode(PortalCode.CODE_0000);
				unitRespVo.setMsgInfo("开户成功");
				JSONObject jsons = JSONObject.fromObject(unitRespVo);
				log.info("资管机构被动开户完成,返回json对象：{}",jsons.toString());
				return unitRespVo;
	    	} else {
	    		log.info("并发锁被动开户失败：证件号码[name={}]",unitAssetReqVo.getCustomerName());
	    		unitRespVo.setMsgCode(PortalCode.CODE_9999);
	    		unitRespVo.setMsgInfo("开户失败");
	    		return unitRespVo;
	    	}
		} catch (CodeCheckedException e) {
			log.error("开户失败："+e.getMessage());
			unitRespVo.setMsgCode(PortalCode.CODE_9999);
			unitRespVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("客户姓名：%s,证件类型：%s,证件号码：%s,机构客户被动开户失败:%s",
					unitAssetReqVo.getCustomerName(), unitAssetReqVo.getCredentialsType(), 
					unitAssetReqVo.getCredentialsNumber(), 
	    			e.getMessage()));
		} catch (Exception e) {
			log.error("开户失败：", e);
			unitRespVo.setMsgCode(PortalCode.CODE_9999);
			unitRespVo.setMsgInfo("开户失败");
			saveErrorExcetpionLog(String.format("客户姓名：%s,证件类型：%s,证件号码：%s,机构客户被动开户失败:%s",
					unitAssetReqVo.getCustomerName(), unitAssetReqVo.getCredentialsType(), 
					unitAssetReqVo.getCredentialsNumber(), 
	    			e.getMessage()));
		} finally {
    		if(StringUtil.isNEmpty(lockKey)) {
    			try {
					RedisCacheUtil.evict(lockKey);
				} catch (Exception e) {
					log.error("锁删除异常:", e);
				}
    		}
    	}
		return unitRespVo;
	}
	/**
	 * 转换证件类型对应值
	 * @param unitAssetReqVo
	 */
	private void changeReqValue(UnitAssetRequestVo unitAssetReqVo) {
		//特殊处理，先清除原来的四个字段值，有可能是传的是巨灾的接口 参数
		unitAssetReqVo.setSocialCreditCode("");
		unitAssetReqVo.setOrganizeCode("");
		unitAssetReqVo.setRevenueCode("");
		unitAssetReqVo.setBusinessLicence("");
		unitAssetReqVo.setOrganOtherCode("");
		
		if(MrsCredentialsType.MCT_74.getValue().equals(unitAssetReqVo.getCredentialsType())){
			unitAssetReqVo.setSocialCreditCode(unitAssetReqVo.getCredentialsNumber());
		}else if(MrsCredentialsType.MCT_71.getValue().equals(unitAssetReqVo.getCredentialsType())){
			unitAssetReqVo.setOrganizeCode(unitAssetReqVo.getCredentialsNumber());
		}else if(MrsCredentialsType.MCT_72.getValue().equals(unitAssetReqVo.getCredentialsType())){
			unitAssetReqVo.setRevenueCode(unitAssetReqVo.getCredentialsNumber());
		}else if(MrsCredentialsType.MCT_73.getValue().equals(unitAssetReqVo.getCredentialsType())){
			unitAssetReqVo.setBusinessLicence(unitAssetReqVo.getCredentialsNumber());
		}else{
			unitAssetReqVo.setOrganOtherCode(unitAssetReqVo.getCredentialsNumber());
		}
	}

	/**
	 * 判断机构是否存在
	 * 	查询出来的数据和入参“对应的”证件号码都非空时，比较证件号码是否一致，如果非空数据都一致则数据一致
	 * 例如1：
		 * 	数据行： 证件1(100)
		 * 	入参：   证件1(100),证件2(200)
		 * 以上数据匹配
	 * 例如2：
		 * 	数据行： 证件1(100),证件2(201)
		 * 	入参：   证件1(100),证件2(200)
		 * 以上数据不匹配
	 * 例如3：
		 * 	数据行1： 证件1(100),证件2(201)
		 * 	数据行2：   证件1(100),证件2(200)
		 * 	入参：   证件1(100),证件2(200),证件3(300)
		 * 和数据行2匹配，将数据行2的数据进行更新
	 * 例如：
		 * 	数据行1： 证件1(100)
		 * 	数据行1： 证件2(200)
		 * 	入参：   证件1(100),证件2(200)
	 * 和数据行1、数据行2匹配，将数据行1和数据行2的数据进行更新
	 * 以上数据匹配
	 * @param organSet
	 * @param unitReqVo
	 * @return
	 */
	public static List<MrsAccountDto> organIsExist(List<MrsAccountDto> organList, String socialCreditCode, 
			String organizeCode, String revenueCode, String businessLicence) {
		// 逐条判断是否有数据
		boolean flag = false;
		List<MrsAccountDto> retOrg = new ArrayList<MrsAccountDto>();
		for (MrsAccountDto dto : organList) {
			flag = true;
			if(StringUtil.isNEmpty(dto.getSocialCreditCode()) && StringUtil.isNEmpty(socialCreditCode) ){
				if(!dto.getSocialCreditCode().equals(socialCreditCode)){
					flag = false;
				}
			}
			if(StringUtil.isNEmpty(dto.getOrganizeCode()) && StringUtil.isNEmpty(organizeCode) ){
				if(!dto.getOrganizeCode().equals(organizeCode)){
					flag = false;
				}
			}
			if(StringUtil.isNEmpty(dto.getRevenueCode()) && StringUtil.isNEmpty(revenueCode) ){
				if(!dto.getRevenueCode().equals(revenueCode)){
					flag = false;
				}
			}
			if(StringUtil.isNEmpty(dto.getBusinessLicence()) && StringUtil.isNEmpty(businessLicence) ){
				if(!dto.getBusinessLicence().equals(businessLicence)){
					flag = false;
				}
			}
			if(flag){
				retOrg.add(dto);
			}
		}
		return retOrg;
	}
	
	
	/**
	 * 检查机构客户开户的必需参数（资管）
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkUnitParams( UnitRequestVO unitReqVo) {
		if(unitReqVo == null){
			return "UnitRequestVO 对象问空";
		}
		if(StringUtil.isEmpty(unitReqVo.getAccountType())) {
			return "账户类型为空";
		}
		/*if(!MrsAccountType.MAT_1.getValue().equals(unitReqVo.getAccountType())) {
			return "只能开保险一户通";
		}*/
		if(MrsAccountType.getEnum(unitReqVo.getAccountType()) == null) {
			return "一户通类别不存在";
		}
		String customerName = unitReqVo.getCustomerName();
		if(StringUtil.isEmpty(customerName)) {
			return "客户名称为空";
		}
		if(customerName.length() > 85){
			return "客户名称超长";
		}
		
		if(StringUtil.isEmpty(unitReqVo.getPlatformCode())) {
			return "开户渠道为空";
		} else {
			// 渠道非空校验
			if(SHIEConfigConstant.GS_CORE.equals(unitReqVo.getPlatformCode())) {
				// ecif渠道
				if(StringUtil.isEmpty(unitReqVo.getCustomerCode())) {
					return "客户编号为空";
				}
				if (StringUtil.isNEmpty(unitReqVo.getCustomerCode())
						&& unitReqVo.getCustomerCode().length() > 32) {
					return "客户编号超长";
				}
			} else {
				// 非ecif渠道
					return "接入平台编号错误，此接口只能是巨灾系统调用！";
			}
		}
		if(StringUtil.isEmpty(unitReqVo.getSocialCreditCode()) && StringUtil.isEmpty(unitReqVo.getOrganizeCode()) &&
				StringUtil.isEmpty(unitReqVo.getRevenueCode()) && StringUtil.isEmpty(unitReqVo.getBusinessLicence()) ) {
			return "四个证件类型都为空";
		}
		String sortCode = unitReqVo.getBusinessSortCode();
		if(StringUtil.isNEmpty(sortCode)  && MrsBusinessSortCode.getEnum(sortCode) == null) {
			return "机构类型不存在";	
		}
		String subCode = unitReqVo.getBusinessSortSubCode();
		if(StringUtil.isNEmpty(subCode)) {
			boolean flag = false;
			if(StringUtil.isNEmpty(sortCode)) {
				// 机构类型不为空  校验子类型是否属于主机构类型
				List<MrsBusinessSortSubCode> list = MrsBusinessSortSubCode.getSubCodeEnumByParentCode(MrsBusinessSortCode.getEnum(sortCode));
				for (MrsBusinessSortSubCode tempSubCode : list) {
					if(tempSubCode.getValue().equals(subCode)){
						flag = true;
						break;
					}
				}
				if(!flag) {
					return "机构类型和子机构类型不匹配";
				}
			} else {
				if(MrsBusinessSortSubCode.getEnum(subCode) == null) {
					return "子机构类型不存在";				
				}
			}
		}
		String businessLicence = unitReqVo.getBusinessLicence();
		if(StringUtil.isNEmpty(businessLicence) && businessLicence.length() > 64){
			return "营业执照编号超长";
		}
		String revenueCode = unitReqVo.getRevenueCode();
		if(StringUtil.isNEmpty(revenueCode) && revenueCode.length() > 32){
			return "税务登记号超长";
		}
		String organizeCode = unitReqVo.getOrganizeCode();
		if(StringUtil.isNEmpty(organizeCode) && organizeCode.length() > 32){
			return "组织机构代码超长";
		}
		String socialCreditCode = unitReqVo.getSocialCreditCode();
		if(StringUtil.isNEmpty(socialCreditCode) && socialCreditCode.length() > 128){
			return "社会统一信用代码超长";
		}
		String shortName = unitReqVo.getCustomerShortName();
		if(StringUtil.isNEmpty(shortName) && shortName.length() > 21){
			return "机构简称超长";
		}
		String eName = unitReqVo.getCustomerEname();
		if(StringUtil.isNEmpty(eName) && shortName.length() > 128) {
			return "机构英文名称超长";
		}
		String notionalityCode = unitReqVo.getNationalityCode();
		if(StringUtil.isNEmpty(notionalityCode)) {
			MrsDataParamDto dto = mrsDataParamService.findByCodeType(notionalityCode, MrsDataParamType.TYPE_01.getValue());
			if(dto == null){
				return "国籍或地区代码不正确";
			}
		}
		String endDate = unitReqVo.getBusinessLicenceEndDate();
		if(StringUtil.isNEmpty(endDate)) {
			if(endDate.length() != 8  && !Pattern.compile(RegexEnum.REGEX_DIGIT8.getRegexValue()).matcher(endDate).matches()) {
				return "营业执照有效期不合法";	
			}
		}
		String registerAddr = unitReqVo.getRegisteredAddr();
		if(StringUtil.isNEmpty(registerAddr) && registerAddr.length() > 85){
			return "注册地址超长";
		}
		String authPersonName = unitReqVo.getAuthPersonName();
		if(StringUtil.isNEmpty(authPersonName) && authPersonName.length() > 85){
			return "法人姓名超长";
		}
		String authIdType = unitReqVo.getAuthPersonIdentifyTypeCode();
		if(StringUtil.isNEmpty(authIdType)){
			if(MrsCredentialsType.getEnum(authIdType) == null) {
				return "法人证件类型不存在";
			}
		}
		String authCertNumber = unitReqVo.getAuthPersonIdentifyNo();
		if(StringUtil.isNEmpty(authCertNumber) && authCertNumber.length() > 16) {
			return "证件号码超长" ;
		}
		String contName = unitReqVo.getContactsName();
		if(StringUtil.isNEmpty(contName) && contName.length() > 85) {
			return "联系人姓名超长" ;
		}
		String zipCode = unitReqVo.getContactsZip();
		if(StringUtil.isNEmpty(zipCode) 
				&& !Pattern.compile(RegexEnum.REGEX_POST.getRegexValue()).matcher(zipCode).matches()) {
			return "邮政编码错误";
		}
		String contAddr = unitReqVo.getContactsAddr();
		if(StringUtil.isNEmpty(contAddr) && contAddr.length() > 85){
			return "联系人地址超长";
		}
		String email = unitReqVo.getContactsEmail();
		if(StringUtil.isNEmpty(email) && email.length() > 64){
			return "联系人电子邮箱超长";
		}
		String mobile = unitReqVo.getContactsMoblie();
		if(StringUtil.isNEmpty(mobile) && mobile.length() > 16){
			return "联系人手机号码超长";
		}
		String tel = unitReqVo.getContactsTel();
		if(StringUtil.isNEmpty(tel) && tel.length() > 16){
			return "联系人固定电话号码超长";
		}
		String spareTel = unitReqVo.getContactsSpareTel();
		if(StringUtil.isNEmpty(spareTel) && spareTel.length() > 16){
			return "联系人备用电话超长";
		}
		String fax = unitReqVo.getContactsFax();
		if(StringUtil.isNEmpty(fax) && fax.length() > 16){
			return "传真超长";
		}
		return null;
	}
	/**
	 * 检查机构客户开户的必需参数（）
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkAssetUnitParams( UnitAssetRequestVo unitReqVo) {
		if(unitReqVo == null){
			return "UnitRequestVO 对象为空";
		}
		if(StringUtil.isEmpty(unitReqVo.getAccountType())) {
			return "账户类型为空";
		}
		/*if(!MrsAccountType.MAT_1.getValue().equals(unitReqVo.getAccountType())) {
			return "只能开保险一户通";
		}*/
		if(MrsAccountType.getEnum(unitReqVo.getAccountType()) == null) {
			return "一户通类别不存在";
		}
		String customerName = unitReqVo.getCustomerName();
		if(StringUtil.isEmpty(customerName)) {
			return "客户名称为空";
		}
		if(StringUtil.isNEmpty(customerName) && customerName.length() > 85){
			return "客户名称超长";
		}
		
		if (StringUtil.isEmpty(unitReqVo.getPlatformCode())) {
			return "接入平台编号为空";
		} else {
			if (StringUtil.isEmpty(unitReqVo.getPlatformCustCode())) {
				return "平台客户编号不能为空";
			}
		}
		if(StringUtil.isNEmpty(unitReqVo.getPlatformCode()) && unitReqVo.getPlatformCode().length() > 16){
			return "接入平台编号超长";
		}
		if(StringUtil.isNEmpty(unitReqVo.getPlatformCustCode()) && unitReqVo.getPlatformCustCode().length() > 32){
			return "平台客户编号超长";
		}
		if(MrsCredentialsType.getEnum(unitReqVo.getCredentialsType()) == null) {
			return "证件类型不存在";
		}
		if(StringUtil.isEmpty(unitReqVo.getCredentialsNumber())) {
			return "证件号码为空";
		}
		if(StringUtil.isNEmpty(unitReqVo.getCredentialsNumber()) && unitReqVo.getCredentialsNumber().length() > 128){
			return "证件号码超长";
		}
		String sortCode = unitReqVo.getBusinessSortCode();
		if(StringUtil.isNEmpty(sortCode)  && MrsBusinessSortCode.getEnum(sortCode) == null) {
			return "机构类型不存在";	
		}
		String subCode = unitReqVo.getBusinessSortSubCode();
		if(StringUtil.isNEmpty(subCode)) {
			boolean flag = false;
			if(StringUtil.isNEmpty(sortCode)) {
				// 机构类型不为空  校验子类型是否属于主机构类型
				List<MrsBusinessSortSubCode> list = MrsBusinessSortSubCode.getSubCodeEnumByParentCode(MrsBusinessSortCode.getEnum(sortCode));
				for (MrsBusinessSortSubCode tempSubCode : list) {
					if(tempSubCode.getValue().equals(subCode)){
						flag = true;
						break;
					}
				}
				if(!flag) {
					return "机构类型和子机构类型不匹配";
				}
			} else {
				if(MrsBusinessSortSubCode.getEnum(subCode) == null) {
					return "子机构类型不存在";				
				}
			}
		}
		String isForce = unitReqVo.getIsForce();
		if(StringUtil.isNEmpty(isForce)) {
			if(!isForce.equals(Constants.FORCE_0) && !isForce.equals(Constants.FORCE_1)) {
				return "强制开户标识错误";
			}
		}
		String businessLicence = unitReqVo.getBusinessLicence();
		if(StringUtil.isNEmpty(businessLicence) && businessLicence.length() > 32){
			return "营业执照编号超长";
		}
		String organizeCode = unitReqVo.getOrganizeCode();
		if(StringUtil.isNEmpty(organizeCode) && organizeCode.length() > 32){
			return "组织机构代码超长";
		}
		String socialCreditCode = unitReqVo.getSocialCreditCode();
		if(StringUtil.isNEmpty(socialCreditCode) && socialCreditCode.length() > 128){
			return "社会统一信用代码超长";
		}
		String shortName = unitReqVo.getCustomerShortName();
		if(StringUtil.isNEmpty(shortName) && shortName.length() > 21){
			return "机构简称超长";
		}
		String eName = unitReqVo.getCustomerEname();
		if(StringUtil.isNEmpty(eName) && shortName.length() > 128) {
			return "机构英文名称超长";
		}
		String notionalityCode = unitReqVo.getNationalityCode();
		if(StringUtil.isNEmpty(notionalityCode)) {
			MrsDataParamDto dto = mrsDataParamService.findByCodeType(notionalityCode, MrsDataParamType.TYPE_01.getValue());
			if(dto == null){
				return "国籍或地区代码不正确";
			}
		}
		String endDate = unitReqVo.getBusinessLicenceEndDate();
		if(StringUtil.isNEmpty(endDate)) {
			if(endDate.length() != 8  && !Pattern.compile(RegexEnum.REGEX_DIGIT8.getRegexValue()).matcher(endDate).matches()) {
				return "营业执照有效期不合法";	
			}
		}
		
		String registerAddr = unitReqVo.getRegisteredAddr();
		if(StringUtil.isNEmpty(registerAddr) && registerAddr.length() > 85){
			return "注册地址超长";
		}
		String authPersonName = unitReqVo.getAuthPersonName();
		if(StringUtil.isNEmpty(authPersonName) && authPersonName.length() > 85){
			return "法人姓名超长";
		}
		String authIdType = unitReqVo.getAuthPersonIdentifyTypeCode();
		if(StringUtil.isNEmpty(authIdType)){
			if(MrsCredentialsType.getEnum(authIdType) == null) {
				return "法人证件类型不存在";
			}
		}
		String authCertNumber = unitReqVo.getAuthPersonIdentifyNo();
		if(StringUtil.isNEmpty(authCertNumber) && authCertNumber.length() > 16){
			return "法人证件号码超长";
		}
		String contName = unitReqVo.getContactsName();
		if(StringUtil.isNEmpty(contName) && contName.length() > 85) {
			return "联系人姓名超长" ;
		}
		String zipCode = unitReqVo.getContactsZip();
		if(StringUtil.isNEmpty(zipCode) && !Pattern.compile(RegexEnum.REGEX_POST.getRegexValue()).matcher(zipCode).matches()) {
			return "邮政编码错误";
		}
		String contAddr = unitReqVo.getContactsAddr();
		if(StringUtil.isNEmpty(contAddr) && contAddr.length() > 85){
			return "联系人地址超长";
		}
		String email = unitReqVo.getContactsEmail();
		if(StringUtil.isNEmpty(email) && email.length() > 64){
			return "联系人电子邮箱超长";
		}
		String mobile = unitReqVo.getContactsMoblie();
		if(StringUtil.isNEmpty(mobile) && mobile.length() > 16){
			return "联系人手机号码超长";
		}
		String tel = unitReqVo.getContactsTel();
		if(StringUtil.isNEmpty(tel) && tel.length() > 16){
			return "联系人固定电话号码超长";
		}
		String spareTel = unitReqVo.getContactsSpareTel();
		if(StringUtil.isNEmpty(spareTel) && spareTel.length() > 16){
			return "联系人备用电话超长";
		}
		String fax = unitReqVo.getContactsFax();
		if(StringUtil.isNEmpty(fax) && fax.length() > 16){
			return "传真超长";
		}
		return null;
	}
	/**
	 * 检查产品客户开户的必需参数
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkProductParams( ProductRequestVO productReqVo) {
		if(productReqVo == null){
			return "ProductRequestVO 对象问空";
		}
		
		String accountType = productReqVo.getAccountType();
		if(StringUtil.isEmpty(accountType)) {
			return "一户通类别为空";
		}
		if(StringUtil.isNEmpty(accountType) && accountType.length() > 1){
			return "一户通类别超长";
		}
		if(!MrsAccountType.MAT_1.getValue().equals(productReqVo.getAccountType())) {
			return "只能开保险一户通";
		}
		if(MrsAccountType.getEnum(accountType) == null) {
			return "一户通类别不存在";
		}
		
		if(StringUtil.isEmpty(productReqVo.getPlatformCode())) {
			return "开户渠道为空";
		} else {
			// 渠道非空校验
			if(SHIEConfigConstant.GS_CORE.equals(productReqVo.getPlatformCode())) {
				// ecif渠道
				if(StringUtil.isEmpty(productReqVo.getCustomerCode())) {
					return "客户编号为空";
				}
				if (StringUtil.isNEmpty(productReqVo.getCustomerCode())
						&& productReqVo.getCustomerCode().length() > 32) {
					return "客户编号超长";
				}
				if(StringUtil.isNEmpty(productReqVo.getPlatformCustCode())) {
					return "平台客户编号必须为空";
				}
			} else {
				// 非ecif渠道
				if(StringUtil.isNEmpty(productReqVo.getCustomerCode())) {
					return "客户编号必须为空";
				}
				if(StringUtil.isEmpty(productReqVo.getPlatformCustCode())) {
					return "平台客户编号为空";
				}
				if (StringUtil.isNEmpty(productReqVo.getPlatformCustCode())
						&& productReqVo.getPlatformCustCode().length() > 32) {
					return "平台客户编号超长";
				}
			}
		}
		
		String productName= productReqVo.getProductName(); // 产品名称
		if(StringUtil.isEmpty(productName)) {
			return "产品名称为空";
		}
		if(StringUtil.isNEmpty(productName) && productName.length() > 42){
			return "产品名称超长";
		}
		String credentialsType= productReqVo.getCredentialsType(); // 证件类型
		if(StringUtil.isEmpty(credentialsType)) {
			return "证件类型为空";
		}
		if(StringUtil.isNEmpty(credentialsType) && credentialsType.length() > 2){
			return "证件类型超长";
		}
		if(StringUtil.isNEmpty(credentialsType)&&MrsCredentialsType.getEnum(credentialsType) == null) {
			return "证件类型不存在";
		}
		if(StringUtil.isNEmpty(credentialsType)&&!MrsCredentialsType.MCT_13.getValue().equals(credentialsType)) {
			return "证件类型只能是13批文";
		}
		String credentialsNumber= productReqVo.getCredentialsNumber(); // 证件号码
		if(StringUtil.isEmpty(credentialsNumber)) {
			return "证件号码为空";
		}
		if(StringUtil.isNEmpty(credentialsNumber) && credentialsNumber.length() > 32){
			return "证件号码超长";
		}
		String productTypeCode= productReqVo.getProductTypeCode(); // 产品类型代码
		if(StringUtil.isEmpty(productTypeCode)) {
			return "产品类型代码为空";
		}
		if(StringUtil.isNEmpty(productTypeCode) && productTypeCode.length() > 2){
			return "产品类型代码超长";
		}
		if(MrsProductType.getEnum(productTypeCode) == null) {
			return "产品类型代码不存在";
		}
		String isForce = productReqVo.getIsForce();
		if(StringUtil.isNEmpty(isForce)) {
			if(!isForce.equals(Constants.FORCE_0) && !isForce.equals(Constants.FORCE_1)) {
				return "强制开户标识错误";
			}
		}
		String productShortName= productReqVo.getProductShortName(); // 产品简称
		if(StringUtil.isNEmpty(productShortName) && productShortName.length() > 21){
			return "产品简称超长";
		}
		String productEndDate= productReqVo.getProductEndDate(); // 产品到期日期
		if(StringUtil.isNEmpty(productEndDate) && productEndDate.length() > 8){
			return "产品到期日期超长";
		}
		if(StringUtil.isNEmpty(productEndDate)) {
			if(!Pattern.compile(RegexEnum.REGEX_DIGIT8.getRegexValue()).matcher(productEndDate).matches()) {
				return "产品到期日期不合法";	
			}
		}
		String managerName= productReqVo.getManagerName(); // 资产管理人名称
		if(StringUtil.isNEmpty(managerName) && managerName.length() > 42){
			return "资产管理人名称超长";
		}
		String managerCerType= productReqVo.getManagerCerType(); // 资产管理人证件类型
		if(StringUtil.isNEmpty(managerCerType) && managerCerType.length() > 2){
			return "资产管理人证件类型超长";
		}
		if(StringUtil.isNEmpty(managerCerType)&&MrsCredentialsType.getEnum(managerCerType) == null) {
			return "资产管理人证件证件类型不存在";
		}
		String managerCerCode= productReqVo.getManagerCerCode(); // 资产管理人证件代码
		if(StringUtil.isNEmpty(managerCerCode) && managerCerCode.length() > 32){
			return "资产管理人证件代码超长";
		}
		
		String trusteeName= productReqVo.getTrusteeName(); // 资产托管人名称
		if(StringUtil.isNEmpty(trusteeName) && trusteeName.length() > 42){
			return "资产托管人名称超长";
		}
		String trusteeCerType= productReqVo.getTrusteeCerType(); // 资产托管人证件类型
		if(StringUtil.isNEmpty(trusteeCerType) && trusteeCerType.length() > 2){
			return "资产托管人证件类型超长";
		}
		if(StringUtil.isNEmpty(trusteeCerType)&&MrsCredentialsType.getEnum(trusteeCerType) == null) {
			return "资产托管人证件类型不存在";
		}
		String trusteeCerCode= productReqVo.getTrusteeCerCode(); // 资产托管人证件代码
		if(StringUtil.isNEmpty(trusteeCerCode) && trusteeCerCode.length() > 32){
			return "资产托管人证件代码超长";
		}
		String contactsMoblie= productReqVo.getContactsMoblie(); // 联系人移动电话
		if(StringUtil.isNEmpty(contactsMoblie) && contactsMoblie.length() > 16){
			return "联系人移动电话超长";
		}
		String contactsTel= productReqVo.getContactsTel(); // 联系人固定电话
		if(StringUtil.isNEmpty(contactsTel) && contactsTel.length() > 16){
			return "联系人固定电话超长";
		}
		String contactsSpareTel= productReqVo.getContactsSpareTel(); // 联系人备用电话
		if(StringUtil.isNEmpty(contactsSpareTel) && contactsSpareTel.length() > 16){
			return "联系人备用电话超长";
		}
		String contactsFax= productReqVo.getContactsFax(); // 联系人传真
		if(StringUtil.isNEmpty(contactsFax) && contactsFax.length() > 16){
			return "联系人传真超长";
		}
		String contactsEmail= productReqVo.getContactsEmail(); // 联系人电子邮箱
		if(StringUtil.isNEmpty(contactsEmail) && contactsEmail.length() > 64){
			return "联系人电子邮箱超长";
		}
		String contactsAddr= productReqVo.getContactsAddr(); // 联系人联系地址
		if(StringUtil.isNEmpty(contactsAddr) && contactsAddr.length() > 42){
			return "联系人联系地址超长";
		}
		String contactsZip= productReqVo.getContactsZip(); // 联系人邮政编码
		if(StringUtil.isNEmpty(contactsZip) && contactsZip.length() > 6){
			return "联系人邮政编码超长";
		}
		if(StringUtil.isNEmpty(contactsZip) 
				&& !Pattern.compile(RegexEnum.REGEX_POST.getRegexValue()).matcher(contactsZip).matches()) {
			return "联系人邮政编码错误";
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
	
	/**
	 * 校验必需参数
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkIndividualParams(IndividualRequestVO individualReqVo){
		
		if(individualReqVo == null){
			return "请求对象为空";
		}
		// 校验一户通类型
		if(StringUtil.isEmpty(individualReqVo.getAccountType())) {
			return "账户类型为空";
		}
		if(!MrsAccountType.MAT_1.getValue().equals(individualReqVo.getAccountType())) {
			return "只能开保险一户通";
		}
		String isForce = individualReqVo.getIsForce();
		if(StringUtil.isNEmpty(isForce)) {
			if(!isForce.equals(Constants.FORCE_0) && !isForce.equals(Constants.FORCE_1)) {
				return "强制开户标识错误";
			}
		}

		if(StringUtil.isEmpty(individualReqVo.getPlatformCode())) {
			return "开户渠道为空";
		} else {
			// 渠道非空校验
			if(SHIEConfigConstant.GS_CORE.equals(individualReqVo.getPlatformCode())) {
				// ecif渠道
				if(StringUtil.isEmpty(individualReqVo.getCustomerCode())) {
					return "客户编号为空";
				}
				if (StringUtil.isNEmpty(individualReqVo.getCustomerCode())
						&& individualReqVo.getCustomerCode().length() > 32) {
					return "客户编号超长";
				}
				if(StringUtil.isNEmpty(individualReqVo.getPlatformCustCode())) {
					return "平台客户编号必须为空";
				}
			} else {
				// 非ecif渠道
				if(StringUtil.isNEmpty(individualReqVo.getCustomerCode())) {
					return "客户编号必须为空";
				}
				if(StringUtil.isEmpty(individualReqVo.getPlatformCustCode())) {
					return "平台客户编号为空";
				}
				if (StringUtil.isNEmpty(individualReqVo.getPlatformCustCode())
						&& individualReqVo.getPlatformCustCode().length() > 32) {
					return "平台客户编号超长";
				}
			}
		}
		if(StringUtil.isEmpty(individualReqVo.getCredentialsType())) {
			return "证件类型为空";
		}
		if(StringUtil.isNEmpty(individualReqVo.getCredentialsType())) {
			boolean checkFlag=false;
			for(MrsCredentialsType credentialsType: MrsCredentialsType.getPersonCredentialsTypeList()){
				if(credentialsType.getValue().equals(individualReqVo.getCredentialsType())){
					checkFlag=true;
				}
			}
			if(!checkFlag){
				return "证件类型不存在";
			}
		}
		String credNumber = individualReqVo.getCredentialsNumber();
		if(StringUtil.isEmpty(credNumber)) {
			return "证件号码为空";
		}
		if(StringUtil.isNEmpty(credNumber) && credNumber.length() > 32) {
			return "证件号码超长";
		}
		String customerName = individualReqVo.getCustomerName();
		if(StringUtil.isEmpty(customerName)) {
			return "客户名称为空";
		}
		if(StringUtil.isNEmpty(customerName) && customerName.length() > 85) {
			return "客户名称为空或者超长" ;
		}
		
		String sexCode = individualReqVo.getSexCode();
		if(StringUtil.isNEmpty(sexCode) && MrsSexCode.getEnum(sexCode) == null) {
			return "性别不存在";
		}
		
		String educationCode = individualReqVo.getEducationCode();
		if(StringUtil.isNEmpty(educationCode) && MrsEducationCode.getEnum(educationCode) == null) {
			return "学历不存在";
		}
		
		String certEndDate = individualReqVo.getCredentialsEnddate();
		if(StringUtil.isNEmpty(certEndDate) && certEndDate.length() > 8) {
			return "证件有效期超长";
		}
		String credFilepath = individualReqVo.getCredentialsFilepath();
		if(StringUtil.isNEmpty(credFilepath) && credFilepath.length() > 85) {
			return "证件留存地址超长";
		}
		String addr = individualReqVo.getContactAddr();
		if(StringUtil.isNEmpty(addr) && addr.length() > 85) {
			return "联系地址超长";
		}
		
		String birthdate = individualReqVo.getBirthdate();
		if(StringUtil.isNEmpty(birthdate)) {
			if(birthdate.length() != 8  && !Pattern.compile(RegexEnum.REGEX_DIGIT8.getRegexValue()).matcher(birthdate).matches()) {
				return "出生日期不合法";	
			}
		}
		
		String endDate = individualReqVo.getCredentialsEnddate();
		if(StringUtil.isNEmpty(endDate)) {
			if(endDate.length() != 8  && !Pattern.compile(RegexEnum.REGEX_DIGIT8.getRegexValue()).matcher(endDate).matches()) {
				return "证件有效期不合法";	
			}
		}
		
		String zipCode = individualReqVo.getZipCode();
		if(StringUtil.isNEmpty(zipCode) && !Pattern.compile(RegexEnum.REGEX_POST.getRegexValue()).matcher(zipCode).matches()) {
			return "邮政编码错误";
		}

		String mobile = individualReqVo.getMobile();
		if(StringUtil.isNEmpty(mobile) && mobile.length() > 16) {
			return "手机号码超长";
		}
		String tel = individualReqVo.getTel();
		if(StringUtil.isNEmpty(tel) && tel.length() > 16) {
			return "固定电话号码超长";
		}
		String spareTel = individualReqVo.getSpareTel();
		if(StringUtil.isNEmpty(spareTel) && spareTel.length() > 16) {
			return "备用电话超长";
		}
		String email = individualReqVo.getEmail();
		if(StringUtil.isNEmpty(email) && email.length() > 64) {
			return "邮箱超长";
		}
		
		String notionalityCode = individualReqVo.getNationalityCode();
		if(StringUtil.isNEmpty(notionalityCode)) {
			MrsDataParamDto dto = mrsDataParamService.findByCodeType(notionalityCode, MrsDataParamType.TYPE_01.getValue());
			if(dto == null){
				return "国籍或地区代码不正确";
			}
		}
		
		String nationalCode = individualReqVo.getNationalCode();
		if(StringUtil.isNEmpty(nationalCode)) {
			MrsDataParamDto dto = mrsDataParamService.findByCodeType(nationalCode, MrsDataParamType.TYPE_02.getValue());
			if(dto == null){
				return "民族代码不正确";
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String regx = "";
		String arg = "";
		System.out.println(Pattern.compile(regx).matcher(arg).matches());
	}
	
	/**
	 * 将个人客户被动开户请求Gson转换为对象
	 * @param params
	 * @param urlData
	 * @param bizContent
	 * @return
	 * @throws Exception 
	 */
	private IndividualRequestVO toIndividualsRequestVO(String params) throws Exception{
		IndividualRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params,IndividualRequestVO.class);
			if(requestVo == null){
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999,"JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误："+ e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999,"JSON转换失败");
		}
	}


	/**
	 * 将机构客户被动开户请求Gson转换为对象
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private UnitRequestVO toUnitRequestVO(String params) throws Exception{
		UnitRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params,UnitRequestVO.class);
			if(requestVo == null){
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999,"JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误："+ e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999,"JSON转换失败");
		}
	}
	

	/**
	 * 将机构客户被动开户请求Gson转换为对象
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private UnitAssetRequestVo toUnitAssetRequestVO(String params) throws Exception{
		UnitAssetRequestVo requestVo = null;
		try {
			requestVo = getObjectBean(params,UnitAssetRequestVo.class);
			if(requestVo == null){
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999,"JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误："+ e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999,"JSON转换失败");
		}
	}


	/**
	 * 将产品客户被动开户请求Gson转换为对象
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private ProductRequestVO toProductRequestVO(String params) throws Exception{
		ProductRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params,ProductRequestVO.class);
			if(requestVo == null){
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999,"JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误："+ e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999,"JSON转换失败");
		}
	}

	@Override
	public ProductResponseVO product(String params) {
		
		log.info("--------进入产品客户被动开户方法：receive:"+params);
    	ProductRequestVO productReqVo = new ProductRequestVO();
    	ProductResponseVO productRespVo = new ProductResponseVO();
    	String lockKey = "";
		log.debug("-----------第一步：转换ProductRequestVO对象");
		try {
			productReqVo = toProductRequestVO(params);
			// 解析参数封装成request对象
			log.debug("-----------第二步，校验入参参数");
			String checkResult = checkProductParams(productReqVo);
			if(checkResult != null){
				log.error("参数["+params+"]校验失败:"+checkResult);
				productRespVo.setMsgCode(PortalCode.CODE_9999);
				productRespVo.setMsgInfo(checkResult);
				return productRespVo;
			}
			// 根据接入平台编号查询平台信息
			MrsPlatformDto mrsPlatformDto = mrsPersonService.checkPlatform(productReqVo.getPlatformCode());
			String customerName = productReqVo.getProductName();
			String credentialsType = productReqVo.getCredentialsType();
			String credentialsNumber = productReqVo.getCredentialsNumber();
			lockKey = RedisLock.getOrganAssetLockKey(credentialsType, credentialsNumber);
	    	boolean flag = RedisCacheUtil.setNXWithWait(lockKey, System.currentTimeMillis()+"",TIME_OUT);
			if (flag) {
				// 根据三要素查询客户信息表
				List<MrsAccountDto> productList = mrsAccountService.findByProduct3Element(customerName, credentialsType,
						credentialsNumber);
				// 客户信息
				MrsAccountDto accountDto = null;
				if (productList == null || productList.size() == 0) {
					log.info("系统不存在客户,新开一户通账户!");
					accountDto = mrsProductService.initOpenAcnt(productReqVo, mrsPlatformDto);
					productRespVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
				} else {
					// 客户存在判断是否为强制开户
					if (Constants.FORCE_1.equals(productReqVo.getIsForce())) {
						log.info("系统存在客户...强制开户");
						accountDto = mrsProductService.forceOpenAcnt(productReqVo, mrsPlatformDto);
						productRespVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
					} else {
						log.info("系统存在客户...");
						accountDto = mrsProductService.updateOpenAcnt(productReqVo, productList, mrsPlatformDto);
						//productRespVo.setLoginPwd(accountDto.getNoEncryptloginPwd());
					}
				}
				// 回最新的一户通账号信息
				String accountCode = accountDto.getCustId();
				productRespVo.setAccountCode(accountCode);
				productRespVo.setMsgCode(PortalCode.CODE_0000);
				productRespVo.setMsgInfo("开户成功");
			} else {
	    		log.info("并发锁被动开户失败：证件号码[number={}]",productReqVo.getCredentialsNumber());
	    		productRespVo.setMsgCode(PortalCode.CODE_9999);
	    		productRespVo.setMsgInfo("开户失败");
	    		return productRespVo;
	    	}
		} catch (CodeCheckedException e) {
			log.error("产品客户被动开户失败："+e.getMessage());
			productRespVo.setMsgCode(e.getCode());
			productRespVo.setMsgInfo(e.getMessage());
			log.error(String.format("产品客户被动开户失败,产品名称：%s,证件类型：%s,证件号码：%s,产品客户被动开户失败:%s",
					productReqVo.getProductName(),
					productReqVo.getCredentialsType(),
					productReqVo.getCredentialsNumber()));
			saveErrorExcetpionLog(String.format("产品名称：%s,证件类型：%s,证件号码：%s,产品客户被动开户失败:%s",
					productReqVo.getProductName(),
					productReqVo.getCredentialsType(),
					productReqVo.getCredentialsNumber(),
	    			e.getMessage()));
		} catch (Exception e) {
			log.error("产品客户被动开户失败：", e);
			productRespVo.setMsgCode(PortalCode.CODE_9999);
			productRespVo.setMsgInfo("开户失败");
			log.error(String.format("产品客户被动开户失败,产品名称：%s,证件类型：%s,证件号码：%s,产品客户被动开户失败:%s",
					productReqVo.getProductName(),
					productReqVo.getCredentialsType(),
					productReqVo.getCredentialsNumber()));
			saveErrorExcetpionLog(String.format("产品名称：%s,证件类型：%s,证件号码：%s,产品客户被动开户失败:%s",
					productReqVo.getProductName(),
					productReqVo.getCredentialsType(),
					productReqVo.getCredentialsNumber(),
	    			e.getMessage()));
		}finally {
    		if(StringUtil.isNEmpty(lockKey)) {
    			try {
					RedisCacheUtil.evict(lockKey);
				} catch (Exception e) {
					log.error("锁删除异常:", e);
				}
    		}
    	}
		return productRespVo;
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
			log.error("被动客户开户,记录异常日志失败！");
		}
	}

}
