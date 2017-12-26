package com.ylink.inetpay.cbs.portal.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import org.ylinkpay.framework.core.util.MD5Utils;

import com.google.gson.Gson;
import com.redrock.ips.support.cache.redis.RedisCacheUtil;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.act.service.ActAccountService;
import com.ylink.inetpay.cbs.act.service.ActBillService;
import com.ylink.inetpay.cbs.bis.service.BisEmailService;
import com.ylink.inetpay.cbs.common.CbsConfig;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.cbs.mrs.service.MrsBankBusiDtoService;
import com.ylink.inetpay.cbs.mrs.service.MrsContactListService;
import com.ylink.inetpay.cbs.mrs.service.MrsLoginUserService;
import com.ylink.inetpay.cbs.mrs.service.MrsPersonService;
import com.ylink.inetpay.cbs.mrs.service.MrsSubAccountService;
import com.ylink.inetpay.cbs.mrs.service.MrsUserPayPasswordService;
import com.ylink.inetpay.cbs.pay.service.PayPaymentService;
import com.ylink.inetpay.cbs.pay.service.PayRechargeService;
import com.ylink.inetpay.cbs.pay.service.PayWithdrawService;
import com.ylink.inetpay.cbs.util.SerialNumber;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.core.constant.EAccountDrCr;
import com.ylink.inetpay.common.core.constant.EAccountInOut;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.EBindCardTypeCode;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.core.constant.EBisExceptionLogType;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.EBusiType;
import com.ylink.inetpay.common.core.constant.EChlChannelCode;
import com.ylink.inetpay.common.core.constant.EChlReturnCode;
import com.ylink.inetpay.common.core.constant.EIsCrossBank;
import com.ylink.inetpay.common.core.constant.EMrsUserType;
import com.ylink.inetpay.common.core.constant.EOrderStatus;
import com.ylink.inetpay.common.core.constant.EResultCode;
import com.ylink.inetpay.common.core.constant.EStartSystemEnum;
import com.ylink.inetpay.common.core.constant.EStatusEnum;
import com.ylink.inetpay.common.core.constant.EmessType;
import com.ylink.inetpay.common.core.constant.FileTypeEnum;
import com.ylink.inetpay.common.core.constant.LoginUserStatus;
import com.ylink.inetpay.common.core.constant.MrsCredentialsType;
import com.ylink.inetpay.common.core.constant.MrsCustType;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.core.constant.MrsEducationCode;
import com.ylink.inetpay.common.core.constant.MrsNationalCode;
import com.ylink.inetpay.common.core.constant.MrsNationaltyCode;
import com.ylink.inetpay.common.core.constant.MrsPlatformCode;
import com.ylink.inetpay.common.core.constant.MrsReturnStatus;
import com.ylink.inetpay.common.core.constant.MrsSexCode;
import com.ylink.inetpay.common.core.constant.RegexEnum;
import com.ylink.inetpay.common.core.constant.SubAcctType;
import com.ylink.inetpay.common.core.dto.ResultCodeDto;
import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.core.util.AmountUtil;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.core.util.EncryptionDecryptionUtil;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.core.util.FileServerUtil;
import com.ylink.inetpay.common.core.util.Md5Util;
import com.ylink.inetpay.common.core.util.RandomUtil;
import com.ylink.inetpay.common.project.access.util.MapUtil;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;
import com.ylink.inetpay.common.project.account.dto.ActBusiRefSubDto;
import com.ylink.inetpay.common.project.cbs.app.BisCheckEmailAppService;
import com.ylink.inetpay.common.project.cbs.app.BisExceptionLogAppService;
import com.ylink.inetpay.common.project.cbs.app.BisSmsAppService;
import com.ylink.inetpay.common.project.cbs.app.BisSysParamAppService;
import com.ylink.inetpay.common.project.cbs.app.MrsAccountAppService;
import com.ylink.inetpay.common.project.cbs.app.MrsLoginUserAppService;
import com.ylink.inetpay.common.project.cbs.app.MrsWithdrawAuditAppService;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankAduitType;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankBindType;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankCardType;
import com.ylink.inetpay.common.project.cbs.constant.mrs.BankPayType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitAttachmentDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsBankBusiDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsContactListDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.PortalCode;
import com.ylink.inetpay.common.project.channel.app.SmsManageAppService;
import com.ylink.inetpay.common.project.channel.dto.request.SpdbDynNmReqPojo;
import com.ylink.inetpay.common.project.channel.dto.response.BaseRespPojo;
import com.ylink.inetpay.common.project.channel.dto.response.DynNumRespPojo;
import com.ylink.inetpay.common.project.pay.dto.PayPaymentDto;
import com.ylink.inetpay.common.project.pay.dto.PayRechargeDto;
import com.ylink.inetpay.common.project.pay.dto.PayWithdrawDto;
import com.ylink.inetpay.common.project.portal.dto.AccountMsg;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.restVO.AccountBaseReqVO;
import com.ylink.inetpay.common.project.portal.restVO.AccountBaseRespVO;
import com.ylink.inetpay.common.project.portal.restVO.CheckPayPwdReqVO;
import com.ylink.inetpay.common.project.portal.restVO.CheckPayPwdRespVO;
import com.ylink.inetpay.common.project.portal.restVO.EditLoginPwdReqVO;
import com.ylink.inetpay.common.project.portal.restVO.EditLoginPwdRespVO;
import com.ylink.inetpay.common.project.portal.restVO.InitPayPwdReqVO;
import com.ylink.inetpay.common.project.portal.restVO.InitPayPwdRespVO;
import com.ylink.inetpay.common.project.portal.restVO.UpdatePayPwdReqVO;
import com.ylink.inetpay.common.project.portal.restVO.UpdatePayPwdRespVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankAddReqVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankAddRespVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankBusiRespEleVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankBusiRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankBusiRespVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankBusiRestReqVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankSetDefaultReqVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.BankSetDefaultRespVO;
import com.ylink.inetpay.common.project.portal.restVO.BankRestVO.GetCodeRespVO;
import com.ylink.inetpay.common.project.portal.restVO.actBillRestVO.ActBillReqVO;
import com.ylink.inetpay.common.project.portal.restVO.actBillRestVO.ActBillRespEleVO;
import com.ylink.inetpay.common.project.portal.restVO.actBillRestVO.ActBillRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.actBillRestVO.ActBillRespVO;
import com.ylink.inetpay.common.project.portal.restVO.paymentRestVO.PaymentReqVO;
import com.ylink.inetpay.common.project.portal.restVO.paymentRestVO.PaymentRespEleVO;
import com.ylink.inetpay.common.project.portal.restVO.paymentRestVO.PaymentRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.paymentRestVO.PaymentRespVO;
import com.ylink.inetpay.common.project.portal.restVO.rechargeRestVO.RechargeReqVO;
import com.ylink.inetpay.common.project.portal.restVO.rechargeRestVO.RechargeRespEleVO;
import com.ylink.inetpay.common.project.portal.restVO.rechargeRestVO.RechargeRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.rechargeRestVO.RechargeRespVO;
import com.ylink.inetpay.common.project.portal.restVO.withdrawRestVO.WithdrawReqVO;
import com.ylink.inetpay.common.project.portal.restVO.withdrawRestVO.WithdrawRespEleVO;
import com.ylink.inetpay.common.project.portal.restVO.withdrawRestVO.WithdrawRespListVO;
import com.ylink.inetpay.common.project.portal.restVO.withdrawRestVO.WithdrawRespVO;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgInfoRespVo;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgInfoRestVo;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgResponseVO;
import com.ylink.inetpay.common.project.portal.vo.AccountVO;
import com.ylink.inetpay.common.project.portal.vo.BaseRespVO;
import com.ylink.inetpay.common.project.portal.vo.ContactListMsgRespVo;
import com.ylink.inetpay.common.project.portal.vo.ContactListMsgRestVo;
import com.ylink.inetpay.common.project.portal.vo.ContactMsgRespVO;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchRequestVO;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchResponseVO;
import com.ylink.inetpay.common.project.portal.vo.RespCheckVO;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.account.AccountAmtRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.AccountAmtResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.AccountInfoRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.AccountInfoResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.AccountMsgVO;
import com.ylink.inetpay.common.project.portal.vo.account.BankauthRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.BankauthResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.CheckEmailRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.FilesRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.LoginCheckResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.LoginRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.LoginResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.PersonAuditRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.PwdresetRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.QuickRechageRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.QuickRechageResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.SendEmailRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.SendsmsRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.SendsmsResponseVO;
import com.ylink.inetpay.common.project.portal.vo.account.SetPwdRequestVO;
import com.ylink.inetpay.common.project.portal.vo.account.WithdrawRequestVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.AccountRespVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.ActAccountRespVO;
import com.ylink.inetpay.common.project.portal.vo.bank.BankAddVo;
import com.ylink.inetpay.common.project.portal.vo.bank.WithdrawVo;
import com.ylink.inetpay.common.project.portal.vo.customer.BankBusiReqVO;
import com.ylink.inetpay.common.project.portal.vo.customer.PersonVO;
import com.ylink.inetpay.common.project.portal.vo.safe.EditPayPwdVo;

import net.sf.json.JSONObject;

@Service("portalRestAccountService")
public class PortalRestAccountServiceImpl implements PortalRestAccountService {

	private final static Logger log = LoggerFactory.getLogger(PortalRestAccountServiceImpl.class);

	private static final String PERSON_UPLOAD = "person_upload/";

	public static final String SESSION_USER = "session_user";
	@Autowired
	MrsAccountAppService mrsAccountAppService;
	@Autowired
	BisCheckEmailAppService bisCheckEmailAppService;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private MrsLoginUserAppService mrsLoginUserAppService;
	@Autowired
	private ActAccountService actAccountService;
	@Autowired
	private BisExceptionLogAppService bisExceptionLogAppService;
	@Autowired
	private BisSmsAppService bisSmsAppService;
	@Autowired
	private MrsLoginUserService mrsLoginUserService;
	@Autowired
	private BisEmailService bisEmailService;
	@Autowired
	private MrsBankBusiDtoService mrsBankBusiDtoService;
	@Autowired
	private MrsPersonService mrsPersonService;
	@Autowired
	private MrsWithdrawAuditAppService mrsWithdrawAuditAppService;
	@Autowired
	private BisSysParamAppService bisSysParamAppService;
	@Autowired
	private MrsUserPayPasswordService mrsUserPayPasswordService;
	@Autowired
	ActBillService actBillService;
	@Autowired
	private PayPaymentService payPaymentService;
	@Autowired
	private SmsManageAppService smsManageAppService;
	@Autowired
	private PayRechargeService payRechargeService;
	@Autowired
	private PayWithdrawService payWithdrawService;
	@Autowired
	private MrsContactListService mrsContactListService;
	@Autowired
	private MrsSubAccountService mrsSubAccountService;
	@Autowired
	private MrsUserAccountDtoMapper mrsUserAccountDtoMapper;

