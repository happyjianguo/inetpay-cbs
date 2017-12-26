package com.ylink.inetpay.cbs.mrs.rest;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.cbs.mrs.service.MrsDataParamService;
import com.ylink.inetpay.cbs.mrs.service.MrsOrganService;
import com.ylink.inetpay.cbs.mrs.service.MrsPersonService;
import com.ylink.inetpay.cbs.mrs.service.MrsProductService;
import com.ylink.inetpay.cbs.mrs.service.MrsSubAccountService;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EProductTypeEnum;
import com.ylink.inetpay.common.core.constant.IsDelete;
import com.ylink.inetpay.common.core.constant.MrsCredentialsType;
import com.ylink.inetpay.common.core.constant.MrsDataParamType;
import com.ylink.inetpay.common.core.constant.MrsSubAccountStatus;
import com.ylink.inetpay.common.core.constant.MrsSubAccountType;
import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataParamDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.PortalCode;
import com.ylink.inetpay.common.project.portal.vo.AccountCloseMsgRespVo;
import com.ylink.inetpay.common.project.portal.vo.AccountCloseRequestVO;
import com.ylink.inetpay.common.project.portal.vo.AccountIndividualUpdateReqVO;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgRespVo;
import com.ylink.inetpay.common.project.portal.vo.AccountProductUpdateRequestVO;
import com.ylink.inetpay.common.project.portal.vo.AccountUnitUpdateReqVO;
import com.ylink.inetpay.common.project.portal.vo.AccountUpdateBusiInfoRequestVO;
import net.sf.json.JSONObject;
@Service("mrsAccountMsgUpdateService")
public class MrsAccountMsgUpdateServiceImpl implements MrsAccountMsgUpdateService {

	private static Logger log = LoggerFactory.getLogger(MrsAccountMsgUpdateServiceImpl.class);

	public static final long TIME_OUT = 60L;

