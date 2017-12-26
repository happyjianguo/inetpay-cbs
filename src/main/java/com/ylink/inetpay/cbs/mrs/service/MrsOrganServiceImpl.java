package com.ylink.inetpay.cbs.mrs.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;
import org.ylinkpay.framework.core.util.MD5Utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shie.constant.SHIEConfigConstant;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.act.dao.ActAccountDtoMapper;
import com.ylink.inetpay.cbs.act.dao.ActBusiRefSubDtoMapper;
import com.ylink.inetpay.cbs.act.dao.ActSubjectDtoMapper;
import com.ylink.inetpay.cbs.bis.service.BisEmailService;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.constants.MrsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAccountPlatformCustDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitAttachmentDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitContentDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitInfoDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitPersonDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsCertFileDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAcctDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditItemDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfSubAcctDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsLoginUserDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsNotifyDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsOrganDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPlatformDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPortalAccountAduitDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsSubAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.rest.MrsPassiveOpenAcntServiceImpl;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.core.constant.AaccountType;
import com.ylink.inetpay.common.core.constant.AcctBusiType;
import com.ylink.inetpay.common.core.constant.ConfAuditBusiType;
import com.ylink.inetpay.common.core.constant.EAduitTypeEnum;
import com.ylink.inetpay.common.core.constant.EAuditChangeType;
import com.ylink.inetpay.common.core.constant.EAuditStatus;
import com.ylink.inetpay.common.core.constant.EAuditUserType;
import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.constant.EBisSmsSystem;
import com.ylink.inetpay.common.core.constant.EDistributTypeEnum;
import com.ylink.inetpay.common.core.constant.EOperaTypeEnum;
import com.ylink.inetpay.common.core.constant.EStartSystemEnum;
import com.ylink.inetpay.common.core.constant.EStatusEnum;
import com.ylink.inetpay.common.core.constant.IsDelete;
import com.ylink.inetpay.common.core.constant.LoginUserIsMain;
import com.ylink.inetpay.common.core.constant.LoginUserStatus;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.core.constant.MrsAccountSource;
import com.ylink.inetpay.common.core.constant.MrsAccountStatus;
import com.ylink.inetpay.common.core.constant.MrsConfSubRelationType;
import com.ylink.inetpay.common.core.constant.MrsCredentialsType;
import com.ylink.inetpay.common.core.constant.MrsCustTypeEnum;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.core.constant.MrsCustomerTypeCode;
import com.ylink.inetpay.common.core.constant.MrsNotifyStatus;
import com.ylink.inetpay.common.core.constant.MrsNotifyType;
import com.ylink.inetpay.common.core.constant.MrsPlatformCode;
import com.ylink.inetpay.common.core.constant.MrsPlatformIsAuth;
import com.ylink.inetpay.common.core.constant.MrsPlatformStatus;
import com.ylink.inetpay.common.core.constant.MrsSubAccountOwnType;
import com.ylink.inetpay.common.core.constant.MrsSubAccountStatus;
import com.ylink.inetpay.common.core.constant.MrsSubAccountType;
import com.ylink.inetpay.common.core.constant.UseAccountType;
import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.core.util.GsonUtil;
import com.ylink.inetpay.common.core.util.JsonUtil;
import com.ylink.inetpay.common.core.util.RandomUtil;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.account.app.ActBookAppService;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.account.pojo.AccountSubsPojo;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountPlatformCustDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitAttachmentDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitContentDtoWithBLOBs;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditItemDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfSubAcctDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsContactListDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsNotifyDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOrganDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalAccountAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.PortalCode;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsActAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsOrganVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsRemoveAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitProductResponseVo;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgRespVo;
import com.ylink.inetpay.common.project.portal.vo.AccountUnitUpdateReqVO;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchRequestVO;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchResponseVO;
import com.ylink.inetpay.common.project.portal.vo.UnitAssetRequestVo;
import com.ylink.inetpay.common.project.portal.vo.UnitRequestVO;
import com.ylink.inetpay.common.project.portal.vo.UploadOrganPojo;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.AccountSearchReqVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.AccountSearchRespVO;
import com.ylink.inetpay.common.project.portal.vo.customer.OrganVO;
import com.ylink.inetpay.common.project.portal.vo.customer.PersonVO;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncFileInfo;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncOrganReq;

import net.sf.json.JSONObject;

@Service("mrsOrganService")
public class MrsOrganServiceImpl implements MrsOrganService {

	private static Logger log = LoggerFactory.getLogger(MrsOrganServiceImpl.class);

	@Autowired
	private MrsOrganDtoMapper mrsOrganDtoMapper;
	@Autowired
	private MrsAccountDtoMapper mrsAccountDtoMapper;
	@Autowired
	private MrsSubAccountDtoMapper mrsSubAccountDtoMapper;
	@Autowired
	private MrsUserAccountDtoMapper mrsUserAccountDtoMapper;
	@Autowired
	private MrsPlatformDtoMapper mrsPlatformDaoMapper;
	@Autowired
	private MrsLoginUserDtoMapper mrsLoginUserDtoMapper;
	@Autowired
	private MrsDataAuditChangeService mrsDataAuditChangeService;
	@Autowired
	private MrsNotifyDtoMapper mrsNotifyDtoMapper;
	@Autowired
	private MrsCertFileDtoMapper mrsCertFileDtoMapper;
	@Autowired
	private MrsConfSubAcctService mrsConfSubAcctService;
	@Autowired
	private ActBookAppService actBookAppService;
	@Autowired
	private ActAccountDtoMapper actAccountDtoMapper;
	@Autowired
	private MrsPortalAccountAduitDtoMapper mrsPortalAccountAduitDtoMapper;
	@Autowired
	private MrsAccountService mrsAccountService;
	//基础配置审核配置信息操作（配置审核人）
	@Autowired
	private MrsConfAuditDtoMapper mrsConfAuditDtoMapper;
	//审核配置明细信息
	@Autowired
	private MrsConfAuditItemDtoMapper mrsConfAuditItemDtoMapper;
	//审核配置明细信息操作
	@Autowired
	private MrsAduitPersonDtoMapper mrsAduitPersonDtoMapper;
	//审核内容信息操作
	@Autowired
	private MrsAduitContentDtoMapper mrsAduitContentDtoMapper;
	//审核主要信息操作
	@Autowired
	private MrsAduitInfoDtoMapper mrsAduitInfoDtoMapper;
	//子账户审核配置信息
	@Autowired
	private MrsConfSubAcctDtoMapper mrsConfSubAcctDtoMapper;
	@Autowired
	private BisEmailService bisEmailService;
	//联系人操作service
	@Autowired
	private MrsContactListService mrsContactListService;
	//附件操作service
	@Autowired
	private MrsCertFileService mrsCertFileService;
	//审核附件操作
	@Autowired
	private MrsAduitAttachmentDtoMapper mrsAduitAttachmentDtoMapper;
	
	//子账户配置查询服务
	@Autowired
	private MrsSubAccountService mrsSubAccountService;
	//系统查询服务
	@Autowired
	private BisSysParamService bisSysParamService;
	@Autowired
	private MrsAccountPlatformCustDtoMapper mrsAccountPlatformCustDtoMapper;
	// 创建审核数据操作服务
	@Autowired
	private MrsAduitInfoService mrsAduitInfoService;
	@Autowired
	private ActSubjectDtoMapper actSubjectDtoMapper;
	@Autowired
	private MrsConfAcctDtoMapper mrsConfAcctDtoMapper;
	@Autowired
	private ActBusiRefSubDtoMapper actBusiRefSubDtoMapper;
	
	@Override
	public List<MrsOrganDto> findBy3Element(String customerName, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence) {
		List<MrsOrganDto> organDtoList = mrsOrganDtoMapper.findBy3Element(customerName, socialCreditCode, organizeCode,
				revenueCode, businessLicence);
		return organDtoList;
	}

	@Override
	public List<MrsOrganDto> findOrganByParams(String customerType, String accountStatus) {
		List<MrsOrganDto> list = mrsOrganDtoMapper.findOrganByParams(customerType, accountStatus);
		return list;
	}
	/**
	 * 根据新生成一户通编号查询“一户通账户表”和“审核主要信息表”是否存在相同一户通编号， 如果存在，则重新生成，直至一户通编号不重复。
	 * 
	 * @return
	 */
	/*private String checkCustIdAndReturnId(String custId) {
		//递归
		if(StringUtils.isEmpty(custId)){
			custId = mrsAccountDtoMapper.getMrsNonPersonSeqVal();
			custId = MrsConstants.ORGAN_ACCOUONT_PREFIX + StringUtils.format(11, custId);
		}
		boolean checkFlag = mrsAccountService.checkCustId(custId);
		// 如果返回True表编号可用
		if (!checkFlag) {
			return checkCustIdAndReturnId(null);
		}else{
			return custId;
		}
	}*/
	/**
	 * 根据客户类型，关联关系查询子账户配置
	 * @param cType
	 * @param platformCode
	 * @return
	 */
	private List<MrsConfSubAcctDto> findByUserTypeAndRationType(MrsCustomerType cType, String platformCode, String actType) {
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService.findBy4Elment(cType, 
				MrsConfSubRelationType.MUST, platformCode, actType);
		return mrsConfSubAcctDtos;
	}
	/**
	 * 一户通客户关系
	 * @param requestVo
	 * @param custId
	 * @return
	 */
	private MrsAccountPlatformCustDto generateAccountPlatformAsset(UnitAssetRequestVo requestVo, String custId) {
		MrsAccountPlatformCustDto accountPlatformCustDto = new MrsAccountPlatformCustDto();
		accountPlatformCustDto.setId(accountPlatformCustDto.getIdentity());
		accountPlatformCustDto.setCustId(custId);
		accountPlatformCustDto.setSource(requestVo.getPlatformCode());
		accountPlatformCustDto.setPlatformCustCode(requestVo.getPlatformCustCode());
		accountPlatformCustDto.setCreateTime(new Date());
		return accountPlatformCustDto;
	}
	/**
	 * 一户通客户关系
	 * @param requestVo
	 * @param custId
	 * @return
	 */
	private MrsAccountPlatformCustDto generateAccountPlatform(UnitRequestVO requestVo, String custId) {
		MrsAccountPlatformCustDto accountPlatformCustDto = new MrsAccountPlatformCustDto();
		accountPlatformCustDto.setId(accountPlatformCustDto.getIdentity());
		accountPlatformCustDto.setCustId(custId);
		accountPlatformCustDto.setSource(requestVo.getPlatformCode());
		accountPlatformCustDto.setCreateTime(new Date());
		return accountPlatformCustDto;
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto initOpenAcnt(UnitRequestVO requestVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException {
		// 获取非自然人保险一户通序列
		//String custId = checkCustIdAndReturnId(null);---20170523修改成调用管理平台的统一接口
		String custId = mrsAccountService.checkCustTypeReturnId(MrsCustomerType.MCT_1,
				AaccountType.getEnum(requestVo.getAccountType()));

		// 将请求对象VO中的值赋值到一户通DTO中
		log.info("开始组装数据信息,一户通账号:[custId={}]", custId);
		MrsAccountDto mrsAccountDto = copy2AccountDto(requestVo, custId, mrsPlatformDto.getIsAuth());
		// 根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_1,requestVo.getPlatformCode(),requestVo.getAccountType());
		// 创建子账户
		List<MrsSubAccountDto> subAccountList = generateSubAccount(custId, mrsPlatformDto,mrsConfSubAcctDtos);
		// 将请求对象VO中的值复制到机构客户的DTO中
		MrsOrganDto mrsOrganDto = copy2OrganDto(requestVo, custId);
		// 创建登陆对象信息
		MrsLoginUserDto loginUserDto = copy2LoginUserDto(custId,requestVo.getContactsEmail());
		MrsUserAccountDto userAccountDto = copy2UserAccount(mrsAccountDto.getId(), loginUserDto.getId());
		log.info("一户通[custId={}]开始插入数据", custId);
		//一户通客户关系
		MrsAccountPlatformCustDto accountPlatformCustDto = generateAccountPlatform(requestVo, custId);
		
		// 保存一户通DTO
		mrsAccountDtoMapper.insert(mrsAccountDto);
		// 保存子账户信息
		for (MrsSubAccountDto mrsSubAccountDto : subAccountList) {
			mrsSubAccountDtoMapper.insert(mrsSubAccountDto);
		}
		// 保存机构信息
		mrsOrganDtoMapper.insert(mrsOrganDto);
		// 保存用户与账户的关系
		mrsUserAccountDtoMapper.insert(userAccountDto);
		// 保存登陆信息
		mrsLoginUserDtoMapper.insert(loginUserDto);
		mrsAccountDto.setNoEncryptloginPwd(loginUserDto.getNoEncryptloginPwd());
		log.info("一户通[custId={}]插入成功", custId);
		//一户通客户关系表
		mrsAccountPlatformCustDtoMapper.insert(accountPlatformCustDto);
		if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())) {
			// 需要同步到客户系统 入库
			MrsNotifyDto notifyDto = generateSyncInfo(mrsOrganDto, MrsNotifyType.ORGAN);
			mrsNotifyDtoMapper.insert(notifyDto);
			//如果存在用户邮箱，则调用邮件接口发送邮件。邮件内容显示用户的一户通号码和一个修改登录密码的链接。
			// 用户点击该链接，直接进入系统的强制密码重置界面，进行密码重置。
			sendEmailOrgan(requestVo, loginUserDto,custId);
		}
		//开通资金账户
		createAccounts(custId, MrsCustomerType.MCT_1, mrsOrganDto.getCustomerName(),mrsConfSubAcctDtos);
		return mrsAccountDto;
	}

