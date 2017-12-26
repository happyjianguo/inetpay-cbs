package com.ylink.inetpay.cbs.mrs.service;

import java.util.ArrayList;
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
import com.ylink.inetpay.cbs.act.service.ActAccountService;
import com.ylink.inetpay.cbs.bis.service.BisEmailService;
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
import com.ylink.inetpay.cbs.mrs.dao.MrsPersonDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPlatformDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPortalAccountAduitDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsSubAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserAccountDtoMapper;
import com.ylink.inetpay.cbs.util.DateUtil;
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
import com.ylink.inetpay.common.core.constant.EYesNo;
import com.ylink.inetpay.common.core.constant.IsDelete;
import com.ylink.inetpay.common.core.constant.LoginUserIsMain;
import com.ylink.inetpay.common.core.constant.LoginUserStatus;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.core.constant.MrsAccountSource;
import com.ylink.inetpay.common.core.constant.MrsAccountStatus;
import com.ylink.inetpay.common.core.constant.MrsConfSubRelationType;
import com.ylink.inetpay.common.core.constant.MrsCustType;
import com.ylink.inetpay.common.core.constant.MrsCustTypeEnum;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.core.constant.MrsCustomerTypeCode;
import com.ylink.inetpay.common.core.constant.MrsNationaltyCode;
import com.ylink.inetpay.common.core.constant.MrsNotifyStatus;
import com.ylink.inetpay.common.core.constant.MrsNotifyType;
import com.ylink.inetpay.common.core.constant.MrsPlatformCode;
import com.ylink.inetpay.common.core.constant.MrsPlatformIsAuth;
import com.ylink.inetpay.common.core.constant.MrsPlatformStatus;
import com.ylink.inetpay.common.core.constant.MrsSubAccountOwnType;
import com.ylink.inetpay.common.core.constant.MrsSubAccountStatus;
import com.ylink.inetpay.common.core.constant.UseAccountType;
import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.core.util.GsonUtil;
import com.ylink.inetpay.common.core.util.JsonUtil;
import com.ylink.inetpay.common.core.util.RandomUtil;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.account.app.ActBookAppService;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActSubjectDto;
import com.ylink.inetpay.common.project.account.pojo.AccountSubsPojo;
import com.ylink.inetpay.common.project.cbs.app.MrsAduitContentAppService;
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
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalAccountAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.cbs.exception.PortalCode;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsActAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsPersonVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsRemoveAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;
import com.ylink.inetpay.common.project.portal.enums.ErrorMsgEnum;
import com.ylink.inetpay.common.project.portal.exception.PortalCheckedException;
import com.ylink.inetpay.common.project.portal.vo.AccountIndividualUpdateReqVO;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgRespVo;
import com.ylink.inetpay.common.project.portal.vo.IndividualRequestVO;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchRequestVO;
import com.ylink.inetpay.common.project.portal.vo.LoginMsgSearchResponseVO;
import com.ylink.inetpay.common.project.portal.vo.UploadPersonPojo;
import com.ylink.inetpay.common.project.portal.vo.UserCheckVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.AccountSearchReqVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.AccountSearchRespVO;
import com.ylink.inetpay.common.project.portal.vo.customer.BankBusiReqVO;
import com.ylink.inetpay.common.project.portal.vo.customer.PersonVO;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncFileInfo;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncPersonReq;

import net.sf.json.JSONObject;

@Service("mrsPersonService")
public class MrsPersonServiceImpl implements MrsPersonService {

	private static Logger log = LoggerFactory.getLogger(MrsPersonServiceImpl.class);

	@Autowired
	private MrsPersonDtoMapper mrsPersonDtoMapper;
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
	private MrsNotifyDtoMapper mrsNotifyDtoMapper;
	@Autowired
	private MrsCertFileDtoMapper mrsCertFileDtoMapper;
	@Autowired
	private ActBookAppService actBookAppService;
	@Autowired
	private MrsDataAuditChangeService mrsDataAuditChangeService;
	@Autowired
	private MrsAduitContentAppService mrsAduitContentAppService;
	@Autowired
	private MrsPortalAccountAduitDtoMapper mrsPortalAccountAduitDtoMapper;
	// 基础配置审核配置信息操作（配置审核人）
	@Autowired
	private MrsConfAuditDtoMapper mrsConfAuditDtoMapper;
	// 审核配置明细信息
	@Autowired
	private MrsConfAuditItemDtoMapper mrsConfAuditItemDtoMapper;
	// 审核配置明细信息操作
	@Autowired
	private MrsAduitPersonDtoMapper mrsAduitPersonDtoMapper;
	// 审核内容信息操作
	@Autowired
	private MrsAduitContentDtoMapper mrsAduitContentDtoMapper;
	// 审核主要信息操作
	@Autowired
	private MrsAduitInfoDtoMapper mrsAduitInfoDtoMapper;
	// 子账户审核配置信息
	@Autowired
	private MrsConfSubAcctDtoMapper mrsConfSubAcctDtoMapper;
	// 一户通操作service
	@Autowired
	private MrsAccountService mrsAccountService;
	// 联系人操作service
	@Autowired
	private MrsContactListService mrsContactListService;
	// 附件操作service
	@Autowired
	private MrsCertFileService mrsCertFileService;
	@Autowired
	private MrsAccountPlatformCustDtoMapper mrsAccountPlatformCustDtoMapper;
	// 审核附件操作
	@Autowired
	private MrsAduitAttachmentDtoMapper mrsAduitAttachmentDtoMapper;
	// 资金账户信息操作
	@Autowired
	private ActAccountService actAccountService;