	/**
	 * 一户通信息查询接口
	 */
	@Override
	public LoginResponseVO findCustInfo(String params) {
		log.info("一户通信息查询接口,请求参数:{}", params);
		LoginRequestVO reqVO = new LoginRequestVO();
		LoginResponseVO respVO = new LoginResponseVO();

		try {
			log.info("--------进入一户通信息查询方法：receive:" + params);
			log.debug("-----------第一步：转换LoginRequestVO对象");
			reqVO = toLoginRequestVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForFindCustInfo(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			MrsLoginUserDto dto = mrsLoginUserAppService.selectLoginUserByCustId(reqVO.getCustId());

			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(dto.getId());

			BeanUtils.copyProperties(dto, respVO);

			if (dto.getLoginTime() != null) {
				respVO.setLoginTime(DateUtils.dateToDateTime(dto.getLoginTime()));
			}
			respVO.setCreateTime(DateUtils.dateToDateTime(dto.getCreateTime()));
			if (null != accountMsgs && accountMsgs.size() > 0) {
				AccountMsg accountMsg = accountMsgs.get(0);
				if (accountMsg != null) {
					respVO.setCoustId(accountMsg.getCustId());
					respVO.setCoustname(accountMsg.getCustomerName());
					respVO.setCustId(accountMsg.getCustId());
					respVO.setCustomerName(accountMsg.getCustomerName());
					respVO.setAccountCode(accountMsg.getCustomerCode());
					respVO.setAccountType(accountMsg.getAccountType());
					respVO.setPlatformCode(accountMsg.getPlatformCode());
					respVO.setCustomerType(accountMsg.getCustomerType());
					respVO.setAccountStatus(accountMsg.getAccountStatus());
				}
				List<AccountMsgVO> accountMsgVOs = new ArrayList<AccountMsgVO>();
				for (AccountMsg act : accountMsgs) {
					AccountMsgVO actVo = new AccountMsgVO();
					BeanUtils.copyProperties(act, actVo);
					accountMsgVOs.add(actVo);
				}
				respVO.setAccountMsgs(accountMsgVOs);
			}

			// 返回
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("一户通信息查询接口完成,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("一户通信息查询接口失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通编号：%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("一户通信息查询接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("一户通信息查询失败");
			saveErrorExcetpionLog(String.format("一户通编号：%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForFindCustInfo(LoginRequestVO reqVO) {
		if (reqVO == null) {
			return "LoginRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "custId为空";
		}
		if (reqVO.getCustId().length() > 16) {
			return "custId超长";
		}

		return null;
	}

	/**
	 * 用户免密登录
	 */
	@Override
	public LoginResponseVO loginToken(String params) {
		log.info("免密登录接口,请求参数:{}", params);
		LoginRequestVO reqVO = new LoginRequestVO();
		LoginResponseVO respVO = new LoginResponseVO();
		try {
			log.info("--------进入一户通信息查询方法：receive:" + params);
			log.debug("-----------第一步：转换LoginRequestVO对象");
			reqVO = toLoginRequestVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForLoginToken(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			// 调用登录接口----只支持个人登录
			//loginToken值即为登陆用户主键
			String loginId = RedisCacheUtil.getString(reqVO.getLoginToken());
			MrsLoginUserDto dto = mrsLoginUserService.findById(loginId);
			MrsLoginUserDto doLogin = mrsLoginUserAppService.doLoginForToken(dto);
			// 检查登录返回的结果
			if (doLogin.getUserCheckVo().isCheckValue()) {
				toDoDate(respVO, doLogin);
			} else {
				log.error(doLogin.getUserCheckVo().getMsg());
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(doLogin.getUserCheckVo().getMsg());
				return respVO;
			}

			// 返回
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("免密登录接口完成,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("免密登录接口失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("免密登录账户：%s", reqVO.getLoginId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("免密登录接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("免密登录失败");
			saveErrorExcetpionLog(String.format("免密登录账户：%s", reqVO.getLoginId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForLoginToken(LoginRequestVO reqVO) {
		if (reqVO == null) {
			return "LoginRequestVO对象问空";
		}
		if (reqVO.getLoginToken().length() > 32) {
			return "loginToken超长";
		}

		return null;
	}

	@Override
	public LoginResponseVO loginIndex(String params) {
		log.info("客户登录接口,请求参数:{}", params);
		LoginRequestVO reqVO = new LoginRequestVO();
		LoginResponseVO respVo = new LoginResponseVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toLoginRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkLoginParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 调用登录接口----只支持个人登录
			MrsLoginUserDto dto = this.getServiceUserDto(reqVO.getLoginId(), reqVO.getPassword());
			MrsLoginUserDto doLogin = null;
			doLogin = mrsLoginUserAppService.doLogin(dto);
			// 检查登录返回的结果
			if (doLogin.getUserCheckVo().isCheckValue()) {
				toDoDate(respVo, doLogin);

				String oldToken = RedisCacheUtil.getString(doLogin.getId());
				if(StringUtils.isNotBlank(oldToken)){
					// 删除原来的RedisCache
					RedisCacheUtil.evict(doLogin.getId());
					RedisCacheUtil.evict(oldToken);
				}
				// 生成Token
				String loginToken = SerialNumber.generatePortalToken(doLogin.getId());

				// 保存到RedisCache服务器----用于删除
				RedisCacheUtil.setNX(doLogin.getId(), loginToken, SerialNumber.CACHE_TIME_OUT);
				// 保存到RedisCache服务器----提供给用户
				RedisCacheUtil.setNX(loginToken, doLogin.getId(), SerialNumber.CACHE_TIME_OUT);

				respVo.setLoginToken(loginToken);
			} else {
				log.error(doLogin.getUserCheckVo().getMsg());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(doLogin.getUserCheckVo().getMsg());
				return respVo;
			}

			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("客户登录接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("客户登录接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("客户登录账户：%s", reqVO.getLoginId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("客户登录接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("客户登录失败");
			saveErrorExcetpionLog(String.format("客户登录账户：%s", reqVO.getLoginId(), e.getMessage()));
			return respVo;
		}
	}

	private void toDoDate(LoginResponseVO respVo, MrsLoginUserDto doLogin) {
		BeanUtils.copyProperties(doLogin, respVo);
		if (doLogin.getLoginTime() != null) {
			respVo.setLoginTime(DateUtils.dateToDateTime(doLogin.getLoginTime()));
		}
		respVo.setCreateTime(DateUtils.dateToDateTime(doLogin.getCreateTime()));
		if (null != doLogin.getAccountMsgs() && doLogin.getAccountMsgs().size() > 0) {
			AccountMsg accountMsg = doLogin.getAccountMsgs().get(0);
			if (accountMsg != null) {
				respVo.setCoustId(accountMsg.getCustId());
				respVo.setCoustname(accountMsg.getCustomerName());
				respVo.setCustId(accountMsg.getCustId());
				respVo.setCustomerName(accountMsg.getCustomerName());
				respVo.setAccountCode(accountMsg.getCustomerCode());
				respVo.setAccountType(accountMsg.getAccountType());
				respVo.setPlatformCode(accountMsg.getPlatformCode());
				respVo.setCustomerType(accountMsg.getCustomerType());
				respVo.setAccountStatus(accountMsg.getAccountStatus());
				respVo.setSetPayPwd(doLogin.isSetPayPwd());
			}
			List<AccountMsgVO> accountMsgs = new ArrayList<AccountMsgVO>();
			for (AccountMsg act : doLogin.getAccountMsgs()) {
				AccountMsgVO actVo = new AccountMsgVO();
				BeanUtils.copyProperties(act, actVo);
				accountMsgs.add(actVo);
			}
			respVo.setAccountMsgs(accountMsgs);
		}
	}

	@Override
	public AccountAmtResponseVO findAccountAmt(String params) {
		log.info("根据一户通查询账户余额接口,请求参数:{}", params);
		AccountAmtRequestVO reqVO = new AccountAmtRequestVO();
		AccountAmtResponseVO respVo = new AccountAmtResponseVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toAccountAmtRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkAccountAmtParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 根据一户通查询账户余额
			ActAccountDto actAccountDto = null;
			List<String> acctTypeNos = new ArrayList<String>();
			List<EActBusiRefSubBusiType> busiTypes = new ArrayList<EActBusiRefSubBusiType>();
			acctTypeNos.add(reqVO.getActBusiType());
			busiTypes.add(EActBusiRefSubBusiType.getEnum(reqVO.getActSubBusiType()));
			List<ActBusiRefSubDto> actList = mrsAccountService.findByAcctTypeNos(acctTypeNos, busiTypes);
			if (actList != null && actList.size() > 0) {
				List<ActAccountDto> accountList = actAccountService.findAcctIdByCustIdAndSubjectNo2(reqVO.getCustId(),
						actList.get(0).getSub2No());
				if (accountList != null && accountList.size() > 0) {
					actAccountDto = accountList.get(0);
				}
			} else {
				log.error("没有查询到对应的数据");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("没有查询到对应的数据");
				return respVo;
			}

			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			respVo.setAccountId(actAccountDto.getAccountId());
			respVo.setCashAmount(actAccountDto.getCashAmount() + "");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("根据一户通查询账户余额完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("根据一户通查询账户余额失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通账号：%s,资金账户类型：%s,子资金账户类型：%s", reqVO.getCustId(),
					reqVO.getActBusiType(), reqVO.getActSubBusiType(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("根据一户通查询账户余额失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("失败");
			saveErrorExcetpionLog(String.format("一户通账号：%s,资金账户类型：%s,子资金账户类型：%s", reqVO.getCustId(),
					reqVO.getActBusiType(), reqVO.getActSubBusiType(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public AccountInfoResponseVO actInfo(String params) {
		log.info("账户资金查询接口,请求参数:{}", params);
		AccountInfoRequestVO reqVO = new AccountInfoRequestVO();
		AccountInfoResponseVO respVo = new AccountInfoResponseVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toAccountInfoRequestVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}

			// 解析参数封装成request对象
			String checkResult = checkAccountInfoParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 根据一户通查询账户余额
			AccountRespVO accountRespVO = mrsAccountAppService.findAccountDtoByCustId(reqVO.getCustId());
			if (accountRespVO != null) {
				BeanUtils.copyProperties(accountRespVO, respVo);
				//检查具体的资金是否有对应的数据
				if(!accountRespVO.isCheckSubAcctTypeBase()
						&& !accountRespVO.isCheckSubAcctTypeManager()
						&& !accountRespVO.isCheckSubAcctTypeCyr()){
					log.error("没有查询到对应的数据");
					respVo.setMsgCode(PortalCode.CODE_9999);
					respVo.setMsgInfo("没有查询到对应的数据");
					respVo.setActAccountRespVOList(null);
					return respVo;
				}
			} else {
				log.error("没有查询到对应的数据");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("没有查询到对应的数据");
				return respVo;
			}

			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("账户资金查询接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("账户资金查询接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通账号：%s", reqVO.getCustId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("账户资金查询接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("失败");
			saveErrorExcetpionLog(String.format("一户通账号：%s", reqVO.getCustId(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public LoginCheckResponseVO checkLoginUser(String params) {
		log.info("验证用户接口,请求参数:{}", params);
		LoginRequestVO reqVO = new LoginRequestVO();
		LoginCheckResponseVO respVo = new LoginCheckResponseVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toLoginRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkLoginUserParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 处理
			MrsLoginUserDto dto = this.getServiceUserDto(reqVO.getLoginId(), null);
			MrsLoginUserDto doLogin = null;
			doLogin = mrsLoginUserAppService.getByLoginName(dto);
			if (doLogin != null && StringUtils.isNotBlank(doLogin.getId())) {
				BeanUtils.copyProperties(doLogin, respVo);
				respVo.setLoginUserId(doLogin.getId());
			} else {
				log.error("没有查询到对应的数据");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("没有查询到对应的数据");
				return respVo;
			}

			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("验证用户接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("验证用户接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("登录账号：%s", reqVO.getLoginId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("验证用户接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("失败");
			saveErrorExcetpionLog(String.format("登录账号：%s", reqVO.getLoginId(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public SendsmsResponseVO sendsms(String params) {
		log.info("发送手机短信接口 ,请求参数:{}", params);
		SendsmsRequestVO reqVO = new SendsmsRequestVO();
		SendsmsResponseVO respVo = new SendsmsResponseVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toSendsmsRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkSendsmsParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 处理
			log.info("发送短信验证码");
			String num = RandomUtil.generateNumber6Random(6);
			List<String> list = new ArrayList<String>();
			list.add(num);
			bisSmsAppService.sendSms(reqVO.getMobile(), EBisSmsSystem.PORTAL, list, EBisTemplateCode.SMS_CODE,
					EmessType.MESSAGE_NOTIFICATION);
			if (StringUtils.isNotBlank(num)) {
				respVo.setSmscode(num);
			} else {
				log.error("发送短信验证码出错");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("发送短信验证码出错");
				return respVo;
			}

			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("发送手机短信接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("发送手机短信接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("手机号：%s", reqVO.getMobile(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("发送手机短信接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("发送手机短信失败");
			saveErrorExcetpionLog(String.format("手机号：%s", reqVO.getMobile(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public BaseRespVO updateLoginPwd(String params) {
		log.info("重置用户登录密码接口 ,请求参数:{}", params);
		PwdresetRequestVO reqVO = new PwdresetRequestVO();
		BaseRespVO respVo = new BaseRespVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toPwdresetRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkPwdresetParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 处理
			mrsLoginUserService.updateLoginPwd(reqVO.getLoginUserId(), reqVO.getLoginPwd());

			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("重置用户登录密码接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("重置用户登录密码接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("重置用户登录密码接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("重置用户登录密码失败");
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public BaseRespVO sendemail(String params) {
		log.info("发送邮件接口 ,请求参数:{}", params);
		SendEmailRequestVO reqVO = new SendEmailRequestVO();
		BaseRespVO respVo = new BaseRespVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toSendEmailRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkSendEmailParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 处理
			Map<String, Object> emailparams = new HashMap<String, Object>();
			emailparams.put(SHIEConfigConstant.CUSTMER_NAME, reqVO.getCustomerName());

			UserCheckVO checkVo = bisEmailService.shieSendEmail(reqVO.getEmail(), reqVO.getLoginUserId(),
					EBisSmsSystem.getEnum(reqVO.getSubSystem()),
					EBisEmailTemplateCode.getEnum(reqVO.getEmailTemplate()), emailparams, null);
			if (!checkVo.isCheckValue()) {
				log.error("邮件发送失败:" + checkVo.getMsg());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("邮件发送失败:" + checkVo.getMsg());
				return respVo;
			}

			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("发送邮件接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("发送邮件接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("发送邮件接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("发送邮件失败");
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public LoginCheckResponseVO checkEmail(String params) {
		log.info("校验邮件接口 ,请求参数:{}", params);
		CheckEmailRequestVO reqVO = new CheckEmailRequestVO();
		LoginCheckResponseVO respVo = new LoginCheckResponseVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toCheckEmailRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkCheckEmailParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 处理
			MrsLoginUserDto shieCheckEmail = bisCheckEmailAppService.shieCheckEmail(reqVO.getCallback_key(),
					reqVO.getLoginUserId(), EBisEmailTemplateCode.getEnum(reqVO.getEmailTemplate()));
			if (shieCheckEmail.getUserCheckVo().isCheckValue()) {
				respVo.setLoginUserId(shieCheckEmail.getId());
				respVo.setCustId(shieCheckEmail.getCustId());
				respVo.setCustomerName(shieCheckEmail.getCustomerName());
				respVo.setAlias(shieCheckEmail.getAlias());
				respVo.setMobile(shieCheckEmail.getMobile());
				respVo.setEmail(shieCheckEmail.getEmail());
			} else {
				String emailMsg = "验证邮件处理失败！";
				if (shieCheckEmail != null && null != shieCheckEmail.getUserCheckVo()) {
					emailMsg = shieCheckEmail.getUserCheckVo().getErrorMsg().getValue();
				}
				log.error(emailMsg);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(emailMsg);
				return respVo;
			}
			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("校验邮件接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("校验邮件接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("校验邮件接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("校验邮件失败");
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public BaseRespVO aduitByPerson(String params) {
		log.info("提交资料(个人)身份证和被动开户接口 ,请求参数:{}", params);
		PersonAuditRequestVO reqVO = new PersonAuditRequestVO();
		LoginCheckResponseVO respVo = new LoginCheckResponseVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toPersonAuditRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkPersonAuditParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 处理
			PersonVO personVo = new PersonVO();
			personVo.setCustId(reqVO.getCustId());
			personVo.setCredentialsType(reqVO.getCredentialsType());
			personVo.setCredentialsNumber(reqVO.getCredentialsNumber());
			personVo.setCustomerName(reqVO.getCustomerName());
			MrsLoginUserDto loginUser = new MrsLoginUserDto();
			loginUser.setId(reqVO.getLoginUserId());
			// 附件信息集合
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDto = new ArrayList<MrsAduitAttachmentDto>();
			if (reqVO.getFiles() != null && reqVO.getFiles().size() > 0) {
				for (FilesRequestVO filesRequestVO : reqVO.getFiles()) {
					MrsAduitAttachmentDto fileVo = new MrsAduitAttachmentDto();
					fileVo.setId(filesRequestVO.getStoragePath());
					fileVo.setFileSize(Long.valueOf(filesRequestVO.getFileSize()));
					fileVo.setName(filesRequestVO.getFileName());
					fileVo.setSuffix(filesRequestVO.getSuffix());
					mrsAduitAttachmentDto.add(fileVo);
				}
			}
			PersonVO personVO = mrsPersonService.saveAduitPersonByPortalRest(personVo, loginUser);
			if (!personVO.getCheckVo().isCheckValue()) {
				log.error("提交资料(个人)身份证和被动开户失败");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("提交资料(个人)身份证和被动开户失败");
				return respVo;
			}
			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("提交资料(个人)身份证和被动开户接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("提交资料(个人)身份证和被动开户接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("提交资料(个人)身份证和被动开户接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("提交资料(个人)身份证和被动开户失败");
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public BankauthResponseVO dobankauth(String params) {
		log.info("授信绑卡实名认证接口 ,请求参数:{}", params);
		BankauthRequestVO reqVO = new BankauthRequestVO();
		BankauthResponseVO respVo = new BankauthResponseVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toCheckBankauthRequestVO(params);
			// 解析参数封装成request对象
			String checkResult = checkBankauthParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 处理
			BankAddVo vo = new BankAddVo();
			vo.setAccNo(reqVO.getAccNo());
			vo.setBankCode(reqVO.getBankCode());
			vo.setBankMoblePwd(reqVO.getBankMoblePwd());
			vo.setBankType(reqVO.getBankType());
			vo.setCardType(BankCardType.DEBIT.getValue());
			vo.setCity(reqVO.getCity());
			vo.setCustName(reqVO.getCustomerName());
			vo.setIdNo(reqVO.getCredentialsNumber());
			vo.setIdType(reqVO.getCredentialsType());
			vo.setOpenBankName(reqVO.getOpenBankName());
			vo.setPayType(reqVO.getPayType());
			vo.setProvince(reqVO.getProvince());
			vo.setRegionCode(reqVO.getRegion());
			RespCheckVO respCheckVO = mrsBankBusiDtoService.addBanksx(vo, MrsCustomerType.MCT_0.getValue(),
					reqVO.getCustId(), reqVO.getCustomerName(), reqVO.getLoginUserId());
			if (!respCheckVO.isCheckValue()) {
				if (ErrorMsgEnum.ADD_OUT_LINE_REMIND.equals(respCheckVO.getErrorMsg())) {
					String authNo = respCheckVO.getMsg();
					if (StringUtils.isNotEmpty(authNo) && authNo.length() > 7) {
						authNo = authNo.substring(7);
					}
					respVo.setReturnCode(ErrorMsgEnum.ADD_OUT_LINE_REMIND.getKey());
					respVo.setReturnMsg("已受理成功，请凭资金账号【" + authNo + "】到银行柜台办理银期或银商签约");
				} else if (ErrorMsgEnum.ADD_CARD_FAIL.equals(respCheckVO.getErrorMsg())) {
					respVo.setReturnCode(ErrorMsgEnum.ADD_CARD_FAIL.getKey());
					respVo.setReturnMsg(ErrorMsgEnum.ADD_CARD_FAIL.getValue());
				} else if (ErrorMsgEnum.SUCCESS_NEED_ADUIT.equals(respCheckVO.getErrorMsg())) {
					respVo.setReturnCode(ErrorMsgEnum.SUCCESS_NEED_ADUIT.getKey());
					respVo.setReturnMsg("已银保处理，需审核！");
				} else {
					String reMsg = "授信绑卡实名认证失败";
					log.error(reMsg);
					respVo.setMsgCode(PortalCode.CODE_9999);
					respVo.setMsgInfo(reMsg);
					return respVo;
				}

			} else {
				respVo.setReturnCode(ErrorMsgEnum.ADD_CARD_SUCC.getKey());
				respVo.setReturnMsg(ErrorMsgEnum.ADD_CARD_SUCC.getValue());
			}
			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("授信绑卡实名认证接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("授信绑卡实名认证接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("授信绑卡实名认证接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("授信绑卡实名认证失败");
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public QuickRechageResponseVO quickRecharge(String params) {
		log.info("充值处理接口 ,请求参数:{}", params);
		QuickRechageRequestVO reqVO = new QuickRechageRequestVO();
		QuickRechageResponseVO respVo = new QuickRechageResponseVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toQuickRechageRequestVO(params);
			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}
			// 解析参数封装成request对象
			String checkResult = checkQuickRechageParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			//校验数据的相关正确性
			
			String checkCustInforesult = checkCustInfo(reqVO);
			if (checkCustInforesult != null) {
				log.error("参数[" + params + "]校验失败:" + checkCustInforesult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkCustInforesult);
				return respVo;
			}
			// 处理
			Map<String, String> reqMap = assebMapParam(reqVO);
			String message = MapUtil.map2UrlStr(reqMap); // 转换成get请求参数进行验签
			String sigture = Md5Util.MD5Encoder(message + "&key=" + SHIEConfigConstant.SALT, "UTF-8");
			String accessUrl = null;
			try {
				accessUrl = bisSysParamAppService.getValue(SystemParamConstants.ACCESS_SYS_URL);
			} catch (CbsCheckedException e) {
				log.error("提现处理失败:获取系统前置系统URL参数异常");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("提现处理失败:获取系统前置系统URL参数异常");
				return respVo;
			}
			String messageBase64 = Base64.encodeBase64URLSafeString(message.getBytes("UTF-8"));
			String sigtureBase64 = Base64.encodeBase64URLSafeString(sigture.getBytes());
			accessUrl = accessUrl + "recharge/checkstand?from=p&message="
					+ EncryptionDecryptionUtil.encrypt(messageBase64, "UTF-8") + "&signature=" + sigtureBase64;
			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			respVo.setAccessUrl(accessUrl);
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("充值处理接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("充值处理接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通账号：%s", reqVO.getCustId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("充值处理接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("充值处理失败");
			saveErrorExcetpionLog(String.format("一户通账号：%s", reqVO.getCustId(), e.getMessage()));
			return respVo;
		}
	}

	@Override
	public BaseRespVO dowithdraw(String params) {
		log.info("提现处理接口 ,请求参数:{}", params);
		WithdrawRequestVO reqVO = new WithdrawRequestVO();
		BaseRespVO respVo = new BaseRespVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toWithdrawRequestVO(params);
			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}
			// 解析参数封装成request对象
			String checkResult = checkWithdrawParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 处理
			WithdrawVo withdrawVo = new WithdrawVo();
			withdrawVo.setLoginUserId(reqVO.getLoginUserId());
			withdrawVo.setLoginUserAlias(reqVO.getAlias());
			withdrawVo.setCustId(reqVO.getCustId());
			withdrawVo.setCustName(reqVO.getCustomerName());
			withdrawVo.setWithdrawAmt(reqVO.getWithdrawAmt());
			withdrawVo.setBankBusiId(reqVO.getBankId());
			withdrawVo.setActBusiType(reqVO.getActBusiType());
			ResultCodeDto<PayWithdrawDto> resultCodeDto = null;
			resultCodeDto = mrsWithdrawAuditAppService.doWithdrawApply(withdrawVo, EBindCardTypeCode.CREDIT_BIND);
			// 提现处理结果
			if (resultCodeDto == null || !EResultCode.SUCCESS.equals(resultCodeDto.getResultCode())) {
				log.error("提现处理失败:" + resultCodeDto.getMsgDetail());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("提现处理失败:" + resultCodeDto.getMsgDetail());
				return respVo;
			}

			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("提现处理接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("提现处理接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(
					String.format("登录用户主键Key：%s,一户通账号：%s", reqVO.getLoginUserId(), reqVO.getCustId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("提现处理接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("提现处理失败");
			saveErrorExcetpionLog(
					String.format("登录用户主键Key：%s,一户通账号：%s", reqVO.getLoginUserId(), reqVO.getCustId(), e.getMessage()));
			return respVo;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkAccountAmtParams(AccountAmtRequestVO accountAmtReqVO) {
		if (accountAmtReqVO == null) {
			return "AccountAmtRequestVO对象问空";
		}
		if (StringUtil.isEmpty(accountAmtReqVO.getCustId())) {
			return "一户通账号为空";
		}
		if (accountAmtReqVO.getCustId().length() > 16) {
			return "一户通账号超长";
		}

		if (StringUtil.isEmpty(accountAmtReqVO.getActBusiType())) {
			return "资金账户类型为空";
		}
		if (accountAmtReqVO.getActBusiType().length() > 4) {
			return "资金账户类型超长";
		}
		if (StringUtil.isNEmpty(accountAmtReqVO.getActBusiType())
				&& SubAcctType.getEnum(accountAmtReqVO.getActBusiType()) == null) {
			return "资金账户类型不存在";
		}

		if (StringUtil.isEmpty(accountAmtReqVO.getActSubBusiType())) {
			return "子资金账户类型为空";
		}
		if (accountAmtReqVO.getActSubBusiType().length() > 2) {
			return "子资金账户类型超长";
		}
		if (StringUtil.isNEmpty(accountAmtReqVO.getActSubBusiType())
				&& EActBusiRefSubBusiType.getEnum(accountAmtReqVO.getActSubBusiType()) == null) {
			return "子资金账户类型不存在";
		}
		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkSendEmailParams(SendEmailRequestVO reqVO) {
		if (reqVO == null) {
			return "SendEmailRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getEmail())) {
			return "邮箱为空";
		}
		if (reqVO.getEmail().length() > 64) {
			return "邮箱超长";
		}

		if (StringUtil.isEmpty(reqVO.getLoginUserId())) {
			return "登录用户主键Key为空";
		}
		if (reqVO.getLoginUserId().length() > 36) {
			return "登录用户主键Key超长";
		}

		if (StringUtil.isEmpty(reqVO.getSubSystem())) {
			return "子系统类型为空";
		}
		if (reqVO.getSubSystem().length() > 2) {
			return "子系统类型超长";
		}
		if (StringUtil.isNEmpty(reqVO.getSubSystem()) && EBisSmsSystem.getEnum(reqVO.getSubSystem()) == null) {
			return "子系统类型不存在";
		}
		if (StringUtil.isEmpty(reqVO.getEmailTemplate())) {
			return "邮件模板为空";
		}
		if (reqVO.getEmailTemplate().length() > 2) {
			return "邮件模板超长";
		}
		if (StringUtil.isNEmpty(reqVO.getEmailTemplate())
				&& EBisEmailTemplateCode.getEnum(reqVO.getEmailTemplate()) == null) {
			return "邮件模板不存在";
		}

		if (StringUtil.isEmpty(reqVO.getCustomerName())) {
			return "客户名称为空";
		}
		if (reqVO.getCustomerName().length() > 30) {
			return "客户名称超长";
		}
		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkCheckEmailParams(CheckEmailRequestVO reqVO) {
		if (reqVO == null) {
			return "CheckEmailRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getCallback_key())) {
			return "邮箱回调Key为空";
		}
		if (reqVO.getCallback_key().length() > 36) {
			return "邮箱回调Key超长";
		}

		if (StringUtil.isEmpty(reqVO.getLoginUserId())) {
			return "登录用户主键Key为空";
		}
		if (reqVO.getLoginUserId().length() > 36) {
			return "登录用户主键Key超长";
		}

		if (StringUtil.isEmpty(reqVO.getEmailTemplate())) {
			return "邮件模板为空";
		}
		if (reqVO.getEmailTemplate().length() > 2) {
			return "邮件模板超长";
		}
		if (StringUtil.isNEmpty(reqVO.getEmailTemplate())
				&& EBisEmailTemplateCode.getEnum(reqVO.getEmailTemplate()) == null) {
			return "邮件模板不存在";
		}

		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkQuickRechageParams(QuickRechageRequestVO reqVO) {
		if (reqVO == null) {
			return "QuickRechageRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "一户通账号为空";
		}
		if (reqVO.getCustId().length() > 16) {
			return "一户通账号超长";
		}
		String custId = reqVO.getCustId();
		if(StringUtil.isNEmpty(custId) 
				&& !Pattern.compile(RegexEnum.REGEX_CH_EH_NUM_80.getRegexValue()).matcher(custId).matches()) {
			return "一户通账号格式错误";
		}
		
		if (StringUtil.isEmpty(reqVO.getAccountId())) {
			return "资金账户编号为空";
		}
		if (reqVO.getAccountId().length() > 36) {
			return "资金账户编号超长";
		}
		String accountId = reqVO.getAccountId();
		if(StringUtil.isNEmpty(accountId) 
				&& !Pattern.compile(RegexEnum.REGEX_CH_EH_NUM_80.getRegexValue()).matcher(accountId).matches()) {
			return "一户通账号格式错误";
		}
		
		if (StringUtil.isEmpty(reqVO.getActBusiType())) {
			return "资金账户类型为空";
		}
		if (reqVO.getActBusiType().length() > 4) {
			return "资金账户类型超长";
		}
		if (StringUtil.isNEmpty(reqVO.getActBusiType()) && SubAcctType.getEnum(reqVO.getActBusiType()) == null) {
			return "资金账户类型不存在";
		}
		if (StringUtil.isEmpty(reqVO.getCustomerName())) {
			return "客户姓名为空";
		}
		if (reqVO.getCustomerName().length() > 32) {
			return "客户姓名超长";
		}
		String amt= reqVO.getRechargeAmt();
		if (StringUtil.isEmpty(amt)) {
			return "金额为空";
		}
		if (amt.length() > 18) {
			return "金额超长";
		}
		if(StringUtil.isNEmpty(amt) && !Pattern.compile(RegexEnum.REGEX_AMOUNT18.getRegexValue()).matcher(amt).matches()) {
			return "金额格式错误";
		}
		return null;
	}
	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkCustInfo(QuickRechageRequestVO reqVO) {
		if (reqVO == null) {
			return "QuickRechageRequestVO对象问空";
		}
		AccountRespVO accountRespVO= mrsAccountAppService.findAccountDtoByCustId(reqVO.getCustId());
		MrsLoginUserDto dto = mrsLoginUserAppService.selectLoginUserByCustId(reqVO.getCustId());
		List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(dto.getId());
		if(accountRespVO!=null){
			if(!accountRespVO.isCheckSubAcctTypeBase()&&SubAcctType.BASE.getValue().equals(reqVO.getActBusiType())){
				return "请校验数据的相关正确性,资金账户类型不对";
			}
			if(!accountRespVO.isCheckSubAcctTypeManager()&&SubAcctType.MANAGER.getValue().equals(reqVO.getActBusiType())){
				return "请校验数据的相关正确性,资金账户类型不对";
			}
			if(!accountRespVO.isCheckSubAcctTypeCyr()&&SubAcctType.PARTICIPANT_BASE.getValue().equals(reqVO.getActBusiType())){
				return "请校验数据的相关正确性,资金账户类型不对";
			}
			boolean checkid=true;
			if(accountRespVO.getActAccountRespVOList()!=null&& accountRespVO.getActAccountRespVOList().size()>0){
				for(ActAccountRespVO actAccountRespVO :accountRespVO.getActAccountRespVOList()){
					if(actAccountRespVO.getSubjectNo2().getAccountId().equals(reqVO.getAccountId())){
						checkid=false;
					}
				}
			}
			if(checkid){
				return "请校验数据的相关正确性,资金账户编号不对";
			}
			if(accountMsgs!=null && !accountMsgs.get(0).getCustomerName().equals(reqVO.getCustomerName())){
				return "请校验数据的相关正确性,客户姓名不对";
			}
		}else{
			return "请校验数据的相关正确性";
		}
		return null;
	}
	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkWithdrawParams(WithdrawRequestVO reqVO) {
		if (reqVO == null) {
			return "WithdrawRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "一户通账号为空";
		}
		if (reqVO.getCustId().length() > 16) {
			return "一户通账号超长";
		}
		String custId = reqVO.getCustId();
		if(StringUtil.isNEmpty(custId) 
				&& !Pattern.compile(RegexEnum.REGEX_CH_EH_NUM_80.getRegexValue()).matcher(custId).matches()) {
			return "一户通账号格式错误";
		}
		/*
		 * if (StringUtil.isEmpty(reqVO.getAccountId())) { return "资金账户编号为空"; }
		 * if (reqVO.getAccountId().length() > 16) { return "资金账户编号超长"; }
		 */
		
		if (StringUtil.isEmpty(reqVO.getActBusiType())) {
			return "资金账户类型为空";
		}
		if (reqVO.getActBusiType().length() > 4) {
			return "资金账户类型超长";
		}
		if (StringUtil.isNEmpty(reqVO.getActBusiType()) && SubAcctType.getEnum(reqVO.getActBusiType()) == null) {
			return "资金账户类型不存在";
		}

		if (StringUtil.isEmpty(reqVO.getLoginType())) {
			return "客户类型为空";
		}
		if (reqVO.getLoginType().length() > 1) {
			return "客户类型超长";
		}
		if (StringUtil.isNEmpty(reqVO.getLoginType()) 
				&& !MrsCustomerType.MCT_0.getValue().equals(reqVO.getLoginType()) ) {
			return "客户类型只支持:个人用户[0]";
		}

		/*
		 * if (StringUtil.isEmpty(reqVO.getLoginUserId())) { return
		 * "登录用户主键Key为空"; }
		 */
		if (reqVO.getLoginUserId().length() > 36) {
			return "登录用户主键Key超长";
		}
		/*
		 * if (StringUtil.isEmpty(reqVO.getCustomerName())) { return "客户姓名为空"; }
		 */
		if (reqVO.getCustomerName().length() > 32) {
			return "客户姓名超长";
		}
		/*
		 * if (StringUtil.isEmpty(reqVO.getAlias())) { return "客户昵称为空"; }
		 */
		if (reqVO.getAlias().length() > 32) {
			return "客户昵称超长";
		}
		if (StringUtil.isEmpty(reqVO.getBankId())) {
			return "银行列表主键Key为空";
		}
		if (reqVO.getBankId().length() > 36) {
			return "银行列表主键Key超长";
		}
		/*
		 * if (StringUtil.isEmpty(reqVO.getBankType())) { return "银行类型为空"; } if
		 * (reqVO.getBankType().length() > 3) { return "银行类型超长"; } if
		 * (StringUtil.isEmpty(reqVO.getAccNo())) { return "银行账号为空"; } if
		 * (reqVO.getAccNo().length() > 64) { return "银行账号超长"; }
		 */
		String amt= reqVO.getWithdrawAmt();
		if (StringUtil.isEmpty(amt)) {
			return "金额为空";
		}
		if (amt.length() > 18) {
			return "金额超长";
		}
		if(StringUtil.isNEmpty(amt) && !Pattern.compile(RegexEnum.REGEX_AMOUNT18.getRegexValue()).matcher(amt).matches()) {
			return "金额格式错误";
		}
		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkLoginParams(LoginRequestVO reqVO) {
		if (reqVO == null) {
			return "LoginRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getLoginId())) {
			return "登录用户Id为空";
		}
		if (reqVO.getLoginId().length() > 32) {
			return "登录用户Id超长";
		}
		if (StringUtil.isEmpty(reqVO.getPassword())) {
			return "登录用户密码为空";
		}
		if (reqVO.getPassword().length() > 64) {
			return "登录用户密码超长";
		}
		if (StringUtil.isEmpty(reqVO.getLoginType())) {
			return "客户类型为空";
		}
		if (reqVO.getLoginType().length() > 1) {
			return "客户类型超长";
		}
		if (StringUtil.isNEmpty(reqVO.getLoginType()) 
				&& !MrsCustomerType.MCT_0.getValue().equals(reqVO.getLoginType()) ) {
			return "客户类型只支持:个人用户[0]";
		}

		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkLoginUserParams(LoginRequestVO reqVO) {
		if (reqVO == null) {
			return "LoginRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getLoginId())) {
			return "登录用户Id为空";
		}
		if (reqVO.getLoginId().length() > 32) {
			return "登录用户Id超长";
		}
		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkSendsmsParams(SendsmsRequestVO reqVO) {
		if (reqVO == null) {
			return "SendsmsRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getMobile())) {
			return "手机号为空";
		}
		if (reqVO.getMobile().length() > 16) {
			return "手机号超长";
		}
		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkAccountInfoParams(AccountInfoRequestVO reqVO) {
		if (reqVO == null) {
			return "AccountInfoRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "一户通账号为空";
		}
		if (reqVO.getCustId().length() > 16) {
			return "一户通账号超长";
		}
		String custId = reqVO.getCustId();
		if(StringUtil.isNEmpty(custId) 
				&& !Pattern.compile(RegexEnum.REGEX_CH_EH_NUM_80.getRegexValue()).matcher(custId).matches()) {
			return "一户通账号格式错误";
		}
		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkLoginTokenParams(String loginToken,String custCode) {
		
		if (StringUtil.isEmpty(loginToken)) {
			return "登录Token为空";
		}
		if (loginToken.length() > 36) {
			return "登录Token超长";
		}
		if(StringUtil.isEmpty(custCode)){
			return "登陆要素[一户通账号、或微信号、或昵称、或手机号、或登陆用户ID]不可全部为空";
		}
		// 获取RedisCache服务器的Token(登陆用户Id)
		String loginUserId = RedisCacheUtil.getString(loginToken);
		MrsLoginUserDto mrsLonginUserDto = null;
		if (StringUtil.isEmpty(loginUserId)) {
			return "登录Token不存在或登录已超时";
		}
	    //根据Token查询用户信息
		mrsLonginUserDto = mrsLoginUserService.findUserDtoByLoginId(loginUserId);
		if(mrsLonginUserDto == null){
			mrsLonginUserDto = mrsLoginUserService.selectByPrimaryKey(loginUserId);
		    if(mrsLonginUserDto == null){
		    	return "根据Token查询用户信息失败！";
		    }
		}
		//根据Token查询一户通信息
		List<MrsAccountDto> mrsAccountDtos = mrsAccountService.findByLoginUserId(loginUserId);
		//已开通一户通账户
		int index = 0;
		if(!CollectionUtil.isEmpty(mrsAccountDtos)){
			for(MrsAccountDto mrsAccountDto:mrsAccountDtos){
				if(!(custCode.equals(mrsAccountDto.getCustId())||custCode.equals(mrsLonginUserDto.getAlias())
						||custCode.equals(mrsLonginUserDto.getMobile())||custCode.equals(mrsLonginUserDto.getWeChatId())||custCode.equals(mrsLonginUserDto.getId()))){
					index++;
				}
			}
			if(index==mrsAccountDtos.size()){
				return "登陆Token不匹配或者登陆信息不存在！";
			}
		}else{
			//未来开通一户通账户
			if(!loginUserId.equals(custCode)){
				return "登陆用户ID不存在！";
			}
		}
		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkPersonAuditParams(PersonAuditRequestVO reqVO) {
		if (reqVO == null) {
			return "PersonAuditRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getLoginUserId())) {
			return "登录用户主键Key为空";
		}
		if (reqVO.getLoginUserId().length() > 36) {
			return "登录用户主键Key超长";
		}

		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "一户通账号为空";
		}
		if (reqVO.getCustId().length() > 16) {
			return "一户通账号超长";
		}
		if (StringUtil.isEmpty(reqVO.getCredentialsType())) {
			return "证件类型为空";
		}
		if (reqVO.getCredentialsType().length() > 2) {
			return "证件类型超长";
		}
		if (StringUtil.isEmpty(reqVO.getCredentialsNumber())) {
			return "证件号码为空";
		}
		if (reqVO.getCredentialsNumber().length() > 32) {
			return "证件号码超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustomerName())) {
			return "客户姓名为空";
		}
		if (reqVO.getCustomerName().length() > 32) {
			return "客户姓名超长";
		}
		if (reqVO.getFiles() == null || reqVO.getFiles().size() == 0) {
			return "附件集合为空";
		}
		return null;
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkPwdresetParams(PwdresetRequestVO reqVO) {
		if (reqVO == null) {
			return "PwdresetRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getLoginUserId())) {
			return "登录用户主键Key为空";
		}
		if (reqVO.getLoginUserId().length() > 36) {
			return "登录用户主键Key超长";
		}
		if (StringUtil.isEmpty(reqVO.getLoginPwd())) {
			return "登录密码为空";
		}
		if (reqVO.getLoginPwd().length() > 64) {
			return "登录密码超长";
		}

		return null;
	}
	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkSetPwdParams(SetPwdRequestVO reqVO) {
		if (reqVO == null) {
			return "SetPwdRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getLoginUserId())) {
			return "登录用户主键Key为空";
		}
		if (reqVO.getLoginUserId().length() > 36) {
			return "登录用户主键Key超长";
		}
		if (StringUtil.isEmpty(reqVO.getOldpwd())) {
			return "原登录密码为空";
		}
		if (reqVO.getOldpwd().length() > 64) {
			return "原登录密码超长";
		}
		if (StringUtil.isEmpty(reqVO.getNewpwd())) {
			return "新登录密码为空";
		}
		if (reqVO.getNewpwd().length() > 64) {
			return "新登录密码超长";
		}
		
		if (reqVO.getMobile().length() > 16) {
			return "手机号超长";
		}
		String checkmobile = reqVO.getMobile();
		if(StringUtil.isNEmpty(checkmobile) 
				&& !Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(checkmobile).matches()) {
			return "手机号格式错误";
		}
		if (StringUtil.isNEmpty(reqVO.getMobile())) {
			MrsLoginUserDto loginUserDto = mrsLoginUserService.findByMobile(reqVO.getMobile());
			MrsLoginUserDto userInfo = mrsLoginUserService.findById(reqVO.getLoginUserId());
			if (userInfo!=null && !userInfo.getMobile().equals(reqVO.getMobile())) {
				if (loginUserDto != null) {
					return "手机号存在";
				}
			}
		}
		//根据Token查询一户通信息
		 List<MrsAccountDto>  mrsAccountDtoList = mrsAccountService.findByLoginUserId(reqVO.getLoginUserId());
		if (mrsAccountDtoList != null && mrsAccountDtoList.size() > 0) {
			MrsAccountDto mrsAccountDto = mrsAccountDtoList.get(0);
			if (mrsAccountDto != null && MrsPlatformCode.ACCOUNT.getValue().equals(mrsAccountDto.getPlatformCode())) {
				return "只能修改被动开户的数据，不能处理账户系统增加的用户";
			}
		}
		
		return null;
	}
	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkBankauthParams(BankauthRequestVO reqVO) {
		if (reqVO == null) {
			return "BankauthRequestVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getLoginUserId())) {
			return "登录用户主键Key为空";
		}
		if (reqVO.getLoginUserId().length() > 36) {
			return "登录用户主键Key超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "一户通编号为空";
		}
		if (reqVO.getCustId().length() > 32) {
			return "一户通编号超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustomerName())) {
			return "姓名为空";
		}
		if (reqVO.getCustomerName().length() > 32) {
			return "姓名超长";
		}
		if (StringUtil.isEmpty(reqVO.getCredentialsType())) {
			return "证件类型为空";
		}
		if (reqVO.getCredentialsType().length() > 2) {
			return "证件类型超长";
		}
		if (StringUtil.isEmpty(reqVO.getCredentialsNumber())) {
			return "证件号码为空";
		}
		if (reqVO.getCredentialsNumber().length() > 32) {
			return "证件号码超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustType())) {
			return "用户类型为空";
		}
		if (reqVO.getCustType().length() > 1) {
			return "用户类型超长";
		}
		if (StringUtil.isEmpty(reqVO.getPayType())) {
			return "绑卡类型为空";
		}
		if (reqVO.getPayType().length() > 2) {
			return "绑卡类型超长";
		}
		if (StringUtil.isEmpty(reqVO.getBankType())) {
			return "银行类型为空";
		}
		if (reqVO.getBankType().length() > 3) {
			return "银行类型超长";
		}
		if (StringUtil.isEmpty(reqVO.getOpenBankName())) {
			return "开户地名称为空";
		}
		if (reqVO.getOpenBankName().length() > 256) {
			return "开户地名称超长";
		}
		if (StringUtil.isEmpty(reqVO.getBankCode())) {
			return "银行行号为空";
		}
		if (reqVO.getBankCode().length() > 14) {
			return "银行行号超长";
		}
		if (StringUtil.isEmpty(reqVO.getAccNo())) {
			return "账号为空";
		}
		if (reqVO.getAccNo().length() > 64) {
			return "账号超长";
		}
		if (reqVO.getBankMoblePwd().length() > 100) {
			return "银行电话密码超长";
		}
		if (StringUtil.isEmpty(reqVO.getProvince())) {
			return "省名称为空";
		}
		if (reqVO.getProvince().length() > 64) {
			return "省名称超长";
		}
		if (StringUtil.isEmpty(reqVO.getCity())) {
			return "城市名称为空";
		}
		if (reqVO.getCity().length() > 64) {
			return "城市名称超长";
		}
		if (StringUtil.isEmpty(reqVO.getRegion())) {
			return "地区名称为空";
		}
		if (reqVO.getRegion().length() > 64) {
			return "地区名称超长";
		}

		return null;
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private QuickRechageRequestVO toQuickRechageRequestVO(String params) throws Exception {
		QuickRechageRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, QuickRechageRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private WithdrawRequestVO toWithdrawRequestVO(String params) throws Exception {
		WithdrawRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, WithdrawRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private CheckEmailRequestVO toCheckEmailRequestVO(String params) throws Exception {
		CheckEmailRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, CheckEmailRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private AccountAmtRequestVO toAccountAmtRequestVO(String params) throws Exception {
		AccountAmtRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, AccountAmtRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private SendEmailRequestVO toSendEmailRequestVO(String params) throws Exception {
		SendEmailRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, SendEmailRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private LoginRequestVO toLoginRequestVO(String params) throws Exception {
		LoginRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, LoginRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private PersonAuditRequestVO toPersonAuditRequestVO(String params) throws Exception {
		PersonAuditRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, PersonAuditRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private SendsmsRequestVO toSendsmsRequestVO(String params) throws Exception {
		SendsmsRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, SendsmsRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private BankauthRequestVO toCheckBankauthRequestVO(String params) throws Exception {
		BankauthRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, BankauthRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private PwdresetRequestVO toPwdresetRequestVO(String params) throws Exception {
		PwdresetRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, PwdresetRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}
	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private SetPwdRequestVO toSetPwdRequestVO(String params) throws Exception {
		SetPwdRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, SetPwdRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}
	/**
	 * 将请求Gson转换为对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private AccountInfoRequestVO toAccountInfoRequestVO(String params) throws Exception {
		AccountInfoRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, AccountInfoRequestVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(MrsReturnStatus.RETURN_STATUS_04.getValue(), "JSON转换失败");
		}
	}

	private Map<String, String> assebMapParam(QuickRechageRequestVO reqVO) {
		Map<String, String> rechargeParam = new HashMap<String, String>();
		String rechargeAmt = reqVO.getRechargeAmt();
		String actBusiType = reqVO.getActBusiType();
		Long pageAmt = AmountUtil.yuanToFen(rechargeAmt);
		rechargeParam.put("rechargeAmt", pageAmt + "");
		rechargeParam.put("allowChannel", "09");
		rechargeParam.put("accountId", reqVO.getAccountId());
		rechargeParam.put("custId", reqVO.getCustId());
		rechargeParam.put("custName", reqVO.getCustomerName());
		rechargeParam.put("actBusiType", actBusiType); // 充值业务类型
		return MapUtil.sortMapByKey(rechargeParam); // key值进行排序
	}

	/**
	 * 创建接口使用的MrsLoginUserDto
	 * 
	 * @param userId
	 * @param password
	 * @return
	 */
	private MrsLoginUserDto getServiceUserDto(String userId, String password) {
		MrsLoginUserDto dto = new MrsLoginUserDto();
		if (userId != null && !userId.equals("")) {
			// 一卡通账户，可能是以下几种
			dto.setAlias(userId);
			dto.setEmail(userId);
			dto.setMobile(userId);
			dto.setCustId(userId);
		}
		if (password != null && !password.equals("")) {
			dto.setLoginPwd(password);
		}

		return dto;
	}

	/**
	 * 校验登陆密码
	 */
	@Override
	public EditLoginPwdRespVO checkLoginPwd(String params) {
		EditLoginPwdReqVO reqVO = new EditLoginPwdReqVO();
		EditLoginPwdRespVO respVO = new EditLoginPwdRespVO();
		
		try {
			log.info("--------进入修改登录密码方法：receive:" + params);
			log.debug("-----------第一步：转换EditLoginPwdReqVO对象");
			reqVO = toEditLoginPwdRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForCheckLoginPwd(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}
			
			MrsLoginUserDto dto = mrsLoginUserService.findById(reqVO.getId());
			String respMsg = "校验登陆密码成功";
			
			if(null == dto){
				respMsg = "用户不存在！";
				log.error("用户不存在[id = " + reqVO.getId() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			
			if (!MD5Utils.MD5(reqVO.getLoginPwd() + SHIEConfigConstant.SALT)
					.equals(dto.getLoginPwd())) {
				respMsg = "登陆密码输入错误！";
				log.error("登陆密码输入错误[loginPwd = " + reqVO.getLoginPwd() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("登陆密码输入正确,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("ID：%s,登陆密码：%s,校验登录密码失败: %s", reqVO.getId(), reqVO.getLoginPwd(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("修改登录密码失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("修改登录密码失败");
			saveErrorExcetpionLog(String.format("ID：%s,登陆密码：%s,校验登录密码失败: %s", reqVO.getId(), reqVO.getLoginPwd(), e.getMessage()));
			return respVO;
		}
	}
	
	/**
	 * 修改登录密码
	 */
	@Override
	public EditLoginPwdRespVO editLoginPwd(String params) {
		EditLoginPwdReqVO reqVO = new EditLoginPwdReqVO();
		EditLoginPwdRespVO respVO = new EditLoginPwdRespVO();

		try {
			log.info("--------进入修改登录密码方法：receive:" + params);
			log.debug("-----------第一步：转换EditLoginPwdReqVO对象");
			reqVO = toEditLoginPwdRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForEditLoginPwd(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			String respMsg = "修改登录密码成功！";
			MrsLoginUserDto loginUserDto = mrsLoginUserService.findById(reqVO.getId());
			if(null == loginUserDto){
				respMsg = "用户不存在！";
				log.error("用户不存在[id = " + reqVO.getId() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			if (!MD5Utils.MD5(reqVO.getOldPwd() + SHIEConfigConstant.SALT).equals(loginUserDto.getLoginPwd())) {
				respMsg = "原登录密码输入错误！";
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			
			UserCheckVO updateLoginPwd = mrsLoginUserService.updateLoginPwd(reqVO.getId(), reqVO.getOldPwd(),
					reqVO.getNewPwd());

			if (!updateLoginPwd.isCheckValue()) {
				respMsg = updateLoginPwd.getMsg();
				log.error("修改登录密码失败：" + respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("修改登录密码成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("ID：%s,旧密码：%s,新密码：%s,修改登录密码失败:%s", reqVO.getId(), reqVO.getOldPwd(),
					reqVO.getNewPwd(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("修改登录密码失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("修改登录密码失败");
			saveErrorExcetpionLog(String.format("ID：%s,旧密码：%s,新密码：%s,修改登录密码失败:%s", reqVO.getId(), reqVO.getOldPwd(),
					reqVO.getNewPwd(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 初始化支付密码
	 */
	@Override
	public InitPayPwdRespVO initPayPwd(String params) {
		InitPayPwdReqVO reqVO = new InitPayPwdReqVO();
		InitPayPwdRespVO respVO = new InitPayPwdRespVO();

		try {
			log.info("--------进入初始化支付密码方法：receive:" + params);
			log.debug("-----------第一步：转换InitPayPwdReqVO对象");
			reqVO = toInitPayPwdRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForInitPayPwd(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}
			MrsLoginUserDto loginUserDto = mrsLoginUserService.findById(reqVO.getId());
			
			MrsLoginUserDto selectLoginUserByCustId = mrsLoginUserService.selectLoginUserByCustId(reqVO.getCustId());
			/*if (!MD5Utils.MD5(reqVO.getLoginPwd() + SHIEConfigConstant.SALT).equals(loginUserDto.getLoginPwd())) {
				String respMsg = "登录密码输入错误！";
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}*/

			String respMsg = "初始化/重置支付密码成功";
			if(null == loginUserDto){
				respMsg = "用户不存在！";
				log.error("用户不存在[id = " + reqVO.getId() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			if(null == selectLoginUserByCustId){
				respMsg = "用户与一户通未绑定！";
				log.error("用户与一户通未绑定[custId = " + reqVO.getCustId() + "][id="+reqVO.getId()+"]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			// 获取短信有效期
			/*
			 * String smsPeriod = ""; try { smsPeriod =
			 * bisSysParamAppService.getValue(SHIEConfigConstant.SMS_PERIOD); }
			 * catch (CbsCheckedException e) { loggger.error("获取短信有效期失败:" +
			 * e.getMessage()); throw new PortalException("短信验证失败"); } int
			 * inDate = Integer.valueOf(smsPeriod); if
			 * (!SmsHandleController.checkSmS(request, vo.getSmscode(), inDate))
			 * { SmsHandleController.smsInvalid(request); return
			 * accountResetIndex(request); }
			 */

			MrsUserPayPasswordDto passwordDto = new MrsUserPayPasswordDto();
			passwordDto.setCustId(reqVO.getCustId());
			passwordDto.setPassword(MD5Utils.MD5(reqVO.getPayPwd() + SHIEConfigConstant.SALT));
			
			//判断无支付密码则初始化，有则重置
			RespCheckVO respCheckVO = new RespCheckVO(false, null);
			MrsUserPayPasswordDto findByCustId = mrsUserPayPasswordService.findByCustId(reqVO.getCustId());
			if(null == findByCustId){
				respCheckVO = mrsUserPayPasswordService.startPwd(reqVO.getIp(), reqVO.getId(), passwordDto);
			}else{
				respCheckVO = mrsUserPayPasswordService.resetPwd(reqVO.getIp(), reqVO.getId(), passwordDto);
			}
			
			if (!respCheckVO.isCheckValue()) {
				respMsg = "初始化密码失败，原因：" + respCheckVO.getErrorMsg().getValue();
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("初始化支付密码成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("ID：%s,IP：%s,CustId：%s,支付密码：%s,修改登录密码失败:%s", reqVO.getId(),
					reqVO.getIp(), reqVO.getCustId(), reqVO.getPayPwd(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("初始化支付密码失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("初始化支付密码失败");
			saveErrorExcetpionLog(String.format("ID：%s,IP：%s,CustId：%s,支付密码：%s,修改登录密码失败:%s", reqVO.getId(),
					reqVO.getIp(), reqVO.getCustId(), reqVO.getPayPwd(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查支付密码
	 */
	@Override
	public CheckPayPwdRespVO checkPayPwd(String params) {
		CheckPayPwdReqVO reqVO = new CheckPayPwdReqVO();
		CheckPayPwdRespVO respVO = new CheckPayPwdRespVO();

		try {
			log.info("--------进入检查支付密码方法：receive:" + params);
			log.debug("-----------第一步：转换CheckPayPwdReqVO对象");
			reqVO = toCheckPayPwdRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForCheckPayPwd(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			MrsUserPayPasswordDto mrsUserPayPasswordDto = mrsUserPayPasswordService.findByCustId(reqVO.getCustId());
			String respMsg = "支付密码输入正确！";
			if (!MD5Utils.MD5(reqVO.getPayPwd() + SHIEConfigConstant.SALT)
					.equals(mrsUserPayPasswordDto.getPassword())) {
				respMsg = "支付密码输入错误！";
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("支付密码输入正确,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("custId：%s,支付密码：%s,支付密码输入错误:%s", reqVO.getCustId(), reqVO.getPayPwd(),
					e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("支付密码输入错误：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("支付密码输入错误");
			saveErrorExcetpionLog(String.format("custId：%s,支付密码：%s,支付密码输入错误:%s", reqVO.getCustId(), reqVO.getPayPwd(),
					e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 修改支付密码
	 */
	@Override
	public UpdatePayPwdRespVO updatePayPwd(String params) {
		UpdatePayPwdReqVO reqVO = new UpdatePayPwdReqVO();
		UpdatePayPwdRespVO respVO = new UpdatePayPwdRespVO();

		try {
			log.info("--------进入修改支付密码方法：receive:" + params);
			log.debug("-----------第一步：转换UpdatePayPwdReqVO对象");
			reqVO = toUpdatePayPwdRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForUpdatePayPwd(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}
			
			// 调检查支付密码接口检查原支付密码是否正确
			MrsUserPayPasswordDto mrsUserPayPasswordDto = mrsUserPayPasswordService.findByCustId(reqVO.getCustId());
			if (!MD5Utils.MD5(reqVO.getOldPayPwd() + SHIEConfigConstant.SALT)
					.equals(mrsUserPayPasswordDto.getPassword())) {
				String respMsg = "原支付密码输入错误！";
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}

			EditPayPwdVo vo = new EditPayPwdVo();
			vo.setOldPayPwd(reqVO.getOldPayPwd());
			vo.setNewPayPwd(reqVO.getNewPayPwd());
			vo.setReNewPayPwd(reqVO.getReNewPayPwd());

			RespCheckVO respCheckVO = mrsUserPayPasswordService.editPayPwd(reqVO.getId(), reqVO.getIp(),
					reqVO.getCustId(), vo);
			String respMsg = "修改支付密码成功";

			if (!respCheckVO.isCheckValue()) {
				respMsg = "修改支付密码失败，原因：" + respCheckVO.getErrorMsg().getValue();
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("修改支付密码成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("id：%s,custId：%s,ip：%s,修改支付密码失败:%s", reqVO.getId(), reqVO.getCustId(),
					reqVO.getIp(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("修改支付密码失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("修改支付密码失败");
			saveErrorExcetpionLog(String.format("id：%s,custId：%s,ip：%s,修改支付密码失败:%s", reqVO.getId(), reqVO.getCustId(),
					reqVO.getIp(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 转换成EditLoginPwdReqVO 修改登录密码
	 */
	private EditLoginPwdReqVO toEditLoginPwdRestReqVO(String params) throws Exception {
		EditLoginPwdReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, EditLoginPwdReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 转换成InitPayPwdReqVO 初始化支付密码
	 */
	private InitPayPwdReqVO toInitPayPwdRestReqVO(String params) throws Exception {
		InitPayPwdReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, InitPayPwdReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 转换成CheckPayPwdReqVO 检查支付密码
	 */
	private CheckPayPwdReqVO toCheckPayPwdRestReqVO(String params) throws Exception {
		CheckPayPwdReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, CheckPayPwdReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 转换成UpdatePayPwdReqVO 修改支付密码
	 */
	private UpdatePayPwdReqVO toUpdatePayPwdRestReqVO(String params) throws Exception {
		UpdatePayPwdReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, UpdatePayPwdReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}
	
	/**
	 * 校验登录密码校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForCheckLoginPwd(EditLoginPwdReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getId()) || reqVO.getId().length() > 36) {
			return "ID为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getLoginPwd()) || reqVO.getLoginPwd().length() > 36) {
			return "登陆密码为空或超长";
		}
		return null;
	}
	
	/**
	 * 修改登录密码校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForEditLoginPwd(EditLoginPwdReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getId()) || reqVO.getId().length() > 36) {
			return "ID为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getOldPwd()) || reqVO.getOldPwd().length() > 36) {
			return "旧密码为空或超长";
		}
		if (StringUtil.isEmpty(reqVO.getNewPwd()) || reqVO.getNewPwd().length() > 36) {
			return "新密码为空或超长";
		}
		return null;
	}

	/**
	 * 初始化支付密码校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForInitPayPwd(InitPayPwdReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getId()) || reqVO.getId().length() > 36) {
			return "id为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getIp()) || reqVO.getIp().length() > 36) {
			return "ip为空或超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 16) {
			return "custId为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getPayPwd()) || reqVO.getPayPwd().length() > 36) {
			return "支付密码为空或超长";
		}
		/*if (StringUtil.isEmpty(reqVO.getRePayPwd())) {
			return "重复支付密码为空";
		}
		if (!reqVO.getPayPwd().equals(reqVO.getRePayPwd())) {
			return "两次密码输入不相同";
		}*/

		return null;
	}

	/**
	 * 检查支付密码校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForCheckPayPwd(CheckPayPwdReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 16) {
			return "custId为空或字段超长";
		}
		if(StringUtil.isNEmpty(reqVO.getCustId()) 
				&& !Pattern.compile(RegexEnum.REGEX_CH_EH_NUM_80.getRegexValue()).matcher(reqVO.getCustId()).matches()) {
			return "一户通账号格式错误";
		}
		if (StringUtil.isEmpty(reqVO.getPayPwd()) || reqVO.getPayPwd().length() > 36) {
			return "支付密码为空或超长";
		}
		return null;
	}

	/**
	 * 修改支付密码校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForUpdatePayPwd(UpdatePayPwdReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getId()) || reqVO.getId().length() > 36) {
			return "id为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 16) {
			return "custId为空或字段超长";
		}
		if(StringUtil.isNEmpty(reqVO.getCustId()) 
				&& !Pattern.compile(RegexEnum.REGEX_CH_EH_NUM_80.getRegexValue()).matcher(reqVO.getCustId()).matches()) {
			return "一户通账号格式错误";
		}
		if (StringUtil.isEmpty(reqVO.getOldPayPwd())) {
			return "旧支付密码为空";
		}
		if (StringUtil.isEmpty(reqVO.getNewPayPwd())) {
			return "新支付密码为空";
		}
		if (StringUtil.isEmpty(reqVO.getReNewPayPwd())) {
			return "重复新支付密码为空";
		}
		if (reqVO.getOldPayPwd().equals(reqVO.getNewPayPwd())) {
			return "新密码和原密码不能相同";
		}
		if (!reqVO.getReNewPayPwd().equals(reqVO.getNewPayPwd())) {
			return "两次密码输入不相同";
		}
		
		return null;
	}

	/**
	 * 获取用户Ip
	 * 
	 * @param request
	 * @return
	 */
	protected String getLocalIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	/**
	 * 修改昵称
	 */
	@Override
	public AccountBaseRespVO aliasEdit(String params) {
		AccountBaseReqVO reqVO = new AccountBaseReqVO();
		AccountBaseRespVO respVO = new AccountBaseRespVO();

		try {
			log.info("--------进入修改昵称方法：receive:" + params);
			log.debug("-----------第一步：转换AccountBaseReqVO对象");
			reqVO = toAccountBaseRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForAliasEdit(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			if (aliasIsExist(reqVO.getAlias()) == "false") {
				log.error("昵称已被占用");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo("昵称已被占用");
				return respVO;
			}

			mrsLoginUserService.updateAlias(reqVO.getId(), reqVO.getAlias());
			String respMsg = "修改昵称成功！";

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询列表成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("ID：%s,修改昵称失败:%s", reqVO.getId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询列表失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询列表失败");
			saveErrorExcetpionLog(String.format("ID：%s,修改昵称失败:%s", reqVO.getId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查手机号
	 */
	@Override
	public AccountBaseRespVO checkMobile(String params) {
		AccountBaseReqVO reqVO = new AccountBaseReqVO();
		AccountBaseRespVO respVO = new AccountBaseRespVO();
		
		try {
			log.info("--------进入手机检查方法：receive:" + params);
			log.debug("-----------第一步：转换AccountBaseReqVO对象");
			reqVO = toAccountBaseRestReqVO(params);
			
			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/
			
			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForCheckMobile(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}
			
			String respMsg = "手机号不存在！"; 
			MrsLoginUserDto loginUserDto = mrsLoginUserService.findByMobile(reqVO.getMobile());
			
			if(null != loginUserDto){
				respMsg = "手机号存在！";
				log.error("手机号存在[id = " + reqVO.getId() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("手机号校验成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("手机号：%s,手机号检查失败:%s", reqVO.getMobile(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("手机号检查失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("手机号检查失败");
			saveErrorExcetpionLog(String.format("手机号：%s,手机号检查失败:%s", reqVO.getMobile(), e.getMessage()));
			return respVO;
		}
	}
	
	/**
	 * 手机设置
	 */
	@Override
	public AccountBaseRespVO addMobile(String params) {
		AccountBaseReqVO reqVO = new AccountBaseReqVO();
		AccountBaseRespVO respVO = new AccountBaseRespVO();

		try {
			log.info("--------进入手机设置方法：receive:" + params);
			log.debug("-----------第一步：转换AccountBaseReqVO对象");
			reqVO = toAccountBaseRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForAddMobile(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			String respMsg = "设置手机号成功！";
			MrsLoginUserDto loginUserDto = mrsLoginUserService.findById(reqVO.getId());
			/*if (!MD5Utils.MD5(reqVO.getLoginPwd() + SHIEConfigConstant.SALT).equals(loginUserDto.getLoginPwd())) {
				respMsg = "登录密码输入错误！";
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}*/
			if(null == loginUserDto){
				respMsg = "用户不存在！";
				log.error("用户不存在[id = " + reqVO.getId() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}

			MrsLoginUserDto dto = mrsLoginUserService.findByMobile(reqVO.getMobile());
			if (dto != null) {
				respMsg = "手机号已存在！";
				log.error("手机已经存在[mobile = " + reqVO.getMobile() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}

			loginUserDto.setMobile(reqVO.getMobile());
			mrsLoginUserService.updateDto(loginUserDto);

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("手机设置成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(
					String.format("id：%s,手机号：%s,手机设置失败:%s", reqVO.getId(), reqVO.getMobile(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("手机设置失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("手机设置失败");
			saveErrorExcetpionLog(
					String.format("id：%s,手机号：%s,手机设置失败:%s", reqVO.getId(), reqVO.getMobile(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 邮箱设置
	 */
	@Override
	public AccountBaseRespVO addEmail(String params) {
		AccountBaseReqVO reqVO = new AccountBaseReqVO();
		AccountBaseRespVO respVO = new AccountBaseRespVO();

		try {
			log.info("--------进入邮箱设置方法：receive:" + params);
			log.debug("-----------第一步：转换AccountBaseReqVO对象");
			reqVO = toAccountBaseRestReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForAddEmail(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			String respMsg = "设置邮箱成功！";
			MrsLoginUserDto loginUserDto = mrsLoginUserService.findById(reqVO.getId());
			if (!MD5Utils.MD5(reqVO.getLoginPwd() + SHIEConfigConstant.SALT).equals(loginUserDto.getLoginPwd())) {
				respMsg = "登录密码输入错误！";
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}

			String customerName = "";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(SHIEConfigConstant.CUSTMER_NAME, customerName);
			UserCheckVO checkVo = bisEmailService.shieSendEmail(reqVO.getEmail(), reqVO.getId(), EBisSmsSystem.PORTAL,
					EBisEmailTemplateCode.SET_EMAIL, param, null);
			if (!checkVo.isCheckValue()) {
				respMsg = "邮件发送失败:" + checkVo.getMsg();
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("邮箱设置成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(
					String.format("id：%s,邮箱：%s,邮箱设置失败:%s", reqVO.getId(), reqVO.getEmail(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("邮箱设置成失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("邮箱设置失败");
			saveErrorExcetpionLog(
					String.format("id：%s,邮箱：%s,邮箱设置失败:%s", reqVO.getId(), reqVO.getEmail(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 转换成AccountBaseReqVO
	 */
	private AccountBaseReqVO toAccountBaseRestReqVO(String params) throws Exception {
		AccountBaseReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, AccountBaseReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 修改昵称校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForAliasEdit(AccountBaseReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getId()) || reqVO.getId().length() > 36) {
			return "参数id为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getAlias()) || reqVO.getAlias().length() > 32) {
			return "参数昵称为空或超长";
		}
		return null;
	}
	
	/**
	 * 检查手机号校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForCheckMobile(AccountBaseReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getMobile()) || reqVO.getMobile().length() > 11) {
			return "手机号为空或字段超长";
		}
		if(!Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(reqVO.getMobile()).matches()){
			return "手机号格式不正确";
		}
		return null;
	}
	
	/**
	 * 设置手机校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForAddMobile(AccountBaseReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getId()) || reqVO.getId().length() > 36) {
			return "用户Id为空或字段超长";
		}
		/*if (StringUtil.isEmpty(reqVO.getLoginPwd()) || reqVO.getLoginPwd().length() > 64) {
			return "登录密码为空或超长";
		}*/
		if (StringUtil.isEmpty(reqVO.getMobile()) || reqVO.getMobile().length() > 11) {
			return "手机号为空或字段超长";
		}
		if(!Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(reqVO.getMobile()).matches()){
			return "手机号格式不正确";
		}
		return null;
	}

	/**
	 * 设置邮箱校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForAddEmail(AccountBaseReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getId()) || reqVO.getId().length() > 36) {
			return "用户Id为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getLoginPwd()) || reqVO.getLoginPwd().length() > 64) {
			return "登录密码为空或超长";
		}
		if (StringUtil.isEmpty(reqVO.getEmail()) || reqVO.getEmail().length() > 64) {
			return "邮箱为空或超长";
		}
		return null;
	}

	/**
	 * 检查昵称是否存在
	 */
	public String aliasIsExist(String alias) {
		UserCheckVO usableAlias = mrsLoginUserAppService.isUsableAlias(alias);
		return String.valueOf(usableAlias.isCheckValue());
	}

	/**
	 * 收支明细列表
	 */
	@Override
	public ActBillRespListVO actbillQueryAllData(String params) {
		ActBillReqVO reqVO = new ActBillReqVO();
		ActBillRespListVO respVO = new ActBillRespListVO();

		try {
			log.info("--------进入收支明细列表方法：receive:" + params);
			log.debug("-----------第一步：转换ActBillReqVO对象");
			reqVO = toActBillReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForActbillQueryAllData(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			log.debug("-----------第三步，业务处理：");
			PageData<ActBillDto> pageData = new PageData<ActBillDto>();
			ActBillDto actBillDto = new ActBillDto();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

			try {
				actBillDto.setCustId(reqVO.getCustId());
				actBillDto.setAccountType(SubAcctType.BASE.getValue());// 个人仅限基础类
				if (StringUtil.isNEmpty(reqVO.getDrCrFlag())) {
					actBillDto.setDrCrFlag(EAccountDrCr.getEnum(reqVO.getDrCrFlag()));
					actBillDto.setDrCrFlagStr(reqVO.getDrCrFlag());
				}
				if (StringUtil.isNEmpty(reqVO.getBusiType())) {
					actBillDto.setBusiType(EBusiType.getEnum(reqVO.getBusiType()));
				}
				if (StringUtil.isNEmpty(reqVO.getStartTime())) {
					actBillDto.setStartTime(sdf.parse(reqVO.getStartTime()));
				}
				if (StringUtil.isNEmpty(reqVO.getEndTime())) {
					actBillDto.setEndTime(sdf.parse(reqVO.getEndTime()));
				}
				actBillDto.setStartAmt(StringUtil.isNEmpty(reqVO.getStartAmt()) ? reqVO.getStartAmt() : "");
				actBillDto.setEndAmt(StringUtil.isNEmpty(reqVO.getEndAmt()) ? reqVO.getEndAmt() : "");

				int pageNo = 1;
				int pageSize = 10;
				try {
					pageNo = Integer.valueOf(reqVO.getPageNo());
					pageSize = Integer.valueOf(reqVO.getPageSize());
				} catch (Exception e) {
					log.error("页数页码输入错误", e);
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo("页数页码请输入数字");
					return respVO;
				}
				pageData.setPageNumber(pageNo);
				if (null == reqVO.getPageSize() || pageSize > 10 || pageSize < 1) {
					pageData.setPageSize(10);
				} else {
					pageData.setPageSize(pageSize);
				}
				// 查询数据
				pageData = actBillService.queryAllDataByPortal(pageData, actBillDto, actBillDto.getAccountType());
			} catch (Exception e) {
				log.error("收支明细查询失败！一户通号：" + reqVO.getCustId(), e);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo("查询失败");
				return respVO;
			}

			respVO.setReturnNum(0);

			// 返回值处理
			List<ActBillRespEleVO> deatils = new ArrayList<ActBillRespEleVO>();
			ActBillRespEleVO detail = new ActBillRespEleVO();
			SimpleDateFormat sdfal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<ActBillDto> actBillList = pageData.getRows();
			if (null != actBillList && actBillList.size() > 0) {
				respVO.setReturnNum(actBillList.size());

				for (ActBillDto dto : actBillList) {
					detail.setCreateTime(sdfal.format(dto.getCreateTime()));
					detail.setAccountType(SubAcctType.BASE.getValue());
					BeanUtils.copyProperties(dto, detail);
					deatils.add(detail);
				}
			}

			respVO.setList(deatils);

			// 是否存在下一页
			if (pageData.getTotal() <= pageData.getPageSize()
					|| pageData.getPageNumber() * pageData.getPageSize() >= pageData.getTotal()) {
				respVO.setHasNextPage("N");
			} else {
				respVO.setHasNextPage("Y");
			}

			String respMsg = "查询列表成功！";

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询详情成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("custId：%s,查询列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询列表失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询列表失败");
			saveErrorExcetpionLog(String.format("custId：%s,查询列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 收支明细详情
	 */
	@Override
	public ActBillRespVO actBillDetail(String params) {
		ActBillReqVO reqVO = new ActBillReqVO();
		ActBillRespVO respVO = new ActBillRespVO();

		try {
			log.info("--------进入收支明细详情方法：receive:" + params);
			log.debug("-----------第一步：转换ActBillReqVO对象");
			reqVO = toActBillReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForActBillDetail(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			ActBillDto actBillDto = actBillService.selectByBillId(reqVO.getBillId());
			ActAccountDto actAccountDto = actAccountService.selectByAccountId(actBillDto.getAccountId());
			String respMsg = "查询详情成功！";

			BeanUtils.copyProperties(actBillDto, respVO);
			respVO.setAccountDrCrFlag(actAccountDto.getDrCrFlag());

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询详情成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("billId：%s,查询详情失败:%s", reqVO.getBillId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询详情失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询详情失败");
			saveErrorExcetpionLog(String.format("billId：%s,查询详情失败:%s", reqVO.getBillId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForActBillDetail(ActBillReqVO reqVO) {
		if (reqVO == null) {
			return "ActBillReqVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getBillId())) {
			return "billId为空";
		}
		if (reqVO.getBillId().length() > 20) {
			return "billId超长";
		}

		return null;
	}

	/**
	 * 转换成ActBillReqVO
	 */
	private ActBillReqVO toActBillReqVO(String params) throws Exception {
		ActBillReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, ActBillReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 收支明细列表校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForActbillQueryAllData(ActBillReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 16) {
			return "custId为空或字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getDrCrFlag()) && EAccountInOut.getEnum(reqVO.getDrCrFlag()) == null) {
			return "收支类型不存在";
		}
		if (StringUtil.isNEmpty(reqVO.getBusiType()) && EBusiType.getEnum(reqVO.getBusiType()) == null) {
			return "交易类型不存在";
		}
		if (StringUtil.isNEmpty(reqVO.getStartTime()) && reqVO.getStartTime().length() != 8) {
			return "查询起始日期格式YYYYMMDD";
		}
		if (StringUtil.isNEmpty(reqVO.getEndTime()) && reqVO.getEndTime().length() != 8) {
			return "查询结束日期格式YYYYMMDD";
		}
		/* 金额校验 */
		String reg = "^[-\\+]?[\\d]*$";
		if (StringUtil.isNEmpty(reqVO.getStartAmt()) && reqVO.getStartAmt().matches(reg)) {
			return "查询起始金额(单位:分)必须为整数";
		}
		if (StringUtil.isNEmpty(reqVO.getEndAmt()) && reqVO.getEndAmt().matches(reg)) {
			return "查询结束金额(单位:分)必须为整数";
		}
		int pageNo = -1;
		try {
			pageNo = Integer.valueOf(reqVO.getPageNo());
		} catch (NumberFormatException e) {
		}
		if (pageNo < 1) {
			return "查询页号必填初始可为1";
		}

		return null;
	}

	/**
	 * 生成快捷绑卡认证码接口
	 */
	@Override
	public GetCodeRespVO getCode(String params) {
		BankAddReqVO reqVO = new BankAddReqVO();
		GetCodeRespVO respVO = new GetCodeRespVO();

		try {
			log.info("--------进入生成快捷绑卡认证码方法：receive:" + params);
			log.debug("-----------第一步：转换BankAddReqVO对象");
			reqVO = toBankAddReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForGetCode(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			SpdbDynNmReqPojo fastSms = new SpdbDynNmReqPojo();
			fastSms.setAcctNo(reqVO.getAccNo());// 银行卡号
			fastSms.setTranAmt("1");// 金额
			fastSms.setIdNo(reqVO.getIdNo());// 证件号
			fastSms.setMobileNo(reqVO.getMobile());// 手机号
			fastSms.setPayCardName(reqVO.getCustName());// 客户号
			fastSms.setChannelCode(EChlChannelCode.BANKTYPE_BOC_FAST);
			BaseRespPojo respPojo = smsManageAppService.SendSms(fastSms);

			if (!respPojo.getRetrunCode().equals(EChlReturnCode.SUCCESS)) {
				respVO.setReturnCode(respPojo.getRetrunCode().toString());
				respVO.setReturnMsg(respPojo.getReturnMsg());
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respPojo.getReturnMsg());
				return respVO;
			}

			DynNumRespPojo pojo = (DynNumRespPojo) respPojo;
			respVO.setUniqueCode(pojo.getToken());

			// 返回
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("生成快捷绑卡认证码接口,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("银行卡号：%s,生成快捷绑卡认证码失败：%s", reqVO.getAccNo(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("生成快捷绑卡认证码失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("生成快捷绑卡认证码失败");
			saveErrorExcetpionLog(String.format("银行卡号：%s,生成快捷绑卡认证码失败：%s", reqVO.getAccNo(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 快捷绑卡
	 */
	@Override
	public BankAddRespVO doQuick(String params) {
		BankAddReqVO reqVO = new BankAddReqVO();
		BankAddRespVO respVO = new BankAddRespVO();

		try {
			log.info("--------进入授信绑卡方法：receive:" + params);
			log.debug("-----------第一步：转换BankAddReqVO对象");
			reqVO = toBankAddReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForDoQuick(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			// 判断银行账户是否存在
			MrsBankBusiDto bankBusiDto = mrsBankBusiDtoService.findByAccnoAndCustidAndPaytype(reqVO.getAccNo(),
					reqVO.getCustId(), BankPayType.BP1.getValue());
			if (null != bankBusiDto) {
				if (BankBindType.SUCCESS.getValue().equals(bankBusiDto.getBindStatus())) {
					log.error("该银行账户已绑定,不能添加");
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo("该银行账户已绑定,不能添加");
					return respVO;
				}
			}
			// 开始处理
			BankAddVo vo = new BankAddVo();
			BeanUtils.copyProperties(reqVO, vo);
			vo.setPhoneNo(reqVO.getMobile());
			RespCheckVO respCheckVO = mrsBankBusiDtoService.addBankkj(vo, EMrsUserType.PERSONL.getValue(),
					reqVO.getCustId());
			String respMsg = "添加银行账户成功";
			if (!respCheckVO.isCheckValue()) {
				if (ErrorMsgEnum.ADD_OUT_LINE_REMIND.equals(respCheckVO.getErrorMsg())) {
					String authNo = respCheckVO.getMsg();
					if (StringUtils.isNotEmpty(authNo) && authNo.length() > 7) {
						authNo = authNo.substring(7);
					}
					respVO.setReturnCode(ErrorMsgEnum.ADD_OUT_LINE_REMIND.getKey());
					respVO.setReturnMsg("已受理成功，请凭资金账号【" + authNo + "】到银行柜台办理银期或银商签约");
				} else if (ErrorMsgEnum.ADD_CARD_FAIL.equals(respCheckVO.getErrorMsg())) {
					respVO.setReturnCode(ErrorMsgEnum.ADD_CARD_FAIL.getKey());
					respVO.setReturnMsg(ErrorMsgEnum.ADD_CARD_FAIL.getValue());
				} else if (ErrorMsgEnum.SUCCESS_NEED_ADUIT.equals(respCheckVO.getErrorMsg())) {
					respVO.setReturnCode(ErrorMsgEnum.SUCCESS_NEED_ADUIT.getKey());
					respVO.setReturnMsg("已银保处理，需审核！");
				} else {
					respMsg = "系统异常,绑卡失败";
					log.error(respMsg);
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				}
			}

			// 返回
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("快捷绑卡完成,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("快捷绑卡失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(
					String.format("一户通编号：%s,银行卡号：%s,快捷绑卡失败：%s,", reqVO.getCustId(), reqVO.getAccNo(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("快捷绑卡失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("快捷绑卡失败");
			saveErrorExcetpionLog(
					String.format("一户通编号：%s,银行卡号：%s,快捷绑卡失败：%s,", reqVO.getCustId(), reqVO.getAccNo(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 快捷解绑
	 */
	@Override
	public BaseRespVO delQuick(String params) {
		BankBusiRestReqVO reqVO = new BankBusiRestReqVO();
		BaseRespVO respVO = new BankBusiRespVO();

		try {
			log.info("--------进入解绑银行账户方法：receive:" + params);
			log.debug("-----------第一步：转换BankBusiRestReqVO对象");
			reqVO = toBankBusiRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsDelQuick(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			String respMsg = "解绑银行账户成功";
			MrsBankBusiDto findById = mrsBankBusiDtoService.findById(reqVO.getId());
			if(null == findById){
				respMsg = "银行账户不存在！";
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			if(null != findById && !reqVO.getCustId().equals(findById.getCustId())){
				respMsg = "银行账户与客户未绑定！";
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			if(null != findById && !findById.getBindStatus().equals(BankBindType.SUCCESS)){
				respMsg = "银行账户为非绑定状态！";
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			
			// 调接口
			BankBusiReqVO bankBusiReqVO = new BankBusiReqVO();
			bankBusiReqVO.setId(reqVO.getId());
			bankBusiReqVO.setCustId(reqVO.getCustId());
			;

			RespCheckVO respCheckVO = mrsBankBusiDtoService.delQuick(bankBusiReqVO);

			if (!respCheckVO.isCheckValue()) {
				if (ErrorMsgEnum.REMOVE_CARD_FAIL.equals(respCheckVO.getErrorMsg())) {
					respMsg = respCheckVO.getMsg();
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				} else if (ErrorMsgEnum.ADD_OUT_LINE_REMIND.equals(respCheckVO.getErrorMsg())) {
					respMsg = "请凭资金账号【" + respCheckVO.getMsg() + "】到银行柜台办理银期或银商解约";
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				} else {
					respMsg = "系统异常";
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				}
			}

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("快捷解绑银行账户成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(
					String.format("一户通号：%s,银行卡号：%s,快捷解绑失败:%s", reqVO.getCustId(), reqVO.getAccNo(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("快捷解绑银行账户失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("快捷解绑银行账户失败");
			saveErrorExcetpionLog(
					String.format("一户通号：%s,银行卡号：%s,快捷解绑失败:%s", reqVO.getCustId(), reqVO.getAccNo(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 授信绑卡
	 */
	@Override
	public BankAddRespVO bankAddsx(String params) {
		BankAddReqVO reqVO = new BankAddReqVO();
		BankAddRespVO respVO = new BankAddRespVO();

		try {
			log.info("--------进入授信绑卡方法：receive:" + params);
			log.debug("-----------第一步：转换BankAddReqVO对象");
			reqVO = toBankAddReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForAddsx(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			// 判断银行账户是否存在
			MrsBankBusiDto bankBusiDto = mrsBankBusiDtoService.findByAccnoAndCustidAndPaytype(reqVO.getAccNo(),
					reqVO.getCustId(), BankPayType.BP2.getValue());
			if (null != bankBusiDto) {
				if (BankBindType.SUCCESS.getValue().equals(bankBusiDto.getBindStatus())) {
					log.error("该银行账户已绑定,不能添加");
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo("该银行账户已绑定,不能添加");
					return respVO;
				}
				if (BankBindType.TODO.getValue().equals(bankBusiDto.getBindStatus())
						&& BankAduitType.WAIT.getValue().equals(bankBusiDto.getAduitStatus())) {
					log.error("该银行账户处于审核中,不能添加");
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo("该银行账户处于审核中,不能添加");
					return respVO;
				}
			}
			BankAddVo vo = new BankAddVo();
			BeanUtils.copyProperties(reqVO, vo);
			vo.setCardType(BankCardType.DEBIT.getValue());
			RespCheckVO respCheckVO = mrsBankBusiDtoService.addBanksx(vo, MrsCustomerType.MCT_0.getValue(),
					reqVO.getCustId(), reqVO.getCustName(), reqVO.getLoginUserId());

			String respMsg = "添加银行账户成功";
			if (!respCheckVO.isCheckValue()) {
				if (ErrorMsgEnum.ADD_OUT_LINE_REMIND.equals(respCheckVO.getErrorMsg())) {
					String authNo = respCheckVO.getMsg();
					if (StringUtils.isNotEmpty(authNo) && authNo.length() > 7) {
						authNo = authNo.substring(7);
					}
					respVO.setReturnCode(ErrorMsgEnum.ADD_OUT_LINE_REMIND.getKey());
					respVO.setReturnMsg("已受理成功，请凭资金账号【" + authNo + "】到银行柜台办理银期或银商签约");
				} else if (ErrorMsgEnum.ADD_CARD_FAIL.equals(respCheckVO.getErrorMsg())) {
					respVO.setReturnCode(ErrorMsgEnum.ADD_CARD_FAIL.getKey());
					respVO.setReturnMsg(ErrorMsgEnum.ADD_CARD_FAIL.getValue());
				} else if (ErrorMsgEnum.SUCCESS_NEED_ADUIT.equals(respCheckVO.getErrorMsg())) {
					respVO.setReturnCode(ErrorMsgEnum.SUCCESS_NEED_ADUIT.getKey());
					respVO.setReturnMsg("已银保处理，需审核！");
				} else {
					respMsg = "系统异常,绑卡失败";
					log.error(respMsg);
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				}
			}

			// 返回
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("授信绑卡完成,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("授信绑卡失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("授信绑卡失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("授信绑卡失败");
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 授信解绑
	 */
	@Override
	public BankBusiRespVO doDelete(String params) {
		BankBusiRestReqVO reqVO = new BankBusiRestReqVO();
		BankBusiRespVO respVO = new BankBusiRespVO();
		try {
			log.info("--------进入解绑银行账户方法：receive:" + params);
			log.debug("-----------第一步：转换BankBusiRestReqVO对象");
			reqVO = toBankBusiRestReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForDoDelete(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}
			
			MrsBankBusiDto mrsBankBusiDto = mrsBankBusiDtoService.findById(reqVO.getId());
			String respMsg = "解绑银行账户成功";
			
			if(null == mrsBankBusiDto){
				respMsg = "用户不存在！";
				log.error("用户不存在[id = " + reqVO.getId() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			if(mrsBankBusiDto.getCustId().equals(reqVO.getCustId())){
				respMsg = "银行卡和用户绑定信息不一致！";
				log.error("银行卡和用户绑定信息不一致[id = " + reqVO.getId() + "],[custId = " + reqVO.getCustId() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}

			RespCheckVO respCheckVO = mrsBankBusiDtoService.delete(reqVO.getId());
			
			if (!respCheckVO.isCheckValue()) {
				if (ErrorMsgEnum.REMOVE_CARD_FAIL.equals(respCheckVO.getErrorMsg())) {
					respMsg = respCheckVO.getMsg();
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				} else if (ErrorMsgEnum.ADD_OUT_LINE_REMIND.equals(respCheckVO.getErrorMsg())) {
					respMsg = "请凭资金账号【" + respCheckVO.getMsg() + "】到银行柜台办理银期或银商解约";
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				} else {
					respMsg = "系统异常";
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				}
			}
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("解绑银行账户成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,解绑失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("解绑银行账户失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("解绑银行账户失败");
			saveErrorExcetpionLog(String.format("一户通号：%s,解绑失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForDoDelete(BankBusiRestReqVO reqVO) {
		if (reqVO == null) {
			return "BankBusiRestReqVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getId())) {
			return "Id为空";
		}
		if (reqVO.getId().length() > 36) {
			return "Id超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "custId为空";
		}
		if (reqVO.getCustId().length() > 36) {
			return "custId超长";
		}

		return null;
	}

	/**
	 * 银行账户列表(现只有个人)
	 */
	@Override
	public BankBusiRespListVO bankCardQueryAllData(String params) {
		BankBusiRestReqVO reqVO = new BankBusiRestReqVO();
		BankBusiRespListVO respVO = new BankBusiRespListVO();

		try {
			log.info("--------进入银行账户列表方法：receive:" + params);
			log.debug("-----------第一步：转换BankBusiRestReqVO对象");
			reqVO = toBankBusiRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForBankCardQueryAllData(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			List<BankBusiRespEleVO> vos = new ArrayList<BankBusiRespEleVO>();
			BankBusiRespEleVO vo = new BankBusiRespEleVO();
			List<MrsBankBusiDto> banks = mrsBankBusiDtoService.findByBindStatusAndCustidAndPaytype(reqVO.getCustId());
			String respMsg = "查询列表成功！";
			
			if(CollectionUtil.isEmpty(banks)){
				respMsg = "查询无银行卡记录！";
				log.error("查询无银行卡记录[custId" + reqVO.getCustId() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			SimpleDateFormat sdfal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (MrsBankBusiDto dto : banks) {
				BeanUtils.copyProperties(dto, vo);
				if(null != dto.getCreateTime()){
					vo.setCreateTime(sdfal.format(dto.getCreateTime()));
				}
				if(null != dto.getModifyTime()){
					vo.setModifyTime(sdfal.format(dto.getModifyTime()));
				}
				vos.add(vo);
			}

			respVO.setList(vos);

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询银行账户列表成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,查询银行账户列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询银行账户列表失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询银行账户列表失败");
			saveErrorExcetpionLog(String.format("一户通号：%s,查询银行账户列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForBankCardQueryAllData(BankBusiRestReqVO reqVO) {
		if (reqVO == null) {
			return "BankBusiRestReqVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "CustId为空";
		}
		if (reqVO.getCustId().length() > 16) {
			return "CustId超长";
		}

		return null;
	}

	/**
	 * 银行账户详情
	 */
	@Override
	public BankBusiRespVO bankCardDetail(String params) {
		BankBusiRestReqVO reqVO = new BankBusiRestReqVO();
		BankBusiRespVO respVO = new BankBusiRespVO();
		try {
			log.info("--------进入银行账户详情方法：receive:" + params);
			log.debug("-----------第一步：转换BankBusiRestReqVO对象");
			reqVO = toBankBusiRestReqVO(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(),reqVO.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForBankCardDetail(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			MrsBankBusiDto dto = mrsBankBusiDtoService.findById(reqVO.getId());
			String respMsg = "查询详情成功！";
			
			if(null == dto){
				respMsg = "查询无记录！";
				log.error("查询无记录[id=" + reqVO.getId() + "]");
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			SimpleDateFormat sdfal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			BeanUtils.copyProperties(dto, respVO);
			
			if(null != dto.getCreateTime()){
				respVO.setCreateTime(sdfal.format(dto.getCreateTime()));
			}
			if(null != dto.getModifyTime()){
				respVO.setModifyTime(sdfal.format(dto.getModifyTime()));
			}
			
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询详情成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(
					String.format("一户通号：%s,银行卡号：%s,查询详情失败:%s", reqVO.getCustId(), reqVO.getAccNo(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询详情失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询详情失败");
			saveErrorExcetpionLog(
					String.format("一户通号：%s,银行卡号：%s,查询详情失败:%s", reqVO.getCustId(), reqVO.getAccNo(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForBankCardDetail(BankBusiRestReqVO reqVO) {
		if (reqVO == null) {
			return "BankBusiRestReqVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getId())) {
			return "Id为空";
		}
		if (reqVO.getId().length() > 36) {
			return "Id超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "CustId为空";
		}
		if (reqVO.getCustId().length() > 16) {
			return "CustId超长";
		}

		return null;
	}

	/**
	 * 删除
	 */
	@Override
	public BankBusiRespVO doDelCard(String params) {
		BankBusiRestReqVO reqVO = new BankBusiRestReqVO();
		BankBusiRespVO respVO = new BankBusiRespVO();
		try {
			log.info("--------进入删除银行账户方法：receive:" + params);
			log.debug("-----------第一步：转换BankBusiRestReqVO对象");
			reqVO = toBankBusiRestReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForDoDelCard(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			RespCheckVO respCheckVO = mrsBankBusiDtoService.delcard(reqVO.getAccNo(), reqVO.getCustId());
			String respMsg = "删除银行账户成功！";
			if (!respCheckVO.isCheckValue()) {
				if (ErrorMsgEnum.REMOVE_CARD_FAIL.equals(respCheckVO.getErrorMsg())) {
					log.info("!");
					respMsg = respCheckVO.getMsg();
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				} else {
					log.info("系统异常");
					respMsg = "系统异常";
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo(respMsg);
					return respVO;
				}
			}
			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("删除银行账户成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(
					String.format("一户通号：%s,银行账户：%s,个人账户变更失败:%s", reqVO.getCustId(), reqVO.getAccNo(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("删除银行账户失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("删除银行账户失败");
			saveErrorExcetpionLog(
					String.format("一户通号：%s,银行账户：%s,个人账户变更失败:%s", reqVO.getCustId(), reqVO.getAccNo(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 设置默认卡
	 */
	@Override
	public BankSetDefaultRespVO bankSetDefault(String params) {
		BankSetDefaultReqVO reqVO = new BankSetDefaultReqVO();
		BankSetDefaultRespVO respVO = new BankSetDefaultRespVO();

		try {
			log.info("--------进入设置默认卡方法：receive:" + params);
			log.debug("-----------第一步：转换BankSetDefaultReqVO对象");
			reqVO = toBankSetDefaultRestReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForBankSetDefault(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			MrsBankBusiDto mrsBankBusiDto = mrsBankBusiDtoService.findById(reqVO.getId());
			if (!mrsBankBusiDto.getCustId().equals(reqVO.getCustId())) {
				log.error("该银行卡:" + reqVO.getId() + ",未绑定当前用户:" + reqVO.getCustId());
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo("该银行卡:" + reqVO.getId() + ",未绑定当前用户:" + reqVO.getCustId());
				return respVO;
			}
			MrsBankBusiDto mrsBankBusi = mrsBankBusiDtoService.findIsDefault(mrsBankBusiDto);
			String respMsg = "默认卡设置成功！";
			/*
			 * MrsAccountDto mrsAccountDto =
			 * mrsAccountAppService.findByCustId(custId); AaccountType
			 * accountType = mrsAccountDto.getAccountType(); if
			 * (!accountType.equals(AaccountType.CYRYHT)) { String respMsg =
			 * "此一户通不能设置默认银行账户!"; loggger.error("此一户通不能设置默认银行账户");
			 * request.setAttribute("respMsg", respMsg); return
			 * bankIndex(request); }
			 */
			if (!BankBindType.SUCCESS.getValue().equals(mrsBankBusiDto.getBindStatus())) {
				respMsg = "此银行账户未签约,不能设默认银行账户!";
				log.error(respMsg);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(respMsg);
				return respVO;
			}
			if (null != mrsBankBusi) {
				mrsBankBusi.setIsDefault(EIsCrossBank.NO.getValue());
				mrsBankBusiDtoService.updateDefault(mrsBankBusi);
				log.info("把已存在默认银行账户修改为非默认银行账户");
			}
			mrsBankBusiDto.setIsDefault(EIsCrossBank.YES.getValue());
			mrsBankBusiDtoService.updateDefault(mrsBankBusiDto);
			log.info("把非默认银行账户修改为默认银行账户");

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("默认卡设置成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("银行账户id：%s,默认卡设置失败:%s", reqVO.getId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("默认卡设置失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("默认卡设置失败");
			saveErrorExcetpionLog(String.format("银行账户id：%s,默认卡设置失败:%s", reqVO.getId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForBankSetDefault(BankSetDefaultReqVO reqVO) {
		if (reqVO == null) {
			return "BankSetDefaultReqVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getId())) {
			return "Id为空";
		}
		if (reqVO.getId().length() > 36) {
			return "Id超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustId())) {
			return "custId为空";
		}
		if (reqVO.getId().length() > 16) {
			return "custId超长";
		}

		return null;
	}

	/**
	 * 转换成BankAddReqVO
	 */
	private BankAddReqVO toBankAddReqVO(String params) throws Exception {
		BankAddReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, BankAddReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 转换成BankBusiReqVO
	 */
	private BankBusiRestReqVO toBankBusiRestReqVO(String params) throws Exception {
		BankBusiRestReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, BankBusiRestReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 转换成BankSetDefaultRespVO
	 */
	private BankSetDefaultReqVO toBankSetDefaultRestReqVO(String params) throws Exception {
		BankSetDefaultReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, BankSetDefaultReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 授信绑卡校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForAddsx(BankAddReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getLoginUserId()) || reqVO.getLoginUserId().length() > 36) {
			return "登录用户主键Key为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 12) {
			return "一户通号为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getIdType()) || reqVO.getIdType().length() > 2) {
			return "证件类型为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getIdNo()) || reqVO.getAccNo().length() > 64) {
			return "证件号码为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getPayType()) || reqVO.getPayType().length() > 2) {
			return "绑卡类型为空或字段超长";
		}
		if (reqVO.getPayType().equals(BankPayType.BP2.getValue())) {
			return "授信绑卡类型为:02";
		}
		if (StringUtil.isEmpty(reqVO.getAccNo()) || reqVO.getAccNo().length() > 64) {
			return "银行账号为空或字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getCustName()) && reqVO.getCustName().length() > 32) {
			return "姓名字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustType()) || reqVO.getCustType().length() > 1) {
			return "用户类型为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getPayType())) {
			return "绑卡类型为空";
		}
		if (reqVO.getPayType().length() > 2) {
			return "绑卡类型超长";
		}
		if (StringUtil.isEmpty(reqVO.getBankType()) || reqVO.getBankType().length() > 3) {
			return "银行类型为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getOpenBankName()) || reqVO.getOpenBankName().length() > 256) {
			return "开户行名称为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getBankCode()) || reqVO.getBankCode().length() > 14) {
			return "银行行号为空或字段超长";
		}
		if (reqVO.getBankMoblePwd().length() > 100) {
			return "银行电话密码超长";
		}
		if (StringUtil.isEmpty(reqVO.getProvince()) || reqVO.getProvince().length() > 64) {
			return "省名称为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getCity()) || reqVO.getCity().length() > 64) {
			return "城市名称为空或字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getRegionCode()) && reqVO.getRegionCode().length() > 64) {
			return "地区名称字段超长";
		}

		return null;
	}

	/**
	 * 生成快捷绑卡认证码接口
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForGetCode(BankAddReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 12) {
			return "一户通号为空或字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getIdNo()) && reqVO.getIdNo().length() > 18) {
			return "身份证号码为空或字段超长";
		}
		if (!Pattern.compile(RegexEnum.REGEX_IDNO.getRegexValue()).matcher(reqVO.getIdNo()).matches()){
			return "身份证格式不正确";
		}
		if (StringUtil.isEmpty(reqVO.getAccNo()) || reqVO.getAccNo().length() > 64) {
			return "银行卡号为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getMobile()) || reqVO.getMobile().length() > 11) {
			return "手机号为空或字段超长";
		}
		if (!Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(reqVO.getMobile()).matches()){
			return "手机格式不正确";
		}
		if (StringUtil.isEmpty(reqVO.getCustName()) || reqVO.getCustName().length() > 256) {
			return "客户名为空或字段超长";
		}
		/*
		 * if(StringUtil.isEmpty(reqVO.getA) || reqVO.getA.length() > 18){
		 * return "金额为空或字段超长"; }
		 */

		return null;
	}

	/**
	 * 快捷绑卡校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForDoQuick(BankAddReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 12) {
			return "一户通号为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustName()) || reqVO.getCustName().length() > 256) {
			return "客户名为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getIdType()) || reqVO.getIdType().length() > 2){
			return "证件类型为空或字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getIdNo()) && reqVO.getIdNo().length() > 18) {
			return "身份证号码为空或字段超长";
		}
		if (!Pattern.compile(RegexEnum.REGEX_IDNO.getRegexValue()).matcher(reqVO.getIdNo()).matches()){
			return "身份证格式不正确";
		}
		if (StringUtil.isEmpty(reqVO.getMobile()) || reqVO.getMobile().length() > 11) {
			return "手机号为空或字段超长";
		}
		if (!Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(reqVO.getMobile()).matches()){
			return "手机格式不正确";
		}
		if (StringUtil.isEmpty(reqVO.getBankType()) || reqVO.getBankType().length() > 3){
			return "银行类型为空或超长";
		}
		if (StringUtil.isEmpty(reqVO.getCardType()) || reqVO.getCardType().length() > 2){
			return "卡类型为空或超长";
		}
		if (StringUtil.isEmpty(reqVO.getAccNo()) || reqVO.getAccNo().length() > 64) {
			return "银行账号为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getUniqueCode()) || reqVO.getUniqueCode().length() > 7) {
			return "签约认证码为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getSmscode()) || reqVO.getSmscode().length() > 6) {
			return "短信验证码为空或字段超长";
		}

		return null;
	}

	/**
	 * 快捷解绑校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsDelQuick(BankBusiRestReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getId()) || reqVO.getId().length() > 36) {
			return "银行卡主键为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 16) {
			return "一户通号为空或字段超长";
		}

		return null;
	}

	/**
	 * 删除银行账户校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForDoDelCard(BankBusiRestReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 12) {
			return "一户通号为空或字段超长";
		}
		if (StringUtil.isEmpty(reqVO.getAccNo()) || reqVO.getAccNo().length() > 64) {
			return "银行账号为空或字段超长";
		}

		return null;
	}

	/**
	 * 支付记录列表
	 */
	@Override
	public PaymentRespListVO paymentQueryAllData(String params) {
		PaymentReqVO reqVO = new PaymentReqVO();
		PaymentRespListVO respVO = new PaymentRespListVO();

		try {
			log.info("--------进入支付记录列表方法：receive:" + params);
			log.debug("-----------第一步：转换PaymentReqVO对象");
			reqVO = toPaymentReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForPaymentQueryAllData(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			log.debug("-----------第三步，业务处理：");
			PageData<PayPaymentDto> pageData = new PageData<PayPaymentDto>();
			PayPaymentDto paymentDto = new PayPaymentDto();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

			try {
				paymentDto.setCustId(reqVO.getCustId());
				if (StringUtil.isNEmpty(reqVO.getStartTime())) {
					paymentDto.setStartTime(sdf.parse(reqVO.getStartTime() + "000000"));
				}
				if (StringUtil.isNEmpty(reqVO.getEndTime())) {
					paymentDto.setEndTime(sdf.parse(reqVO.getEndTime() + "235959"));
				}

				int pageNo = 1;
				int pageSize = 10;
				try {
					pageNo = Integer.valueOf(reqVO.getPageNo());
					pageSize = Integer.valueOf(reqVO.getPageSize());
				} catch (Exception e) {
					log.error("页数页码输入错误", e);
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo("页数页码请输入数字");
					return respVO;
				}
				pageData.setPageNumber(pageNo);
				if (null == reqVO.getPageSize() || pageSize > 10 || pageSize < 1) {
					pageData.setPageSize(10);
				} else {
					pageData.setPageSize(pageSize);
				}
				// 查询数据
				pageData = payPaymentService.queryAllData(pageData, paymentDto);
			} catch (Exception e) {
				log.error("支付记录查询失败！一户通号：" + reqVO.getCustId(), e);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo("查询失败");
				return respVO;
			}

			respVO.setReturnNum(0);

			// 返回值处理
			List<PaymentRespEleVO> deatils = new ArrayList<PaymentRespEleVO>();
			PaymentRespEleVO detail = new PaymentRespEleVO();
			SimpleDateFormat sdfal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<PayPaymentDto> paymentList = pageData.getRows();
			if (null != paymentList && paymentList.size() > 0) {
				respVO.setReturnNum(paymentList.size());

				for (PayPaymentDto dto : paymentList) {
					detail.setCreateTime(sdfal.format(dto.getCreateTime()));
					BeanUtils.copyProperties(dto, detail);
					deatils.add(detail);
				}
			}

			respVO.setList(deatils);

			// 是否存在下一页
			if (pageData.getTotal() <= pageData.getPageSize()
					|| pageData.getPageNumber() * pageData.getPageSize() >= pageData.getTotal()) {
				respVO.setHasNextPage("N");
			} else {
				respVO.setHasNextPage("Y");
			}

			String respMsg = "查询列表成功！";

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询详情成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("custId：%s,查询列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询列表失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询列表失败");
			saveErrorExcetpionLog(String.format("custId：%s,查询列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 支付详情
	 */
	@Override
	public PaymentRespVO paymentDetail(String params) {
		PaymentReqVO reqVO = new PaymentReqVO();
		PaymentRespVO respVO = new PaymentRespVO();

		try {
			log.info("--------进入支付记录列表方法：receive:" + params);
			log.debug("-----------第一步：转换PaymentReqVO对象");
			reqVO = toPaymentReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForPaymentDetail(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			PayPaymentDto payPaymentDto = payPaymentService.selectByBusiId(reqVO.getBusiId());
			String respMsg = "查询详情成功！";

			BeanUtils.copyProperties(payPaymentDto, respVO);

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询详情成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("busiId：%s,查询详情失败:%s", reqVO.getBusiId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询详情失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询详情失败");
			saveErrorExcetpionLog(String.format("busiId：%s,查询详情失败:%s", reqVO.getBusiId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForPaymentDetail(PaymentReqVO reqVO) {
		if (reqVO == null) {
			return "PaymentReqVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getBusiId())) {
			return "Id为空";
		}
		if (reqVO.getBusiId().length() > 36) {
			return "Id超长";
		}

		return null;
	}

	/**
	 * 转换成PaymentReqVO
	 */
	private PaymentReqVO toPaymentReqVO(String params) throws Exception {
		PaymentReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, PaymentReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 支付记录列表校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForPaymentQueryAllData(PaymentReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 16) {
			return "custId为空或字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getPayStyle()) && EAccountInOut.getEnum(reqVO.getPayStyle()) == null) {
			return "支付方式不存在";
		}
		if (StringUtil.isNEmpty(reqVO.getOrderStatus()) && EOrderStatus.getEnum(reqVO.getOrderStatus()) == null) {
			return "充值状态类型不存在";
		}
		if (StringUtil.isNEmpty(reqVO.getBusiId()) && reqVO.getBusiId().length() > 20) {
			return "交易流水号字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getStartTime()) && reqVO.getStartTime().length() != 8) {
			return "查询起始日期格式YYYYMMDD";
		}
		if (StringUtil.isNEmpty(reqVO.getEndTime()) && reqVO.getEndTime().length() != 8) {
			return "查询结束日期格式YYYYMMDD";
		}
		/* 金额校验 */
		String reg = "^[-\\+]?[\\d]*$";
		if (StringUtil.isNEmpty(reqVO.getStartAmt()) && reqVO.getStartAmt().matches(reg)) {
			return "查询起始金额(单位:分)必须为整数";
		}
		if (StringUtil.isNEmpty(reqVO.getEndAmt()) && reqVO.getEndAmt().matches(reg)) {
			return "查询结束金额(单位:分)必须为整数";
		}
		int pageNo = -1;
		try {
			pageNo = Integer.valueOf(reqVO.getPageNo());
		} catch (NumberFormatException e) {
		}
		if (pageNo < 1) {
			return "查询页号必填初始可为1";
		}

		return null;
	}

	/**
	 * 充值记录列表
	 */
	@Override
	public RechargeRespListVO rechageQueryAllData(String params) {
		RechargeReqVO reqVO = new RechargeReqVO();
		RechargeRespListVO respVO = new RechargeRespListVO();

		try {
			log.info("--------进入充值记录列表方法：receive:" + params);
			log.debug("-----------第一步：转换RechargeReqVO对象");
			reqVO = toRechargeReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForRechageQueryAllData(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			log.debug("-----------第三步，业务处理：");
			PageData<PayRechargeDto> pageData = new PageData<PayRechargeDto>();
			PayRechargeDto rechargeDto = new PayRechargeDto();

			try {
				rechargeDto.setCustId(reqVO.getCustId());
				/*
				 * PayRechargeDto.setStartTime(sdf.parse(reqVO.getStartTime()+
				 * "000000"));
				 * PayRechargeDto.setEndTime(sdf.parse(reqVO.getEndTime()+
				 * "235959"));
				 */

				int pageNo = 1;
				int pageSize = 10;
				try {
					pageNo = Integer.valueOf(reqVO.getPageNo());
					pageSize = Integer.valueOf(reqVO.getPageSize());
				} catch (Exception e) {
					log.error("页数页码输入错误", e);
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo("页数页码请输入数字");
					return respVO;
				}
				pageData.setPageNumber(pageNo);
				if (null == reqVO.getPageSize() || pageSize > 10 || pageSize < 1) {
					pageData.setPageSize(10);
				} else {
					pageData.setPageSize(pageSize);
				}
				// 查询数据
				pageData = payRechargeService.queryAllData(pageData, rechargeDto);
			} catch (Exception e) {
				log.error("充值记录询失败！一户通号：" + reqVO.getCustId(), e);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo("查询失败");
				return respVO;
			}

			respVO.setReturnNum(0);

			// 返回值处理
			List<RechargeRespEleVO> deatils = new ArrayList<RechargeRespEleVO>();
			RechargeRespEleVO detail = new RechargeRespEleVO();
			SimpleDateFormat sdfal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<PayRechargeDto> paymentList = pageData.getRows();
			if (null != paymentList && paymentList.size() > 0) {
				respVO.setReturnNum(paymentList.size());

				for (PayRechargeDto dto : paymentList) {
					detail.setCreateTime(sdfal.format(dto.getCreateTime()));
					BeanUtils.copyProperties(dto, detail);
					deatils.add(detail);
				}
			}

			respVO.setList(deatils);

			// 是否存在下一页
			if (pageData.getTotal() <= pageData.getPageSize()
					|| pageData.getPageNumber() * pageData.getPageSize() >= pageData.getTotal()) {
				respVO.setHasNextPage("N");
			} else {
				respVO.setHasNextPage("Y");
			}

			String respMsg = "查询列表成功！";

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询详情成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("custId：%s,查询列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询列表失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询列表失败");
			saveErrorExcetpionLog(String.format("custId：%s,查询列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 充值详情
	 */
	@Override
	public RechargeRespVO rechageDetail(String params) {
		RechargeReqVO reqVO = new RechargeReqVO();
		RechargeRespVO respVO = new RechargeRespVO();

		try {
			log.info("--------进入充值记录详情方法：receive:" + params);
			log.debug("-----------第一步：转换RechargeReqVO对象");
			reqVO = toRechargeReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForRechageDetail(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			PayRechargeDto rechargeDto = payRechargeService.selectByBusiId(reqVO.getBusiId());
			String respMsg = "查询详情成功！";

			BeanUtils.copyProperties(rechargeDto, respVO);

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询详情成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("busiId：%s,查询详情失败:%s", reqVO.getBusiId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询详情失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询详情失败");
			saveErrorExcetpionLog(String.format("busiId：%s,查询详情失败:%s", reqVO.getBusiId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForRechageDetail(RechargeReqVO reqVO) {
		if (reqVO == null) {
			return "PaymentReqVO对象问空";
		}
		if (StringUtil.isEmpty(reqVO.getBusiId())) {
			return "busiId为空";
		}
		if (reqVO.getBusiId().length() > 20) {
			return "busiId超长";
		}

		return null;
	}

	/**
	 * 转换成RechargeReqVO
	 */
	private RechargeReqVO toRechargeReqVO(String params) throws Exception {
		RechargeReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, RechargeReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 充值记录列表校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForRechageQueryAllData(RechargeReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 16) {
			return "custId为空或字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getRechargeStyle()) && EAccountInOut.getEnum(reqVO.getRechargeStyle()) == null) {
			return "充值渠道不存在";
		}
		if (StringUtil.isNEmpty(reqVO.getBusiId()) && reqVO.getBusiId().length() > 20) {
			return "交易流水号字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getStartTime()) && reqVO.getStartTime().length() != 8) {
			return "查询起始日期格式YYYYMMDD";
		}
		if (StringUtil.isNEmpty(reqVO.getEndTime()) && reqVO.getEndTime().length() != 8) {
			return "查询结束日期格式YYYYMMDD";
		}
		/* 金额校验 */
		String reg = "^[-\\+]?[\\d]*$";
		if (StringUtil.isNEmpty(reqVO.getStartAmt()) && reqVO.getStartAmt().matches(reg)) {
			return "查询起始金额(单位:分)必须为整数";
		}
		if (StringUtil.isNEmpty(reqVO.getEndAmt()) && reqVO.getEndAmt().matches(reg)) {
			return "查询结束金额(单位:分)必须为整数";
		}
		int pageNo = -1;
		try {
			pageNo = Integer.valueOf(reqVO.getPageNo());
		} catch (NumberFormatException e) {
		}
		if (pageNo < 1) {
			return "查询页号必填初始可为1";
		}

		return null;
	}

	/**
	 * 提现记录列表
	 */
	@Override
	public WithdrawRespListVO withdrawQueryAllData(String params) {
		WithdrawReqVO reqVO = new WithdrawReqVO();
		WithdrawRespListVO respVO = new WithdrawRespListVO();

		try {
			log.info("--------进入提现记录列表方法：receive:" + params);
			log.debug("-----------第一步：转换WithdrawReqVO对象");
			reqVO = toWithdrawReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForWithdrawQueryAllData(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			log.debug("-----------第三步，业务处理：");
			PageData<PayWithdrawDto> pageData = new PageData<PayWithdrawDto>();
			PayWithdrawDto withdrawDto = new PayWithdrawDto();

			try {
				withdrawDto.setCustId(reqVO.getCustId());
				/*
				 * PayWithdrawDto.setStartTime(sdf.parse(reqVO.getStartTime()+
				 * "000000"));
				 * PayWithdrawDto.setEndTime(sdf.parse(reqVO.getEndTime()+
				 * "235959"));
				 */

				int pageNo = 1;
				int pageSize = 10;
				try {
					pageNo = Integer.valueOf(reqVO.getPageNo());
					pageSize = Integer.valueOf(reqVO.getPageSize());
				} catch (Exception e) {
					log.error("页数页码输入错误", e);
					respVO.setMsgCode(PortalCode.CODE_9999);
					respVO.setMsgInfo("页数页码请输入数字");
					return respVO;
				}
				pageData.setPageNumber(pageNo);
				if (null == reqVO.getPageSize() || pageSize > 10 || pageSize < 1) {
					pageData.setPageSize(10);
				} else {
					pageData.setPageSize(pageSize);
				}
				// 查询数据
				pageData = payWithdrawService.queryAllData(pageData, withdrawDto);
			} catch (Exception e) {
				log.error("支付记录查询失败！一户通号：" + reqVO.getCustId(), e);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo("查询失败");
				return respVO;
			}

			respVO.setReturnNum(0);

			// 返回值处理
			List<WithdrawRespEleVO> deatils = new ArrayList<WithdrawRespEleVO>();
			WithdrawRespEleVO detail = new WithdrawRespEleVO();
			SimpleDateFormat sdfal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<PayWithdrawDto> withdrawList = pageData.getRows();
			if (null != withdrawList && withdrawList.size() > 0) {
				respVO.setReturnNum(withdrawList.size());

				for (PayWithdrawDto dto : withdrawList) {
					detail.setCreateTime(sdfal.format(dto.getCreateTime()));
					BeanUtils.copyProperties(dto, detail);
					deatils.add(detail);
				}
			}

			respVO.setList(deatils);

			// 是否存在下一页
			if (pageData.getTotal() <= pageData.getPageSize()
					|| pageData.getPageNumber() * pageData.getPageSize() >= pageData.getTotal()) {
				respVO.setHasNextPage("N");
			} else {
				respVO.setHasNextPage("Y");
			}

			String respMsg = "查询列表成功！";

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询详情成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("custId：%s,查询列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询列表失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询列表失败");
			saveErrorExcetpionLog(String.format("custId：%s,查询列表失败:%s", reqVO.getCustId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 提现记录详情
	 */
	@Override
	public WithdrawRespVO withdrawDetail(String params) {
		WithdrawReqVO reqVO = new WithdrawReqVO();
		WithdrawRespVO respVO = new WithdrawRespVO();

		try {
			log.info("--------进入提现记录详情方法：receive:" + params);
			log.debug("-----------第一步：转换WithdrawReqVO对象");
			reqVO = toWithdrawReqVO(params);

			// 检查登录Token
			/*String checkToken = checkLoginTokenParams(reqVO.getLoginToken());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkToken);
				return respVO;
			}*/

			log.debug("-----------第二步，校验入参参数：");
			String checkResult = checkParamsForWithdrawDetail(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVO.setMsgCode(PortalCode.CODE_9999);
				respVO.setMsgInfo(checkResult);
				return respVO;
			}

			PayWithdrawDto payWithdrawDto = payWithdrawService.selectByBusiId(reqVO.getBusiId());
			String respMsg = "查询详情成功！";

			BeanUtils.copyProperties(payWithdrawDto, respVO);

			respVO.setMsgCode(PortalCode.CODE_0000);
			respVO.setMsgInfo(respMsg);
			JSONObject jsons = JSONObject.fromObject(respVO);
			log.info("查询详情成功,返回json对象：{}", jsons.toString());
			return respVO;
		} catch (CodeCheckedException e) {
			log.error("失败：" + e.getMessage());
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("busiId：%s,查询详情失败:%s", reqVO.getBusiId(), e.getMessage()));
			return respVO;
		} catch (Exception e) {
			log.error("查询详情失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVO.setMsgCode(PortalCode.CODE_9999);
			respVO.setMsgInfo("查询详情失败");
			saveErrorExcetpionLog(String.format("busiId：%s,查询详情失败:%s", reqVO.getBusiId(), e.getMessage()));
			return respVO;
		}
	}

	/**
	 * 检查必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForWithdrawDetail(WithdrawReqVO reqVO) {
		if (reqVO == null) {
			return "WithdrawReqVO对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getBusiId())) {
			return "busiId为空";
		}
		if (reqVO.getBusiId().length() > 20) {
			return "busiId超长";
		}

		return null;
	}

	/**
	 * 转换成WithdrawReqVO
	 */
	private WithdrawReqVO toWithdrawReqVO(String params) throws Exception {
		WithdrawReqVO requestVo = null;
		try {
			requestVo = getObjectBean(params, WithdrawReqVO.class);
			if (requestVo == null) {
				log.error("转换对象为空、Gson对象转换失败!");
				throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
			}
			return requestVo;
		} catch (Exception e) {
			log.error("Gson转换错误：" + e.toString());
			throw new CodeCheckedException(PortalCode.CODE_9999, "JSON转换失败");
		}
	}

	/**
	 * 提现记录列表校验必需参数
	 * 
	 * @param baseContent
	 * @param bizContent
	 * @return
	 */
	private String checkParamsForWithdrawQueryAllData(WithdrawReqVO reqVO) {
		if (reqVO == null) {
			return "请求对象为空";
		}
		if (StringUtil.isEmpty(reqVO.getCustId()) || reqVO.getCustId().length() > 16) {
			return "custId为空或字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getOrderStatus()) && EOrderStatus.getEnum(reqVO.getOrderStatus()) == null) {
			return "提现状态类型不存在";
		}
		if (StringUtil.isNEmpty(reqVO.getBusiId()) && reqVO.getBusiId().length() > 20) {
			return "交易流水号字段超长";
		}
		if (StringUtil.isNEmpty(reqVO.getStartTime()) && reqVO.getStartTime().length() != 8) {
			return "查询起始日期格式YYYYMMDD";
		}
		if (StringUtil.isNEmpty(reqVO.getEndTime()) && reqVO.getEndTime().length() != 8) {
			return "查询结束日期格式YYYYMMDD";
		}
		/* 金额校验 */
		String reg = "^[-\\+]?[\\d]*$";
		if (StringUtil.isNEmpty(reqVO.getStartAmt()) && reqVO.getStartAmt().matches(reg)) {
			return "查询起始金额(单位:分)必须为整数";
		}
		if (StringUtil.isNEmpty(reqVO.getEndAmt()) && reqVO.getEndAmt().matches(reg)) {
			return "查询结束金额(单位:分)必须为整数";
		}
		int pageNo = -1;
		try {
			pageNo = Integer.valueOf(reqVO.getPageNo());
		} catch (NumberFormatException e) {
		}
		if (pageNo < 1) {
			return "查询页号必填初始可为1";
		}

		return null;
	}

	/**
	 * @接口描述 
	 * 客户信息查询接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public AccountMsgInfoRespVo findMrsPersonDtoByCustId(String params) {
		log.info("开始执行客户信息查询方法，service：" + params);
		AccountMsgInfoRestVo restVo = new AccountMsgInfoRestVo();
		AccountMsgInfoRespVo respVo = new AccountMsgInfoRespVo();
		try {
			// 转换AccountMsgInfoRestVo对象
			log.debug("转换AccountMsgInfoRestVo对象...");
			restVo = toAccountMsgInfoRestVo(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}
			
			// 解析请求参数
			log.debug("解析请求参数...");
			String result = checkRequestValue(restVo);
			// 解析返回结果不为空
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			MrsPersonDto mrsPersonDro = mrsPersonService.findByCustId(restVo.getCustId());
			if (mrsPersonDro == null) {
				log.error("根据一户通查询客户信息为空！");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("查询客户信息为空！");
				return respVo;
			}
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			respVo.setCustId(mrsPersonDro.getCustId());
			respVo.setCustomerName(mrsPersonDro.getCustomerName());
			respVo.setCustomerCode(mrsPersonDro.getCustomerCode());
			respVo.setCredentialsType(mrsPersonDro.getCredentialsType());
			respVo.setCredentialsNumber(mrsPersonDro.getCredentialsNumber());
			respVo.setNationalCode(MrsNationaltyCode.getEnum(mrsPersonDro.getNationalityCode().getValue()).getDisplayName());
			respVo.setCredentialsEnddate(mrsPersonDro.getCredentialsEnddate());
			respVo.setSexCode(mrsPersonDro.getSexCode());
			respVo.setEducationCode(mrsPersonDro.getEducationCode());
			respVo.setNationalCode(mrsPersonDro.getNationalCode());
			respVo.setMobile(mrsPersonDro.getMobile());
			respVo.setEmail(mrsPersonDro.getEmail());
			respVo.setTel(mrsPersonDro.getTel());
			respVo.setContactAddr(mrsPersonDro.getContactAddr());
			respVo.setZipCode(mrsPersonDro.getZipCode());
			respVo.setProviceCode(mrsPersonDro.getProviceCode());
			respVo.setCityCode(mrsPersonDro.getCityCode());
			respVo.setDictCode(mrsPersonDro.getDictCode());
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("客户信息查询成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("客户信息查询失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,机构账户变更失败:%s", restVo.getCustId(), e.getMessage()));
			return respVo;
		}
		return respVo;
	}

	/**
	 * @接口描述
	 * 客户信息修改接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public AccountMsgInfoRespVo updateMrsPersonDto(String params) {
		log.info("开始执行客户修改方法，service：" + params);
		AccountMsgInfoRestVo restVo = new AccountMsgInfoRestVo();
		AccountMsgInfoRespVo respVo = new AccountMsgInfoRespVo();
		try {
			// 将请求参数转换成AccountMsgInfoRestVo对象
			log.debug("转换请求参数为AccountMsgInfoRestVo对象...");
			restVo = toAccountMsgInfoRestVo(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}
			
			log.debug("开始校验参数...");
			String result = checkRequest2Value(restVo);
			// 解析返回结果不为空返回处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			MrsPersonDto mrsPersonDto = mrsPersonService.findByCustId(restVo.getCustId());
			if (mrsPersonDto == null) {
				log.error("根据一户通查询客户信息为空！");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("查询客户信息为空！");
				return respVo;
			}
			mrsPersonDto = updateMrsPersonDto(restVo);
			mrsPersonService.update(mrsPersonDto);
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			respVo.setCustId(restVo.getCustId());
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("客户信息修改成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("客户信息查询失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,机构账户变更失败:%s", restVo.getCustId(), e.getMessage()));
			return respVo;
		}
		return respVo;

	}

	/**
	 * 将请求参数转换成AccountMsgInfoRestVo对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private AccountMsgInfoRestVo toAccountMsgInfoRestVo(String params) throws Exception {
		AccountMsgInfoRestVo requestVo = null;
		try {
			requestVo = getObjectBean(params, AccountMsgInfoRestVo.class);
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
	 * 校验请求参数
	 * 
	 * @param restVo
	 * @return
	 */
	private String checkRequestValue(AccountMsgInfoRestVo restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getCustId()) || restVo.getCustId().length() > 12) {
			return "一户通账户为空或超长";
		}
		return null;
	}

	/**
	 * 查询接口参数检验
	 * 
	 * @param restVo
	 * @return
	 */
	private String checkRequest2Value(AccountMsgInfoRestVo restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getCustId())) {
			return "一户通账户不能为空";
		}
		if (!StringUtil.isEmpty(restVo.getCustomerName())) {
			return "客户名称不能修改";
		}
		if (!StringUtil.isEmpty(restVo.getCredentialsType())) {
			return "证件类型不能修改";
		}
		if (!StringUtil.isEmpty(restVo.getCredentialsNumber())) {
			return "证件号码不能修改";
		}
		if (!StringUtil.isEmpty(restVo.getCredentialsEnddate())) {
			if (restVo.getCredentialsEnddate().length() > 8) {
				return "证件有效期超长";
			}
			if(!Pattern.compile(RegexEnum.REGEX_DIGIT8.getRegexValue()).matcher(restVo.getCredentialsEnddate()).matches()){
				return "证件有效期格式不正确";
			}
		}
		if (!StringUtil.isEmpty(restVo.getBirthdate())) {
			if (restVo.getBirthdate().length() > 8) {
				return "出生日期超长";
			}
			if(!Pattern.compile(RegexEnum.REGEX_DIGIT8.getRegexValue()).matcher(restVo.getBirthdate()).matches()){
				return "出生日期格式不正确";
			}
		}
		if (!StringUtil.isEmpty(restVo.getSexCode())) {
			if (restVo.getSexCode().length() > 1) {
				return "性别长度超长";
			}
			MrsSexCode sexEunmCode = MrsSexCode.getEnum(restVo.getSexCode());
		    if(sexEunmCode == null){
		    	return "性别类型不存在";
		    }
		}
		if (!StringUtil.isEmpty(restVo.getEducationCode())) {
			if (restVo.getEducationCode().length() > 2) {
				return "学历长度超长";
			}
			MrsEducationCode educationCode = MrsEducationCode.getEnum(restVo.getEducationCode());
		    if(educationCode == null){
		    	return "学历类型不存在";
		    }
		}
		if (!StringUtil.isEmpty(restVo.getNationalCode())) {
			if (restVo.getNationalCode().length() > 2) {
				return "民族长度超长";
			}
			MrsNationalCode nationalCode = MrsNationalCode.getEnum(restVo.getNationalCode());
			if(nationalCode == null){
				return "民族类型不存在";
			}
		}
		if (!StringUtil.isEmpty(restVo.getMobile())) {
			if (restVo.getMobile().length() > 11) {
				return "手机号长度超长";
			}
			if(!Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(restVo.getMobile()).matches()){
				return "手机号格式不正确";
			}
		}
		if (!StringUtil.isEmpty(restVo.getTel())) {
			if (restVo.getTel().length() > 8) {
				return "固定电话长度超长";
			}
			if(!Pattern.compile(RegexEnum.REGEX_PHONE.getRegexValue()).matcher(restVo.getTel()).matches()){
				return "固定电话格式不正确";
			}
		}
		if (!StringUtil.isEmpty(restVo.getContactAddr())) {
			if (restVo.getContactAddr().length() > 80) {
				return "联系地址长度超长";
			}
		}
		if (!StringUtil.isEmpty(restVo.getZipCode())) {
			if (restVo.getZipCode().length() > 6) {
				return "邮编长度超长";
			}
			if (!Pattern.compile(RegexEnum.REGEX_POST.getRegexValue()).matcher(restVo.getZipCode()).matches()) {
				return "邮编格式不正确";
			}
		}
		if (!StringUtil.isEmpty(restVo.getSpareTel())) {
			if (restVo.getSpareTel().length() > 16) {
				return "备用联系电话长度超长";
			}
		}
		if (!StringUtil.isEmpty(restVo.getEmail())) {
			if (restVo.getEmail().length() > 64) {
				return "邮箱长度超长";
			}
			if(!Pattern.compile(RegexEnum.REGEX_EMAIL.getRegexValue()).matcher(restVo.getEmail()).matches()){
				return "邮箱格式不正确";
			}
		}
		return null;
	}

	/**
	 * 修改客户信息
	 * 
	 * @param restVo
	 * @return
	 */
	private MrsPersonDto updateMrsPersonDto(AccountMsgInfoRestVo restVo) {
		MrsPersonDto mrsPersonDto = mrsPersonService.findByCustId(restVo.getCustId());
		if (!StringUtil.isEmpty(restVo.getCustomerName())) {
			mrsPersonDto.setCustomerName(restVo.getCustomerName());
		}
		if (!StringUtil.isEmpty(restVo.getCredentialsType())) {
			mrsPersonDto.setCredentialsType(restVo.getCredentialsType());
		}
		if (!StringUtil.isEmpty(restVo.getCredentialsNumber())) {
			mrsPersonDto.setCredentialsNumber(restVo.getCredentialsNumber());
		}
		if (!StringUtil.isEmpty(restVo.getCredentialsEnddate())) {
			mrsPersonDto.setCredentialsEnddate(restVo.getCredentialsEnddate());
		}
		if (!StringUtil.isEmpty(restVo.getBirthdate())) {
			mrsPersonDto.setBirthdate(restVo.getBirthdate());
		}
		if (!StringUtil.isEmpty(restVo.getSexCode())) {
			mrsPersonDto.setSexCode(restVo.getSexCode());
		}
		if (!StringUtil.isEmpty(restVo.getEducationCode())) {
			mrsPersonDto.setEducationCode(restVo.getEducationCode());
		}
		if (!StringUtil.isEmpty(restVo.getNationalCode())) {
			mrsPersonDto.setNationalCode(restVo.getNationalCode());
		}
		if (!StringUtil.isEmpty(restVo.getMobile())) {
			mrsPersonDto.setMobile(restVo.getMobile());
		}
		if (!StringUtil.isEmpty(restVo.getTel())) {
			mrsPersonDto.setTel(restVo.getTel());
		}
		if (!StringUtil.isEmpty(restVo.getContactAddr())) {
			mrsPersonDto.setContactAddr(restVo.getContactAddr());
		}
		if (!StringUtil.isEmpty(restVo.getZipCode())) {
			mrsPersonDto.setZipCode(restVo.getZipCode());
		}
		if (!StringUtil.isEmpty(restVo.getSpareTel())) {
			mrsPersonDto.setSpareTel(restVo.getSpareTel());
		}
		if (!StringUtil.isEmpty(restVo.getEmail())) {
			mrsPersonDto.setEmail(restVo.getEmail());
		}
		if (!StringUtil.isEmpty(restVo.getProviceCode())) {
			mrsPersonDto.setEmail(restVo.getProviceCode());
		}
		if (!StringUtil.isEmpty(restVo.getCityCode())) {
			mrsPersonDto.setEmail(restVo.getCityCode());
		}
		if (!StringUtil.isEmpty(restVo.getDictCode())) {
			mrsPersonDto.setEmail(restVo.getDictCode());
		}
		mrsPersonDto.setUpdateTime(new Date());
		return mrsPersonDto;
	}

	/**
	 * @接口描述
	 * 新增联系人接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public BaseRespVO saveMrsContactDto(String params) {
		log.info("开始执行新增联系人方法，service:" + params);
		ContactListMsgRestVo restVo = new ContactListMsgRestVo();
		BaseRespVO respVo = new BaseRespVO();
		try {
			log.debug("开始转换请求参数为ContactListMsgRestVo对象...");
			restVo = toContactListMsgRespVo(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}			
			
			log.debug("开始校验参数...");
			String result = checkValues(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			// 根据一户通账户查询一户通信息
			MrsAccountDto mrsAccountDto = mrsAccountService.findByCustId(restVo.getCustId());
			if (mrsAccountDto == null) {
				log.error("根据一户通账户：{}，查询一户通信息为空！", restVo.getCustId());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("根据一户通账户查询一户通信息为空！");
				return respVo;
			}
			MrsContactListDto mrsContactDto = createContactDto(restVo);
			mrsContactListService.insert(mrsContactDto);
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("新增联系人成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("联系人新增失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,联系人新增失败:%s", restVo.getCustId(), e.getMessage()));
			return respVo;
		}
		return respVo;
	}

	/**
	 * @接口描述
	 * 修改联系人信息接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public BaseRespVO updateMrsContactDto(String params) {
		log.info("开始执行联系人修改方法，service:" + params);
		ContactListMsgRestVo restVo = new ContactListMsgRestVo();
		BaseRespVO respVo = new BaseRespVO();
		try {
			log.debug("转换请求参数为ContactListMsgRestVo对象...");
			restVo = toContactListMsgRespVo(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}	
			
			log.debug("校验请求参数...");
			String result = checkUpadteValues(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			
//			MrsContactListDto mrsContactListDto = mrsContactListService.selectByPrimaryKey(restVo.getContactId());
			MrsContactListDto mrsContactListDto = mrsContactListService.findByIdandCustId(restVo.getContactId(),restVo.getCustId());
			if (mrsContactListDto == null) {
				log.error("根据ID:{},custId:{},查询联系人为空！", restVo.getContactId(),restVo.getCustId());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("根据ID["+restVo.getContactId()+",custId:["+restVo.getCustId()+"]查询联系人为空！");
				return respVo;
			}
			MrsContactListDto mrsContactDto = updateContactDto(restVo);
			mrsContactListService.updateByPrimaryKey(mrsContactDto);
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("修改联系人成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("联系人修改失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("主键ID：%s,联系人修改失败:%s", restVo.getContactId(), e.getMessage()));
			return respVo;
		}
		return respVo;

	}

	/**
	 * @接口描述
	 * 查询联系人信息接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public List<Object> selectMrsContactDto(String params) {
		log.info("开始执行查询联系人信息方法，service：" + params);
		ContactListMsgRestVo restVo = new ContactListMsgRestVo();
		ContactListMsgRespVo respVo = new ContactListMsgRespVo();
		BaseRespVO vo = new BaseRespVO();
		ContactMsgRespVO resVo = new ContactMsgRespVO();
		List<Object> contactDtos = new ArrayList<Object>();
		try {
			log.debug("转换请求参数为ContactListMsgRespVo对象...");
			restVo = toContactListMsgRespVo(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				contactDtos.add(respVo);
				return contactDtos;
			}
			
			log.debug("请求参数校验开始...");
			String result = checkFindValues(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				vo.setMsgCode(PortalCode.CODE_9999);
				vo.setMsgInfo(result);
				contactDtos.add(vo);
				return contactDtos;
			}
			// 获取联系人信息
			List<MrsContactListDto> mrsContactListDtos = mrsContactListService.findByCustId(restVo.getCustId());
			if (CollectionUtil.isEmpty(mrsContactListDtos)) {
				log.error("根据一户通账户：{}，查询联系人信息为空！", restVo.getCustId());
				vo.setMsgCode(PortalCode.CODE_0000);
				vo.setMsgInfo("查询联系人信息为空！");
				contactDtos.add(vo);
				return contactDtos;
			}

			vo.setMsgCode(PortalCode.CODE_0000);
			vo.setMsgInfo("处理成功");
			contactDtos.add(vo);
			for (MrsContactListDto dto : mrsContactListDtos) {
				// 处理成功
				resVo = new ContactMsgRespVO();
				resVo.setCustId(dto.getCustId());
				resVo.setContactId(dto.getId());
				resVo.setName(dto.getName());
				resVo.setCertType(dto.getCertType());
				resVo.setCertNo(dto.getCertNo());
				resVo.setMobill(dto.getMobill());
				resVo.setPhoneNo(dto.getPhoneNo());
				resVo.setEmail(dto.getEmail());
				resVo.setSource(dto.getSource());
				resVo.setStatus(dto.getStatus());
				resVo.setAddress(dto.getAddress());
				contactDtos.add(resVo);
			}
			// JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("查询联系人信息成功,返回json对象：{}",contactDtos);
		} catch (Exception e) {
			log.error("查询联系人信息失败：" + e.getMessage());
			vo.setMsgCode(PortalCode.CODE_9999);
			vo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,联系人信息查询失败:%s", restVo.getCustId(), e.getMessage()));
			contactDtos.add(respVo);
			return contactDtos;
		}
		return contactDtos;
	}

	/**
	 * @接口描述
	 * 删除联系人接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public BaseRespVO deleteMrsContactDto(String params) {
		log.info("开始执行联系人删除方法，service：" + params);
		ContactListMsgRestVo restVo = new ContactListMsgRestVo();
		BaseRespVO respVo = new BaseRespVO();
		try {
			log.debug("转换请求参数为ContactListMsgRestVo对象...");
			restVo = toContactListMsgRespVo(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}
			
			log.debug("校验请求参数开始...");
			String result = checkDeleteValues(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			MrsContactListDto mrsContactDto = mrsContactListService.findByIdandCustId(restVo.getContactId(), restVo.getCustId());
			if(mrsContactDto == null){
				log.error("该联系人不属于custId:{},用户！",restVo.getCustId());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("该联系人不属于custId["+restVo.getCustId()+"]用户！");
				return respVo;
			}
			//根据联系人ID删除联系人
			mrsContactListService.deleteByPrimaryKey(restVo.getContactId());
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("删除联系人信息成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("删除联系人信息失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,删除联系人信息失败:%s", restVo.getCustId(), e.getMessage()));
			return respVo;
		}
		return respVo;
	}

	/**
	 * 将请求参数转换成ContactListMsgRespVo对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private ContactListMsgRestVo toContactListMsgRespVo(String params) throws Exception {
		ContactListMsgRestVo requestVo = null;
		try {
			requestVo = getObjectBean(params, ContactListMsgRestVo.class);
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
	 * 新增请求参数校验
	 * 
	 * @param restVo
	 * @return
	 */
	private String checkValues(ContactListMsgRestVo restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getCustId())) {
			return "一户通账户为空";
		}
		if (StringUtil.isEmpty(restVo.getSource()) || restVo.getSource().length() > 2) {
			return "联系人来源为空或超长";
		}
		
		EStartSystemEnum start = EStartSystemEnum.getEnum(restVo.getSource());
		if(start == null){
			return "联系人来源不存在";
		}
		if (StringUtil.isEmpty(restVo.getName()) || restVo.getName().length() > 30) {
			return "联系人姓名为空或超长";
		}
		if (StringUtil.isEmpty(restVo.getStatus()) || restVo.getStatus().length() > 2) {
			return "联系人状态为空或超长";
		}
		if(!StringUtil.isEmpty(restVo.getCertType())){
			if(restVo.getCertType().length() > 2){
				return "证件类型超长";
			}
			MrsCredentialsType certType = MrsCredentialsType.getEnum(restVo.getCertType());
		    if(certType == null){
		    	return "证件类型不存在";
		    }
		}
		if(!StringUtil.isEmpty(restVo.getCertNo())){
			if(!Pattern.compile(RegexEnum.REGEX_CH_EH_NUM_32.getRegexValue()).matcher(restVo.getCertNo()).matches()){
				return "证件号码格式不正确";
			}
		}
		if(!StringUtil.isEmpty(restVo.getMobill())){
			if(!Pattern.compile(RegexEnum.REGEX_PHONE.getRegexValue()).matcher(restVo.getMobill()).matches()){
				return "电话格式不正确";
			}
		}
		if(!StringUtil.isEmpty(restVo.getPhoneNo())){
			if(!Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(restVo.getPhoneNo()).matches()){
				return "手机号格式不正确";
			}
		}
		if(!StringUtil.isEmpty(restVo.getEmail())){
			if(!Pattern.compile(RegexEnum.REGEX_EMAIL.getRegexValue()).matcher(restVo.getEmail()).matches()){
				return "邮箱格式不正确";
			}
		}
		if(!StringUtil.isEmpty(restVo.getAddress())){
			if(restVo.getAddress().length() > 120){
				return "地址超长";
			}
		}
		return null;
	}

	/**
	 * 检验查询请求参数
	 * 
	 * @param restVo
	 * @return
	 */
	private String checkFindValues(ContactListMsgRestVo restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getCustId())) {
			return "一户通账户为空";
		}
		return null;
	}

	/**
	 * 删除联系人请求参数校验
	 * 
	 * @param restVo
	 * @return
	 */
	private String checkDeleteValues(ContactListMsgRestVo restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getCustId())) {
			return "一户通账户为空";
		}
		if (StringUtil.isEmpty(restVo.getContactId())) {
			return "联系人ID为空";
		}
		return null;
	}

	/**
	 * 校验查询联系人详情
	 * 
	 * @return
	 */
	private String checkDetailValue(ContactListMsgRestVo restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getContactId())) {
			return "联系人ID为空";
		}
		if (StringUtil.isEmpty(restVo.getCustId())) {
			return "一户通账号为空";
		}
		return null;
	}

	/**
	 * 校验修改请求参数
	 * 
	 * @return
	 */
	private String checkUpadteValues(ContactListMsgRestVo restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getCustId())||restVo.getCustId().length() > 12) {
			return "一户通账户为空或超长";
		}
		if (StringUtil.isEmpty(restVo.getContactId())) {
			return "联系人ID为空";
		}
		if (!StringUtil.isEmpty(restVo.getName())) {
			if (restVo.getName().length() > 30) {
				return "联系人姓名长度超长";
			}
		}
		if (!StringUtil.isEmpty(restVo.getSource())) {
			if (restVo.getSource().length() > 2) {
				return "联系人来源长度超长";
			}
			if(EStartSystemEnum.getEnum(restVo.getSource())==null){
				return "联系人来源类型不存在";
			}
		}
		if (!StringUtil.isEmpty(restVo.getPhoneNo())) {
			if (restVo.getPhoneNo().length() > 11) {
				return "联系人手机号超长";
			}
			if(!Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(restVo.getPhoneNo()).matches()){
				return "联系人手机号格式不正确";
			}
		}
		if (!StringUtil.isEmpty(restVo.getMobill())) {
			if (restVo.getMobill().length() > 20) {
				return "联系人电话超长";
			}
			if(!Pattern.compile(RegexEnum.REGEX_PHONE.getRegexValue()).matcher(restVo.getPhoneNo()).matches()){
				return "联系人电话格式不正确";
			}
		}
		if (!StringUtil.isEmpty(restVo.getCertType())) {
			if (restVo.getCertType().length() > 2) {
				return "联系人证件类型超长";
			}
			if(MrsCredentialsType.getEnum(restVo.getCertType())==null){
				return "证件类型不存在";
			}
		}
		if (!StringUtil.isEmpty(restVo.getCertNo())) {
			if (restVo.getCertNo().length() > 30) {
				return "联系人证件号码超长";
			}
			if(!Pattern.compile(RegexEnum.REGEX_CH_EH_NUM_30.getRegexValue()).matcher(restVo.getCertNo()).matches()){
				return "联系人证件号码格式不正确";
			}
		}
		if (!StringUtil.isEmpty(restVo.getStatus())) {
			if (restVo.getStatus().length() > 2) {
				return "联系人状态超长";
			}
			if(EStatusEnum.getEnum(restVo.getStatus())==null){
				return "联系人状态类型不存在";
			}
		}
		if (!StringUtil.isEmpty(restVo.getAddress())) {
			if (restVo.getAddress().length() > 120) {
				return "联系人地址超长";
			}
		}
		return null;
	}

	/**
	 * 创建联系人对象
	 * 
	 * @param restVo
	 * @return
	 */
	private MrsContactListDto createContactDto(ContactListMsgRestVo restVo) {
		MrsContactListDto mrsContactDto = new MrsContactListDto();
		mrsContactDto.setId(UUID.randomUUID().toString());
		mrsContactDto.setCustId(restVo.getCustId());
		mrsContactDto.setCertNo(restVo.getCertNo());
		mrsContactDto.setCertType(restVo.getCertType());
		mrsContactDto.setSource(restVo.getSource());
		mrsContactDto.setName(restVo.getName());
		mrsContactDto.setPhoneNo(restVo.getPhoneNo());
		mrsContactDto.setMobill(restVo.getMobill());
		mrsContactDto.setStatus(restVo.getStatus());
		mrsContactDto.setEmail(restVo.getEmail());
		mrsContactDto.setAddress(restVo.getAddress());
		mrsContactDto.setCreateTime(new Date());
		return mrsContactDto;
	}

	/**
	 * 修改联系人信息
	 * 
	 * @param dto
	 * @return
	 */
	private MrsContactListDto updateContactDto(ContactListMsgRestVo restVo) {
		
		MrsContactListDto mrsConatctListDto = mrsContactListService.selectByPrimaryKey(restVo.getContactId());
		if (!StringUtil.isEmpty(restVo.getSource())) {
			mrsConatctListDto.setSource(restVo.getSource());
		}
		if (!StringUtil.isEmpty(restVo.getName())) {
			mrsConatctListDto.setName(restVo.getName());
		}
		if (!StringUtil.isEmpty(restVo.getPhoneNo())) {
			mrsConatctListDto.setPhoneNo(restVo.getPhoneNo());
		}
		if (!StringUtil.isEmpty(restVo.getMobill())) {
			mrsConatctListDto.setMobill(restVo.getMobill());
		}
		if (!StringUtil.isEmpty(restVo.getCertType())) {
			mrsConatctListDto.setCertType(restVo.getCertType());
		}
		if (!StringUtil.isEmpty(restVo.getCertNo())) {
			mrsConatctListDto.setCertNo(restVo.getCertNo());
		}
		if (!StringUtil.isEmpty(restVo.getStatus())) {
			mrsConatctListDto.setStatus(restVo.getStatus());
		}
		if (!StringUtil.isEmpty(restVo.getEmail())) {
			mrsConatctListDto.setEmail(restVo.getEmail());
		}
		if (!StringUtil.isEmpty(restVo.getAddress())) {
			mrsConatctListDto.setAddress(restVo.getAddress());
		}
		return mrsConatctListDto;
	}

	/**
	 * 查询联系人详情（单个联系人）
	 */
	@Override
	public ContactListMsgRespVo selectMrsContactDetail(String params) {
		log.info("开始执行查询联系人详情方法，service：" + params);
		ContactListMsgRestVo restVo = new ContactListMsgRestVo();
		ContactListMsgRespVo respVo = new ContactListMsgRespVo();
		try {
			log.debug("转换请求参数为ContactListMsgRestVo对象...");
			restVo = toContactListMsgRespVo(params);

			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(), restVo.getCustId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}

			log.debug("校验请求参数开始...");
			String result = checkDetailValue(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			// 根据ID查询联系人信息
			MrsContactListDto mrsContactDto = mrsContactListService.selectByPrimaryKey(restVo.getContactId());
			if (mrsContactDto == null) {
				log.error("根据ID{},查询联系人为空", restVo.getContactId());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("根据ID:" + restVo.getContactId() + "查询联系人为空！");
				return respVo;
			}
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			respVo.setCustId(mrsContactDto.getCustId());
			respVo.setContactId(mrsContactDto.getId());
			respVo.setCertType(mrsContactDto.getCertType());
			respVo.setCertNo(mrsContactDto.getCertNo());
			respVo.setName(mrsContactDto.getName());
			respVo.setPhoneNo(mrsContactDto.getPhoneNo());
			respVo.setMobill(mrsContactDto.getMobill());
			respVo.setAddress(mrsContactDto.getAddress());
			respVo.setEmail(mrsContactDto.getEmail());
			respVo.setSource(mrsContactDto.getSource());
			respVo.setStatus(mrsContactDto.getStatus());
			// respVo.setPrivoiceCode();
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("查询联系人信息成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("查询联系人信息失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("ID：%s,查询联系人信息失败:%s", restVo.getContactId(), e.getMessage()));
			return respVo;
		}
		return respVo;
	}

	/**
	 * @接口描述
	 * 根据微信号查询用户信息接口(微信号登陆)
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public LoginMsgSearchResponseVO findUserByWeChatId(String params) {

		log.info("开始执行查询用户信息方法，service:" + params);
		LoginMsgSearchRequestVO restVo = new LoginMsgSearchRequestVO();
		LoginMsgSearchResponseVO respVo = new LoginMsgSearchResponseVO();
		try {
			log.debug("转换请求参数为LoginMsgSearchRequestVO对象...");
			restVo = toLoginUserDtoMsgRespVo(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getWeChatId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}
			
			log.debug("开始校验请求参数...");
			String result = checkweChatValue(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			//微信号查询用户信息
			MrsLoginUserDto mrsLoginUserDto = mrsLoginUserService.findLoginUserDtoByWeChatId(restVo.getWeChatId());
			//微信号查询一户通信息
			List<MrsAccountDto> mrsAccountDtos = mrsAccountService.findByWeChatId(restVo.getWeChatId());
			if (mrsLoginUserDto == null) {
				log.error("根据微信号查询用户信息为空！");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("根据微信号["+restVo.getWeChatId()+"]查询用户信息为空！");
				return respVo;
			}
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			respVo.setCustId(mrsAccountDtos.get(0).getCustId());
			respVo.setContactId(mrsLoginUserDto.getId());
			respVo.setAccountName(mrsAccountDtos.get(0).getAccountName());
			respVo.setAccountType(mrsAccountDtos.get(0).getAccountType().getDisplayName());
			respVo.setCustomerName(mrsAccountDtos.get(0).getCustomerName());
			respVo.setAccountSource(mrsAccountDtos.get(0).getAccountSource());
			respVo.setAuthStatus(mrsAccountDtos.get(0).getAuthStatus());
			respVo.setPlatformCode(mrsAccountDtos.get(0).getPlatformCode());
			respVo.setAlias(mrsLoginUserDto.getAlias());
			respVo.setMobile(mrsLoginUserDto.getMobile());
			respVo.setEmail(mrsLoginUserDto.getEmail());
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("查询用户信息成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("微信号登陆失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,微信号登陆失败:%s", restVo.getAccountCode(), e.getMessage()));
			return respVo;
		}
		return respVo;
	}

	/**
	 * @接口描述
	 * 根据微信号查询一户通信息接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public List<Object> findMrsAccountByWeChatId(String params) {
		log.info("开始执行查询一户通信息方法，service:" + params);
		LoginMsgSearchRequestVO restVo = new LoginMsgSearchRequestVO();
//		LoginMsgSearchResponseVO respVo = new LoginMsgSearchResponseVO();
		AccountMsgResponseVO respVo = new AccountMsgResponseVO();
		BaseRespVO baseVo = new BaseRespVO();
		List<Object> respList = new ArrayList<Object>();
		try {
			log.debug("转换请求参数为LoginMsgSearchRequestVO对象...");
			restVo = toLoginUserDtoMsgRespVo(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getWeChatId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				baseVo.setMsgCode(PortalCode.CODE_9999);
				baseVo.setMsgInfo(checkToken);
				respList.add(baseVo);
				return respList;
			}
			
			log.debug("开始校验请求参数...");
			String result = checkweChatValue(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				baseVo.setMsgCode(PortalCode.CODE_9999);
				baseVo.setMsgInfo(result);
				respList.add(baseVo);
				return respList;
			}
			// 根据微信号查询一户通信息
			List<MrsAccountDto> mrsAccountDtos = mrsAccountService.findByWeChatId(restVo.getWeChatId());
			//微信号查询用户信息
			MrsLoginUserDto mrsloginDto = mrsLoginUserService.findLoginUserDtoByWeChatId(restVo.getWeChatId());
			if (CollectionUtil.isEmpty(mrsAccountDtos)) {
				log.error("根据微信号查询一户通信息为空！");
				baseVo.setMsgCode(PortalCode.CODE_9999);
				baseVo.setMsgInfo("根据微信号["+restVo.getWeChatId()+"]查询一户通信息为空！");
				respList.add(baseVo);
				return respList;
			}
			// 处理成功
			baseVo.setMsgCode(PortalCode.CODE_0000);
			baseVo.setMsgInfo("处理成功");
			respList.add(baseVo);
			for(MrsAccountDto mrsAccountDto:mrsAccountDtos){
				respVo = new AccountMsgResponseVO();
				respVo.setCustId(restVo.getAccountCode());
				respVo.setContactId(mrsloginDto.getId());
				respVo.setAlias(mrsloginDto.getAlias());
				respVo.setMobile(mrsloginDto.getMobile());
				respVo.setEmail(mrsloginDto.getEmail());
				respVo.setUserStatus(mrsloginDto.getUserStatus());
				respVo.setLoginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mrsloginDto.getLoginTime()));
				respVo.setCustomerType(mrsAccountDto.getCustomerType());
				respVo.setCustomerName(mrsAccountDto.getCustomerName());
				respVo.setAccountStatus(mrsAccountDto.getAccountStatus());
				respVo.setPlatformCode(mrsAccountDto.getPlatformCode());
				respVo.setAccountType(mrsAccountDto.getAccountType().getValue());
				respVo.setAccountName(mrsAccountDto.getAccountName());
				respVo.setAccountSource(mrsAccountDto.getAccountSource());
				respVo.setAuthStatus(mrsAccountDto.getAuthStatus());
				respList.add(respVo);
			}
//			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("查询一户通信息成功,返回json对象：{}", respList);
		} catch (Exception e) {
			log.error("一户通信息查询失败：" + e.getMessage());
			baseVo.setMsgCode(PortalCode.CODE_9999);
			baseVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,一户通信息查询失败:%s", restVo.getAccountCode(), e.getMessage()));
			respList.add(baseVo);
			return respList;
		}
		return respList;
	}

	/**
	 * 根据微信号查询一户通状态、子账户状态 (投保人子账户状态)
	 */
	@Override
	public LoginMsgSearchResponseVO findMrsAccountStatusByWeChatId(String params) {
		log.info("开始执行查询一户通账户状态、子账户状态方法，service:" + params);
		LoginMsgSearchRequestVO restVo = new LoginMsgSearchRequestVO();
		LoginMsgSearchResponseVO respVo = new LoginMsgSearchResponseVO();
		try {
			log.debug("转换请求参数为LoginMsgSearchRequestVO对象...");
			restVo = toLoginUserDtoMsgRespVo(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getWeChatId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}
			
			log.debug("开始校验请求参数...");
			String result = checkweChatValue(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			// 根据微信号查询一户通信息
			List<MrsAccountDto> mrsAccountDtos = mrsAccountService.findByWeChatId(restVo.getWeChatId());
			if (CollectionUtil.isEmpty(mrsAccountDtos)) {
				log.error("根据微信号查询一户通信息为空！");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("根据微信号["+restVo.getWeChatId()+"]查询一户通信息为空！");
				return respVo;
			}
			// 根据一户通账号查询子账户状态(投保子账户状态)
			MrsSubAccountDto mrsSubAccountDto = mrsSubAccountService
					.findSubAccountStatusByCustIdAndType(mrsAccountDtos.get(0).getCustId());
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			respVo.setAccountStatus(mrsAccountDtos.get(0).getAccountStatus());
			respVo.setSubAccountStatus(mrsSubAccountDto.getSubAccountStatus());
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("查询一户通账户状态、子账户状态成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("一户通信息查询失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通号：%s,一户通状态、子账户状态查询失败:%s", restVo.getAccountCode(), e.getMessage()));
			return respVo;
		}
		return respVo;
	}

	/**
	 * 将请求参数转换成对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private LoginMsgSearchRequestVO toLoginUserDtoMsgRespVo(String params) throws Exception {
		LoginMsgSearchRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, LoginMsgSearchRequestVO.class);
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
	 * 校验微信号、一户通账号
	 * 
	 * @param restVo
	 * @return
	 */
	private String checkWechatIdandCustId(LoginMsgSearchRequestVO restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getWeChatId())||restVo.getWeChatId().length() > 32) {
			return "微信号为空或微信号超长";
		}
		if (StringUtil.isEmpty(restVo.getAccountCode())) {
			return "一互通账号为空";
		}
		// if(StringUtil.isEmpty(restVo.getLoginPwd())){
		// return "登陆密码为空";
		// }
		return null;
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
		dto.setType(EBisExceptionLogType.PORTAL_REST);
		dto.setContent(errorMsg);
		dto.setAllpath(CbsConfig.getLogFullPath());
		try {
			bisExceptionLogAppService.saveLog(dto);
		} catch (Exception e) {
			log.error("操作异常,记录异常日志失败！");
		}
	}

	/**
	 * @接口描述
	 * 微信号、一户通账号绑定接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public BaseRespVO bindingWeChatIdandCustId(String params) {
		log.info("开始执行微信号、一户通账号绑定方法...");
		LoginMsgSearchRequestVO restVo = new LoginMsgSearchRequestVO();
		BaseRespVO respVo = new BaseRespVO();
		MrsAccountDto mrsAccountDto = null;
		try {
			log.debug("转换请求参数为LoginMsgSearchRequestVO对象...");
			restVo = toLoginUserDtoMsgRespVo(params);
            
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getAccountCode());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}
			
			log.debug("开始校验请求参数...");
			String result = checkWechatIdandCustId(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			//根据一户通账号查询一户通信息
			mrsAccountDto = mrsAccountService.findByCustId(restVo.getAccountCode());
			if(mrsAccountDto == null){
				log.error("根据一户通账户:{}查询一互通信息为空！",restVo.getAccountCode());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("根据一户通账户["+restVo.getAccountCode()+"]查询一互通信息为空！");
				return respVo;
			}
			List<MrsAccountDto> mrsAccountDtos = mrsAccountService.findByWeChatId(restVo.getWeChatId());
			MrsLoginUserDto mrsLoginUserDto = mrsLoginUserService.findLoginUserDtoByWeChatId(restVo.getWeChatId());
			// 该微信号未使用且未绑定一户通
			if (mrsLoginUserDto == null && CollectionUtil.isEmpty(mrsAccountDtos)) {
				mrsAccountDto = mrsAccountService.findByCustId(restVo.getAccountCode());
				if (mrsAccountDto != null) {
					// 查询登陆用户关联表
					List<MrsUserAccountDto> mrsUserAccountDtos = mrsUserAccountDtoMapper
							.findByIsAccountId(mrsAccountDto.getId());
					if (!CollectionUtil.isEmpty(mrsUserAccountDtos)) {
						mrsLoginUserDto = new MrsLoginUserDto();
						mrsLoginUserDto.setId(mrsUserAccountDtos.get(0).getLoginId());
						mrsLoginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
						mrsLoginUserDto.setWeChatId(restVo.getWeChatId());
						mrsLoginUserDto.setLoginPwd(restVo.getLoginPwd());
						mrsLoginUserDto.setCreateTime(new Date());
						mrsLoginUserService.updateLoginUserDto(mrsLoginUserDto);
					}
				} else {
					log.error("根据一户通custId:" + restVo.getAccountCode() + "查询一户通信息为空！");
					respVo.setMsgCode(PortalCode.CODE_9999);
					respVo.setMsgInfo("根据一户通custId:" + restVo.getAccountCode() + "查询一户通信息为空！");
					return respVo;
				}
			}

			// 若用户未绑定微信号、一户通账号已存在
			// if(mrsLoginUserDto==null&&mrsAccountDto!=null){
			// mrsLoginUserDto = new MrsLoginUserDto();
			// mrsLoginUserDto.setId(UUID.randomUUID().toString());
			// mrsLoginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
			// mrsLoginUserDto.setWeChatId(restVo.getWeChatId());
			// mrsLoginUserDto.setLoginPwd(restVo.getLoginPwd());
			// mrsLoginUserDto.setCreateTime(new Date());
			// mrsLoginUserService.insertSelective(mrsLoginUserDto);
			// }
			// 该微信号已存在且属于该一户通用户
			if (mrsLoginUserDto != null && !CollectionUtil.isEmpty(mrsAccountDtos)) {
				mrsLoginUserDto.setUpdateTime(new Date());
				mrsLoginUserService.updateLoginUserDto(mrsLoginUserDto);
			}
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("微信号、一户通账号绑定成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("微信号、一户通账号绑定失败！");
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("微信号、一户通账号绑定失败！");
			return respVo;
		}
		return respVo;
	}

	/**
	 * @接口描述
	 * 个人客户注册接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public BaseRespVO doRegisterPersonDto(String params) {
		log.info("开始执行个人客户注册方法，service:" + params);
		LoginMsgSearchRequestVO restVo = new LoginMsgSearchRequestVO();
		BaseRespVO respVo = new BaseRespVO();
		MrsLoginUserDto mrsLoginUserDto = null;
		try {
			log.debug("转换请求参数为LoginMsgSearchRequestVO对象...");
			restVo = toLoginMsgSearchRequestVO(params);
			log.debug("开始校验请求参数...");
			String result = checkValues(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			//校验昵称是否存在
			if(StringUtils.isNotBlank(restVo.getAlias())){
				mrsLoginUserDto = mrsLoginUserService.findByAlias(restVo.getAlias());
				if(mrsLoginUserDto!=null){
					log.error("该昵称：{}已存在！",restVo.getAlias());
					respVo.setMsgCode(PortalCode.CODE_9999);
					respVo.setMsgInfo("该昵称["+restVo.getAlias()+"]已存在！");
					return respVo;
				}
			}
			
			//校验手机号是否存在
			mrsLoginUserDto = mrsLoginUserService.findByMobile(restVo.getMobile());
			if(mrsLoginUserDto!=null){
				log.error("该手机号：{}已存在！",restVo.getMobile());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("该手机号["+restVo.getMobile()+"]已存在！");
				return respVo;
			}
			mrsLoginUserDto = createLoginUserDto(restVo);
			// 保存用户记录
			mrsLoginUserService.insertSelective(mrsLoginUserDto);
			// 处理成功
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("处理成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("用户注册成功,返回json对象：{}", jsons.toString());
		} catch (Exception e) {
			log.error("用户注册失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("昵称：%s,用户注册失败:%s", restVo.getAlias(), e.getMessage()));
			return respVo;
		}
		return respVo;
	}

	/**
	 * @接口描述
	 * 个人客户实名认证开户接口
	 * @创建时间：20170515
	 * @author pst19
	 */
	@Override
	public BaseRespVO doRealRegister(String params) {
		log.info("开始执行个人实名认证开户方法...");
		PersonVO restVo = new PersonVO();
		BaseRespVO respVo = new BaseRespVO();
		try {
			log.debug("转换请求参数为PersonVo对象...");
			restVo = toPersonVO(params);
			
//			// 检查登录Token
//			String checkToken = checkLoginTokenParams(restVo.getLoginToken());
//			if (checkToken != null) {
//				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
//				respVo.setMsgCode(PortalCode.CODE_9999);
//				respVo.setMsgInfo(checkToken);
//				return respVo;
//			}
			
			log.debug("开始校验请求参数...");
			String result = checkRegisterValues(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			// uploadFiles(restVo);
			// 根据id查询登陆用户信息
			MrsLoginUserDto mrsLoginUserDto = mrsLoginUserService.selectByPrimaryKey(restVo.getLoginUserId());
			if (mrsLoginUserDto == null) {
				log.error("根据loginUserId:" + restVo.getLoginUserId() + "查询用户信息为空！");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("根据loginUserId:" + restVo.getLoginUserId() + "查询用户信息为空！");
				return respVo;
			}
			// 保存个人客户基本信息
			BaseRespVO vo = uploadFiles(restVo.getUploadFiles());
			if (!vo.getMsgCode().equals(PortalCode.CODE_0000)) {
				log.error("附件上传失败！");
				respVo.setMsgCode(vo.getMsgCode());
				respVo.setMsgInfo(vo.getMsgInfo());
				return respVo;
			}
			PersonVO pensonVO = mrsPersonService.saveAduitPersonByPortal(restVo, mrsLoginUserDto);
			if (pensonVO.getCheckVo().isCheckValue()) {
				// 处理成功
				respVo.setMsgCode(PortalCode.CODE_0000);
				respVo.setMsgInfo("处理成功");
				JSONObject jsons = JSONObject.fromObject(respVo);
				log.info("个人实名认证成功,返回json对象：{}", jsons.toString());
			}
		} catch (Exception e) {
			log.error("个人实名认证失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("个人实名认证失败:%s", e.getMessage()));
			return respVo;
		}
		return respVo;
	}

	/**
	 * 开通一户通账户、资金账户
	 */
	@Override
	public AccountVO createAccountandActAccount(String params) {
		log.info("开始执行开通一户通账户、资金账户方法...");
		PersonVO restVo = new PersonVO();
		AccountVO respVo = new AccountVO();
		try {
			log.debug("转换请求参数为PersonVo对象...");
			restVo = toPersonVO(params);
			
			// 检查登录Token
			String checkToken = checkLoginTokenParams(restVo.getLoginToken(),restVo.getLoginUserId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}
			log.debug("开始校验请求参数...");
			String result = checkRegisterValues(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			MrsLoginUserDto mrsLoginUserDto = mrsLoginUserService.findById(restVo.getLoginUserId());
			if(mrsLoginUserDto == null){
				log.error("根据ID:{}查询用户信息为空！",restVo.getLoginUserId());
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("根据ID["+restVo.getLoginUserId()+"]查询用户信息为空！");
				return respVo;
			}
			PersonVO responVo = mrsPersonService.directlyOpenAccount(restVo);
			if (responVo.getCheckVo().isCheckValue()) {
				// 处理成功
				respVo.setMsgCode(PortalCode.CODE_0000);
				respVo.setMsgInfo("处理成功");
				respVo.setCustId(responVo.getCustId());
				JSONObject jsons = JSONObject.fromObject(respVo);
				log.info("一户通账户、资金账户开通成功,返回json对象：{}", jsons.toString());
			} else {
				// 处理失败
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(responVo.getCheckVo().getMsg());
				JSONObject jsons = JSONObject.fromObject(respVo);
				log.info("一户通账户、资金账户开通失败,返回json对象：{}", jsons.toString());
			}
		} catch (Exception e) {
			log.error("一户通账户、资金账户开通失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("一户通账户、资金账户开通失败:%s", e.getMessage()));
			return respVo;
		}
		return respVo;
	}

	/**
	 * 将请求参数转换成对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private LoginMsgSearchRequestVO toLoginMsgSearchRequestVO(String params) throws Exception {
		LoginMsgSearchRequestVO requestVo = null;
		try {
			requestVo = getObjectBean(params, LoginMsgSearchRequestVO.class);
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
	 * 将请求参数转换成对象
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private PersonVO toPersonVO(String params) throws Exception {
		PersonVO requestVo = null;
		try {
			requestVo = getObjectBean(params, PersonVO.class);
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

	/**
	 * 校验请求参数
	 * 
	 * @return
	 */
	private String checkValues(LoginMsgSearchRequestVO requestVo) {
		if (requestVo == null) {
			return "请求参数为空";
		}
		if(requestVo.getAlias().length() > 32){
			return "用户昵称超长";
		}
		if(StringUtil.isEmpty(requestVo.getMobile())||requestVo.getMobile().length() > 11){
			return "用户手机号为空或手机号超长";
		}
		if(!Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(requestVo.getMobile()).matches()){
			return "手机号格式不正确";
		}
		if(StringUtil.isEmpty(requestVo.getLoginPwd())||requestVo.getLoginPwd().length() > 36){
			return "用户登陆密码为空或密码超长";
		}
		return null;
	}
    
	/**
     * 微信号登陆查询相关参数校验
     * @return
     */
	private String checkweChatValue(LoginMsgSearchRequestVO requestVo){
		if (requestVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(requestVo.getWeChatId())) {
			return "微信号为空";
		}
		return null;
	}
	
	/**
	 * 个人实名认证开户
	 * 
	 * @param vo
	 * @return
	 */
	private String checkRegisterValues(PersonVO restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getCustomerName()) || restVo.getCustomerName().length() > 80) {
			return "客户名称为空或超长";
		}
		if (StringUtil.isEmpty(restVo.getCredentialsType()) || restVo.getCredentialsType().length() > 2) {
			return "证件类型为空或超长";
		}
		MrsCredentialsType creType = MrsCredentialsType.getEnum(restVo.getCredentialsType());
		if(creType == null){
			return "证件类型不存在";
		}
		if (StringUtil.isEmpty(restVo.getCredentialsNumber()) || restVo.getCredentialsNumber().length() > 32) {
			return "证件号码为空或超长";
		}
		if(!Pattern.compile(RegexEnum.REGEX_CH_EH_NUM0_32.getRegexValue()).matcher(restVo.getCredentialsNumber()).matches()){
			return "证件号码格式不正确";
		}
		if (StringUtil.isEmpty(restVo.getLoginUserId())) {
			return "登陆用户主键为空";
		}
		if (!StringUtil.isEmpty(restVo.getSexCode())) {
			if (restVo.getSexCode().length() > 1) {
				return "性别长度超长";
			}
			if(MrsSexCode.getEnum(restVo.getSexCode())==null){
				return "性别类型不存在";
			}
		}
		if (!StringUtil.isEmpty(restVo.getBirthdate())) {
			if (restVo.getBirthdate().length() > 8) {
				return "出生日期超长";
			}
		}
		if (!StringUtil.isEmpty(restVo.getEducationCode())) {
			if (restVo.getEducationCode().length() > 2) {
				return "学历长度长超";
			}
			if(MrsEducationCode.getEnum(restVo.getEducationCode())==null){
				return "学历类型不存在";
			}
		}
		if (!StringUtil.isEmpty(restVo.getNationalCode())) {
			if (restVo.getNationalCode().length() > 2) {
				return "民族长度长超";
			}
			if(MrsNationalCode.getEnum(restVo.getNationalCode())==null){
				return "民族类型不存在";
			}
		}
		if (!StringUtil.isEmpty(restVo.getMobile())) {
			if (restVo.getMobile().length() > 11) {
				return "手机号长度长超";
			}
			if(!Pattern.compile(RegexEnum.REWGEX_TEL.getRegexValue()).matcher(restVo.getCredentialsNumber()).matches()){
				return "手机号码格式不正确";
			}
		}
		if (!StringUtil.isEmpty(restVo.getTel())) {
			if (restVo.getTel().length() > 16) {
				return "电话长度长超";
			}
		}
		if (!StringUtil.isEmpty(restVo.getZipCode())) {
			if (restVo.getZipCode().length() > 6) {
				return "邮编长度长超";
			}
			if(!Pattern.compile(RegexEnum.REGEX_POST.getRegexValue()).matcher(restVo.getZipCode()).matches()){
				return "邮编格式不正确";
			}
		}
		if(!StringUtil.isEmpty(restVo.getNationalityCode())){
			if(restVo.getNationalityCode().length() > 3){
				return "国籍或地区超长";
			}
			if(MrsNationaltyCode.getEnum(restVo.getNationalityCode())==null){
				return "国籍或地区类型不存在";
			}
		}
		if(!StringUtil.isEmpty(restVo.getContactAddr())){
			if(restVo.getContactAddr().length() > 80){
				return "联系地址超长";
			}
		}
		return null;
	}

	/**
	 * 校验上传附件请求参数
	 * 
	 * @param restVo
	 * @return
	 */
	private String checkUploadFilesParams(PersonVO restVo) {
		if (restVo == null) {
			return "请求参数为空";
		}
		if (StringUtil.isEmpty(restVo.getFileName())) {
			return "文件名为空";
		}
		if (StringUtil.isEmpty(restVo.getFileType())) {
			return "文件类型为空";
		}
		if (StringUtil.isEmpty(restVo.getUploadFiles())) {
			return "附件信息为空";
		}
		return null;
	}

	/**
	 * 创建用户信息
	 * 
	 * @return
	 */
	private MrsLoginUserDto createLoginUserDto(LoginMsgSearchRequestVO restVo) {
		MrsLoginUserDto mrsLonginDto = new MrsLoginUserDto();
		mrsLonginDto.setId(UUID.randomUUID().toString());
		mrsLonginDto.setAlias(restVo.getAlias());
		mrsLonginDto.setMobile(restVo.getMobile());
		mrsLonginDto.setEmail(restVo.getEmail());
		mrsLonginDto.setLoginPwd(MD5Utils.MD5(restVo.getLoginPwd() + SHIEConfigConstant.SALT));
		mrsLonginDto.setPayPwd(restVo.getPayPwd());
		mrsLonginDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
		mrsLonginDto.setWeChatId(restVo.getWeChatId());
		mrsLonginDto.setCreateTime(new Date());
		mrsLonginDto.setRegisterType(MrsCustType.MRS_CUST_TYPE_01.getValue());
		return mrsLonginDto;
	}

	/**
	 * 数据上传
	 * 
	 * @param url
	 * @param userCode
	 * @param custId
	 * @param file
	 * @return
	 * @throws CbsCheckedException
	 */
	private String uploadByFileServer(String url, String userCode, String systemName, String busiType, File file)
			throws CbsCheckedException {
		try {
			return FileServerUtil.upload(url, file, userCode, systemName, busiType);
		} catch (Exception e) {
			log.error("文件服务器错误，数据上传失败", e);
			throw new CbsCheckedException("文件服务器错误，数据上传失败");
		}
	}

	/**
	 * 文件上传 (用于手机端开户上传附件)
	 */
	@Override
	public BaseRespVO uploadFiles(String params) {
		log.info("开始执行附件上传方法...");
		BaseRespVO respVo = new BaseRespVO();
		PersonVO restVo = new PersonVO();
		try {
			log.debug("转换请求参数为PersonVo对象...");
			restVo = toPersonVO(params);
			log.debug("开始校验请求参数...");
			String result = checkUploadFilesParams(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			// 文件服务器参数
			String url = bisSysParamAppService.getValue(SystemParamConstants.FILE_SERVER_URL);
			String userCode = bisSysParamAppService.getValue(SystemParamConstants.FILE_SERVER_USER_CODE);
			String systemName = bisSysParamAppService.getValue(SystemParamConstants.FILE_SERVER_SYSTEM_NAME);
			String busiType = bisSysParamAppService.getValue(SystemParamConstants.FILE_SERVER_BUSI_TYPE);
			// // 校验文件名称、类型是否存在
			// String result = checkUploadFilesParams(restVo);
			// 若校验结果不为空则处理失败
			if (!StringUtil.isEmpty(result)) {
				log.error(result);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(result);
				return respVo;
			}
			// 将字符串还原成file文件上传至文件服务器
			Base64 decoder = new Base64();

			byte[] datas = decoder.decode(restVo.getUploadFiles());
			for (int i = 0; i < datas.length; i++) {
				if (datas[i] < 0) {
					datas[i] += 256;
				}
			}
			String newPath = PERSON_UPLOAD;
			// 文件临时保存目录
			File file = new File(newPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			if (!newPath.endsWith("/")) {
				newPath += "/" + restVo.getFileName() + "." + restVo.getFileType();
			} else {
				newPath += restVo.getFileName() + "." + restVo.getFileType();
			}
			OutputStream out = new FileOutputStream(newPath);
			out.write(datas);
			out.flush();
			out.close();
			// 组装文件
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDtoList = new ArrayList<MrsAduitAttachmentDto>();
			file = new File(newPath);
			File[] files = file.listFiles();
			if (!(files == null || files.length == 0)) {
				for (File f : files) {
					String fileName = f.getName();
					String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
					String fileServerId = uploadByFileServer(url, userCode, systemName, busiType, f);
					if (fileServerId != null) {
						MrsAduitAttachmentDto fileDto = new MrsAduitAttachmentDto();
						fileDto.setName(f.getName());
						fileDto.setStoragePath(fileServerId);
						if (org.apache.commons.lang.StringUtils.isNotBlank(restVo.getCredentialsType())) {
							fileDto.setCertType(MrsCredentialsType.getEnum(restVo.getCredentialsType()).getValue());
						}
						fileDto.setCertNo(restVo.getCredentialsNumber());
						if (org.apache.commons.lang.StringUtils.isNotBlank(suffix.toLowerCase())) {
							fileDto.setSuffix(FileTypeEnum.getEnum(suffix.toLowerCase()).getValue());
						}
						fileDto.setFileSize(f.length());
						mrsAduitAttachmentDtoList.add(fileDto);
					} else {
						log.error("文件服务器错误，数据上传失败");
						respVo.setMsgCode(PortalCode.CODE_9999);
						respVo.setMsgInfo("文件服务器错误，数据上传失败");
						return respVo;
					}
				}
				restVo.setMrsAduitAttachmentDto(mrsAduitAttachmentDtoList);
			}
		} catch (Exception e) {
			log.error("文件服务器错误，数据上传失败");
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("文件服务器错误，数据上传失败");
			return respVo;
		}
		// 处理成功
		respVo.setMsgCode(PortalCode.CODE_0000);
		respVo.setMsgInfo("处理成功");
		return respVo;
	}
	
	@Override
	public BaseRespVO dosetinfo(String params) {
		log.info("设置密码和手机接口 ,请求参数:{}", params);
		SetPwdRequestVO reqVO = new SetPwdRequestVO();
		BaseRespVO respVo = new BaseRespVO();
		try {
			// 将请求Gson转换为对象
			reqVO = toSetPwdRequestVO(params);
			// 检查登录Token
			String checkToken = checkLoginTokenParams(reqVO.getLoginToken(), reqVO.getLoginUserId());
			if (checkToken != null) {
				log.error("参数登录Token[" + params + "]校验失败:" + checkToken);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkToken);
				return respVo;
			}

			// 解析参数封装成request对象
			String checkResult = checkSetPwdParams(reqVO);
			if (checkResult != null) {
				log.error("参数[" + params + "]校验失败:" + checkResult);
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo(checkResult);
				return respVo;
			}
			// 处理
			UserCheckVO result = mrsLoginUserService.updateLoginPwdByMobile(reqVO.getLoginUserId(), reqVO.getOldpwd(),
					reqVO.getNewpwd(), reqVO.getMobile());
			if (result != null) {
				if (!result.isCheckValue()) {
					log.error(result.getMsg());
					respVo.setMsgCode(PortalCode.CODE_9999);
					respVo.setMsgInfo(result.getMsg());
					return respVo;
				}
			} else {
				log.error("设置密码和手机失败");
				respVo.setMsgCode(PortalCode.CODE_9999);
				respVo.setMsgInfo("设置密码和手机失败");
				return respVo;
			}

			// 返回
			respVo.setMsgCode(PortalCode.CODE_0000);
			respVo.setMsgInfo("成功");
			JSONObject jsons = JSONObject.fromObject(respVo);
			log.info("设置密码和手机接口完成,返回json对象：{}", jsons.toString());
			return respVo;
		} catch (CodeCheckedException e) {
			log.error("设置密码和手机接口失败：" + e.getMessage());
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo(e.getMessage());
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		} catch (Exception e) {
			log.error("设置密码和手机接口失败：" + ExceptionProcUtil.getExceptionDesc(e));
			respVo.setMsgCode(PortalCode.CODE_9999);
			respVo.setMsgInfo("设置密码和手机失败");
			saveErrorExcetpionLog(String.format("登录用户主键Key：%s", reqVO.getLoginUserId(), e.getMessage()));
			return respVo;
		}
	}
	
}
