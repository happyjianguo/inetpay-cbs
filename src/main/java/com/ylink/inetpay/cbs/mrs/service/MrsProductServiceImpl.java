package com.ylink.inetpay.cbs.mrs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.ylink.inetpay.cbs.mrs.dao.MrsProductDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsSubAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserAccountDtoMapper;
import com.ylink.inetpay.common.core.constant.AaccountType;
import com.ylink.inetpay.common.core.constant.AcctBusiType;
import com.ylink.inetpay.common.core.constant.ConfAuditBusiType;
import com.ylink.inetpay.common.core.constant.EAduitTypeEnum;
import com.ylink.inetpay.common.core.constant.EDistributTypeEnum;
import com.ylink.inetpay.common.core.constant.EOperaTypeEnum;
import com.ylink.inetpay.common.core.constant.EProductTypeEnum;
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
import com.ylink.inetpay.common.core.constant.MrsNotifyStatus;
import com.ylink.inetpay.common.core.constant.MrsNotifyType;
import com.ylink.inetpay.common.core.constant.MrsPlatformCode;
import com.ylink.inetpay.common.core.constant.MrsPlatformIsAuth;
import com.ylink.inetpay.common.core.constant.MrsSubAccountOwnType;
import com.ylink.inetpay.common.core.constant.MrsSubAccountStatus;
import com.ylink.inetpay.common.core.constant.UseAccountType;
import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.core.util.DateUtils;
import com.ylink.inetpay.common.core.util.GsonUtil;
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
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsProductDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsActAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsProductVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsRemoveAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitProductResponseVo;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgRespVo;
import com.ylink.inetpay.common.project.portal.vo.AccountProductUpdateRequestVO;
import com.ylink.inetpay.common.project.portal.vo.ProductRequestVO;
import com.ylink.inetpay.common.project.portal.vo.sync.LinkMans;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncFileInfo;
import com.ylink.inetpay.common.project.portal.vo.sync.SyncProductReq;

import net.sf.json.JSONObject;

@Service("mrsProductService")
public class MrsProductServiceImpl implements MrsProductService {

	private static Logger log = LoggerFactory.getLogger(MrsProductServiceImpl.class);

	@Autowired
	private MrsAccountDtoMapper mrsAccountDtoMapper;
	@Autowired
	private MrsSubAccountDtoMapper mrsSubAccountDtoMapper;
	@Autowired
	private MrsUserAccountDtoMapper mrsUserAccountDtoMapper;
	@Autowired
	private MrsLoginUserDtoMapper mrsLoginUserDtoMapper;
	@Autowired
	private MrsProductDtoMapper mrsProductDtoMapper;
	@Autowired
	private MrsNotifyDtoMapper mrsNotifyDtoMapper;
	@Autowired
	private MrsConfSubAcctDtoMapper mrsConfSubAcctDtoMapper;
	@Autowired
	private MrsConfAuditDtoMapper mrsConfAuditDtoMapper;
	@Autowired
	private MrsAduitInfoDtoMapper mrsAduitInfoDtoMapper;
	@Autowired
	private MrsConfAuditItemDtoMapper mrsConfAuditItemDtoMapper;
	@Autowired
	private MrsAduitPersonDtoMapper mrsAduitPersonDtoMapper;
	@Autowired
	private MrsAduitContentDtoMapper mrsAduitContentDtoMapper;
	@Autowired
	private MrsAduitAttachmentDtoMapper mrsAduitAttachmentDtoMapper;
	@Autowired
	private MrsContactListService mrsContactListService;
	@Autowired
	private MrsCertFileService mrsCertFileService;
	@Autowired
	private ActAccountDtoMapper actAccountDtoMapper;
	@Autowired
	private ActBookAppService actBookAppService;
	@Autowired
	private MrsCertFileDtoMapper mrsCertFileDtoMapper;
	@Autowired
	private MrsAccountPlatformCustDtoMapper mrsAccountPlatformCustDtoMapper;
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private MrsConfSubAcctService mrsConfSubAcctService;
	@Autowired
	private ActSubjectDtoMapper actSubjectDtoMapper;
	@Autowired
	private MrsConfAcctDtoMapper mrsConfAcctDtoMapper;
	@Autowired
	private ActBusiRefSubDtoMapper actBusiRefSubDtoMapper;
	
