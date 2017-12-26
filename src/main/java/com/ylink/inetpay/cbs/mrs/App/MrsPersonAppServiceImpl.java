package com.ylink.inetpay.cbs.mrs.App;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;
import org.ylinkpay.framework.core.util.MD5Utils;

import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.cbs.mrs.service.MrsCertAuditService;
import com.ylink.inetpay.cbs.mrs.service.MrsDataAuditChangeService;
import com.ylink.inetpay.cbs.mrs.service.MrsDataParamService;
import com.ylink.inetpay.cbs.mrs.service.MrsLoginUserService;
import com.ylink.inetpay.cbs.mrs.service.MrsPersonService;
import com.ylink.inetpay.common.core.constant.EAcntType;
import com.ylink.inetpay.common.core.constant.EAuditStatus;
import com.ylink.inetpay.common.core.constant.EChlReturnCode;
import com.ylink.inetpay.common.core.constant.EIdCardType;
import com.ylink.inetpay.common.core.constant.EIsVali;
import com.ylink.inetpay.common.core.constant.EReqType;
import com.ylink.inetpay.common.core.constant.ESHIECardType;
import com.ylink.inetpay.common.core.constant.EYesNo;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.core.constant.MrsDataParamType;
import com.ylink.inetpay.common.core.constant.MrsNationaltyCode;
import com.ylink.inetpay.common.core.util.JsonUtil;
import com.ylink.inetpay.common.project.cbs.app.MrsPersonAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataAuditChangeDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataParamDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPersonDto;
import com.ylink.inetpay.common.project.cbs.vo.bis.ResponseMessage;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsActAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsPersonVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsRemoveAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;
import com.ylink.inetpay.common.project.channel.app.IdentifVerifAppService;
import com.ylink.inetpay.common.project.channel.dto.request.IdentityPojo;
import com.ylink.inetpay.common.project.channel.dto.request.ShieIdentityPojo;
import com.ylink.inetpay.common.project.channel.dto.response.BaseRespPojo;
import com.ylink.inetpay.common.project.portal.dto.AccountMsg;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
import com.ylink.inetpay.common.project.portal.vo.UploadPersonPojo;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.customer.BankBusiReqVO;
import com.ylink.inetpay.common.project.portal.vo.customer.DataParamVO;
import com.ylink.inetpay.common.project.portal.vo.customer.PersonVO;

@Service("mrsPersonAppService")
public class MrsPersonAppServiceImpl implements MrsPersonAppService {

	private static Logger log = LoggerFactory.getLogger(MrsPersonAppServiceImpl.class);
	@Autowired
	private MrsPersonService mrsPersonService;
	@Autowired
	private MrsDataParamService mrsDataParamService;
	@Autowired
	private MrsCertAuditService mrsCertAuditService;
	@Autowired
	private IdentifVerifAppService identifVerifAppService;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private MrsLoginUserService mrsLoginUserService;
	@Autowired
	private MrsDataAuditChangeService mrsDataAuditChangeService;
	
	@Override
	public PageData<MrsPersonDto> findPerson(PageData<MrsPersonDto> pageData, MrsPersonDto searchDto) {
		log.debug("MrsPersonAppServiceImpl.findPerson run....");
		return mrsPersonService.findPerson(pageData, searchDto);
	}
	public PageData<MrsPersonDto> findPersonByUpdateAudit(PageData<MrsPersonDto> pageData, MrsPersonDto searchDto) {
		log.debug("MrsPersonAppServiceImpl.findPersonByUpdateAudit run....");
		return mrsPersonService.findPersonByUpdateAudit(pageData, searchDto);
	}
	@Override
	public MrsPersonDto findById(String id) {
		log.debug("MrsPersonAppServiceImpl.findById run....");
		return mrsPersonService.findById(id);
	}

	@Override
	public MrsPersonDto findByCustId(String custId) {
		log.debug("MrsPersonAppServiceImpl.findByCustId run....");
		return mrsPersonService.findByCustId(custId);
	}