	@Autowired
	private MrsPersonService mrsPersonService;
	@Autowired
	private MrsOrganService mrsOrganService;
	@Autowired
	private MrsSubAccountService mrsSubAccountService;
	@Autowired
	private MrsProductService mrsProductService;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private BisExceptionLogAppService bisExceptionLogAppService;
	@Autowired
	private MrsDataParamService mrsDataParamService;
	/**
	 * author dxd 个人账户变更
	 */
	@Override
	public AccountMsgRespVo individualUpdate(String params) {
		log.info("--------进入个人账户变更方法：receive:" + params);
		AccountIndividualUpdateReqVO reqVo = new AccountIndividualUpdateReqVO();
		AccountMsgRespVo respVo = new AccountMsgRespVo();
		try {
			log.debug("-----------第一步：转换AccountIndividualUpdateReqVO对象");
			reqVo = toAccountIndividualUpdateReqVO(params);

			// 解析参数封装成request对象
			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkAccountIndividualUpdateParams(reqVo);
			if (StringUtil.isNEmpty(checkResult)) {
				log.error(checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 根据接入平台编号查询平台信息
			MrsPlatformDto mrsPlatformDto = mrsPersonService.checkPlatform(reqVo.getPlatformCode());
			// 根据请求参数中一户通账号和一户通状态为非“注销”状态关联查询“个人客户信息表”和“一户通账户表”
			List<MrsAccountDto> actPersonList = mrsAccountService.findByUpdatePerson3Element(reqVo.getAccountCode());
			// 客户信息
			if (actPersonList == null || actPersonList.size() == 0) {
				log.error("一户通账户不存在!");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("一户通账户不存在!");
				return respVo;
			}
			// 如果存在记录，更新信息（除三要素信息）
			else {
				mrsPersonService.updatePersonInfo(reqVo, actPersonList.get(0), mrsPlatformDto);
			}
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("个人账户变更完成,返回json对象：{}",jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("客户姓名：%s,证件类型：%s,证件号码：%s,个人账户变更失败:%s", reqVo.getCustomerName(),
					reqVo.getCredentialsType(), reqVo.getCredentialsNumber(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("机构账户变更失败:", e);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("机构变更失败");
			saveErrorExcetpionLog(String.format("客户姓名：%s,证件类型：%s,证件号码：%s,个人账户变更失败:%s", reqVo.getCustomerName(),
					reqVo.getCredentialsType(), reqVo.getCredentialsNumber(), e.getMessage()));
			return respVo;
		}
	}

	/**
	 * author dxd 机构账户变更
	 */
	@Override
	public AccountMsgRespVo unitUpdate(String params) {
		log.info("--------进入机构账户变更方法：receive:" + params);
		AccountUnitUpdateReqVO reqVo = new AccountUnitUpdateReqVO();
		AccountMsgRespVo respVo = new AccountMsgRespVo();
		try {
			log.debug("-----------第一步：转换AccountUnitUpdateReqVO对象");
			reqVo = toAccountUnitUpdateReqVO(params);

			// 解析参数封装成request对象
			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkAccountUnitUpdateParams(reqVo);
			if (StringUtil.isNEmpty(checkResult)) {
				log.error(checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 根据接入平台编号查询平台信息
			MrsPlatformDto mrsPlatformDto = mrsPersonService.checkPlatform(reqVo.getPlatformCode());
			// 根据请求参数中一户通账号和一户通状态为非“注销”状态关联查询“机构客户信息表”和“一户通账户表”
			List<MrsAccountDto> actList = mrsAccountService.findByUpdateOrgan3Element(reqVo.getAccountCode());
			// 客户信息
			if (actList == null || actList.size() == 0) {
				log.error("一户通账户不存在!");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("一户通账户不存在!");
				return respVo;
			}
			// 如果存在记录，更新信息（除三要素信息）
			else {
				mrsOrganService.updateOrganInfo(reqVo, actList.get(0), mrsPlatformDto);
			}
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("机构账户变更完成,返回json对象：{}",jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("机构账户变更失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,机构账户变更失败:%s", reqVo.getAccountCode(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("机构账户变更失败:", e);
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("机构变更失败");
			saveErrorExcetpionLog(String.format("一户通号：%s,机构账户变更失败:%s", reqVo.getAccountCode(), e.getMessage()));
			return respVo;
		}
	}

	/**
	 * 校验个人账户变更参数
	 */
	public String checkAccountIndividualUpdateParams(AccountIndividualUpdateReqVO reqVo) {
		if (reqVo == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVo.getAccountCode()) || reqVo.getAccountCode().length() > 32) {
			return "一户通账号为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVo.getCustomerName()) || reqVo.getCustomerName().length() > 85) {
			return "客户姓名为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVo.getCredentialsType()) || reqVo.getCredentialsType().length() > 2) {
			return "证件类型为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVo.getCredentialsNumber()) || reqVo.getCredentialsNumber().length() > 32) {
			return "证件号码为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVo.getPlatformCode()) || reqVo.getPlatformCode().length() > 16) {
			return "接入平台编号为空或字段超长";
		} else {
			if (SHIEConfigConstant.GS_CORE.equals(reqVo.getPlatformCode())) {
				// ecif渠道，客户编号必填，平台客户编号必不填
				if (StringUtil.isEmpty(reqVo.getCustomerCode()) || reqVo.getCustomerCode().length() > 32) {
					return "客户编号为空或者字段超长";
				}
				if (StringUtil.isNEmpty(reqVo.getPlatformCustCode())) {
					return "平台客户编号必须为空";
				}
			} else {
				// 非ecif渠道，平台客户编号必填，客户编号必不填
				if (StringUtil.isNEmpty(reqVo.getCustomerCode())) {
					return "客户编号必须为空";
				}
				if (StringUtil.isEmpty(reqVo.getPlatformCustCode()) || reqVo.getPlatformCustCode().length() > 32) {
					return "平台客户编号为空或字段超长";
				}
			}
		}

		if ((StringUtil.isNEmpty(reqVo.getNationalityCode())&& reqVo.getNationalityCode().length() > 3) 
				) {
			return "国籍字段超长";
		}
		
		String notionalityCode = reqVo.getNationalityCode();
		if(StringUtil.isNEmpty(notionalityCode)) {
			MrsDataParamDto dto = mrsDataParamService.findByCodeType(notionalityCode, MrsDataParamType.TYPE_01.getValue());
			if(dto == null){
				return "国籍或地区代码不正确";
			}
		}
	
		if (StringUtil.isNEmpty(reqVo.getNationalCode()) && reqVo.getNationalCode().length() > 2) {
			return "民族字段超长";
		}
		String nationalCode = reqVo.getNationalCode();
		if(StringUtil.isNEmpty(nationalCode)) {
			MrsDataParamDto dto = mrsDataParamService.findByCodeType(nationalCode, MrsDataParamType.TYPE_02.getValue());
			if(dto == null){
				return "民族代码不正确";
			}
		}
		
		if (StringUtil.isNEmpty(reqVo.getEducationCode()) && reqVo.getEducationCode().length() > 2) {
			return "学历字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getCredentialsEnddate()) && reqVo.getCredentialsEnddate().length() > 8) {
			return "证件有效期字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getCredentialsFilepath()) && reqVo.getCredentialsFilepath().length() > 85) {
			return "证件留存地址字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getBirthdate()) && reqVo.getBirthdate().length() > 8) {
			return "出生日期字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getSexCode()) && reqVo.getSexCode().length() > 1) {
			return "性别字段超长";
		}
		
		
		if (StringUtil.isNEmpty(reqVo.getMobile()) && reqVo.getMobile().length() > 16) {
			return "移动电话字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getTel()) && reqVo.getTel().length() > 16) {
			return "固定电话字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getSpareTel()) && reqVo.getSpareTel().length() > 16) {
			return "备用电话字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getContactAddr()) && reqVo.getContactAddr().length() > 85) {
			return "联系地址字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getZipCode()) && reqVo.getZipCode().length() > 8) {
			return "邮政编码字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getEmail()) && reqVo.getEmail().length() > 64) {
			return "邮箱字段超长";
		}

		return null;
	}

	/**
	 * 校验机构账户变更参数
	 */
	public String checkAccountUnitUpdateParams(AccountUnitUpdateReqVO reqVo) {
		if (reqVo == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVo.getAccountCode()) || reqVo.getAccountCode().length() > 32) {
			return "一户通账号为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVo.getCustomerName()) || reqVo.getCustomerName().length() > 85) {
			return "机构名称为空或字段超长";
		}
		/*
		 * if(StringUtil.isEmpty(reqVo.getCredentialsType()) ||
		 * reqVo.getCredentialsType().length() > 2){ return "证件类型为空"; }
		 * if(StringUtil.isEmpty(reqVo.getCredentialsNumber()) ||
		 * reqVo.getCredentialsNumber().length() > 32){ return "证件号码为空"; }
		 */
		if (StringUtil.isEmpty(reqVo.getPlatformCode()) || reqVo.getPlatformCode().length() > 16) {
			return "接入平台编号为空或字段超长";
		} else {
			if (SHIEConfigConstant.GS_CORE.equals(reqVo.getPlatformCode())) {
				// ecif渠道(客户：客户编号必填，平台客户编号必不填，社会统一信用代码、组织机构代码、税务登记号码、营业执照编码这四个字段不能同时为空)
				
				if (StringUtil.isEmpty(reqVo.getCustomerCode()) || reqVo.getCustomerCode().length() > 32) {
					return "客户编号为空或者字段超长";
				}
				if (StringUtil.isNEmpty(reqVo.getPlatformCustCode())) {
					return "平台客户编号必须为空";
				}
				if (StringUtil.isEmpty(reqVo.getSocialCreditCode()) && StringUtil.isEmpty(reqVo.getOrganizeCode())
						&& StringUtil.isEmpty(reqVo.getRevenueCode())
						&& StringUtil.isEmpty(reqVo.getBusinessLicence())) {
					return "社会统一信用代码、组织机构代码、税务登记号码、营业执照编码这四个字段不能同时为空！";
				}
			} else {
				// 非ecif渠道(资管：平台客户编号，证件类型，证件号码必填)
				if (StringUtil.isEmpty(reqVo.getCredentialsType()) || reqVo.getCredentialsType().length() > 2) {
					return "证件类型为空或字段超长";
				}
				if(StringUtil.isNEmpty(reqVo.getCredentialsType())) {
					boolean checkFlag=false;
					for(MrsCredentialsType credentialsType: MrsCredentialsType.getMrsOganTypeList()){
						if(credentialsType.getValue().equals(reqVo.getCredentialsType())){
							checkFlag=true;
						}
					}
					if(!checkFlag){
						return "证件类型不存在";
					}
				}
				if (StringUtil.isEmpty(reqVo.getCredentialsNumber()) || reqVo.getCredentialsNumber().length() > 32) {
					return "证件号码为空或字段超长";
				}
				if (StringUtil.isNEmpty(reqVo.getCustomerCode())) {
					return "客户编号必须为空";
				}
				if (StringUtil.isEmpty(reqVo.getPlatformCustCode()) || reqVo.getPlatformCustCode().length() > 32) {
					return "平台客户编号为空或字段超长";
				}
			}
		}

		if (StringUtil.isNEmpty(reqVo.getSocialCreditCode()) &&  reqVo.getSocialCreditCode().length() > 128) {
			return "社会统一信用代码字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getOrganizeCode()) &&  reqVo.getOrganizeCode().length() > 32) {
			return "组织机构代码字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getRevenueCode()) &&  reqVo.getRevenueCode().length() > 32) {
			return "税务登记号码字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getBusinessLicence()) &&  reqVo.getBusinessLicence().length() > 64) {
			return "营业执照编码字段超长";
		}

		if (StringUtil.isNEmpty(reqVo.getCustomerShortName()) &&  reqVo.getCustomerShortName().length() > 21) {
			return "机构简称字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getCustomerEname()) &&  reqVo.getCustomerEname().length() > 128) {
			return "机构英文名称字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getNationalityCode()) &&  reqVo.getNationalityCode().length() > 3) {
			return "国籍字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getBusinessLicenceEndDate()) &&  reqVo.getBusinessLicenceEndDate().length() > 8) {
			return "营业执照有效期字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getBusinessSortCode()) &&  reqVo.getBusinessSortCode().length() > 2) {
			return "机构类型字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getBusinessSortSubCode()) &&  reqVo.getBusinessSortSubCode().length() > 4) {
			return "机构子类型字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getRegisteredAddr()) &&  reqVo.getRegisteredAddr().length() > 85) {
			return "注册地址字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getAuthPersonName()) &&  reqVo.getAuthPersonName().length() > 85) {
			return "法人姓名字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getAuthPersonIdentifyTypeCode()) &&  reqVo.getAuthPersonIdentifyTypeCode().length() > 2) {
			return "法人证件类型字段超长";
		}
		if (StringUtil.isNEmpty(reqVo.getAuthPersonIdentifyNo()) &&  reqVo.getAuthPersonIdentifyNo().length() > 32) {
			return "法人证件号码字段超长";
		}
		if (reqVo.getContactsName().length() > 85) {
			return "联系人姓名字段超长";
		}
		if (reqVo.getContactsMoblie().length() > 16) {
			return "联系人移动电话字段超长";
		}
		if (reqVo.getContactsTel().length() > 16) {
			return "联系人固定电话字段超长";
		}
		if (reqVo.getContactsSpareTel().length() > 16) {
			return "联系人备用电话字段超长";
		}
		if (reqVo.getContactsFax().length() > 16) {
			return "联系人传真字段超长";
		}
		if (reqVo.getContactsEmail().length() > 64) {
			return "联系人电子邮箱字段超长";
		}
		if (reqVo.getContactsAddr().length() > 128) {
			return "联系人联系地址字段超长";
		}
		if (reqVo.getContactsZip().length() > 6) {
			return "联系人邮政编码字段超长";
		}

		return null;
	}

	/**
	 * 个人账户变更json转对象
	 */
	private AccountIndividualUpdateReqVO toAccountIndividualUpdateReqVO(String params) throws Exception {
		AccountIndividualUpdateReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, AccountIndividualUpdateReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、json对象转换失败");
				throw new Exception("转换对象为空、json对象转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("json转换错误：", e);
			throw new Exception("json转换错误：" + e.toString());
		}
	}

	/**
	 * 机构账户变更json转对象
	 */
	private AccountUnitUpdateReqVO toAccountUnitUpdateReqVO(String params) throws Exception {
		AccountUnitUpdateReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, AccountUnitUpdateReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、json对象转换失败");
				throw new Exception("转换对象为空、json对象转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("json转换错误：", e);
			throw new Exception("json转换错误：" + e.toString());
		}
	}

	@Override
	public AccountMsgRespVo updateBusi(String params) throws Exception {
		log.info("--------进入业务权限变更方法：receive:" + params);
		AccountUpdateBusiInfoRequestVO reqVo = new AccountUpdateBusiInfoRequestVO();
		AccountMsgRespVo respVo = new AccountMsgRespVo();
		try {
			log.debug("-----------第一步：转换AccountUpdateBusiInfoRequestVO对象");
			reqVo = toAccountUpdateBusiInfoRequestVO(params);

			// 解析参数封装成request对象
			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkLongMsgSearchParams(reqVo);
			if (StringUtil.isNEmpty(checkResult)) {
				log.error(checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 根据接入平台编号查询平台信息
			mrsPersonService.checkPlatform(reqVo.getPlatformCode());
			MrsSubAccountDto SubAccountDto = new MrsSubAccountDto();
			SubAccountDto.setCustId(reqVo.getAccountCode());
			SubAccountDto.setSubAccountType(reqVo.getSubAccountType());
			// 根据一户通查询子账户信息
			List<MrsSubAccountDto> mrsSubAccountDtoList = mrsSubAccountService
					.findByCustIdAndSubAccountType(SubAccountDto);
			if (mrsSubAccountDtoList == null || mrsSubAccountDtoList.size() == 0) {
				// 没有子账户信息
				log.info("系统不存在子账户信息!");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("失败!子账户信息不存在");
				return respVo;
			}
			// 0-解冻 1-冻结 2-注销
			// a、 操作类型为“解冻”，更改子账户记录状态为“有效”；
			if (MrsSubAccountStatus.MSAS_0.getValue().equals(reqVo.getSubOperType())) {
				mrsSubAccountService.updateByCustIdAndSubType(reqVo.getAccountCode(),reqVo.getSubAccountType(), MrsSubAccountStatus.MSAS_0.getValue(),
						new Date());
			}
			// b、 操作类型为“冻结”，更改子账户记录状态为“冻结”；
			else if (MrsSubAccountStatus.MSAS_1.getValue().equals(reqVo.getSubOperType())) {
				mrsSubAccountService.updateByCustIdAndSubType(reqVo.getAccountCode(),reqVo.getSubAccountType(),  MrsSubAccountStatus.MSAS_1.getValue(),
						new Date());
			}
			// c、 操作类型为“注销”，更改子账户记录状态为“注销”；
			else if (MrsSubAccountStatus.MSAS_2.getValue().equals(reqVo.getSubOperType())) {
				mrsSubAccountService.updateByCustIdAndSubType(reqVo.getAccountCode(),reqVo.getSubAccountType(),  MrsSubAccountStatus.MSAS_2.getValue(),
						new Date());
			} else {
				log.error("操作类型不明，不进行处理;参数是：", reqVo.getSubOperType());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("操作类型不明，不进行处理");
				return respVo;
			}
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("业务权限变更完成,返回json对象：{}",jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("业务权限变更失败：" + e.getMessage());
			respVo.setMsgCode(e.getCode());
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,业务权限变更失败:%s", reqVo.getAccountCode(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("业务权限变更失败：一户通号码[custId={}]", reqVo.getAccountCode());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("权限变更失败!获取锁失败");
			saveErrorExcetpionLog(String.format("一户通号：%s,业务权限变更失败:%s", reqVo.getAccountCode(), e.getMessage()));
			return respVo;
		}
	}

	private String checkLongMsgSearchParams(AccountUpdateBusiInfoRequestVO reqVo) {
		if (reqVo == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVo.getAccountCode()) || reqVo.getAccountCode().length() > 32) {
			return "一户通账号为空或者超长";
		}
		if (StringUtil.isEmpty(reqVo.getSubAccountType()) || reqVo.getSubAccountType().length() > 2) {
			return "子账户类型为空或者超长";
		}
		if (StringUtil.isNEmpty(reqVo.getSubAccountType())
				&& MrsSubAccountType.getEnum(reqVo.getSubAccountType()) == null) {
			return "子账户类型不存在";
		}
		if (StringUtil.isEmpty(reqVo.getSubOperType()) || reqVo.getSubOperType().length() > 1) {
			return "操作类型为空或者超长";
		}
		if (StringUtil.isNEmpty(reqVo.getSubOperType())
				&& MrsSubAccountStatus.getEnum(reqVo.getSubOperType()) == null) {
			return "操作类型不存在";
		}
		return null;
	}

	private AccountUpdateBusiInfoRequestVO toAccountUpdateBusiInfoRequestVO(String params) throws Exception {
		AccountUpdateBusiInfoRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, AccountUpdateBusiInfoRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("转换对象为空、Gson对象转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：", e);
			throw new Exception("Gson转换错误：" + e.toString());
		}
	}

	/**
	 * 从json获取Bean 对象
	 * 
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

	@Override
	public AccountMsgRespVo productUpdate(String params) throws Exception {
		log.info("--------进入产品账户变更方法：receive:" + params);
		AccountProductUpdateRequestVO reqVo = new AccountProductUpdateRequestVO();
		AccountMsgRespVo respVo = new AccountMsgRespVo();
		try {
			log.debug("-----------第一步：转换AccountProductUpdateRequestVO对象");
			reqVo = toAccountProductUpdateRequestVO(params);

			// 解析参数封装成request对象
			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkLongMsgSearchParams(reqVo);
			if (StringUtil.isNEmpty(checkResult)) {
				log.error(checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 根据接入平台编号查询平台信息
			MrsPlatformDto mrsPlatformDto = mrsPersonService.checkPlatform(reqVo.getPlatformCode());
			// 根据请求参数中一户通账号和一户通状态为非“注销”状态关联查询“产品客户信息表”和“一户通账户表”
			List<MrsAccountDto> actList = mrsAccountService.findByUpdateProduct3Element(reqVo.getAccountCode());
			// 客户信息
			if (actList == null || actList.size() == 0) {
				log.error("一户通账户不存在!");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("一户通账户不存在!");
				return respVo;
			}
			// 如果存在记录，更新信息（除三要素信息）
			else {
				mrsProductService.updateProductInfo(reqVo, actList.get(0), mrsPlatformDto);
			}
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("产品账户变更完成,返回json对象：{}",jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVo.setMsgCode(e.getCode());
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("客户姓名：%s,证件类型：%s,证件号码：%s,产品账户变更失败:%s", reqVo.getProductName(),
					reqVo.getCredentialsType(), reqVo.getCredentialsNumber(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.info("并发锁产品变更失败：一户通号码[custId={}]", reqVo.getAccountCode());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("产品变更失败!获取锁失败");
			saveErrorExcetpionLog(String.format("客户姓名：%s,证件类型：%s,证件号码：%s,产品账户变更失败:%s", reqVo.getProductName(),
					reqVo.getCredentialsType(), reqVo.getCredentialsNumber(), e.getMessage()));
			return respVo;
		}
	}

	private String checkLongMsgSearchParams(AccountProductUpdateRequestVO reqVo) {
		if (reqVo == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVo.getAccountCode()) || reqVo.getAccountCode().length() > 32) {
			return "一户通账号为空或者超长";
		}
		// platformCode 接入平台编号
		// 非ECIF系统用, asset_shie-资管系统时必填
		if (StringUtil.isEmpty(reqVo.getPlatformCode()) || reqVo.getPlatformCode().length() > 16) {
			return "接入平台编号为空或者超长";
		} else {
			// 非ECIF系统用, gscore-客户系统时必填
			if (SHIEConfigConstant.GS_CORE.equals(reqVo.getPlatformCode())) {
				// ecif渠道
				// 客户编号 customerCode
				if (StringUtil.isEmpty(reqVo.getCustomerCode()) || reqVo.getCustomerCode().length() > 32) {
					return "客户编号为空或者字段超长";
				}
				// 平台客户编号 platformCustCode
				if (StringUtil.isNEmpty(reqVo.getPlatformCustCode())) {
					return "平台客户编号必须为空";
				}
			} else {
				// 非ecif渠道
				if (StringUtil.isNEmpty(reqVo.getCustomerCode())) {
					return "客户编号必须为空";
				}
				if (StringUtil.isEmpty(reqVo.getPlatformCustCode()) || reqVo.getPlatformCustCode().length() > 32) {
					return "平台客户编号为空或字段超长";
				}
			}
		}
		// 产品名称 productName
		if (StringUtil.isEmpty(reqVo.getProductName()) || reqVo.getProductName().length() > 42) {
			return "产品名称为空或者超长";
		}
		// 证件类型 credentialsType
		if (StringUtil.isEmpty(reqVo.getCredentialsType()) || reqVo.getCredentialsType().length() > 2) {
			return "证件类型为空或者超长";
		}
		if (StringUtil.isNEmpty(reqVo.getCredentialsType())
				&& MrsCredentialsType.getEnum(reqVo.getCredentialsType()) == null) {
			return "证件类型不存在";
		}
		// 证件号码 credentialsNumber
		if (StringUtil.isEmpty(reqVo.getCredentialsNumber()) || reqVo.getCredentialsNumber().length() > 32) {
			return "证件号码为空或者超长";
		}
		// 产品类型代码 productTypeCode EProductTypeEnum
		if (StringUtil.isEmpty(reqVo.getProductTypeCode()) || reqVo.getProductTypeCode().length() > 2) {
			return "产品类型代码为空或者超长";
		}
		if (StringUtil.isNEmpty(reqVo.getProductTypeCode())
				&& EProductTypeEnum.getEnum(reqVo.getProductTypeCode()) == null) {
			return "产品类型代码不存在";
		}
		// 产品简称 productShortName
		if (StringUtil.isNEmpty(reqVo.getProductShortName()) && reqVo.getProductShortName().length() > 21) {
			return "产品简称超长";
		}
		// 产品到期日期 productEndDate
		if (StringUtil.isNEmpty(reqVo.getProductEndDate()) && reqVo.getProductEndDate().length() > 8) {
			return "产品到期日期超长";
		}
		// 资产管理人名称 managerName
		if (StringUtil.isNEmpty(reqVo.getManagerName()) && reqVo.getManagerName().length() > 42) {
			return "资产管理人名称超长";
		}
		// 资产管理人证件类型 managerCerType
		if (StringUtil.isNEmpty(reqVo.getManagerCerType()) && reqVo.getManagerCerType().length() > 2) {
			return "资产管理人证件类型超长";
		}
		// 资产管理人证件代码 managerCerCode
		if (StringUtil.isNEmpty(reqVo.getManagerCerCode()) && reqVo.getManagerCerCode().length() > 32) {
			return "资产管理人证件代码超长";
		}
		// 资产托管人名称 trusteeName
		if (StringUtil.isNEmpty(reqVo.getTrusteeName()) && reqVo.getTrusteeName().length() > 42) {
			return "资产托管人名称超长";
		}
		// 资产托管人证件类型 trusteeCerType
		if (StringUtil.isNEmpty(reqVo.getTrusteeCerType()) && reqVo.getTrusteeCerType().length() > 16) {
			return "资产托管人证件类型超长";
		}
		// 资产托管人证件代码 trusteeCerCode
		if (StringUtil.isNEmpty(reqVo.getTrusteeCerCode()) && reqVo.getTrusteeCerCode().length() > 32) {
			return "资产托管人证件代码超长";
		}
		// 联系人移动电话 contactsMoblie
		if (StringUtil.isNEmpty(reqVo.getContactsMoblie()) && reqVo.getContactsMoblie().length() > 16) {
			return "联系人移动电话超长";
		}
		// 联系人固定电话 contactsTel
		if (StringUtil.isNEmpty(reqVo.getContactsTel()) && reqVo.getContactsTel().length() > 16) {
			return "联系人固定电话超长";
		}
		// 联系人备用电话 contactsSpareTel
		if (StringUtil.isNEmpty(reqVo.getContactsSpareTel()) && reqVo.getContactsSpareTel().length() > 16) {
			return "联系人备用电话超长";
		}
		// 联系人传真 contactsFax
		if (StringUtil.isNEmpty(reqVo.getContactsFax()) && reqVo.getContactsFax().length() > 16) {
			return "联系人传真超长";
		}
		// 联系人电子邮箱 contactsEmail
		if (StringUtil.isNEmpty(reqVo.getContactsEmail()) && reqVo.getContactsEmail().length() > 64) {
			return "联系人电子邮箱超长";
		}
		// 联系人联系地址 contactsAddr
		if (StringUtil.isNEmpty(reqVo.getContactsAddr()) && reqVo.getContactsAddr().length() > 42) {
			return "联系人地址超长";
		}
		// 联系人邮政编码 contactsZip
		if (StringUtil.isNEmpty(reqVo.getContactsZip()) && reqVo.getContactsZip().length() > 6) {
			return "联系人邮政编码超长";
		}

		return null;
	}

	private AccountProductUpdateRequestVO toAccountProductUpdateRequestVO(String params) throws Exception {
		AccountProductUpdateRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, AccountProductUpdateRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("转换对象为空、Gson对象转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：", e);
			throw new Exception("Gson转换错误：" + e.toString());
		}
	}

	@Override
	public AccountCloseMsgRespVo closeAccount(String params) throws Exception {
		log.info("--------进入账户注销校验接口方法：receive:" + params);
		AccountCloseRequestVO reqVo = new AccountCloseRequestVO();
		AccountCloseMsgRespVo respVo = new AccountCloseMsgRespVo();
		try {
			log.debug("-----------第一步：转换AccountCloseRequestVO对象");
			reqVo = toAccountCloseRequestVO(params);

			// 解析参数封装成request对象
			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkLongMsgSearchParams(reqVo);
			if (StringUtil.isNEmpty(checkResult)) {
				log.error(checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				respVo.setCancle(IsDelete.IS_DELETE_N.getValue());
				return respVo;
			}
			// 根据接入平台编号查询平台信息
			mrsPersonService.checkPlatform(reqVo.getPlatformCode());
			MrsSubAccountDto SubAccountDto = new MrsSubAccountDto();
			SubAccountDto.setCustId(reqVo.getAccountCode());
			SubAccountDto.setSubAccountType(reqVo.getSubAccountType());
			// 根据一户通查询子账户信息
			List<MrsSubAccountDto> mrsSubAccountDtoList = mrsSubAccountService.findByCustIdAndSubAccountType(SubAccountDto);
			if (mrsSubAccountDtoList == null || mrsSubAccountDtoList.size() == 0) {
				// 没有子账户信息
				log.info("系统不存在子账户信息!");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("失败!子账户信息不存在");
				respVo.setCancle(IsDelete.IS_DELETE_N.getValue());
				JSONObject jsons = JSONObject.fromObject(respVo);
				log.info("账户注销校验完成,返回json对象：{}",jsons.toString());
				return respVo;
			}
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			respVo.setCancle(IsDelete.IS_DELETE_Y.getValue());
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("账户注销校验完成,返回json对象：{}",jsons.toString());
			return respVo;
		} catch (Exception e) {
			log.info("账户注销校验失败：一户通号码[custId={}]", reqVo.getAccountCode());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("账户注销校验失败");
			respVo.setCancle(IsDelete.IS_DELETE_N.getValue());
			saveErrorExcetpionLog(String.format("一户通号：%s,账户注销校验失败:%s", reqVo.getAccountCode(), e.getMessage()));
			return respVo;
		}
	}

	private String checkLongMsgSearchParams(AccountCloseRequestVO reqVo) {
		if (reqVo == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVo.getPlatformCode()) && reqVo.getAccountCode().length() > 16) {
			return "开户渠道为空或者超长";
		}
		if (StringUtil.isEmpty(reqVo.getAccountCode()) && reqVo.getAccountCode().length() > 32) {
			return "一户通账号为空或者超长";
		}
		if (StringUtil.isEmpty(reqVo.getSubAccountType()) && reqVo.getSubAccountType().length() > 2) {
			return "子账户类型为空或者超长";
		}
		if (StringUtil.isNEmpty(reqVo.getSubAccountType())
				&& MrsSubAccountType.getEnum(reqVo.getSubAccountType()) == null) {
			return "子账户类型不存在";
		}
		return null;
	}

	private AccountCloseRequestVO toAccountCloseRequestVO(String params) throws Exception {
		AccountCloseRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, AccountCloseRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败");
				throw new Exception("转换对象为空、Gson对象转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：", e);
			throw new Exception("Gson转换错误：" + e.toString());
		}
	}

	/**
	 * 记录异常日志
	 * 
	 * @param errorMsg
	 */
	private void saveErrorExcetpionLog(String errorMsg) {
		BisExceptionLogDto dto = new BisExceptionLogDto();
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