	private void sendEmailOrgan(UnitRequestVO requestVo, MrsLoginUserDto loginUserDto,String custId) {
		String userEmail = requestVo.getContactsEmail();
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(userEmail)) {
			log.info("调用邮件接口发送邮件,用于密码重置");
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(SHIEConfigConstant.CUSTMER_NAME, requestVo.getCustomerName());
				UserCheckVO checkVo = bisEmailService.shieSendEmail(userEmail, loginUserDto.getId(), EBisSmsSystem.PORTAL,
						EBisEmailTemplateCode.REST_LOGIN_PWD, params,custId);
				if (!checkVo.isCheckValue()) {
					log.error("邮件发送失败:" + checkVo.getMsg());
				}
			} catch (Exception e) {
				log.error("邮件发送失败:", ExceptionProcUtil.getExceptionDesc(e));
			}
		}
	}
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto initOpenAssetAcnt(UnitAssetRequestVo requestVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException {
		// 获取非自然人保险一户通序列
		//String custId = checkCustIdAndReturnId(null);---20170523修改成调用管理平台的统一接口
		String custId = mrsAccountService.checkCustTypeReturnId(MrsCustomerType.MCT_1,
				AaccountType.getEnum(requestVo.getAccountType()));
		// 将请求对象VO中的值赋值到一户通DTO中
		log.info("开始组装数据信息,一户通账号:[custId={}]", custId);
		MrsAccountDto mrsAccountDto = copy2AccountDtoAsset(requestVo, custId, mrsPlatformDto.getIsAuth());
		// 根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_1,requestVo.getPlatformCode(),requestVo.getAccountType());
		// 创建子账户
		List<MrsSubAccountDto> subAccountList = generateSubAccount(custId, mrsPlatformDto,mrsConfSubAcctDtos);
		// 将请求对象VO中的值复制到机构客户的DTO中
		MrsOrganDto mrsOrganDto = copyAsset2OrganDto(requestVo, custId);
		// 创建登陆对象信息
		MrsLoginUserDto loginUserDto = copy2LoginUserDto(custId,requestVo.getContactsEmail());
		MrsUserAccountDto userAccountDto = copy2UserAccount(mrsAccountDto.getId(), loginUserDto.getId());
		log.info("一户通[custId={}]开始插入数据", custId);
		//一户通客户关系
		MrsAccountPlatformCustDto accountPlatformCustDto = generateAccountPlatformAsset(requestVo, custId);
		
		// 保存一户通DTO
		mrsAccountDtoMapper.insert(mrsAccountDto);
		// 保存子账户信息
		for (MrsSubAccountDto mrsSubAccountDto : subAccountList) {
			mrsSubAccountDtoMapper.insert(mrsSubAccountDto);
		}
		// 保存机构信息
		mrsOrganDtoMapper.insert(mrsOrganDto);
		// 保存用户与账户的关系
		mrsUserAccountDtoMapper.insert(userAccountDto);
		// 保存登陆信息
		mrsLoginUserDtoMapper.insert(loginUserDto);
		mrsAccountDto.setNoEncryptloginPwd(loginUserDto.getNoEncryptloginPwd());
		log.info("一户通[custId={}]插入成功", custId);
		//一户通客户关系表
		mrsAccountPlatformCustDtoMapper.insert(accountPlatformCustDto);
		if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())) {
			// 需要同步到客户系统 入库
			MrsNotifyDto notifyDto = generateSyncInfo(mrsOrganDto, MrsNotifyType.ORGAN);
			mrsNotifyDtoMapper.insert(notifyDto);
			//如果存在用户邮箱，则调用邮件接口发送邮件。邮件内容显示用户的一户通号码和一个修改登录密码的链接。
			// 用户点击该链接，直接进入系统的强制密码重置界面，进行密码重置。
			sendEmail(requestVo, loginUserDto,custId);
		}
		//开通资金账户
		createAccounts(custId, MrsCustomerType.MCT_1, mrsOrganDto.getCustomerName(),mrsConfSubAcctDtos);
		return mrsAccountDto;
	}

	private void sendEmail(UnitAssetRequestVo requestVo, MrsLoginUserDto loginUserDto,String custId) {
		String userEmail = requestVo.getContactsEmail();
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(userEmail)) {
			log.info("调用邮件接口发送邮件,用于密码重置");
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(SHIEConfigConstant.CUSTMER_NAME, requestVo.getCustomerName());
				UserCheckVO checkVo = bisEmailService.shieSendEmail(userEmail, loginUserDto.getId(),
						EBisSmsSystem.PORTAL, EBisEmailTemplateCode.REST_LOGIN_PWD, params,custId);
				if (!checkVo.isCheckValue()) {
					log.error("邮件发送失败:" + checkVo.getMsg());
				}
			} catch (Exception e) {
				log.error("邮件发送失败:", ExceptionProcUtil.getExceptionDesc(e));
			}
		}
	}
	
	/**
	 * 强制开户
	 * @throws Exception 
	 * @throws CodeCheckedException 
	 */
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto forceOpenOrganAcnt(UnitAssetRequestVo unitReqVo, List<MrsOrganDto> organList) throws CodeCheckedException{
		// 根据接入平台编号查询平台信息
		MrsPlatformDto newPlatform = mrsPlatformDaoMapper.findByPlatformCode(unitReqVo.getPlatformCode(),
				MrsPlatformStatus.PS_0.getValue());
		if (newPlatform == null) {
			log.error("开户渠道不存在，查询条件[status = {},platformCode={}]", MrsPlatformStatus.PS_0.getValue(),
					unitReqVo.getPlatformCode());
			throw new CodeCheckedException(PortalCode.CODE_9999, "不存在有效的开户渠道!");
		}
		// 如果不为空就只有一个
		MrsOrganDto organ = organList.get(0);

		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.findOrganLoginByCustId(organ.getCustId());
		// 修改客户信息
//		unitReqVo.setPlatformCustCode(null);
//		BeanUtils.copyProperties(unitReqVo, organ);
//		organ.setUpdateTime(new Date());
//		mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);

		// 获取非自然人保险一户通序列
		String custId = mrsAccountDtoMapper.getMrsNonPersonSeqVal();
		custId = MrsConstants.ORGAN_ACCOUONT_PREFIX + StringUtils.format(11, custId);
		
		//新增客户信息
		MrsOrganDto mrsOrganDto = copy2MrsOrganDto( unitReqVo, custId);
		mrsOrganDtoMapper.insert(mrsOrganDto);

		log.info("开始组装非自然人数据信息,一户通账号:[custId={}]", custId);
		// 将请求对象VO中的值赋值到一户通DTO中
		MrsAccountDto newMrsAccountDto = copyAsset2AccountDto(unitReqVo, custId, newPlatform.getIsAuth());
		List<MrsSubAccountDto> subAccountList = new ArrayList<MrsSubAccountDto>();
		//根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService.findBy4Elment(
				MrsCustomerType.MCT_1,
				MrsConfSubRelationType.MUST,unitReqVo.getPlatformCode(), unitReqVo.getAccountType());
		if(mrsConfSubAcctDtos!=null && mrsConfSubAcctDtos.size()>0){
			for(MrsConfSubAcctDto mrsConfSubAcctDto :mrsConfSubAcctDtos ){
				subAccountList.add(generateSubAccount(custId, newPlatform, mrsConfSubAcctDto.getSubAccountCode()));
			}
		}
		
		MrsUserAccountDto userAccount = copy2UserAccount(newMrsAccountDto.getId(), loginUser.getId());
		// 保存一户通DTO
		mrsAccountDtoMapper.insert(newMrsAccountDto);
		// 改以前一户通状态为失效
		// MrsAccountDto accountDto =
		// mrsAccountDtoMapper.findByCustId(person.getCustId());
		// accountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_2.getValue());
		// mrsAccountDtoMapper.updateByPrimaryKey(accountDto);
		// 保存子账户信息
		mrsSubAccountDtoMapper.insertList(subAccountList);
		mrsUserAccountDtoMapper.insert(userAccount);
		log.info("一户通[custId={}]插入完成", custId);
		//开通资金账户
		//createAccounts(custId, MrsCustomerType.MCT_1, organ.getCustomerName(),null);
		return newMrsAccountDto;
	}
	/**
	 * 强制开户
	 * 
	 * @throws Exception
	 */
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto forceOpenAcnt(UnitAssetRequestVo reqVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException {
		MrsAccountDto mrsAccountDto = null;
		// （a） 判断查询记录中认证状态为“已生效”、账户状态为非“已注销”的记录条数，
		// 并查询“系统参数表”强制开户的个数限制，如果记录条数大于等于个数限制，
		// 则记录错误日志并返回处理结果“开通失败”。
		String socialCreditCode = reqVo.getSocialCreditCode();
		String organizeCode = reqVo.getOrganizeCode();
		String revenueCode = reqVo.getRevenueCode();
		String businessLicence = reqVo.getBusinessLicence();
		String organOtherCode = reqVo.getOrganOtherCode();
		boolean checkFlag = this.checkOrgan3ElmentIsMax(reqVo.getCustomerName(), 
				socialCreditCode, organizeCode, revenueCode, businessLicence, organOtherCode);
		// （b） 进行一户通开户，具体流程与上面开户流程相同
		if (checkFlag) {
			log.info("进行强制开户");
			mrsAccountDto = this.initOpenAssetAcnt(reqVo,mrsPlatformDto);
		} else {
			log.error("强制开户失败,查询“系统参数表”强制开户的个数已超过限制");
			throw new CodeCheckedException("强制开户失败,查询“系统参数表”强制开户的个数已超过限制");
		}
		return mrsAccountDto;
	}

	/**
	 * 同步证件信息
	 * 
	 * @param dto
	 * @param notifyType
	 * @return
	 */
	private MrsNotifyDto generateSyncFileInfo(MrsOrganDto dto, MrsNotifyType notifyType) {
		List<String> statusList = new ArrayList<String>();
		statusList.add(MrsNotifyStatus.NO_NOTIFY.getValue());
		statusList.add(MrsNotifyStatus.NOTIFY_FAIL.getValue());
		MrsNotifyDto sysNotifyDto = mrsNotifyDtoMapper.findByCustIdStatus(notifyType.getValue(), dto.getCustId(),
				statusList);
		if (sysNotifyDto != null) {
			// 将原记录置为失效(确保通知记录表只有一个客户只存在一条通知数据)
			mrsNotifyDtoMapper.updateStatusById(sysNotifyDto.getId(), MrsNotifyStatus.NOTIFY_INVALID.getValue(),
					sysNotifyDto.getNotifyNum(), new Date());
		}
		// 查询客户的所有证件
		List<SyncFileInfo> syncFileList = new ArrayList<SyncFileInfo>();
		MrsCertFileDto record = new MrsCertFileDto();
		record.setCustId(dto.getCustId());
		List<MrsCertFileDto> queryCertFileList = mrsCertFileDtoMapper.queryCertFile(record);
		if (queryCertFileList != null && queryCertFileList.size() > 0) {
			for (MrsCertFileDto mcfDto : queryCertFileList) {
				SyncFileInfo syncfile = new SyncFileInfo();
				syncfile.setCustomerCode(dto.getCustomerCode());// 客户号
				syncfile.setFileUrl(mcfDto.getFileId());
				syncfile.setCredentialsType(mcfDto.getCertType());
				//syncfile.setFileTypeCode(FileTypeEnum.getEnum(mcfDto.getFileType()).toString());
				syncfile.setFileTypeCode(mcfDto.getFileType());
				syncfile.setSysSourceCode(SHIEConfigConstant.SYNC_SYS_SOURCE_CODE);
				syncfile.setCreateBy(SHIEConfigConstant.SYNC_CREATE_BY);
				syncfile.setUpdateBy(SHIEConfigConstant.SYNC_UPDATE_BY);
				syncfile.setFileName(mcfDto.getFileRemark());
				syncFileList.add(syncfile);
			}
			String jsonStr = GsonUtil.noAnnotaGson.toJson(syncFileList);
			log.info("同步字符串:" + jsonStr);

			MrsNotifyDto notifyDto = new MrsNotifyDto();
			notifyDto.setBusiType(notifyType.getValue());
			notifyDto.setCreateTime(new Date());
			notifyDto.setCustId(dto.getCustId());
			notifyDto.setId(UUID.randomUUID().toString());
			notifyDto.setNotifyNum(0);
			notifyDto.setReqJson(jsonStr);
			notifyDto.setStatus(MrsNotifyStatus.NO_NOTIFY.getValue());
			notifyDto.setCustomerCode(dto.getCustomerCode());
			return notifyDto;
		} else {
			return null;
		}
	}

	private MrsNotifyDto generateSyncInfo(MrsOrganDto dto, MrsNotifyType notifyType) {
		List<String> statusList = new ArrayList<String>();
		statusList.add(MrsNotifyStatus.NO_NOTIFY.getValue());
		statusList.add(MrsNotifyStatus.NOTIFY_FAIL.getValue());
		MrsNotifyDto sysNotifyDto = mrsNotifyDtoMapper.findByCustIdStatus(notifyType.getValue(), dto.getCustId(),
				statusList);
		if (sysNotifyDto != null) {
			// 将原记录置为失效(确保通知记录表只有一个客户只存在一条通知数据)
			mrsNotifyDtoMapper.updateStatusById(sysNotifyDto.getId(), MrsNotifyStatus.NOTIFY_INVALID.getValue(),
					sysNotifyDto.getNotifyNum(), new Date());
		}
		SyncOrganReq req = new SyncOrganReq();
		req.setAuthPersonIdentifyNo(dto.getAuthPersonIdentifyNo());
		req.setAuthPersonIdentifyTypeCode(dto.getAuthPersonIdentifyTypeCode());
		req.setAuthPersonName(dto.getAuthPersonName());
		req.setBusinessLicence(dto.getBusinessLicence());

		MrsAccountDto accountDto = mrsAccountDtoMapper.findByCustId(dto.getCustId());
		if(null!=accountDto){
			req.setCustomerCode(accountDto.getCustomerCode());
		}
		req.setBusinessSortCode(dto.getBusinessSortCode());
		req.setCustomerEName(dto.getCustomerEname());
		req.setCustomerName(dto.getCustomerName());
		req.setCustomerShortName(dto.getCustomerShortName());

		req.setNationalityCode(dto.getNationalityCode());
		req.setOrganizeCode(dto.getOrganizeCode());
		req.setRegistAddress(dto.getRegisteredAddr());
		req.setRevenueCode(dto.getRevenueCode());
		req.setSocialCredit(dto.getSocialCreditCode());
		req.setCustomerTypeCode(MrsCustomerTypeCode.MCT_2.getValue());
//		req.setOrganOtherCode(dto.getOrganOtherCode());
		
		req.setLinkManEmail(dto.getContactsEmail());
		req.setLinkManMobile(dto.getContactsMobile());
		req.setLinkManName(dto.getContactsName());
		req.setLinkManPhone(dto.getContactsTel());
		req.setBusinessDetailSortCode(dto.getBusinessSortSubCode());
		req.setLinkManAddress(dto.getContactsAddr());
		req.setLinkManFax(dto.getContactsFax());
		req.setLinkManSpareTel(dto.getContactsSpareTel());
		req.setLinkManZip(dto.getContactsZip());
		req.setSysSourceCode(SHIEConfigConstant.SYNC_SYS_SOURCE_CODE);
		req.setCreateBy(SHIEConfigConstant.SYNC_CREATE_BY);
		req.setUpdateBy(SHIEConfigConstant.SYNC_UPDATE_BY);
		
		if(org.apache.commons.lang.StringUtils.isNotBlank(dto.getBusinessLicenceEndDate())){
			req.setBusLicusefulLife(DateUtils.stringToString(dto.getBusinessLicenceEndDate()));
		}
		
		String jsonStr = GsonUtil.noAnnotaGson.toJson(req);
		log.info("同步字符串:" + jsonStr);

		MrsNotifyDto notifyDto = new MrsNotifyDto();
		notifyDto.setBusiType(notifyType.getValue());
		notifyDto.setCreateTime(new Date());
		notifyDto.setCustId(dto.getCustId());
		notifyDto.setId(UUID.randomUUID().toString());
		notifyDto.setNotifyNum(0);
		notifyDto.setReqJson(jsonStr);
		notifyDto.setStatus(MrsNotifyStatus.NO_NOTIFY.getValue());
		return notifyDto;
	}
	/**
	 * 根据请求参数中渠道编号查询“子账户配置表”该渠道开通机构客户必须开通的子账户编号，
	 * 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 * @param newPlatform
	 * @return
	 * @throws CodeCheckedException
	 */
	private List<MrsConfSubAcctDto> checkAndSaveAct(String platformCode, MrsAccountDto retrunMrsAccountDto,
			MrsPlatformDto newPlatform) throws CodeCheckedException {
		List<MrsSubAccountDto> subAccountList = null;
		List<MrsSubAccountDto> subActList = mrsSubAccountDtoMapper.findByCustId(retrunMrsAccountDto.getCustId());
		// 根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_0,
				platformCode,retrunMrsAccountDto.getAccountType().getValue());
		// 需要创建子账户的数据
		List<MrsConfSubAcctDto> creageSubList = new ArrayList<MrsConfSubAcctDto>();
		List<MrsConfSubAcctDto> deleteSubList = new ArrayList<MrsConfSubAcctDto>();
		if (subActList != null && subActList.size() > 0) {
			if (mrsConfSubAcctDtos != null && mrsConfSubAcctDtos.size() > 0) {
				for (MrsConfSubAcctDto confSubDto : mrsConfSubAcctDtos) {
					for (MrsSubAccountDto mrsSubAct : subActList) {
						if (mrsSubAct.getSubAccountType().equals(confSubDto.getSubAccountCode())) {
							// 删除不要子账户，相当于只创建不存在的子账户
							deleteSubList.add(confSubDto);
						}
					}
				}
			}
			//如果子账户的状态为“已注销”或“未生效”,且一户通认证状态为“已认证”或“无需认证”，则更改状态为“有效”
			MrsAccountAuthStatus authStatus = MrsAccountAuthStatus.getEnum(retrunMrsAccountDto.getAuthStatus());
			for (MrsSubAccountDto mrsSubActDto : subActList) {
				if ((MrsSubAccountStatus.MSAS_2.getValue().equals(mrsSubActDto.getSubAccountStatus())
						|| MrsSubAccountStatus.MSAS_9.getValue().equals(mrsSubActDto.getSubAccountStatus()))
						&&(MrsAccountAuthStatus.senseAuthStaus(authStatus))) {
					mrsSubActDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());
					mrsSubActDto.setUpdateTime(new Date());
					mrsSubAccountDtoMapper.updateByPrimaryKeySelective(mrsSubActDto);
				}
			}
			//删除多余的子账户，再保存为生成的子账户
			if(deleteSubList!=null && deleteSubList.size()>0){
				mrsConfSubAcctDtos.removeAll(deleteSubList);
				creageSubList = mrsConfSubAcctDtos;
			}
		} else {
			// 如果没有子账户，则创建所有配置的子账户
			creageSubList = mrsConfSubAcctDtos;
		}
		subAccountList = generateSubAccount(retrunMrsAccountDto.getCustId(), newPlatform, creageSubList);
		// 保存子账户信息
		if (subAccountList != null && subAccountList.size() > 0) {
			for (MrsSubAccountDto mrsSubAccountDto : subAccountList) {
				mrsSubAccountDtoMapper.insert(mrsSubAccountDto);
			}
		}
		return creageSubList;
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto updateOpenAcnt(UnitRequestVO reqVo, List<MrsAccountDto> organDtoList
			,MrsPlatformDto newPlatform)
			throws CodeCheckedException {
		MrsAccountDto retrunMrsAccountDto = new MrsAccountDto();
		// c1、更新信息后需要返回一户通信息，查询返回一户通。查询结果存在多条记录，
		// 那么首先取认证状态为“认证成功”或“无需认证”记录
		// 其次再取其他状态的记录，如果相同状态仍存在多条取再次取一户通创建时间为最近的记录
		if (organDtoList.size() == 1) {
			// 返回账户信息
			retrunMrsAccountDto = organDtoList.get(0);
		} else {
			// 首先取认证状态为“认证成功”或“无需认证”记录
			List<MrsAccountDto> actOrganLastList = mrsAccountService.findByOrgan3ElementLast(reqVo.getCustomerName(),
					reqVo.getSocialCreditCode(), reqVo.getOrganizeCode(), reqVo.getRevenueCode(),
					reqVo.getBusinessLicence(),null);
			if (actOrganLastList != null && actOrganLastList.size() > 0) {
				// 返回账户信息,最近的记录
				retrunMrsAccountDto = actOrganLastList.get(0);
			}
			// 其次再取其他状态的记录
			else {
				// 返回账户信息,最近的记录
				retrunMrsAccountDto = organDtoList.get(0);
			}
		}
		log.info("更新一户通的机构和账户信息");
		senseAccount(reqVo, retrunMrsAccountDto, newPlatform);
		// （a）	根据请求参数中渠道编号查询“子账户配置表”该渠道开通机构客户必须开通的子账户编号，
		//判断该记录是否已经存在所有子账户，如果不存在或部分不存在或子账户的状态为“已注销”，
		// 则开通子账户，详见上面a4。如果子账户的状态为“已注销”，则更改状态为“有效”。
		log.info("场景c2(a)   处理子账户配置信息");
		List<MrsConfSubAcctDto> creageSubList = checkAndSaveAct(reqVo.getPlatformCode(), retrunMrsAccountDto, newPlatform);
		// （c） 开通资金账户。则将创建资金账户，详见上面a8。账务系统会判断要求开的资金账户是否已经存在，
		// 如果不存在则会新增。
		log.info("场景c2(b)   开通资金账户");
		createAccounts(retrunMrsAccountDto.getCustId(), MrsCustomerType.MCT_0, reqVo.getCustomerName(), creageSubList);
		// （d） 根据该记录中一户通编号、请求参数中接入平台编号和平台客户编号查询“一户通客户关系表”是否存在记录，
		// 如果不存在，则创建新的记录，记录内容与查询条件相同。
		List<MrsAccountPlatformCustDto> apcList = mrsAccountPlatformCustDtoMapper
				.findActPlatformCust(retrunMrsAccountDto.getCustId(), null, reqVo.getPlatformCode());
		if (apcList != null && apcList.size() > 0) {
			log.info("场景c2(c)   存在一户通客户关系");
		} else {
			log.info("场景c2(c)   建新一户通客户关系");
			saveMrsAccountPlatformCust(reqVo, retrunMrsAccountDto);
		}
		// 需要同步到客户系统
		if(MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(newPlatform.getIsAuth())){
			log.info("场景c6 保存同步信息到通知表");
			MrsOrganDto organ = mrsOrganDtoMapper.selectByPrimaryKey(retrunMrsAccountDto.getUserInfoId());
			saveNotifyInfo(reqVo,organ);
		}
		// 回最新的一户通账号信息
		// 1，已存在用户且状态为生效的，不返回密码 2. 已经登录过门户系统，不返回密码。
		/*MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(retrunMrsAccountDto.getCustId());
		if (MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(retrunMrsAccountDto.getAccountStatus())
				|| (null != loginUser && null != loginUser.getLoginTime())) {
			log.info("一户通账户[custId={}]账户状态是正常或已经登录过门户系统,不返回密码", retrunMrsAccountDto.getCustId());
		} else {
			//返回密码
			if (null != loginUser) {
				String loginPwd = RandomUtil.generate6Random();
				String encryLoginPwd = MD5Utils.MD5(MD5Utils.MD5(loginPwd) + SHIEConfigConstant.SALT);
				loginUser.setLoginPwd(encryLoginPwd);
				mrsLoginUserDtoMapper.updateByPrimaryKeySelective(loginUser);
				retrunMrsAccountDto.setNoEncryptloginPwd(loginPwd);
				log.info("一户通账户[custId={}]账户状态为生效，或没有登录过门户系统,返回新创建的登录密码", retrunMrsAccountDto.getCustId());
			}
		}*/
		return retrunMrsAccountDto;
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto updateOpenAcntAsset(UnitAssetRequestVo reqVo, List<MrsAccountDto> organDtoList
			,MrsPlatformDto newPlatform)
			throws CodeCheckedException {
		MrsAccountDto retrunMrsAccountDto = new MrsAccountDto();
		// c1、更新信息后需要返回一户通信息，查询返回一户通。查询结果存在多条记录，
		// 那么首先取认证状态为“认证成功”或“无需认证”记录
		// 其次再取其他状态的记录，如果相同状态仍存在多条取再次取一户通创建时间为最近的记录
		if (organDtoList.size() == 1) {
			// 返回账户信息
			retrunMrsAccountDto = organDtoList.get(0);
		} else {
			// 首先取认证状态为“认证成功”或“无需认证”记录
			List<MrsAccountDto> actOrganLastList = mrsAccountService.findByOrganAsset3ElementLast(reqVo.getCustomerName(),
					reqVo.getCredentialsType(), reqVo.getCredentialsNumber());
			if (actOrganLastList != null && actOrganLastList.size() > 0) {
				// 返回账户信息,最近的记录
				retrunMrsAccountDto = actOrganLastList.get(0);
			}
			// 其次再取其他状态的记录
			else {
				// 返回账户信息,最近的记录
				retrunMrsAccountDto = organDtoList.get(0);
			}
		}
		log.info("更新一户通的机构和账户信息");
		senseAccountAsset(reqVo, retrunMrsAccountDto, newPlatform);
		// （a）	根据请求参数中渠道编号查询“子账户配置表”该渠道开通机构客户必须开通的子账户编号，
		//判断该记录是否已经存在所有子账户，如果不存在或部分不存在或子账户的状态为“已注销”，
		// 则开通子账户，详见上面a4。如果子账户的状态为“已注销”，则更改状态为“有效”。
		log.info("场景c2(a)   处理子账户配置信息");
		List<MrsConfSubAcctDto> creageSubList = checkAndSaveAct(reqVo.getPlatformCode(), retrunMrsAccountDto, newPlatform);
		// （c） 开通资金账户。则将创建资金账户，详见上面a8。账务系统会判断要求开的资金账户是否已经存在，
		// 如果不存在则会新增。
		log.info("场景c2(b)   开通资金账户");
		createAccounts(retrunMrsAccountDto.getCustId(), MrsCustomerType.MCT_0, reqVo.getCustomerName(), creageSubList);
		// （d） 根据该记录中一户通编号、请求参数中接入平台编号和平台客户编号查询“一户通客户关系表”是否存在记录，
		// 如果不存在，则创建新的记录，记录内容与查询条件相同。
		List<MrsAccountPlatformCustDto> apcList = mrsAccountPlatformCustDtoMapper
				.findActPlatformCust(retrunMrsAccountDto.getCustId(), null, reqVo.getPlatformCode());
		if (apcList != null && apcList.size() > 0) {
			log.info("场景c2(c)   存在一户通客户关系");
		} else {
			log.info("场景c2(c)   建新一户通客户关系");
			saveMrsAccountPlatformCustAsset(reqVo, retrunMrsAccountDto);
		}
		// 需要同步到客户系统
		if(MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(newPlatform.getIsAuth())){
			log.info("场景c6 保存同步信息到通知表");
			MrsOrganDto organ = mrsOrganDtoMapper.selectByPrimaryKey(retrunMrsAccountDto.getUserInfoId());
			saveNotifyInfoAsset(reqVo,organ);
		}
		// 回最新的一户通账号信息
		// 1，已存在用户且状态为生效的，不返回密码 2. 已经登录过门户系统，不返回密码。
		/*MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(retrunMrsAccountDto.getCustId());
		if (MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(retrunMrsAccountDto.getAccountStatus())
				|| (null != loginUser && null != loginUser.getLoginTime())) {
			log.info("一户通账户[custId={}]账户状态是正常或已经登录过门户系统,不返回密码", retrunMrsAccountDto.getCustId());
		} else {
			//返回密码
			if (null != loginUser) {
				String loginPwd = RandomUtil.generate6Random();
				String encryLoginPwd = MD5Utils.MD5(MD5Utils.MD5(loginPwd) + SHIEConfigConstant.SALT);
				loginUser.setLoginPwd(encryLoginPwd);
				mrsLoginUserDtoMapper.updateByPrimaryKeySelective(loginUser);
				retrunMrsAccountDto.setNoEncryptloginPwd(loginPwd);
				log.info("一户通账户[custId={}]账户状态为生效，或没有登录过门户系统,返回新创建的登录密码", retrunMrsAccountDto.getCustId());
			}
		}*/
		return retrunMrsAccountDto;
	}
	/**
	 * 保存同步信息到通知表
	 * @param reqVo
	 */
	private void saveNotifyInfo(UnitRequestVO reqVo,MrsOrganDto organ) {
		BeanUtils.copyProperties(reqVo, organ);
		organ.setUpdateTime(new Date());
		MrsNotifyDto mrsNotifyDto = generateSyncInfo(organ, MrsNotifyType.ORGAN);
		mrsNotifyDtoMapper.insert(mrsNotifyDto);
	}
	/**
	 * 保存同步信息到通知表
	 * @param reqVo
	 */
	private void saveNotifyInfoAsset(UnitAssetRequestVo reqVo,MrsOrganDto organ) {
		BeanUtils.copyProperties(reqVo, organ);
		organ.setUpdateTime(new Date());
		MrsNotifyDto mrsNotifyDto = generateSyncInfo(organ, MrsNotifyType.ORGAN);
		mrsNotifyDtoMapper.insert(mrsNotifyDto);
	}
	/**
	 * 建新一户通客户关系
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 */
	private void saveMrsAccountPlatformCust(UnitRequestVO reqVo, MrsAccountDto retrunMrsAccountDto) {
		MrsAccountPlatformCustDto actPlatformDto = new MrsAccountPlatformCustDto();
		actPlatformDto.setId(actPlatformDto.getIdentity());
		actPlatformDto.setCustId(retrunMrsAccountDto.getCustId());
		actPlatformDto.setSource(reqVo.getPlatformCode());
		actPlatformDto.setCreateTime(new Date());
		mrsAccountPlatformCustDtoMapper.insert(actPlatformDto);
	}
	/**
	 * 建新一户通客户关系
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 */
	private void saveMrsAccountPlatformCustAsset(UnitAssetRequestVo reqVo, MrsAccountDto retrunMrsAccountDto) {
		MrsAccountPlatformCustDto actPlatformDto = new MrsAccountPlatformCustDto();
		actPlatformDto.setId(actPlatformDto.getIdentity());
		actPlatformDto.setCustId(retrunMrsAccountDto.getCustId());
		actPlatformDto.setSource(reqVo.getPlatformCode());
		actPlatformDto.setPlatformCustCode(reqVo.getPlatformCustCode());
		actPlatformDto.setCreateTime(new Date());
		mrsAccountPlatformCustDtoMapper.insert(actPlatformDto);
	}
//	/**
//	 * 开通资金账户
//	 * 
//	 * @param custId
//	 * @param type
//	 * @param customerName
//	 * @throws Exception
//	 */
//	private void createAccounts(String custId, MrsCustomerType type, String customerName,
//			List<MrsConfSubAcctDto> mrsConfSubAcctDtos) throws CodeCheckedException {
//		// 根据客户类型，关联关系查询子账户配置
//		if (mrsConfSubAcctDtos != null && mrsConfSubAcctDtos.size() > 0) {
//			List<String> confSubIds = new ArrayList<String>();
//			for (MrsConfSubAcctDto mrsConfSubAcctDto : mrsConfSubAcctDtos) {
//				confSubIds.add(mrsConfSubAcctDto.getId());
//			}
//			// 开通资金账户
//			// 科目支持业务类型信息 科目编号
//			List<String> mrsSubPayBusiDtos = mrsSubPayBusiDtoMapper.findByConfSubAcctIds(confSubIds);
//			// 开通资金账户需要初始化类
//			List<AccountSubsPojo> subPojos = new ArrayList<AccountSubsPojo>();
//			for (String sub : mrsSubPayBusiDtos) {
//				AccountSubsPojo pojo = new AccountSubsPojo();
//				pojo.setActSub(sub);
//				pojo.setActBusiType(AcctBusiType.YES_DEBIT_CERDIT);
//				subPojos.add(pojo);
//			}
//			// 开资金账户
//			try {
//				actBookAppService.createAccounts(custId, customerName, subPojos);
//			} catch (Exception e) {
//				log.error("调用actBookAppService.createAccounts开资金账户失败");
//				throw new CodeCheckedException("调用actBookAppService.createAccounts开资金账户失败");
//			}
//			
//		} 
//	}
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto initOpenAcnt(UnitAssetRequestVo requestVo, List<MrsOrganDto> organDtoList) throws CodeCheckedException {
		// 根据接入平台编号查询平台信息
		MrsPlatformDto newPlatform = mrsPlatformDaoMapper.findByPlatformCode(requestVo.getPlatformCode(), MrsPlatformStatus.PS_0.getValue());
		if(newPlatform == null) {
			log.error("开户渠道不存在，查询条件[status = {},platformCode={}]", MrsPlatformStatus.PS_0.getValue(),requestVo.getPlatformCode());
			throw new CodeCheckedException(PortalCode.CODE_9999, "不存在有效的开户渠道!");
		}
		List<MrsAccountDto> acctList = new ArrayList<MrsAccountDto>();
		for (MrsOrganDto dto : organDtoList) {
			MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(dto.getCustId());
			// 更新客户信息
			log.info("更新新客户信息");
			senseAssetAccount(requestVo, mrsAccountDto, dto, newPlatform);
			acctList.add(mrsAccountDto);
		}

		// 回最新的一户通账号信息
		MrsAccountDto accountDto = latelyAccount(acctList);
		MrsOrganDto mrsOrganDto = mrsOrganDtoMapper.findByCustId(accountDto.getCustId());
		if(MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(newPlatform.getIsAuth())){
    		// 需要同步到客户系统 
			MrsNotifyDto notifyDto = generateSyncInfo(mrsOrganDto, MrsNotifyType.ORGAN);
			mrsNotifyDtoMapper.insert(notifyDto);
    	}
		return accountDto;
	}
	
	@Override
	public void addAccount(UnitAssetRequestVo unitReqVo,String custId) {
		MrsPlatformDto newPlatform = mrsPlatformDaoMapper.findByPlatformCode(unitReqVo.getPlatformCode(), MrsPlatformStatus.PS_0.getValue());
		MrsSubAccountDto accountDto= generateSubAccount(custId, newPlatform, MrsSubAccountType.MSAT_03.getValue());
		mrsSubAccountDtoMapper.insert(accountDto);

	}

	/**
	 * 根据不同场景更新客户信息 0:不需要更新 1:只需要更新客户信息，不需要修改接入平台信息,不更新客户编号customerCode
	 * 2:需要更新客户信息，需要修改接入平台的信息,不更新客户编号customerCode，修改认证状态为无需认证
	 * 3:需要更新客户信息、客户编号customerCode，不需要修改接入平台信息 场景 新请求类型 原记录 结果 1 可信渠道 认证成功 或
	 * 无需认证 1 2 可信渠道 未认证 认证中 认证失败 2 3 不可信渠道 认证成功 或 无需认证 0 4 不可信渠道 未认证
	 * 数据库客户编号为空且请求platformCustCode为空 3 5 不可信渠道 未认证
	 * !(数据库客户编号为空且请求platformCustCode为空) 0 6 不可信渠道 认证中 认证失败 0
	 * 
	 * @param reqVo
	 * @param account
	 * @param authStatus
	 * @param newIsAuth
	 */
	private void senseAccount(UnitRequestVO reqVo, MrsAccountDto account,
			MrsPlatformDto newPlatform) {
		log.info("senseAccount方法执行....");
		MrsOrganDto organ = new MrsOrganDto();
		MrsAccountAuthStatus authStatus = MrsAccountAuthStatus.getEnum(account.getAuthStatus());
		MrsPlatformIsAuth newIsAuth = MrsPlatformIsAuth.getEnum(newPlatform.getIsAuth());
		if (MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_1.equals(newIsAuth)) {
			// 场景a原记录已经认证成功,新请求是可信渠道 只需要更新客户信息
			log.info("场景a   只更新客户信息");
			BeanUtils.copyProperties(reqVo, organ);
			organ.setUpdateTime(new Date());
			organ.setId(account.getUserInfoId());
			organ.setContactsMobile(reqVo.getContactsMoblie());
			mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
			
		} else if (!MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_1.equals(newIsAuth)) {
			// 场景b 原记录已经认证未成功,新请求是可信渠道
			log.info("场景b  更新“机构客户信息表”机构信息，需要更新“一户通账户表");
			BeanUtils.copyProperties(reqVo, organ);
			organ.setUpdateTime(new Date());
			organ.setId(account.getUserInfoId());
			organ.setContactsMobile(reqVo.getContactsMoblie());
			mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);

			if(MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(account.getAccountStatus())){
				account.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
				account.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
				account.setOpenTime(new Date());
				account.setUpdateTime(new Date());
				mrsAccountDtoMapper.updateByPrimaryKeySelective(account);
			}
			//给用户发邮件，重置密码
			MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(account.getCustId());
			sendEmailOrgan(reqVo, loginUser,account.getCustId());
			
		} else if (MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_0.equals(newIsAuth)) {
			// 场景c 原记录已经认证成功,新请求是不可信渠道
			log.info("场景c  更新信息证件信息");
			organ.setId(account.getUserInfoId());
			updateCredentials(reqVo, organ);
		} else if (!MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_0.equals(newIsAuth)) {
			// 场景d原记录已经认证未成功,新请求是不可信渠道
			log.info("原记录已经认证未成功,新请求是不可信渠道");
			if (MrsAccountAuthStatus.MAAS_0.equals(authStatus)
					&& SHIEConfigConstant.GS_CORE.equals(reqVo.getPlatformCode())
					&& StringUtil.isEmpty(account.getCustomerCode())) {
				// 场景4 请求为渠道是否为ecif
				log.info("场景d  需要更新客户信息、客户编号");
				BeanUtils.copyProperties(reqVo, organ);
				organ.setUpdateTime(new Date());
				organ.setId(account.getUserInfoId());
				organ.setContactsMobile(reqVo.getContactsMoblie());
				mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);

				account.setUpdateTime(new Date());
				account.setCustomerCode(reqVo.getCustomerCode());
				mrsAccountDtoMapper.updateByPrimaryKey(account);
			} else {
				log.info("场景e 更新信息证件信息");
				organ.setId(account.getUserInfoId());
				updateCredentials(reqVo, organ);
			}
		}
	}
	/**
	 * 根据不同场景更新客户信息 0:不需要更新 1:只需要更新客户信息，不需要修改接入平台信息,不更新客户编号customerCode
	 * 2:需要更新客户信息，需要修改接入平台的信息,不更新客户编号customerCode，修改认证状态为无需认证
	 * 3:需要更新客户信息、客户编号customerCode，不需要修改接入平台信息 场景 新请求类型 原记录 结果 1 可信渠道 认证成功 或
	 * 无需认证 1 2 可信渠道 未认证 认证中 认证失败 2 3 不可信渠道 认证成功 或 无需认证 0 4 不可信渠道 未认证
	 * 数据库客户编号为空且请求platformCustCode为空 3 5 不可信渠道 未认证
	 * !(数据库客户编号为空且请求platformCustCode为空) 0 6 不可信渠道 认证中 认证失败 0
	 * 
	 * @param reqVo
	 * @param account
	 * @param authStatus
	 * @param newIsAuth
	 */
	private void senseAccountAsset(UnitAssetRequestVo reqVo, MrsAccountDto account,
			MrsPlatformDto newPlatform) {
		log.info("senseAccount方法执行....");
		MrsOrganDto organ = new MrsOrganDto();
		MrsAccountAuthStatus authStatus = MrsAccountAuthStatus.getEnum(account.getAuthStatus());
		MrsPlatformIsAuth newIsAuth = MrsPlatformIsAuth.getEnum(newPlatform.getIsAuth());
		if (MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_1.equals(newIsAuth)) {
			// 场景a原记录已经认证成功,新请求是可信渠道 只需要更新客户信息
			log.info("场景a   只更新客户信息");
			BeanUtils.copyProperties(reqVo, organ);
			organ.setUpdateTime(new Date());
			organ.setId(account.getUserInfoId());
			organ.setContactsMobile(reqVo.getContactsMoblie());
			mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
			
		} else if (!MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_1.equals(newIsAuth)) {
			// 场景b 原记录已经认证未成功,新请求是可信渠道
			log.info("场景b  更新“机构客户信息表”机构信息，需要更新“一户通账户表");
			BeanUtils.copyProperties(reqVo, organ);
			organ.setUpdateTime(new Date());
			organ.setId(account.getUserInfoId());
			organ.setContactsMobile(reqVo.getContactsMoblie());
			mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
			
			if(MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(account.getAccountStatus())){
				account.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
				account.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
				account.setOpenTime(new Date());
				account.setUpdateTime(new Date());
				mrsAccountDtoMapper.updateByPrimaryKeySelective(account);
			}
			//给用户发邮件，重置密码
			MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(account.getCustId());
			sendEmail(reqVo, loginUser,account.getCustId());
		} else if (MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_0.equals(newIsAuth)) {
			// 场景c 原记录已经认证成功,新请求是不可信渠道
			log.info("场景c  更新信息证件信息");
			organ.setId(account.getUserInfoId());
			updateCredentialsAsset(reqVo, organ);
		} else if (!MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_0.equals(newIsAuth)) {
			// 场景d原记录已经认证未成功,新请求是不可信渠道
			log.info("原记录已经认证未成功,新请求是不可信渠道");
			if (MrsAccountAuthStatus.MAAS_0.equals(authStatus)
					&& SHIEConfigConstant.GS_CORE.equals(reqVo.getPlatformCode())
					&& StringUtil.isEmpty(account.getCustomerCode())) {
				// 场景4 请求为渠道是否为ecif
				log.info("场景d  需要更新客户信息、客户编号");
				BeanUtils.copyProperties(reqVo, organ);
				organ.setUpdateTime(new Date());
				organ.setContactsMobile(reqVo.getContactsMoblie());
				mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
			} else {
				log.info("场景e 更新信息证件信息");
				organ.setId(account.getUserInfoId());
				updateCredentialsAsset(reqVo, organ);
			}
		}
	}
	
	/**
	 * 根据不同场景更新客户信息
	 * 	0:不需要更新
	 * 	1:只需要更新客户信息，不需要修改接入平台信息,不更新客户编号customerCode
	 * 	2:需要更新客户信息，需要修改接入平台的信息,不更新客户编号customerCode，修改认证状态为无需认证
	 *  3:需要更新客户信息、客户编号customerCode，不需要修改接入平台信息
	 *  场景	新请求类型			原记录		 								    结果
	 *   1	可信渠道 		认证成功  或  无需认证   								   1
	 *   2	可信渠道		未认证  认证中  认证失败								   2
	 *   3	不可信渠道		认证成功  或 无需认证									   0
	 *   4	不可信渠道		未认证  数据库客户编号为空且请求platformCustCode为空		   3
	 *   5	不可信渠道		未认证  !(数据库客户编号为空且请求platformCustCode为空)	   0
	 *   6	不可信渠道		认证中  认证失败										   0
	 * @param reqVo
	 * @param account
	 * @param authStatus
	 * @param newIsAuth
	 */
	private void senseAssetAccount(UnitAssetRequestVo reqVo, MrsAccountDto account, MrsOrganDto organ, MrsPlatformDto newPlatform) {
		log.info("senseAccount方法执行....");
		MrsAccountAuthStatus authStatus = MrsAccountAuthStatus.getEnum(account.getAuthStatus());
		MrsPlatformIsAuth newIsAuth = MrsPlatformIsAuth.getEnum(newPlatform.getIsAuth());
		if(MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_1.equals(newIsAuth)) {
			// 场景1  原记录已经认证成功,新请求是可信渠道   只需要更新客户信息 
			log.info("场景1   只更新客户信息");
			reqVo.setPlatformCustCode(null);
			BeanUtils.copyProperties(reqVo, organ);
			organ.setUpdateTime(new Date());
			mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
		} else if(!MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_1.equals(newIsAuth)) {
			// 场景2   原记录已经认证未成功,新请求是可信渠道
			log.info("场景2  更新客户信息，修改接入平台的信息  修改认证状态为无需认证");
			reqVo.setPlatformCustCode(null);
			BeanUtils.copyProperties(reqVo, organ);
			organ.setUpdateTime(new Date());;
			mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
			account.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
			account.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
			account.setOpenTime(new Date());
			account.setPlatformCode(newPlatform.getPlatformCode());
			account.setUpdateTime(new Date());
			mrsAccountDtoMapper.updateByPrimaryKey(account);
			mrsSubAccountDtoMapper.updateByCustId(account.getCustId(), MrsSubAccountStatus.MSAS_0.getValue(),new Date());
		} else if(MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_0.equals(newIsAuth)) {
			// 场景3   原记录已经认证成功,新请求是不可信渠道
			log.info("场景3  更新信息证件信息");
			organ.setId(account.getUserInfoId());
			updateAssetCredentials(reqVo, organ);  
		} else if(!MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_0.equals(newIsAuth)) {
			// 场景4、5、6   原记录已经认证未成功,新请求是不可信渠道
			log.info("原记录已经认证未成功,新请求是不可信渠道");
			if(MrsAccountAuthStatus.MAAS_0.equals(authStatus) && 
					SHIEConfigConstant.GS_CORE.equals(reqVo.getPlatformCode()) && StringUtil.isEmpty(account.getCustomerCode())) {
				// 场景4  请求为渠道是否为ecif
				log.info("场景4  需要更新客户信息、客户编号");
				BeanUtils.copyProperties(reqVo, organ);
				organ.setUpdateTime(new Date());
				organ.setId(account.getUserInfoId());
				mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
				
				account.setUpdateTime(new Date());
				account.setCustomerCode(reqVo.getPlatformCode());
				mrsAccountDtoMapper.updateByPrimaryKey(account);
			} else {
				// 场景5、6
				log.info("场景5、6 更新信息证件信息");
				organ.setId(account.getUserInfoId());
				updateAssetCredentials(reqVo, organ);
			}
		}
	}

	/**
	 * 更新证件信息
	 */
	public void updateCredentials(UnitRequestVO reqVo, MrsOrganDto organ) {
		if (StringUtil.isNEmpty(reqVo.getBusinessLicence())) {
			organ.setBusinessLicence(reqVo.getBusinessLicence());
		}
		if (StringUtil.isNEmpty(reqVo.getOrganizeCode())) {
			organ.setOrganizeCode(reqVo.getOrganizeCode());
		}
		if (StringUtil.isNEmpty(reqVo.getRevenueCode())) {
			organ.setRevenueCode(reqVo.getRevenueCode());
		}
		if (StringUtil.isNEmpty(reqVo.getSocialCreditCode())) {
			organ.setSocialCreditCode(reqVo.getSocialCreditCode());
		}
		organ.setUpdateTime(new Date());
		mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
	}
	/**
	 * 更新证件信息
	 */
	public void updateCredentialsAsset(UnitAssetRequestVo reqVo, MrsOrganDto organ) {
		//证件类型转换，四个类型的取对应的值，然后其它类型存OtherCode
		if(MrsCredentialsType.MCT_74.getValue().equals(reqVo.getCredentialsType())){
			organ.setSocialCreditCode(reqVo.getCredentialsNumber());
		}else if(MrsCredentialsType.MCT_71.getValue().equals(reqVo.getCredentialsType())){
			organ.setOrganizeCode(reqVo.getCredentialsNumber());
		}else if(MrsCredentialsType.MCT_72.getValue().equals(reqVo.getCredentialsType())){
			organ.setRevenueCode(reqVo.getCredentialsNumber());
		}else if(MrsCredentialsType.MCT_73.getValue().equals(reqVo.getCredentialsType())){
			organ.setBusinessLicence(reqVo.getCredentialsNumber());
		}else{
			organ.setOrganOtherCode(reqVo.getCredentialsNumber());
		}
		organ.setUpdateTime(new Date());
		mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
	}
	/**
	 *  更新三个证件信息（资管）
	 */
	public void updateAssetCredentials(UnitAssetRequestVo reqVo, MrsOrganDto organ){
		if(StringUtil.isNEmpty(reqVo.getBusinessLicence())){
			organ.setBusinessLicence(reqVo.getBusinessLicence());
		}
		if(StringUtil.isNEmpty(reqVo.getOrganizeCode())) {
			organ.setOrganizeCode(reqVo.getOrganizeCode());
		}
		if(StringUtil.isNEmpty(reqVo.getSocialCreditCode())) {
			organ.setSocialCreditCode(reqVo.getSocialCreditCode());
		}
		organ.setUpdateTime(new Date());
		mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
	}
	
	private MrsAccountDto latelyAccount(List<MrsAccountDto> acctList) {
		Date latelyTime = null;
		Date currObjTime = null;
		MrsAccountDto accountDto = null;
		for (MrsAccountDto mrsAccountDto : acctList) {
			currObjTime = mrsAccountDto.getCreateTime();
			if (latelyTime == null) {
				latelyTime = currObjTime;
				accountDto = mrsAccountDto;
			} else {
				// 取latelyTime和currObjTime的最近值
				if (compareTime(latelyTime, currObjTime) < 0) {
					latelyTime = currObjTime;
					accountDto = mrsAccountDto;
				}
			}
		}
		return accountDto;
	}
	/**
	 * 创建登录用户与账户的关系
	 * @param accountId
	 * @param userId
	 * @return
	 */
	private MrsUserAccountDto copy2UserAccount(String accountId, String userId) {
		MrsUserAccountDto userAccount = new MrsUserAccountDto();
		userAccount.setId(UUID.randomUUID().toString());
		userAccount.setAccountId(accountId);
		userAccount.setLoginId(userId);
		userAccount.setIsMain(LoginUserIsMain.IS_MAIN_0.getValue());
		userAccount.setCreateTime(new Date());
		return userAccount;
	}
	/**
	 * 封装一户通信息（机构）
	 * 
	 * @param requestVo
	 * @param custId
	 * @param isAuth
	 * @return
	 */
	private MrsAccountDto copy2AccountDtoAsset(UnitAssetRequestVo requestVo, String custId, String isAuth) {
		MrsAccountDto dto = new MrsAccountDto();
		BeanUtils.copyProperties(requestVo, dto);
		dto.setCustId(custId);
		dto.setAccountName(requestVo.getCustomerName());
		dto.setCustomerType(MrsCustomerType.MCT_1.getValue());
		dto.setAccountSource(MrsAccountSource.SOURCE_05.getValue());
		if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(isAuth)) {
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
		} else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(isAuth)) {
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
		}
		if(AaccountType.BSYHT.getValue().equals(requestVo.getAccountType())){
			dto.setAccountType(AaccountType.BSYHT);
		}else if(AaccountType.CYRYHT.getValue().equals(requestVo.getAccountType())){
			dto.setAccountType(AaccountType.CYRYHT);
		}
		dto.setCreateTime(new Date());
		dto.setOpenTime(new Date());
		dto.setId(UUID.randomUUID().toString());
		dto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
		dto.setExtOrgId(custId);
		return dto;
	}
	/**
	 * 封装一户通信息（机构）
	 * 
	 * @param requestVo
	 * @param custId
	 * @param isAuth
	 * @return
	 */
	private MrsAccountDto copy2AccountDto(UnitRequestVO requestVo, String custId, String isAuth) {
		MrsAccountDto dto = new MrsAccountDto();
		BeanUtils.copyProperties(requestVo, dto);
		dto.setCustId(custId);
		dto.setAccountName(requestVo.getCustomerName());
		dto.setCustomerType(MrsCustomerType.MCT_1.getValue());
		dto.setAccountSource(MrsAccountSource.SOURCE_05.getValue());
		if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(isAuth)) {
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
		} else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(isAuth)) {
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
		}
		if(AaccountType.BSYHT.getValue().equals(requestVo.getAccountType())){
			dto.setAccountType(AaccountType.BSYHT);
		}else if(AaccountType.CYRYHT.getValue().equals(requestVo.getAccountType())){
			dto.setAccountType(AaccountType.CYRYHT);
		}
		dto.setCreateTime(new Date());
		dto.setOpenTime(new Date());
		dto.setId(UUID.randomUUID().toString());
		dto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
		dto.setExtOrgId(custId);
		return dto;
	}
	/**
	 * 封装一户通信息（机构资管）
	 * @param requestVo
	 * @param custId
	 * @param isAuth
	 * @return
	 */
	private MrsAccountDto copyAsset2AccountDto(UnitAssetRequestVo requestVo, String custId, String isAuth) {
		MrsAccountDto dto = new MrsAccountDto();
		BeanUtils.copyProperties(requestVo, dto);
		dto.setCustId(custId);
		dto.setAccountName(requestVo.getCustomerName());
		dto.setCustomerType(MrsCustomerType.MCT_1.getValue());
		dto.setAccountSource(MrsAccountSource.SOURCE_05.getValue());
		if(MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(isAuth)){
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
		} else if(MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(isAuth)){
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
		}
		if("1".equals(requestVo.getAccountType())){
			dto.setAccountType(AaccountType.BSYHT);
		}else if("2".equals(requestVo.getAccountType())){
			dto.setAccountType(AaccountType.CYRYHT);
		}
		dto.setCreateTime(new Date());
		dto.setId(UUID.randomUUID().toString());
		dto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
		return dto;
	}
	/**
	 * 新增子账户
	 * 
	 * @param custId
	 * @param mrsPlatformDto
	 * @param subAcntType
	 * @return
	 */
	private MrsSubAccountDto generateSubAccount(String custId, MrsPlatformDto mrsPlatformDto, String subAcntType) {
		MrsSubAccountDto subAccount  = new MrsSubAccountDto();
		subAccount.setId(UUID.randomUUID().toString());
		subAccount.setCustId(custId);	// 一户通编号
		String subAccountCode = subAcntType + "0" +custId;
		subAccount.setSubAccountCode(subAccountCode);	// 子账户编号 
		subAccount.setSubAccountType(subAcntType);	// 子账户类型
		if(MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(mrsPlatformDto.getIsAuth())){
			subAccount.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());	// 子账户状态
		} else if(MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())){
			subAccount.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());	// 子账户状态
		}
		subAccount.setOpenTime(new Date());
		subAccount.setAccountOwnType(MrsSubAccountOwnType.MSAOT_01.getValue());	// 账户持有类型
		subAccount.setAccountSource(MrsAccountSource.SOURCE_05.getValue());	// 开户方式
		subAccount.setPlatformCode(mrsPlatformDto.getPlatformCode());	// 开户渠道
		subAccount.setCreateTime(new Date());		// 创建时间
		return subAccount; 
		
}

	/**
	 * 生成两个子账户信息
	 * 
	 * @return
	 * @throws CodeCheckedException 
	 */
	private List<MrsSubAccountDto> generateSubAccount(String custId, MrsPlatformDto dto,List<MrsConfSubAcctDto> mrsConfSubAcctDtos) throws CodeCheckedException {
		List<MrsSubAccountDto> subAccountDtoList = new ArrayList<MrsSubAccountDto>();
		if(mrsConfSubAcctDtos!=null && mrsConfSubAcctDtos.size()>0){
			for(MrsConfSubAcctDto mrsConfSubAcctDto :mrsConfSubAcctDtos ){
				MrsSubAccountDto insureAccountDto = new MrsSubAccountDto();
				insureAccountDto.setId(UUID.randomUUID().toString());
				insureAccountDto.setCustId(custId);	// 一户通编号
				String subInsure = mrsConfSubAcctDto.getSubAccountCode() + "0" + custId;
				insureAccountDto.setSubAccountCode(subInsure);	// 子账户编号 
				insureAccountDto.setSubAccountName(mrsConfSubAcctDto.getSubAccountName());//子账户名称
				insureAccountDto.setSubAccountType(mrsConfSubAcctDto.getSubAccountCode());	// 子账户类型
				if(MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(dto.getIsAuth())){
					insureAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());	// 子账户状态
				} else if(MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(dto.getIsAuth())){
					insureAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());	// 子账户状态
				}
				insureAccountDto.setAccountOwnType(MrsSubAccountOwnType.MSAOT_01.getValue());	// 账户持有类型
				insureAccountDto.setAccountSource(MrsAccountSource.SOURCE_05.getValue());	// 开户方式
				insureAccountDto.setPlatformCode(dto.getPlatformCode());	// 开户渠道
				insureAccountDto.setOpenTime(new Date());		// 开户时间
				insureAccountDto.setCreateTime(new Date());		// 创建时间
				subAccountDtoList.add(insureAccountDto);
			}
		}
		return subAccountDtoList;

	}
	/**
	 * 巨灾机构数据
	 * @param requestVo
	 * @param custId
	 * @return
	 */
	private MrsOrganDto copy2OrganDto(UnitRequestVO requestVo, String custId) {
		MrsOrganDto dto = new MrsOrganDto();
		BeanUtils.copyProperties(requestVo, dto);
		dto.setCreateTime(new Date());
		dto.setContactsMobile(requestVo.getContactsMoblie());
		dto.setId(UUID.randomUUID().toString());
		dto.setCustId(custId);
		return dto;
	}
	/**
	 * 资管机构数据
	 * @param requestVo
	 * @param custId
	 * @return
	 */
	private MrsOrganDto copyAsset2OrganDto(UnitAssetRequestVo requestVo, String custId) {
		MrsOrganDto dto = new MrsOrganDto();
		BeanUtils.copyProperties(requestVo, dto);
		dto.setCreateTime(new Date());
		dto.setId(UUID.randomUUID().toString());
		dto.setCustId(custId);
		dto.setContactsMobile(requestVo.getContactsMoblie());
		//证件类型转换，四个类型的取对应的值，然后其它类型存OtherCode
		if(MrsCredentialsType.MCT_74.getValue().equals(requestVo.getCredentialsType())){
			dto.setSocialCreditCode(requestVo.getCredentialsNumber());
		}else if(MrsCredentialsType.MCT_71.getValue().equals(requestVo.getCredentialsType())){
			dto.setOrganizeCode(requestVo.getCredentialsNumber());
		}else if(MrsCredentialsType.MCT_72.getValue().equals(requestVo.getCredentialsType())){
			dto.setRevenueCode(requestVo.getCredentialsNumber());
		}else if(MrsCredentialsType.MCT_73.getValue().equals(requestVo.getCredentialsType())){
			dto.setBusinessLicence(requestVo.getCredentialsNumber());
		}else{
			dto.setOrganOtherCode(requestVo.getCredentialsNumber());
		}
		return dto;
	}
	
	private MrsLoginUserDto copy2LoginUserDto(String custId,String userEmail) {
		MrsLoginUserDto dto = new MrsLoginUserDto();
		dto.setCustId(custId);
		dto.setCreateTime(new Date());
		String loginPwd = RandomUtil.generate6Random();
		dto.setNoEncryptloginPwd(loginPwd); // 明文密码
		loginPwd = MD5Utils.MD5(loginPwd);
		loginPwd = MD5Utils.MD5(loginPwd + SHIEConfigConstant.SALT);
		dto.setAccountCode(custId);
		dto.setLoginPwd(loginPwd);
		dto.setAccountCode(custId);
		dto.setId(UUID.randomUUID().toString());
		dto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
		//如果客户填写邮箱，就同步更新到登录表
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(userEmail)) {
			dto.setEmail(userEmail);
		}
		return dto;
	}

	/**
	 * 1,根据账号和证件信息查找未生效状态的一户通账号并降序排列，(只有全部符合条件才能查出数据) 2,判断一户通信息是否为空，如果为空直接返回
	 * 3,如果存在多条数据，则取第一条一户通账号 3,重新生成登陆密码(因为之前生成的密码是密文形式，不支持解密) 4,修改登陆密码 5,返回登陆信息
	 * 
	 * @throws CodeCheckedException
	 */
	@Override
	public LoginMsgSearchResponseVO findLoginMsg(LoginMsgSearchRequestVO reqVo) throws CodeCheckedException {
		String custId = reqVo.getAccountCode();
		String customerName = reqVo.getCustomerName();
		String socialCreditCode = reqVo.getSocialCreditCode();
		String organizeCode = reqVo.getOrganizeCode();
		String revenueCode = reqVo.getRevenueCode();
		String businessLicence = reqVo.getBusinessLicence();
		LoginMsgSearchResponseVO respVo = new LoginMsgSearchResponseVO();
		// 根据三要素获取机构客户信息
		List<MrsAccountDto> organList = mrsAccountDtoMapper.findByOrgan3Element(customerName, socialCreditCode, organizeCode, revenueCode, businessLicence,null);
		if (CollectionUtil.isEmpty(organList)) {
			log.info("一户通账户[name={},socialCreditCode={},organizeCode={},revenueCode={},businessLicence={}]不存在",
					new Object[] { customerName, socialCreditCode, organizeCode, revenueCode, businessLicence });
			return null;
		}
		// 过滤证件号码部分相同的数据
		log.info("查询到{}条机构客户信息", organList.size());
		List<MrsAccountDto> orgList = MrsPassiveOpenAcntServiceImpl.organIsExist(organList, socialCreditCode,
				organizeCode, revenueCode, businessLicence);
		List<String> custIds = new ArrayList<String>();
		for (MrsAccountDto mrsOrganDto : orgList) {
			custIds.add(mrsOrganDto.getCustId());
		}
		log.info("过滤证件号码部分相同的数据结果[custIds={}]", custIds);

		if (CollectionUtil.isEmpty(custIds)) {
			log.info("证件信息不存在，返回空！");
			return null;
		}
		MrsAccountDto accountDto = null;
		if (StringUtil.isNEmpty(custId)) {
			// 入参custId不为空，校验custId是否存在
			if (custIds.contains(custId)) {
				// 返回custId的账户和密码
				log.info("一户通编号[custId={}]匹配", custId);
				accountDto = mrsAccountDtoMapper.findByCustId(custId);
			} else {
				// 报错 custId和证件信息不匹配
				log.error("证件信息和一户通账号[custId={}]不匹配", custId);
				throw new CodeCheckedException(PortalCode.CODE_9999, "证件信息和一户通账号不匹配");
			}
		} else {
			// 入参custId为空
			accountDto = getLatelyAccount(custIds);
		}
		custId = accountDto.getCustId();
		log.info("返回账号:custId={}", custId);
//		if (MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(accountDto.getAccountStatus())) {
//			// 未生效
//			String loginPwd = RandomUtil.generate6Random();
//			String encryLoginPwd = MD5Utils.MD5(MD5Utils.MD5(loginPwd) + SHIEConfigConstant.SALT);
//			MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(custId);
//			loginUser.setLoginPwd(encryLoginPwd);
//			mrsLoginUserDtoMapper.updateByPrimaryKeySelective(loginUser);
//			respVo.setCustId(custId);
//			respVo.setLoginPwd(loginPwd);
//			respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_0);
//		} else {
//			//
//			respVo.setCustId(custId);
//			respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_1);
//		}
		if(MrsAccountAuthStatus.MAAS_2.getValue().equals(accountDto.getAuthStatus())
				||MrsAccountAuthStatus.MAAS_9.getValue().equals(accountDto.getAuthStatus())
				||MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(accountDto.getAccountStatus())){
			log.info("一户通账户[custId={}]认证成功或无需认证,或账户状态是正常,不返回密码", custId);
			respVo.setCustId(custId);
			respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_1);
			//非认证状态和非无需认证,返回密码
		}else{
			log.info("一户通账户[custId={}]非认证成功或非无需认证返回密码]", custId);
			MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(custId);
			//已经登录过门户系统,上次登录时间不为空，不返回密码
			if(null != loginUser && null != loginUser.getLoginTime()){
				log.info("一户通账户[custId={}]认证成功或无需认证,或账户状态是正常,不返回密码", custId);
			}else{
				String loginPwd = RandomUtil.generate6Random();
				String encryLoginPwd = MD5Utils.MD5(MD5Utils.MD5(loginPwd) + SHIEConfigConstant.SALT);
				loginUser.setLoginPwd(encryLoginPwd);
				mrsLoginUserDtoMapper.updateByPrimaryKeySelective(loginUser);
				respVo.setLoginPwd(loginPwd);
			}
			respVo.setCustId(custId);
			respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_0);
		}
		return respVo;

	}

	@Override
	public AccountSearchRespVO findAccountExist(AccountSearchReqVO reqVo) throws CodeCheckedException {
		String customerName = reqVo.getCustomerName();
		String socialCreditCode = reqVo.getSocialCreditCode();
		String organizeCode = reqVo.getOrganizeCode();
		String revenueCode = reqVo.getRevenueCode();
		String businessLicence = reqVo.getBusinessLicence();
		AccountSearchRespVO respVo = new AccountSearchRespVO();
		MrsPlatformDto mrsPlatformDto = mrsPlatformDaoMapper.findByPlatformCode(reqVo.getPlatformCode(),
				MrsPlatformStatus.PS_0.getValue());
		if (mrsPlatformDto == null) {
			log.error("开户渠道不存在，查询条件[status={},platformCode={}]",
					new Object[] { MrsPlatformStatus.PS_0.getValue(), reqVo.getPlatformCode() });
			throw new CodeCheckedException(PortalCode.CODE_9999, "开户渠道不存在!");
		}
		// 根据三要素获取机构客户信息
		List<MrsAccountDto> organList = mrsAccountDtoMapper.findByOrgan3Element(customerName, socialCreditCode, organizeCode, revenueCode, businessLicence,null);
		if (CollectionUtil.isEmpty(organList)) {
			log.info("一户通账户[name={},socialCreditCode={},organizeCode={},revenueCode={},businessLicence={}]不存在",
					new Object[] { customerName, socialCreditCode, organizeCode, revenueCode, businessLicence });
			respVo.setHasAccount("N");
			return respVo;
		}
		log.info("查询到{}条机构客户信息", organList.size());
		List<MrsAccountDto> orgList = MrsPassiveOpenAcntServiceImpl.organIsExist(organList, socialCreditCode,
				organizeCode, revenueCode, businessLicence);
		if (CollectionUtil.isEmpty(orgList)) {
			log.info("过滤后结果为空");
			respVo.setHasAccount("N");
			return respVo;
		} else {
			log.info("过滤证件号码部分相同的数据结果{}条", orgList.size());
			respVo.setHasAccount("Y");
			return respVo;
		}

	}

	/**
	 * 根据custId列表获取客户号 如果存在
	 * 
	 * @param custIds
	 * @return
	 */
	private MrsAccountDto getLatelyAccount(List<String> custIds) {
		List<MrsAccountDto> list = mrsAccountDtoMapper.findByCustIds(custIds);
		Date latelyDate = null;
		MrsAccountDto accountDto = null;
		for (MrsAccountDto mrsAccountDto : list) {
			if (!MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(mrsAccountDto.getAccountStatus())) {
				// 如果存在不是“未有效”状态的账户 直接返回
				return mrsAccountDto;
			} else {
				if (latelyDate == null) {
					latelyDate = mrsAccountDto.getCreateTime();
					accountDto = mrsAccountDto;
				} else {
					if (compareTime(latelyDate, mrsAccountDto.getCreateTime()) < 0) {
						latelyDate = mrsAccountDto.getCreateTime();
						accountDto = mrsAccountDto;
					}
				}
			}
		}
		return accountDto;
	}

	@Override
	public PageData<MrsOrganDto> findOrgan(PageData<MrsOrganDto> pageData, MrsOrganDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsOrganDto> list = mrsOrganDtoMapper.list(searchDto);
		Page<MrsOrganDto> page = (Page<MrsOrganDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<MrsOrganDto> findOrganByUpdateAudit(PageData<MrsOrganDto> pageData, MrsOrganDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsOrganDto> list = mrsOrganDtoMapper.updateAuditList(searchDto);
		Page<MrsOrganDto> page = (Page<MrsOrganDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public MrsOrganDto findById(String id) {
		return mrsOrganDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public MrsOrganDto findByCustId(String custId) {
		return mrsOrganDtoMapper.findByCustId(custId);
	}

	@Override
	public OrganVO findOrganVoByCustId(String custId) {
		return mrsOrganDtoMapper.findOrganVoByCustId(custId);
	}

	@Override
	public void update(MrsOrganDto dto) {
		mrsOrganDtoMapper.updateByPrimaryKeySelective(dto);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateAndSync(MrsOrganDto dto) {
		mrsOrganDtoMapper.updateByPrimaryKey(dto);
		MrsNotifyDto notifyDto = generateSyncInfo(dto, MrsNotifyType.ORGAN);
		mrsNotifyDtoMapper.insert(notifyDto);
		// 同步证件信息
		MrsNotifyDto fileNotifyDto = generateSyncFileInfo(dto, MrsNotifyType.FILE);
		if (fileNotifyDto != null) {
			mrsNotifyDtoMapper.insert(fileNotifyDto);
		}
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updatePicAndSync(MrsOrganDto dto) {
		mrsOrganDtoMapper.updateByPrimaryKey(dto);

		// 同步证件信息
		MrsNotifyDto fileNotifyDto = generateSyncFileInfo(dto, MrsNotifyType.FILE);
		if (fileNotifyDto != null) {
			mrsNotifyDtoMapper.insert(fileNotifyDto);
		}
	}

	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitProductResponseVo doUpdateAccountStatus(MrsActAccountVo mrsActAccountVo) {
		SaveAduitProductResponseVo respvo = new SaveAduitProductResponseVo();
		try {
			// 一户通信息
			MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(mrsActAccountVo.getCustId());
			// 子账户信息
			List<MrsSubAccountDto> mrsSubAccountList = mrsSubAccountDtoMapper.findByCustId(mrsActAccountVo.getCustId());
			// 资金账户信息
			List<ActAccountDto> actAccountList = actAccountDtoMapper.findListByCustId(mrsActAccountVo.getCustId());
			//机构产品信息
			MrsOrganDto mrsOrganDto = mrsOrganDtoMapper.findByCustId(mrsActAccountVo.getCustId());
			if (actAccountList == null||actAccountList.isEmpty()) {
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("获取资金账户为空!");
				return respvo;
			}
			
			//一户通状态为注销不能修改
			MrsAccountDto account = mrsAccountDtoMapper.findByCustId(mrsActAccountVo.getCustId());
			if(account==null){
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("数据有误，一户通信息不存在!");
				return respvo;
			}
			
			if( MrsAccountStatus.ACCOUNT_STATUS_2.getDisplayName().equals(account.getAccountStatusName()) ){
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("一户通状态为注销不能修改!");
				return respvo;
			}

			// 生成审核记录
			MrsAduitInfoDto mrsAduitInfoDto = new MrsAduitInfoDto();
			// 查询审核配置表获取审核人数
			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					EOperaTypeEnum.OP_MONUPDATE.getValue(), EStartSystemEnum.SYS_COUNTER.getValue());
			if (mrsConfAuditDto == null) {
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("资金账户变更操作未进行配置!");
				return respvo;
			}
			mrsAduitInfoDto.setCustId(mrsOrganDto.getCustId());
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short) 0);
			mrsAduitInfoDto.setBusiType(EOperaTypeEnum.OP_MONUPDATE);
			mrsAduitInfoDto.setStartSystem(EStartSystemEnum.SYS_COUNTER);
			mrsAduitInfoDto.setCartNo(mrsOrganDto.getOganCertNo());
			mrsAduitInfoDto.setCartType(mrsOrganDto.getOganCertType());
			mrsAduitInfoDto.setName(mrsOrganDto.getCustomerName());
			// 机构客户类型
			mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_01);
			if(org.apache.commons.lang3.StringUtils.isNotBlank(mrsActAccountVo.getCurrentUser().getLoginName())
					&& mrsActAccountVo.getCurrentUser().getLoginName().length()>10){
				mrsAduitInfoDto.setCreateOperator(mrsActAccountVo.getCurrentUser().getLoginName().subSequence(0, 10).toString());
			}else{
				mrsAduitInfoDto.setCreateOperator(mrsActAccountVo.getCurrentUser().getLoginName());
			}
			mrsAduitInfoDto.setCreateTime(new Date());
			mrsAduitInfoDto.setProductStatus(mrsAccountDto.getAccountStatus());
			mrsAduitInfoDto.setProductAuthStatus(mrsAccountDto.getAuthStatus());
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAduitInfoDto.setId(UUID.randomUUID().toString());
			mrsAduitInfoDto.setCreateOperator(mrsActAccountVo.getCurrentUser().getLoginName());
			if (!StringUtils.isEmpty(mrsActAccountVo.getCreateRemark())) {
				mrsAduitInfoDto.setRemark(mrsActAccountVo.getCreateRemark());
			}
			// 保存审核信息记录
			mrsAduitInfoDtoMapper.insertSelective(mrsAduitInfoDto);

			// 生成审核人
			// 查询审核配置明细表
			List<MrsConfAuditItemDto> mrsConfAuditItemList = mrsConfAuditItemDtoMapper
					.selectByAuditId(mrsConfAuditDto.getId());
			List<MrsAduitPersonDto> mrsAduitPersonDtoList = new ArrayList<MrsAduitPersonDto>();

			MrsAduitPersonDto aduitPerson = null;
			for (MrsConfAuditItemDto itemDto : mrsConfAuditItemList) {
				aduitPerson = new MrsAduitPersonDto();
				aduitPerson.setAduitId(mrsAduitInfoDto.getId());
				aduitPerson.setAduitStatus(EAduitTypeEnum.ADUIT_WAIT);
				aduitPerson.setAduitUserId(itemDto.getUserId());
				aduitPerson.setAduitUserName(itemDto.getUserName());
				aduitPerson.setType(EDistributTypeEnum.AUTO_DISTRIBUT);
				aduitPerson.setStatus(EStatusEnum.EFFECTIVE);
				aduitPerson.setId(UUID.randomUUID().toString());
				aduitPerson.setCreateTime(new Date());
				mrsAduitPersonDtoList.add(aduitPerson);

			}

			// 保存审核人信息记录
			mrsAduitPersonDtoMapper.batchInsert(mrsAduitPersonDtoList);

			// 原json值
			MrsToJsonDto oldMrsToJson = new MrsToJsonDto();
			oldMrsToJson.setActAccountDtos(actAccountList);
			oldMrsToJson.setMrsAccountDto(mrsAccountDto);
			oldMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
			oldMrsToJson.setMrsOrganDto(mrsOrganDto);
			
			// 新json值
			MrsToJsonDto newMrsToJson = new MrsToJsonDto();
			List<ActAccountDto> accountList = new ArrayList<ActAccountDto>();
	        ActAccountDto acctDto = null;
	        ActSubjectDto subjectDto = null;
	        for(ActAccountDto dto:mrsActAccountVo.getActAccountDtos()){
				if(dto.getOperationType()!=null){
				   acctDto = actAccountDtoMapper.selectByPrimaryKey(dto.getId());
				   subjectDto = actSubjectDtoMapper.getSubjectName(acctDto.getSubjectNo2());
				   acctDto.setSubjectName(subjectDto.getSubjectName());
				   acctDto.setOperationType(dto.getOperationType());
				   accountList.add(acctDto);
				}else{
					continue;
				}
			}
			newMrsToJson.setActAccountDtos(accountList);
			newMrsToJson.setMrsAccountDto(mrsAccountDto);
			newMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
			newMrsToJson.setMrsOrganDto(mrsOrganDto);
			
			// 创建审核内容信息,保存json串
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			// 将java对象转换为json串
			JSONObject jsons = JSONObject.fromObject(newMrsToJson);
			String str = jsons.toString();
			JSONObject oldJson = JSONObject.fromObject(oldMrsToJson);
			String oldStr = oldJson.toString();

			blobs.setAduitId(mrsAduitInfoDto.getId());
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			blobs.setOldValue(oldStr);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			
			respvo.setIsSucess(true);
			respvo.setMsgCode("");
			respvo.setMsgInfo("资金账户变更申请成功!");
			return respvo;
		} catch (Exception e) {
			e.printStackTrace();
			respvo.setIsSucess(false);
			respvo.setMsgCode("");
			respvo.setMsgInfo("资金账户变更申请失败!");
			return respvo;

		}

	}

	@Override
	public MrsOrganDto findByExtOrgId(String extOrgId) {
		return mrsOrganDtoMapper.findByExOrgNo(extOrgId);
	}

	@Override
	public void updateFileId(String custId, String businessLicenceFileId, String organizeCodeFileId,
			String revenueCodeFileId, String socialcreditCodeFileId, String otherFileId) throws PortalCheckedException {
		// 修改TB_MRS_ORGAN表的
		int rows = mrsOrganDtoMapper.updateFileId(custId, businessLicenceFileId, organizeCodeFileId, revenueCodeFileId,
				socialcreditCodeFileId, otherFileId, new Date());
		if (rows < 0) {
			log.error("更新失败:[rows = " + rows + "]");
			throw new PortalCheckedException(ErrorMsgEnum.FILE_UPLOAD_FAIL.getKey());
		}
		// 修改TB_MRS_ACCOUNT
		rows = mrsAccountDtoMapper.updateAuthStatus(custId, MrsAccountAuthStatus.MAAS_1.getValue(), new Date());
		if (rows < 0) {
			log.error("更新失败:[rows = " + rows + "]");
			throw new PortalCheckedException(ErrorMsgEnum.FILE_UPLOAD_FAIL.getKey());
		}
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public UserCheckVO saveAduitOrganByPortalRest(String custId,String loginUserId,  List<MrsAduitAttachmentDto> mrsAduitAttachmentDtoList) {
		PersonVO response = new PersonVO();
		try {
			//根据一户通号查询对应的机构表
			MrsOrganDto mrsOrganDto = mrsOrganDtoMapper.findByCustId(custId);
			if (mrsOrganDto == null) {
					log.error("机构客户信息不存在！");
					UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
					response.setCheckVo(userCheckVO);
					return userCheckVO;
			}
			// 子账户
			List<MrsSubAccountDto>  confSubIds = mrsSubAccountDtoMapper.findByCustId(custId);
			// 资金账户
			List<ActAccountDto>  actList = actAccountDtoMapper.getUserAccounts(custId);
			List<AccountSubsPojo> subPojos = new ArrayList<AccountSubsPojo>();
			for (ActAccountDto actAccountDto : actList) {
				AccountSubsPojo pojo = new AccountSubsPojo();
				pojo.setActSub(actAccountDto.getSubjectNo2());
				pojo.setActBusiType(AcctBusiType.YES_DEBIT_CERDIT);
				subPojos.add(pojo);
			}
			// 审核主要信息
			MrsAduitInfoDto mrsAduitInfoDto = new MrsAduitInfoDto();
			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					ConfAuditBusiType.OP_CONFIRM.getValue(), EStartSystemEnum.SYS_CUSTOM.getValue());
			if (mrsConfAuditDto == null) {
				log.info("客户端开户没有配置审核信息！");
				UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.NO_CONFIG_AUDIT_INFO);
				response.setCheckVo(userCheckVO);
				return userCheckVO;
			}
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short) 0);
			mrsAduitInfoDto.setBusiType(EOperaTypeEnum.OP_CONFIRM);
			mrsAduitInfoDto.setStartSystem(EStartSystemEnum.SYS_CUSTOM);
			//证件号对应
		    //社会统一信用代码
			if(org.apache.commons.lang.StringUtils.isNotEmpty(mrsOrganDto.getSocialCreditCode())){
				mrsAduitInfoDto.setCartNo(mrsOrganDto.getSocialCreditCode());
				mrsAduitInfoDto.setCartType(MrsCredentialsType.MCT_74.getValue());
			}
			//组织机构代码
			else if(org.apache.commons.lang.StringUtils.isNotEmpty(mrsOrganDto.getOrganizeCode())){
				mrsAduitInfoDto.setCartNo(mrsOrganDto.getOrganizeCode());
				mrsAduitInfoDto.setCartType(MrsCredentialsType.MCT_71.getValue());
			}
			//税务登记代码
			else if(org.apache.commons.lang.StringUtils.isNotEmpty(mrsOrganDto.getRevenueCode())){
				mrsAduitInfoDto.setCartNo(mrsOrganDto.getRevenueCode());
				mrsAduitInfoDto.setCartType(MrsCredentialsType.MCT_72.getValue());
			}
			//营业执照
			else if(org.apache.commons.lang.StringUtils.isNotEmpty(mrsOrganDto.getBusinessLicence())){
				mrsAduitInfoDto.setCartNo(mrsOrganDto.getBusinessLicence());
				mrsAduitInfoDto.setCartType(MrsCredentialsType.MCT_73.getValue());
			}
			//其他证件类型
			else{
				mrsAduitInfoDto.setCartNo(mrsOrganDto.getOrganOtherCode());
				mrsAduitInfoDto.setCartType(MrsCredentialsType.MCT_99.getValue());
			}
			mrsAduitInfoDto.setName(mrsOrganDto.getCustomerName());
			mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_01);
			mrsAduitInfoDto.setCreateOperator(mrsOrganDto.getCustomerName());
			
			if(org.apache.commons.lang3.StringUtils.isNotBlank(mrsOrganDto.getCustomerName())
					&& mrsOrganDto.getCustomerName().length()>10){
				mrsAduitInfoDto.setCreateOperator(mrsOrganDto.getCustomerName().subSequence(0, 10).toString());
			}else{
				mrsAduitInfoDto.setCreateOperator(mrsOrganDto.getCustomerName());
			}
			mrsAduitInfoDto.setCreateTime(new Date());
			mrsAduitInfoDto.setProductStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
			mrsAduitInfoDto.setProductAuthStatus(MrsAccountAuthStatus.MAAS_1.getValue());
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAduitInfoDto.setId(UUID.randomUUID().toString());
			mrsAduitInfoDto.setCustId(custId);
			// 创建审核主要信息
			mrsAduitInfoDtoMapper.insertSelective(mrsAduitInfoDto);

			List<MrsConfAuditItemDto> mrsConfAuditItems = mrsConfAuditItemDtoMapper
					.selectByAuditId(mrsConfAuditDto.getId());

			List<MrsAduitPersonDto> mrsAduitPersonDtos = new ArrayList<MrsAduitPersonDto>();
			// 创建审核人信息
			for (MrsConfAuditItemDto item : mrsConfAuditItems) {
				MrsAduitPersonDto aduitPerson = new MrsAduitPersonDto();
				aduitPerson.setAduitId(mrsAduitInfoDto.getId());
				aduitPerson.setAduitStatus(EAduitTypeEnum.ADUIT_WAIT);
				aduitPerson.setAduitUserId(item.getUserId());
				aduitPerson.setAduitUserName(item.getUserName());
				aduitPerson.setType(EDistributTypeEnum.AUTO_DISTRIBUT);
				aduitPerson.setStatus(EStatusEnum.EFFECTIVE);
				aduitPerson.setId(UUID.randomUUID().toString());
				aduitPerson.setCreateTime(new Date());
				mrsAduitPersonDtos.add(aduitPerson);
			}
			mrsAduitPersonDtoMapper.batchInsert(mrsAduitPersonDtos);
			
			// 登录用户表
			List<MrsLoginUserDto> mrsLoginUserDtoList = new ArrayList<MrsLoginUserDto>();
			MrsLoginUserDto userDto = new MrsLoginUserDto();
			userDto.setId(loginUserId);
			mrsLoginUserDtoList.add(userDto);
			//一户通账户表
			MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(custId);
		
			// JSON
			MrsToJsonDto perMrsToJson = new MrsToJsonDto();
			perMrsToJson.setMrsAccountDto(mrsAccountDto);
			perMrsToJson.setMrsOrganDto(mrsOrganDto);
			perMrsToJson.setMrsAduitAttachmentDtos(mrsAduitAttachmentDtoList);
			perMrsToJson.setMrsSubAccountDtoList(confSubIds);
			perMrsToJson.setAccountSubsPojos(subPojos);
			perMrsToJson.setMrsLoginUserDtoList(mrsLoginUserDtoList);
			perMrsToJson.setOpenUser(true);
			// 创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsons = JSONObject.fromObject(perMrsToJson);// 将java对象转换为json对象
			String str = jsons.toString();
			log.debug("门户提交的机构被动开户送审信息失败！"+str);
			blobs.setAduitId(mrsAduitInfoDto.getId());
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			// 保持客户端开通一户通信息审核关联表
			MrsPortalAccountAduitDto portalAccountAduitDto = new MrsPortalAccountAduitDto();
			portalAccountAduitDto.setId(UUID.randomUUID().toString());
			portalAccountAduitDto.setBusiSource(EStartSystemEnum.SYS_CUSTOM.getValue());
			portalAccountAduitDto.setAduitId(mrsAduitInfoDto.getId());
			portalAccountAduitDto.setLoginId(loginUserId);
			portalAccountAduitDto.setStatus(EStatusEnum.EFFECTIVE.getValue());
			portalAccountAduitDto.setCreateTime(new Date());
			mrsPortalAccountAduitDtoMapper.insertSelective(portalAccountAduitDto);
			/**
			 * 附件信息上传至服务器，保存附件审核信息
			 */
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDtoLists = new ArrayList<MrsAduitAttachmentDto>();
			if(!CollectionUtil.isEmpty(mrsAduitAttachmentDtoList)){
				for(MrsAduitAttachmentDto att : mrsAduitAttachmentDtoList){
					att.setCreateTime(new Date());
					att.setStatus(EStatusEnum.EFFECTIVE.getValue());
					//发起端
					att.setCatalog(EStartSystemEnum.SYS_CUSTOM.getValue());
					att.setAduitContentId(blobs.getId());
					att.setId(UUID.randomUUID().toString());
					mrsAduitAttachmentDtoLists.add(att);
				}
				mrsAduitAttachmentDtoMapper.batchInsert(mrsAduitAttachmentDtoLists);
			}
			// 更改一户通账户 认证状态 
			// 未认证、认证失败 可上传资料 重新认证  状态  改为认证中  等待审核
			if(null!=mrsAccountDto && (MrsAccountAuthStatus.MAAS_0.getValue().equals(mrsAccountDto.getAuthStatus()) 
					|| MrsAccountAuthStatus.MAAS_3.getValue().equals(mrsAccountDto.getAuthStatus()))){
				mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_1.getValue());
				mrsAccountService.updateByPrimaryKey(mrsAccountDto);
			}
						
			UserCheckVO userCheckVO = new UserCheckVO(true);
			return userCheckVO;
		} catch (Exception e) {
			log.info("门户提交的机构被动开户送审信息失败！", e);
			UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			return userCheckVO;
		}
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public UserCheckVO updateFileId(String custId, List<UploadOrganPojo> uploadPojoList) {
		int rows = mrsCertFileDtoMapper.deleteByCustId(custId);
		log.info("根据[custId={}]删除了{}条数据", new Object[] { custId, rows });
		List<MrsCertFileDto> list = new ArrayList<MrsCertFileDto>();
		MrsCertFileDto dto = null;
		for (UploadOrganPojo pojo : uploadPojoList) {
			dto = new MrsCertFileDto();
			dto.setCertType(pojo.getCertType().getValue());
			dto.setCreateTime(new Date());
			dto.setCustId(custId);
			dto.setCustomerType(MrsCustomerType.MCT_1.getValue());
			dto.setFileId(pojo.getFileId());
			dto.setFileType(pojo.getFileType().getValue());
			dto.setId(UUID.randomUUID().toString());
			list.add(dto);
		}
		mrsCertFileDtoMapper.batchInsert(list);
		// 修改TB_MRS_ACCOUNT
		rows = mrsAccountDtoMapper.updateAuthStatus(custId, MrsAccountAuthStatus.MAAS_1.getValue(), new Date());
		if (rows < 0) {
			log.error("更新失败:[rows = " + rows + "]");
			return new UserCheckVO(false, ErrorMsgEnum.FILE_UPLOAD_FAIL);
		}
		return new UserCheckVO(true);
	}

	@Override
	public void updateOrgan(MrsOrganDto dto, String loginName) {
		// 转成json串
		String jsonStr = JsonUtil.ObjectToJson(dto);
		// 保存数据审核信息表
		mrsDataAuditChangeService.save(dto.getId(), EAuditChangeType.AUDIT_UPDATE, jsonStr, EAuditStatus.AUDIT_WAIT,
				loginName, EAuditUserType.AUDIT_CB);
	}

	/**
	 * 如果: date1的时间早于date2 则返回 1 date1的时间等于date2 则返回 0 date1的时间晚于date2 则返回 -1
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	private static int compareTime(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		return cal1.compareTo(cal2);
	}

	@Override
	public int saveMrsUserAccountDto(MrsUserAccountDto mrsUserAccountDto) {
		return mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);
	}

	@Override
	public SaveAduitPersonResponseVo saveAduitOrgan(MrsOrganVo mrsOrganVo) {
		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		MrsAccountDto account = null;
		List<AccountSubsPojo> subPojos = null;
		try {
			// 基本校验 校验传递的对象是否为空
			if (UseAccountType.USE_01.getValue().equals(mrsOrganVo.getIsForce())) {
				
				List<MrsOrganDto> organs = findBy3ElementAndAcountStatus(mrsOrganVo.getMrsOrganDto().getCustomerName(),
						mrsOrganVo.getMrsOrganDto().getSocialCreditCode(),
						mrsOrganVo.getMrsOrganDto().getOrganizeCode(), mrsOrganVo.getMrsOrganDto().getRevenueCode(),
						mrsOrganVo.getMrsOrganDto().getBusinessLicence(),
						mrsOrganVo.getMrsOrganDto().getOrganOtherCode(), MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
				if (CollectionUtil.isEmpty(organs)) {
					// 如果一户通账户不为为生效的状态，直接返回
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("不存在未生效的一户通信息，不能复用！");
					return response;
				}
				// 一户通信息
				account = mrsAccountDtoMapper.findByCustId(mrsOrganVo.getMrsAccountDto().getCustId());
			} else {
				response = validateBaseOgan(mrsOrganVo);
				if(response!=null && !response.getIsSucess()){
					return response;
				}
				//校验强制楷书数量
				boolean oganMax = checkOrgan3ElmentIsMax(mrsOrganVo.getMrsOrganDto().getCustomerName(),
						mrsOrganVo.getMrsOrganDto().getSocialCreditCode(),
						mrsOrganVo.getMrsOrganDto().getOrganizeCode(),
						mrsOrganVo.getMrsOrganDto().getRevenueCode(),
						mrsOrganVo.getMrsOrganDto().getBusinessLicence(),
						mrsOrganVo.getMrsOrganDto().getOrganOtherCode());
				if(!oganMax){
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("一户通开户已经达到强制开户数量！");
					return response;
				}
				
				if (!StringUtils.isBlank(mrsOrganVo.getMrsAccountDto().getCustId())) {
				    boolean iscust = mrsAccountService.checkCustId(mrsOrganVo.getMrsAccountDto().getCustId());
					if (!iscust) {
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("一户通编号已经存在！");
						return response;
					}
				}
				/**
				 * 检查子账户信息 子账户的个数是否大于机构必须的
				 */
				if (CollectionUtil.isEmpty(mrsOrganVo.getMrsConfSubAcctIds())) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加机构用户信息送审时，没有配置子账户设置参数！");
					return response;
				}

				/*List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctAppService
						.findByUserTypeAndRationType(MrsCustomerType.MCT_1, MrsConfSubRelationType.MUST,null);
				if (mrsConfSubAcctDtos == null || mrsConfSubAcctDtos.isEmpty()) {
					response.setIsSucess(false);	
					response.setMsgCode("");
					response.setMsgInfo("添加机构用户信息送审时，没有配置子账户设置参数！");
					return response;
				}
				if (mrsOrganVo.getMrsConfSubAcctIds().size() < mrsConfSubAcctDtos.size()) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加机构用户信息送审时，子账户信息异常！");
					return response;
				}*/
				List<MrsSubAccountDto> mrsSubAccountDtos = new ArrayList<MrsSubAccountDto>();
				for (String sub : mrsOrganVo.getMrsConfSubAcctIds()) {
					MrsSubAccountDto subAccount = new MrsSubAccountDto();
					MrsConfSubAcctDto dto = mrsConfSubAcctDtoMapper.selectByPrimaryKey(sub);
					if (dto == null) {
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("添加机构用户信息送审时，配置子账户信息异常！");
						return response;
					}
					//校验子账户信息是否正确
					if(MrsConfSubRelationType.NO.getValue().equals(dto.getOrganType())){
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("添加机构用户信息送审时，请选择机构正确的子账户类型！");
						return response;
					}
					if(!MrsPlatformCode.ACCOUNT.getValue().equals(dto.getPlatformCode())){
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("添加机构用户信息送审时，选择的子账户信息渠道错误！");
						return response;
					}
					subAccount.setSubAccountName(dto.getSubAccountName());
//					subAccount.setSubAccountCode(dto.getSubAccountCode());
					subAccount.setOpenTime(new Date());
					subAccount.setAccountOwnType(MrsSubAccountOwnType.MSAOT_01.getValue());
					subAccount.setAccountSource(MrsAccountSource.SOURCE_01.getValue());
					subAccount.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());
					// 废弃字段，没有用了，设置默认值
					subAccount.setSubAccountType(dto.getSubAccountCode());
					// 柜台
					subAccount.setPlatformCode("account");
					mrsSubAccountDtos.add(subAccount);
				}
				mrsOrganVo.setMrsSubAccountDtos(mrsSubAccountDtos);
				/**
				 * 校验用户信息，用户昵称，用户手机号
				 */
				MrsLoginUserDto loginUser;
				for (MrsLoginUserDto login : mrsOrganVo.getMrsLoginUserDtos()) {
					if (!StringUtils.isBlank(login.getId())) {
						loginUser = mrsLoginUserDtoMapper.findByAlias(login.getAlias());
						if (loginUser != null) {
							response.setIsSucess(false);
							response.setMsgCode("");
							response.setMsgInfo("添加机构用户信息送审时，用户昵称已经存在！");
							return response;
						}
						loginUser = mrsLoginUserDtoMapper.findByMobile(login.getMobile());
						if (loginUser != null) {
							response.setIsSucess(false);
							response.setMsgCode("");
							response.setMsgInfo("添加机构用户信息送审时，用户手机已经存在！");
							return response;
						}
					}

				}
				account = mrsOrganVo.getMrsAccountDto();
				account.setAccountName(mrsOrganVo.getMrsOrganDto().getCustomerName());
				//1，先查子账户配置跟，子账号得出子账号属于大类
				List<String> subAcctTypes = mrsConfAcctDtoMapper.findSubAcctTypesByConfSubIds(mrsOrganVo.getMrsConfSubAcctIds());
				//2，根据大类查询科目编号 调用账务系统
				List<String> mrsSubPayBusiDtos = actBusiRefSubDtoMapper.findSub2NoByAcctTypeNos(subAcctTypes);
				// 开通资金账户需要初始化类
				subPojos = new ArrayList<AccountSubsPojo>();
				for (String sub : mrsSubPayBusiDtos) {
					AccountSubsPojo pojo = new AccountSubsPojo();
					pojo.setActSub(sub);
					pojo.setActBusiType(AcctBusiType.YES_DEBIT_CERDIT);
					subPojos.add(pojo);
				}
			}
			// 设置一户通
			mrsOrganVo.getMrsAccountDto().setOpenTime(new Date());
			mrsOrganVo.getMrsAccountDto().setAccountSource(MrsAccountSource.SOURCE_01.getValue());
			mrsOrganVo.getMrsAccountDto().setCustomerType(MrsCustomerType.MCT_1.getValue());
			// 柜台
			mrsOrganVo.getMrsAccountDto().setPlatformCode("account");
			
			// 科目支持业务类型信息 科目编号