	@Override
	public void update(MrsPersonDto dto) {
		log.debug("MrsPersonAppServiceImpl.update run....");
		mrsPersonService.update(dto);		
	}
	@Override
	public void updatePerson(MrsPersonDto dto,String loginName) {
		log.debug("MrsPersonAppServiceImpl.updatePerson run....");
		mrsPersonService.updatePerson(dto,loginName);		
	}
	@Override
	public List<MrsCertAuditDto> findCertByCustId(String custId) {
		log.debug("MrsPersonAppServiceImpl.findCertByCustId run....");
		return mrsCertAuditService.findByCustId(custId);
	}

	@Override
	public PageData<MrsDataParamDto> findDataParam(PageData<MrsDataParamDto> pageData, MrsDataParamDto queryParam) {
		log.debug("MrsPersonAppServiceImpl.findDataParam run....");
		return mrsDataParamService.findDataParam(pageData, queryParam);
	}

	@Override
	public MrsLoginUserDto restMobile(String custId, String mobile) {
		try {
			log.debug("MrsPersonAppServiceImpl.restMobile[2 arg...] run....");
			MrsLoginUserDto loginUser = mrsPersonService.restMobile(custId, mobile);
			if(loginUser == null) {
				log.error("密码错误,[custId="+custId+"]");
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
				MrsLoginUserDto retDto = new MrsLoginUserDto();
				retDto.setUserCheckVo(vo);
				return retDto;
			}
			// 将成功返回的登陆密码和支付密码置空
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		} catch (Exception e) {
			log.error("重置手机失败：", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.REST_MOBILE_FAIL );
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}
	
	@Override
	public MrsLoginUserDto restMobile(String type,String custId, String mobile, String loginPwd) {
		try {
			log.debug("MrsPersonAppServiceImpl.restMobile[3 arg...] run....");
			MrsLoginUserDto loginUser = mrsLoginUserService.getByCustIdLoginPwd(custId, loginPwd);;
			if(loginUser == null) {
				log.error("密码错误,[custId="+custId+"]");
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.LOGIN_PWD_ERROR);
				MrsLoginUserDto retDto = new MrsLoginUserDto();
				retDto.setUserCheckVo(vo);
				return retDto;
			}
			loginUser = mrsPersonService.restMobile(custId, mobile);
			if(!loginUser.getUserCheckVo().isCheckValue()) {
				log.error("修改失败:"+loginUser.getUserCheckVo().getMsg());
				return loginUser;
			}
			// 将成功返回的登陆密码和支付密码置空
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			loginUser.setMobile(mobile);
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = new ArrayList<AccountMsg>();
			if("2".equals(type)){
				accountMsgs = mrsAccountService.findOrganAccountMsgByLoginUserId(loginUser.getId());
			}else{
				accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			}
			
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		}  catch (Exception e) {
			log.error("重置手机失败：", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.REST_MOBILE_FAIL );
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}
	
	@Override
	public MrsLoginUserDto restNewMobile(String type,String logId, String mobile, String loginPwd) {
		try {
			log.debug("MrsPersonAppServiceImpl.restMobile[3 arg...] run....");
			MrsLoginUserDto loginUser = mrsLoginUserService.findById(logId);
			loginPwd = MD5Utils.MD5(loginPwd + SHIEConfigConstant.SALT);
			if(!loginPwd.equals(loginUser.getLoginPwd())) {
				log.error("密码错误,[loginId="+logId+"]");
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.LOGIN_PWD_ERROR);
				MrsLoginUserDto retDto = new MrsLoginUserDto();
				retDto.setUserCheckVo(vo);
				return retDto;
			}
			MrsLoginUserDto dto = mrsLoginUserService.findByMobile(mobile);
			if (dto != null) {
				log.error("手机已经存在[mobile = " + mobile + "]");
				dto = new MrsLoginUserDto();
				dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.MOBILE_EXIST));
				return dto;
			}
			loginUser.setMobile(mobile);
			mrsLoginUserService.updateDto(loginUser);
			// 将成功返回的登陆密码和支付密码置空
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = new ArrayList<AccountMsg>();
			if("2".equals(type)){
				accountMsgs = mrsAccountService.findOrganAccountMsgByLoginUserId(loginUser.getId());
			}else{
				accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			}
			
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		}  catch (Exception e) {
			log.error("重置手机失败：", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.REST_MOBILE_FAIL );
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}

	@Override
	public MrsLoginUserDto restEmail(String custId, String email, String loginPwd) {
		try {
			log.debug("MrsPersonAppServiceImpl.restEmail run....");
			MrsLoginUserDto loginUser = mrsLoginUserService.getByCustIdLoginPwd(custId, loginPwd);;
			if(loginUser == null) {
				log.error("密码错误,[custId="+custId+"]");
				UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
				MrsLoginUserDto retDto = new MrsLoginUserDto();
				retDto.setUserCheckVo(vo);
				return retDto;
			}
			MrsLoginUserDto dto = mrsPersonService.restEmail(custId, email);
			if(!dto.getUserCheckVo().isCheckValue()) {
				log.error("修改失败:"+dto.getUserCheckVo().getMsg());
				return dto;
			}
			// 将成功返回的登陆密码和支付密码置空
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			loginUser.setEmail(email);
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		} catch (Exception e) {
			log.error("重置邮件失败：", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.REST_EMAIL_FAIL );
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}

	
	@Override
	public MrsLoginUserDto restEmail(String custId, String email) {
		try {
			log.debug("MrsPersonAppServiceImpl.restEmail[2 args] run....");
			MrsLoginUserDto loginUser = mrsPersonService.restEmail(custId, email);
			if(!loginUser.getUserCheckVo().isCheckValue()) {
				log.error("修改失败:"+loginUser.getUserCheckVo().getMsg());
				return loginUser;
			}
			// 将成功返回的登陆密码和支付密码置空
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			loginUser.setEmail(email);
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			loginUser.setAccountMsgs(accountMsgs);
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			UserCheckVO vo = new UserCheckVO(true);
			loginUser.setUserCheckVo(vo);
			return loginUser;
		} catch (Exception e) {
			log.error("重置邮件失败：", e);
			UserCheckVO vo = new UserCheckVO(false, ErrorMsgEnum.REST_EMAIL_FAIL );
			MrsLoginUserDto retDto = new MrsLoginUserDto();
			retDto.setUserCheckVo(vo);
			return retDto;
		}
	}

	@Override
	public UserCheckVO bankCardAuth(String acntNo, String custId, EAcntType acntType) {
		try {
			log.debug("MrsPersonAppServiceImpl.identityAuth run....");
			MrsPersonDto personDto = mrsPersonService.findByCustId(custId);
			IdentityPojo pojo = new IdentityPojo();
			pojo.setIsVali(EIsVali.BYM);	// 不验密
			pojo.setAcntNo(acntNo);	// 卡号/账号
			pojo.setiDCardType(EIdCardType.SFC);  // 证件类型
			pojo.setiDCardNo(personDto.getCredentialsNumber());	// 证件号码
			pojo.setAcntName(personDto.getCustomerName());		// 客户姓名
			pojo.setAcntType(acntType);// 卡类型
			BaseRespPojo respPojo = identifVerifAppService.VerifIdentif(pojo);			
			if(EChlReturnCode.SUCCESS.equals(respPojo.getRetrunCode())){
				mrsAccountService.doModifyAuthStatus(MrsAccountAuthStatus.MAAS_2, custId);
				return new UserCheckVO(true);
			} else {
				log.error("验证失败："+respPojo.getErrorMsg());
				mrsAccountService.doModifyAuthStatus(MrsAccountAuthStatus.MAAS_3, custId);
				return new UserCheckVO(false, ErrorMsgEnum.IDENTITY_AUTH_FAIL);
			}
		} catch (Exception e) {
			log.error("身份认证失败：", e);
			return new UserCheckVO(false, ErrorMsgEnum.IDENTITY_AUTH_FAIL);
		}
	}
	
	@Override
	public MrsLoginUserDto gztAuth(String custId, String certNo, String customerName) {
		MrsLoginUserDto dto = null;
		try {
			log.debug("MrsPersonAppServiceImpl.gztAuth run....");
			ShieIdentityPojo pojo = new ShieIdentityPojo();
			pojo.setCertiCode(certNo);
			pojo.setiDCardType(ESHIECardType.SFC);
			pojo.setName(customerName);
			pojo.setReqType(EReqType.SINGLE);
			BaseRespPojo respPojo = identifVerifAppService.ShieVerifIdentif(pojo);
			UserCheckVO checkVo = null;
			if(respPojo == null){
				log.error("身份认证失败,返回信息为空!");
				mrsAccountService.doModifyAuthStatus(MrsAccountAuthStatus.MAAS_3, custId);
				checkVo = new UserCheckVO(false, ErrorMsgEnum.IDENTITY_AUTH_FAIL);
			} else {
				if(EChlReturnCode.SUCCESS.equals(respPojo.getRetrunCode())){
					mrsPersonService.authSuccess(custId);
					checkVo = new UserCheckVO(true);
				} else {
					log.error("身份认证失败："+respPojo.getErrorMsg());
					mrsAccountService.doModifyAuthStatus(MrsAccountAuthStatus.MAAS_3, custId);
					checkVo = new UserCheckVO(false, ErrorMsgEnum.IDENTITY_AUTH_FAIL);
				}
			}
			MrsLoginUserDto loginUser = mrsLoginUserService.selectByCustId(custId);
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			loginUser.setUserCheckVo(checkVo);
			return loginUser;
		} catch (PortalCheckedException e) {
			log.error("身份认证失败：", e);
			UserCheckVO checkVo = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(checkVo);
			return dto ;
		} catch (Exception e) {
			log.error("身份认证失败：", e);
			UserCheckVO checkVo = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(checkVo);
			return dto ;
		}
	}

	@Override
	public UserCheckVO portalUpdate(PersonVO vo) {
		try {
			log.debug("MrsPersonAppServiceImpl.portalUpdate run....");
			MrsPersonDto dto = mrsPersonService.findById(vo.getId());
			if(dto == null){
				log.error("客户MrsPersonDto[id = "+vo.getId()+"]信息不存在");
				UserCheckVO checkVo = new UserCheckVO(false, ErrorMsgEnum.USER_NULL);
				return checkVo;
			}
			String custId = dto.getCustId();
//			BeanUtils.copyProperties(vo, dto);
			dto.setSexCode(vo.getSexCode());
			dto.setBirthdate(vo.getBirthdate());
			dto.setNationalCode(vo.getNationalCode());
			dto.setEducationCode(vo.getEducationCode());
			MrsNationaltyCode nationalityCode = StringUtil.isEmpty(vo.getNationalityCode()) ? null : MrsNationaltyCode.getEnum(vo.getNationalityCode());
			dto.setNationalityCode(nationalityCode);
			dto.setCredentialsEnddate(vo.getCredentialsEnddate());
			dto.setTel(vo.getTel());
			dto.setSpareTel(vo.getSpareTel());
			dto.setZipCode(vo.getZipCode());
			dto.setContactAddr(vo.getContactAddr());
			dto.setCustId(custId);
			mrsPersonService.updateAndSync(dto);
			
			return new UserCheckVO(true);
		} catch (Exception e) {
			log.error("更新失败:", e);
			return new UserCheckVO(false, ErrorMsgEnum.UPDATE_FAIL);
		}
	}
	
	@Override
	public List<DataParamVO> findDataParamByType(MrsDataParamType typeEnum) {
		return mrsDataParamService.findByType(typeEnum);
	}
	
	

	@Override
	public PersonVO portalFindPersonByCustId(String custId) {
		PersonVO vo = null;
		try {
			vo = mrsPersonService.findPersonVoByCustId(custId);
			if(vo == null){
				log.error("客户[custId = "+custId+"]信息不存在");
				UserCheckVO checkVo = new UserCheckVO(false, ErrorMsgEnum.USER_NULL);
				vo = new PersonVO();
				vo.setCheckVo(checkVo);
				return vo;
			}
			vo.setCheckVo(new UserCheckVO(true));
			return vo;
		} catch (Exception e) {
			log.error("查询失败:", e);
			vo = new PersonVO();
			vo.setCheckVo(new UserCheckVO(false, ErrorMsgEnum.SEARCH_FAIL));
			return vo;
		}
	}

	@Override
	public MrsLoginUserDto uploadFile(String custId, List<UploadPersonPojo> uploadPojoList) {
		try {
			UserCheckVO insertCheckVo = mrsPersonService.uploadPojoList(custId, uploadPojoList);
			if(!insertCheckVo.isCheckValue()) {
				log.error("文件保存失败:", insertCheckVo.getMsg());
				MrsLoginUserDto dto = new MrsLoginUserDto();
				dto.setUserCheckVo(insertCheckVo);
				return dto ;
			}
			MrsLoginUserDto loginUser = mrsLoginUserService.selectByCustId(custId);
			loginUser.setLoginPwd("");
			loginUser.setPayPwd("");
			// 根据登陆信息获取账户信息
			List<AccountMsg> accountMsgs = mrsAccountService.findPersonAccountMsgByLoginUserId(loginUser.getId());
			if(CollectionUtil.isEmpty(accountMsgs)) {
				log.info("根据登陆ID[id="+loginUser.getId()+"]没有找到账户信息");
				// 二期改造    表示客户只有客户信息没有账户信息
				loginUser.setHasAcnt(false);
			} else {
				loginUser.setHasAcnt(true);
				loginUser.setAccountMsgs(accountMsgs);
				if(StringUtil.isEmpty(loginUser.getMobile())) {
					loginUser.setBindMobile(false);
				}else {
					loginUser.setBindMobile(true);
				}
				if(StringUtil.isEmpty(loginUser.getEmail())) {
					loginUser.setBindEmail(false);
				}else {
					loginUser.setBindEmail(true);
				}
			}
			UserCheckVO checkVo = new UserCheckVO(true);
			loginUser.setUserCheckVo(checkVo);
			return loginUser;
		}  catch (Exception e) {
			log.error("文件保存失败：", e);
			UserCheckVO checkVo = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			MrsLoginUserDto dto = new MrsLoginUserDto();
			dto.setUserCheckVo(checkVo);
			return dto ;
		}
	}
	@Override
	public ResponseMessage auditPerson(String auditType, String auditId, String renson, String loginName) {
		ResponseMessage responseMessage = new ResponseMessage();	
		// 根据审核主键ID查审核信息
		MrsDataAuditChangeDto baseDataAuditChangeDto = mrsDataAuditChangeService.getMrsDataAuditChangeById(auditId);
		if(baseDataAuditChangeDto.getAuditStatus()!=EAuditStatus.AUDIT_WAIT){
			responseMessage.setMessage("审核失败,数据已被其他管理员审核！");
		}else{
			MrsPersonDto personDto = JsonUtil.JsonToObject(baseDataAuditChangeDto.getChangeContent(), MrsPersonDto.class);
			if(EAuditStatus.AUDIT_PASS.getValue().equals(auditType)){//通过
				//更新信息
				mrsPersonService.update(personDto);
				baseDataAuditChangeDto.setAuditStatus(EAuditStatus.AUDIT_PASS);//复核核状态【1-通过】
				//同步数据到客户系统
				mrsPersonService.updateAndSync(personDto);
			}else if(EAuditStatus.AUDIT_FAIL.getValue().equals(auditType)){//拒绝
				baseDataAuditChangeDto.setAuditStatus(EAuditStatus.AUDIT_FAIL);//复核核状态【2-拒绝】
				baseDataAuditChangeDto.setAuditReason(renson);
			}
			baseDataAuditChangeDto.setAuditUser(loginName);//复核用户
			baseDataAuditChangeDto.setAuditTime(new Date());//复核时间
			//更新复核信息
			mrsDataAuditChangeService.updateBaseDataAuditChange(baseDataAuditChangeDto);
			responseMessage.setMessage(baseDataAuditChangeDto.getChangeType().getDisplayName()+"操作审核成功！");
			responseMessage.setSuccess(true);
		}
		return responseMessage;
	
	}
	@Override
	public void updateAndSync(MrsPersonDto dto) {
		//同步数据到客户系统
		mrsPersonService.updatePicAndSync(dto);
		
	}
	@Override
	public int insertSelective(MrsPersonDto dto) {
		return mrsPersonService.insertSelective(dto);
	}
	/**
	 * 系统根据“客户三要素”检查是否存在相同三要素且状态为“正常”的一户通账户，
	 * 若存在，系统终止流程并提示“您已存在有效一户通，一户通号码为XXX，请直接使用该号码登录”。
	 */
	@Override
	public PersonVO checkPersonBy3Element(PersonVO vo) {
		try {
			log.debug("MrsPersonAppServiceImpl.checkPersonBy3Element run....");
//			MrsPersonDto mrsPersonDto = mrsPersonService.findBy3Element(vo.getCustomerName(), vo.getCredentialsType(),
//					vo.getCredentialsNumber());
//			if (null != mrsPersonDto && StringUtils.isNotBlank(mrsPersonDto.getCustId())) {
//				MrsAccountDto accountDto = mrsAccountService.findByCustId(mrsPersonDto.getCustId());
//				if (null != accountDto && MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(accountDto.getAccountStatus())) {
//					UserCheckVO userCheckVO = new UserCheckVO(true);
//					vo.setCustId(mrsPersonDto.getCustId());
//					vo.setCheckVo(userCheckVO);
//					return vo;
//				}
//			}
			UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.USER_NULL);
			vo.setCheckVo(userCheckVO);
			return vo;
		} catch (Exception e) {
			log.error("检查是否存在相同三要素失败:", e);
			UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			vo.setCheckVo(userCheckVO);
			return vo;
		}
	}
	
	@Override
	public SaveAduitPersonResponseVo saveAduitPerson(MrsPersonVo personVo) {
		return mrsPersonService.saveAduitPerson(personVo);
	}
	@Override
	public SaveAduitPersonResponseVo saveAduitPersondobb(MrsPersonVo personVo,String id) {
		return mrsPersonService.saveAduitPersondobb(personVo,id);
	}
	@Override
	public PersonVO saveAduitPersonByPortal(PersonVO personVo,MrsLoginUserDto loginUser) {
		if(EYesNo.YES.getValue().equals(personVo.getIsOpenUser())){
			//被动开户
			return mrsPersonService.saveAduitPersonByPortalRest(personVo,loginUser);
		}else{
			//直接先开户，再送审
			return mrsPersonService.openAndAduitPersonByPortal(personVo,loginUser);
		}
		
	}
	@Override
	public SaveAduitPersonResponseVo doUpdateSaveAduit(MrsPersonVo personVo) {
		
		return mrsPersonService.doUpdateSaveAduit(personVo);
	}
	@Override
	public PersonVO openAccountByPortal(PersonVO personVo,BankBusiReqVO bankBusiReqVO) {
		return mrsPersonService.openAccountByPortal(personVo, bankBusiReqVO);
	}
	@Override
	public PersonVO directlyOpenAccount(PersonVO personVo) {
		return mrsPersonService.directlyOpenAccount(personVo);
	}
	@Override
	public SaveAduitPersonResponseVo doUpdateActSaveAduit(MrsActAccountVo actAccountVo) {
		return mrsPersonService.doUpdateActSaveAduit(actAccountVo);
	}
	@Override
	public void updateByPrimaryKey(MrsPersonDto dto) {
		mrsPersonService.updateByPrimaryKey(dto);
	}
	@Override
	public void updateBaseAndSync(MrsPersonDto dto) {
		mrsPersonService.updateBaseAndSync(dto);//更新个人客户信息并同步
	}
	@Override
	public void updateBaseFileAndSync(MrsPersonDto dto) {
		mrsPersonService.updateBaseFileAndSync(dto);//更新个人客户信息并同步基础信息及文件
	}
	@Override
	public List<MrsPersonDto> findBy3Element(String customerName, String type, String number) {
		return mrsPersonService.findBy3Element(customerName, type, number);
	}
	@Override
	public SaveAduitPersonResponseVo removeSaveAduit(MrsRemoveAccountVo removeAccountVo) {
		return mrsPersonService.removeSaveAduit(removeAccountVo);
	}
	
}