	@Override
	public MrsProductDto findBy3Element(String productName, String credentialsType, String credentialsNumber) {

		List<MrsProductDto> productList = mrsProductDtoMapper.findBy3Element(productName, credentialsType,
				credentialsNumber);
		if (productList != null && productList.size() > 0) {
			List<String> custIds = new ArrayList<String>();
			for (MrsProductDto mpdto : productList) {
				custIds.add(mpdto.getCustId());
			}
			// 若存在多个一户通账户，优先返回认证状态的、最近更新的
			List<MrsAccountDto> maList = mrsAccountDtoMapper.findByCustIds(custIds);
			if (maList != null && maList.size() > 0) {
				String custId = "";
				for (MrsAccountDto madto : maList) {
					// 无需认证
					if (MrsAccountAuthStatus.MAAS_9.getValue().equals(madto.getAuthStatus())) {
						custId = madto.getCustId();
						break;
					}
					// 认证成功
					else if (MrsAccountAuthStatus.MAAS_2.getValue().equals(madto.getAuthStatus())) {
						custId = madto.getCustId();
						break;
					}
					// 认证中
					else if (MrsAccountAuthStatus.MAAS_1.getValue().equals(madto.getAuthStatus())) {
						custId = madto.getCustId();
						break;
					}
					// 认证失败
					else if (MrsAccountAuthStatus.MAAS_3.getValue().equals(madto.getAuthStatus())) {
						custId = madto.getCustId();
						break;
					} else {
						custId = madto.getCustId();
					}
				}
				for (MrsProductDto mptdto : productList) {
					if (custId.equals(mptdto.getCustId())) {
						return mptdto;
					}
				}
			}

		}
		return null;
	}
	/**
	 * 根据新生成一户通编号查询“一户通账户表”和“审核主要信息表”是否存在相同一户通编号， 如果存在，则重新生成，直至一户通编号不重复。
	 * 
	 * @return
	 */
	private String checkCustIdAndReturnId(String custId) {
		//递归
		if(StringUtils.isEmpty(custId)){
			custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
			custId = MrsConstants.ORGAN_ACCOUONT_PREFIX + StringUtils.format(11, custId);
		}
		boolean checkFlag = mrsAccountService.checkCustId(custId);
		// 如果返回True表编号可用
		if (!checkFlag) {
			return checkCustIdAndReturnId(null);
		}else{
			return custId;
		}
	}
	/**
	 * 根据客户类型，关联关系查询子账户配置
	 * @param cType
	 * @param platformCode
	 * @return
	 */
	private List<MrsConfSubAcctDto> findByUserTypeAndRationType(MrsCustomerType cType, String platformCode,String actType) {
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctService.findBy4Elment(cType, 
				MrsConfSubRelationType.MUST, platformCode, actType);
		return mrsConfSubAcctDtos;
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto initOpenAcnt(ProductRequestVO requestVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException {
		// 获取自然人保险一户通序列
		String custId = checkCustIdAndReturnId(null);
		// 将请求对象VO中的值赋值到一户通DTO中
		MrsAccountDto mrsAccountDto = copy2AccountDto(requestVo, custId, mrsPlatformDto.getIsAuth());
		// 根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_2,requestVo.getPlatformCode(),requestVo.getAccountType());
		// 创建子账户
		List<MrsSubAccountDto> subAccountList = generateSubAccount(custId, mrsPlatformDto,mrsConfSubAcctDtos);
		// 将请求对象VO中的值复制到产品客户的DTO中
		MrsProductDto mrsProductDto = copy2ProductDto(requestVo, custId);
		// 创建登陆对象信息
		MrsLoginUserDto mrsUserDto = copy2LoginUserDto(custId);
		// 账户信息和登陆用户信息关联
		MrsUserAccountDto userAccount = copy2UserAccount(mrsAccountDto.getId(), mrsUserDto.getId());
		log.info("一户通[custId={}]开始插入数据", custId);
		//一户通客户关系
		MrsAccountPlatformCustDto accountPlatformCustDto = generateAccountPlatform(requestVo, custId);
		
		// 保存一户通DTO
		mrsAccountDtoMapper.insert(mrsAccountDto);
		// 保存账户和登陆关联的信息
		mrsUserAccountDtoMapper.insert(userAccount);
		// 保存子账户信息
		for (MrsSubAccountDto mrsSubAccountDto : subAccountList) {
			mrsSubAccountDtoMapper.insert(mrsSubAccountDto);
		}
		// 保存产品客户的DTO
		mrsProductDtoMapper.insert(mrsProductDto);
		// 保存登陆信息
		mrsLoginUserDtoMapper.insert(mrsUserDto);
		mrsAccountDto.setNoEncryptloginPwd(mrsUserDto.getNoEncryptloginPwd());
		//一户通客户关系表
		mrsAccountPlatformCustDtoMapper.insert(accountPlatformCustDto);
		if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())) {
			// 需要同步到客户系统入库
			MrsNotifyDto mrsNotifyDto = generateSyncInfo(mrsProductDto, MrsNotifyType.PRODUCT);
			mrsNotifyDtoMapper.insert(mrsNotifyDto);
		}
		// 开通资金账户
		createAccounts(custId, MrsCustomerType.MCT_2, mrsProductDto.getProductName(),mrsConfSubAcctDtos);
		return mrsAccountDto;
	}
	/**
	 * 开通资金账户
	 * @param custId
	 * @param type
	 * @param customerName
	 * @throws Exception
	 */
	private void createAccounts(String custId, MrsCustomerType type,String customerName,
			List<MrsConfSubAcctDto> mrsConfSubAcctDtos) throws CodeCheckedException {
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
	/**
	 * 一户通客户关系
	 * @param requestVo
	 * @param custId
	 * @return
	 */
	private MrsAccountPlatformCustDto generateAccountPlatform(ProductRequestVO requestVo, String custId) {
		MrsAccountPlatformCustDto accountPlatformCustDto = new MrsAccountPlatformCustDto();
		accountPlatformCustDto.setId(accountPlatformCustDto.getIdentity());
		accountPlatformCustDto.setCustId(custId);
		accountPlatformCustDto.setSource(requestVo.getPlatformCode());
		accountPlatformCustDto.setPlatformCustCode(requestVo.getPlatformCustCode());
		accountPlatformCustDto.setCreateTime(new Date());
		return accountPlatformCustDto;
	}
	/**
	 * 封装一户通账户信息
	 * 
	 * @param requestVo
	 * @param custId
	 * @param platformCode
	 * @return
	 */
	private MrsAccountDto copy2AccountDto(ProductRequestVO requestVo, String custId, String isAuth) {
		MrsAccountDto dto = new MrsAccountDto();
		Date now = new Date();
		BeanUtils.copyProperties(requestVo, dto);
		dto.setCustId(custId);
		dto.setAccountName(requestVo.getProductName());
		dto.setCustomerType(MrsCustomerType.MCT_2.getValue());
		dto.setAccountSource(MrsAccountSource.SOURCE_05.getValue());
		if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(isAuth)) {
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
		} else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(isAuth)) {
			dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
			dto.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
		}
		if(AaccountType.BSYHT.getValue().equals(requestVo.getAccountType())){
			dto.setAccountType(AaccountType.BSYHT);
		}else if(AaccountType.CYRYHT.getValue().equals(requestVo.getAccountType())){
			dto.setAccountType(AaccountType.CYRYHT);
		}
		dto.setCreateTime(now);
		dto.setOpenTime(new Date());
		dto.setId(UUID.randomUUID().toString());
		dto.setIsDelete(IsDelete.IS_DELETE_N.getValue());
		dto.setOpenTime(now);
		dto.setExtOrgId(custId);
		return dto;
	}

	/**
	 * 同步信息至客户系统
	 */
	private MrsNotifyDto generateSyncInfo(MrsProductDto dto, MrsNotifyType notifyType) {
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
		SyncProductReq req = new SyncProductReq();
		
		
		BeanUtils.copyProperties(dto, req);
		
		MrsAccountDto accountDto = mrsAccountDtoMapper.findByCustId(dto.getCustId());
		if(null!=accountDto){
			req.setCustomerCode(accountDto.getCustomerCode());
		}
		
		if (org.apache.commons.lang.StringUtils.isNotBlank(dto.getProductEndDate())) {
			req.setProductEndDate(DateUtils.stringToString(dto.getProductEndDate()));
		}
		// 联系人
		List<LinkMans> linkMans = new ArrayList<LinkMans>();
		LinkMans LinkMan = new LinkMans();
		// 联系人名称
		LinkMan.setLinkManName(dto.getContactsName());
		// linkManMobile; // 联系人移动电话
		LinkMan.setLinkManMobile(dto.getContactsMobile());
		// linkManPhone; // 联系人固定电话
		LinkMan.setLinkManPhone(dto.getContactsTel());
		// linkManEmail; // 联系人电子邮箱
		LinkMan.setLinkManEmail(dto.getContactsEmail());
		// linkManSpareTel;// 联系人备用电话
		LinkMan.setLinkManSpareTel(dto.getContactsSpareTel());
		// linkManFax;// 联系人传真 公司有
		LinkMan.setLinkManFax(dto.getContactsFax());
		// linkManAddress;// 联系人联系地址
		LinkMan.setLinkManAddress(dto.getContactsContactAdd());
		// linkManZip;// 联系人邮政编码 公司有
		LinkMan.setLinkManZip(dto.getContactsZip());
		linkMans.add(LinkMan);
		req.setLinkMans(linkMans);
		
		
		
		req.setCustomerCode(dto.getCustomerCode());
		req.setProductName(dto.getProductName());
		if(null !=dto.getCredentialsType()){
			
		}
		req.setCredentialsType(dto.getCredentialsType().getValue());
		req.setCredentialsNumber(dto.getCredentialsNumber());
		if (null != dto.getProductTypeCode()) {
			req.setProductTypeCode(dto.getProductTypeCode().getValue());
		}
		req.setProductShortName(dto.getProductShortName());
		req.setProductEndDate(dto.getProductEndDate());
		req.setManagerName(dto.getManagerName());
		if (null != dto.getManagerCerType()) {
			req.setManagerCerType(dto.getManagerCerType().getValue());
		}
		req.setManagerCerCode(dto.getManagerCerCode());
		req.setTrusteeName(dto.getTrusteeName());
		if (null != dto.getTrusteeCerType()) {
			req.setTrusteeCerType(dto.getTrusteeCerType().getValue());
		}
		req.setTrusteeCerCode(dto.getTrusteeCerCode());
		req.setSysSourceCode(SHIEConfigConstant.SYNC_SYS_SOURCE_CODE);
		req.setCreateBy(SHIEConfigConstant.SYNC_CREATE_BY);
		req.setUpdateBy(SHIEConfigConstant.SYNC_UPDATE_BY);

		String jsonStr = GsonUtil.noAnnotaGson.toJson(req);
		log.info("同步字符串:" + jsonStr);
		// 生成通知数据
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
	 * 强制开户
	 * @throws CodeCheckedException 
	 */
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto forceOpenAcnt(ProductRequestVO reqVo, MrsPlatformDto mrsPlatformDto) throws CodeCheckedException {
		MrsAccountDto mrsAccountDto = null;
		// （a） 判断查询记录中认证状态为“已生效”、账户状态为非“已注销”的记录条数，
		// 并查询“系统参数表”强制开户的个数限制，如果记录条数大于等于个数限制，
		// 则记录错误日志并返回处理结果“开通失败”。
		boolean checkFlag = mrsAccountService.check3ElmentIsMax(reqVo.getCredentialsType(),
				reqVo.getCredentialsNumber(), reqVo.getProductName(), MrsCustomerType.MCT_2.getValue());
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
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsAccountDto updateOpenAcnt(ProductRequestVO requestVo,List<MrsAccountDto> productList, MrsPlatformDto newPlatform)
			throws CodeCheckedException {
		MrsAccountDto retrunMrsAccountDto = new MrsAccountDto();
		// c1、更新信息后需要返回一户通信息，查询返回一户通。查询结果存在多条记录，
		// 那么首先取认证状态为“认证成功”或“无需认证”记录
		// 其次再取其他状态的记录，如果相同状态仍存在多条取再次取一户通创建时间为最近的记录
		if (productList.size() == 1) {
			// 返回账户信息
			retrunMrsAccountDto = productList.get(0);
		} else {
			// 首先取认证状态为“认证成功”或“无需认证”记录
			List<MrsAccountDto> actProductLastList = mrsAccountService.findByProduct3ElementLast(requestVo.getProductName(),
					requestVo.getCredentialsType(), requestVo.getCredentialsNumber());
			if (actProductLastList != null && actProductLastList.size() > 0) {
				// 返回账户信息,最近的记录
				retrunMrsAccountDto = actProductLastList.get(0);
			}
			// 其次再取其他状态的记录
			else {
				// 返回账户信息,最近的记录
				retrunMrsAccountDto = productList.get(0);
			}
		}
		log.info("更新一户通的机构和账户信息");
		//c2、更新返回一户通的产品和账户信息
		log.info("场景c2  更新返回一户通的产品和账户信息");
		senseAccount(requestVo, retrunMrsAccountDto, newPlatform);
		// c3	根据请求参数中渠道编号查询“子账户配置表”该渠道开通机构客户必须开通的子账户编号，
		//判断该记录是否已经存在所有子账户，如果不存在或部分不存在或子账户的状态为“已注销”，
		// 则开通子账户，详见上面a4。如果子账户的状态为“已注销”，则更改状态为“有效”。
		log.info("场景c3   处理子账户配置信息");
		List<MrsConfSubAcctDto> creageSubList = checkAndSaveAct(requestVo.getPlatformCode(), retrunMrsAccountDto, newPlatform);
		//  开通资金账户。则将创建资金账户，详见上面a8。账务系统会判断要求开的资金账户是否已经存在，
		// 如果不存在则会新增。
		log.info("场景c4   开通资金账户");
		createAccounts(retrunMrsAccountDto.getCustId(), MrsCustomerType.MCT_2,
				requestVo.getProductName(), creageSubList);
		// （d） 根据该记录中一户通编号、请求参数中接入平台编号和平台客户编号查询“一户通客户关系表”是否存在记录，
		// 如果不存在，则创建新的记录，记录内容与查询条件相同。
		List<MrsAccountPlatformCustDto> apcList = mrsAccountPlatformCustDtoMapper
				.findActPlatformCust(retrunMrsAccountDto.getCustId(), 
						requestVo.getPlatformCustCode(), requestVo.getPlatformCode());
		if (apcList != null && apcList.size() > 0) {
			log.info("场景c2(c)   存在一户通客户关系");
		} else {
			log.info("场景c2(c)   建新一户通客户关系");
			saveMrsAccountPlatformCust(requestVo, retrunMrsAccountDto);
		}
		// 需要同步到客户系统
		if(MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(newPlatform.getIsAuth())){
			log.info("场景c6 保存同步信息到通知表");
			MrsProductDto product = mrsProductDtoMapper.selectByPrimaryKey(retrunMrsAccountDto.getUserInfoId());
			saveNotifyInfo(requestVo,product);
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
	private void saveNotifyInfo(ProductRequestVO reqVo,MrsProductDto product) {
		BeanUtils.copyProperties(reqVo, product);
		product.setUpdateTime(new Date());
		MrsNotifyDto mrsNotifyDto = generateSyncInfo(product, MrsNotifyType.PRODUCT);
		mrsNotifyDtoMapper.insert(mrsNotifyDto);
	}
	/**
	 * 根据请求参数中渠道编号查询“子账户配置表”该渠道开通产品客户必须开通的子账户编号，
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
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_2,
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
	/**
	 * 更新返回一户通的产品和账户信息
	 * @param reqVo
	 * @param account
	 * @param product
	 * @param newPlatform
	 */
	private void senseAccount(ProductRequestVO reqVo, MrsAccountDto account,
			MrsPlatformDto newPlatform) {
		log.info("senseAccount方法执行....");
		MrsProductDto product = new MrsProductDto();
		MrsAccountAuthStatus authStatus = MrsAccountAuthStatus.getEnum(account.getAuthStatus());
		MrsPlatformIsAuth newIsAuth = MrsPlatformIsAuth.getEnum(newPlatform.getIsAuth());
		if (MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_1.equals(newIsAuth)) {
			// 场景a原记录已经认证成功,新请求是可信渠道 只需要更新客户信息
			log.info("场景a   只更新产品客户信息");
			updateProductInfo(reqVo, account, product);
		} else if (!MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_1.equals(newIsAuth)) {
			// 场景b 原记录已经认证未成功,新请求是可信渠道
			log.info("场景b  更新“产品客户信息表”机构信息，需要更新“一户通账户表");
			updateProductInfo(reqVo, account, product);
			if(MrsAccountStatus.ACCOUNT_STATUS_9.getValue().equals(account.getAccountStatus())){
				account.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
				account.setAuthStatus(MrsAccountAuthStatus.MAAS_9.getValue());
				account.setOpenTime(new Date());
				account.setUpdateTime(new Date());
				mrsAccountDtoMapper.updateByPrimaryKeySelective(account);
			}
		} else if (MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_0.equals(newIsAuth)) {
			// 场景c 原记录已经认证成功,新请求是不可信渠道
			log.info("场景c  不更新任何信息");
		} else if (!MrsAccountAuthStatus.senseAuthStaus(authStatus) && MrsPlatformIsAuth.IS_AUTH_0.equals(newIsAuth)) {
			// 场景d原记录已经认证未成功,新请求是不可信渠道
			log.info("原记录已经认证未成功,新请求是不可信渠道");
			if (MrsAccountAuthStatus.MAAS_0.equals(authStatus)
					&& StringUtil.isEmpty(account.getCustomerCode())
					&& StringUtil.isEmpty(reqVo.getPlatformCustCode())) {
				// 场景4 请求更新“产品客户信息表”产品信息，更新“一户通账户表”客户编号为请求参数客户号。
				log.info("场景d  更新“产品客户信息表”产品信息，更新“一户通账户表”客户编号为请求参数客户号。");
				updateProductInfo(reqVo, account, product);

				account.setUpdateTime(new Date());
				account.setCustomerCode(reqVo.getCustomerCode());
				mrsAccountDtoMapper.updateByPrimaryKey(account);
			}
		}// （e） 如果接入平台认证类型为“需要认证”且一户通认证状态为非“认证成功”和非“无需认证”，
		// 且一户通客户编号不为空或请求参数中平台客户编号不为空，不更新任何信息。
		else if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(newPlatform.getIsAuth())
				&& !MrsAccountAuthStatus.senseAuthStaus(authStatus)
			|| StringUtil.isNEmpty(reqVo.getPlatformCustCode())
			|| StringUtil.isNEmpty(account.getCustomerCode())) {
			log.info("场景5 不更新任何信息");
			return;
		} else {
			log.info("没有对应的状态数据，不更新任何信息");
		}
	}
	private void updateProductInfo(ProductRequestVO reqVo, MrsAccountDto account, MrsProductDto product) {
		BeanUtils.copyProperties(reqVo, product);
		product.setUpdateTime(new Date());
		product.setId(account.getUserInfoId());
		
		if(null!=reqVo.getCredentialsType()){
			product.setCredentialsType(MrsCredentialsType.getEnum(reqVo.getCredentialsType()));
		}
		if(null!=reqVo.getProductTypeCode()){
			product.setProductTypeCode(EProductTypeEnum.getEnum(reqVo.getProductTypeCode()));
		}
		product.setContactsMobile(reqVo.getContactsMoblie());
		product.setContactsContactAdd(reqVo.getContactsAddr());
		if(null!=reqVo.getTrusteeCerType()){
			product.setTrusteeCerType(MrsCredentialsType.getEnum(reqVo.getTrusteeCerType()));
		}
		if(null!=reqVo.getManagerCerType()){
			product.setManagerCerType(MrsCredentialsType.getEnum(reqVo.getManagerCerType()));
		}
		mrsProductDtoMapper.updateByPrimaryKeySelective(product);
	}
	/**
	 * 生成账户信息
	 * 
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
	 * 生成子账户信息
	 * 
	 * @return
	 */
	private List<MrsSubAccountDto> generateSubAccount(String custId, MrsPlatformDto dto,List<MrsConfSubAcctDto> mrsConfSubAcctDtos) {
		List<MrsSubAccountDto> subAccountDtoList = new ArrayList<MrsSubAccountDto>();
		if(mrsConfSubAcctDtos!=null && mrsConfSubAcctDtos.size()>0){
			for(MrsConfSubAcctDto mrsConfSubAcctDto :mrsConfSubAcctDtos ){
				MrsSubAccountDto insureAccountDto = new MrsSubAccountDto();
				insureAccountDto.setId(UUID.randomUUID().toString());
				insureAccountDto.setCustId(custId); // 一户通编号
				String subInsure = mrsConfSubAcctDto.getSubAccountCode() + "0" + custId;
				insureAccountDto.setSubAccountCode(subInsure);	// 子账户编号 
				insureAccountDto.setSubAccountName(mrsConfSubAcctDto.getSubAccountName());//子账户名称
				insureAccountDto.setSubAccountType(mrsConfSubAcctDto.getSubAccountCode());	// 子账户类型
				if (MrsPlatformIsAuth.IS_AUTH_0.getValue().equals(dto.getIsAuth())) {
					insureAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue()); // 子账户状态
				} else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(dto.getIsAuth())) {
					insureAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue()); // 子账户状态
				}
				insureAccountDto.setAccountOwnType(MrsSubAccountOwnType.MSAOT_01.getValue()); // 账户持有类型
				insureAccountDto.setAccountSource(MrsAccountSource.SOURCE_05.getValue()); // 开户方式
				insureAccountDto.setPlatformCode(dto.getPlatformCode()); // 开户渠道
				insureAccountDto.setOpenTime(new Date()); // 开户时间
				insureAccountDto.setCreateTime(new Date()); // 创建时间
				subAccountDtoList.add(insureAccountDto);
			}
		}
		return subAccountDtoList;
	}

	/**
	 * 封装产品客户信息
	 * 
	 * @param requestVo
	 * @param custId
	 * @return
	 */
	private MrsProductDto copy2ProductDto(ProductRequestVO requestVo, String custId) {
		MrsProductDto dto = new MrsProductDto();
		BeanUtils.copyProperties(requestVo, dto);
		dto.setCreateTime(new Date());
		if(null!=requestVo.getCredentialsType()){
			dto.setCredentialsType(MrsCredentialsType.getEnum(requestVo.getCredentialsType()));
		}
		if(null!=requestVo.getProductTypeCode()){
			dto.setProductTypeCode(EProductTypeEnum.getEnum(requestVo.getProductTypeCode()));
		}
		dto.setId(UUID.randomUUID().toString());
		dto.setCustId(custId);
		dto.setContactsMobile(requestVo.getContactsMoblie());
		dto.setContactsContactAdd(requestVo.getContactsAddr());
		if(null!=requestVo.getTrusteeCerType()){
			dto.setTrusteeCerType(MrsCredentialsType.getEnum(requestVo.getTrusteeCerType()));
		}
		if(null!=requestVo.getManagerCerType()){
			dto.setManagerCerType(MrsCredentialsType.getEnum(requestVo.getManagerCerType()));
		}
		return dto;
	}

	/**
	 * 生成用户信息
	 * 
	 * @return
	 */
	private MrsLoginUserDto copy2LoginUserDto(String custId) {
		MrsLoginUserDto dto = new MrsLoginUserDto();
		dto.setCustId(custId);
		dto.setCreateTime(new Date());
		String loginPwd = RandomUtil.generate6Random();
		dto.setNoEncryptloginPwd(loginPwd); // 明文密码
		loginPwd = MD5Utils.MD5(loginPwd);
		loginPwd = MD5Utils.MD5(loginPwd + SHIEConfigConstant.SALT);
		dto.setAccountCode(custId);
		dto.setLoginPwd(loginPwd);
		dto.setId(UUID.randomUUID().toString());
		dto.setAccountCode(custId);
		dto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
		return dto;
	}

	@Override
	public void updateByPrimaryKey(MrsProductDto record) {
		mrsProductDtoMapper.updateByPrimaryKey(record);
	}

	@Override
	public PageData<MrsProductDto> findListPage(PageData<MrsProductDto> pageData, MrsProductDto proDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsProductDto> list = mrsProductDtoMapper.findListPage(proDto);
		Page<MrsProductDto> page = (Page<MrsProductDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitProductResponseVo saveAduitProductVo(MrsProductVo productVo) {

		SaveAduitProductResponseVo respVo = new SaveAduitProductResponseVo();

		// 校验一户通信息
		if (!StringUtils.isEmpty(productVo.getMrsAccountDto().getId())) {
			MrsAccountDto dto = mrsAccountDtoMapper.selectByPrimaryKey(productVo.getMrsAccountDto().getId());
			if (dto != null) {
				respVo.setIsSucess(false);
				respVo.setMsgCode("");
				respVo.setMsgInfo("一户通编号已存在!");
				return respVo;
			}
		}

		// 设置一户通为产品用户
		productVo.getMrsAccountDto().setOpenTime(new Date());
		productVo.getMrsAccountDto().setCustomerType(MrsCustomerType.MCT_2.getValue());
		productVo.getMrsAccountDto().setAccountSource(MrsAccountSource.SOURCE_01.getValue());
		// 柜台
		productVo.getMrsAccountDto().setPlatformCode("account");
		// 设置联系人来源柜台
		for (MrsContactListDto dto : productVo.getMrsContactListDtoList()) {
			dto.setSource(MrsAccountSource.SOURCE_01.getValue());
		}
		
		// 检查三要素确定唯一性
		List<MrsProductDto> MrsProductDtoList = findBy3ElementAndNoEff(productVo.getMrsProductDto().getProductName(),
				productVo.getMrsProductDto().getCredentialsType().getValue(),
				productVo.getMrsProductDto().getCredentialsNumber());
        //校验三要素最大上限
		boolean flag = mrsAccountService.check3ElmentIsMax(productVo.getMrsProductDto().getProductName(),
				productVo.getMrsProductDto().getCredentialsType().getValue(),
				productVo.getMrsProductDto().getCredentialsNumber(), MrsCustomerType.MCT_2.getValue());
		
		if (!MrsProductDtoList.isEmpty()&&MrsProductDtoList!=null) {
			if(!flag){
				respVo.setIsSucess(false);
				respVo.setMsgCode("");
				respVo.setMsgInfo("该三要素已存在，且已达最大上限!");
				return respVo;
			}
		}
		//校验联系人是否为空
        List<MrsContactListDto> mrsContactDtoList = new ArrayList<MrsContactListDto>();
		for(MrsContactListDto dto:productVo.getMrsContactListDtoList()){
			if(dto!=null){
				//新增联系人
				if(!StringUtil.isEmpty(dto.getName())&&StringUtil.isEmpty(dto.getId())){
					dto.setSource(EStartSystemEnum.SYS_COUNTER.getValue());
					dto.setCreateTime(new Date());
					mrsContactDtoList.add(dto);
				//使用未生效信息
				}else if(!StringUtil.isEmpty(dto.getName())){
					mrsContactDtoList.add(dto);
				}
			}
		}
        /**
		 * 检查子账户信息 子账户的个数是否大于产品必须的
		 */
		if (productVo.getMrsConfSubAcctIds() == null || productVo.getMrsConfSubAcctIds().isEmpty()) {
			respVo.setIsSucess(false);
			respVo.setMsgCode("");
			respVo.setMsgInfo("添加产品用户信息送审时，没有配置子账户设置参数！");
			return respVo;
		}

		// 检查子账户信息,子账户个数必须大于产品客户子账户必须数
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = selectMrsConfSubAcctDtos(MrsCustomerType.MCT_2,
				MrsConfSubRelationType.MUST);
		if (mrsConfSubAcctDtos == null || mrsConfSubAcctDtos.isEmpty()) {
			respVo.setIsSucess(false);
			respVo.setMsgCode("");
			respVo.setMsgInfo("添加产品用户信息送审时，没有配置子账户设置参数！");
			return respVo;
		}

		// List<String> confSubIdList = new ArrayList<String>();
		List<MrsSubAccountDto> mrsSubAccountDtoList = new ArrayList<MrsSubAccountDto>();
		// 子账户dto
		MrsSubAccountDto subAcctDto = null;
		// 子账户配置dto
		MrsConfSubAcctDto dto = null;
		// String conId = null;
		for (String acctDto : productVo.getMrsConfSubAcctIds()) {
			dto = mrsConfSubAcctDtoMapper.selectByPrimaryKey(acctDto);
			if (dto == null) {
				respVo.setIsSucess(false);
				respVo.setMsgCode("");
				respVo.setMsgInfo("添加产品用户信息送审时，配置子账户信息异常！");
				return respVo;

			}
			//校验子账户信息是否正确
			if(MrsConfSubRelationType.NO.getValue().equals(dto.getProductType())){
				respVo.setIsSucess(false);
				respVo.setMsgCode("");
				respVo.setMsgInfo("添加产品用户信息送审时，请选择个人正确的子账户类型！");
				return respVo;
			}
			if(!MrsPlatformCode.ACCOUNT.getValue().equals(dto.getPlatformCode())){
				respVo.setIsSucess(false);
				respVo.setMsgCode("");
				respVo.setMsgInfo("添加产品用户信息送审时，选择的子账户信息渠道错误！");
				return respVo;
			}
			subAcctDto = new MrsSubAccountDto();
			subAcctDto.setSubAccountName(dto.getSubAccountName());
			subAcctDto.setSubAccountCode(dto.getSubAccountCode());
			subAcctDto.setOpenTime(new Date());
			subAcctDto.setAccountOwnType(MrsAccountSource.SOURCE_01.getValue());
			subAcctDto.setAccountSource(MrsAccountSource.SOURCE_01.getValue());
			//子账户状态为未生效
			subAcctDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_9.getValue());
			subAcctDto.setSubAccountType(dto.getSubAccountCode());
			// 柜台
			subAcctDto.setPlatformCode("account");
			mrsSubAccountDtoList.add(subAcctDto);
		}
		productVo.setMrsSubAccountDtoList(mrsSubAccountDtoList);

		// 科目支持业务类型信息 科目编号
//		List<String> mrsSubPayBusiDtos = mrsSubPayBusiDtoMapper.findByConfSubAcctIds(productVo.getMrsConfSubAcctIds());
		List<AccountSubsPojo> subPojos = null;
		if( !UseAccountType.USE_01.getValue().equals(productVo.getIsForce())){
			//1，先查子账户配置跟，子账号得出子账号属于大类
			List<String> subAcctTypes = mrsConfAcctDtoMapper.findSubAcctTypesByConfSubIds(productVo.getMrsConfSubAcctIds());
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

		// 校验用户昵称、手机号
		MrsLoginUserDto userAliasDto = null;
		MrsLoginUserDto userPhoneDto = null;
		// 获取用户信息
		List<MrsLoginUserDto> loginUserDtoList = productVo.getMrsLoginUserDtos();
		if (!loginUserDtoList.isEmpty() && loginUserDtoList != null) {
			for (MrsLoginUserDto loginUserDto : loginUserDtoList) {
				//新增用户
				if(loginUserDto!=null&&StringUtil.isEmpty(loginUserDto.getId())){
					userAliasDto = mrsLoginUserDtoMapper.findByAlias(loginUserDto.getAlias());
					userPhoneDto = mrsLoginUserDtoMapper.findByMobile(loginUserDto.getMobile());
					if (userAliasDto != null || userPhoneDto != null) {
						respVo.setIsSucess(false);
						respVo.setMsgCode("");
						respVo.setMsgInfo("添加产品用户信息送审时，该用户昵称或手机号已存在!");
						return respVo;
					}
				}
			}

		}
		// 设置审核人，查询基础配置设置产品开户申请审核配置人数
		// 生成审核信息
		MrsAduitInfoDto infoDto = new MrsAduitInfoDto();
		// 查询审核配置表获取审核人数
		MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
				ConfAuditBusiType.OP_OPEN.getValue(), EStartSystemEnum.SYS_COUNTER.getValue());
		if (mrsConfAuditDto == null) {
			respVo.setIsSucess(false);
			respVo.setMsgCode("");
			respVo.setMsgInfo("柜台端开户没有配置审核信息!");
			return respVo;
		}

		infoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
		infoDto.setAduitNum((short) 0);
		infoDto.setBusiType(EOperaTypeEnum.OP_OPEN);
		infoDto.setStartSystem(EStartSystemEnum.SYS_COUNTER);
		infoDto.setCartNo(productVo.getMrsProductDto().getCredentialsNumber());
		infoDto.setCartType(productVo.getMrsProductDto().getCredentialsType().getValue());
		infoDto.setName(productVo.getMrsProductDto().getProductName());
		infoDto.setCreateOperator(productVo.getCurrentUser().getLoginName());
		// 产品客户类型
		infoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_02);
		infoDto.setCreateOperator(productVo.getCurrentUser().getLoginName());
		infoDto.setCreateTime(new Date());
		//账户状态
		infoDto.setProductStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
		//认证状态
		infoDto.setProductAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
		infoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
		infoDto.setId(UUID.randomUUID().toString());
		//手动填写一户通账号
		if(!StringUtil.isEmpty(productVo.getMrsAccountDto().getCustId())){
			infoDto.setCustId(productVo.getMrsAccountDto().getCustId());
		}
		if (!StringUtils.isEmpty(productVo.getCreateRemark())) {
			infoDto.setRemark(productVo.getCreateRemark());
		}
		// 保存审核信息记录
		mrsAduitInfoDtoMapper.insertSelective(infoDto);

		List<MrsConfAuditItemDto> mrsConfAuditItemList = mrsConfAuditItemDtoMapper
				.selectByAuditId(mrsConfAuditDto.getId());
		List<MrsAduitPersonDto> mrsAduitPersonDtoList = new ArrayList<MrsAduitPersonDto>();

		// 创建审核人记录
		MrsAduitPersonDto aduitPerson = null;
		for (MrsConfAuditItemDto itemDto : mrsConfAuditItemList) {
			aduitPerson = new MrsAduitPersonDto();
			aduitPerson.setAduitId(infoDto.getId());
			aduitPerson.setAduitStatus(EAduitTypeEnum.ADUIT_WAIT);
			aduitPerson.setAduitUserId(itemDto.getUserId());
			aduitPerson.setAduitUserName(itemDto.getUserName());
			aduitPerson.setType(EDistributTypeEnum.AUTO_DISTRIBUT);
			aduitPerson.setStatus(EStatusEnum.EFFECTIVE);
			aduitPerson.setId(UUID.randomUUID().toString());
			aduitPerson.setCreateTime(new Date());
			mrsAduitPersonDtoList.add(aduitPerson);

		}

		mrsAduitPersonDtoMapper.batchInsert(mrsAduitPersonDtoList);

		MrsToJsonDto proMrsToJson = new MrsToJsonDto();
		proMrsToJson.setMrsAccountDto(productVo.getMrsAccountDto());
		proMrsToJson.setMrsContactListDtos(mrsContactDtoList);
		proMrsToJson.setMrsLoginUserDtoList(productVo.getMrsLoginUserDtos());
		proMrsToJson.setMrsProductDto(productVo.getMrsProductDto());
		proMrsToJson.setMrsSubAccountDtoList(productVo.getMrsSubAccountDtoList());
		proMrsToJson.setMrsUserPayPasswordDto(productVo.getMrsUserPayPasswordDto());
		proMrsToJson.setAccountSubsPojos(subPojos);
		proMrsToJson.setMrsAduitAttachmentDtos(productVo.getMrsAduitAttachmentDtos());
        if(productVo.getIsForce()!=null){
        	proMrsToJson.setIsForce(productVo.getIsForce());
        }
		// 创建审核内容信息,保存json串
		MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
		// List<MrsAduitContentDtoWithBLOBs> contentList =
		// mrsAduitContentDtoMapper.selectByAuditId();
		// 将java对象转换为json串
		JSONObject jsons = JSONObject.fromObject(proMrsToJson);
		String str = jsons.toString();

		blobs.setAduitId(infoDto.getId());
		blobs.setId(UUID.randomUUID().toString());
		blobs.setNewValue(str);
		mrsAduitContentDtoMapper.insertSelective(blobs);

		/**
		 * 上传附件
		 */
		List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
		if(!productVo.getMrsAduitAttachmentDtos().isEmpty()&&productVo.getMrsAduitAttachmentDtos().size() > 0){
			for (MrsAduitAttachmentDto att : productVo.getMrsAduitAttachmentDtos()) {
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
		respVo.setIsSucess(true);
		respVo.setMsgCode("");
		respVo.setMsgInfo("柜台端产品开户送审信息成功!");
		return respVo;

	}

	/**
	 * 产品信息修改申请
	 * 
	 * @return
	 */
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitProductResponseVo updateAduitProduct(MrsProductVo mrsProductVo) {

		SaveAduitProductResponseVo respVo = new SaveAduitProductResponseVo();
		try {
			// 产品信息主键
			MrsProductDto mrsProductDto = mrsProductDtoMapper
					.selectByPrimaryKey(mrsProductVo.getMrsProductDto().getId());
			// 联系人信息
			List<MrsContactListDto> mrsContactListDtos = mrsContactListService.findByCustId(mrsProductDto.getCustId());
			// 查询附件信息并且归类处理
			List<MrsCertFileDto> mrsCertFileDtos = mrsCertFileService.findByCustId(mrsProductDto.getCustId());
			// 校验一户通账户状态
			MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(mrsProductDto.getCustId());
			if (mrsAccountDto.getAccountStatus().equals(MrsAccountStatus.ACCOUNT_STATUS_2.getValue())) {
				respVo.setIsSucess(false);
				respVo.setMsgCode("");
				respVo.setMsgInfo("该产品一户通账户已注销!");
				return respVo;
			}
			//检验产品三要素
			List<MrsProductDto> mrsProdcDtoList = findBy3ElementAndNoEff(mrsProductVo.getMrsProductDto().getProductName(),
					mrsProductVo.getMrsProductDto().getCredentialsType().getValue(),
					mrsProductVo.getMrsProductDto().getCredentialsNumber());
			//校验三要素最大数
//			boolean flag = mrsAccountService.check3ElmentIsMax(mrsProductVo.getMrsProductDto().getCredentialsType().getValue(), mrsProductVo.getMrsProductDto().getCredentialsNumber(),
//					mrsProductVo.getMrsProductDto().getProductName(),MrsCustomerType.MCT_2.getValue());
		    if(!mrsProdcDtoList.isEmpty()&&mrsProdcDtoList.size() > 1){
					respVo.setIsSucess(false);
					respVo.setMsgCode("");
					respVo.setMsgInfo("该产品三要素已存在,不能修改!");
					return respVo;
		    }
			
		    //校验联系人信息
		    List<MrsContactListDto> mrsContactDtoList = new ArrayList<MrsContactListDto>();
			for(MrsContactListDto dto:mrsProductVo.getMrsContactListDtoList()){
				if(dto!=null){
					//新增联系人
					if(!StringUtil.isEmpty(dto.getName())&&StringUtil.isEmpty(dto.getId())){
						dto.setSource(EStartSystemEnum.SYS_COUNTER.getValue());
						dto.setCreateTime(new Date());
						mrsContactDtoList.add(dto);
					//使用未生效信息或修改
					}else if(!StringUtil.isEmpty(dto.getName())){
						mrsContactDtoList.add(dto);
					}
				}
			}
			
			// 设置审核人，查询基础配置设置产品开户申请审核配置人数
			// 生成审核信息
			MrsAduitInfoDto infoDto = new MrsAduitInfoDto();
			// 查询审核配置表获取审核人数
			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					ConfAuditBusiType.OP_UPADTE.getValue(), EStartSystemEnum.SYS_COUNTER.getValue());
			if (mrsConfAuditDto == null) {
				respVo.setIsSucess(false);
				respVo.setMsgCode("");
				respVo.setMsgInfo("柜台端信息变更没有配置审核信息!");
				return respVo;
			}
			infoDto.setCustId(mrsProductDto.getCustId());
			infoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			infoDto.setAduitNum((short) 0);
			infoDto.setBusiType(EOperaTypeEnum.OP_UPADTE);
			infoDto.setStartSystem(EStartSystemEnum.SYS_COUNTER);
//			infoDto.setCartNo(mrsProductVo.getMrsProductDto().getCredentialsNumber());
//			infoDto.setCartType(mrsProductVo.getMrsProductDto().getCredentialsType().getValue());
			infoDto.setCartNo(mrsProductDto.getCredentialsNumber());
			infoDto.setCartType(mrsProductDto.getCredentialsType().getValue());
			infoDto.setName(mrsProductDto.getProductName());
			infoDto.setCreateOperator(mrsProductVo.getCurrentUser().getLoginName());
			// 产品客户类型
			infoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_02);
			infoDto.setCreateOperator(mrsProductVo.getCurrentUser().getLoginName());
			infoDto.setCreateTime(new Date());
			infoDto.setProductStatus(mrsAccountDto.getAccountStatus());
			infoDto.setProductAuthStatus(mrsAccountDto.getAuthStatus());
			infoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			infoDto.setId(UUID.randomUUID().toString());
			if (!StringUtils.isEmpty(mrsProductVo.getCreateRemark())) {
				infoDto.setRemark(mrsProductVo.getCreateRemark());
			}
			// 保存审核信息记录
			mrsAduitInfoDtoMapper.insertSelective(infoDto);

			List<MrsConfAuditItemDto> mrsConfAuditItemList = mrsConfAuditItemDtoMapper
					.selectByAuditId(mrsConfAuditDto.getId());
			List<MrsAduitPersonDto> mrsAduitPersonDtoList = new ArrayList<MrsAduitPersonDto>();

			// 创建审核人记录
			MrsAduitPersonDto aduitPerson = null;
			for (MrsConfAuditItemDto itemDto : mrsConfAuditItemList) {
				aduitPerson = new MrsAduitPersonDto();
				aduitPerson.setAduitId(infoDto.getId());
				aduitPerson.setAduitStatus(EAduitTypeEnum.ADUIT_WAIT);
				aduitPerson.setAduitUserId(itemDto.getUserId());
				aduitPerson.setAduitUserName(itemDto.getUserName());
				aduitPerson.setType(EDistributTypeEnum.AUTO_DISTRIBUT);
				aduitPerson.setStatus(EStatusEnum.EFFECTIVE);
				aduitPerson.setId(UUID.randomUUID().toString());
				aduitPerson.setCreateTime(new Date());
				mrsAduitPersonDtoList.add(aduitPerson);

			}

			mrsAduitPersonDtoMapper.batchInsert(mrsAduitPersonDtoList);
//			List<MrsContactListDto> mrsContactDtoList = new ArrayList<MrsContactListDto>();
//			for(MrsContactListDto dto:mrsProductVo.getMrsContactListDtoList()){
//				dto.setSource(EStartSystemEnum.SYS_COUNTER.getValue());
//				mrsContactDtoList.add(dto);
//			}
			// json新值
			mrsProductVo.getMrsProductDto().setCustId(mrsProductDto.getCustId());
			MrsToJsonDto proMrsToJson = new MrsToJsonDto();
			proMrsToJson.setMrsContactListDtos(mrsContactDtoList);
			proMrsToJson.setMrsProductDto(mrsProductVo.getMrsProductDto());
			proMrsToJson.setMrsAduitAttachmentDtos(mrsProductVo.getMrsAduitAttachmentDtos());
			proMrsToJson.setMrsAccountDto(mrsAccountDto);
			
			// json原值
			MrsToJsonDto oldMrsToJson = new MrsToJsonDto();
			oldMrsToJson.setMrsProductDto(mrsProductDto);
			oldMrsToJson.setMrsContactListDtos(mrsContactListDtos);
			oldMrsToJson.setMrsCertFileDtos(mrsCertFileDtos);
			oldMrsToJson.setMrsAccountDto(mrsAccountDto);

			MrsAduitContentDtoWithBLOBs blobs = new MrsAduitContentDtoWithBLOBs();
			// 将java对象转换为json串
			JSONObject jsons = JSONObject.fromObject(proMrsToJson);
			String str = jsons.toString();
			JSONObject oldJson = JSONObject.fromObject(oldMrsToJson);
			String oldStr = oldJson.toString();

			blobs.setAduitId(infoDto.getId());
			blobs.setId(UUID.randomUUID().toString());
			blobs.setNewValue(str);
			blobs.setOldValue(oldStr);
			mrsAduitContentDtoMapper.insertSelective(blobs);
			/**
			 * 上传附件
			 */
			List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = new ArrayList<MrsAduitAttachmentDto>();
			if(!CollectionUtil.isEmpty(mrsProductVo.getMrsAduitAttachmentDtos())){
				for (MrsAduitAttachmentDto att : mrsProductVo.getMrsAduitAttachmentDtos()) {
					att.setCreateTime(new Date());
					att.setStatus(EStatusEnum.EFFECTIVE.getValue());
					// 发起端
					att.setCatalog(EStartSystemEnum.SYS_COUNTER.getValue());
					att.setAduitContentId(blobs.getId());
					att.setId(UUID.randomUUID().toString());
					att.setRemark(mrsProductVo.getCreateRemark());
					mrsAduitAttachmentDtos.add(att);
				}
				mrsAduitAttachmentDtoMapper.batchInsert(mrsAduitAttachmentDtos);
			}
			respVo.setIsSucess(true);
			respVo.setMsgCode("");
			respVo.setMsgInfo("柜台端产品信息变更送审信息成功!");
			return respVo;
		} catch (Exception e) {
			respVo.setIsSucess(false);
			respVo.setMsgCode("");
			respVo.setMsgInfo("柜台端产品信息变更送审失败!");
			return respVo;
		}

	}
    
	
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitProductResponseVo doUpdateAcctStatus(MrsActAccountVo mrsActAccountVo) {

		SaveAduitProductResponseVo respvo = new SaveAduitProductResponseVo();
		MrsProductDto mrsProductDto = mrsProductDtoMapper.findCustId(mrsActAccountVo.getCustId());
		List<MrsSubAccountDto> mrsSubAccountList = mrsSubAccountDtoMapper.findByCustId(mrsActAccountVo.getCustId());
		try {
			List<ActAccountDto> actAccountList = actAccountDtoMapper.findListByCustId(mrsActAccountVo.getCustId());
			// 获取资金账户
			if (actAccountList == null|| actAccountList.isEmpty()) {
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("获取资金账户为空!");
				return respvo;
			}
			//一户通状态为注销  不让修改
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
			mrsAduitInfoDto.setCustId(mrsProductDto.getCustId());
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short) 0);
			mrsAduitInfoDto.setBusiType(EOperaTypeEnum.OP_MONUPDATE);
			mrsAduitInfoDto.setStartSystem(EStartSystemEnum.SYS_COUNTER);
			mrsAduitInfoDto.setCartNo(mrsProductDto.getCredentialsNumber());
			mrsAduitInfoDto.setCartType(mrsProductDto.getCredentialsType().getValue());
			mrsAduitInfoDto.setName(mrsProductDto.getProductName());
			// 产品客户类型
			mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_02);
			mrsAduitInfoDto.setCreateOperator(mrsActAccountVo.getCurrentUser().getLoginName());
			mrsAduitInfoDto.setCreateTime(new Date());
			mrsAduitInfoDto.setProductStatus(account.getAccountStatus());
			mrsAduitInfoDto.setProductAuthStatus(account.getAuthStatus());
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAduitInfoDto.setId(UUID.randomUUID().toString());
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
			oldMrsToJson.setMrsProductDto(mrsProductDto);
			oldMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
			oldMrsToJson.setMrsAccountDto(account);
			
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
			newMrsToJson.setMrsProductDto(mrsProductDto);
			newMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
			newMrsToJson.setMrsAccountDto(account);
			
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

	/**
	 * 校验账户有效性
	 * 
	 * @return
	 */
	public boolean checkAccountStatus(String id) {
		MrsAccountDto acctDto = mrsAccountDtoMapper.selectByPrimaryKey(id);
		if (acctDto != null) {
			if (MrsAccountStatus.ACCOUNT_STATUS_9.getDisplayName().equals(acctDto.getAccountStatusName())) {
				return false;
			}
		}
		return true;
	}

	public List<MrsConfSubAcctDto> selectMrsConfSubAcctDtos(MrsCustomerType custType,
			MrsConfSubRelationType relationType) {
		List<MrsConfSubAcctDto> subAccts;
		if (MrsCustomerType.MCT_0.equals(custType)) {
			subAccts = mrsConfSubAcctDtoMapper.findByPersonType(relationType.getValue(),null);
		} else if (MrsCustomerType.MCT_1.equals(custType)) {
			subAccts = mrsConfSubAcctDtoMapper.findByOrganType(relationType.getValue(),null);
		} else if (MrsCustomerType.MCT_2.equals(custType)) {
			subAccts = mrsConfSubAcctDtoMapper.findByProductType(relationType.getValue(),null);
		} else {
			subAccts = new ArrayList<MrsConfSubAcctDto>();
		}
		return subAccts;

	}

	@Override
	public MrsAccountDto findByCustId(String custId) {
		return mrsAccountDtoMapper.findByCustId(custId);
	}

	@Override
	public PageData<MrsSubAccountDto> findSubAccountByCustId(PageData<MrsSubAccountDto> pageData, String custId) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsSubAccountDto> list = mrsSubAccountDtoMapper.findByCustId(custId);
		Page<MrsSubAccountDto> page = (Page<MrsSubAccountDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<MrsLoginUserDto> findLoginUserByCustId(PageData<MrsLoginUserDto> pageData, String custId) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsLoginUserDto> list = mrsLoginUserDtoMapper.findUserDtoByCustId(custId);
		Page<MrsLoginUserDto> page = (Page<MrsLoginUserDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;

	}

	@Override
	public MrsProductDto selectByPrimaryKey(String id) {
		return mrsProductDtoMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public MrsProductDto findCustId(String custId) {
		return mrsProductDtoMapper.findCustId(custId);
	}

	@Override
	public void updateBaseAndSync(MrsProductDto dto) {
		// 更新基础信息
		updateByPrimaryKey(dto);
		//同步基本信息
		MrsAccountDto mrsAccountDto  = mrsAccountDtoMapper.findByCustId(dto.getCustId());
		if(mrsAccountDto.getAuthStatus().equals(MrsAccountAuthStatus.MAAS_2.getValue()) || mrsAccountDto.getAuthStatus().equals(MrsAccountAuthStatus.MAAS_9.getValue())){
			MrsNotifyDto notifyDto = generateSyncInfo(dto, MrsNotifyType.PRODUCT);
			mrsNotifyDtoMapper.insert(notifyDto);
		}
	}
	
	
	
	@Override
	public void updateBaseFileAndSync(MrsProductDto dto) {
		// 更新基础信息  并 入同步表 同步基础信息
		updateBaseAndSync(dto);
		// 同步证件信息
		MrsNotifyDto fileNotifyDto = generateSyncFileInfo(dto, MrsNotifyType.FILE);
		if (fileNotifyDto != null) {
			mrsNotifyDtoMapper.insert(fileNotifyDto);
		}
		
	}

	public MrsProductDto copy2MrsProductDto(ProductRequestVO reqVo,String custId){
		MrsProductDto mrsProductDto = new MrsProductDto();
		BeanUtils.copyProperties(reqVo, mrsProductDto);
		mrsProductDto.setCreateTime(new Date());
		mrsProductDto.setId(UUID.randomUUID().toString());
		mrsProductDto.setCustId(custId);
		if(StringUtil.isNEmpty(reqVo.getCredentialsType())){
			mrsProductDto.setCredentialsType(MrsCredentialsType.getEnum(reqVo.getCredentialsType()));
		}
		if(StringUtil.isNEmpty(reqVo.getManagerCerType())){
			mrsProductDto.setManagerCerType(MrsCredentialsType.getEnum(reqVo.getManagerCerType()));
		}
		if(StringUtil.isNEmpty(reqVo.getProductTypeCode())){
			mrsProductDto.setProductTypeCode(EProductTypeEnum.getEnum(reqVo.getProductTypeCode()));
		}
		
		return mrsProductDto;
	}
	/**
	 * 建新一户通客户关系
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 */
	private void saveMrsAccountPlatformCust(ProductRequestVO reqVo, MrsAccountDto retrunMrsAccountDto) {
		MrsAccountPlatformCustDto actPlatformDto = new MrsAccountPlatformCustDto();
		actPlatformDto.setId(actPlatformDto.getIdentity());
		actPlatformDto.setCustId(retrunMrsAccountDto.getCustId());
		actPlatformDto.setSource(reqVo.getPlatformCode());
		actPlatformDto.setPlatformCustCode(reqVo.getPlatformCustCode());
		actPlatformDto.setCreateTime(new Date());
		mrsAccountPlatformCustDtoMapper.insert(actPlatformDto);
	}
	@Override
	public List<MrsProductDto> findBy3ElementAndNoEff(String productName, String credentialsType,
			String credentialsNumber) {
		return mrsProductDtoMapper.findBy3ElementAndNoEff(productName,credentialsType,credentialsNumber);
	}

	@Override
	public List<MrsProductDto> findBy3ElementAndStatus(String productName, String credentialsType,
			String credentialsNumber, String accountStatus) {
		return mrsProductDtoMapper.findBy3ElementAndStatus(productName, credentialsType, credentialsNumber, accountStatus);
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public AccountMsgRespVo updateProductInfo(AccountProductUpdateRequestVO reqVo, MrsAccountDto account,
			MrsPlatformDto mrsPlatformDto) throws CodeCheckedException {

		log.info("一户通账户存在,custId={}", account.getCustId());
		// b1 如果接入平台认证类型为“无需认证”且一户通认证状态为“认证成功”或“无需认证”，
		// 更新“产品客户信息表”产品信息
		MrsProductDto product = new MrsProductDto();
		MrsAccountAuthStatus authStatus = MrsAccountAuthStatus.getEnum(account.getAuthStatus());
		if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())
				&& MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			log.info("场景b1 更新产品信息");
			// 转换数据
			copyProductDto(reqVo, product);
			product.setId(account.getUserInfoId());
			mrsProductDtoMapper.updateByPrimaryKeySelective(product);
		}
		// b2、如果接入平台认证类型为“无需认证”且一户通认证状态为非“认证成功”和非“无需认证”，
		// 更新“产品客户信息表”产品信息。更新“一户通账户表”认证状态为“无需认证”，
		// 如果一户通账户状态为“未生效”，更新账户状态为“正常”。同步客户信息，详见下面
		else if (MrsPlatformIsAuth.IS_AUTH_1.getValue().equals(mrsPlatformDto.getIsAuth())
				&& !MrsAccountAuthStatus.senseAuthStaus(authStatus)) {
			log.info("场景b2 更新产品信息,一户通账户");
			copyProductDto(reqVo, product);
			product.setId(account.getUserInfoId());
			mrsProductDtoMapper.updateByPrimaryKeySelective(product);
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
			// c1、更新子账户信息。根据请求参数中渠道编号查询“子账户配置表”该渠道开通产品客户必须开通的子账户编号，
			// 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户，详见“产品被动开户a4”。
			// 如果子账户的状态为“已注销”，则更改状态为“有效”。
			log.info("场景c1 不更新信息");
			List<MrsConfSubAcctDto> creageSubList = checkAndSaveProduct(reqVo, account, mrsPlatformDto);
			// c2、开通资金账户，详见“产品被动开户a8”。账务系统会判断要求开的资金账户是否已经存在，
			// 如果不存在则会新增，如果存在，则不会再开通。
			log.info("场景c2  开通资金账户");
			createAccounts(account.getCustId(), MrsCustomerType.MCT_2, reqVo.getProductName(), creageSubList);

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
			// 首先，根据通知类型“产品开户通知”和一户通编号查询通知状态为“未通知”和“通知失败”的记录，
			// 如果存在记录，则将查询出的记录状态改为“通知失效”。并创建新的通知记录
			log.info("场景c4 保存同步信息到通知表");
			MrsProductDto productDto = mrsProductDtoMapper.selectByPrimaryKey(account.getUserInfoId());
			saveNotifyInfoProduct(reqVo, productDto);
		}
		return null;
	}
	/**
	 * 保存同步信息到通知表
	 * @param reqVo
	 */
	private void saveNotifyInfoProduct(AccountProductUpdateRequestVO reqVo,MrsProductDto productDto) {
		BeanUtils.copyProperties(reqVo, productDto);
		productDto.setUpdateTime(new Date());
		MrsNotifyDto mrsNotifyDto = generateSyncInfo(productDto, MrsNotifyType.PRODUCT);
		mrsNotifyDtoMapper.insert(mrsNotifyDto);
	}
	/**
	 * 建新一户通客户关系
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 */
	private void saveMrsAccountPlatform(AccountProductUpdateRequestVO reqVo, MrsAccountDto retrunMrsAccountDto) {
		MrsAccountPlatformCustDto actPlatformDto = new MrsAccountPlatformCustDto();
		actPlatformDto.setId(actPlatformDto.getIdentity());
		actPlatformDto.setCustId(retrunMrsAccountDto.getCustId());
		actPlatformDto.setPlatformCustCode(reqVo.getPlatformCustCode());
		actPlatformDto.setSource(reqVo.getPlatformCode());
		actPlatformDto.setCreateTime(new Date());
		mrsAccountPlatformCustDtoMapper.insert(actPlatformDto);
	}
	/**
	 * 根据请求参数中渠道编号查询“子账户配置表”该渠道开通产品客户必须开通的子账户编号，
	 * 判断该记录是否已经存在所有子账户，如果不存在或部分不存在，则开通子账户
	 * @param reqVo
	 * @param retrunMrsAccountDto
	 * @param newPlatform
	 * @return
	 * @throws CodeCheckedException
	 */
	private List<MrsConfSubAcctDto> checkAndSaveProduct(AccountProductUpdateRequestVO reqVo, MrsAccountDto retrunMrsAccountDto,
			MrsPlatformDto newPlatform) throws CodeCheckedException {
	 	List<MrsSubAccountDto> subAccountList = null;
		List<MrsSubAccountDto> subActList = mrsSubAccountDtoMapper.findByCustId(retrunMrsAccountDto.getCustId());
		// 根据客户类型，关联关系查询子账户配置
		List<MrsConfSubAcctDto> mrsConfSubAcctDtos = findByUserTypeAndRationType(MrsCustomerType.MCT_2,
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
	private void copyProductDto(AccountProductUpdateRequestVO reqVo, MrsProductDto mrsProductDto) {
		//必填
    	//mrsProductDto.setCustId(reqVo.getAccountCode());// 一户通编号
    	//mrsProductDto.setProductName(reqVo.getProductName());//产品名称
    	//mrsProductDto.setCredentialsNumber(reqVo.getCredentialsNumber());//证件号码
    	//mrsProductDto.setCredentialsType(credentialsType);//证件类型
    	EProductTypeEnum productTypeCode = StringUtil.isEmpty(reqVo.getProductTypeCode()) ? null : EProductTypeEnum.getEnum(reqVo.getProductTypeCode());
    	mrsProductDto.setProductTypeCode(productTypeCode);//产品类型代码
    	//可为空
    	if (StringUtil.isNEmpty(reqVo.getContactsAddr())) {
    		mrsProductDto.setContactsContactAdd(reqVo.getContactsAddr());
		}
    	if (StringUtil.isNEmpty(reqVo.getContactsEmail())) {
    		mrsProductDto.setContactsEmail(reqVo.getContactsEmail());
		}
    	if (StringUtil.isNEmpty(reqVo.getContactsFax())) {
    		mrsProductDto.setContactsFax(reqVo.getContactsFax());
		}
    	if (StringUtil.isNEmpty(reqVo.getContactsMoblie())) {
    		mrsProductDto.setContactsMobile(reqVo.getContactsMoblie());
		}
    	if (StringUtil.isNEmpty(reqVo.getContactsSpareTel())) {
    		mrsProductDto.setContactsSpareTel(reqVo.getContactsSpareTel());
		}
    	if (StringUtil.isNEmpty(reqVo.getContactsTel())) {
    		mrsProductDto.setContactsTel(reqVo.getContactsTel());
		}
    	if (StringUtil.isNEmpty(reqVo.getContactsZip())) {
    		mrsProductDto.setContactsZip(reqVo.getContactsZip());
		}
    	if (StringUtil.isNEmpty(reqVo.getCustomerCode())) {
    		mrsProductDto.setCustomerCode(reqVo.getCustomerCode());
		}
    	if (StringUtil.isNEmpty(reqVo.getManagerCerCode())) {
    		mrsProductDto.setManagerCerCode(reqVo.getManagerCerCode());
		}
    	MrsCredentialsType managerCerType = StringUtil.isEmpty(reqVo.getManagerCerType()) ? null : MrsCredentialsType.getEnum(reqVo.getManagerCerType());
    	if (StringUtil.isNEmpty(reqVo.getManagerCerType()) || managerCerType != null) {
        	mrsProductDto.setManagerCerType(managerCerType);//资产管理人证件类型
		}
    	if (StringUtil.isNEmpty(reqVo.getManagerName())) {
    		mrsProductDto.setManagerName(reqVo.getManagerName());
		}
    	if (StringUtil.isNEmpty(reqVo.getProductEndDate())) {
    		mrsProductDto.setProductEndDate(reqVo.getProductEndDate()); //产品到期日期
		}
    	if (StringUtil.isNEmpty(reqVo.getProductShortName())) {
    		mrsProductDto.setProductShortName(reqVo.getProductShortName());//产品简称
		}
    	if (StringUtil.isNEmpty(reqVo.getTrusteeCerCode())) {
    		mrsProductDto.setTrusteeCerCode(reqVo.getTrusteeCerCode());//资产托管人证件代码
		}
    	MrsCredentialsType trusteeCerType = StringUtil.isEmpty(reqVo.getTrusteeCerType()) ? null : MrsCredentialsType.getEnum(reqVo.getTrusteeCerType());
    	if (StringUtil.isNEmpty(reqVo.getTrusteeCerType()) || trusteeCerType != null) {
    		mrsProductDto.setTrusteeCerType(trusteeCerType);//资产托管人证件类型
		}
    	if (StringUtil.isNEmpty(reqVo.getTrusteeName())) {
    		mrsProductDto.setTrusteeName(reqVo.getTrusteeName());//资产托管人名称
		}
    	
		mrsProductDto.setUpdateTime(new Date());
	}
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateFileAndSync(MrsProductDto dto) {
		//同步基本信息
		mrsProductDtoMapper.updateByPrimaryKey(dto);
	    
	}
	
	private MrsNotifyDto generateSyncFileInfo(MrsProductDto dto, MrsNotifyType notifyType) {
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
	@Override
	public SaveAduitProductResponseVo removeSaveAduit(MrsRemoveAccountVo removeAccountVo) {
		SaveAduitProductResponseVo respvo = new SaveAduitProductResponseVo();
		MrsProductDto mrsProductDto = mrsProductDtoMapper.findCustId(removeAccountVo.getCustId());
		List<MrsSubAccountDto> mrsSubAccountList = mrsSubAccountDtoMapper.findByCustId(removeAccountVo.getCustId());
		try {
			List<ActAccountDto> actAccountList = actAccountDtoMapper.findListByCustId(removeAccountVo.getCustId());
			//一户通状态为注销  不让修改
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
				respvo.setMsgInfo("产品用户注销操作未进行配置!");
				return respvo;
			}
			mrsAduitInfoDto.setCustId(mrsProductDto.getCustId());
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short) 0);
			mrsAduitInfoDto.setBusiType(EOperaTypeEnum.OP_CANCEL);
			mrsAduitInfoDto.setStartSystem(EStartSystemEnum.SYS_COUNTER);
			mrsAduitInfoDto.setCartNo(mrsProductDto.getCredentialsNumber());
			mrsAduitInfoDto.setCartType(mrsProductDto.getCredentialsType().getValue());
			mrsAduitInfoDto.setName(mrsProductDto.getProductName());
			// 产品客户类型
			mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_02);
			mrsAduitInfoDto.setCreateOperator(removeAccountVo.getCurrentUser().getLoginName());
			mrsAduitInfoDto.setCreateTime(new Date());
			mrsAduitInfoDto.setProductStatus(account.getAccountStatus());
			mrsAduitInfoDto.setProductAuthStatus(account.getAuthStatus());
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAduitInfoDto.setId(UUID.randomUUID().toString());
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
			oldMrsToJson.setMrsProductDto(mrsProductDto);
			oldMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
			oldMrsToJson.setMrsAccountDto(account);
			
			// 新json值
			MrsToJsonDto newMrsToJson = new MrsToJsonDto();
			MrsAccountDto mrsAccountDton = account;
			mrsAccountDton.setCloseTime(new Date());
			mrsAccountDton.setCloseOperator(removeAccountVo.getCurrentUser().getLoginName());
			mrsAccountDton.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_2.getValue());
			newMrsToJson.setActAccountDtos(actAccountList);
			newMrsToJson.setMrsProductDto(mrsProductDto);
			newMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
			newMrsToJson.setMrsAccountDto(mrsAccountDton);
			
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
			respvo.setMsgInfo("产品用户注销申请成功!");
			return respvo;
		} catch (Exception e) {
			e.printStackTrace();
			respvo.setIsSucess(false);
			respvo.setMsgCode("");
			respvo.setMsgInfo("产品用户注销申请失败!");
			return respvo;
		}
	}
	
}