//			List<String> mrsSubPayBusiDtos = mrsSubPayBusiDtoMapper
//					.findByConfSubAcctIds(mrsOrganVo.getMrsConfSubAcctIds());

			/**
			 * 设置审核相关信息 1,设置审核人 2，设置审核主要信息 3，根据选择配置子账户信息 4，设置审核内容信息 5，上传附件到文件服务器
			 */
			// 1设置审核人，查询基础配置设置机构开户审核配置人数
			// 审核主要信息
			String aduitId = mrsAduitInfoService.craateMrsAduitInfo(StringUtils.isBlank(mrsOrganVo.getCreateRemark()) ? null: mrsOrganVo.getCreateRemark() , 
					mrsOrganVo.getMrsOrganDto().getOganCertNo(),
					mrsOrganVo.getMrsOrganDto().getOganCertType(),
					mrsOrganVo.getMrsOrganDto().getCustomerName(),
					StringUtils.isBlank(account.getCustId()) ? null : account.getCustId() ,
					StringUtils.isBlank(account.getAccountStatus()) ? MrsAccountStatus.ACCOUNT_STATUS_9.getValue() :  account.getAccountStatus(),
					StringUtils.isBlank(account.getAuthStatus()) ? MrsAccountAuthStatus.MAAS_0.getValue() : account.getAuthStatus(), 
					EStartSystemEnum.SYS_COUNTER,
					mrsOrganVo.getCurrentUser().getLoginName(), 
					EOperaTypeEnum.OP_OPEN,
					MrsCustTypeEnum.MRS_CUST_TYPE_01);

			// 子账户信息
			List<MrsSubAccountDto> oldMrsSubAccountDtos = mrsSubAccountService
					.findByCustId(mrsOrganVo.getMrsAccountDto().getCustId());
			// 用户信息
			List<MrsLoginUserDto> mrsLoginUserDtos = mrsLoginUserDtoMapper
					.findUserDtoByCustId(mrsOrganVo.getMrsAccountDto().getCustId());
			
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();

			MrsToJsonDto perMrsToJson = new MrsToJsonDto();
			perMrsToJson.setMrsContactListDtos(mrsOrganVo.getMrsContactListDtos());
			
			perMrsToJson.setMrsUserPayPasswordDto(mrsOrganVo.getMrsUserPayPasswordDto());
			perMrsToJson.setAccountSubsPojos(subPojos);
			perMrsToJson.setMrsAduitAttachmentDtos(mrsOrganVo.getMrsAduitAttachmentDtos());
			// 设置强制开户标识
			perMrsToJson.setIsForce(mrsOrganVo.getIsForce());
			// 为未生效的需要去查询数据库拿出值作为
			if (UseAccountType.USE_01.getValue().equals(mrsOrganVo.getIsForce())) {
				//子账户信息，用户信息不做修改
				perMrsToJson.setMrsLoginUserDtoList(mrsLoginUserDtos);
				perMrsToJson.setMrsSubAccountDtoList(oldMrsSubAccountDtos);
				// 机构基本信息
				MrsOrganDto organ = mrsOrganDtoMapper.findByCustId(mrsOrganVo.getMrsAccountDto().getCustId());
				// 联系人信息
				List<MrsContactListDto> mrsContactListDtos = mrsContactListService
						.findByCustId(mrsOrganVo.getMrsAccountDto().getCustId());
				// 创建审核内容信息
				MrsToJsonDto oldPerMrsToJson = new MrsToJsonDto();
				oldPerMrsToJson.setMrsAccountDto(account);
				oldPerMrsToJson.setMrsContactListDtos(mrsContactListDtos);
				oldPerMrsToJson.setMrsLoginUserDtoList(mrsLoginUserDtos);
				oldPerMrsToJson.setMrsOrganDto(organ);
				oldPerMrsToJson.setMrsSubAccountDtoList(oldMrsSubAccountDtos);

				JSONObject oldJsons = JSONObject.fromObject(oldPerMrsToJson);// 将java对象转换为json对象
				String oldStr = oldJsons.toString();
				blobs.setOldValue(oldStr);
				
				//机构基本信息
				MrsOrganDto newOganDto = new MrsOrganDto();
				BeanUtils.copyProperties(organ, newOganDto);
				
				newOganDto.setNationalityCode(mrsOrganVo.getMrsOrganDto().getNationalityCode());
				newOganDto.setCustomerShortName(mrsOrganVo.getMrsOrganDto().getCustomerShortName());
				newOganDto.setCustomerEname(mrsOrganVo.getMrsOrganDto().getCustomerEname());
				//机构证件类型，证件号码
				newOganDto.setBusinessSortCode(mrsOrganVo.getMrsOrganDto().getBusinessSortCode());
				newOganDto.setBusinessSortSubCode(mrsOrganVo.getMrsOrganDto().getBusinessSortSubCode());
				newOganDto.setAuthPersonName(mrsOrganVo.getMrsOrganDto().getAuthPersonName());
				newOganDto.setAuthPersonIdentifyTypeCode(mrsOrganVo.getMrsOrganDto().getAuthPersonIdentifyTypeCode());
				newOganDto.setAuthPersonIdentifyNo(mrsOrganVo.getMrsOrganDto().getAuthPersonIdentifyNo());
				newOganDto.setContactsMobile(mrsOrganVo.getMrsOrganDto().getContactsMobile());
				newOganDto.setContactsFax(mrsOrganVo.getMrsOrganDto().getContactsFax());
				newOganDto.setInternetAddress(mrsOrganVo.getMrsOrganDto().getInternetAddress());
				newOganDto.setContactsEmail(mrsOrganVo.getMrsOrganDto().getContactsEmail());
				newOganDto.setContactsAddr(mrsOrganVo.getMrsOrganDto().getContactsAddr());
				newOganDto.setContactsZip(mrsOrganVo.getMrsOrganDto().getContactsZip());
				//一户通信息
				MrsAccountDto newAccount = new MrsAccountDto();
				BeanUtils.copyProperties(account, newAccount);
				newAccount.setAccountSource(MrsAccountSource.SOURCE_01.getValue());
				newAccount.setCustomerType(MrsCustomerType.MCT_1.getValue());
				//柜台
				newAccount.setPlatformCode("account");
				
				perMrsToJson.setMrsAccountDto(newAccount);
				perMrsToJson.setMrsOrganDto(newOganDto);
			}else {
				perMrsToJson.setMrsOrganDto(mrsOrganVo.getMrsOrganDto());
				perMrsToJson.setMrsAccountDto(account);
				perMrsToJson.setMrsLoginUserDtoList(mrsOrganVo.getMrsLoginUserDtos());
				perMrsToJson.setMrsSubAccountDtoList(mrsOrganVo.getMrsSubAccountDtos());
			}
			// 创建审核内容信息
			JSONObject jsons = JSONObject.fromObject(perMrsToJson);// 将java对象转换为json对象
			String str = jsons.toString();

			blobs.setAduitId(aduitId);
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			/**
			 * 附件信息上传至服务器，保存附件审核信息
			 */
			if (!CollectionUtil.isEmpty(mrsOrganVo.getMrsAduitAttachmentDtos())) {
				List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
				for (MrsAduitAttachmentDto att : mrsOrganVo.getMrsAduitAttachmentDtos()) {
					att.setCreateTime(new Date());
					att.setStatus(EStatusEnum.EFFECTIVE.getValue());
					// 发起端
					att.setCatalog(EStartSystemEnum.SYS_COUNTER.getValue());
					att.setAduitContentId(blobs.getId());
					att.setId(UUID.randomUUID().toString());
					mrsAduitAttachmentDtos.add(att);
				}
				mrsAduitAttachmentDtoMapper.batchInsert(mrsAduitAttachmentDtos);
			}

			response.setIsSucess(true);
			response.setMsgCode("00");
			response.setMsgInfo("柜台端开户送审信息成功！");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response = new SaveAduitPersonResponseVo();
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("柜台端开户送审信息失败！" + e.getMessage());
			return response;
		}
	}

	public SaveAduitPersonResponseVo validateBaseOgan(MrsOrganVo oganVo) {
		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		if (oganVo == null) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存数据信息为空！");
			return response;
		}
		// 校验传入对象信息是否为空
		// 一户通信息
		if (oganVo.getMrsAccountDto() == null) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存机构送审信息，一户通信息为空！");
			return response;
		}
		// 支付密码信息
		if (oganVo.getMrsUserPayPasswordDto() == null) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存机构送审信息，支付密码信息为空！");
			return response;
		}
		// 用户信息
		if (oganVo.getMrsLoginUserDtos() == null) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存机构送审信息，用户信息为空！");
			return response;
		}
		// 附件信息后续校验
		if (CollectionUtil.isEmpty(oganVo.getMrsAduitAttachmentDtos())) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存机构送审信息，附件信息为空！");
			return response;
		}
		// 联系人信息
		if (CollectionUtil.isEmpty(oganVo.getMrsContactListDtos())) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存机构送审信息，联系人信息为空！");
			return response;
		}
		if (CollectionUtil.isEmpty(oganVo.getMrsConfSubAcctIds())) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存机构送审信息，子账户信息为空！");
			return response;
		}
		response.setIsSucess(true);
		response.setMsgCode("");
		response.setMsgInfo("");
		return response;
	}

	/**
	 * 
	 * 方法描述：校验一户通账户状态 创建人：ydx 创建时间：2017年2月25日 下午2:00:53
	 * 
	 * @param id
	 * @return
	 */
	public boolean validateMrsAccountStatus(String id) {
		MrsAccountDto account = mrsAccountDtoMapper.selectByPrimaryKey(id);
		if (account != null) {
			if (!MrsAccountStatus.ACCOUNT_STATUS_9.equals(account.getAccountStatusName())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<MrsOrganDto> findBy3ElementAndAcountStatus(String customerName, String socialCreditCode,
			String organizeCode, String revenueCode, String businessLicence, String organOtherCode,
			String accountStatus) {

		return mrsOrganDtoMapper.findBy3ElementAndAcountStatus(customerName, socialCreditCode, organizeCode,
				revenueCode, businessLicence, organOtherCode, accountStatus);
	}

	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	@Override
	public SaveAduitPersonResponseVo doUpdateSaveAduit(MrsOrganVo vo) {
		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		try {
			// 机构用户主键
			String id = vo.getMrsOrganDto().getId();

			MrsOrganDto oganDto = mrsOrganDtoMapper.selectByPrimaryKey(id);
			if (oganDto == null || StringUtils.isEmpty(oganDto.getCustId())) {
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("数据有误，一户通号码不存在！");
				return response;
			}
			// 一户通状态为注销 不让修改
			MrsAccountDto account = mrsAccountDtoMapper.findByCustId(oganDto.getCustId());
			if (account == null) {
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("数据有误，一户通信息不存在！");
				return response;
			}

			if (MrsAccountStatus.ACCOUNT_STATUS_2.getValue().equals(account.getAccountStatus())) {
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("一户通状态为注销不可以修改！");
				return response;
			}
			// 校验如果三要素有修改，校验修改的三要素是否存在未生效、正常、异常的一户通账户，如果存在，系统终止流程
			response = check3ElementEdit(oganDto, vo.getMrsOrganDto());
			if (!response.getIsSucess()) {
				return response;
			}
			// 查询联系人信息，附件信息
			// 联系人信息
			List<MrsContactListDto> mrsContactListDtos = mrsContactListService.findByCustId(oganDto.getCustId());
			// 查询附件信息并且归类处理
			List<MrsCertFileDto> mrsCertFileDtos = mrsCertFileService.findByCustId(oganDto.getCustId());
			/**
			 * 设置审核相关信息 1,设置审核人 2，设置审核主要信息 3，根据选择配置子账户信息 4，设置审核内容信息 5，上传附件到文件服务器
			 */
			// 1设置审核人，查询基础配置设置机构开户审核配置人数
			// 审核主要信息
			String aduitId = mrsAduitInfoService.craateMrsAduitInfo(StringUtils.isBlank(vo.getCreateRemark()) ? null: vo.getCreateRemark() , 
					StringUtils.isBlank(oganDto.getOganCertNo())? vo.getMrsOrganDto().getOganCertNo() : oganDto.getOganCertNo(),
					StringUtils.isBlank(oganDto.getOganCertType())? vo.getMrsOrganDto().getOganCertType() : oganDto.getOganCertType(),
					oganDto.getCustomerName(),
					StringUtils.isBlank(account.getCustId()) ? null : account.getCustId() ,
					StringUtils.isBlank(account.getAccountStatus()) ? MrsAccountStatus.ACCOUNT_STATUS_9.getValue() :  account.getAccountStatus(),
					StringUtils.isBlank(account.getAuthStatus()) ? MrsAccountAuthStatus.MAAS_0.getValue() : account.getAuthStatus(), 
					EStartSystemEnum.SYS_COUNTER,
					vo.getCurrentUser().getLoginName(), 
					EOperaTypeEnum.OP_UPADTE,
					MrsCustTypeEnum.MRS_CUST_TYPE_01);
			// 原值
			MrsToJsonDto perMrsToJsonOld = new MrsToJsonDto();
			perMrsToJsonOld.setMrsAccountDto(account);
			perMrsToJsonOld.setMrsContactListDtos(mrsContactListDtos);
			perMrsToJsonOld.setMrsOrganDto(oganDto);
			perMrsToJsonOld.setMrsCertFileDtos(mrsCertFileDtos);
			JSONObject jsonsOld = JSONObject.fromObject(perMrsToJsonOld);// 将java对象转换为json对象
			String strOld = jsonsOld.toString();
			
			MrsOrganDto newOganDto = new MrsOrganDto();
			BeanUtils.copyProperties(oganDto, newOganDto);
			
			newOganDto.setCustomerName(vo.getMrsOrganDto().getCustomerName());
			newOganDto.setNationalityCode(vo.getMrsOrganDto().getNationalityCode());
			newOganDto.setCustomerShortName(vo.getMrsOrganDto().getCustomerShortName());
			newOganDto.setCustomerEname(vo.getMrsOrganDto().getCustomerEname());
			// 机构证件类型，证件号码
			newOganDto.setOrganizeCode(vo.getMrsOrganDto().getOrganizeCode());
			newOganDto.setRevenueCode(vo.getMrsOrganDto().getRevenueCode());
			newOganDto.setBusinessLicence(vo.getMrsOrganDto().getBusinessLicence());
			newOganDto.setSocialCreditCode(vo.getMrsOrganDto().getSocialCreditCode());
			newOganDto.setOrganOtherCode(vo.getMrsOrganDto().getOrganOtherCode());
			newOganDto.setBusinessSortCode(vo.getMrsOrganDto().getBusinessSortCode());
			newOganDto.setBusinessSortSubCode(vo.getMrsOrganDto().getBusinessSortSubCode());
			newOganDto.setAuthPersonName(vo.getMrsOrganDto().getAuthPersonName());
			newOganDto.setAuthPersonIdentifyTypeCode(vo.getMrsOrganDto().getAuthPersonIdentifyTypeCode());
			newOganDto.setAuthPersonIdentifyNo(vo.getMrsOrganDto().getAuthPersonIdentifyNo());
			
			newOganDto.setContactsName(vo.getMrsOrganDto().getContactsName());
			newOganDto.setRegisteredAddr(vo.getMrsOrganDto().getRegisteredAddr());
			newOganDto.setContactsTel(vo.getMrsOrganDto().getContactsTel());
			
			newOganDto.setContactsMobile(vo.getMrsOrganDto().getContactsMobile());
			newOganDto.setContactsFax(vo.getMrsOrganDto().getContactsFax());
			newOganDto.setInternetAddress(vo.getMrsOrganDto().getInternetAddress());
			newOganDto.setContactsEmail(vo.getMrsOrganDto().getContactsEmail());
			newOganDto.setContactsAddr(vo.getMrsOrganDto().getContactsAddr());
			newOganDto.setContactsZip(vo.getMrsOrganDto().getContactsZip());

			// 新值
			MrsToJsonDto perMrsToJsonNew = new MrsToJsonDto();
			MrsAccountDto newAccount = new MrsAccountDto();
			BeanUtils.copyProperties(account, newAccount);
//			newAccount.setAccountType(vo.getMrsAccountDto().getAccountType());
			newAccount.setAccountName(newOganDto.getCustomerName());
			newAccount.setIsAccessParty(vo.getMrsAccountDto().getIsAccessParty());
			perMrsToJsonNew.setMrsAccountDto(newAccount);
			perMrsToJsonNew.setMrsContactListDtos(vo.getMrsContactListDtos());
			perMrsToJsonNew.setMrsOrganDto(newOganDto);
			perMrsToJsonNew.setMrsAduitAttachmentDtos(vo.getMrsAduitAttachmentDtos());
//			account.setAccountType(vo.getMrsAccountDto().getAccountType());
			// 创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsonsNew = JSONObject.fromObject(perMrsToJsonNew);// 将java对象转换为json对象
			String strNew = jsonsNew.toString();

			blobs.setAduitId(aduitId);
			blobs.setOldValue(strOld);
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(strNew);

			//创建审核内容信息
			mrsAduitContentDtoMapper.insertSelective(blobs);

			/**
			 * 附件信息上传至服务器，保存附件审核信息
			 */
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
			if (!CollectionUtil.isEmpty(vo.getMrsAduitAttachmentDtos())) {
				for (MrsAduitAttachmentDto att : vo.getMrsAduitAttachmentDtos()) {
					att.setCreateTime(new Date());
					att.setStatus(EStatusEnum.EFFECTIVE.getValue());
					// 发起端
					att.setCatalog(EStartSystemEnum.SYS_COUNTER.getValue());
					att.setAduitContentId(blobs.getId());
					att.setId(UUID.randomUUID().toString());
					mrsAduitAttachmentDtos.add(att);
				}
				mrsAduitAttachmentDtoMapper.batchInsert(mrsAduitAttachmentDtos);
			}
			response.setIsSucess(true);
			response.setMsgCode("00");
			response.setMsgInfo("柜台端机构信息变更送审信息成功！");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("柜台端机构信息变更送审信息失败！调用核心失败！");
			return response;
		}
	}

	/**
	 * 
	 * 方法描述：校验三要素是否改变 没有改变返回True 做了修改检查新三要素是否存在一户通账户，存在 创建人：ydx 创建时间：2017年2月21日
	 * 下午3:51:10
	 * 
	 * @param oldPersonDto
	 * @param newPersonDto
	 * @return
	 */
	public SaveAduitPersonResponseVo check3ElementEdit(MrsOrganDto oldOganDto, MrsOrganDto newOganDto) {
		// 证件类型
		SaveAduitPersonResponseVo reponse = new SaveAduitPersonResponseVo();
		try {
			boolean isMax = checkOrgan3ElmentUpdateIsMax(newOganDto.getCustomerName(), 
					newOganDto.getSocialCreditCode(), 
					newOganDto.getOrganizeCode(), 
					newOganDto.getRevenueCode(), 
					newOganDto.getBusinessLicence(), 
					newOganDto.getOrganOtherCode(),
					oldOganDto.getCustId()
					);
			if (!isMax) {
				reponse.setIsSucess(false);
				reponse.setMsgCode("");
				reponse.setMsgInfo("机构三要素修改后存在不为注销的一户通信息，且超过最大开户数量！");
			} else {
				reponse.setIsSucess(true);
				reponse.setMsgCode("");
				reponse.setMsgInfo("三要素可用！");
			}
			return reponse;
		} catch (Exception e) {
			e.printStackTrace();
			reponse.setIsSucess(false);
			reponse.setMsgCode("");
			reponse.setMsgInfo("校验异常！" + e.getMessage());
			return reponse;
		}
	}

	/**
	 * 开通资金账户
	 * @param custId
	 * @param type
	 * @param customerName
	 * @throws Exception
	 */
	private void createAccounts(String custId, MrsCustomerType type,String customerName
			 ,List<MrsConfSubAcctDto> mrsConfSubAcctDtos) throws CodeCheckedException {
		if(mrsConfSubAcctDtos!=null && mrsConfSubAcctDtos.size()>0){
			List<String> confSubIds = new ArrayList<String>();
			for(MrsConfSubAcctDto mrsConfSubAcctDto :mrsConfSubAcctDtos ){
				confSubIds.add(mrsConfSubAcctDto.getId());
			}
			//  开通资金账户
			// 科目支持业务类型信息 科目编号
//			List<String> mrsSubPayBusiDtos = mrsSubPayBusiDtoMapper.findByConfSubAcctIds(confSubIds);
			//1，先查子账户配置跟，子账号得出子账号属于大类
			List<String> subAcctTypes = mrsConfAcctDtoMapper.findSubAcctTypesByConfSubIds(confSubIds);
			//2，根据大类查询科目编号 调用账务系统
			List<String> mrsSubPayBusiDtos = actBusiRefSubDtoMapper.findSub2NoByAcctTypeNos(subAcctTypes);
			// 开通资金账户需要初始化类
			List<AccountSubsPojo> subPojos = new ArrayList<AccountSubsPojo>();
			for (String sub : mrsSubPayBusiDtos) {
				AccountSubsPojo pojo = new AccountSubsPojo();
				pojo.setActSub(sub);
				pojo.setActBusiType(AcctBusiType.YES_DEBIT_CERDIT);
				subPojos.add(pojo);
			}
			// 开资金账户
			try {
				actBookAppService.createAccounts(custId, customerName, subPojos);
			} catch (Exception e) {
				log.error("调用actBookAppService.createAccounts开资金账户失败",e);
				throw new CodeCheckedException("调用actBookAppService.createAccounts开资金账户失败");
			}
		}
	}
	//只更新机构信息
	@Override
	public void updateByPrimaryKey(MrsOrganDto dto) {
		mrsOrganDtoMapper.updateByPrimaryKey(dto);
	}

	@Override
	public void updateBaseAndSync(MrsOrganDto dto) {
		//更新机构信息
		updateByPrimaryKey(dto);
		//查询一户通认证状态
		MrsAccountDto mrsAccountDto = mrsAccountService.findByCustId(dto.getCustId());
		if(mrsAccountDto.getAuthStatus().equals(MrsAccountAuthStatus.MAAS_2.getValue()) || mrsAccountDto.getAuthStatus().equals(MrsAccountAuthStatus.MAAS_9.getValue())){
			// 入同步表  同步基本信息
			MrsNotifyDto notifyDto = generateSyncInfo(dto, MrsNotifyType.ORGAN);
			mrsNotifyDtoMapper.insert(notifyDto);
		}
	}
	
	@Override
	public void updateBaseFileAndSync(MrsOrganDto dto) {
		//更新机构信息  并 入同步表
		updateBaseAndSync(dto);
		// 同步证件信息
		MrsNotifyDto fileNotifyDto = generateSyncFileInfo(dto, MrsNotifyType.FILE);
		if (fileNotifyDto != null) {
			mrsNotifyDtoMapper.insert(fileNotifyDto);
		}
	}

	public MrsOrganDto copy2MrsOrganDto(UnitAssetRequestVo unitReqVo,String custId){
		MrsOrganDto organDto = new MrsOrganDto();
		BeanUtils.copyProperties(unitReqVo, organDto);
		organDto.setCreateTime(new Date());
		organDto.setId(UUID.randomUUID().toString());
		organDto.setCustId(custId);
		return organDto;
	}

	@Override
	public boolean checkOrgan3ElmentIsMax(String customerName, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence, String organOtherCode) {
		try {
			int length = 0;
			int maxLength = 0;
			//查询状态为未注销的机构信息
			List<MrsOrganDto> mrsOrganDtos = mrsOrganDtoMapper.findBy3ElementNoEff( customerName,  socialCreditCode,  organizeCode,
					 revenueCode,  businessLicence,  organOtherCode);
			if(!CollectionUtil.isEmpty(mrsOrganDtos)){
				 length = mrsOrganDtos.size();
			}
			//查询配置的最大数，比较两个数 如果存在的不为注销的数大于设置数返回false,相反返回true
			String vlaue = bisSysParamService.getValue(SystemParamConstants.ACCOUNT_MAX_NUM);
			if(StringUtils.isBlank(vlaue)){
				log.error("系统参数没有配置强制开户最大数量！");
				vlaue = "5";
//				return false;
			}
			maxLength = Integer.parseInt(vlaue)-1;
			if(length>maxLength){
				log.error("三要素在数据库已经存在["+length+"]条数据，超过设置的["+vlaue+"]");
				return false;
			}else {
				return true;
			}
		} catch (Exception e) {
			log.error("机构校验三要素能开最大的一户通数量接口错误！");
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<MrsOrganDto> findBy3ElementNoEff(String name, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence, String organOtherCode) {
		return mrsOrganDtoMapper.findBy3ElementNoEff(name, socialCreditCode, organizeCode, revenueCode, businessLicence, organOtherCode);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public AccountMsgRespVo updateOrganInfo(AccountUnitUpdateReqVO reqVo, MrsAccountDto account,
			MrsPlatformDto mrsPlatformDto) throws CodeCheckedException {

		log.info("一户通账户存在,custId={}", account.getCustId());
		// b1 如果接入平台认证类型为“无需认证”且一户通认证状态为“认证成功”或“无需认证”，
		// 更新“机构客户信息表”机构信息
		MrsOrganDto organ = new MrsOrganDto();
		MrsAccountAuthStatus authStatus = MrsAccountAuthStatus.getEnum(account.getAuthStatus());
		if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())
				&& MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			log.info("场景b1 更新机构信息");
			// 转换数据
			copyOrganDto(reqVo, organ);
			organ.setId(account.getUserInfoId());
			mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
		}
		// b2、如果接入平台认证类型为“无需认证”且一户通认证状态为非“认证成功”和非“无需认证”，
		// 更新“机构客户信息表”机构信息。更新“一户通账户表”认证状态为“无需认证”，
		// 如果一户通账户状态为“未生效”，更新账户状态为“正常”。同步客户信息，详见下面
		else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())
				&& !MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			log.info("场景b2 更新机构信息,一户通账户");
			copyOrganDto(reqVo, organ);
			organ.setId(account.getUserInfoId());
			mrsOrganDtoMapper.updateByPrimaryKeySelective(organ);
			if (MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(account.getAccountStatus())) {
				account.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
			}
			account.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
			account.setOpenTime(new Date());
			account.setUpdateTime(new Date());
			mrsAccountService.update(account);
		}
		// b3、如果接入平台认证类型为“需要认证”且一户通认证状态为“认证成功”和“无需认证”，不更新任何信息。
		else if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(mrsPlatformDto.getIsAuth())
				&& MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			// 场景3 原记录已经认证成功,新请求是不可信渠道
			log.info("场景b3 不更新信息");
		}

		// c、 如果接入平台认证类型为“无需认证”，进行以下处理流程：
		if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())) {
			// c1、更新子账户信息。根据请求参数中渠道编号查询“子账户配置表”该渠道开通机构客户必须开通的子账户编号，
			// 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户，详见“机构被动开户a4”。
			// 如果子账户的状态为“已注销”，则更改状态为“有效”。
			log.info("场景c1 不更新信息");
			List<MrsConfSubAcctDto> creageSubList = checkAndSaveOrgan(reqVo, account, mrsPlatformDto);
			// c2、开通资金账户，详见“机构被动开户a8”。账务系统会判断要求开的资金账户是否已经存在，
			// 如果不存在则会新增，如果存在，则不会再开通。
			log.info("场景c2  开通资金账户");
			createAccounts(account.getCustId(), MrsCustomerType.MCT_1, reqVo.getCustomerName(), creageSubList);

			// c3、根据该记录中一户通编号、请求参数中接入平台编号和平台客户编号查询“一户通客户关系表”是否存在记录，
			// 如果不存在，则创建新的记录，记录内容与查询条件相同。
			List<MrsAccountPlatformCustDto> apcList = mrsAccountPlatformCustDtoMapper
					.findActPlatformCust(account.getCustId(), reqVo.getPlatformCustCode(), reqVo.getPlatformCode());
			if (apcList != null && apcList.size() > 0) {
				log.info("场景c3    存在一户通客户关系");
			} else {
				log.info("场景c3   建新一户通客户关系");
				saveMrsAccountPlatform(reqVo, account);
			}
			// c4、如果信息需要同步客户信息。，则创建“同步信息通知表”记录来通知ECIF平台。
			// 首先，根据通知类型“机构开户通知”和一户通编号查询通知状态为“未通知”和“通知失败”的记录，
			// 如果存在记录，则将查询出的记录状态改为“通知失效”。并创建新的通知记录
			log.info("场景c4 保存同步信息到通知表");
			MrsOrganDto organDto = mrsOrganDtoMapper.selectByPrimaryKey(account.getUserInfoId());
			saveNotifyInfoOrgan(reqVo, organDto);
		}
		return null;
	}
	/**
	 * 保存同步信息到通知表
	 * @param reqVo
	 */
	private void saveNotifyInfoOrgan(AccountUnitUpdateReqVO reqVo,MrsOrganDto organ) {
		BeanUtils.copyProperties(reqVo, organ);
		organ.setUpdateTime(new Date());
		MrsNotifyDto mrsNotifyDto = generateSyncInfo(organ, MrsNotifyType.ORGAN);
		mrsNotifyDtoMapper.insert(mrsNotifyDto);
	}
	/**
	 * 建新一户通客户关系
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 */
	private void saveMrsAccountPlatform(AccountUnitUpdateReqVO reqVo, MrsAccountDto retrunMrsAccountDto) {
		MrsAccountPlatformCustDto actPlatformDto = new MrsAccountPlatformCustDto();
		actPlatformDto.setId(actPlatformDto.getIdentity());
		actPlatformDto.setCustId(retrunMrsAccountDto.getCustId());
		actPlatformDto.setPlatformCustCode(reqVo.getPlatformCustCode());
		actPlatformDto.setSource(reqVo.getPlatformCode());
		actPlatformDto.setCreateTime(new Date());
		mrsAccountPlatformCustDtoMapper.insert(actPlatformDto);
	}
	/**
	 * 根据请求参数中渠道编号查询“子账户配置表”该渠道开通机构客户必须开通的子账户编号，
	 * 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 * @param newPlatform
	 * @return
	 * @throws CodeCheckedException
	 */
	private List<MrsConfSubAcctDto> checkAndSaveOrgan(AccountUnitUpdateReqVO reqVo, MrsAccountDto retrunMrsAccountDto,
			MrsPlatformDto newPlatform) throws CodeCheckedException {
	 	List<MrsSubAccountDto> subAccountList = null;
		List<MrsSubAccountDto> subActList = mrsSubAccountDtoMapper.findByCustId(retrunMrsAccountDto.getCustId());
		// 根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_1,
				reqVo.getPlatformCode(),retrunMrsAccountDto.getAccountType().getValue());
		// 需要创建子账户的数据
		List<MrsConfSubAcctDto> creageSubList = new ArrayList<MrsConfSubAcctDto>();
		List<MrsConfSubAcctDto> deleteSubList = new ArrayList<MrsConfSubAcctDto>();
		if (subActList != null && subActList.size() > 0) {
			if (mrsConfSubAcctDtos != null && mrsConfSubAcctDtos.size() > 0) {
				for (MrsConfSubAcctDto confSubDto : mrsConfSubAcctDtos) {
					for (MrsSubAccountDto mrsSubAct : subActList) {
						if (mrsSubAct.getSubAccountType().equals(confSubDto.getSubAccountCode())) {
							// 删除不要子账户，相当于只创建不存在的子账户
							deleteSubList.add(confSubDto);
						}
					}
				}
			}
			//如果子账户的状态为“已注销”或“未生效”，则更改状态为“有效”
			for (MrsSubAccountDto mrsSubActDto : subActList) {
				if (MrsSubAccountStatus.MSAS_2.getValue().equals(mrsSubActDto.getSubAccountStatus())
						|| MrsSubAccountStatus.MSAS_9.getValue().equals(mrsSubActDto.getSubAccountStatus())) {
					mrsSubActDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());
					mrsSubActDto.setUpdateTime(new Date());
					mrsSubAccountDtoMapper.updateByPrimaryKeySelective(mrsSubActDto);
				}
			}
			//删除多余的子账户，再保存为生成的子账户
			if(deleteSubList!=null && deleteSubList.size()>0){
				mrsConfSubAcctDtos.removeAll(deleteSubList);
				creageSubList = mrsConfSubAcctDtos;
			}
		} else {
			// 如果没有子账户，则创建所有配置的子账户
			creageSubList = mrsConfSubAcctDtos;
		}
		subAccountList = generateSubAccount(retrunMrsAccountDto.getCustId(), newPlatform, creageSubList);
		// 保存子账户信息
		if (subAccountList != null && subAccountList.size() > 0) {
			for (MrsSubAccountDto mrsSubAccountDto : subAccountList) {
				mrsSubAccountDtoMapper.insert(mrsSubAccountDto);
			}
		}
		return creageSubList;
	}
	/**
	 * 转换数据
	 * @param reqVo
	 * @param mrsPersonDto
	 */
	private void copyOrganDto(AccountUnitUpdateReqVO reqVo, MrsOrganDto mrsOrganDto) {
		//必填项
		//mrsOrganDto.setCustId(reqVo.getAccountCode());//一户通
		//mrsOrganDto.setCustomerName(reqVo.getCustomerName());//机构名称
		//mrsOrganDto.setCredentialsType(reqVo.getCredentialsType());//证件类型
		//mrsOrganDto.setCredentialsNumber(reqVo.getCredentialsNumber());//证件号码
		//非必填项
		if(StringUtil.isNEmpty(reqVo.getCustomerCode())){
			mrsOrganDto.setCustomerCode(reqVo.getCustomerCode());//客户编号
		}
		if(StringUtil.isNEmpty(reqVo.getSocialCreditCode())){
			mrsOrganDto.setSocialCreditCode(reqVo.getSocialCreditCode());//社会统一信用代码
		}
		if(StringUtil.isNEmpty(reqVo.getOrganizeCode())){
			mrsOrganDto.setOrganizeCode(reqVo.getOrganizeCode());//组织机构代码
		}
		if(StringUtil.isNEmpty(reqVo.getRevenueCode())){
			mrsOrganDto.setRevenueCode(reqVo.getRevenueCode());//税务登记号码
		}
		if(StringUtil.isNEmpty(reqVo.getBusinessLicence())){
			mrsOrganDto.setBusinessLicence(reqVo.getBusinessLicence());//营业执照编码
		}
		
		if(StringUtil.isNEmpty(reqVo.getCustomerShortName())){
			mrsOrganDto.setCustomerShortName(reqVo.getCustomerShortName());//机构简称
		}
		if(StringUtil.isNEmpty(reqVo.getCustomerEname())){
			mrsOrganDto.setCustomerEname(reqVo.getCustomerEname());//机构英文名称
		}
		if(StringUtil.isNEmpty(reqVo.getNationalityCode())){
			//MrsNationaltyCode nationaltyCode = StringUtil.isEmpty(reqVo.getNationalityCode())? null  : MrsNationaltyCode.getEnum(reqVo.getNationalityCode());
    		mrsOrganDto.setNationalityCode(reqVo.getNationalityCode());//国籍码
		}
		if(StringUtil.isNEmpty(reqVo.getBusinessLicenceEndDate())){
			mrsOrganDto.setBusinessLicenceEndDate(reqVo.getBusinessLicenceEndDate());//营业执照有效期
		}
		if(StringUtil.isNEmpty(reqVo.getBusinessSortCode())){
    		mrsOrganDto.setBusinessSortCode(reqVo.getBusinessSortCode());//机构类型
		}
		if(StringUtil.isNEmpty(reqVo.getBusinessSortSubCode())){
			mrsOrganDto.setBusinessSortSubCode(reqVo.getBusinessSortSubCode());//机构子类型
		}
		if(StringUtil.isNEmpty(reqVo.getRegisteredAddr())){
			mrsOrganDto.setRegisteredAddr(reqVo.getRegisteredAddr());//注册地址
		}
		if(StringUtil.isNEmpty(reqVo.getAuthPersonName())){
			mrsOrganDto.setAuthPersonName(reqVo.getAuthPersonName());//法人姓名
		}
		if(StringUtil.isNEmpty(reqVo.getAuthPersonIdentifyTypeCode())){
    		mrsOrganDto.setAuthPersonIdentifyTypeCode(reqVo.getAuthPersonIdentifyTypeCode());//法人证件类型
		}
		if(StringUtil.isNEmpty(reqVo.getAuthPersonIdentifyNo())){
			mrsOrganDto.setAuthPersonIdentifyNo(reqVo.getAuthPersonIdentifyNo());//法人证件号码
		}
		if(StringUtil.isNEmpty(reqVo.getContactsName())){
			mrsOrganDto.setContactsName(reqVo.getContactsName());//联系人姓名
		}
		if(StringUtil.isNEmpty(reqVo.getContactsMoblie())){
			mrsOrganDto.setContactsMobile(reqVo.getContactsMoblie());//联系人移动电话
		}
		if(StringUtil.isNEmpty(reqVo.getContactsTel())){
			mrsOrganDto.setContactsTel(reqVo.getContactsTel());//联系人固定电话
		}
		if(StringUtil.isNEmpty(reqVo.getContactsSpareTel())){
			mrsOrganDto.setContactsSpareTel(reqVo.getContactsSpareTel());//联系人备用电话
		}
		if(StringUtil.isNEmpty(reqVo.getContactsFax())){
			mrsOrganDto.setContactsFax(reqVo.getContactsFax());//联系人传真
		}
		if(StringUtil.isNEmpty(reqVo.getContactsEmail())){
			mrsOrganDto.setContactsEmail(reqVo.getContactsEmail());//联系人邮箱
		}
		if(StringUtil.isNEmpty(reqVo.getContactsAddr())){
			mrsOrganDto.setContactsAddr(reqVo.getContactsAddr());//联系人地址
		}
		if(StringUtil.isNEmpty(reqVo.getContactsZip())){
			mrsOrganDto.setContactsZip(reqVo.getContactsZip());//联系人邮编
		}
		
		mrsOrganDto.setUpdateTime(new Date());
		mrsOrganDto.setUpdateOperator("");
		
	}

	@Override
	public boolean checkOrgan3ElmentUpdateIsMax(String customerName, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence, String organOtherCode, String custId) {
		try {
			int length = 0;
			int maxLength = 0;
			//查询状态为未注销的机构信息
			List<MrsOrganDto> mrsOrganDtos = mrsOrganDtoMapper.findBy3ElementNoCustIdEff( customerName,  socialCreditCode,  organizeCode,
					 revenueCode,  businessLicence,  organOtherCode,custId);
			if(!CollectionUtil.isEmpty(mrsOrganDtos)){
				 length = mrsOrganDtos.size();
			}
			//查询配置的最大数，比较两个数 如果存在的不为注销的数大于设置数返回false,相反返回true
			String vlaue = bisSysParamService.getValue(SystemParamConstants.ACCOUNT_MAX_NUM);
			if(StringUtils.isBlank(vlaue)){
				log.error("系统参数没有配置强制开户最大数量！");
				return false;
			}
			maxLength = Integer.parseInt(vlaue)-1;
			if(length>maxLength){
				log.error("三要素在数据库已经存在["+length+"]条数据，超过设置的["+vlaue+"]");
				return false;
			}else {
				return true;
			}
		} catch (Exception e) {
			log.error("机构校验三要素能开最大的一户通数量接口错误！");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public SaveAduitProductResponseVo removeSaveAduit(MrsRemoveAccountVo removeAccountVo) {
		SaveAduitProductResponseVo respvo = new SaveAduitProductResponseVo();
		try {
			// 一户通信息
			MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(removeAccountVo.getCustId());
			// 子账户信息
			List<MrsSubAccountDto> mrsSubAccountList = mrsSubAccountDtoMapper.findByCustId(removeAccountVo.getCustId());
			// 资金账户信息
			List<ActAccountDto> actAccountList = actAccountDtoMapper.findListByCustId(removeAccountVo.getCustId());
			//机构产品信息
			MrsOrganDto mrsOrganDto = mrsOrganDtoMapper.findByCustId(removeAccountVo.getCustId());
			
			//一户通状态为注销不能修改
			MrsAccountDto account = mrsAccountDtoMapper.findByCustId(removeAccountVo.getCustId());
			if(account==null){
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("数据有误，一户通信息不存在!");
				return respvo;
			}
			
			if( MrsAccountStatus.ACCOUNT_STATUS_2.getDisplayName().equals(account.getAccountStatusName()) ){
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("一户通状态为注销不能修改!");
				return respvo;
			}

			// 生成审核记录
			MrsAduitInfoDto mrsAduitInfoDto = new MrsAduitInfoDto();
			// 查询审核配置表获取审核人数
			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					EOperaTypeEnum.OP_CANCEL.getValue(), EStartSystemEnum.SYS_COUNTER.getValue());
			if (mrsConfAuditDto == null) {
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("用户注销未进行配置!");
				return respvo;
			}
			mrsAduitInfoDto.setCustId(mrsOrganDto.getCustId());
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short) 0);
			mrsAduitInfoDto.setBusiType(EOperaTypeEnum.OP_CANCEL);
			mrsAduitInfoDto.setStartSystem(EStartSystemEnum.SYS_COUNTER);
			mrsAduitInfoDto.setCartNo(mrsOrganDto.getOganCertNo());
			mrsAduitInfoDto.setCartType(mrsOrganDto.getOganCertType());
			mrsAduitInfoDto.setName(mrsOrganDto.getCustomerName());
			// 机构客户类型
			mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_01);
			if(org.apache.commons.lang3.StringUtils.isNotBlank(removeAccountVo.getCurrentUser().getLoginName())
					&& removeAccountVo.getCurrentUser().getLoginName().length()>10){
				mrsAduitInfoDto.setCreateOperator(removeAccountVo.getCurrentUser().getLoginName().subSequence(0, 10).toString());
			}else{
				mrsAduitInfoDto.setCreateOperator(removeAccountVo.getCurrentUser().getLoginName());
			}
			mrsAduitInfoDto.setCreateTime(new Date());
			mrsAduitInfoDto.setProductStatus(mrsAccountDto.getAccountStatus());
			mrsAduitInfoDto.setProductAuthStatus(mrsAccountDto.getAuthStatus());
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAduitInfoDto.setId(UUID.randomUUID().toString());
			mrsAduitInfoDto.setCreateOperator(removeAccountVo.getCurrentUser().getLoginName());
			if (!StringUtils.isEmpty(removeAccountVo.getCreateRemark())) {
				mrsAduitInfoDto.setRemark(removeAccountVo.getCreateRemark());
			}
			// 保存审核信息记录
			mrsAduitInfoDtoMapper.insertSelective(mrsAduitInfoDto);

			// 生成审核人
			// 查询审核配置明细表
			List<MrsConfAuditItemDto> mrsConfAuditItemList = mrsConfAuditItemDtoMapper
					.selectByAuditId(mrsConfAuditDto.getId());
			List<MrsAduitPersonDto> mrsAduitPersonDtoList = new ArrayList<MrsAduitPersonDto>();

			MrsAduitPersonDto aduitPerson = null;
			for (MrsConfAuditItemDto itemDto : mrsConfAuditItemList) {
				aduitPerson = new MrsAduitPersonDto();
				aduitPerson.setAduitId(mrsAduitInfoDto.getId());
				aduitPerson.setAduitStatus(EAduitTypeEnum.ADUIT_WAIT);
				aduitPerson.setAduitUserId(itemDto.getUserId());
				aduitPerson.setAduitUserName(itemDto.getUserName());
				aduitPerson.setType(EDistributTypeEnum.AUTO_DISTRIBUT);
				aduitPerson.setStatus(EStatusEnum.EFFECTIVE);
				aduitPerson.setId(UUID.randomUUID().toString());
				aduitPerson.setCreateTime(new Date());
				mrsAduitPersonDtoList.add(aduitPerson);

			}

			// 保存审核人信息记录
			mrsAduitPersonDtoMapper.batchInsert(mrsAduitPersonDtoList);

			// 原json值
			MrsToJsonDto oldMrsToJson = new MrsToJsonDto();
			oldMrsToJson.setActAccountDtos(actAccountList);
			oldMrsToJson.setMrsAccountDto(mrsAccountDto);
			oldMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
			oldMrsToJson.setMrsOrganDto(mrsOrganDto);
			
			// 新json值
			MrsToJsonDto newMrsToJson = new MrsToJsonDto();
			MrsAccountDto mrsAccountDton = mrsAccountDto;
			mrsAccountDton.setCloseTime(new Date());
			mrsAccountDton.setCloseOperator(removeAccountVo.getCurrentUser().getLoginName());
			mrsAccountDton.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_2.getValue());
			newMrsToJson.setActAccountDtos(actAccountList);
			newMrsToJson.setMrsAccountDto(mrsAccountDton);
			newMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
			newMrsToJson.setMrsOrganDto(mrsOrganDto);
			
			// 创建审核内容信息,保存json串
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			// 将java对象转换为json串
			JSONObject jsons = JSONObject.fromObject(newMrsToJson);
			String str = jsons.toString();
			JSONObject oldJson = JSONObject.fromObject(oldMrsToJson);
			String oldStr = oldJson.toString();

			blobs.setAduitId(mrsAduitInfoDto.getId());
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			blobs.setOldValue(oldStr);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			
			respvo.setIsSucess(true);
			respvo.setMsgCode("");
			respvo.setMsgInfo("机构用户注销申请成功!");
			return respvo;
		} catch (Exception e) {
			e.printStackTrace();
			respvo.setIsSucess(false);
			respvo.setMsgCode("");
			respvo.setMsgInfo("机构用户注销申请失败!");
			return respvo;

		}
	}
}