	@Autowired
	private MrsConfSubAcctService mrsConfSubAcctService;
	// 创建审核数据操作服务
	@Autowired
	private MrsAduitInfoService mrsAduitInfoService;
	@Autowired
	private ActAccountDtoMapper actAccountDtoMapper;
	@Autowired
	private ActSubjectDtoMapper actSubjectDtoMapper;
	@Autowired
	private MrsConfAcctDtoMapper mrsConfAcctDtoMapper;
	@Autowired
	private ActBusiRefSubDtoMapper actBusiRefSubDtoMapper;
	@Autowired
	private BisEmailService bisEmailService;
	@Override
	public List<MrsPersonDto> findBy3Element(String customerName, String type, String number) {
		return mrsPersonDtoMapper.findBy3Element(customerName, type, number);
	}
	@Override
	public List<MrsPersonDto> findActPersonBy3Element(String customerName, String type, String number) {
		return mrsPersonDtoMapper.findActPersonBy3Element(customerName, type, number);
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto initOpenAcnt(IndividualRequestVO requestVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException{
		// 获取自然人保险一户通序列
		String custId = checkCustIdAndReturnId(null);
		log.info("开始组装数据信息,一户通账号:[custId={}]", custId);
		// 根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_0,
				requestVo.getPlatformCode(),requestVo.getAccountType());
		log.info("子账户配置个数:{}", mrsConfSubAcctDtos.size());
		// 将请求对象VO中的值赋值到一户通DTO中
		MrsAccountDto mrsAccountDto = copy2AccountDto(requestVo, custId, mrsPlatformDto.getIsAuth());
		// 创建子账户
		List<MrsSubAccountDto> subAccountList = generateSubAccount(custId, mrsPlatformDto, mrsConfSubAcctDtos);
		// 将请求对象VO中的值复制到个人客户的DTO中
		MrsPersonDto mrsPersonDto = copy2PersonDto(requestVo, custId);
		// 创建登陆对象信息
		MrsLoginUserDto mrsUserDto = copy2LoginUserDto(requestVo, custId);
		// 账户信息和登陆用户信息关联
		MrsUserAccountDto userAccount = copy2UserAccount(mrsAccountDto.getId(), mrsUserDto.getId());
		// 一户通客户关系
		MrsAccountPlatformCustDto accountPlatformCustDto = generateAccountPlatform(requestVo, custId);
		
		// 保存一户通DTO
		mrsAccountDtoMapper.insert(mrsAccountDto);
		// 保存账户和登陆关联的信息
		mrsUserAccountDtoMapper.insert(userAccount);
		// 保存子账户信息
		for (MrsSubAccountDto mrsSubAccountDto : subAccountList) {
			mrsSubAccountDtoMapper.insert(mrsSubAccountDto);
		}
		// 保存个人客户的DTO
		mrsPersonDtoMapper.insert(mrsPersonDto);
		// 保存登陆信息
		mrsLoginUserDtoMapper.insert(mrsUserDto);
		mrsAccountDto.setNoEncryptloginPwd(mrsUserDto.getNoEncryptloginPwd());
		// 一户通客户关系表
		mrsAccountPlatformCustDtoMapper.insert(accountPlatformCustDto);
		if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())) {
			// 需要同步到客户系统 入库
			log.info("保存同步信息到通知表");
			MrsNotifyDto mrsNotifyDto = generateSyncInfo(mrsPersonDto, MrsNotifyType.PERSON);
			mrsNotifyDtoMapper.insert(mrsNotifyDto);
			//如果存在用户邮箱，则调用邮件接口发送邮件。邮件内容显示用户的一户通号码和一个修改登录密码的链接。
			//用户点击该链接，直接进入系统的强制密码重置界面，进行密码重置。
			sendEmail(requestVo, mrsUserDto,custId);
		}
		// 开通资金账户
		createAccounts(custId, MrsCustomerType.MCT_0, mrsPersonDto.getCustomerName(), mrsConfSubAcctDtos);
		return mrsAccountDto;
	}
	private void sendEmail(IndividualRequestVO requestVo, MrsLoginUserDto mrsUserDto,String custId) {
		String userEmail = requestVo.getEmail();
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(userEmail)){
			log.info("调用邮件接口发送邮件,用于密码重置");
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put(SHIEConfigConstant.CUSTMER_NAME, requestVo.getCustomerName());
				UserCheckVO checkVo = bisEmailService.shieSendEmail(userEmail, 
						mrsUserDto.getId(), 
						EBisSmsSystem.PORTAL, 
						EBisEmailTemplateCode.REST_LOGIN_PWD, 
						params,custId);
				if(!checkVo.isCheckValue()){
					log.error("邮件发送失败:"+checkVo.getMsg());
				}
			} catch (Exception e) {
				log.error("邮件发送失败:",ExceptionProcUtil.getExceptionDesc(e));
			}
		}
	}

	/**
	 * 一户通客户关系
	 * 
	 * @param requestVo
	 * @param custId
	 * @return
	 */
	private MrsAccountPlatformCustDto generateAccountPlatform(IndividualRequestVO requestVo, String custId) {
		MrsAccountPlatformCustDto accountPlatformCustDto = new MrsAccountPlatformCustDto();
		accountPlatformCustDto.setId(accountPlatformCustDto.getIdentity());
		accountPlatformCustDto.setCustId(custId);
		accountPlatformCustDto.setSource(requestVo.getPlatformCode());
		accountPlatformCustDto.setPlatformCustCode(requestVo.getPlatformCustCode());
		accountPlatformCustDto.setCreateTime(new Date());
		return accountPlatformCustDto;
	}

	/**
	 * 根据新生成一户通编号查询“一户通账户表”和“审核主要信息表”是否存在相同一户通编号， 如果存在，则重新生成，直至一户通编号不重复。
	 * 
	 * @return
	 */
	private String checkCustIdAndReturnId(String custId) {
		// 递归
		if (StringUtils.isEmpty(custId)) {
			custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
			custId = MrsConstants.PERSON_ACCOUNT_PREFIX + StringUtils.format(11, custId);
		}
		boolean checkFlag = mrsAccountService.checkCustId(custId);
		// 如果返回True表编号可用
		if (!checkFlag) {
			return checkCustIdAndReturnId(null);
		} else {
			return custId;
		}
	}

	/**
	 * 根据客户类型，关联关系查询子账户配置
	 * 
	 * @param cType
	 * @param platformCode
	 * @return
	 */
	private List<MrsConfSubAcctDto> findByUserTypeAndRationType(MrsCustomerType cType, String platformCode,String actType) {
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService.findBy4Elment(cType, 
				MrsConfSubRelationType.MUST, platformCode, actType);
		return mrsConfSubAcctDtos;
	}

	/**
	 * 根据接入平台编号查询平台信息
	 * 
	 * @param platformCode
	 * @return
	 * @throws CodeCheckedException
	 */
	@Override
	public MrsPlatformDto checkPlatform(String platformCode) throws CodeCheckedException {
		MrsPlatformDto mrsPlatformDto = mrsPlatformDaoMapper.findByPlatformCode(platformCode,
				MrsPlatformStatus.PS_0.getValue());
		// 判断平台开户信息是否需要认证
		if (mrsPlatformDto == null) {
			log.error("开户渠道不存在，查询条件[status = " + MrsPlatformStatus.PS_0.getValue() + ",platformCode=" + platformCode
					+ "]");
			throw new CodeCheckedException(PortalCode.CODE_9999, "开户渠道不存在");
		}
		return mrsPlatformDto;
	}
	/**
	 * 创建用户和账户关系
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
	 * 可能查询出多条记录，则循环处理记录，更新一户通信息
	 * @throws Exception 
	 */
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto updateOpenAcnt(IndividualRequestVO reqVo, List<MrsAccountDto> actPersonList,
			MrsPlatformDto newPlatform)
			throws CodeCheckedException {
		MrsAccountDto retrunMrsAccountDto = new MrsAccountDto();
		// c1、更新信息后需要返回一户通信息，查询返回一户通。查询结果存在多条记录，
		// 那么首先取认证状态为“认证成功”或“无需认证”记录
		// 其次再取其他状态的记录，如果相同状态仍存在多条取再次取一户通创建时间为最近的记录
		if (actPersonList.size() == 1) {
			// 返回账户信息
			retrunMrsAccountDto = actPersonList.get(0);
		} else {
			//首先取认证状态为“认证成功”或“无需认证”记录
			List<MrsAccountDto> actPersonLastList = mrsAccountService.findByPerson3ElementLast(reqVo.getCustomerName(),
					reqVo.getCredentialsType(), reqVo.getCredentialsNumber());
			if (actPersonLastList != null && actPersonLastList.size() > 0) {
				// 返回账户信息,最近的记录
				retrunMrsAccountDto = actPersonLastList.get(0);
			}
			//其次再取其他状态的记录
			else{
				// 返回账户信息,最近的记录
				retrunMrsAccountDto = actPersonList.get(0);
			}
		}
		// c2、更新返回一户通的个人和账户信息，逻辑判断
		checkActAndPerson(reqVo, newPlatform, retrunMrsAccountDto);
		// c3 根据请求参数中渠道编号查询“子账户配置表”该渠道开通个人客户必须开通的子账户编号，
		// 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户
		log.info("场景c3   处理子账户配置信息");
		List<MrsConfSubAcctDto> creageSubList = checkAndSaveAct(reqVo, retrunMrsAccountDto, newPlatform);
		// c4 开通资金账户。则将创建资金账户，详见上面a8。账务系统会判断要求开的资金账户是否已经存在，
		// 如果不存在则会新增。
		log.info("场景c4   开通资金账户");
		createAccounts(retrunMrsAccountDto.getCustId(), MrsCustomerType.MCT_0, reqVo.getCustomerName(), creageSubList);
		// c5 根据该记录中一户通编号、请求参数中接入平台编号和平台客户编号查询“一户通客户关系表”是否存在记录，
		// 如果不存在，则创建新的记录，记录内容与查询条件相同。
		List<MrsAccountPlatformCustDto> apcList = mrsAccountPlatformCustDtoMapper.findActPlatformCust(
				retrunMrsAccountDto.getCustId(), reqVo.getPlatformCustCode(), reqVo.getPlatformCode());
		if (apcList != null && apcList.size() > 0) {
			log.info("场景c5    存在一户通客户关系");
		} else {
			log.info("场景c5    建新一户通客户关系");
			saveMrsAccountPlatformCust(reqVo, retrunMrsAccountDto);
		}
		// 需要同步到客户系统 入库
		if(MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(newPlatform.getIsAuth())){
			log.info("场景c6 保存同步信息到通知表");
			MrsPersonDto person = mrsPersonDtoMapper.selectByPrimaryKey(retrunMrsAccountDto.getUserInfoId());
			saveNotifyInfo(reqVo,person);
		}
		// 返回该记录的一户通账号，开户结果“开户成功”。
		log.info("场景c7返回该记录的一户通账号");
		//	1，已存在用户且状态为生效的，不返回密码  2. 已经登录过门户系统，不返回密码。
		/*MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(retrunMrsAccountDto.getCustId());
		if(MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(retrunMrsAccountDto.getAccountStatus())
				||(null != loginUser && null != loginUser.getLoginTime())){
			log.info("一户通账户[custId={}]账户状态是正常或已经登录过门户系统,不返回密码", retrunMrsAccountDto.getCustId());
		}else{
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
	private void saveNotifyInfo(IndividualRequestVO reqVo,MrsPersonDto person) {
		BeanUtils.copyProperties(reqVo, person);
		person.setUpdateTime(new Date());
		MrsNotifyDto mrsNotifyDto = generateSyncInfo(person, MrsNotifyType.PERSON);
		mrsNotifyDtoMapper.insert(mrsNotifyDto);
	}
	/**
	 * 保存同步信息到通知表
	 * @param reqVo
	 */
	private void saveNotifyInfoIvidual(AccountIndividualUpdateReqVO reqVo,MrsPersonDto person) {
		BeanUtils.copyProperties(reqVo, person);
		person.setUpdateTime(new Date());
		MrsNotifyDto mrsNotifyDto = generateSyncInfo(person, MrsNotifyType.PERSON);
		mrsNotifyDtoMapper.insert(mrsNotifyDto);
	}
	/**
	 * 检查账户状态并更新对应数据
	 * @param reqVo
	 * @param newPlatform
	 * @param mrsAccountDto
	 */
	private void checkActAndPerson(IndividualRequestVO reqVo, MrsPlatformDto newPlatform, MrsAccountDto mrsAccountDto) {
		// （a） 如果接入平台认证类型为“无需认证”且一户通认证状态为“认证成功”或“无需认证”，
		// 更新“个人客户信息表”个人信息，但不需要更新“一户通账户表”接入平台信息和客户编号。同步客户信息至ECIF
		MrsPersonDto person = new MrsPersonDto();
		MrsAccountAuthStatus authStatus = MrsAccountAuthStatus.getEnum(mrsAccountDto.getAuthStatus());
		if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(newPlatform.getIsAuth())
				&& MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			log.info("场景1   只更新客户信息");
			BeanUtils.copyProperties(reqVo, person);
			person.setUpdateTime(new Date());
			person.setId(mrsAccountDto.getUserInfoId());
			MrsNationaltyCode nationaltyCode = StringUtil.isEmpty(reqVo.getNationalityCode())? null  : MrsNationaltyCode.getEnum(reqVo.getNationalityCode());
			if(nationaltyCode != null){
				person.setNationalityCode(nationaltyCode);//国籍码
			}
			mrsPersonDtoMapper.updateByPrimaryKeySelective(person);
			return;
		}
		// （b） 如果接入平台认证类型为“无需认证”且一户通认证状态为非“认证成功”和非“无需认证”，
		// 更新“个人客户信息表”个人信息，需要更新“一户通账户表”接入平台信息，但不更新客户编号，
		// 更新“子账户表”该一户通下子账户状态为“有效”。
		else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(newPlatform.getIsAuth())
				&& !MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			log.info("场景2  更新客户信息，修改接入平台的信息");
			BeanUtils.copyProperties(reqVo, person);
			person.setUpdateTime(new Date());
			person.setId(mrsAccountDto.getUserInfoId());
			MrsNationaltyCode nationaltyCode = StringUtil.isEmpty(reqVo.getNationalityCode())? null  : MrsNationaltyCode.getEnum(reqVo.getNationalityCode());
			if(nationaltyCode != null){
				person.setNationalityCode(nationaltyCode);//国籍码
			}
			mrsPersonDtoMapper.updateByPrimaryKeySelective(person);
			
			if(MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(mrsAccountDto.getAccountStatus())){
				mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
				mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
				mrsAccountDto.setOpenTime(new Date());
				mrsAccountDto.setUpdateTime(new Date());
				mrsAccountDtoMapper.updateByPrimaryKeySelective(mrsAccountDto);
			}
			//给用户发邮件，重置密码
			MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(mrsAccountDto.getCustId());
			sendEmail(reqVo, loginUser,mrsAccountDto.getCustId());
			return;
		}
		// （c） 如果接入平台认证类型为“需要认证”且一户通认证状态为“认证成功”和“无需认证”，不更新任何信息。
		else if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(newPlatform.getIsAuth())
				&& MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			// 场景3 原记录已经认证成功,新请求是不可信渠道
			log.info("场景3  不更新信息");
			return;
		}
		// （d） 如果接入平台认证类型为“需要认证”且一户通认证状态为非“认证成功”和非“无需认证”，
		// 且一户通客户编号为空和请求参数中平台客户编号为空，认证状态为“未认证”（本场景出现在先有密码查询的请求，
		// 后有被动开户的请求情况），更新“个人客户信息表”个人信息，更新“一户通账户表”客户编号。
		else if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(newPlatform.getIsAuth())
				&& !MrsAccountAuthStatus.senseAuthStaus(authStatus)
				&& StringUtil.isEmpty(reqVo.getPlatformCustCode()) 
				&& StringUtil.isEmpty(mrsAccountDto.getCustomerCode())
				&& MrsAccountAuthStatus.MAAS_0.getValue().equals(mrsAccountDto.getAuthStatus())) {
			log.info("场景4  更新客户信息，一户通账户信息");
			BeanUtils.copyProperties(reqVo, person);
			person.setUpdateTime(new Date());
			person.setId(mrsAccountDto.getUserInfoId());
			MrsNationaltyCode nationaltyCode = StringUtil.isEmpty(reqVo.getNationalityCode())? null  : MrsNationaltyCode.getEnum(reqVo.getNationalityCode());
			if(nationaltyCode != null){
				person.setNationalityCode(nationaltyCode);//国籍码
			}
			mrsPersonDtoMapper.updateByPrimaryKeySelective(person);
			mrsAccountDto.setUpdateTime(new Date());
			mrsAccountDto.setCustomerCode(reqVo.getCustomerCode());
			mrsAccountDtoMapper.updateByPrimaryKeySelective(mrsAccountDto);
			return;
		}
		// （e） 如果接入平台认证类型为“需要认证”且一户通认证状态为非“认证成功”和非“无需认证”，
		// 且一户通客户编号不为空或请求参数中平台客户编号不为空，不更新任何信息。
		else if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(newPlatform.getIsAuth())
				&& !MrsAccountAuthStatus.senseAuthStaus(authStatus)
				&& StringUtil.isNEmpty(reqVo.getPlatformCustCode())
				&& StringUtil.isNEmpty(mrsAccountDto.getCustomerCode())) {
			log.info("场景5 不更新任何信息");
			return;
		} else {
			log.info("没有对应的状态数据，不更新任何信息");
		}
	}
	/**
	 * 根据请求参数中渠道编号查询“子账户配置表”该渠道开通个人客户必须开通的子账户编号，
	 * 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 * @param newPlatform
	 * @return
	 * @throws CodeCheckedException
	 */
	private List<MrsConfSubAcctDto> checkAndSaveAct(IndividualRequestVO reqVo, MrsAccountDto retrunMrsAccountDto,
			MrsPlatformDto newPlatform) throws CodeCheckedException {
		List<MrsSubAccountDto> subAccountList = null;
		List<MrsSubAccountDto> subActList = mrsSubAccountDtoMapper.findByCustId(retrunMrsAccountDto.getCustId());
		// 根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_0,
				reqVo.getPlatformCode(),reqVo.getAccountType());
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
	/**
	 * 根据请求参数中渠道编号查询“子账户配置表”该渠道开通个人客户必须开通的子账户编号，
	 * 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 * @param newPlatform
	 * @return
	 * @throws CodeCheckedException
	 */
	private List<MrsConfSubAcctDto> checkAndSavePerson(AccountIndividualUpdateReqVO reqVo, MrsAccountDto retrunMrsAccountDto,
			MrsPlatformDto newPlatform) throws CodeCheckedException {
	 	List<MrsSubAccountDto> subAccountList = null;
		List<MrsSubAccountDto> subActList = mrsSubAccountDtoMapper.findByCustId(retrunMrsAccountDto.getCustId());
		// 根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_0,
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
	 * 建新一户通客户关系
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 */
	private void saveMrsAccountPlatformCust(IndividualRequestVO reqVo, MrsAccountDto retrunMrsAccountDto) {
		MrsAccountPlatformCustDto actPlatformDto = new MrsAccountPlatformCustDto();
		actPlatformDto.setId(actPlatformDto.getIdentity());
		actPlatformDto.setCustId(retrunMrsAccountDto.getCustId());
		actPlatformDto.setPlatformCustCode(reqVo.getPlatformCustCode());
		actPlatformDto.setSource(reqVo.getPlatformCode());
		actPlatformDto.setCreateTime(new Date());
		mrsAccountPlatformCustDtoMapper.insert(actPlatformDto);
	}
	/**
	 * 建新一户通客户关系
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 */
	private void saveMrsAccountPlatformIvidual(AccountIndividualUpdateReqVO reqVo, MrsAccountDto retrunMrsAccountDto) {
		MrsAccountPlatformCustDto actPlatformDto = new MrsAccountPlatformCustDto();
		actPlatformDto.setId(actPlatformDto.getIdentity());
		actPlatformDto.setCustId(retrunMrsAccountDto.getCustId());
		actPlatformDto.setPlatformCustCode(reqVo.getPlatformCustCode());
		actPlatformDto.setSource(reqVo.getPlatformCode());
		actPlatformDto.setCreateTime(new Date());
		mrsAccountPlatformCustDtoMapper.insert(actPlatformDto);
	}
	/**
	 * 强制开户
	 * 
	 * @throws Exception
	 */
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto forceOpenAcnt(IndividualRequestVO reqVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException {
		MrsAccountDto mrsAccountDto = null;
		// （a） 判断查询记录中认证状态为“已生效”、账户状态为非“已注销”的记录条数，
		// 并查询“系统参数表”强制开户的个数限制，如果记录条数大于等于个数限制，
		// 则记录错误日志并返回处理结果“开通失败”。
		boolean checkFlag = mrsAccountService.check3ElmentIsMax(reqVo.getCredentialsType(),
				reqVo.getCredentialsNumber(), reqVo.getCustomerName(), MrsCustomerType.MCT_0.getValue());
		// （b） 进行一户通开户，具体流程与上面开户流程相同
		if (checkFlag) {
			log.info("进行强制开户");
			mrsAccountDto = this.initOpenAcnt(reqVo,mrsPlatformDto);
		} else {
			log.error("强制开户失败,查询“系统参数表”强制开户的个数已超过限制");
			throw new CodeCheckedException("强制开户失败,查询“系统参数表”强制开户的个数已超过限制");
		}
		return mrsAccountDto;
	}

	@Override
	public AccountSearchRespVO findAccountExist(AccountSearchReqVO reqVo) throws CodeCheckedException {
		AccountSearchRespVO respVo = new AccountSearchRespVO();
		// 根据接入平台编号查询平台信息
		MrsPlatformDto mrsPlatformDto = mrsPlatformDaoMapper.findByPlatformCode(reqVo.getPlatformCode(),
				MrsPlatformStatus.PS_0.getValue());
		// 判断平台开户信息是否需要认证
		if (mrsPlatformDto == null) {
			log.error("开户渠道不存在，查询条件[status = {},platformCode={}]",
					new Object[] { MrsPlatformStatus.PS_0.getValue(), reqVo.getPlatformCode() });
			throw new CodeCheckedException(PortalCode.CODE_9999, "开户渠道不存在");
		}
		List<MrsAccountDto> accountList = mrsAccountDtoMapper.findByPerson3Element(reqVo.getCustomerName(),
				reqVo.getCertType(), reqVo.getCertNo());
		if (CollectionUtil.isEmpty(accountList)) {
			log.info("一户通账户[name={}, type = {}, number = {}]不存在",
					new Object[] { reqVo.getCustomerName(), reqVo.getCertType(), reqVo.getCertNo() });
			respVo.setHasAccount("N");
		} else {
			respVo.setHasAccount("Y");
		}
		return respVo;
	}

	

	private MrsLoginUserDto copy2LoginUserDto(IndividualRequestVO requestVo, String custId) {
		MrsLoginUserDto dto = new MrsLoginUserDto();
		dto.setCreateTime(new Date());
		String loginPwd = RandomUtil.generate6Random();
		dto.setNoEncryptloginPwd(loginPwd); // 明文密码
		loginPwd = MD5Utils.MD5(loginPwd);
		loginPwd = MD5Utils.MD5(loginPwd + SHIEConfigConstant.SALT);
		dto.setLoginPwd(loginPwd);
		dto.setAccountCode(custId);
		dto.setId(UUID.randomUUID().toString());
		dto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
		//如果客户填写邮箱，就同步更新到登录表
		String userEmail = requestVo.getEmail();
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(userEmail)){
			dto.setEmail(userEmail);
		}
		return dto;
	}

	

	/**
	 * 生成子账户信息
	 * 
	 * @return
	 * @throws CodeCheckedException
	 */
	private List<MrsSubAccountDto> generateSubAccount(String custId, MrsPlatformDto mrsPlatformDto,
			List<MrsConfSubAcctDto> mrsConfSubAcctDtos) throws CodeCheckedException {
		List<MrsSubAccountDto> subAccountDtoList = new ArrayList<MrsSubAccountDto>();
		// 根据客户类型，关联关系查询子账户配置
		if (mrsConfSubAcctDtos != null && mrsConfSubAcctDtos.size() > 0) {
			for (MrsConfSubAcctDto mrsConfSubAcctDto : mrsConfSubAcctDtos) {
				MrsSubAccountDto insureAccountDto = new MrsSubAccountDto();
				insureAccountDto.setId(UUID.randomUUID().toString());
				insureAccountDto.setCustId(custId); // 一户通编号
				String subInsure = mrsConfSubAcctDto.getSubAccountCode() + "0" + custId;
				insureAccountDto.setSubAccountCode(subInsure); // 子账户编号
				insureAccountDto.setSubAccountName(mrsConfSubAcctDto.getSubAccountName());//子账户名称
				insureAccountDto.setSubAccountType(mrsConfSubAcctDto.getSubAccountCode()); // 子账户类型
				if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(mrsPlatformDto.getIsAuth())) {
					insureAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue()); // 子账户状态
				} else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())) {
					insureAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue()); // 子账户状态
				}
				insureAccountDto.setAccountOwnType(MrsSubAccountOwnType.MSAOT_01.getValue()); // 账户持有类型
				insureAccountDto.setAccountSource(MrsAccountSource.SOURCE_05.getValue()); // 开户方式
				insureAccountDto.setPlatformCode(mrsPlatformDto.getPlatformCode()); // 开户渠道
				insureAccountDto.setOpenTime(new Date()); // 开户时间
				insureAccountDto.setCreateTime(new Date()); // 创建时间
				subAccountDtoList.add(insureAccountDto);
			}
		}
		return subAccountDtoList;
	}

	/**
	 * 开通资金账户
	 * 
	 * @param custId
	 * @param type
	 * @param customerName
	 * @throws Exception
	 */
	private void createAccounts(String custId, MrsCustomerType type, String customerName,
			List<MrsConfSubAcctDto> mrsConfSubAcctDtos) throws CodeCheckedException {
		// 根据客户类型，关联关系查询子账户配置
		if (mrsConfSubAcctDtos != null && mrsConfSubAcctDtos.size() > 0) {
			List<String> confSubIds = new ArrayList<String>();
			for (MrsConfSubAcctDto mrsConfSubAcctDto : mrsConfSubAcctDtos) {
				confSubIds.add(mrsConfSubAcctDto.getId());
			}
			// 开通资金账户
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

	/**
	 * 封装个人客户信息
	 * 
	 * @param requestVo
	 * @param custId
	 * @return
	 */
	private MrsPersonDto copy2PersonDto(IndividualRequestVO requestVo, String custId) {
		MrsPersonDto dto = new MrsPersonDto();
		BeanUtils.copyProperties(requestVo, dto);
		MrsNationaltyCode nationaltyCode = StringUtil.isEmpty(requestVo.getNationalityCode())? null  : MrsNationaltyCode.getEnum(requestVo.getNationalityCode());
		if(nationaltyCode != null){
			dto.setNationalityCode(nationaltyCode);//国籍码
		}
		dto.setCreateTime(new Date());
		dto.setId(UUID.randomUUID().toString());
		dto.setCustId(custId);
		return dto;
	}

	/**
	 * 封装一户通账户信息
	 * 
	 * @param requestVo
	 * @param custId
	 * @param platformCode
	 * @return
	 */
	private MrsAccountDto copy2AccountDto(IndividualRequestVO requestVo, String custId, String isAuth) {
		MrsAccountDto dto = new MrsAccountDto();
		Date now = new Date();
		BeanUtils.copyProperties(requestVo, dto);
		dto.setCustId(custId);
		dto.setAccountName(requestVo.getCustomerName());
		dto.setCustomerType(MrsCustomerType.MCT_0.getValue());
		dto.setAccountSource(MrsAccountSource.SOURCE_05.getValue());
		if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(isAuth)) {
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
		} else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(isAuth)) {
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
		}
		if (AaccountType.BSYHT.getValue().equals(requestVo.getAccountType())) {
			dto.setAccountType(AaccountType.BSYHT);
		} else if (AaccountType.CYRYHT.getValue().equals(requestVo.getAccountType())) {
			dto.setAccountType(AaccountType.CYRYHT);
		}
		dto.setCreateTime(now);
		dto.setOpenTime(now);
		dto.setId(UUID.randomUUID().toString());
		dto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
		dto.setOpenTime(now);
		return dto;
	}

	/**
	 * 1,根据账号和三要素查找有效的一户通账号(只有全部符合条件才能查出数据) 2,判断一户通账号是否为空，如果为空直接返回
	 * 3,重新生成登陆密码(因为之前生成的密码是密文形式，不支持解密) 4,修改登陆密码 5,返回登陆信息
	 * 
	 * @throws CodeCheckedException
	 */
	@Override
	public LoginMsgSearchResponseVO findLoginMsg(LoginMsgSearchRequestVO reqVo) throws CodeCheckedException {

		LoginMsgSearchResponseVO respVo = new LoginMsgSearchResponseVO();

		List<MrsAccountDto> accountList = mrsAccountDtoMapper.findByCustIdAnd3Elements(reqVo.getCustomerName(),
				reqVo.getCertType(), reqVo.getCertNo());
		if (CollectionUtil.isEmpty(accountList)) {
			log.error("一户通账户[name={}, type = {}, number = {}]不存在",
					new Object[] { reqVo.getCustomerName(), reqVo.getCertType(), reqVo.getCertNo() });
			return null;
		}
		//若返回结果为单笔
		if(accountList.size()==1){
			MrsAccountDto accountDto = accountList.get(0);
			String custId = accountDto.getCustId();
			if (StringUtil.isNEmpty(reqVo.getAccountCode()) && !reqVo.getAccountCode().equals(custId)) {
				log.error("一户通账户错误[custId={}, accountCode = {}]", new Object[] { custId, reqVo.getAccountCode() });
				throw new CodeCheckedException(PortalCode.CODE_9999, "一户通账号错误");
			}
			/*if (MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(accountDto.getAccountStatus())) {
				// 未生效
				log.info("一户通账户[custId={}]未生效返回密码]", custId);
				String loginPwd = RandomUtil.generate6Random();
				String encryLoginPwd = MD5Utils.MD5(MD5Utils.MD5(loginPwd) + SHIEConfigConstant.SALT);
				MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(custId);
				loginUser.setLoginPwd(encryLoginPwd);
				mrsLoginUserDtoMapper.updateByPrimaryKeySelective(loginUser);
				respVo.setCustId(custId);
				respVo.setLoginPwd(loginPwd);
				respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_0);
			} else {
				//
				log.info("一户通账户[custId={}]已生效不返回密码", custId);
				respVo.setCustId(custId);
				respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_1);
			}*/
			//根据一户通认证状态返回不同信息
			//认证成功或无需认证,或账户状态是正常,不返回密码
			if(MrsAccountAuthStatus.MAAS_2.getValue().equals(accountDto.getAuthStatus())
					||MrsAccountAuthStatus.MAAS_9.getValue().equals(accountDto.getAuthStatus())
					||MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(accountDto.getAccountStatus())){
				log.info("一户通账户[custId={}]认证成功或无需认证,或账户状态是正常,不返回密码", custId);
				respVo.setCustId(custId);
				respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_1);
				//非认证状态和非无需认证,返回密码
			}else{
				returnCustInfo(respVo, custId);
			}
		//返回结果为多笔
		}else{
			//请求参数一户通编号不为空,查询一户通相同的记录
			if(!StringUtils.isBlank(reqVo.getAccountCode())){
				MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(reqVo.getAccountCode());
			    if(mrsAccountDto == null){
			    	log.error("根据一户通编号:custId[{}]查询结果为空!",reqVo.getAccountCode());
			    	respVo.setMsgInfo("密码查询失败!");
			    }else{
			    	if(MrsAccountAuthStatus.MAAS_2.getValue().equals(mrsAccountDto.getAuthStatus())
			    	        ||MrsAccountAuthStatus.MAAS_9.getValue().equals(mrsAccountDto.getAuthStatus())){
			    		log.info("一户通账户[custId={}]认证成功或无需认证不返回密码", mrsAccountDto.getCustId());
						respVo.setCustId(mrsAccountDto.getCustId());
						respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_1);
			    	}else{
			    		returnCustInfo(respVo, mrsAccountDto.getCustId());
			    	}
			    }
			//请求参数中一户通编号为空,首先取认证状态为"认证成功"或"无需认证"的记录
			}else{
				List<MrsAccountDto> mrsAcccountList = new ArrayList<MrsAccountDto>();
			    for(MrsAccountDto dto:accountList){
			    	if(MrsAccountAuthStatus.MAAS_2.getValue().equals(dto.getAuthStatus())
			    	        ||MrsAccountAuthStatus.MAAS_9.getValue().equals(dto.getAuthStatus())){
			    		mrsAcccountList.add(dto);
			    	}
			    }
			    if(mrsAcccountList.size()>1){
			    	MrsAccountDto mrsAccountDto = accountList.get(0);
			        if(MrsAccountAuthStatus.MAAS_2.getValue().equals(mrsAccountDto.getAuthStatus())
			    	        ||MrsAccountAuthStatus.MAAS_9.getValue().equals(mrsAccountDto.getAuthStatus())
			    	        ||MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(mrsAccountDto.getAccountStatus())){
						log.info("一户通账户[custId={}]认证成功或无需认证,或账户状态是正常,不返回密码", mrsAccountDto.getCustId());
						respVo.setCustId(mrsAccountDto.getCustId());
						respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_1);
			        }else{
			        	returnCustInfo(respVo, mrsAccountDto.getCustId());
			        }
			    }else{
			    	MrsAccountDto mrsAccountDto = mrsAcccountList.get(0);
			        if(MrsAccountAuthStatus.MAAS_2.getValue().equals(mrsAccountDto.getAuthStatus())
			    	        ||MrsAccountAuthStatus.MAAS_9.getValue().equals(mrsAccountDto.getAuthStatus())
			    	        ||MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(mrsAccountDto.getAccountStatus())){
						log.info("一户通账户[custId={}]认证成功或无需认证,或账户状态是正常,不返回密码", mrsAccountDto.getCustId());
						respVo.setCustId(mrsAccountDto.getCustId());
						respVo.setRealNameAuth(SHIEConfigConstant.REAL_NAME_AUTH_1);
			        }else{
			        	returnCustInfo(respVo, mrsAccountDto.getCustId());
			        }
			    }
			
			}
			
		}
		return respVo;
	}
	/**
	 * 返回账户的密码
	 * @param respVo
	 * @param custId
	 */
	private void returnCustInfo(LoginMsgSearchResponseVO respVo, String custId) {
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

	@Override
	public PageData<MrsPersonDto> findPerson(PageData<MrsPersonDto> pageData, MrsPersonDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsPersonDto> list = mrsPersonDtoMapper.list(searchDto);
		Page<MrsPersonDto> page = (Page<MrsPersonDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public MrsPersonDto findById(String id) {
		return mrsPersonDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public MrsPersonDto findByCustId(String custId) {
		return mrsPersonDtoMapper.findByCustId(custId);
	}

	@Override
	public PersonVO findPersonVoByCustId(String custId) {
		return mrsPersonDtoMapper.findPersonVoByCustId(custId);
	}

	@Override
	public void update(MrsPersonDto dto) {
		mrsPersonDtoMapper.updateByPrimaryKeySelective(dto);
	}

	@Override
	public void updateByPrimaryKey(MrsPersonDto dto) {
		mrsPersonDtoMapper.updateByPrimaryKey(dto);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateAndSync(MrsPersonDto dto) {
		mrsPersonDtoMapper.updateByPrimaryKey(dto);
		// 同步基本信息
		MrsNotifyDto notifyDto = generateSyncInfo(dto, MrsNotifyType.PERSON);
		mrsNotifyDtoMapper.insert(notifyDto);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updatePicAndSync(MrsPersonDto dto) {
		mrsPersonDtoMapper.updateByPrimaryKey(dto);
		// 同步证件信息
		MrsNotifyDto fileNotifyDto = generateSyncFileInfo(dto, MrsNotifyType.FILE);
		if (fileNotifyDto != null) {
			mrsNotifyDtoMapper.insert(fileNotifyDto);
		}
	}

	/**
	 * 同步证件信息
	 * 
	 * @param dto
	 * @param notifyType
	 * @return
	 */
	private MrsNotifyDto generateSyncFileInfo(MrsPersonDto dto, MrsNotifyType notifyType) {
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

	/**
	 * 同步基本信息
	 * 
	 * @param dto
	 * @param notifyType
	 * @return
	 */
	private MrsNotifyDto generateSyncInfo(MrsPersonDto dto, MrsNotifyType notifyType) {
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
		SyncPersonReq req = new SyncPersonReq();
		MrsAccountDto accountDto = mrsAccountDtoMapper.findByCustId(dto.getCustId());
		if(null!=accountDto){
			req.setCustomerCode(accountDto.getCustomerCode());
		}
		req.setCustomerName(dto.getCustomerName());
		req.setCredentialsType(dto.getCredentialsType());
		req.setCredentialsNumber(dto.getCredentialsNumber());
		req.setNationalityCode(dto.getNationalityCode());
		req.setCustomerTypeCode(MrsCustomerTypeCode.MCT_1.getValue());
		String endDate = dto.getCredentialsEnddate();
		if (StringUtil.isNEmpty(endDate)) {
			endDate = endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8);
			req.setCredentialsEndDate(endDate);
		}
		String birthDay = dto.getBirthdate();
		if (StringUtil.isNEmpty(birthDay)) {
			birthDay = birthDay.substring(0, 4) + "-" + birthDay.substring(4, 6) + "-" + birthDay.substring(6, 8);
			req.setBirthDate(birthDay);
		}
		req.setSexCode(dto.getSexCode());
		req.setEducationCode(dto.getEducationCode());
		req.setNationalCode(dto.getNationalCode());

		req.setMobile(dto.getMobile());
		String phoneNumber = "";
		if (StringUtil.isNEmpty(dto.getTel())) {
			phoneNumber = dto.getTel();
		}

		if (StringUtil.isNEmpty(dto.getSpareTel())) {
			if (StringUtil.isNEmpty(phoneNumber)) {
				phoneNumber += "," + dto.getSpareTel();
			} else {
				phoneNumber = dto.getTel();
			}
		}
		req.setPhoneNumber(phoneNumber);
		req.setFullAddress(dto.getContactAddr());
		req.setEmail(dto.getEmail());
		req.setZipCode(dto.getZipCode());
		req.setSysSourceCode(SHIEConfigConstant.SYNC_SYS_SOURCE_CODE);
		req.setCreateBy(SHIEConfigConstant.SYNC_CREATE_BY);
		req.setUpdateBy(SHIEConfigConstant.SYNC_UPDATE_BY);
		req.setOccupationCode(dto.getTwoOccupation());  // 二级 小类
		req.setOccuriskRankCode(dto.getOneOccupation()); // 一级  大类

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

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updatePerson(MrsPersonDto dto, String loginName) {
		// 转成json串
		String jsonStr = JsonUtil.ObjectToJson(dto);
		// 保存数据审核信息表
		mrsDataAuditChangeService.save(dto.getId(), EAuditChangeType.AUDIT_UPDATE, jsonStr, EAuditStatus.AUDIT_WAIT,
				loginName, EAuditUserType.AUDIT_CP);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsLoginUserDto restMobile(String custId, String mobile) {

		// 修改TB_MRS_LOGIN_USER中的手机号

		MrsLoginUserDto dto = mrsLoginUserDtoMapper.findByMobile(mobile);
		if (dto != null) {
			log.error("手机已经存在[mobile = " + mobile + "]");
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.MOBILE_EXIST));
			return dto;
		}
		int modifyRow = mrsLoginUserDtoMapper.updateMobile(custId, mobile);
		if (modifyRow < 1) {
			log.error("更新失败[custId = " + custId + ", mobile = " + mobile + "]");
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.UPDATE_FAIL));
			return dto;
		}
		// 修改TB_MRS_PERSON中的手机号
		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(custId);
		if (loginUser == null) {
			log.error("查询MrsLoginUserDto对象失败[custId = " + custId + "]");
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.UPDATE_FAIL));
			return dto;
		}
		loginUser.setUserCheckVo(new UserCheckVO(true));
		return loginUser;
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsLoginUserDto restEmail(String custId, String email) {
		// 修改TB_MRS_LOGIN_USER中的邮箱
		MrsLoginUserDto dto = new MrsLoginUserDto();
		List<MrsLoginUserDto>  usrtList = mrsLoginUserDtoMapper.findByEmail(email);
		if (usrtList != null && usrtList.size()>0) {
			log.error("邮箱已经存在[email = " + email + "]");
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.EMAIL_EXIST));
			return dto;
		}
		int modifyRow = mrsLoginUserDtoMapper.updateMobile(custId, email);
		if (modifyRow < 1) {
			log.error("更新失败[custId = " + custId + ", email = " + email + "]");
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.UPDATE_FAIL));
			return dto;
		}
		// 修改TB_MRS_PERSON中的邮箱
		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByCustId(custId);
		if (loginUser == null) {
			log.error("查询MrsLoginUserDto对象失败[custId = " + custId + "]");
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.UPDATE_FAIL));
			return dto;
		}
		loginUser.setUserCheckVo(new UserCheckVO(true));
		return loginUser;
	}

	@Override
	public MrsLoginUserDto restEmailById(String id, String email) {
		// 修改TB_MRS_LOGIN_USER中的邮箱
		MrsLoginUserDto dto = new MrsLoginUserDto();
		List<MrsLoginUserDto>  usrtList = mrsLoginUserDtoMapper.findByEmail(email);
		if (usrtList != null && usrtList.size()>0) {
			log.error("邮箱已经存在[email = " + email + "]");
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.EMAIL_EXIST));
			return dto;
		}
		int modifyRow = mrsLoginUserDtoMapper.updateEmailById(id, email);
		if (modifyRow < 1) {
			log.error("更新失败[id = " + id + ", mobile = " + email + "]");
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.UPDATE_FAIL));
			return dto;
		}
		// 修改TB_MRS_PERSON中的邮箱
		MrsLoginUserDto loginUser = mrsLoginUserDtoMapper.selectByPrimaryKey(id);
		if (loginUser == null) {
			log.error("查询MrsLoginUserDto对象失败[id = " + id + "]");
			dto = new MrsLoginUserDto();
			dto.setUserCheckVo(new UserCheckVO(false, ErrorMsgEnum.UPDATE_FAIL));
			return dto;
		}
		loginUser.setUserCheckVo(new UserCheckVO(true));
		return loginUser;
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void authSuccess(String custId) throws PortalCheckedException {
		// 修改主账户状态和认证状态，修改子账户状态
		Date date = new Date();
		int rows = mrsAccountDtoMapper.updateAcntStatusByCustId(custId, MrsAccountStatus.ACCOUNT_STATUS_0.getValue(),
				MrsAccountAuthStatus.MAAS_2.getValue(), date);
		if (rows <= 0) {
			log.error("修改失败，影响的行数[rows=" + rows + "],custId = " + custId);
			throw new PortalCheckedException(ErrorMsgEnum.UPDATE_FAIL.getKey());
		}
		rows = mrsSubAccountDtoMapper.updateByCustId(custId, MrsSubAccountStatus.MSAS_0.getValue(), date);
		// 同步信息
		MrsPersonDto dto = mrsPersonDtoMapper.findByCustId(custId);
		MrsNotifyDto notifyDto = generateSyncInfo(dto, MrsNotifyType.PERSON);
		mrsNotifyDtoMapper.insert(notifyDto);
		if (rows <= 0) {
			log.error("修改失败，影响的行数[rows=" + rows + "],custId = " + custId);
			throw new PortalCheckedException(ErrorMsgEnum.UPDATE_FAIL.getKey());
		}
	}

	@Override
	public void updateFileId(String custId, String fileId) throws PortalCheckedException {
		// 将FileId字段更新到TB_MRS_PERSON表中
		int rows = mrsPersonDtoMapper.updateFileId(custId, fileId, new Date());
		if (rows < 0) {
			log.error("更新失败:" + rows);
			throw new PortalCheckedException(ErrorMsgEnum.FILE_UPLOAD_FAIL.getKey());
		}
		// 将TB＿MRS_ACCOUNT表的认证状态修改为认证中
		rows = mrsAccountDtoMapper.updateAuthStatus(custId, MrsAccountAuthStatus.MAAS_1.getValue(), new Date());
		if (rows < 0) {
			log.error("更新失败:" + rows);
			throw new PortalCheckedException(ErrorMsgEnum.FILE_UPLOAD_FAIL.getKey());
		}
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public UserCheckVO uploadPojoList(String custId, List<UploadPersonPojo> uploadPojoList) {
		int rows = mrsCertFileDtoMapper.deleteByCustId(custId);
		log.info("根据[custId={}]删除了{}条数据", new Object[] { custId, rows });
		List<MrsCertFileDto> list = new ArrayList<MrsCertFileDto>();
		MrsCertFileDto dto = null;
		for (UploadPersonPojo pojo : uploadPojoList) {
			dto = new MrsCertFileDto();
			dto.setCertType(pojo.getCertType().getValue());
			dto.setCreateTime(new Date());
			dto.setCustId(custId);
			dto.setCustomerType(MrsCustomerType.MCT_0.getValue());
			dto.setFileId(pojo.getFileId());
			dto.setFileType(pojo.getFileType().getValue());
			dto.setId(UUID.randomUUID().toString());
			list.add(dto);
		}
		mrsCertFileDtoMapper.batchInsert(list);
		rows = mrsAccountDtoMapper.updateAuthStatus(custId, MrsAccountAuthStatus.MAAS_1.getValue(), new Date());
		if (rows < 0) {
			log.error("更新失败:" + rows);
			return new UserCheckVO(false, ErrorMsgEnum.FILE_UPLOAD_FAIL);
		}
		return new UserCheckVO(true);
	}

	@Override
	public PageData<MrsPersonDto> findPersonByUpdateAudit(PageData<MrsPersonDto> pageData, MrsPersonDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsPersonDto> list = mrsPersonDtoMapper.updateAuditList(searchDto);
		Page<MrsPersonDto> page = (Page<MrsPersonDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;

	}

	@Override
	public int insertSelective(MrsPersonDto dto) {
		return mrsPersonDtoMapper.insertSelective(dto);
	}

	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitPersonResponseVo saveAduitPerson(MrsPersonVo personVo) {
		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		try {
			List<MrsLoginUserDto> loginUsers;
			MrsPersonDto newPerson = null;
			MrsAccountDto account = null;
			List<AccountSubsPojo> subPojos = null;
			//不为非有效的一户通开户
			if (!UseAccountType.USE_01.getValue().equals(personVo.getIsForce())) {
				/**
				 * 查询正常的
				 */
				List<MrsAccountDto> effAccounts = mrsAccountDtoMapper.findPersonBy3EleAndStatus(personVo.getMrsPersonDto().getCustomerName(),
						personVo.getMrsPersonDto().getCredentialsType(),
						personVo.getMrsPersonDto().getCredentialsNumber(),MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
				if(!CollectionUtil.isEmpty(effAccounts)){
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，一户通已经存在！");
					return response;
				}
				
				// 基本校验 校验传递的对象是否为空
				response = validateBasePerson(personVo);
				if(!response.getIsSucess()){
					return response;
				}
				
				//子账户配置信息查询需要改造
				List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService.findBy4Elment(
						MrsCustomerType.MCT_0,  MrsConfSubRelationType.MUST, 
						MrsPlatformCode.ACCOUNT.getValue(), personVo.getMrsAccountDto().getAccountType().getValue());
//				/**
//				 * 检查子账户信息 子账户的个数是否大于个人必须的
//				 */
//				List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService
//						.findByUserTypeAndRationType(MrsCustomerType.MCT_0, MrsConfSubRelationType.MUST, null);
				if (mrsConfSubAcctDtos == null || mrsConfSubAcctDtos.isEmpty()) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，没有配置子账户设置参数！");
					return response;
				}
				if (personVo.getMrsConfSubAcctIds().size() < mrsConfSubAcctDtos.size()) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，子账户信息异常！");
					return response;
				}
				
				List<MrsSubAccountDto> mrsSubAccountDtos = new ArrayList<MrsSubAccountDto>();

				for (String sub : personVo.getMrsConfSubAcctIds()) {
					MrsSubAccountDto subAccount = new MrsSubAccountDto();
					MrsConfSubAcctDto dto = mrsConfSubAcctDtoMapper.selectByPrimaryKey(sub);
					if (dto == null) {
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("添加个人用户信息送审时，配置子账户信息异常！");
						return response;
					}
					//校验子账户信息是否正确
					if(MrsConfSubRelationType.NO.getValue().equals(dto.getPersonType())){
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("添加个人用户信息送审时，请选择个人正确的子账户类型！");
						return response;
					}
					if(!MrsPlatformCode.ACCOUNT.getValue().equals(dto.getPlatformCode())){
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("添加个人用户信息送审时，选择的子账户信息渠道错误！");
						return response;
					}
					subAccount.setSubAccountName(dto.getSubAccountName());
					// 子账户编号审核通过后生成
					// subAccount.setSubAccountCode(dto.getSubAccountCode());
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
				personVo.setMrsSubAccountDtos(mrsSubAccountDtos);
				// 校验一户通信息
				if (!StringUtils.isBlank(personVo.getMrsAccountDto().getCustId())) {
					//校验一户通号是否预留改动
					boolean acct = mrsAccountService.checkCustId(personVo.getMrsAccountDto().getCustId());
//					MrsAccountDto accountDto = mrsAccountDtoMapper.findByCustId(personVo.getMrsAccountDto().getCustId());
					if (!acct) {
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("一户通编号已经存在！");
						return response;
					}

				}
				/**
				 * 校验用户信息，用户昵称，用户手机号
				 */
				MrsLoginUserDto loginUser;
				loginUser = mrsLoginUserDtoMapper.findByAlias(personVo.getMrsLoginUserDtos().get(0).getAlias());
				if (loginUser != null) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，用户昵称已经存在！");
					return response;
				}
				loginUser = mrsLoginUserDtoMapper.findByMobile(personVo.getMrsLoginUserDtos().get(0).getMobile());
				if (loginUser != null) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，用户手机已经存在！");
					return response;
				}
				
				loginUsers = personVo.getMrsLoginUserDtos();
				//1，先查子账户配置跟，子账号得出子账号属于大类
				List<String> subAcctTypes = mrsConfAcctDtoMapper.findSubAcctTypesByConfSubIds(personVo.getMrsConfSubAcctIds());
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
				
			} else  {
				/**
				 * 查询未生效的
				 */
				List<MrsAccountDto> noEffAccounts = mrsAccountDtoMapper.findPersonBy3EleAndStatus(personVo.getMrsPersonDto().getCustomerName(),
						personVo.getMrsPersonDto().getCredentialsType(),
						personVo.getMrsPersonDto().getCredentialsNumber(),MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
				if(CollectionUtil.isEmpty(noEffAccounts)){
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("不存在未生效的个人一户通信息！");
					return response;
				}
				// 未生效的 修改后的个人信息
				newPerson = mrsPersonDtoMapper.findByCustId(personVo.getMrsAccountDto().getCustId());
				// 查询个人信息 设置修改的个人信息
				// newPerson.setCustomerName(personVo.getMrsPersonDto().getCustomerName());
				newPerson.setNationalityCode((personVo.getMrsPersonDto().getNationalityCode()));
				// newPerson.setCredentialsType(person.getCredentialsType());
				// newPerson.setCredentialsNumber(personVo.getMrsPersonDto().getCredentialsNumber());
				newPerson.setCredentialsEnddate(personVo.getMrsPersonDto().getCredentialsEnddate());
				newPerson.setBirthdate(personVo.getMrsPersonDto().getBirthdate());
				newPerson.setSexCode(personVo.getMrsPersonDto().getSexCode());
				newPerson.setNationalCode(personVo.getMrsPersonDto().getNationalCode());
				newPerson.setEducationCode(personVo.getMrsPersonDto().getEducationCode());
				// 职业代码
				newPerson.setOneOccupation(personVo.getMrsPersonDto().getOneOccupation());
				newPerson.setTwoOccupation(personVo.getMrsPersonDto().getTwoOccupation());
				newPerson.setMobile(personVo.getMrsPersonDto().getMobile());
				newPerson.setEmail(personVo.getMrsPersonDto().getEmail());
				newPerson.setContactAddr(personVo.getMrsPersonDto().getContactAddr());
				newPerson.setZipCode(personVo.getMrsPersonDto().getZipCode());

				account = mrsAccountDtoMapper.findByCustId(personVo.getMrsAccountDto().getCustId());
				
				loginUsers = new ArrayList<MrsLoginUserDto>();
				
				for(MrsLoginUserDto user : personVo.getMrsLoginUserDtos()){
					MrsLoginUserDto udto = mrsLoginUserDtoMapper.selectByPrimaryKey(user.getId());
					loginUsers.add(udto);
				}
				//子账户信息
				List<MrsSubAccountDto> mrsSubAccounts = mrsSubAccountDtoMapper.findByCustId(newPerson.getCustId());
				personVo.setMrsSubAccountDtos(mrsSubAccounts);
				
			} 
			
			if (newPerson == null) {
				newPerson = personVo.getMrsPersonDto();
				account = personVo.getMrsAccountDto();
				account.setAccountName(personVo.getMrsPersonDto().getCustomerName());
			}
			// 设置一户通
			account.setOpenTime(new Date());
			account.setAccountSource(MrsAccountSource.SOURCE_01.getValue());
			account.setCustomerType(MrsCustomerType.MCT_0.getValue());
			// 柜台
			account.setPlatformCode("account");
			// 科目支持业务类型信息 科目编号
//			List<String> mrsSubPayBusiDtos = mrsSubPayBusiDtoMapper
//					.findByConfSubAcctIds(personVo.getMrsConfSubAcctIds());
			//需要改造的

			/**
			 * 设置审核相关信息 1,设置审核人 2，设置审核主要信息 3，根据选择配置子账户信息 4，设置审核内容信息 5，上传附件到文件服务器
			 */
			// 1设置审核人，查询基础配置设置个人开户审核配置人数
			// 审核主要信息

			String aduitId = mrsAduitInfoService.craateMrsAduitInfo(personVo.getCreateRemark(),
					newPerson.getCredentialsNumber(), newPerson.getCredentialsType(), newPerson.getCustomerName(),
					personVo.getMrsAccountDto().getCustId() == null ? null : personVo.getMrsAccountDto().getCustId(),
					personVo.getMrsAccountDto().getAccountStatus() == null
							? MrsAccountStatus.ACCOUNT_STATUS_9.getValue() : account.getAccountStatus(),
					personVo.getMrsAccountDto().getAuthStatus() == null ? MrsAccountAuthStatus.MAAS_0.getValue()
							: account.getAuthStatus(),
					EStartSystemEnum.SYS_COUNTER, personVo.getCurrentUser().getLoginName(), EOperaTypeEnum.OP_OPEN,
					MrsCustTypeEnum.MRS_CUST_TYPE_00);

			MrsToJsonDto perMrsToJson = new MrsToJsonDto();
			perMrsToJson.setMrsAccountDto(account);
			perMrsToJson.setMrsContactListDtos(personVo.getMrsContactListDtos());
			perMrsToJson.setMrsLoginUserDtoList(loginUsers);
			perMrsToJson.setMrsPersonDto(newPerson);
			perMrsToJson.setMrsSubAccountDtoList(personVo.getMrsSubAccountDtos());
			perMrsToJson.setMrsUserPayPasswordDto(personVo.getMrsUserPayPasswordDto());
			perMrsToJson.setAccountSubsPojos(subPojos);
			perMrsToJson.setMrsAduitAttachmentDtos(personVo.getMrsAduitAttachmentDtos());
			perMrsToJson.setIsForce(personVo.getIsForce());

			// 创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsons = JSONObject.fromObject(perMrsToJson);// 将java对象转换为json对象
			String str = jsons.toString();

			blobs.setAduitId(aduitId);
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			/**
			 * 附件信息上传至服务器，保存附件审核信息
			 */
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
			if (!CollectionUtil.isEmpty(personVo.getMrsAduitAttachmentDtos())) {
				for (MrsAduitAttachmentDto att : personVo.getMrsAduitAttachmentDtos()) {
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
			response.setMsgInfo("柜台端开户送审信息失败！调用核心失败！");
			return response;
		}

	}
	
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitPersonResponseVo saveAduitPersondobb(MrsPersonVo personVo,String id) {
		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		try {
			List<MrsLoginUserDto> loginUsers;
			MrsPersonDto newPerson = null;
			MrsAccountDto account = null;
			List<AccountSubsPojo> subPojos = null;
			//不为非有效的一户通开户
			if (!UseAccountType.USE_01.getValue().equals(personVo.getIsForce())) {
				/**
				 * 查询正常的
				 */
				List<MrsAccountDto> effAccounts = mrsAccountDtoMapper.findPersonBy3EleAndStatus(personVo.getMrsPersonDto().getCustomerName(),
						personVo.getMrsPersonDto().getCredentialsType(),
						personVo.getMrsPersonDto().getCredentialsNumber(),MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
				if(!CollectionUtil.isEmpty(effAccounts)){
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，一户通已经存在！");
					return response;
				}
				
				// 基本校验 校验传递的对象是否为空
				response = validateBasePerson(personVo);
				if(!response.getIsSucess()){
					return response;
				}
				
				//子账户配置信息查询需要改造
				List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService.findBy4Elment(
						MrsCustomerType.MCT_0,  MrsConfSubRelationType.MUST, 
						MrsPlatformCode.ACCOUNT.getValue(), personVo.getMrsAccountDto().getAccountType().getValue());
//				/**
//				 * 检查子账户信息 子账户的个数是否大于个人必须的
//				 */
//				List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService
//						.findByUserTypeAndRationType(MrsCustomerType.MCT_0, MrsConfSubRelationType.MUST, null);
				if (mrsConfSubAcctDtos == null || mrsConfSubAcctDtos.isEmpty()) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，没有配置子账户设置参数！");
					return response;
				}
				if (personVo.getMrsConfSubAcctIds().size() < mrsConfSubAcctDtos.size()) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，子账户信息异常！");
					return response;
				}
				
				List<MrsSubAccountDto> mrsSubAccountDtos = new ArrayList<MrsSubAccountDto>();

				for (String sub : personVo.getMrsConfSubAcctIds()) {
					MrsSubAccountDto subAccount = new MrsSubAccountDto();
					MrsConfSubAcctDto dto = mrsConfSubAcctDtoMapper.selectByPrimaryKey(sub);
					if (dto == null) {
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("添加个人用户信息送审时，配置子账户信息异常！");
						return response;
					}
					//校验子账户信息是否正确
					if(MrsConfSubRelationType.NO.getValue().equals(dto.getPersonType())){
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("添加个人用户信息送审时，请选择个人正确的子账户类型！");
						return response;
					}
					if(!MrsPlatformCode.ACCOUNT.getValue().equals(dto.getPlatformCode())){
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("添加个人用户信息送审时，选择的子账户信息渠道错误！");
						return response;
					}
					subAccount.setSubAccountName(dto.getSubAccountName());
					// 子账户编号审核通过后生成
					// subAccount.setSubAccountCode(dto.getSubAccountCode());
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
				personVo.setMrsSubAccountDtos(mrsSubAccountDtos);
				// 校验一户通信息
				if (!StringUtils.isBlank(personVo.getMrsAccountDto().getCustId())) {
					//校验一户通号是否预留改动
					boolean acct = mrsAccountService.checkCustId(personVo.getMrsAccountDto().getCustId());
//					MrsAccountDto accountDto = mrsAccountDtoMapper.findByCustId(personVo.getMrsAccountDto().getCustId());
					if (!acct) {
						response.setIsSucess(false);
						response.setMsgCode("");
						response.setMsgInfo("一户通编号已经存在！");
						return response;
					}

				}
				/**
				 * 校验用户信息，用户昵称，用户手机号
				 */
				MrsLoginUserDto loginUser;
				loginUser = mrsLoginUserDtoMapper.findByAlias(personVo.getMrsLoginUserDtos().get(0).getAlias());
				if (loginUser != null) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，用户昵称已经存在！");
					return response;
				}
				loginUser = mrsLoginUserDtoMapper.findByMobile(personVo.getMrsLoginUserDtos().get(0).getMobile());
				if (loginUser != null) {
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("添加个人用户信息送审时，用户手机已经存在！");
					return response;
				}
				
				loginUsers = personVo.getMrsLoginUserDtos();
				//1，先查子账户配置跟，子账号得出子账号属于大类
				List<String> subAcctTypes = mrsConfAcctDtoMapper.findSubAcctTypesByConfSubIds(personVo.getMrsConfSubAcctIds());
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
				
			} else  {
				/**
				 * 查询未生效的
				 */
				List<MrsAccountDto> noEffAccounts = mrsAccountDtoMapper.findPersonBy3EleAndStatus(personVo.getMrsPersonDto().getCustomerName(),
						personVo.getMrsPersonDto().getCredentialsType(),
						personVo.getMrsPersonDto().getCredentialsNumber(),MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
				if(CollectionUtil.isEmpty(noEffAccounts)){
					response.setIsSucess(false);
					response.setMsgCode("");
					response.setMsgInfo("不存在未生效的个人一户通信息！");
					return response;
				}
				// 未生效的 修改后的个人信息
				newPerson = mrsPersonDtoMapper.findByCustId(personVo.getMrsAccountDto().getCustId());
				// 查询个人信息 设置修改的个人信息
				// newPerson.setCustomerName(personVo.getMrsPersonDto().getCustomerName());
				newPerson.setNationalityCode((personVo.getMrsPersonDto().getNationalityCode()));
				// newPerson.setCredentialsType(person.getCredentialsType());
				// newPerson.setCredentialsNumber(personVo.getMrsPersonDto().getCredentialsNumber());
				newPerson.setCredentialsEnddate(personVo.getMrsPersonDto().getCredentialsEnddate());
				newPerson.setBirthdate(personVo.getMrsPersonDto().getBirthdate());
				newPerson.setSexCode(personVo.getMrsPersonDto().getSexCode());
				newPerson.setNationalCode(personVo.getMrsPersonDto().getNationalCode());
				newPerson.setEducationCode(personVo.getMrsPersonDto().getEducationCode());
				// 职业代码
				newPerson.setOneOccupation(personVo.getMrsPersonDto().getOneOccupation());
				newPerson.setTwoOccupation(personVo.getMrsPersonDto().getTwoOccupation());
				newPerson.setMobile(personVo.getMrsPersonDto().getMobile());
				newPerson.setEmail(personVo.getMrsPersonDto().getEmail());
				newPerson.setContactAddr(personVo.getMrsPersonDto().getContactAddr());
				newPerson.setZipCode(personVo.getMrsPersonDto().getZipCode());

				account = mrsAccountDtoMapper.findByCustId(personVo.getMrsAccountDto().getCustId());
				
				loginUsers = new ArrayList<MrsLoginUserDto>();
				
				for(MrsLoginUserDto user : personVo.getMrsLoginUserDtos()){
					MrsLoginUserDto udto = mrsLoginUserDtoMapper.selectByPrimaryKey(user.getId());
					loginUsers.add(udto);
				}
				//子账户信息
				List<MrsSubAccountDto> mrsSubAccounts = mrsSubAccountDtoMapper.findByCustId(newPerson.getCustId());
				personVo.setMrsSubAccountDtos(mrsSubAccounts);
				
			} 
			
			if (newPerson == null) {
				newPerson = personVo.getMrsPersonDto();
				account = personVo.getMrsAccountDto();
				account.setAccountName(personVo.getMrsPersonDto().getCustomerName());
			}
			// 设置一户通
			account.setOpenTime(new Date());
			account.setAccountSource(MrsAccountSource.SOURCE_01.getValue());
			account.setCustomerType(MrsCustomerType.MCT_0.getValue());
			// 柜台
			account.setPlatformCode("account");
			// 科目支持业务类型信息 科目编号
//			List<String> mrsSubPayBusiDtos = mrsSubPayBusiDtoMapper
//					.findByConfSubAcctIds(personVo.getMrsConfSubAcctIds());
			//需要改造的

			/**
			 * 设置审核相关信息 1,设置审核人 2，设置审核主要信息 3，根据选择配置子账户信息 4，设置审核内容信息 5，上传附件到文件服务器
			 */
			// 1设置审核人，查询基础配置设置个人开户审核配置人数
			// 审核主要信息

			String aduitId = mrsAduitInfoService.craateMrsAduitInfo(personVo.getCreateRemark(),
					newPerson.getCredentialsNumber(), newPerson.getCredentialsType(), newPerson.getCustomerName(),
					personVo.getMrsAccountDto().getCustId() == null ? null : personVo.getMrsAccountDto().getCustId(),
					personVo.getMrsAccountDto().getAccountStatus() == null
							? MrsAccountStatus.ACCOUNT_STATUS_9.getValue() : account.getAccountStatus(),
					personVo.getMrsAccountDto().getAuthStatus() == null ? MrsAccountAuthStatus.MAAS_0.getValue()
							: account.getAuthStatus(),
					EStartSystemEnum.SYS_COUNTER, personVo.getCurrentUser().getLoginName(), EOperaTypeEnum.OP_OPEN,
					MrsCustTypeEnum.MRS_CUST_TYPE_00);

			MrsToJsonDto perMrsToJson = new MrsToJsonDto();
			perMrsToJson.setMrsAccountDto(account);
			perMrsToJson.setMrsContactListDtos(personVo.getMrsContactListDtos());
			perMrsToJson.setMrsLoginUserDtoList(loginUsers);
			perMrsToJson.setMrsPersonDto(newPerson);
			perMrsToJson.setMrsSubAccountDtoList(personVo.getMrsSubAccountDtos());
			perMrsToJson.setMrsUserPayPasswordDto(personVo.getMrsUserPayPasswordDto());
			perMrsToJson.setAccountSubsPojos(subPojos);
			perMrsToJson.setMrsAduitAttachmentDtos(personVo.getMrsAduitAttachmentDtos());
			perMrsToJson.setIsForce(personVo.getIsForce());

			// 创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsons = JSONObject.fromObject(perMrsToJson);// 将java对象转换为json对象
			String str = jsons.toString();
			
			// 根据审核信息表ID查询审核内容表
			List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentAppService.selectByAuditId(id);
			String oldValue = list.get(0).getNewValue();
			
			blobs.setAduitId(aduitId);
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			blobs.setOldValue(oldValue);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			/**
			 * 附件信息上传至服务器，保存附件审核信息
			 */
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
			if (!CollectionUtil.isEmpty(personVo.getMrsAduitAttachmentDtos())) {
				for (MrsAduitAttachmentDto att : personVo.getMrsAduitAttachmentDtos()) {
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
			response.setMsgInfo("柜台端开户送审信息失败！调用核心失败！");
			return response;
		}

	}

	/**
	 * 
	 * 方法描述：传入一户通主键校验一户通账户的有效性 创建人：ydx 创建时间：2017年2月10日 下午2:36:10
	 * 
	 * @param id
	 * @return
	 */
	public boolean validateMrsAccountStatus(String id) {
		MrsAccountDto account = mrsAccountDtoMapper.findByCustId(id);
		if (account != null) {
			//如果是已经已注销
			if (MrsAccountStatus.ACCOUNT_STATUS_2.getValue().equals(account.getAccountStatus())) {
				return true;
			}
		}
		return false;
	}

	public SaveAduitPersonResponseVo validateBasePerson(MrsPersonVo personVo) {
		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		if (personVo == null) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存数据信息为空！");
			return response;
		}
		// 校验传入对象信息是否为空
		// 一户通信息
		if (personVo.getMrsAccountDto() == null) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存个人送审信息，一户通信息为空！");
			return response;
		}
		// 个人用户信息
		if (personVo.getMrsPersonDto() == null) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存个人送审信息，个人用户信息为空！");
			return response;
		}
		// 支付密码信息
		if (personVo.getMrsUserPayPasswordDto() == null) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存个人送审信息，支付密码信息为空！");
			return response;
		}
		// 用户信息
		if (personVo.getMrsLoginUserDtos() == null) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存个人送审信息，用户信息为空！");
			return response;
		}
		// 附件信息后续校验
		if (CollectionUtil.isEmpty(personVo.getMrsAduitAttachmentDtos())) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存个人送审信息，附件信息为空！");
			return response;
		}
		// 联系人信息
		if (CollectionUtil.isEmpty(personVo.getMrsContactListDtos())) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存个人送审信息，联系人信息为空！");
			return response;
		}
		if (CollectionUtil.isEmpty(personVo.getMrsConfSubAcctIds())) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("保存个人送审信息，子账户信息为空！");
			return response;
		}

		response.setIsSucess(true);
		response.setMsgCode("");
		response.setMsgInfo("基本校验通过！");
		return response;
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public PersonVO saveAduitPersonByPortal(PersonVO personVo, MrsLoginUserDto loginUser) {
		PersonVO response = new PersonVO();
		try {
			// 检查三要素
			List<MrsPersonDto> personList = findBy3Element(personVo.getCustomerName(), personVo.getCredentialsType(),
					personVo.getCredentialsNumber());
			if (personList != null&& personList.size()>0) {
				for(MrsPersonDto person: personList){
					// 如果一户通账户不为为生效的状态，直接返回
					if (!validateMrsAccountStatus(person.getCustId())) {
						log.info("一户通信息已经存在！");
						UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.ACCOUNT_CHECK_FAIL);
						response.setCheckVo(userCheckVO);
						return response;
					}
				}
			}
			//设置一户通账户
			MrsAccountDto mrsAccountDto =new MrsAccountDto();
			mrsAccountDto.setOpenTime(new Date());
			mrsAccountDto.setAccountName(personVo.getCustomerName());
			mrsAccountDto.setAccountSource(MrsAccountSource.SOURCE_03.getValue());
			mrsAccountDto.setCustomerType(MrsCustomerType.MCT_0.getValue());
			mrsAccountDto.setPlatformCode(MrsPlatformCode.ACCOUNT.getValue());
			mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_1.getValue());
			mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
			// 子账户
			List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService
					.findBy4Elment(MrsCustomerType.MCT_0, MrsConfSubRelationType.MUST, 
							MrsPlatformCode.ACCOUNT.getValue(),AaccountType.BSYHT.getValue());
			if (mrsConfSubAcctDtos == null || mrsConfSubAcctDtos.isEmpty()) {
				log.info("添加个人用户信息送审时，没有配置子账户设置参数！");
				UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.NO_CONFIG_AUDIT_INFO);
				response.setCheckVo(userCheckVO);
				return response;
			}
			List<String> confSubIds = new ArrayList<String>();
			List<MrsSubAccountDto> mrsSubAccountDtos = new ArrayList<MrsSubAccountDto>();
			for (MrsConfSubAcctDto sub : mrsConfSubAcctDtos) {
				MrsSubAccountDto subAccount = new MrsSubAccountDto();
				subAccount.setSubAccountName(sub.getSubAccountName());
				subAccount.setSubAccountCode(sub.getSubAccountCode());
				subAccount.setSubAccountType(sub.getSubAccountCode());
				subAccount.setOpenTime(new Date());
				subAccount.setAccountOwnType(MrsSubAccountOwnType.MSAOT_01.getValue());
				subAccount.setAccountSource(MrsAccountSource.SOURCE_03.getValue());
				subAccount.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());
				subAccount.setSubAccountType(sub.getSubAccountCode());
				mrsSubAccountDtos.add(subAccount);
				String confSubId = sub.getId();
				confSubIds.add(confSubId);

			}
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
			// 审核主要信息
			MrsAduitInfoDto mrsAduitInfoDto = new MrsAduitInfoDto();

			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					ConfAuditBusiType.OP_CONFIRM.getValue(), EStartSystemEnum.SYS_CUSTOM.getValue());
			if (mrsConfAuditDto == null) {
				log.info("客户端开户没有配置审核信息！");
				UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.NO_CONFIG_AUDIT_INFO);
				response.setCheckVo(userCheckVO);
				return response;
			}
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short) 0);
			mrsAduitInfoDto.setBusiType(EOperaTypeEnum.OP_CONFIRM);
			mrsAduitInfoDto.setStartSystem(EStartSystemEnum.SYS_CUSTOM);
			mrsAduitInfoDto.setCartNo(personVo.getCredentialsNumber());
			mrsAduitInfoDto.setCartType(personVo.getCredentialsType());
			mrsAduitInfoDto.setName(personVo.getCustomerName());
			// 个人
			mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_00);
			if(org.apache.commons.lang3.StringUtils.isNotBlank(personVo.getCustomerName())
					&& personVo.getCustomerName().length()>10){
				mrsAduitInfoDto.setCreateOperator(personVo.getCustomerName().subSequence(0, 10).toString());
			}else{
				mrsAduitInfoDto.setCreateOperator(personVo.getCustomerName());
			}
			mrsAduitInfoDto.setCreateTime(new Date());
			mrsAduitInfoDto.setProductStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
			mrsAduitInfoDto.setProductAuthStatus(MrsAccountAuthStatus.MAAS_1.getValue());
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAduitInfoDto.setId(UUID.randomUUID().toString());

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
			// 个人客户信息表
			MrsPersonDto mrsPersonDto = new MrsPersonDto();
			BeanUtils.copyProperties(personVo, mrsPersonDto);
			mrsPersonDto.setNationalityCode(MrsNationaltyCode.getEnum(personVo.getNationalityCode()));
			String endDate = mrsPersonDto.getCredentialsEnddate();
			if (StringUtil.isNEmpty(endDate)) {
				mrsPersonDto.setCredentialsEnddate(DateUtil.dateStringToString(endDate));
			}
			String birthDay = mrsPersonDto.getBirthdate();
			if (StringUtil.isNEmpty(birthDay)) {
				mrsPersonDto.setBirthdate(DateUtil.dateStringToString(birthDay));
			}
			// 登录用户表
			List<MrsLoginUserDto> mrsLoginUserDtoList = new ArrayList<MrsLoginUserDto>();
			MrsLoginUserDto userDto = new MrsLoginUserDto();
			userDto.setId(loginUser.getId());
			mrsLoginUserDtoList.add(userDto);
			// JSON
			MrsToJsonDto perMrsToJson = new MrsToJsonDto();
			perMrsToJson.setMrsAccountDto(mrsAccountDto);
			perMrsToJson.setMrsPersonDto(mrsPersonDto);
			perMrsToJson.setMrsAduitAttachmentDtos(personVo.getMrsAduitAttachmentDto());
			perMrsToJson.setMrsSubAccountDtoList(mrsSubAccountDtos);
			perMrsToJson.setAccountSubsPojos(subPojos);
			perMrsToJson.setMrsLoginUserDtoList(mrsLoginUserDtoList);
			// TOTO 创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsons = JSONObject.fromObject(perMrsToJson);// 将java对象转换为json对象
			String str = jsons.toString();
			blobs.setAduitId(mrsAduitInfoDto.getId());
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			// 保持客户端开通一户通信息审核关联表
			MrsPortalAccountAduitDto portalAccountAduitDto = new MrsPortalAccountAduitDto();
			portalAccountAduitDto.setId(UUID.randomUUID().toString());
			portalAccountAduitDto.setBusiSource(EStartSystemEnum.SYS_CUSTOM.getValue());
			portalAccountAduitDto.setAduitId(mrsAduitInfoDto.getId());
			portalAccountAduitDto.setLoginId(personVo.getLoginUserId());
			portalAccountAduitDto.setStatus(EStatusEnum.EFFECTIVE.getValue());
			portalAccountAduitDto.setCreateTime(new Date());
			mrsPortalAccountAduitDtoMapper.insertSelective(portalAccountAduitDto);
			/**
			 * 附件信息上传至服务器，保存附件审核信息
			 */
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
			if (!CollectionUtil.isEmpty(personVo.getMrsAduitAttachmentDto())) {
				for (MrsAduitAttachmentDto att : personVo.getMrsAduitAttachmentDto()) {
					att.setCreateTime(new Date());
					att.setStatus(EStatusEnum.EFFECTIVE.getValue());
					// 发起端
					att.setCatalog(EStartSystemEnum.SYS_CUSTOM.getValue());
					att.setAduitContentId(blobs.getId());
					att.setId(UUID.randomUUID().toString());
					mrsAduitAttachmentDtos.add(att);
				}
				mrsAduitAttachmentDtoMapper.batchInsert(mrsAduitAttachmentDtos);
			}
			UserCheckVO userCheckVO = new UserCheckVO(true);
			response.setCheckVo(userCheckVO);
			return response;
		} catch (Exception e) {
			log.info("开户送审信息失败！", e);
			UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			response.setCheckVo(userCheckVO);
			return response;
		}
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public PersonVO openAndAduitPersonByPortal(PersonVO personVo, MrsLoginUserDto loginUser) {
		PersonVO response = new PersonVO();
		try {
			// 检查三要素,by yuqingjun 20170412 update code 
			List<MrsPersonDto> personList = findActPersonBy3Element(personVo.getCustomerName(),
					personVo.getCredentialsType(), personVo.getCredentialsNumber());
			String checkCustId = "";
			MrsPersonDto newPerson = new MrsPersonDto();
			if (personList != null && personList.size() > 0) {
				for (MrsPersonDto person : personList) {
					// 如果一户通账户为生效的状态，直接返回
					if (MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(person.getAccountStatus())) {
						List<MrsLoginUserDto> loginUserList = creageSearchData(person.getCustId());
						if (loginUserList != null && loginUserList.size() > 0) {
							for (MrsLoginUserDto mloDto : loginUserList) {
								// 如果有手机号，则返回
								if (!StringUtils.isBlank(mloDto.getMobile())) {
									log.info("一户通账户{}和手机号码{}已经存在", person.getCustId(), mloDto.getMobile());
									UserCheckVO userCheckVO = new UserCheckVO(false,
											ErrorMsgEnum.ACCOUNT_PERSON_CHECK_FAIL);
									response.setCheckVo(userCheckVO);
									response.setCustId(person.getCustId());
									response.setMobile(mloDto.getMobile());
									return response;
								}
							}
						}
						log.info("一户通信息已经存在！");
						UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.ACCOUNT_CHECK_FAIL);
						response.setCheckVo(userCheckVO);
						response.setCustId(person.getCustId());
						return response;
					}
					// 如果有多条未生效记录，查询创建时间最新的一个即可
					else {
						if (StringUtils.isBlank(checkCustId)) {
							checkCustId = person.getCustId();
							newPerson = person;
						}
					}
				}
			}
			// 如果存在未生效记录，则检查对应的手机号,处理流程与门户登录查询一致
			if (!StringUtils.isBlank(checkCustId)) {
				List<MrsLoginUserDto> loginUserList = creageSearchData(checkCustId);
				if (loginUserList != null && loginUserList.size() > 0) {
					for (MrsLoginUserDto mloDto : loginUserList) {
						// 如果有手机号，则返回
						if (!StringUtils.isBlank(mloDto.getMobile())) {
							log.info("一户通账户{}和手机号码{}已经存在", checkCustId, mloDto.getMobile());
							UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.ACCOUNT_PERSON_CHECK_FAIL);
							response.setCheckVo(userCheckVO);
							response.setCustId(checkCustId);
							response.setMobile(mloDto.getMobile());
							return response;
						}
					}
				}
				log.info("根据一户通号{}，检查对应的资金账户是否完整，再建立新的用户关联信息", checkCustId);
				// 根据接入平台编号查询平台信息
				MrsPlatformDto newPlatform = this.checkPlatform(MrsPlatformCode.ACCOUNT.getValue());
				// 组装条件
				IndividualRequestVO reqVo = new IndividualRequestVO();
				BeanUtils.copyProperties(personVo, reqVo);
				reqVo.setPlatformCode(MrsPlatformCode.ACCOUNT.getValue());
				if (StringUtil.isNEmpty(personVo.getBirthdate())) {
					reqVo.setBirthdate(DateUtil.dateStringToString(personVo.getBirthdate()));
				}
				MrsAccountDto retrunMrsAccountDto = new MrsAccountDto();
				retrunMrsAccountDto.setCustId(checkCustId);
				retrunMrsAccountDto.setAuthStatus(newPerson.getAuthStatus());
				retrunMrsAccountDto.setId(newPerson.getAccountId());
				retrunMrsAccountDto.setUserInfoId(newPerson.getId());
				// c2、更新返回一户通的个人和账户信息，逻辑判断
				checkActAndPerson(reqVo, newPlatform, retrunMrsAccountDto);
				// c3 根据请求参数中渠道编号查询“子账户配置表”该渠道开通个人客户必须开通的子账户编号，
				// 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户
				log.info("c3   处理子账户配置信息");
				List<MrsConfSubAcctDto> creageSubList = checkAndSaveAct(reqVo, retrunMrsAccountDto, newPlatform);
				// c4 开通资金账户。则将创建资金账户，详见上面a8。账务系统会判断要求开的资金账户是否已经存在，
				// 如果不存在则会新增。
				log.info("c4   开通资金账户");
				createAccounts(retrunMrsAccountDto.getCustId(), MrsCustomerType.MCT_0, reqVo.getCustomerName(),
						creageSubList);
				// c5 删除用户与账户的关系，并新建关联
				List<MrsUserAccountDto> userAccountList = mrsUserAccountDtoMapper.findByIsAccountId(newPerson.getAccountId());
				if(userAccountList!=null && userAccountList.size()>0){
					for(MrsUserAccountDto muaDto: userAccountList){
						//删除登录用户表
						mrsLoginUserDtoMapper.deleteByPrimaryKey(muaDto.getLoginId());
					}
					//删除用户账户关系表
					mrsUserAccountDtoMapper.deleteByAccountId(newPerson.getAccountId());
				}
				log.info("c5 插入账户登陆用户关联信息表");
				// 插入账户登陆用户关联信息表TB_MRS_USER_ACCOUNT
				MrsUserAccountDto mrsUserAccountDto = new MrsUserAccountDto();
				mrsUserAccountDto.setId(UUID.randomUUID().toString());
				mrsUserAccountDto.setCreateTime(new Date());
				mrsUserAccountDto.setIsMain(LoginUserIsMain.IS_MAIN_0.getValue());
				mrsUserAccountDto.setLoginId(personVo.getLoginUserId());
				mrsUserAccountDto.setAccountId(retrunMrsAccountDto.getId());
				mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);
				UserCheckVO userCheckVO = new UserCheckVO(true);
				personVo.setCheckVo(userCheckVO);
				personVo.setCustId(checkCustId);
				return personVo;
			}
			// 子账户
			List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService
					.findBy4Elment(MrsCustomerType.MCT_0, MrsConfSubRelationType.MUST, 
							MrsPlatformCode.ACCOUNT.getValue(),AaccountType.BSYHT.getValue());
			if (mrsConfSubAcctDtos == null || mrsConfSubAcctDtos.isEmpty()) {
				log.info("添加个人用户信息送审时，没有配置子账户设置参数！");
				UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.NO_CONFIG_AUDIT_INFO);
				response.setCheckVo(userCheckVO);
				return response;
			}
			List<String> confSubIds = new ArrayList<String>();
			List<MrsSubAccountDto> mrsSubAccountDtos = new ArrayList<MrsSubAccountDto>();
			for (MrsConfSubAcctDto sub : mrsConfSubAcctDtos) {
				MrsSubAccountDto subAccount = new MrsSubAccountDto();
				subAccount.setSubAccountName(sub.getSubAccountName());
				subAccount.setSubAccountCode(sub.getSubAccountCode());
				subAccount.setSubAccountType(sub.getSubAccountCode());
				subAccount.setOpenTime(new Date());
				subAccount.setAccountOwnType(MrsSubAccountOwnType.MSAOT_01.getValue());
				subAccount.setAccountSource(MrsAccountSource.SOURCE_03.getValue());
				subAccount.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());
				mrsSubAccountDtos.add(subAccount);
				String confSubId = sub.getId();
				confSubIds.add(confSubId);
			}
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

			/** 保存所有数据 */
			// 开立一户通账户cust_id
			String custId = "";
			custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
			custId = MrsConstants.PERSON_ACCOUNT_PREFIX + StringUtils.format(11, custId);

			log.info("保存一户通账户表");
			// 保存一户通账户表
			MrsAccountDto mrsAccountDto = new MrsAccountDto();
			mrsAccountDto.setId(UUID.randomUUID().toString());
			mrsAccountDto.setAccountType(AaccountType.BSYHT);
			mrsAccountDto.setCustomerType(MrsCustomerType.MCT_0.getValue());
			mrsAccountDto.setPlatformCode(MrsPlatformCode.ACCOUNT.getValue());
			mrsAccountDto.setAccountSource(MrsAccountSource.SOURCE_03.getValue());
			mrsAccountDto.setCustId(custId);
			mrsAccountDto.setAccountName(personVo.getCustomerName());
			mrsAccountDto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
			mrsAccountDto.setCreateTime(new Date());
			mrsAccountDto.setOpenTime(new Date());
			mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_1.getValue());
			mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());// 账户状态设置为未生效
			mrsAccountDtoMapper.insertSelective(mrsAccountDto);

			// 开资金账户
			try {
				actBookAppService.createAccounts(custId, personVo.getCustomerName(), subPojos);
			} catch (Exception e) {
				log.error("调用actBookAppService.createAccounts开资金账户失败",e);
				throw new CodeCheckedException("调用actBookAppService.createAccounts开资金账户失败");
			}
			log.info("保存个人客户信息表");
			// 保存个人客户信息表
			MrsPersonDto mrsPersonDto = new MrsPersonDto();
			BeanUtils.copyProperties(personVo, mrsPersonDto);
			MrsNationaltyCode nationaltyCode = StringUtil.isEmpty(personVo.getNationalityCode()) ? null
					: MrsNationaltyCode.getEnum(personVo.getNationalityCode());
			if (nationaltyCode != null) {
				mrsPersonDto.setNationalityCode(nationaltyCode);// 国籍码
			}
			String endDate = mrsPersonDto.getCredentialsEnddate();
			if (StringUtil.isNEmpty(endDate)) {
				mrsPersonDto.setCredentialsEnddate(DateUtil.dateStringToString(endDate));
			}
			String birthDay = mrsPersonDto.getBirthdate();
			if (StringUtil.isNEmpty(birthDay)) {
				mrsPersonDto.setBirthdate(DateUtil.dateStringToString(birthDay));
			}
			//职业一级编码
			mrsPersonDto.setOneOccupation(personVo.getProfessionalCode());
			mrsPersonDto.setId(UUID.randomUUID().toString());
			mrsPersonDto.setCustId(custId);
			mrsPersonDto.setCreateTime(new Date());
			mrsPersonDtoMapper.insertSelective(mrsPersonDto);

			log.info("保存子账户信息表");
			// 保存子账户信息表
			if (mrsSubAccountDtos != null && mrsSubAccountDtos.size() > 0) {
				for (MrsSubAccountDto subAccountDto : mrsSubAccountDtos) {
					subAccountDto.setId(UUID.randomUUID().toString());
					subAccountDto.setCustId(custId);
					if (subAccountDto.getPlatformCode() == null || subAccountDto.getPlatformCode().trim().equals("")) {
						subAccountDto.setPlatformCode(MrsPlatformCode.ACCOUNT.getValue());
					}
					String subInsure = subAccountDto.getSubAccountCode() + "0" + custId;
					subAccountDto.setSubAccountCode(subInsure); // 子账户编号
					subAccountDto.setCreateTime(new Date());
					subAccountDto.setSubAccountName(subAccountDto.getSubAccountName());
					
					subAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());// 子账户状态设置为有效
					mrsSubAccountDtoMapper.insertSelective(subAccountDto);
				}
			}

			MrsUserAccountDto mrsUserAccountDto = new MrsUserAccountDto();
			log.info("插入账户登陆用户关联信息表");
			// 插入账户登陆用户关联信息表TB_MRS_USER_ACCOUNT
			mrsUserAccountDto.setId(UUID.randomUUID().toString());
			mrsUserAccountDto.setCreateTime(new Date());
			mrsUserAccountDto.setIsMain(LoginUserIsMain.IS_MAIN_0.getValue());
			mrsUserAccountDto.setLoginId(personVo.getLoginUserId());
			mrsUserAccountDto.setAccountId(mrsAccountDto.getId());
			mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);

			// 审核主要信息
			MrsAduitInfoDto mrsAduitInfoDto = new MrsAduitInfoDto();

			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					ConfAuditBusiType.OP_CONFIRM.getValue(), EStartSystemEnum.SYS_CUSTOM.getValue());
			if (mrsConfAuditDto == null) {
				log.info("客户端开户没有配置审核信息！");
				UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.NO_CONFIG_AUDIT_INFO);
				response.setCheckVo(userCheckVO);
				return response;
			}
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short) 0);
			mrsAduitInfoDto.setBusiType(EOperaTypeEnum.OP_CONFIRM);
			mrsAduitInfoDto.setStartSystem(EStartSystemEnum.SYS_CUSTOM);
			mrsAduitInfoDto.setCartNo(personVo.getCredentialsNumber());
			mrsAduitInfoDto.setCartType(personVo.getCredentialsType());
			mrsAduitInfoDto.setName(personVo.getCustomerName());
			// 个人
			mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_00);
			if(org.apache.commons.lang3.StringUtils.isNotBlank(personVo.getCustomerName())
					&& personVo.getCustomerName().length()>10){
				mrsAduitInfoDto.setCreateOperator(personVo.getCustomerName().subSequence(0, 10).toString());
			}else{
				mrsAduitInfoDto.setCreateOperator(personVo.getCustomerName());
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
			userDto.setId(loginUser.getId());
			mrsLoginUserDtoList.add(userDto);
			// JSON
			MrsToJsonDto perMrsToJson = new MrsToJsonDto();
			perMrsToJson.setMrsAccountDto(mrsAccountDto);
			perMrsToJson.setMrsPersonDto(mrsPersonDto);
			perMrsToJson.setMrsAduitAttachmentDtos(personVo.getMrsAduitAttachmentDto());
			perMrsToJson.setMrsSubAccountDtoList(mrsSubAccountDtos);
			perMrsToJson.setAccountSubsPojos(subPojos);
			perMrsToJson.setMrsLoginUserDtoList(mrsLoginUserDtoList);
			perMrsToJson.setOpenUser(true);// 门户直接开户处理，与被动开户审核一致
			// 创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsons = JSONObject.fromObject(perMrsToJson);// 将java对象转换为json对象
			String str = jsons.toString();
			blobs.setAduitId(mrsAduitInfoDto.getId());
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			// 保持客户端开通一户通信息审核关联表
			MrsPortalAccountAduitDto portalAccountAduitDto = new MrsPortalAccountAduitDto();
			portalAccountAduitDto.setId(UUID.randomUUID().toString());
			portalAccountAduitDto.setBusiSource(EStartSystemEnum.SYS_CUSTOM.getValue());
			portalAccountAduitDto.setAduitId(mrsAduitInfoDto.getId());
			portalAccountAduitDto.setLoginId(personVo.getLoginUserId());
			portalAccountAduitDto.setStatus(EStatusEnum.EFFECTIVE.getValue());
			portalAccountAduitDto.setCreateTime(new Date());
			mrsPortalAccountAduitDtoMapper.insertSelective(portalAccountAduitDto);
			/**
			 * 附件信息上传至服务器，保存附件审核信息
			 */
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
			if (!CollectionUtil.isEmpty(personVo.getMrsAduitAttachmentDto())) {
				for (MrsAduitAttachmentDto att : personVo.getMrsAduitAttachmentDto()) {
					att.setCreateTime(new Date());
					att.setStatus(EStatusEnum.EFFECTIVE.getValue());
					att.setCatalog(EStartSystemEnum.SYS_CUSTOM.getValue());
					att.setAduitContentId(blobs.getId());
					att.setId(UUID.randomUUID().toString());
					mrsAduitAttachmentDtos.add(att);
				}
				mrsAduitAttachmentDtoMapper.batchInsert(mrsAduitAttachmentDtos);
			}
			UserCheckVO userCheckVO = new UserCheckVO(true);
			response.setCheckVo(userCheckVO);
			return response;
		} catch (Exception e) {
			log.error("开户送审信息失败！", e);
			UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			response.setCheckVo(userCheckVO);
			return response;
		}
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public PersonVO saveAduitPersonByPortalRest(PersonVO personVo, MrsLoginUserDto loginUser) {
		PersonVO response = new PersonVO();
		try {
			// 根据客户号获取个人客户信息
			MrsPersonDto person = mrsPersonDtoMapper.findByCustId(personVo.getCustId());
			if (person == null) {
				log.error("客户信息不存在！");
				UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.CUSTOMER_SEAARCH_FAIL);
				response.setCheckVo(userCheckVO);
				return response;
			}
			// 子账户
			List<MrsSubAccountDto> confSubIds = mrsSubAccountDtoMapper.findByCustId(person.getCustId());
			// 资金账户
			List<ActAccountDto> actList = actAccountDtoMapper.getUserAccounts(person.getCustId());
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
				return response;
			}
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short) 0);
			mrsAduitInfoDto.setBusiType(EOperaTypeEnum.OP_CONFIRM);
			mrsAduitInfoDto.setStartSystem(EStartSystemEnum.SYS_CUSTOM);
			mrsAduitInfoDto.setCartNo(personVo.getCredentialsNumber());
			mrsAduitInfoDto.setCartType(personVo.getCredentialsType());
			mrsAduitInfoDto.setName(personVo.getCustomerName());
			// 个人
			mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_00);
			if(org.apache.commons.lang3.StringUtils.isNotBlank(personVo.getCustomerName())
					&& personVo.getCustomerName().length()>10){
				mrsAduitInfoDto.setCreateOperator(personVo.getCustomerName().subSequence(0, 10).toString());
			}else{
				mrsAduitInfoDto.setCreateOperator(personVo.getCustomerName());
			}
			mrsAduitInfoDto.setCreateTime(new Date());
			mrsAduitInfoDto.setProductStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
			mrsAduitInfoDto.setProductAuthStatus(MrsAccountAuthStatus.MAAS_1.getValue());
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAduitInfoDto.setId(UUID.randomUUID().toString());
			mrsAduitInfoDto.setCustId(person.getCustId());
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
			userDto.setId(loginUser.getId());
			mrsLoginUserDtoList.add(userDto);
			// 一户通账户表
			MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(personVo.getCustId());
			
			// 更改一户通账户 认证状态 
			// 未认证、认证失败 可上传资料 重新认证  状态  改为认证中  等待审核
			if(null!=mrsAccountDto && (MrsAccountAuthStatus.MAAS_0.getValue().equals(mrsAccountDto.getAuthStatus()) 
					|| MrsAccountAuthStatus.MAAS_3.getValue().equals(mrsAccountDto.getAuthStatus()))){
				mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_1.getValue());
				mrsAccountService.updateByPrimaryKey(mrsAccountDto);
			}
						
			// JSON
			MrsToJsonDto perMrsToJson = new MrsToJsonDto();
			perMrsToJson.setMrsAccountDto(mrsAccountDto);
			perMrsToJson.setMrsPersonDto(person);
			perMrsToJson.setMrsAduitAttachmentDtos(personVo.getMrsAduitAttachmentDto());
			perMrsToJson.setMrsSubAccountDtoList(confSubIds);
			perMrsToJson.setAccountSubsPojos(subPojos);
			perMrsToJson.setMrsLoginUserDtoList(mrsLoginUserDtoList);
			perMrsToJson.setOpenUser(true);
			// 创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsons = JSONObject.fromObject(perMrsToJson);// 将java对象转换为json对象
			String str = jsons.toString();
			log.debug("门户提交的个人被动开户送审信息:" + str);
			blobs.setAduitId(mrsAduitInfoDto.getId());
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			// 保持客户端开通一户通信息审核关联表
			MrsPortalAccountAduitDto portalAccountAduitDto = new MrsPortalAccountAduitDto();
			portalAccountAduitDto.setId(UUID.randomUUID().toString());
			portalAccountAduitDto.setBusiSource(EStartSystemEnum.SYS_CUSTOM.getValue());
			portalAccountAduitDto.setAduitId(mrsAduitInfoDto.getId());
			portalAccountAduitDto.setLoginId(personVo.getLoginUserId());
			portalAccountAduitDto.setStatus(EStatusEnum.EFFECTIVE.getValue());
			portalAccountAduitDto.setCreateTime(new Date());
			mrsPortalAccountAduitDtoMapper.insertSelective(portalAccountAduitDto);
			/**
			 * 附件信息上传至服务器，保存附件审核信息
			 */
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
			if (!CollectionUtil.isEmpty(personVo.getMrsAduitAttachmentDto())) {
				for (MrsAduitAttachmentDto att : personVo.getMrsAduitAttachmentDto()) {
					att.setCreateTime(new Date());
					att.setStatus(EStatusEnum.EFFECTIVE.getValue());
					// 发起端
					att.setCatalog(EStartSystemEnum.SYS_CUSTOM.getValue());
					att.setAduitContentId(blobs.getId());
					att.setId(UUID.randomUUID().toString());
					mrsAduitAttachmentDtos.add(att);
				}
				mrsAduitAttachmentDtoMapper.batchInsert(mrsAduitAttachmentDtos);
			}
			
			UserCheckVO userCheckVO = new UserCheckVO(true);
			response.setCheckVo(userCheckVO);
			return response;
		} catch (Exception e) {
			log.info("门户提交的个人被动开户送审信息失败！", e);
			UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			response.setCheckVo(userCheckVO);
			return response;
		}
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public PersonVO openAccountByPortal(PersonVO personVo, BankBusiReqVO bankBusiReqVO) {
		log.info("门户开户处理开始openAccountByPortal");
		PersonVO response = new PersonVO();
		try {
			// 检查三要素
			List<MrsPersonDto> personList = findBy3Element(personVo.getCustomerName(), personVo.getCredentialsType(),
					personVo.getCredentialsNumber());
			if (personList != null&& personList.size()>0) {
				for(MrsPersonDto person: personList){
					// 如果一户通账户不为为生效的状态，直接返回
					if (!validateMrsAccountStatus(person.getCustId())) {
						log.info("一户通信息已经存在！");
						UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.ACCOUNT_CHECK_FAIL);
						response.setCheckVo(userCheckVO);
						return response;
					}
				}
			}
			// 子账户
			List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService
					.findBy4Elment(MrsCustomerType.MCT_0, MrsConfSubRelationType.MUST, 
							MrsPlatformCode.ACCOUNT.getValue(),AaccountType.BSYHT.getValue());
			if (mrsConfSubAcctDtos == null || mrsConfSubAcctDtos.isEmpty()) {
				log.info("添加个人用户信息送审时，没有配置子账户设置参数！");
				UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.NO_CONFIG_AUDIT_INFO);
				response.setCheckVo(userCheckVO);
				return response;
			}
			List<String> confSubIds = new ArrayList<String>();
			List<MrsSubAccountDto> mrsSubAccountDtos = new ArrayList<MrsSubAccountDto>();
			for (MrsConfSubAcctDto sub : mrsConfSubAcctDtos) {
				MrsSubAccountDto subAccount = new MrsSubAccountDto();
				subAccount.setSubAccountName(sub.getSubAccountName());
				subAccount.setSubAccountCode(sub.getSubAccountCode());
				subAccount.setSubAccountType(sub.getSubAccountCode());
				subAccount.setOpenTime(new Date());
				subAccount.setAccountOwnType(MrsSubAccountOwnType.MSAOT_01.getValue());
				subAccount.setAccountSource(MrsAccountSource.SOURCE_03.getValue());
				subAccount.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());
				mrsSubAccountDtos.add(subAccount);
				String confSubId = sub.getId();
				confSubIds.add(confSubId);
			}
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

			/** 保存所有数据 */
			// 开立一户通账户cust_id
			String custId = "";
			custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
			custId = MrsConstants.PERSON_ACCOUNT_PREFIX + StringUtils.format(11, custId);

			log.info("保存一户通账户表");
			// 保存一户通账户表
			MrsAccountDto mrsAccountDto = new MrsAccountDto();
			mrsAccountDto.setId(UUID.randomUUID().toString());
			mrsAccountDto.setAccountType(AaccountType.BSYHT);
			mrsAccountDto.setCustomerType(MrsCustomerType.MCT_0.getValue());
			mrsAccountDto.setPlatformCode(MrsPlatformCode.ACCOUNT.getValue());
			mrsAccountDto.setAccountSource(MrsAccountSource.SOURCE_03.getValue());
			mrsAccountDto.setCustId(custId);
			mrsAccountDto.setAccountName(personVo.getCustomerName());
			mrsAccountDto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
			mrsAccountDto.setCreateTime(new Date());
			mrsAccountDto.setOpenTime(new Date());
			if (EYesNo.NO.getValue().equals(bankBusiReqVO.getIsPageCheck())) {
				mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
				mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());// 账户状态设置为未生效
			} else {
				mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
				mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());// 账户状态设置为正常
			}

			mrsAccountDtoMapper.insertSelective(mrsAccountDto);

			// 开资金账户
			try {
				actBookAppService.createAccounts(custId, personVo.getCustomerName(), subPojos);
			} catch (Exception e) {
				log.error("调用actBookAppService.createAccounts开资金账户失败");
				throw new CodeCheckedException("调用actBookAppService.createAccounts开资金账户失败");
			}
			log.info("保存个人客户信息表");
			// 保存个人客户信息表
			MrsPersonDto mrsPersonDto = new MrsPersonDto();
			BeanUtils.copyProperties(personVo, mrsPersonDto);
			String endDate = mrsPersonDto.getCredentialsEnddate();
			if (StringUtil.isNEmpty(endDate)) {
				mrsPersonDto.setCredentialsEnddate(DateUtil.dateStringToString(endDate));
			}
			String birthDay = mrsPersonDto.getBirthdate();
			if (StringUtil.isNEmpty(birthDay)) {
				mrsPersonDto.setBirthdate(DateUtil.dateStringToString(birthDay));
			}
			//职业一级编码
			mrsPersonDto.setOneOccupation(personVo.getProfessionalCode());
			mrsPersonDto.setId(UUID.randomUUID().toString());
			mrsPersonDto.setCustId(custId);
			mrsPersonDto.setCreateTime(new Date());
			mrsPersonDtoMapper.insertSelective(mrsPersonDto);

			log.info("保存子账户信息表");
			// 保存子账户信息表
			if (mrsSubAccountDtos != null && mrsSubAccountDtos.size() > 0) {
				for (MrsSubAccountDto subAccountDto : mrsSubAccountDtos) {
					subAccountDto.setId(UUID.randomUUID().toString());
					subAccountDto.setCustId(custId);
					if (subAccountDto.getPlatformCode() == null || subAccountDto.getPlatformCode().trim().equals("")) {
						subAccountDto.setPlatformCode(MrsPlatformCode.ACCOUNT.getValue());
					}
					String subInsure = subAccountDto.getSubAccountCode() + "0" + custId;
					subAccountDto.setSubAccountCode(subInsure); // 子账户编号
					subAccountDto.setCreateTime(new Date());
					subAccountDto.setSubAccountName(subAccountDto.getSubAccountName());
					
					subAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());// 子账户状态设置为有效
					mrsSubAccountDtoMapper.insertSelective(subAccountDto);
				}
			}

			MrsUserAccountDto mrsUserAccountDto = new MrsUserAccountDto();
			log.info("插入账户登陆用户关联信息表");
			// 插入账户登陆用户关联信息表TB_MRS_USER_ACCOUNT
			mrsUserAccountDto.setId(UUID.randomUUID().toString());
			mrsUserAccountDto.setCreateTime(new Date());
			mrsUserAccountDto.setIsMain(LoginUserIsMain.IS_MAIN_0.getValue());
			mrsUserAccountDto.setLoginId(personVo.getLoginUserId());
			mrsUserAccountDto.setAccountId(mrsAccountDto.getId());
			mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);
			
			UserCheckVO userCheckVO = new UserCheckVO(true);
			response.setCheckVo(userCheckVO);
			response.setCustId(custId);
			return response;
		} catch (Exception e) {
			log.error("开通一户通账户和资金账户失败！",e);
			UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			response.setCheckVo(userCheckVO);
			return response;
		}

	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public PersonVO directlyOpenAccount(PersonVO personVo) {
		log.info("门户开户处理开始directlyOpenAccount");
		PersonVO response = new PersonVO();
		try {
			// 检查三要素,by yuqingjun 20170412 update code 
			List<MrsPersonDto> personList = findActPersonBy3Element(personVo.getCustomerName(),
					personVo.getCredentialsType(), personVo.getCredentialsNumber());
			String checkCustId = "";
			MrsPersonDto newPerson = new MrsPersonDto();
			if (personList != null && personList.size() > 0) {
				for (MrsPersonDto person : personList) {
					// 如果一户通账户为生效的状态，直接返回
					if (MrsAccountStatus.ACCOUNT_STATUS_0.getValue().equals(person.getAccountStatus())) {
						List<MrsLoginUserDto> loginUserList = creageSearchData(person.getCustId());
						if (loginUserList != null && loginUserList.size() > 0) {
							for (MrsLoginUserDto mloDto : loginUserList) {
								// 如果有手机号，则返回
								if (!StringUtils.isBlank(mloDto.getMobile())) {
									log.info("一户通账户{}和手机号码{}已经存在", person.getCustId(), mloDto.getMobile());
									UserCheckVO userCheckVO = new UserCheckVO(false,
											ErrorMsgEnum.ACCOUNT_PERSON_CHECK_FAIL);
									response.setCheckVo(userCheckVO);
									response.setCustId(person.getCustId());
									response.setMobile(mloDto.getMobile());
									return response;
								}
							}
						}
						log.info("一户通信息已经存在！");
						UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.ACCOUNT_CHECK_FAIL);
						response.setCheckVo(userCheckVO);
						response.setCustId(person.getCustId());
						return response;
					}
					// 如果有多条未生效记录，查询创建时间最新的一个即可
					else {
						if (StringUtils.isBlank(checkCustId)) {
							checkCustId = person.getCustId();
							newPerson = person;
						}
					}
				}
			}
			// 如果存在未生效记录，则检查对应的手机号,处理流程与门户登录查询一致
			if (!StringUtils.isBlank(checkCustId)) {
				List<MrsLoginUserDto> loginUserList = creageSearchData(checkCustId);
				if (loginUserList != null && loginUserList.size() > 0) {
					for (MrsLoginUserDto mloDto : loginUserList) {
						// 如果有手机号，则返回
						if (!StringUtils.isBlank(mloDto.getMobile())) {
							log.info("一户通账户{}和手机号码{}已经存在", checkCustId, mloDto.getMobile());
							UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.ACCOUNT_PERSON_CHECK_FAIL);
							response.setCheckVo(userCheckVO);
							response.setCustId(checkCustId);
							response.setMobile(mloDto.getMobile());
							return response;
						}
					}
				}
				log.info("根据一户通号{}，检查对应的资金账户是否完整，再建立新的用户关联信息", checkCustId);
				// 根据接入平台编号查询平台信息
				MrsPlatformDto newPlatform = this.checkPlatform(MrsPlatformCode.ACCOUNT.getValue());
				// 组装条件
				IndividualRequestVO reqVo = new IndividualRequestVO();
				BeanUtils.copyProperties(personVo, reqVo);
				reqVo.setAccountType(AaccountType.BSYHT.getValue());
				reqVo.setPlatformCode(MrsPlatformCode.ACCOUNT.getValue());
				if (StringUtil.isNEmpty(personVo.getBirthdate())) {
					reqVo.setBirthdate(DateUtil.dateStringToString(personVo.getBirthdate()));
				}
				MrsAccountDto retrunMrsAccountDto = new MrsAccountDto();
				retrunMrsAccountDto.setCustId(checkCustId);
				retrunMrsAccountDto.setAuthStatus(newPerson.getAuthStatus());
				retrunMrsAccountDto.setId(newPerson.getAccountId());
				retrunMrsAccountDto.setUserInfoId(newPerson.getId());
				// c2、更新返回一户通的个人和账户信息，逻辑判断
				checkActAndPerson(reqVo, newPlatform, retrunMrsAccountDto);
				// c3 根据请求参数中渠道编号查询“子账户配置表”该渠道开通个人客户必须开通的子账户编号，
				// 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户
				log.info("c3   处理子账户配置信息");
				List<MrsConfSubAcctDto> creageSubList = checkAndSaveAct(reqVo, retrunMrsAccountDto, newPlatform);
				// c4 开通资金账户。则将创建资金账户，详见上面a8。账务系统会判断要求开的资金账户是否已经存在，
				// 如果不存在则会新增。
				log.info("c4   开通资金账户");
				createAccounts(retrunMrsAccountDto.getCustId(), MrsCustomerType.MCT_0, reqVo.getCustomerName(),
						creageSubList);
				// c5 删除用户与账户的关系，并新建关联
				List<MrsUserAccountDto> userAccountList = mrsUserAccountDtoMapper.findByIsAccountId(newPerson.getAccountId());
				if(userAccountList!=null && userAccountList.size()>0){
					for(MrsUserAccountDto muaDto: userAccountList){
						//删除登录用户表
						mrsLoginUserDtoMapper.deleteByPrimaryKey(muaDto.getLoginId());
					}
					//删除用户账户关系表
					mrsUserAccountDtoMapper.deleteByAccountId(newPerson.getAccountId());
				}
				log.info("c5 插入账户登陆用户关联信息表");
				// 插入账户登陆用户关联信息表TB_MRS_USER_ACCOUNT
				MrsUserAccountDto mrsUserAccountDto = new MrsUserAccountDto();
				mrsUserAccountDto.setId(UUID.randomUUID().toString());
				mrsUserAccountDto.setCreateTime(new Date());
				mrsUserAccountDto.setIsMain(LoginUserIsMain.IS_MAIN_0.getValue());
				mrsUserAccountDto.setLoginId(personVo.getLoginUserId());
				mrsUserAccountDto.setAccountId(retrunMrsAccountDto.getId());
				mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);
				UserCheckVO userCheckVO = new UserCheckVO(true);
				personVo.setCheckVo(userCheckVO);
				personVo.setCustId(checkCustId);
				return personVo;
			}
			// 子账户
			List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService
					.findBy4Elment(MrsCustomerType.MCT_0, MrsConfSubRelationType.MUST, 
							MrsPlatformCode.ACCOUNT.getValue(),AaccountType.BSYHT.getValue());
			if (mrsConfSubAcctDtos == null || mrsConfSubAcctDtos.isEmpty()) {
				log.info("添加个人用户信息送审时，没有配置子账户设置参数！");
				UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.NO_CONFIG_AUDIT_INFO);
				response.setCheckVo(userCheckVO);
				return response;
			}
			List<String> confSubIds = new ArrayList<String>();
			List<MrsSubAccountDto> mrsSubAccountDtos = new ArrayList<MrsSubAccountDto>();
			for (MrsConfSubAcctDto sub : mrsConfSubAcctDtos) {
				MrsSubAccountDto subAccount = new MrsSubAccountDto();
				subAccount.setSubAccountName(sub.getSubAccountName());
				subAccount.setSubAccountCode(sub.getSubAccountCode());
				subAccount.setSubAccountType(sub.getSubAccountCode());
				subAccount.setOpenTime(new Date());
				subAccount.setAccountOwnType(MrsSubAccountOwnType.MSAOT_01.getValue());
				subAccount.setAccountSource(MrsAccountSource.SOURCE_03.getValue());
				subAccount.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());
				mrsSubAccountDtos.add(subAccount);
				String confSubId = sub.getId();
				confSubIds.add(confSubId);
			}
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

			/** 保存所有数据 */
			// 开立一户通账户cust_id
			String custId = "";
			custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
			custId = MrsConstants.PERSON_ACCOUNT_PREFIX + StringUtils.format(11, custId);

			log.info("保存一户通账户表");
			// 保存一户通账户表
			MrsAccountDto mrsAccountDto = new MrsAccountDto();
			mrsAccountDto.setId(UUID.randomUUID().toString());
			mrsAccountDto.setAccountType(AaccountType.BSYHT);
			mrsAccountDto.setCustomerType(MrsCustomerType.MCT_0.getValue());
			mrsAccountDto.setPlatformCode(MrsPlatformCode.ACCOUNT.getValue());
			mrsAccountDto.setAccountSource(MrsAccountSource.SOURCE_03.getValue());
			mrsAccountDto.setCustId(custId);
			mrsAccountDto.setAccountName(personVo.getCustomerName());
			mrsAccountDto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
			mrsAccountDto.setCreateTime(new Date());
			mrsAccountDto.setOpenTime(new Date());
			mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
			mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());// 账户状态设置为未生效
			mrsAccountDtoMapper.insertSelective(mrsAccountDto);

			// 开资金账户
			try {
				actBookAppService.createAccounts(custId, personVo.getCustomerName(), subPojos);
			} catch (Exception e) {
				log.error("调用actBookAppService.createAccounts开资金账户失败",e);
				throw new CodeCheckedException("调用actBookAppService.createAccounts开资金账户失败");
			}
			log.info("保存个人客户信息表");
			// 保存个人客户信息表
			MrsPersonDto mrsPersonDto = new MrsPersonDto();
			BeanUtils.copyProperties(personVo, mrsPersonDto);
			MrsNationaltyCode nationaltyCode = StringUtil.isEmpty(personVo.getNationalityCode())? null  : MrsNationaltyCode.getEnum(personVo.getNationalityCode());
			if(nationaltyCode != null){
				mrsPersonDto.setNationalityCode(nationaltyCode);//国籍码
			}
			String endDate = mrsPersonDto.getCredentialsEnddate();
			if (StringUtil.isNEmpty(endDate)) {
				mrsPersonDto.setCredentialsEnddate(DateUtil.dateStringToString(endDate));
			}
			String birthDay = mrsPersonDto.getBirthdate();
			if (StringUtil.isNEmpty(birthDay)) {
				mrsPersonDto.setBirthdate(DateUtil.dateStringToString(birthDay));
			}
			//职业一级编码
			mrsPersonDto.setOneOccupation(personVo.getProfessionalCode());
			mrsPersonDto.setId(UUID.randomUUID().toString());
			mrsPersonDto.setCustId(custId);
			mrsPersonDto.setCreateTime(new Date());
			mrsPersonDtoMapper.insertSelective(mrsPersonDto);

			log.info("保存子账户信息表");
			// 保存子账户信息表
			if (mrsSubAccountDtos != null && mrsSubAccountDtos.size() > 0) {
				for (MrsSubAccountDto subAccountDto : mrsSubAccountDtos) {
					subAccountDto.setId(UUID.randomUUID().toString());
					subAccountDto.setCustId(custId);
					if (subAccountDto.getPlatformCode() == null || subAccountDto.getPlatformCode().trim().equals("")) {
						subAccountDto.setPlatformCode(MrsPlatformCode.ACCOUNT.getValue());
					}
					String subInsure = subAccountDto.getSubAccountCode() + "0" + custId;
					subAccountDto.setSubAccountCode(subInsure); // 子账户编号
					subAccountDto.setCreateTime(new Date());
					subAccountDto.setSubAccountName(subAccountDto.getSubAccountName());
					
					subAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());// 子账户状态设置为有效
					mrsSubAccountDtoMapper.insertSelective(subAccountDto);
				}
			}

			MrsUserAccountDto mrsUserAccountDto = new MrsUserAccountDto();
			log.info("插入账户登陆用户关联信息表");
			// 插入账户登陆用户关联信息表TB_MRS_USER_ACCOUNT
			mrsUserAccountDto.setId(UUID.randomUUID().toString());
			mrsUserAccountDto.setCreateTime(new Date());
			mrsUserAccountDto.setIsMain(LoginUserIsMain.IS_MAIN_0.getValue());
			mrsUserAccountDto.setLoginId(personVo.getLoginUserId());
			mrsUserAccountDto.setAccountId(mrsAccountDto.getId());
			mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);
			
			UserCheckVO userCheckVO = new UserCheckVO(true);
			personVo.setCheckVo(userCheckVO);
			personVo.setCustId(custId);
			return personVo;
		} catch (Exception e) {
			log.error("开通一户通账户和资金账户失败！",e);
			UserCheckVO userCheckVO = new UserCheckVO(false, ErrorMsgEnum.SYSTEM_ERROR);
			response.setCheckVo(userCheckVO);
			return response;
		}

	}
	private List<MrsLoginUserDto> creageSearchData(String checkCustId) {
		MrsLoginUserDto dto = new MrsLoginUserDto();
		dto.setAlias(checkCustId);
		dto.setEmail(checkCustId);
		dto.setMobile(checkCustId);
		dto.setCustId(checkCustId);
		dto.setCustomerType(MrsCustomerType.MCT_0.getValue());
		dto.setRegisterType(MrsCustType.MRS_CUST_TYPE_01.getValue());
		List<MrsLoginUserDto> loginUserList = mrsLoginUserDtoMapper.findByLoginMsg(dto);
		return loginUserList;
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitPersonResponseVo doUpdateSaveAduit(MrsPersonVo personVo) {
		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		try {
			// 个人用户主键
			String id = personVo.getMrsPersonDto().getId();

			MrsPersonDto personDto = mrsPersonDtoMapper.selectByPrimaryKey(id);
			if (personDto == null || StringUtils.isEmpty(personDto.getCustId())) {
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("数据有误，一户通号码不存在！");
				return response;
			}
			// 一户通状态为注销 不让修改
			MrsAccountDto oldAccount = mrsAccountDtoMapper.findByCustId(personDto.getCustId());
			if (oldAccount == null) {
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("数据有误，一户通信息不存在！");
				return response;
			}

			if (MrsAccountStatus.ACCOUNT_STATUS_2.getValue().equals(oldAccount.getAccountStatus())) {
				response.setIsSucess(false);
				response.setMsgCode("");
				response.setMsgInfo("一户通状态为注销不可以修改！");
				return response;
			}
			// 校验如果三要素有修改，校验修改的三要素是否存在未生效、正常、异常的一户通账户，如果存在，系统终止流程
			response = check3ElementEdit(personDto, personVo.getMrsPersonDto());
			if (!response.getIsSucess()) {
				return response;
			}

			// 查询联系人信息，附件信息
			// 联系人信息
			List<MrsContactListDto> mrsContactListDtos = mrsContactListService.findByCustId(personDto.getCustId());
			// 查询附件信息并且归类处理
			List<MrsCertFileDto> mrsCertFileDtos = mrsCertFileService.findByCustId(personDto.getCustId());
			/**
			 * 设置审核相关信息 1,设置审核人 2，设置审核主要信息 3，根据选择配置子账户信息 4，设置审核内容信息 5，上传附件到文件服务器
			 */
			// 1设置审核人，查询基础配置设置个人开户审核配置人数
			// 审核主要信息
			String aduitId = mrsAduitInfoService.craateMrsAduitInfo(personVo.getCreateRemark(),
					personDto.getCredentialsNumber(), personDto.getCredentialsType(), personDto.getCustomerName(), personDto.getCustId(),
					oldAccount.getAccountStatus(), oldAccount.getAuthStatus(), EStartSystemEnum.SYS_COUNTER,
					personVo.getCurrentUser().getLoginName(), EOperaTypeEnum.OP_UPADTE,
					MrsCustTypeEnum.MRS_CUST_TYPE_00);

			// 原值
			MrsToJsonDto perMrsToJsonOld = new MrsToJsonDto();
			perMrsToJsonOld.setMrsAccountDto(oldAccount);
			perMrsToJsonOld.setMrsContactListDtos(mrsContactListDtos);
			perMrsToJsonOld.setMrsPersonDto(personDto);
			perMrsToJsonOld.setMrsCertFileDtos(mrsCertFileDtos);

			MrsPersonDto newPerson = new MrsPersonDto();
			BeanUtils.copyProperties(personDto, newPerson);
			
			MrsPersonDto person = personVo.getMrsPersonDto();
			// 查询个人信息 设置修改的个人信息
			newPerson.setCustomerName(person.getCustomerName());
			newPerson.setNationalityCode(person.getNationalityCode());
			newPerson.setCredentialsType(person.getCredentialsType());
			newPerson.setCredentialsNumber(person.getCredentialsNumber());
			newPerson.setCredentialsEnddate(person.getCredentialsEnddate());
			newPerson.setBirthdate(person.getBirthdate());
			newPerson.setSexCode(person.getSexCode());
			newPerson.setNationalCode(person.getNationalCode());
			newPerson.setEducationCode(person.getEducationCode());
			// 职业代码未加
			newPerson.setOneOccupation(person.getOneOccupation());
			newPerson.setTwoOccupation(person.getTwoOccupation());
			newPerson.setMobile(person.getMobile());
			newPerson.setEmail(person.getEmail());
			newPerson.setContactAddr(person.getContactAddr());
			newPerson.setZipCode(person.getZipCode());
			// 新值
			MrsToJsonDto perMrsToJsonNew = new MrsToJsonDto();
			
			//一户通修改
			MrsAccountDto newAccount = new MrsAccountDto();
			BeanUtils.copyProperties(oldAccount, newAccount);
//			newAccount.setAccountType(personVo.getMrsAccountDto().getAccountType());
			newAccount.setAccountName(newPerson.getCustomerName());
			perMrsToJsonNew.setMrsAccountDto(newAccount);
			perMrsToJsonNew.setMrsContactListDtos(personVo.getMrsContactListDtos());
			perMrsToJsonNew.setMrsPersonDto(newPerson);
			perMrsToJsonNew.setMrsAduitAttachmentDtos(personVo.getMrsAduitAttachmentDtos());

			// 创建审核内容信息
			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			JSONObject jsonsNew = JSONObject.fromObject(perMrsToJsonNew);// 将java对象转换为json对象
			String strNew = jsonsNew.toString();

			JSONObject jsonsOld = JSONObject.fromObject(perMrsToJsonOld);// 将java对象转换为json对象
			String strOld = jsonsOld.toString();

			blobs.setAduitId(aduitId);
			blobs.setOldValue(strOld);
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(strNew);

			// 创建审核主要信息

			mrsAduitContentDtoMapper.insertSelective(blobs);

			/**
			 * 附件信息上传至服务器，保存附件审核信息
			 */
			if (!CollectionUtil.isEmpty(personVo.getMrsAduitAttachmentDtos())) {
				List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
				for (MrsAduitAttachmentDto att : personVo.getMrsAduitAttachmentDtos()) {
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
			if (e instanceof CbsUncheckedException) {
				throw new CbsUncheckedException(ECbsErrorCode.getByDisplay(e.getMessage()));
			}
			throw new CbsUncheckedException(ECbsErrorCode.PERSON_OPEN_0102);
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
	public SaveAduitPersonResponseVo check3ElementEdit(MrsPersonDto oldPersonDto, MrsPersonDto newPersonDto) {
		// 证件类型
		SaveAduitPersonResponseVo reponse = new SaveAduitPersonResponseVo();
		try {
			if (oldPersonDto.getCredentialsType().equals(newPersonDto.getCredentialsType())
					&& oldPersonDto.getCredentialsNumber().equals(newPersonDto.getCredentialsNumber())
					&& oldPersonDto.getCustomerName().equals(newPersonDto.getCustomerName())) {
				reponse.setIsSucess(true);
				reponse.setMsgCode("");
				reponse.setMsgInfo("没有改变！");
				return reponse;
			} else {
				List<MrsPersonDto> persons = findBy3Element(newPersonDto.getCustomerName(), newPersonDto.getCredentialsType(),
						newPersonDto.getCredentialsNumber());
				if(CollectionUtil.isEmpty(persons)){
					reponse.setIsSucess(true);
					reponse.setMsgCode("");
					reponse.setMsgInfo("三要素不存在一户通可修改！");
					return reponse;
				}else {
					reponse.setIsSucess(false);
					reponse.setMsgCode("");
					reponse.setMsgInfo("三要素存在一户通账户，不能修改！");
					return reponse;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			reponse.setIsSucess(false);
			reponse.setMsgCode("");
			reponse.setMsgInfo("校验异常！" + e.getMessage());
			return reponse;
		}

	}

	@Override
	public SaveAduitPersonResponseVo doUpdateActSaveAduit(MrsActAccountVo actAccountVo) {

		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		String custId = actAccountVo.getCustId();

		MrsPersonDto personDto = mrsPersonDtoMapper.findByCustId(custId);
		// 子账户信息
		List<MrsSubAccountDto> mrsSubAccountList = mrsSubAccountDtoMapper.findByCustId(custId);

		if (personDto == null || StringUtils.isEmpty(personDto.getCustId())) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("数据有误，一户通号码不存在！");
			return response;
		}
		// 一户通状态为注销 不让修改
		MrsAccountDto account = mrsAccountDtoMapper.findByCustId(custId);
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
		// 查询资金账户信息
		List<ActAccountDto> actAccountDtos = actAccountService.findListByCustId(custId);
		if (CollectionUtil.isEmpty(actAccountDtos)) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("资金账户异常！");
			return response;
		}
		String aduitId = mrsAduitInfoService.craateMrsAduitInfo(actAccountVo.getCreateRemark(),
				personDto.getCredentialsNumber(), personDto.getCredentialsType(), personDto.getCustomerName(), custId,
				account.getAccountStatus(), account.getAuthStatus(), EStartSystemEnum.SYS_COUNTER,
				actAccountVo.getCurrentUser().getLoginName(), EOperaTypeEnum.OP_MONUPDATE,
				MrsCustTypeEnum.MRS_CUST_TYPE_00);

		List<ActAccountDto> actAccounts = new ArrayList<ActAccountDto>();
		ActAccountDto acctDto = null;
		ActSubjectDto subjectDto = null;
		for (ActAccountDto dto : actAccountVo.getActAccountDtos()) {
			if (dto.getOperationType() != null) {
				acctDto = actAccountDtoMapper.selectByPrimaryKey(dto.getId());
				subjectDto = actSubjectDtoMapper.getSubjectName(acctDto.getSubjectNo2());
				acctDto.setSubjectName(subjectDto.getSubjectName());
				acctDto.setOperationType(dto.getOperationType());
				actAccounts.add(acctDto);
			} else {
				continue;
			}
		}

		// 新json值
		MrsToJsonDto newMrsToJson = new MrsToJsonDto();
		newMrsToJson.setActAccountDtos(actAccounts);
		newMrsToJson.setMrsAccountDto(account);
		newMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
		newMrsToJson.setMrsPersonDto(personDto);
		
		MrsToJsonDto oldMrsToJson = new MrsToJsonDto();
		oldMrsToJson.setActAccountDtos(actAccountDtos);
		oldMrsToJson.setMrsAccountDto(account);
		oldMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
		oldMrsToJson.setMrsPersonDto(personDto);

		// 创建审核内容信息
		MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
		JSONObject jsonsNew = JSONObject.fromObject(newMrsToJson);// 将java对象转换为json对象
		String strNew = jsonsNew.toString();

		JSONObject jsonsOld = JSONObject.fromObject(oldMrsToJson);// 将java对象转换为json对象
		String strOld = jsonsOld.toString();

		blobs.setAduitId(aduitId);
		blobs.setOldValue(strOld);
		blobs.setId(UUID.randomUUID().toString());
		blobs.setNewValue(strNew);
		mrsAduitContentDtoMapper.insertSelective(blobs);

		response.setIsSucess(true);
		response.setMsgCode("00");
		response.setMsgInfo("柜台端资金账户变更送审信息成功！");
		return response;
	}

	public MrsPersonDto copy2MrsPersonDto(IndividualRequestVO reqVo, String custId) {
		MrsPersonDto newPerson = new MrsPersonDto();
		BeanUtils.copyProperties(reqVo, newPerson);
		newPerson.setCreateTime(new Date());
		newPerson.setId(UUID.randomUUID().toString());
		newPerson.setCustId(custId);
		if (StringUtil.isEmpty(reqVo.getNationalCode())) {
			newPerson.setNationalityCode(MrsNationaltyCode.getEnum(reqVo.getNationalCode()));
		}
		return newPerson;
	}

	@Override
	public void updateBaseAndSync(MrsPersonDto dto) {
		// 更新个人信息
		updateByPrimaryKey(dto);
		MrsAccountDto mrsAccountDto = mrsAccountService.findByCustId(dto.getCustId());
		if(mrsAccountDto.getAuthStatus().equals(MrsAccountAuthStatus.MAAS_2.getValue()) || mrsAccountDto.getAuthStatus().equals(MrsAccountAuthStatus.MAAS_9.getValue())){
			// 同步基本信息
			MrsNotifyDto notifyDto = generateSyncInfo(dto, MrsNotifyType.PERSON);
			mrsNotifyDtoMapper.insert(notifyDto);
		}
	}

	@Override
	public void updateBaseFileAndSync(MrsPersonDto dto) {
		// 更新个人信息 并 入同步表 同步基础信息
		updateBaseAndSync(dto);
		// 同步证件信息
		MrsNotifyDto fileNotifyDto = generateSyncFileInfo(dto, MrsNotifyType.FILE);
		if (fileNotifyDto != null) {
			mrsNotifyDtoMapper.insert(fileNotifyDto);
		}
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public AccountMsgRespVo updatePersonInfo(AccountIndividualUpdateReqVO reqVo, MrsAccountDto account,
			MrsPlatformDto mrsPlatformDto) throws CodeCheckedException {
		log.info("一户通账户存在,custId={}", account.getCustId());
		// b1 如果接入平台认证类型为“无需认证”且一户通认证状态为“认证成功”或“无需认证”，
		// 更新“个人客户信息表”个人信息
		MrsPersonDto mrsPersonDto = new MrsPersonDto();
		MrsAccountAuthStatus authStatus = MrsAccountAuthStatus.getEnum(account.getAuthStatus());
		if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())
				&& MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			log.info("场景b1 更新个人信息");
			// 转换数据
			copyPersonDto(reqVo, mrsPersonDto);
			mrsPersonDto.setId(account.getUserInfoId());
			mrsPersonDtoMapper.updateByPrimaryKeySelective(mrsPersonDto);
		}
		// b2、如果接入平台认证类型为“无需认证”且一户通认证状态为非“认证成功”和非“无需认证”，
		// 更新“个人客户信息表”个人信息。更新“一户通账户表”认证状态为“无需认证”，
		// 如果一户通账户状态为“未生效”，更新账户状态为“正常”。同步客户信息，详见下面
		else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())
				&& !MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			log.info("场景b2 更新个人信息,一户通账户");
			copyPersonDto(reqVo, mrsPersonDto);
			mrsPersonDto.setId(account.getUserInfoId());
			MrsNationaltyCode nationaltyCode = StringUtil.isEmpty(reqVo.getNationalityCode())? null  : MrsNationaltyCode.getEnum(reqVo.getNationalityCode());
			if(nationaltyCode != null){
				mrsPersonDto.setNationalityCode(nationaltyCode);//国籍码
			}
			mrsPersonDtoMapper.updateByPrimaryKeySelective(mrsPersonDto);
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
			// c1、更新子账户信息。根据请求参数中渠道编号查询“子账户配置表”该渠道开通个人客户必须开通的子账户编号，
			// 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户，详见“个人被动开户a4”。
			// 如果子账户的状态为“已注销”，则更改状态为“有效”。
			log.info("场景c1 不更新信息");
			List<MrsConfSubAcctDto> creageSubList = checkAndSavePerson(reqVo, account, mrsPlatformDto);
			// c2、开通资金账户，详见“个人被动开户a8”。账务系统会判断要求开的资金账户是否已经存在，
			// 如果不存在则会新增，如果存在，则不会再开通。
			log.info("场景c2  开通资金账户");
			createAccounts(account.getCustId(), MrsCustomerType.MCT_0, reqVo.getCustomerName(), creageSubList);

			// c3、根据该记录中一户通编号、请求参数中接入平台编号和平台客户编号查询“一户通客户关系表”是否存在记录，
			// 如果不存在，则创建新的记录，记录内容与查询条件相同。
			List<MrsAccountPlatformCustDto> apcList = mrsAccountPlatformCustDtoMapper
					.findActPlatformCust(account.getCustId(), reqVo.getPlatformCustCode(), reqVo.getPlatformCode());
			if (apcList != null && apcList.size() > 0) {
				log.info("场景c3    存在一户通客户关系");
			} else {
				log.info("场景c3   建新一户通客户关系");
				saveMrsAccountPlatformIvidual(reqVo, account);
			}
			// c4、如果信息需要同步客户信息。，则创建“同步信息通知表”记录来通知ECIF平台。
			// 首先，根据通知类型“个人开户通知”和一户通编号查询通知状态为“未通知”和“通知失败”的记录，
			// 如果存在记录，则将查询出的记录状态改为“通知失效”。并创建新的通知记录
			log.info("场景c4 保存同步信息到通知表");
			MrsPersonDto person = mrsPersonDtoMapper.selectByPrimaryKey(account.getUserInfoId());
			saveNotifyInfoIvidual(reqVo, person);
		}
		return null;
	}
	/**
	 * 转换数据
	 * @param reqVo
	 * @param mrsPersonDto
	 */
	private void copyPersonDto(AccountIndividualUpdateReqVO reqVo, MrsPersonDto mrsPersonDto) {
		//必填项
		//mrsPersonDto.setCustId(reqVo.getAccountCode());//一户通号
		//mrsPersonDto.setCustomerName(reqVo.getCustomerName());//客户姓名
		//mrsPersonDto.setCredentialsType(reqVo.getCredentialsType());//证件类型
		//mrsPersonDto.setCredentialsNumber(reqVo.getCredentialsNumber());//证件号码
		//非必填项
		if(StringUtil.isNEmpty(reqVo.getCustomerCode())){
			mrsPersonDto.setCustomerCode(reqVo.getCustomerCode());//客户编号//ECIF必填
		}
		MrsNationaltyCode nationaltyCode = StringUtil.isEmpty(reqVo.getNationalityCode())? null  : MrsNationaltyCode.getEnum(reqVo.getNationalityCode());
		if(nationaltyCode != null){
			mrsPersonDto.setNationalityCode(nationaltyCode);//国籍码
		}
		if(StringUtil.isNEmpty(reqVo.getCredentialsEnddate())){
			mrsPersonDto.setCredentialsEnddate(reqVo.getCredentialsEnddate());//证件有效期
		}
		if(StringUtil.isNEmpty(reqVo.getCredentialsFilepath())){
			mrsPersonDto.setCredentialsFilepath(reqVo.getCredentialsFilepath());//证件留存地址
		}
		if(StringUtil.isNEmpty(reqVo.getBirthdate())){
			mrsPersonDto.setBirthdate(reqVo.getBirthdate());//出生日期
		}
		if(StringUtil.isNEmpty(reqVo.getSexCode())){
			mrsPersonDto.setSexCode(reqVo.getSexCode());//性别
		}
		if(StringUtil.isNEmpty(reqVo.getEducationCode())){
			mrsPersonDto.setEducationCode(reqVo.getEducationCode());//学历
		}
		if(StringUtil.isNEmpty(reqVo.getNationalCode())){
			mrsPersonDto.setNationalCode(reqVo.getNationalCode());//民族
		}
		if(StringUtil.isNEmpty(reqVo.getMobile())){
			mrsPersonDto.setMobile(reqVo.getMobile());//移动电话
		}
		if(StringUtil.isNEmpty(reqVo.getTel())){
			mrsPersonDto.setTel(reqVo.getTel());//固定电话
		}
		if(StringUtil.isNEmpty(reqVo.getSpareTel())){
			mrsPersonDto.setSpareTel(reqVo.getSpareTel());//备用电话
		}
		if(StringUtil.isNEmpty(reqVo.getContactAddr())){
			mrsPersonDto.setContactAddr(reqVo.getContactAddr());//联系地址
		}
		if(StringUtil.isNEmpty(reqVo.getZipCode())){
			mrsPersonDto.setZipCode(reqVo.getZipCode());//邮政编码
		}
		if(StringUtil.isNEmpty(reqVo.getEmail())){
			mrsPersonDto.setEmail(reqVo.getEmail());//邮箱
		}

		mrsPersonDto.setUpdateTime(new Date());
		mrsPersonDto.setUpdateOperator("");
	}
	/**
	 * 用户注销
	 */
	@Override
	public SaveAduitPersonResponseVo removeSaveAduit(MrsRemoveAccountVo removeAccountVo) {
		SaveAduitPersonResponseVo response = new SaveAduitPersonResponseVo();
		String custId = removeAccountVo.getCustId();

		MrsPersonDto personDto = mrsPersonDtoMapper.findByCustId(custId);
		// 子账户信息
		List<MrsSubAccountDto> mrsSubAccountList = mrsSubAccountDtoMapper.findByCustId(custId);

		if (personDto == null || StringUtils.isEmpty(personDto.getCustId())) {
			response.setIsSucess(false);
			response.setMsgCode("");
			response.setMsgInfo("数据有误，一户通号码不存在！");
			return response;
		}
		// 一户通状态为注销 不让修改
		MrsAccountDto account = mrsAccountDtoMapper.findByCustId(custId);
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
		// 查询资金账户信息
		List<ActAccountDto> actAccountDtos = actAccountService.findListByCustId(custId);
		String remark = removeAccountVo.getCreateRemark();
		String aduitId = mrsAduitInfoService.craateMrsAduitInfo(remark,
				personDto.getCredentialsNumber(), personDto.getCredentialsType(), personDto.getCustomerName(), custId,
				account.getAccountStatus(), account.getAuthStatus(), EStartSystemEnum.SYS_COUNTER,
				removeAccountVo.getCurrentUser().getLoginName(), EOperaTypeEnum.OP_CANCEL,
				MrsCustTypeEnum.MRS_CUST_TYPE_00);

		MrsAccountDto accountDto = account;
		accountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_2.getValue());
		accountDto.setCloseTime(new Date());
		accountDto.setCloseOperator(removeAccountVo.getCurrentUser().getLoginName());
		// 新json值
		MrsToJsonDto newMrsToJson = new MrsToJsonDto();
//		newMrsToJson.setActAccountDtos(actAccounts);
		newMrsToJson.setMrsAccountDto(accountDto);
		newMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
		newMrsToJson.setMrsPersonDto(personDto);
		newMrsToJson.setActAccountDtos(actAccountDtos);
		
		MrsToJsonDto oldMrsToJson = new MrsToJsonDto();
		oldMrsToJson.setActAccountDtos(actAccountDtos);
		oldMrsToJson.setMrsAccountDto(account);
		oldMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
		oldMrsToJson.setMrsPersonDto(personDto);

		// 创建审核内容信息
		MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
		JSONObject jsonsNew = JSONObject.fromObject(newMrsToJson);// 将java对象转换为json对象
		String strNew = jsonsNew.toString();

		JSONObject jsonsOld = JSONObject.fromObject(oldMrsToJson);// 将java对象转换为json对象
		String strOld = jsonsOld.toString();

		blobs.setAduitId(aduitId);
		blobs.setOldValue(strOld);
		blobs.setId(UUID.randomUUID().toString());
		blobs.setNewValue(strNew);
		mrsAduitContentDtoMapper.insertSelective(blobs);

		response.setIsSucess(true);
		response.setMsgCode("00");
		response.setMsgInfo("柜台端用户注销送审信息成功！");
		return response;
	}
}
