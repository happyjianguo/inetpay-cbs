package com.ylink.inetpay.cbs.mrs.service;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.JsonObject;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.eu.util.tools.StringUtil;
import com.ylink.inetpay.cbs.act.dao.ActAccountDtoMapper;
import com.ylink.inetpay.cbs.act.dao.ActBusiRefSubDtoMapper;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.constants.MrsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitAttachmentDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitContentDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitInfoDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitPersonDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitPersonHisDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsCertFileDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAcctDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAuditItemDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfSubAcctDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsContactListDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsLoginUserDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsOrganDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPersonDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPortalAccountAduitDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsProductDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsSubAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsSubPayBusiDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserPayPasswordDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsUserPayPasswordHisDtoMapper;
import com.ylink.inetpay.common.core.constant.AaccountType;
import com.ylink.inetpay.common.core.constant.AcctBusiType;
import com.ylink.inetpay.common.core.constant.EAduitTypeEnum;
import com.ylink.inetpay.common.core.constant.ECustomTypeEnum;
import com.ylink.inetpay.common.core.constant.EDistributTypeEnum;
import com.ylink.inetpay.common.core.constant.EOperaTypeEnum;
import com.ylink.inetpay.common.core.constant.EStartSystemEnum;
import com.ylink.inetpay.common.core.constant.EStatusEnum;
import com.ylink.inetpay.common.core.constant.LoginUserIsMain;
import com.ylink.inetpay.common.core.constant.LoginUserStatus;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.core.constant.MrsAccountSource;
import com.ylink.inetpay.common.core.constant.MrsAccountStatus;
import com.ylink.inetpay.common.core.constant.MrsCustTypeEnum;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.core.constant.MrsSubAccountStatus;
import com.ylink.inetpay.common.core.constant.UseAccountType;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.account.app.ActBookAppService;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.exception.AccountUncheckedException;
import com.ylink.inetpay.common.project.account.pojo.AccountSubsPojo;
import com.ylink.inetpay.common.project.cbs.constant.mrs.CreateType;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitAttachmentDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitContentDtoWithBLOBs;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonHisDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditItemDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfSubAcctDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsContactListDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOrganDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalAccountAduitDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsProductDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsToJsonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordHisDto;
import com.ylink.inetpay.common.project.cbs.dto.ucs.sec.UcsSecUserDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.cbs.exception.CbsUncheckedException;
import com.ylink.inetpay.common.project.cbs.exception.ECbsErrorCode;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsAttachmentVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsSubAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitProductResponseVo;

import net.sf.json.JSONObject;

@Service("mrsAduitService")
public class MrsAduitServiceImpl implements MrsAduitService {

	protected static final Logger logger = LoggerFactory.getLogger(MrsAduitServiceImpl.class);
	@Autowired
	private MrsAduitInfoDtoMapper mrsAduitInfoDtoMapper;
	@Autowired
	private MrsAduitPersonHisDtoMapper mrsAduitPersonHisDtoMapper;
	@Autowired
	private MrsAduitPersonDtoMapper mrsAduitPersonDtoMapper;
	@Autowired
	private MrsAduitContentDtoMapper mrsAduitContentDtoMapper;
	@Autowired
	private MrsAccountDtoMapper mrsAccountDtoMapper;
	@Autowired
	private ActBookAppService actBookAppService;
	@Autowired
	private MrsPersonDtoMapper mrsPersonDtoMapper;
	@Autowired
	private MrsSubAccountDtoMapper mrsSubAccountDtoMapper;
	@Autowired
	private MrsContactListDtoMapper mrsContactListDtoMapper;
	@Autowired
	private MrsLoginUserDtoMapper mrsLoginUserDtoMapper;
	@Autowired
	private MrsUserAccountDtoMapper mrsUserAccountDtoMapper;
	@Autowired
	private MrsCertFileDtoMapper mrsCertFileDtoMapper;
	@Autowired
	private MrsPortalAccountAduitDtoMapper mrsPortalAccountAduitDtoMapper;
	@Autowired
	private MrsOrganDtoMapper mrsOrganDtoMapper;
	@Autowired
	private MrsAduitAttachmentDtoMapper mrsAduitAttachmentDtoMapper;
	@Autowired
	private MrsProductDtoMapper mrsProductDtoMapper;
	@Autowired
	private MrsUserPayPasswordDtoMapper mrsUserPayPasswordDtoMapper;
	@Autowired
	private MrsUserPayPasswordHisDtoMapper mrsUserPayPasswordHisDtoMapper;
	@Autowired
	private MrsConfSubAcctDtoMapper mrsConfSubAcctDtoMapper;
	@Autowired
	private ActAccountDtoMapper actAccountDtoMapper;
	@Autowired
	private MrsConfAcctDtoMapper mrsConfAcctDtoMapper;
	@Autowired
	private ActBusiRefSubDtoMapper actBusiRefSubDtoMapper;
	// 一户通操作
	@Autowired
	private MrsAccountService mrsAccountService;
	// 机构操作服务
	@Autowired
	private MrsOrganService mrsOrganService;
	@Autowired
	private MrsConfAuditDtoMapper mrsConfAuditDtoMapper;
	@Autowired
	private MrsConfAuditItemDtoMapper mrsConfAuditItemDtoMapper;
	@Autowired
	private MrsSubAccountService mrsSubAccountService;

	@Override
	public MrsAduitInfoDto selectByPrimaryKey(String id) {
		return mrsAduitInfoDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public void deleteByPrimaryKey(String id) {
		mrsAduitInfoDtoMapper.deleteByPrimaryKey(id);
	}

	@Override
	public PageData<MrsAduitInfoDto> findListPage(PageData<MrsAduitInfoDto> pageData, MrsAduitInfoDto dto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsAduitInfoDto> list = mrsAduitInfoDtoMapper.findListPage(dto);
		Page<MrsAduitInfoDto> page = (Page<MrsAduitInfoDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public PageData<MrsAduitInfoDto> getByCont(PageData<MrsAduitInfoDto> pageData, MrsAduitInfoDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsAduitInfoDto> list = mrsAduitInfoDtoMapper.getByIds(queryParam);
		Page<MrsAduitInfoDto> page = (Page<MrsAduitInfoDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public int insertSelective(MrsAduitPersonHisDto dto) {
		return mrsAduitPersonHisDtoMapper.insertSelective(dto);
	}

	@Override
	public int updateByPrimaryKeySelective(MrsAduitInfoDto mrsAduitInfoDto) {
		return mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
	}

	@Override
	public MrsAduitPersonDto selectByAduitId(String aduitId, String aduitUserId) {
		return mrsAduitPersonDtoMapper.selectByAduitId(aduitId, aduitUserId);
	}

	@Override
	public int updateAduitPerson(MrsAduitPersonDto mrsAduitPersonDto) {
		return mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);
	}

	@Override
	public void updateByPrimaryKey(MrsAduitInfoDto record) {
		mrsAduitInfoDtoMapper.updateByPrimaryKey(record);
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsToJsonDto saveAfAduit(String type, UcsSecUserDto currentUser, MrsAduitInfoDto mrsAduitInfoDto,
			EAduitTypeEnum eAduitTypeEnum, String aduitId, String remark) throws CbsCheckedException {
		logger.info("开始个人审核，当前用户ID是：{}，审核主要信息表ID是：{}", new Object[] { currentUser.getId(), aduitId });
		logger.info("插入审核信息记录表");
		// 插入审核信息记录表
		MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
		mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
		mrsAduitPersonHisDto.setAduitId(aduitId);
		mrsAduitPersonHisDto.setAduitRemark(remark);
		mrsAduitPersonHisDto.setAduitStatus(eAduitTypeEnum);
		mrsAduitPersonHisDto.setAduitTime(new Date());
		mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
		mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
		mrsAduitPersonHisDto.setCreateTime(new Date());
		mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

		logger.info("修改审核人信息表");
		// 修改审核人信息表
		MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(aduitId, currentUser.getId());
		mrsAduitPersonDto.setAduitStatus(eAduitTypeEnum);
		mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

		// 查询审核主要信息表，判断是否为最后一个审核人，若不是，则只改变
		if (mrsAduitInfoDto.getAduitNum() + 1 < mrsAduitInfoDto.getAduitTotal()) {
			// 还不是最后一个审核人，只更新审核主要信息表记录
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_ING);
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
			return null;
		} else {
			mrsAduitInfoDto.setStatus(eAduitTypeEnum);
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_PASS);
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);

			logger.info("审核信息表ID查询审核内容表");
			// 所有信息入库
			// 根据审核信息表ID查询审核内容表
			List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper.selectByAuditId(aduitId);
			if (list == null || list.size() != 1) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
			}
			logger.info("转json");
			String json = list.get(0).getNewValue();
			MrsToJsonDto dto = null;
			try {
				dto = jsonToDto(json);
			} catch (Exception e) {
				e.printStackTrace();
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0088);
			}
			try {
				// 使用未生效一户通时为true 其他时候为默认的false
				boolean flag = false;
				String rsflag = dto.getIsForce();
				// 根据cust_id 和 rsflag 检测是否与开户审核时状态相同
				if (rsflag != null && !(rsflag.trim().equals(""))) {
					if (UseAccountType.USE_01.getValue().equals(rsflag)) {
						// 状态未生效 校验一户通状态是否已经改变，如果已经改变终止审核流程
						MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.selectByCustIdAndStatus(
								dto.getMrsAccountDto().getCustId(), MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
						if (mrsAccountDto == null) {
							throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0098);
						} else {
							flag = true;
						}
					} else if (UseAccountType.USE_02.getValue().equals(rsflag)) {
						MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.selectByCustIdAndStatus(
								dto.getMrsAccountDto().getCustId(), MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
						// 如果手动输入 一户通号码已经存在，报错不能使用！
						if (mrsAccountDto != null) {
							throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0102);
						}
					} else {
						if (dto.getMrsAccountDto() != null) {
							MrsAccountDto mrsAccountDto = mrsAccountDtoMapper
									.findByCustId(dto.getMrsAccountDto().getCustId());
							if (mrsAccountDto != null) {
								throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0099);
							}
						}
					}
				}

				String custId = "";
				String name = "";
				MrsCustomerType custType = null;
				if (MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue().equals(type)) {
					custType = MrsCustomerType.MCT_0;
				} else if (MrsCustTypeEnum.MRS_CUST_TYPE_01.getValue().equals(type)) {
					custType = MrsCustomerType.MCT_1;
				} else if (MrsCustTypeEnum.MRS_CUST_TYPE_02.getValue().equals(type)) {
					custType = MrsCustomerType.MCT_2;
				} else {
					logger.info("审核通过时不知道的客户类型：" + type);
				}
				/** 保存所有数据 */
				// 开立一户通账户cust_id
				// ydx添加
				// if (dto.getMrsAccountDto() == null ||
				// dto.getMrsAccountDto().getCustId() == null
				// || dto.getMrsAccountDto().getCustId().trim().equals("")) {
				if (StringUtils.isBlank(dto.getMrsAccountDto().getCustId())) {
					// ydx 修改
					// AaccountType accountType = AaccountType.BSYHT;
					// if(MrsAccountType.MAT_2.getValue().equals(dto.getMrsAccountDto().getAccountType().getValue())){
					// accountType = AaccountType.CYRYHT;
					// }
					if (custType != null) {
						custId = mrsAccountService.checkCustTypeReturnId(custType,
								dto.getMrsAccountDto().getAccountType());
					} else {
						// 不知道客户类型的情况下，暂时用个人的
						custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
						custId = MrsConstants.PERSON_ACCOUNT_PREFIX + StringUtils.format(11, custId);
					}
					/*
					 * custId = mrsAccountDtoMapper.getMrsPersonSeqVal(); if
					 * (MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue().equals(type)
					 * ) { //个人客户 custId = MrsConstants.PERSON_ACCOUNT_PREFIX +
					 * StringUtils.format(11, custId); } else { //机构客户 custId =
					 * MrsConstants.ORGAN_ACCOUONT_PREFIX +
					 * StringUtils.format(11, custId); }
					 */
				} else {
					custId = dto.getMrsAccountDto().getCustId();
				}

				// 将custId保存至审核信息主表
				if (EStartSystemEnum.SYS_COUNTER.equals(mrsAduitInfoDto.getStartSystem().getValue())) {
					// 柜台端
					mrsAduitInfoDto.setCustId(custId);
					mrsAduitInfoDto.setProductAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
					mrsAduitInfoDto.setProductStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
					mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
				} else {
					// 客户端
					mrsAduitInfoDto.setCustId(custId);
					mrsAduitInfoDto.setProductAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
					mrsAduitInfoDto.setProductStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
					mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
				}

				logger.info("保存附件");
				List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = dto.getMrsAduitAttachmentDtos();
				if (mrsAduitAttachmentDtos != null && mrsAduitAttachmentDtos.size() > 0) {
					// 如果使用未生效的时候 fla为true 删除原来的附件
					if (flag) {
						mrsCertFileDtoMapper.deleteByCustId(custId);
					}
					for (MrsAduitAttachmentDto mrsAduitAttachmentDto : mrsAduitAttachmentDtos) {
						MrsCertFileDto mrsCertFileDto = new MrsCertFileDto();
						mrsCertFileDto.setId(UUID.randomUUID().toString());
						mrsCertFileDto.setCustId(custId);
						mrsCertFileDto.setFileRemark(mrsAduitAttachmentDto.getRemark());
						mrsCertFileDto.setCustomerType(ECustomTypeEnum.CUS_PERSON.getValue());
						mrsCertFileDto.setCertType(mrsAduitAttachmentDto.getCertType());
						mrsCertFileDto.setFileType(mrsAduitAttachmentDto.getSuffix());
						mrsCertFileDto.setFileId(mrsAduitAttachmentDto.getStoragePath());
						mrsCertFileDto.setCreateTime(new Date());
						mrsCertFileDto.setCreateOperator(currentUser.getId());
						if (mrsAduitAttachmentDto.getCheckIsImageBoolean()) {
							mrsCertFileDto.setIsPicFlag("1");// 为图片
						} else {
							mrsCertFileDto.setIsPicFlag("2");// 不为图片
						}
						mrsCertFileDtoMapper.insert(mrsCertFileDto);
					}
				}

				// 是否被动开户，如果是，则只保存附件
				boolean isNative = dto.isOpenUser();
				if (isNative) {
					return null;
				}

				// 柜台端校验
				if (mrsAduitInfoDto.getStartSystem().getValue().equals(EStartSystemEnum.SYS_COUNTER.getValue())
						&& !flag) {
					// 校验 一户通cust_id ，用户的昵称和手机号
					// MrsAccountDto validAccountDto =
					// mrsAccountDtoMapper.findByCustId(custId);
					// if (validAccountDto != null) {
					// throw new
					// CbsUncheckedException(ECbsErrorCode.ADUIT_0099);
					// }
					List<MrsLoginUserDto> userDtoList = dto.getMrsLoginUserDtoList();
					for (MrsLoginUserDto validUserDto : userDtoList) {
						List<MrsLoginUserDto> dtos = mrsLoginUserDtoMapper.findByMobileAndAlias(validUserDto);
						if (dtos.size() > 0) {
							logger.info("审核通过时，用户昵称{" + validUserDto.getAlias() + "}或手机{" + validUserDto.getMobile()
									+ "}已经被使用");
							throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0100);
						}
					}
				}

				logger.info("保存一户通账户表");
				/*
				 * 后续添加 ydx 保存一户通前根据三要素校验一户通是否存在 强制开户标识等
				 * 
				 */
				// 是否达到已经开户最大数量
				boolean isMax = false;
				// 存同步数据的副本
				MrsToJsonDto SyncDto = new MrsToJsonDto();
				MrsAccountDto mrsAccountDto = dto.getMrsAccountDto();
				if (MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue().equals(type)) {
					logger.info("保存个人客户信息表");
					// 保存个人客户信息表
					MrsPersonDto mrsPersonDto = dto.getMrsPersonDto();
					if (!flag) {
						mrsPersonDto.setId(UUID.randomUUID().toString());
						mrsPersonDto.setCustId(custId);
						mrsPersonDto.setCreateTime(new Date());
						mrsPersonDtoMapper.insertSelective(mrsPersonDto);
						SyncDto.setMrsPersonDto(mrsPersonDto);

						/* ydx 添加校验三要素强制开户时是否达到最大开户数量 */
						// 查询一户通信息根据三要素
						List<MrsAccountDto> mrsAccountDtos = mrsAccountDtoMapper.findPersonBy3EleAndStatus(
								mrsPersonDto.getCustomerName(), mrsPersonDto.getCredentialsType(),
								mrsPersonDto.getCredentialsNumber(), MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
						// 如果不是空 查看强制开户标识，如果强制开户标识为空 则抛错 如果强制开户标识有，查询最大开户数
						if (!CollectionUtil.isEmpty(mrsAccountDtos)) {
							if (UseAccountType.USE_02.getValue().equals(rsflag)) {
								isMax = mrsAccountService.check3ElmentIsMax(mrsPersonDto.getCredentialsType(),
										mrsPersonDto.getCredentialsNumber(), mrsPersonDto.getCustomerName(),
										custType.getValue());
								if (!isMax) {
									logger.info("审核通过时，个人三要素名称{" + mrsPersonDto.getCustomerName() + "}证件类型" + "{"
											+ mrsPersonDto.getCredentialsTypeName() + "}证件号码{"
											+ mrsPersonDto.getCredentialsNumber() + "}已经达到最大开户数量。");
									throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0103);
								}
							} else {
								logger.info("审核通过时，个人三要素名称{" + mrsPersonDto.getCustomerName() + "}证件类型" + "{"
										+ mrsPersonDto.getCredentialsTypeName() + "}证件号码{"
										+ mrsPersonDto.getCredentialsNumber() + "}已经存在不为注销的一户通信息。");
								throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0097);
							}
						}

					} else {
						// 使用未生效的
						MrsPersonDto personDto = mrsPersonDtoMapper.selectByPrimaryKey(mrsPersonDto.getId());
						if (personDto != null) {
							mrsPersonDto.setCreateTime(new Date());
							mrsPersonDtoMapper.updateByPrimaryKey(mrsPersonDto);
							SyncDto.setMrsPersonDto(mrsPersonDto);
						} else {
							mrsPersonDto.setId(UUID.randomUUID().toString());
							mrsPersonDto.setCustId(custId);
							mrsPersonDto.setCreateTime(new Date());
							mrsPersonDtoMapper.insertSelective(mrsPersonDto);
							SyncDto.setMrsPersonDto(mrsPersonDto);
						}
					}
					name = mrsPersonDto.getCustomerName();
					// 根据三要素校验一户通信息
				} else if (MrsCustTypeEnum.MRS_CUST_TYPE_01.getValue().equals(type)) {
					// 机构
					logger.info("保存机构客户信息表");
					// 保存机构客户信息表
					MrsOrganDto mrsOrganDto = dto.getMrsOrganDto();
					if (!flag) {
						mrsOrganDto.setId(UUID.randomUUID().toString());
						mrsOrganDto.setCustId(custId);
						mrsOrganDto.setCreateTime(new Date());
						mrsOrganDto.setCreateOperator(currentUser.getId());
						mrsOrganDtoMapper.insert(mrsOrganDto);
						SyncDto.setMrsOrganDto(mrsOrganDto);

						/* ydx 添加校验三要素强制开户时是否达到最大开户数量 */
						// 查询一户通信息根据三要素
						List<MrsOrganDto> mrsOrganDtos = mrsOrganService.findBy3ElementNoEff(
								mrsOrganDto.getCustomerName(), mrsOrganDto.getSocialCreditCode(),
								mrsOrganDto.getOrganizeCode(), mrsOrganDto.getRevenueCode(),
								mrsOrganDto.getBusinessLicence(), mrsOrganDto.getOrganOtherCode());
						// 如果不是空 查看强制开户标识，如果强制开户标识为空 则抛错 如果强制开户标识有，查询最大开户数
						if (!CollectionUtil.isEmpty(mrsOrganDtos)) {
							if (UseAccountType.USE_02.getValue().equals(rsflag)) {

								isMax = mrsOrganService.checkOrgan3ElmentIsMax(mrsOrganDto.getCustomerName(),
										mrsOrganDto.getSocialCreditCode(), mrsOrganDto.getOrganizeCode(),
										mrsOrganDto.getRevenueCode(), mrsOrganDto.getBusinessLicence(),
										mrsOrganDto.getOrganOtherCode());
								if (!isMax) {
									logger.info("审核通过时，机构名称{" + mrsOrganDto.getCustomerName() + "}组织机构号" + "{"
											+ mrsOrganDto.getOrganizeCode() + "}营业执照{"
											+ mrsOrganDto.getBusinessLicence() + "" + "税务登记证{"
											+ mrsOrganDto.getRevenueCode() + "}社会统一信用代码" + "{"
											+ mrsOrganDto.getSocialCreditCode() + "}其他证件号{"
											+ mrsOrganDto.getOrganOtherCode() + "" + "}已经达到最大开户数量。");
									throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0103);
								}
							} else {
								logger.info("审核通过时，机构名称{" + mrsOrganDto.getCustomerName() + "}组织机构号" + "{"
										+ mrsOrganDto.getOrganizeCode() + "}营业执照{" + mrsOrganDto.getBusinessLicence()
										+ "" + "税务登记证{" + mrsOrganDto.getRevenueCode() + "}社会统一信用代码" + "{"
										+ mrsOrganDto.getSocialCreditCode() + "}其他证件号{"
										+ mrsOrganDto.getOrganOtherCode() + "" + "}已经存在不为注销的一户通号码，审核流程结束！。");
								throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0097);
							}
						}
						//0524 修改机构商户号未初始化的一户通号码
						mrsAccountDto.setExtOrgId(custId);
					} else {
						// 使用未生效的
						MrsOrganDto organDto = mrsOrganDtoMapper.selectByPrimaryKey(mrsOrganDto.getId());
						if (organDto != null) {
							mrsOrganDto.setCreateTime(new Date());
							mrsOrganDto.setCreateOperator(currentUser.getId());
							mrsOrganDtoMapper.updateByPrimaryKey(mrsOrganDto);
							SyncDto.setMrsOrganDto(mrsOrganDto);
						} else {
							mrsOrganDto.setId(UUID.randomUUID().toString());
							mrsOrganDto.setCustId(custId);
							mrsOrganDto.setCreateTime(new Date());
							mrsOrganDto.setCreateOperator(currentUser.getId());
							mrsOrganDtoMapper.insert(mrsOrganDto);
							SyncDto.setMrsOrganDto(mrsOrganDto);
						}
					}
					name = mrsOrganDto.getCustomerName();

				} else if (MrsCustTypeEnum.MRS_CUST_TYPE_02.getValue().equals(type)) {
					// 产品
					logger.info("保存产品客户信息表");
					// 保存产品客户信息表
					MrsProductDto mrsProductDto = dto.getMrsProductDto();
					if (!flag) {
						mrsProductDto.setId(UUID.randomUUID().toString());
						mrsProductDto.setCustId(custId);
						mrsProductDto.setCreateTime(new Date());
						mrsProductDto.setCreateOperator(currentUser.getId());
						mrsProductDtoMapper.insert(mrsProductDto);
						SyncDto.setMrsProductDto(mrsProductDto);

						/* ydx 添加校验三要素强制开户时是否达到最大开户数量 */
						// 查询一户通信息根据三要素
						List<MrsProductDto> mrsProductDtos = mrsProductDtoMapper.findBy3ElementAndNoEff(
								mrsProductDto.getProductName(), mrsProductDto.getCredentialsType().getValue(),
								mrsProductDto.getCredentialsNumber());
						// 如果不是空 查看强制开户标识，如果强制开户标识为空 则抛错 如果强制开户标识有，查询最大开户数
						if (!CollectionUtil.isEmpty(mrsProductDtos)) {
							if (UseAccountType.USE_02.getValue().equals(rsflag)) {
								isMax = mrsAccountService.check3ElmentIsMax(
										mrsProductDto.getCredentialsType().getValue(),
										mrsProductDto.getCredentialsNumber(), mrsProductDto.getProductName(),
										custType.getValue());
								if (!isMax) {
									logger.info("审核通过时，个人三要素名称{" + mrsProductDto.getProductName() + "}证件类型" + "{"
											+ mrsProductDto.getCredentialsType() + "}证件号码{"
											+ mrsProductDto.getCredentialsNumber() + "}已经达到最大开户数量。");
									throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0103);
								}
							} else {
								logger.info("审核通过时，个人三要素名称{" + mrsProductDto.getProductName() + "}证件类型" + "{"
										+ mrsProductDto.getCredentialsType() + "}证件号码{"
										+ mrsProductDto.getCredentialsNumber() + "}已经存在有效的一户通信息。");
								throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0097);
							}

						}

					} else {
						// 使用未生效的
						MrsProductDto productDto = null;
						if (!StringUtil.isEmpty(mrsProductDto.getId())) {
							productDto = mrsProductDtoMapper.selectByPrimaryKey(mrsProductDto.getId());
						} else {
							productDto = mrsProductDtoMapper.findCustId(mrsProductDto.getCustId());
						}
						if (productDto != null) {
							mrsProductDto.setCreateTime(new Date());
							mrsProductDto.setCreateOperator(currentUser.getId());
							mrsProductDtoMapper.updateByPrimaryKey(mrsProductDto);
							SyncDto.setMrsProductDto(mrsProductDto);
						} else {
							mrsProductDto.setId(UUID.randomUUID().toString());
							mrsProductDto.setCustId(custId);
							mrsProductDto.setCreateTime(new Date());
							mrsProductDto.setCreateOperator(currentUser.getId());
							mrsProductDtoMapper.insert(mrsProductDto);
							SyncDto.setMrsProductDto(mrsProductDto);
						}
					}
					name = mrsProductDto.getProductName();
				}

				// 保存一户通账户表
				if (!flag) {
					if (mrsAccountDto != null) {
						mrsAccountDto.setId(UUID.randomUUID().toString());
						mrsAccountDto.setCustId(custId);
						mrsAccountDto.setAccountName(name);
						mrsAccountDto.setIsDelete("N");
						mrsAccountDto.setCreateTime(new Date());
						mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
						mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());// 账户状态设置为正常
						mrsAccountDtoMapper.insertSelective(mrsAccountDto);
					} else {
						mrsAccountDto = new MrsAccountDto();
						mrsAccountDto.setId(UUID.randomUUID().toString());
						mrsAccountDto.setAccountType(AaccountType.BSYHT);
						mrsAccountDto.setCustomerType("0");
						mrsAccountDto.setPlatformCode("gscore");
						mrsAccountDto.setAccountSource(MrsAccountSource.SOURCE_05.getValue());
						mrsAccountDto.setCustId(custId);
						mrsAccountDto.setAccountName(name);
						mrsAccountDto.setIsDelete("N");
						mrsAccountDto.setCreateTime(new Date());
						mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_0.getValue());
						mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());// 账户状态设置为正常
						mrsAccountDtoMapper.insertSelective(mrsAccountDto);
					}
				} else {
					// 使用未生效的
					// 若ID不为空
					if (!StringUtil.isEmpty(mrsAccountDto.getId())) {
						mrsAccountDto.setIsDelete("N");
						mrsAccountDto.setCreateTime(new Date());
						mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
						mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());// 账户状态设置为正常
						// mrsAccountDtoMapper.updateByPrimaryKey(mrsAccountDto);
						mrsAccountDtoMapper.updateByPrimaryKeySelective(mrsAccountDto);
					} else {
						MrsAccountDto accountDto = mrsAccountDtoMapper.findByCustId(mrsAccountDto.getCustId());
						accountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
						accountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());// 账户状态设置为正常
						accountDto.setUpdateTime(new Date());
						accountDto.setIsDelete("N");
						mrsAccountDtoMapper.updateByPrimaryKeySelective(accountDto);
					}
				}
				// 开资金账户
				actBookAppService.createAccounts(custId, name, dto.getAccountSubsPojos());

				logger.info("保存子账户信息表");
				// 保存子账户信息表
				List<MrsSubAccountDto> mrsSubAccountDtoList = dto.getMrsSubAccountDtoList();
				if (!flag) {
					if (mrsSubAccountDtoList != null && mrsSubAccountDtoList.size() > 0) {
						for (MrsSubAccountDto subAccountDto : mrsSubAccountDtoList) {
							subAccountDto.setId(UUID.randomUUID().toString());
							subAccountDto.setCustId(custId);
							if (subAccountDto.getPlatformCode() == null
									|| subAccountDto.getPlatformCode().trim().equals("")) {
								subAccountDto.setPlatformCode("gscore");
							}
							subAccountDto.setCreateTime(new Date());
							subAccountDto.setSubAccountCode(subAccountDto.getSubAccountType() + "0" + custId);
							subAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());// 子账户状态设置为有效
							mrsSubAccountDtoMapper.insertSelective(subAccountDto);
						}
					}
				} else {
					// 使用未生效的子账户信息
					if (mrsSubAccountDtoList != null && mrsSubAccountDtoList.size() > 0) {
						for (MrsSubAccountDto subAccountDto : mrsSubAccountDtoList) {
							MrsSubAccountDto mrsSubAccountDto = mrsSubAccountDtoMapper
									.selectByPrimaryKey(subAccountDto.getId());
							if (mrsSubAccountDto != null) {
								subAccountDto.setCreateTime(new Date());
								subAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());// 子账户状态设置为有效
								mrsSubAccountDtoMapper.updateByPrimaryKey(subAccountDto);
							} else {
								subAccountDto.setId(UUID.randomUUID().toString());
								subAccountDto.setCustId(custId);
								if (subAccountDto.getPlatformCode() == null
										|| subAccountDto.getPlatformCode().trim().equals("")) {
									subAccountDto.setPlatformCode("gscore");
								}
								subAccountDto.setSubAccountCode(subAccountDto.getSubAccountType() + "0" + custId);
								subAccountDto.setCreateTime(new Date());
								subAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());// 子账户状态设置为有效
								mrsSubAccountDtoMapper.insertSelective(subAccountDto);
							}
						}
					}
				}

				logger.info("保存联系人");
				// 保存联系人
				List<MrsContactListDto> mrsContactListDtos = dto.getMrsContactListDtos();
				if (!flag) {
					if (mrsContactListDtos != null && mrsContactListDtos.size() > 0) {
						for (MrsContactListDto mrsContactListDto : mrsContactListDtos) {
							mrsContactListDto.setId(UUID.randomUUID().toString());
							mrsContactListDto.setCustId(custId);
							mrsContactListDto.setCreateTime(new Date());
							mrsContactListDto.setStatus(EStatusEnum.EFFECTIVE.getValue());
							mrsContactListDtoMapper.insert(mrsContactListDto);
						}
					}
				} else {
					// 使用未生效的
					// 先将现有的标记为删除状态 ydx修改
					List<MrsContactListDto> contacts = mrsContactListDtoMapper.findByCustId(custId);
					if (!CollectionUtil.isEmpty(contacts)) {
						for (MrsContactListDto contact : contacts) {
							contact.setStatus(EStatusEnum.UNEFFECTIVE.getValue());
							mrsContactListDtoMapper.updateByPrimaryKey(contact);
						}
					}
					// 修改联系人信息
					if (mrsContactListDtos != null && mrsContactListDtos.size() > 0) {
						for (MrsContactListDto mrsContactListDto : mrsContactListDtos) {
							MrsContactListDto contactListDto = mrsContactListDtoMapper
									.selectByPrimaryKey(mrsContactListDto.getId());
							if (contactListDto != null) {
								contactListDto.setName(mrsContactListDto.getName());
								contactListDto.setPhoneNo(mrsContactListDto.getPhoneNo());
								contactListDto.setMobill(mrsContactListDto.getMobill());
								contactListDto.setEmail(mrsContactListDto.getEmail());
								contactListDto.setCertType(mrsContactListDto.getCertType());
								contactListDto.setCertNo(mrsContactListDto.getCertNo());
								contactListDto.setAddress(mrsContactListDto.getAddress());
								contactListDto.setStatus(EStatusEnum.EFFECTIVE.getValue());
								mrsContactListDtoMapper.updateByPrimaryKey(contactListDto);
							} else {
								mrsContactListDto.setId(UUID.randomUUID().toString());
								mrsContactListDto.setCustId(custId);
								mrsContactListDto.setCreateTime(new Date());
								mrsContactListDto.setStatus(EStatusEnum.EFFECTIVE.getValue());
								mrsContactListDtoMapper.insert(mrsContactListDto);
							}
						}
					}
				}

				MrsUserAccountDto mrsUserAccountDto = new MrsUserAccountDto();
				logger.info("保存用户信息表");
				// 保存用户信息表
				List<MrsLoginUserDto> mrsLoginUserDtoList = dto.getMrsLoginUserDtoList();
				if (!flag) {
					if (mrsLoginUserDtoList != null && mrsLoginUserDtoList.size() > 0) {
						for (MrsLoginUserDto mrsLoginUserDto : mrsLoginUserDtoList) {
							if (mrsLoginUserDto.getId() != null && !(mrsLoginUserDto.getId().trim().equals(""))) {
								mrsLoginUserDto = mrsLoginUserDtoMapper.selectByPrimaryKey(mrsLoginUserDto.getId());
							} else {
								mrsLoginUserDto.setId(UUID.randomUUID().toString());
								mrsLoginUserDto.setCustId(custId);
								mrsLoginUserDto.setCreateTime(new Date());
								mrsLoginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());// 用户状态设置为有效
								mrsLoginUserDtoMapper.insertSelective(mrsLoginUserDto);
							}

							logger.info("插入账户登陆用户关联信息表");
							// 插入账户登陆用户关联信息表TB_MRS_USER_ACCOUNT
							mrsUserAccountDto.setId(UUID.randomUUID().toString());
							mrsUserAccountDto.setCreateTime(new Date());
							if (mrsLoginUserDto.getIsMain() == null || mrsLoginUserDto.getIsMain().trim().equals("")) {
								mrsUserAccountDto.setIsMain(LoginUserIsMain.IS_MAIN_1.getValue());
							} else {
								mrsUserAccountDto.setIsMain(LoginUserIsMain.IS_MAIN_0.getValue());
							}
							mrsUserAccountDto.setLoginId(mrsLoginUserDto.getId());
							mrsUserAccountDto.setAccountId(mrsAccountDto.getId());
							mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);
						}
					}
				} else {
					// 使用未生效的
					if (mrsLoginUserDtoList != null && mrsLoginUserDtoList.size() > 0) {
						for (MrsLoginUserDto mrsLoginUserDto : mrsLoginUserDtoList) {
							MrsLoginUserDto loginUserDto = mrsLoginUserDtoMapper
									.selectByPrimaryKey(mrsLoginUserDto.getId());
							if (loginUserDto != null) {
								mrsLoginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());// 用户状态设置为有效
								// mrsLoginUserDtoMapper.updateByPrimaryKey(mrsLoginUserDto);
								mrsLoginUserDtoMapper.updateByPrimaryKeySelective(mrsLoginUserDto);
								// logger.info("修改账户登陆用户关联信息表");
							} else {
								mrsLoginUserDto.setId(UUID.randomUUID().toString());
								mrsLoginUserDto.setCustId(custId);
								mrsLoginUserDto.setCreateTime(new Date());
								mrsLoginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());// 用户状态设置为有效
								mrsLoginUserDtoMapper.insertSelective(mrsLoginUserDto);

								logger.info("插入账户登陆用户关联信息表");
								mrsUserAccountDto.setId(UUID.randomUUID().toString());
								mrsUserAccountDto.setCreateTime(new Date());
								mrsUserAccountDto.setIsMain(LoginUserIsMain.IS_MAIN_0.getValue());
								mrsUserAccountDto.setLoginId(mrsLoginUserDto.getId());
								mrsUserAccountDto.setAccountId(mrsAccountDto.getId());
								mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);
							}

							//
							// 插入账户登陆用户关联信息表TB_MRS_USER_ACCOUNT
						}
					}
				}

				MrsUserPayPasswordDto mrsUserPayPasswordDto = dto.getMrsUserPayPasswordDto();
				if (mrsUserPayPasswordDto != null) {
					logger.info("保存支付密码");
					if (!flag) {
						mrsUserPayPasswordDto.setId(UUID.randomUUID().toString());
						mrsUserPayPasswordDto.setCustId(custId);
						mrsUserPayPasswordDto.setErrorNum((short) 0);
						mrsUserPayPasswordDto.setCreateTime(new Date());
						mrsUserPayPasswordDtoMapper.insert(mrsUserPayPasswordDto);
					} else {
						MrsUserPayPasswordDto passwordDto = mrsUserPayPasswordDtoMapper
								.selectByPrimaryKey(mrsUserPayPasswordDto.getId());
						if (passwordDto != null) {
							mrsUserPayPasswordDto.setErrorNum((short) 0);
							mrsUserPayPasswordDto.setCreateTime(new Date());
							mrsUserPayPasswordDtoMapper.updateByPrimaryKey(mrsUserPayPasswordDto);
						} else {
							mrsUserPayPasswordDto.setId(UUID.randomUUID().toString());
							mrsUserPayPasswordDto.setCustId(custId);
							mrsUserPayPasswordDto.setErrorNum((short) 0);
							mrsUserPayPasswordDto.setCreateTime(new Date());
							mrsUserPayPasswordDtoMapper.insert(mrsUserPayPasswordDto);
						}
					}

					logger.info("保存支付密码记录表");
					MrsUserPayPasswordHisDto mrsUserPayPasswordHisDto = new MrsUserPayPasswordHisDto();
					mrsUserPayPasswordHisDto.setId(UUID.randomUUID().toString());
					mrsUserPayPasswordHisDto.setCustId(custId);
					mrsUserPayPasswordHisDto.setPwdId(mrsUserPayPasswordDto.getId());
					mrsUserPayPasswordHisDto.setOldPassword("");
					mrsUserPayPasswordHisDto.setNewPassword(mrsUserPayPasswordDto.getPassword());
					mrsUserPayPasswordHisDto.setCreateTime(new Date());
					mrsUserPayPasswordHisDto.setCreateType(CreateType.STRAT.getValue());
					mrsUserPayPasswordHisDto.setCreateIp(currentUser.getId());
					mrsUserPayPasswordHisDto.setCreateOperator(currentUser.getLoginName());
					mrsUserPayPasswordHisDtoMapper.insert(mrsUserPayPasswordHisDto);
				}

				return SyncDto;

			} catch (Exception e) {
				e.printStackTrace();
				logger.info(ExceptionProcUtil.getExceptionDesc(e));
				// 自定义异常
				if (e instanceof CbsUncheckedException) {
					throw new CbsUncheckedException(ECbsErrorCode.getByDisplay(e.getMessage()));
				} else if (e instanceof AccountUncheckedException) {
					// 开资金账户异常
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0105);
				}
				// 其他异常
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0095);
			}
		}
	}

	/**
	 * 审核拒绝
	 */
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void rbAduit(UcsSecUserDto currentUser, MrsAduitInfoDto mrsAduitInfoDto, String aduitId, String remark) {
		// 插入审核信息记录表
		try {
			MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
			mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
			mrsAduitPersonHisDto.setAduitId(aduitId);
			mrsAduitPersonHisDto.setAduitRemark(remark);
			mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_REJUST);// 审核拒绝
			mrsAduitPersonHisDto.setAduitTime(new Date());
			mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
			mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
			mrsAduitPersonHisDto.setCreateTime(new Date());
			mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

			// 修改审核主要信息表
			// 根据审核信息表ID查询审核信息表
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_REJUST);
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDto.setRemark(remark);
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);

			// 修改审核人信息表
			MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(aduitId, currentUser.getId());
			mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_REJUST);
			mrsAduitPersonDto.setRemark(remark);
			mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);
            //删除临时数据
			mrsAduitContentDtoMapper.deleteByAduitId(mrsAduitInfoDto.getCustId());
			
			// 开户审核拒绝修改客户端开通一户通信息审核关联表
			if (mrsAduitInfoDto.getBusiType().getValue().equals(EOperaTypeEnum.OP_OPEN.getValue())) {
				MrsPortalAccountAduitDto mrsPortalAccountAduitDto = mrsPortalAccountAduitDtoMapper
						.selectByAduitId(aduitId);
				if (mrsPortalAccountAduitDto != null) {
					mrsPortalAccountAduitDto.setStatus(EStatusEnum.UNEFFECTIVE.getValue());
					mrsPortalAccountAduitDtoMapper.updateByPrimaryKey(mrsPortalAccountAduitDto);
				}
			}

		} catch (Exception e) {
			ExceptionProcUtil.getExceptionDesc(e);
			throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0095);
		}

	}

	/**
	 * json转对象
	 */
	@SuppressWarnings("rawtypes")
	public MrsToJsonDto jsonToDto(String json) {
		// 2个地方 MrsAduitServiceImpl 200行
		JSONObject obj = JSONObject.fromObject(json);// 将json字符串转换为json对象
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("mrsSubAccountDtoList", MrsSubAccountDto.class);
		classMap.put("mrsContactListDtos", MrsContactListDto.class);
		classMap.put("accountSubsPojos", AccountSubsPojo.class);
		classMap.put("mrsAduitAttachmentDtos", MrsAduitAttachmentDto.class);
		classMap.put("mrsLoginUserDtoList", MrsLoginUserDto.class);
		classMap.put("mrsCertFileDtos", MrsCertFileDto.class);
		classMap.put("actAccountDtos", ActAccountDto.class);
		return (MrsToJsonDto) JSONObject.toBean(obj, MrsToJsonDto.class, classMap);
	}

	@Override
	public List<String> findMrsAduitAttachmentDtoByAduitId(String aduitId) {
		return mrsAduitAttachmentDtoMapper.findMrsAduitAttachmentDtoByAduitId(aduitId);
	}

	@Override
	public List<MrsAttachmentVo> findFiled(String aduitId, List<MrsAduitAttachmentDto> mrsAduitAttachmentDtoList) {
		List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper.selectByAuditId(aduitId);
		String id = list.get(0).getId();
		List<String> certTypeList = mrsAduitAttachmentDtoMapper.findMrsAduitAttachmentDtoByAduitId(id);
		List<MrsAttachmentVo> voList = new ArrayList<>();
		for (String certType : certTypeList) {
			MrsAttachmentVo vo = new MrsAttachmentVo();
			vo.setCertType(certType);
			List<MrsAduitAttachmentDto> path = new ArrayList<MrsAduitAttachmentDto>();
			for (MrsAduitAttachmentDto mrsAduitAttachmentDto : mrsAduitAttachmentDtoList) {
				if (certType.equals(mrsAduitAttachmentDto.getCertType())) {
					path.add(mrsAduitAttachmentDto);
					vo.setRemark(mrsAduitAttachmentDto.getRemark());
				}
			}
			vo.setPath(path);
			voList.add(vo);
		}

		return voList;
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsToJsonDto approveAduit(MrsAduitInfoDto mrsAduitInfoDto, MrsAccountAuthStatus mrsAccountAuthStatus,
			UcsSecUserDto currentUser, String remark) throws CbsCheckedException {
		try {

			if (mrsAduitInfoDto.getProductAuthStatus().equals(MrsAccountAuthStatus.MAAS_2.getValue())
					|| mrsAduitInfoDto.getProductAuthStatus().equals(MrsAccountAuthStatus.MAAS_3.getValue())) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0101);
			}

			logger.info("开始个人审核，当前用户ID是：{}，审核主要信息表ID是：{}",
					new Object[] { currentUser.getId(), mrsAduitInfoDto.getId() });
			logger.info("插入审核信息记录表");
			// 插入审核信息记录表
			MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
			mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
			mrsAduitPersonHisDto.setAduitId(mrsAduitInfoDto.getId());
			mrsAduitPersonHisDto.setAduitRemark(remark);
			if (mrsAccountAuthStatus.getValue().equals(MrsAccountAuthStatus.MAAS_2.getValue())) {
				mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
			} else {
				mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_REJUST);
			}
			mrsAduitPersonHisDto.setAduitTime(new Date());
			mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
			mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
			mrsAduitPersonHisDto.setCreateTime(new Date());
			mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

			logger.info("修改审核人信息表");
			// 修改审核人信息表
			MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(mrsAduitInfoDto.getId(),
					currentUser.getId());
			if (mrsAccountAuthStatus.getValue().equals(MrsAccountAuthStatus.MAAS_2.getValue())) {
				mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
			} else {
				mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_REJUST);
			}
			mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

			// 查询审核主要信息表，判断是否为最后一个审核人，若不是，则只改变
			// 若是审核成功
			if (mrsAccountAuthStatus.getValue().equals(MrsAccountAuthStatus.MAAS_2.getValue())) {
				if (mrsAduitInfoDto.getAduitNum() + 1 < mrsAduitInfoDto.getAduitTotal()) {
					// 还不是最后一个审核人，只更新审核主要信息表记录
					mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
					mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_ING);
					mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
					return null;
				} else {
					mrsAduitInfoDto.setProductAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
					mrsAduitInfoDto.setProductStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
					mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
					mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_PASS);
					mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
					// 更新一户通状态
					MrsAccountDto dto = mrsAccountDtoMapper.findByCustId(mrsAduitInfoDto.getCustId());
					dto.setAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
					dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
					mrsAccountDtoMapper.updateByPrimaryKey(dto);
					// 更新子账户状态
					List<MrsSubAccountDto> subAccountDtoList = mrsSubAccountDtoMapper
							.findByCustId(mrsAduitInfoDto.getCustId());
					if (!subAccountDtoList.isEmpty() && subAccountDtoList != null) {
						for (MrsSubAccountDto subAccountDto : subAccountDtoList) {
							subAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());
							mrsSubAccountDtoMapper.updateByPrimaryKeySelective(subAccountDto);
						}
					}

					// 存同步数据的副本
					MrsToJsonDto SyncDto = new MrsToJsonDto();
					logger.info("保存个人客户信息表");
					// 个人客户
					if (mrsAduitInfoDto.getCustType().getValue().equals(MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue())) {
						// 保存个人客户信息表
						MrsPersonDto mrsPersonDto = mrsPersonDtoMapper.findByCustId(mrsAduitInfoDto.getCustId());
						SyncDto.setMrsPersonDto(mrsPersonDto);
					} else if (mrsAduitInfoDto.getCustType().getValue()
							.equals(MrsCustTypeEnum.MRS_CUST_TYPE_01.getValue())) {
						// 保存机构客户信息
						MrsOrganDto mrsOrganDto = mrsOrganDtoMapper.findByCustId(mrsAduitInfoDto.getCustId());
						SyncDto.setMrsOrganDto(mrsOrganDto);
					}
					// 保存附件信息
					List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper
							.selectByAuditId(mrsAduitInfoDto.getId());
					if (list == null || list.size() != 1) {
						throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
					}
					logger.info("转json");
					String json = list.get(0).getNewValue();
					MrsToJsonDto jsonDto = null;
					try {
						jsonDto = jsonToDto(json);
					} catch (Exception e) {
						e.printStackTrace();
						throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0088);
					}
					List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = jsonDto.getMrsAduitAttachmentDtos();
					List<MrsCertFileDto> mrsCertFileDtoList = new ArrayList<MrsCertFileDto>();
					if (mrsAduitAttachmentDtos != null && mrsAduitAttachmentDtos.size() > 0) {
						for (MrsAduitAttachmentDto mrsAduitAttachmentDto : mrsAduitAttachmentDtos) {
							MrsCertFileDto mrsCertFileDto = new MrsCertFileDto();
							mrsCertFileDto.setId(UUID.randomUUID().toString());
							mrsCertFileDto.setCustId(mrsAduitInfoDto.getCustId());
							mrsCertFileDto.setFileRemark(mrsAduitAttachmentDto.getRemark());
							mrsCertFileDto.setCustomerType(ECustomTypeEnum.CUS_PERSON.getValue());
							mrsCertFileDto.setCertType(mrsAduitAttachmentDto.getCertType());
							mrsCertFileDto.setFileType(mrsAduitAttachmentDto.getSuffix());
							mrsCertFileDto.setFileId(mrsAduitAttachmentDto.getStoragePath());
							mrsCertFileDto.setCreateTime(new Date());
							mrsCertFileDto.setCreateOperator(currentUser.getId());
							if (mrsAduitAttachmentDto.getCheckIsImageBoolean()) {
								mrsCertFileDto.setIsPicFlag("1");// 为图片
							} else {
								mrsCertFileDto.setIsPicFlag("2");// 不为图片
							}
							mrsCertFileDtoList.add(mrsCertFileDto);
							mrsCertFileDtoMapper.insert(mrsCertFileDto);
						}
						SyncDto.setMrsCertFileDtos(mrsCertFileDtoList);
					}
					return SyncDto;
				}
				// 若是审核拒绝
			} else {
				mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
				mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_REJUST);
				mrsAduitInfoDto.setProductAuthStatus(MrsAccountAuthStatus.MAAS_3.getValue());
				mrsAduitInfoDto.setProductStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
				mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
				// 若一户通账户不为空,修改一户通状态
				if (mrsAduitInfoDto.getCustId() != null) {
					MrsAccountDto dto = mrsAccountDtoMapper.findByCustId(mrsAduitInfoDto.getCustId());
					dto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_9.getValue());
					dto.setAuthStatus(MrsAccountAuthStatus.MAAS_3.getValue());
					mrsAccountDtoMapper.updateByPrimaryKeySelective(dto);
				}
				MrsPortalAccountAduitDto mrsPortalAccountAduitDto = mrsPortalAccountAduitDtoMapper
						.selectByAduitId(mrsAduitInfoDto.getId());
				mrsPortalAccountAduitDto.setStatus(EStatusEnum.UNEFFECTIVE.getValue());
				mrsPortalAccountAduitDto.setUpdateTime(new Date());
				mrsPortalAccountAduitDtoMapper.updateByPrimaryKeySelective(mrsPortalAccountAduitDto);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info(ExceptionProcUtil.getExceptionDesc(e));
			if (e instanceof CbsUncheckedException) {
				throw new CbsUncheckedException(ECbsErrorCode.getByDisplay(e.getMessage()));
			}
			throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0095);
		}
		return null;
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsToJsonDto updateAfAduit(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsUncheckedException {
		logger.info("开始信息变更审核，当前用户ID是：{}，审核主要信息表ID是：{}", new Object[] { currentUser.getId(), id });
		logger.info("插入审核信息记录表");
		// 插入审核信息记录表
		MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
		mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
		mrsAduitPersonHisDto.setAduitId(id);
		mrsAduitPersonHisDto.setAduitRemark(remark);
		mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonHisDto.setAduitTime(new Date());
		mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
		mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
		mrsAduitPersonHisDto.setCreateTime(new Date());
		mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

		logger.info("修改审核人信息表");
		// 修改审核人信息表
		MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(id, currentUser.getId());
		mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

		// 查询审核主要信息表，判断是否为最后一个审核人，若不是，则只改变
		if (mrsAduitInfoDto.getAduitNum() + 1 < mrsAduitInfoDto.getAduitTotal()) {
			// 还不是最后一个审核人，只更新审核主要信息表记录
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_ING);
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
			return null;
		} else {
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_PASS);
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);

			List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper.selectByAuditId(id);
			if (list == null || list.size() != 1) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
			}

			String json = list.get(0).getNewValue();
			String oldjson = list.get(0).getOldValue();
			MrsToJsonDto dto = null;
			MrsToJsonDto oldDto = null;
			try {
				// json转换成对象
				dto = jsonToDto(json);
				oldDto = jsonToDto(oldjson);
			} catch (Exception e) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0088);
			}

			if (MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue().equals(type)) {
				MrsPersonDto mrsPersonDtoById = mrsPersonDtoMapper.selectByPrimaryKey(oldDto.getMrsPersonDto().getId());
				int mrsPersonDtoSize = equalsObj(mrsPersonDtoById, oldDto.getMrsPersonDto());
				if (mrsPersonDtoSize != 0) {
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0089);
				}
			} else if (MrsCustTypeEnum.MRS_CUST_TYPE_01.getValue().equals(type)) {
				MrsOrganDto mrsOrganDtoById = mrsOrganDtoMapper.selectByPrimaryKey(oldDto.getMrsOrganDto().getId());
				int mrsOrganDtoSize = equalsObj(mrsOrganDtoById, oldDto.getMrsOrganDto());
				if (mrsOrganDtoSize != 0) {
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0090);
				}
			} else if (MrsCustTypeEnum.MRS_CUST_TYPE_02.getValue().equals(type)) {
				MrsProductDto mrsProductDtoById = mrsProductDtoMapper
						.selectByPrimaryKey(oldDto.getMrsProductDto().getId());
				int mrsProductDtoSize = equalsObj(mrsProductDtoById, oldDto.getMrsProductDto());
				if (mrsProductDtoSize != 0) {
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0091);
				}
			}

			MrsAccountDto mrsAccountDtoById = mrsAccountDtoMapper.selectByPrimaryKey(oldDto.getMrsAccountDto().getId());
			String custCode = mrsAccountDtoById.getCustomerCode();
			// 0524  修改商户编码，不做校验
			String extOrgId = mrsAccountDtoById.getExtOrgId();
			// 0322 ydx 修改客户编码，同步其他系统返回的客户编码不做校验
			mrsAccountDtoById.setCustomerCode(oldDto.getMrsAccountDto().getCustomerCode());
			mrsAccountDtoById.setExtOrgId(oldDto.getMrsAccountDto().getExtOrgId());

			int mrsAccountDtoSize = equalsObj(mrsAccountDtoById, oldDto.getMrsAccountDto());
			if (mrsAccountDtoSize != 0) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0092);
			}

			List<MrsCertFileDto> mrsCertFileDtolist = oldDto.getMrsCertFileDtos();
			for (MrsCertFileDto mrsCertFileDto : mrsCertFileDtolist) {
				MrsCertFileDto dtos = mrsCertFileDtoMapper.selectByPrimaryKey(mrsCertFileDto.getId());
				int mrsCertFileDtosSize = equalsObj(dtos, mrsCertFileDto);
				if (mrsCertFileDtosSize != 0) {
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0093);
				}
			}

			List<MrsContactListDto> mrsContactListDtoList = oldDto.getMrsContactListDtos();
			for (MrsContactListDto mrsContactListDto : mrsContactListDtoList) {
				MrsContactListDto dtos = null;
				int mrsContactListDtosSize = equalsObj(dtos, mrsContactListDto);
				if (mrsContactListDtosSize != 0) {
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0094);
				}
			}

			/** 保存所有数据 */
			try {
				// 更新一户通账户表
				logger.info("更新一户通账户表");
				MrsAccountDto accountDto = dto.getMrsAccountDto();
				accountDto.setUpdateTime(new Date());
				accountDto.setUpdateOperator(currentUser.getId());
				// 0322 ydx 修改客户编码，同步其他系统返回的客户编码不做校验 修改的时候以现在数据库字段属性为准
				accountDto.setCustomerCode(custCode);
				// 0524  修改商户编码，不做校验
				accountDto.setExtOrgId(extOrgId);
				mrsAccountDtoMapper.updateByPrimaryKey(accountDto);

				// 将custId保存至审核信息主表
				mrsAduitInfoDto.setCustId(accountDto.getCustId());
				mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);

				// 存同步数据的副本
				MrsToJsonDto SyncDto = new MrsToJsonDto();
				if (MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue().equals(type)) {
					logger.info("更新个人客户信息表");

					/* ydx 0320 修改 校验修改后的三要素是否存在有效的一户通信息，如果有直接报错 */
					MrsPersonDto oldPerson = oldDto.getMrsPersonDto();
					MrsPersonDto newPerson = dto.getMrsPersonDto();
					if (oldPerson.getCredentialsType().equals(newPerson.getCredentialsType())
							&& oldPerson.getCredentialsNumber().equals(newPerson.getCredentialsNumber())
							&& oldPerson.getCustomerName().equals(newPerson.getCustomerName())) {
						// 没有改变
					} else {
						// 查询不为生效的个人一户通信息
						List<MrsPersonDto> persons = mrsPersonDtoMapper.findBy3Element(newPerson.getCustomerName(),
								newPerson.getCredentialsType(), newPerson.getCredentialsNumber());
						if (!CollectionUtil.isEmpty(persons)) {
							// 不可以修改 存在不为注销的一户通信息
							logger.info("更新个人信息时，三要素已经改变，名称{" + newPerson.getCustomerName() + "}，" + "证件类型{"
									+ newPerson.getCredentialsType() + "}，" + "证件号码{" + newPerson.getCredentialsNumber()
									+ "}，存在对应的不为生效的一户通信息。不能做相应的修改！");
							throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0104);
						}
					}
					MrsPersonDto personDto = dto.getMrsPersonDto();
					personDto.setUpdateTime(new Date());
					personDto.setUpdateOperator(currentUser.getId());
					mrsPersonDtoMapper.updateByPrimaryKeySelective(personDto);
					SyncDto.setMrsPersonDto(personDto);
				} else if (MrsCustTypeEnum.MRS_CUST_TYPE_01.getValue().equals(type)) {
					logger.info("保存机构客户信息表");

					/* ydx 0320 修改 校验修改后的三要素是否存在有效的一户通信息，如果有直接报错 */
					MrsOrganDto newOrgan = dto.getMrsOrganDto();
					boolean isMax = mrsOrganService.checkOrgan3ElmentUpdateIsMax(newOrgan.getCustomerName(),
							newOrgan.getSocialCreditCode(), newOrgan.getOrganizeCode(), newOrgan.getRevenueCode(),
							newOrgan.getBusinessLicence(), newOrgan.getOrganOtherCode(), newOrgan.getCustId());
					if (!isMax) {
						logger.info("审核通过时，机构名称{" + newOrgan.getCustomerName() + "}组织机构号" + "{"
								+ newOrgan.getOrganizeCode() + "}营业执照{" + newOrgan.getBusinessLicence() + "" + "税务登记证{"
								+ newOrgan.getRevenueCode() + "}社会统一信用代码" + "{" + newOrgan.getSocialCreditCode()
								+ "}其他证件号{" + newOrgan.getOrganOtherCode() + "" + "}已经存在不为注销的一户通号码，审核流程结束！。");
						throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0106);
					}
					MrsOrganDto organDto = dto.getMrsOrganDto();
					organDto.setUpdateTime(new Date());
					organDto.setUpdateOperator(currentUser.getId());
					mrsOrganDtoMapper.updateByPrimaryKeySelective(organDto);
					SyncDto.setMrsOrganDto(organDto);
				} else if (MrsCustTypeEnum.MRS_CUST_TYPE_02.getValue().equals(type)) {
					logger.info("保存产品客户信息表");
					// TODO 产品现在校验没有加上去
					/* ydx 0320 修改 校验修改后的三要素是否存在有效的一户通信息，如果有直接报错 */
					MrsProductDto oldProduct = oldDto.getMrsProductDto();
					MrsProductDto newProduct = dto.getMrsProductDto();
					if (oldProduct.getCredentialsType().equals(newProduct.getCredentialsType())
							&& oldProduct.getCredentialsNumber().equals(newProduct.getCredentialsNumber())
							&& oldProduct.getProductName().equals(newProduct.getProductName())) {
						// 没有改变
					} else {
						// 查询不为生效的个人一户通信息
						List<MrsProductDto> products = mrsProductDtoMapper.findBy3ElementAndNoEff(
								newProduct.getProductName(), newProduct.getCredentialsType().getValue(),
								newProduct.getCredentialsNumber());
						if (!CollectionUtil.isEmpty(products)) {
							// 不可以修改 存在不为注销的一户通信息
							logger.info("更新产品信息时，三要素已经改变，名称{" + newProduct.getProductName() + "}，" + "证件类型{"
									+ newProduct.getCredentialsType() + "}，" + "证件号码{"
									+ newProduct.getCredentialsNumber() + "}，存在对应的不为注销的一户通信息。不能做相应的修改！");
							throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0104);
						}
					}

					MrsProductDto productDto = dto.getMrsProductDto();
					productDto.setUpdateTime(new Date());
					productDto.setUpdateOperator(currentUser.getId());
					mrsProductDtoMapper.updateByPrimaryKeySelective(productDto);
					SyncDto.setMrsProductDto(mrsProductDtoMapper.findCustId(productDto.getCustId()));
				}

				logger.info("先删除所有联系人");
				List<MrsContactListDto> mrsContactListDtos = oldDto.getMrsContactListDtos();
				if (mrsContactListDtos != null && mrsContactListDtos.size() != 0) {
					for (MrsContactListDto contacts : mrsContactListDtos) {
						// mrsContactListDtoMapper.deleteByPrimaryKey(contacts.getId());
						// ydx修改 不做直接删除
						contacts.setStatus(EStatusEnum.UNEFFECTIVE.getValue());
						mrsContactListDtoMapper.updateByPrimaryKey(contacts);
					}
				}

				logger.info("更新联系人");
				List<MrsContactListDto> newMrsContactListDtos = dto.getMrsContactListDtos();
				if (newMrsContactListDtos != null && newMrsContactListDtos.size() > 0) {
					for (MrsContactListDto mrsContactListDto : newMrsContactListDtos) {
						if (StringUtils.isBlank(mrsContactListDto.getId())) {
							mrsContactListDto.setId(UUID.randomUUID().toString());
							mrsContactListDto.setCreateTime(new Date());
							mrsContactListDto.setCustId(accountDto.getCustId());
							mrsContactListDto.setStatus(EStatusEnum.EFFECTIVE.getValue());
							mrsContactListDtoMapper.insert(mrsContactListDto);
						} else {
							MrsContactListDto con = mrsContactListDtoMapper
									.selectByPrimaryKey(mrsContactListDto.getId());
							if (con != null) {
								con.setName(mrsContactListDto.getName());
								con.setPhoneNo(mrsContactListDto.getPhoneNo());
								con.setMobill(mrsContactListDto.getMobill());
								con.setEmail(mrsContactListDto.getEmail());
								con.setCertType(mrsContactListDto.getCertType());
								con.setCertNo(mrsContactListDto.getCertNo());
								con.setAddress(mrsContactListDto.getAddress());
								con.setStatus(EStatusEnum.EFFECTIVE.getValue());
								mrsContactListDtoMapper.updateByPrimaryKey(con);
							}
						}
					}
				}

				logger.info("先删除所有附件");
				List<MrsCertFileDto> mrsCertFileDtos = oldDto.getMrsCertFileDtos();
				for (MrsCertFileDto mrsCertFileDto : mrsCertFileDtos) {
					mrsCertFileDtoMapper.deleteById(mrsCertFileDto.getId());
				}
				logger.info("更新附件");
				List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = dto.getMrsAduitAttachmentDtos();
				if (mrsAduitAttachmentDtos != null && mrsAduitAttachmentDtos.size() > 0) {
					for (MrsAduitAttachmentDto mrsAduitAttachmentDto : mrsAduitAttachmentDtos) {
						MrsCertFileDto mrsCertFileDto = new MrsCertFileDto();
						mrsCertFileDto.setId(UUID.randomUUID().toString());
						mrsCertFileDto.setCustId(accountDto.getCustId());
						mrsCertFileDto.setFileRemark(mrsAduitAttachmentDto.getRemark());
						mrsCertFileDto.setCustomerType(type);
						mrsCertFileDto.setCertType(mrsAduitAttachmentDto.getCertType());
						mrsCertFileDto.setFileType(mrsAduitAttachmentDto.getSuffix());
						mrsCertFileDto.setFileId(mrsAduitAttachmentDto.getStoragePath());
						mrsCertFileDto.setCreateTime(new Date());
						mrsCertFileDto.setCreateOperator(currentUser.getId());
						if (mrsAduitAttachmentDto.getCheckIsImageBoolean()) {
							mrsCertFileDto.setIsPicFlag("1");// 为图片
						} else {
							mrsCertFileDto.setIsPicFlag("2");// 不为图片
						}
						mrsCertFileDtoMapper.insert(mrsCertFileDto);
					}
				}

				return SyncDto;

			} catch (Exception e) {
				e.printStackTrace();
				logger.info(ExceptionProcUtil.getExceptionDesc(e));
				if (e instanceof CbsUncheckedException) {
					throw new CbsUncheckedException(ECbsErrorCode.getByDisplay(e.getMessage()));
				}
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0095);
			}
		}
	}

	/**
	 * 资金账户信息变更审核
	 */
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateActAccountStatus(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsUncheckedException {
		logger.info("开始审核信息,审核人:[{}],审核信息记录id:[{}]", new Object[] { currentUser.getLoginName(), id });
		// 保存审核记录表
		MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
		mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
		mrsAduitPersonHisDto.setAduitId(id);
		mrsAduitPersonHisDto.setAduitRemark(remark);
		mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonHisDto.setAduitTime(new Date());
		mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
		mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
		mrsAduitPersonHisDto.setCreateTime(new Date());
		mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

		// 修改审核人状态
		MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(id, currentUser.getId());
		mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

		// 查询审核主要信息表，判断是否为最后一个审核人，若不是，则只改变
		if (mrsAduitInfoDto.getAduitNum() + 1 < mrsAduitInfoDto.getAduitTotal()) {
			// 还不是最后一个审核人，只更新审核主要信息表记录
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_ING);
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
			return;
		} else {
			// 若审核进度已完成,则修改原记录
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_PASS);
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);

			List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper.selectByAuditId(id);
			if (list == null || list.isEmpty()) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
			}
			String oldJson = list.get(0).getOldValue();
			String newJson = list.get(0).getNewValue();
			MrsToJsonDto oldJsonDto = null;
			MrsToJsonDto newJsonDto = null;
			try {
				// 将json字符串转换成json对象
				oldJsonDto = jsonToDto(oldJson);
				newJsonDto = jsonToDto(newJson);
			} catch (Exception e) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0088);
			}
			// 根据不同客户类型做不同数据对比
			// //检验原json中的值与数据库中的值是否相同,若不相同则终止信息变更审核
			// ActAccountDto mrsAccountDtoById =
			// mrsAccountDtoMapper.selectByPrimaryKey(oldJsonDto.getActAccountDtos().get(0));
			// int mrsAccountDtoSize = equalsObj(mrsAccountDtoById,
			// oldDto.getMrsAccountDto());
			// if (mrsAccountDtoSize != 0) {
			// throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0092);
			// }
			// 检验原json中的资金账户信息与数据库中的值是否相同,若不相同则终止信息变更审核
			List<ActAccountDto> actAccountDtos = oldJsonDto.getActAccountDtos();
			ActAccountDto actAccountDto = null;
			for (ActAccountDto dto : actAccountDtos) {
				actAccountDto = actAccountDtoMapper.selectByPrimaryKey(dto.getId());
				int size = equalsObj(actAccountDto, dto);
				if (size != 0) {
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0092);
				}
			}
			try {
				// 修改原资金账户信息
				List<ActAccountDto> actAccountList = newJsonDto.getActAccountDtos();
				List<ActAccountDto> actStatusList = new ArrayList<ActAccountDto>();
				for (ActAccountDto dto : actAccountList) {
					dto.setAcctBusiType(dto.getOperationType());
					actStatusList.add(dto);
				}
				// actBookAppService.changeAccountBusiType(actAccountDto.getAccountId(),
				// actAccountDto.getOperationType());
				actBookAppService.changeAccountBusiTypes(actStatusList);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(ExceptionProcUtil.getExceptionDesc(e));
				if (e instanceof CbsUncheckedException) {
					throw new CbsUncheckedException(ECbsErrorCode.getByDisplay(e.getMessage()));
				}
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0095);
			}

		}
	}
	
	/**
	 * 用户注销审核
	 */
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void removeStatus(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsUncheckedException {
		logger.info("开始审核信息,审核人:[{}],审核信息记录id:[{}]", new Object[] { currentUser.getLoginName(), id });
		// 保存审核记录表
		MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
		mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
		mrsAduitPersonHisDto.setAduitId(id);
		mrsAduitPersonHisDto.setAduitRemark(remark);
		mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonHisDto.setAduitTime(new Date());
		mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
		mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
		mrsAduitPersonHisDto.setCreateTime(new Date());
		mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

		// 修改审核人状态
		MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(id, currentUser.getId());
		mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

		// 查询审核主要信息表，判断是否为最后一个审核人，若不是，则只改变
		if (mrsAduitInfoDto.getAduitNum() + 1 < mrsAduitInfoDto.getAduitTotal()) {
			// 还不是最后一个审核人，只更新审核主要信息表记录
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_ING);
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
			return;
		} else {
			// 若审核进度已完成,则修改原记录
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_PASS);
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);

			List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper.selectByAuditId(id);
			if (list == null || list.isEmpty()) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
			}
			String oldJson = list.get(0).getOldValue();
			String newJson = list.get(0).getNewValue();
			MrsToJsonDto oldJsonDto = null;
			MrsToJsonDto newJsonDto = null;
			try {
				// 将json字符串转换成json对象
				oldJsonDto = jsonToDto(oldJson);
				newJsonDto = jsonToDto(newJson);
			} catch (Exception e) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0088);
			}
			/*// 个人客户
			String name = null;
			if (type.equals(MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue())) {
				MrsPersonDto mrsPersonDto = mrsPersonDtoMapper.findByCustId(mrsAduitInfoDto.getCustId());
				name = mrsPersonDto.getCustomerName();
				// 机构客户
			} else if (type.equals(MrsCustTypeEnum.MRS_CUST_TYPE_01.getValue())) {
				MrsOrganDto mrsOrganDto = mrsOrganDtoMapper.findByCustId(mrsAduitInfoDto.getCustId());
				name = mrsOrganDto.getCustomerName();
				// 产品客户
			} else {
				MrsProductDto mrsProductDto = mrsProductDtoMapper.findCustId(mrsAduitInfoDto.getCustId());
				name = mrsProductDto.getProductName();
			}*/
			// 检验原json中的子账户信息与数据库中的值是否相同,若不相同则终止信息变更审核
			List<MrsSubAccountDto> subAccountDtos = oldJsonDto.getMrsSubAccountDtoList();
			MrsSubAccountDto mrsSubAccountDto = null;
			for (MrsSubAccountDto dto : subAccountDtos) {
				mrsSubAccountDto = mrsSubAccountDtoMapper.selectByPrimaryKey(dto.getId());
				int size = equalsObj(mrsSubAccountDto, dto);
				if (size != 0) {
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0092);
				}
			}

			try {
				//更新一户通信息
				MrsAccountDto mrsAccountDto = newJsonDto.getMrsAccountDto();
				mrsAccountDto.setUpdateTime(new Date());
				mrsAccountDtoMapper.updateByPrimaryKeySelective(mrsAccountDto);

			} catch (Exception e) {
				e.printStackTrace();
				logger.info(ExceptionProcUtil.getExceptionDesc(e));
				if (e instanceof CbsUncheckedException) {
					throw new CbsUncheckedException(ECbsErrorCode.getByDisplay(e.getMessage()));
				}
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0095);
			}
		}
	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public MrsToJsonDto approveAduitAndSave(UcsSecUserDto currentUser, MrsAduitInfoDto mrsAduitInfoDto, String aduitId,
			String remark) throws CbsCheckedException {
		logger.info("开始审核信息,审核人:[{}],审核信息记录id:[{}]",
				new Object[] { currentUser.getLoginName(), mrsAduitInfoDto.getId() });
		// 保存审核记录表
		MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
		mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
		mrsAduitPersonHisDto.setAduitId(aduitId);
		mrsAduitPersonHisDto.setAduitRemark(remark);
		mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonHisDto.setAduitTime(new Date());
		mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
		mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
		mrsAduitPersonHisDto.setCreateTime(new Date());
		mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

		// 修改审核人状态
		MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(aduitId, currentUser.getId());
		mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

		// 查询审核主要信息表，判断是否为最后一个审核人，若不是，则只改变
		if (mrsAduitInfoDto.getAduitNum() + 1 < mrsAduitInfoDto.getAduitTotal()) {
			// 还不是最后一个审核人，只更新审核主要信息表记录
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_ING);
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
			return null;
		} else {
			// 若审核进度已完成,则修改原记录
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_PASS);
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);

			logger.info("审核信息表ID查询审核内容表");
			// 所有信息入库
			// 根据审核信息表ID查询审核内容表
			List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper.selectByAuditId(aduitId);
			if (list == null || list.size() != 1) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
			}
			logger.info("转json");
			String json = list.get(0).getNewValue();
			MrsToJsonDto dto = null;
			try {
				dto = jsonToDto(json);
			} catch (Exception e) {
				e.printStackTrace();
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0088);
			}
			try {
				String custId = "";
				String name = "";
				/** 保存所有数据 */
				// 开立一户通账户cust_id
				custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
				custId = MrsConstants.PERSON_ACCOUNT_PREFIX + StringUtils.format(11, custId);
				// 将custId保存至审核信息主表
				mrsAduitInfoDto.setCustId(custId);
				mrsAduitInfoDto.setProductAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
				mrsAduitInfoDto.setProductStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
				mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);

				logger.info("保存附件");
				List<MrsAduitAttachmentDto> mrsAduitAttachmentDtos = dto.getMrsAduitAttachmentDtos();
				if (mrsAduitAttachmentDtos != null && mrsAduitAttachmentDtos.size() > 0) {
					for (MrsAduitAttachmentDto mrsAduitAttachmentDto : mrsAduitAttachmentDtos) {
						MrsCertFileDto mrsCertFileDto = new MrsCertFileDto();
						mrsCertFileDto.setId(UUID.randomUUID().toString());
						mrsCertFileDto.setCustId(custId);
						mrsCertFileDto.setFileRemark(mrsAduitAttachmentDto.getRemark());
						mrsCertFileDto.setCustomerType(ECustomTypeEnum.CUS_PERSON.getValue());
						mrsCertFileDto.setCertType(mrsAduitAttachmentDto.getCertType());
						mrsCertFileDto.setFileType(mrsAduitAttachmentDto.getSuffix());
						mrsCertFileDto.setFileId(mrsAduitAttachmentDto.getStoragePath());
						mrsCertFileDto.setCreateTime(new Date());
						mrsCertFileDto.setCreateOperator(currentUser.getId());
						if (mrsAduitAttachmentDto.getCheckIsImageBoolean()) {
							mrsCertFileDto.setIsPicFlag("1");// 为图片
						} else {
							mrsCertFileDto.setIsPicFlag("2");// 不为图片
						}
						mrsCertFileDtoMapper.insert(mrsCertFileDto);
					}
				}

				logger.info("保存一户通账户表");
				// 保存一户通账户表
				MrsAccountDto mrsAccountDto = dto.getMrsAccountDto();
				mrsAccountDto = new MrsAccountDto();
				mrsAccountDto.setId(UUID.randomUUID().toString());
				mrsAccountDto.setAccountType(AaccountType.BSYHT);
				mrsAccountDto.setCustomerType("0");
				mrsAccountDto.setPlatformCode("gscore");
				mrsAccountDto.setAccountSource(MrsAccountSource.SOURCE_03.getValue());
				mrsAccountDto.setCustId(custId);
				mrsAccountDto.setIsDelete("N");
				mrsAccountDto.setCreateTime(new Date());
				mrsAccountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
				mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());// 账户状态设置为正常
				mrsAccountDtoMapper.insertSelective(mrsAccountDto);

				// 存同步数据的副本
				MrsToJsonDto SyncDto = new MrsToJsonDto();
				logger.info("保存个人客户信息表");
				// 个人客户
				if (mrsAduitInfoDto.getCustType().getValue().equals(MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue())) {
					// 保存个人客户信息表
					MrsPersonDto mrsPersonDto = dto.getMrsPersonDto();
					mrsPersonDto.setId(UUID.randomUUID().toString());
					mrsPersonDto.setCustId(custId);
					mrsPersonDto.setCreateTime(new Date());
					mrsPersonDtoMapper.insertSelective(mrsPersonDto);
					SyncDto.setMrsPersonDto(mrsPersonDto);
					name = mrsPersonDto.getCustomerName();
				} else {
					// 保存机构客户信息
					MrsOrganDto mrsOrganDto = dto.getMrsOrganDto();
					mrsOrganDto.setId(UUID.randomUUID().toString());
					mrsOrganDto.setCustId(custId);
					mrsOrganDto.setCreateTime(new Date());
					mrsOrganDtoMapper.insertSelective(mrsOrganDto);
					SyncDto.setMrsOrganDto(mrsOrganDto);
					name = mrsOrganDto.getCustomerName();
				}
				// 开资金账户
				actBookAppService.createAccounts(custId, name, dto.getAccountSubsPojos());

				logger.info("保存子账户信息表");
				// 保存子账户信息表
				List<MrsSubAccountDto> mrsSubAccountDtoList = dto.getMrsSubAccountDtoList();
				if (mrsSubAccountDtoList != null && mrsSubAccountDtoList.size() > 0) {
					for (MrsSubAccountDto subAccountDto : mrsSubAccountDtoList) {
						subAccountDto.setId(UUID.randomUUID().toString());
						subAccountDto.setCustId(custId);
						if (subAccountDto.getPlatformCode() == null
								|| subAccountDto.getPlatformCode().trim().equals("")) {
							subAccountDto.setPlatformCode("gscore");
						}
						subAccountDto.setCreateTime(new Date());
						subAccountDto.setSubAccountCode(subAccountDto.getSubAccountType() + "0" + custId);
						// subAccountDto.setSubAccountType("01");//
						// 该字段已废弃，因必填字段，无法插入null，故给任意固值。
						subAccountDto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());// 子账户状态设置为有效
						mrsSubAccountDtoMapper.insertSelective(subAccountDto);
					}
				}
				// 保存用户信息表
				List<MrsLoginUserDto> mrsLoginUserDtos = dto.getMrsLoginUserDtoList();
				MrsUserAccountDto mrsUserAccountDto = null;
				for (MrsLoginUserDto loginUserDto : mrsLoginUserDtos) {
					// loginUserDto.setId(UUID.randomUUID().toString());
					loginUserDto.setAccountCode(custId);
					loginUserDto.setUserStatus(LoginUserStatus.LOGIN_USER_STATUS_0.getValue());
					loginUserDto.setUpdateTime(new Date());
					mrsLoginUserDtoMapper.updateByPrimaryKeySelective(loginUserDto);
					// 保存账户登陆用户关联信息表
					mrsUserAccountDto = new MrsUserAccountDto();
					mrsUserAccountDto.setId(UUID.randomUUID().toString());
					mrsUserAccountDto.setAccountId(mrsAccountDto.getId());
					mrsUserAccountDto.setLoginId(loginUserDto.getId());
					mrsUserAccountDto.setIsMain("0");
					mrsUserAccountDto.setCreateTime(new Date());
					mrsUserAccountDtoMapper.insertSelective(mrsUserAccountDto);
				}
				return SyncDto;
			} catch (Exception e) {
				e.printStackTrace();
				if (e instanceof CbsUncheckedException) {
					throw new CbsUncheckedException(ECbsErrorCode.getByDisplay(e.getMessage()));
				} else if (e instanceof AccountUncheckedException) {
					// 开资金账户异常
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0105);
				}
				// 其他异常
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0095);
			}
		}
	}

	/**
	 * 查看2个同类的对象属性是否相同
	 */
	public static int equalsObj(Object obj1, Object obj2) {
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		try {
			if (obj1 != null && obj2 != null && obj1.getClass() == obj2.getClass()) {
				Class clazz = obj1.getClass();
				PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					String name = pd.getName();
					if (name.equals("identity")) {
						continue;
					}
					Method readMethod = pd.getReadMethod();
					Object o1 = readMethod.invoke(obj1);
					Object o2 = readMethod.invoke(obj2);
					if (o1 != null && o2 != null && !o1.equals(o2)) {
						List<Object> list = new ArrayList<Object>();
						list.add(o1);
						list.add(o2);
						map.put(name, list);
					}
				}
			}

			logger.info("2个同类对象属性map：" + map.toString());
			System.out.println("2个同类对象属性map：" + map.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map.size();
	}

	/**
	 * 对老对象进行新值的替换
	 */
	public static Object setObj(Object obj1, Object obj2) {
		try {
			if (obj1 != null && obj2 != null && obj1.getClass() == obj2.getClass()) {
				Class clazz = obj1.getClass();
				PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz, Object.class).getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					String name = pd.getName();
					Method readMethod = pd.getReadMethod();
					Object o1 = readMethod.invoke(obj1);
					Object o2 = readMethod.invoke(obj2);
					if (o1 != null && o2 != null && !o1.equals(o2)) {
						Field f = clazz.getDeclaredField(name);
						f.setAccessible(true);
						f.set(obj1, o2);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj1;
	}

	@Override
	public boolean checkDataIsAduit(String aduitId, String loginId) throws CbsCheckedException {

		if (StringUtils.isBlank(aduitId) || StringUtils.isBlank(loginId)) {
			return false;
		} else {
			try {
				MrsAduitPersonHisDto his = mrsAduitPersonHisDtoMapper.findByAduitIdAndUserId(aduitId, loginId);
				if (his == null) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new CbsCheckedException("校验 审核历史数据异常！");
			}
			return false;
		}

	}

	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public SaveAduitProductResponseVo doUpdataSubAccountStatus(MrsSubAccountVo mrsSubAccountVo, String flag) {
		SaveAduitProductResponseVo respvo = new SaveAduitProductResponseVo();
		MrsPersonDto mrsPersonDto = null;
		MrsOrganDto mrsOrganDto = null;
		MrsProductDto mrsProductDto = null;
		try {
			// 个人客户
			if (flag.equals(MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue())) {
				mrsPersonDto = mrsPersonDtoMapper.findByCustId(mrsSubAccountVo.getCustId());
				// 机构客户
			} else if (flag.equals(MrsCustTypeEnum.MRS_CUST_TYPE_01.getValue())) {
				mrsOrganDto = mrsOrganDtoMapper.findByCustId(mrsSubAccountVo.getCustId());
			} else {
				// 产品客户
				mrsProductDto = mrsProductDtoMapper.findCustId(mrsSubAccountVo.getCustId());
			}
			// 获取子账户信息
			List<MrsSubAccountDto> mrsSubAccountDtos = mrsSubAccountDtoMapper.findByCustId(mrsSubAccountVo.getCustId());
//			if (CollectionUtil.isEmpty(mrsSubAccountDtos)) {
//				respvo.setIsSucess(false);
//				respvo.setMsgCode("");
//				respvo.setMsgInfo("获取子账户信息为空!");
//				return respvo;
//			}
			// 获取一户通信息
			MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(mrsSubAccountVo.getCustId());

			if (mrsAccountDto == null) {
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("数据有误，一户通信息不存在!");
				return respvo;
			}
			// 一户通状态为注销不可修改
			if (mrsAccountDto.getAccountStatus().equals(MrsAccountStatus.ACCOUNT_STATUS_2.getValue())) {
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("一户通状态为已注销，不能发起信息变更!");
				return respvo;
			}

			// 生成审核记录
			MrsAduitInfoDto mrsAduitInfoDto = new MrsAduitInfoDto();
			// 查询审核配置表获取审核人数
			MrsConfAuditDto mrsConfAuditDto = mrsConfAuditDtoMapper.findByBusiTypeAndSendType(
					EOperaTypeEnum.OP_SONUPDATE.getValue(), EStartSystemEnum.SYS_COUNTER.getValue());
			if (mrsConfAuditDto == null) {
				respvo.setIsSucess(false);
				respvo.setMsgCode("");
				respvo.setMsgInfo("子账户变更操作未进行配置!");
				return respvo;
			}
			mrsAduitInfoDto.setAduitTotal(mrsConfAuditDto.getAuditNo());
			mrsAduitInfoDto.setAduitNum((short) 0);
			mrsAduitInfoDto.setBusiType(EOperaTypeEnum.OP_SONUPDATE);
			mrsAduitInfoDto.setStartSystem(EStartSystemEnum.SYS_COUNTER);
			if (flag.equals(MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue())) {
				mrsAduitInfoDto.setCustId(mrsPersonDto.getCustId());
				mrsAduitInfoDto.setCartNo(mrsPersonDto.getCredentialsNumber());
				mrsAduitInfoDto.setCartType(mrsPersonDto.getCredentialsType());
				mrsAduitInfoDto.setName(mrsPersonDto.getCustomerName());
				mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_00);
			} else if (flag.equals(MrsCustTypeEnum.MRS_CUST_TYPE_01.getValue())) {
				mrsAduitInfoDto.setCustId(mrsOrganDto.getCustId());
				mrsAduitInfoDto.setCartNo(mrsOrganDto.getOganCertNo());
				mrsAduitInfoDto.setCartType(mrsOrganDto.getOganCertType());
				mrsAduitInfoDto.setName(mrsOrganDto.getCustomerName());
				mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_01);
			} else {
				mrsAduitInfoDto.setCustId(mrsProductDto.getCustId());
				mrsAduitInfoDto.setCartNo(mrsProductDto.getCredentialsNumber());
				mrsAduitInfoDto.setCartType(mrsProductDto.getCredentialsType().getValue());
				mrsAduitInfoDto.setName(mrsProductDto.getProductName());
				mrsAduitInfoDto.setCustType(MrsCustTypeEnum.MRS_CUST_TYPE_02);
			}
			mrsAduitInfoDto.setCreateOperator(mrsSubAccountVo.getCurrentUser().getLoginName());
			mrsAduitInfoDto.setCreateTime(new Date());
			mrsAduitInfoDto.setProductStatus(mrsAccountDto.getAccountStatus());
			mrsAduitInfoDto.setProductAuthStatus(mrsAccountDto.getAuthStatus());
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_WAIT);
			mrsAduitInfoDto.setId(UUID.randomUUID().toString());
			if (!StringUtils.isEmpty(mrsSubAccountVo.getRemark())) {
				mrsAduitInfoDto.setRemark(mrsSubAccountVo.getRemark());
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
			oldMrsToJson.setMrsSubAccountDtoList(mrsSubAccountDtos);
			oldMrsToJson.setMrsAccountDto(mrsAccountDto);

			// 新json值
			MrsToJsonDto newMrsToJson = new MrsToJsonDto();
			List<MrsSubAccountDto> mrsSubAccountList = new ArrayList<MrsSubAccountDto>();
			List<String> confIds = new ArrayList<String>();
			MrsSubAccountDto mrsSubAccountDto = null;
			for (MrsSubAccountDto dto : mrsSubAccountVo.getMrsSubAccountDtos()) {
				if (dto.getOperationType() != null) {
					mrsSubAccountDto = mrsSubAccountDtoMapper.selectByPrimaryKey(dto.getId());
					// 子账户信息表中数据
					if (mrsSubAccountDto != null) {
						mrsSubAccountDto.setOperationType(dto.getOperationType());
						mrsSubAccountList.add(mrsSubAccountDto);
						// 新增子账户
					} else {
						List<MrsAduitContentDtoWithBLOBs> blobs = mrsAduitContentDtoMapper
								.selectByAuditId(mrsSubAccountVo.getCustId());
						String str = null;
						MrsToJsonDto mrsToJsonDto = null;
						if (!CollectionUtil.isEmpty(blobs)) {
							for (MrsAduitContentDtoWithBLOBs blob : blobs) {
								str = blob.getNewValue();
								mrsToJsonDto = jsonToDto(str);
								List<MrsSubAccountDto> list = mrsToJsonDto.getMrsSubAccountDtoList();
								for (MrsSubAccountDto acctDto : list) {
									if (dto.getId().equals(acctDto.getId())) {
										acctDto.setOperationType(dto.getOperationType());
										mrsSubAccountList.add(acctDto);
										// 获取子账户编号
										List<MrsConfSubAcctDto> mrsConfSubAcctDtos = mrsConfSubAcctDtoMapper
												.findByAccountName(acctDto.getSubAccountName());
										confIds.add(mrsConfSubAcctDtos.get(0).getSubAccountCode());
									}
								}
							}
						}
					}
				}
			}
			// 开通资金账户需要初始化类
			List<AccountSubsPojo> subPojos = new ArrayList<AccountSubsPojo>();
			if (!CollectionUtil.isEmpty(confIds)) {
				// 科目支持业务类型信息 科目编号
				// List<String> mrsSubPayBusiDtos =
				// mrsSubPayBusiDtoMapper.findByConfSubAcctIds(confIds);
				List<String> subAcctTypes = mrsConfAcctDtoMapper.findSubAcctTypesByConfSubIds(confIds);
				List<String> mrsSubPayBusiDtos = actBusiRefSubDtoMapper.findSub2NoByAcctTypeNos(subAcctTypes);
				for (String sub : mrsSubPayBusiDtos) {
					AccountSubsPojo pojo = new AccountSubsPojo();
					pojo.setActSub(sub);
					pojo.setActBusiType(AcctBusiType.YES_DEBIT_CERDIT);
					subPojos.add(pojo);
				}
			}
			newMrsToJson.setMrsPersonDto(mrsPersonDto);
			newMrsToJson.setMrsOrganDto(mrsOrganDto);
			newMrsToJson.setMrsProductDto(mrsProductDto);
			newMrsToJson.setMrsAccountDto(mrsAccountDto);
			newMrsToJson.setMrsSubAccountDtoList(mrsSubAccountList);
			newMrsToJson.setAccountSubsPojos(subPojos);
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
			respvo.setMsgInfo("子账户变更申请成功!");
			return respvo;

		} catch (Exception e) {
			e.printStackTrace();
			respvo.setIsSucess(false);
			respvo.setMsgCode("");
			respvo.setMsgInfo("子账户变更申请失败!");
			return respvo;

		}

	}

	/**
	 * 子账户变更
	 */
	@Override
	@Transactional(value = CbsConstants.TX_MANAGER_MRS)
	public void updateSubAccountStatus(String type, String id, String remark, MrsAduitInfoDto mrsAduitInfoDto,
			UcsSecUserDto currentUser) throws CbsCheckedException {
		logger.info("开始审核信息,审核人:[{}],审核信息记录id:[{}]", new Object[] { currentUser.getLoginName(), id });
		// 保存审核记录表
		MrsAduitPersonHisDto mrsAduitPersonHisDto = new MrsAduitPersonHisDto();
		mrsAduitPersonHisDto.setId(UUID.randomUUID().toString());
		mrsAduitPersonHisDto.setAduitId(id);
		mrsAduitPersonHisDto.setAduitRemark(remark);
		mrsAduitPersonHisDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonHisDto.setAduitTime(new Date());
		mrsAduitPersonHisDto.setAduitUserId(currentUser.getId());
		mrsAduitPersonHisDto.setAduitUserName(currentUser.getLoginName());
		mrsAduitPersonHisDto.setCreateTime(new Date());
		mrsAduitPersonHisDtoMapper.insertSelective(mrsAduitPersonHisDto);

		// 修改审核人状态
		MrsAduitPersonDto mrsAduitPersonDto = mrsAduitPersonDtoMapper.selectByAduitId(id, currentUser.getId());
		mrsAduitPersonDto.setAduitStatus(EAduitTypeEnum.ADUIT_PASS);
		mrsAduitPersonDtoMapper.updateByPrimaryKeySelective(mrsAduitPersonDto);

		// 查询审核主要信息表，判断是否为最后一个审核人，若不是，则只改变
		if (mrsAduitInfoDto.getAduitNum() + 1 < mrsAduitInfoDto.getAduitTotal()) {
			// 还不是最后一个审核人，只更新审核主要信息表记录
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_ING);
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);
			return;
		} else {
			// 若审核进度已完成,则修改原记录
			mrsAduitInfoDto.setStatus(EAduitTypeEnum.ADUIT_PASS);
			mrsAduitInfoDto.setAduitNum((short) (mrsAduitInfoDto.getAduitNum() + 1));
			mrsAduitInfoDtoMapper.updateByPrimaryKeySelective(mrsAduitInfoDto);

			List<MrsAduitContentDtoWithBLOBs> list = mrsAduitContentDtoMapper.selectByAuditId(id);
			if (list == null || list.isEmpty()) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0087);
			}
			String oldJson = list.get(0).getOldValue();
			String newJson = list.get(0).getNewValue();
			MrsToJsonDto oldJsonDto = null;
			MrsToJsonDto newJsonDto = null;
			try {
				// 将json字符串转换成json对象
				oldJsonDto = jsonToDto(oldJson);
				newJsonDto = jsonToDto(newJson);
			} catch (Exception e) {
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0088);
			}
			// 个人客户
			String name = null;
			if (type.equals(MrsCustTypeEnum.MRS_CUST_TYPE_00.getValue())) {
				MrsPersonDto mrsPersonDto = mrsPersonDtoMapper.findByCustId(mrsAduitInfoDto.getCustId());
				name = mrsPersonDto.getCustomerName();
				// 机构客户
			} else if (type.equals(MrsCustTypeEnum.MRS_CUST_TYPE_01.getValue())) {
				MrsOrganDto mrsOrganDto = mrsOrganDtoMapper.findByCustId(mrsAduitInfoDto.getCustId());
				name = mrsOrganDto.getCustomerName();
				// 产品客户
			} else {
				MrsProductDto mrsProductDto = mrsProductDtoMapper.findCustId(mrsAduitInfoDto.getCustId());
				name = mrsProductDto.getProductName();
			}
			// 检验原json中的资金账户信息与数据库中的值是否相同,若不相同则终止信息变更审核
			List<MrsSubAccountDto> subAccountDtos = oldJsonDto.getMrsSubAccountDtoList();
			MrsSubAccountDto mrsSubAccountDto = null;
			for (MrsSubAccountDto dto : subAccountDtos) {
				mrsSubAccountDto = mrsSubAccountDtoMapper.selectByPrimaryKey(dto.getId());
				int size = equalsObj(mrsSubAccountDto, dto);
				if (size != 0) {
					throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0092);
				}
			}

			try {
				List<MrsSubAccountDto> subAccountList = newJsonDto.getMrsSubAccountDtoList();
				//获取添加子账户数据
				List<MrsAduitContentDtoWithBLOBs> mrsAduitContentDtos = mrsAduitContentDtoMapper.selectByAuditId(mrsAduitInfoDto.getCustId());
				List<MrsSubAccountDto> mrsSubAccountDtosList = new ArrayList<MrsSubAccountDto>();
				String str = null;
				MrsToJsonDto mrsToJsonDto = null;
				
				if(!CollectionUtil.isEmpty(mrsAduitContentDtos)){
					for(MrsAduitContentDtoWithBLOBs blob:mrsAduitContentDtos){
						str = blob.getNewValue();
						mrsToJsonDto = jsonToDto(str);
						List<MrsSubAccountDto> mrsSubAccountDtos = mrsToJsonDto.getMrsSubAccountDtoList();
						//保存所有添加子账户数据
						mrsSubAccountDtosList.addAll(mrsSubAccountDtos);
					}
				}
				
				for (MrsSubAccountDto dto : subAccountList) {
					// 原子账户信息表中数据
					mrsSubAccountDto = mrsSubAccountDtoMapper.selectByPrimaryKey(dto.getId());
					if (mrsSubAccountDto != null) {
						if(dto.getOperationType().equals(MrsSubAccountStatus.MSAS_3)){
							dto.setSubAccountStatus(MrsSubAccountStatus.MSAS_0.getValue());
						}else{
							dto.setSubAccountStatus(dto.getOperationType().getValue());
						}
						dto.setUpdateTime(new Date());
						mrsSubAccountDtoMapper.updateByPrimaryKeySelective(dto);

						// 任意一个子账户状态变更为”冻结“或者”注销“，一户通状态变更为异常
						if (MrsSubAccountStatus.MSAS_1.getValue().equals(dto.getSubAccountStatus())
								|| MrsSubAccountStatus.MSAS_2.getValue().equals(dto.getSubAccountStatus())) {
							MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(dto.getCustId());
							mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_1.getValue());
							mrsAccountDto.setUpdateTime(new Date());
							mrsAccountDtoMapper.updateByPrimaryKeySelective(mrsAccountDto);
						}
						
						List<MrsSubAccountDto> mrsSubAccountDtoList = mrsSubAccountDtoMapper
								.findByCustId(mrsAduitInfoDto.getCustId());
						// 子账户状态由“冻结”变更为生效，一互通状态为正常
						boolean flag = mrsSubAccountService.checkSubAccountStatus(mrsSubAccountDtoList);
						if (dto.getOperationType().getValue().equals(MrsSubAccountStatus.MSAS_0.getValue()) 
								||dto.getOperationType().getValue().equals(MrsSubAccountStatus.MSAS_3.getValue())&& flag) {
							MrsAccountDto mrsAccountDto = mrsAccountDtoMapper.findByCustId(dto.getCustId());
							mrsAccountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
							mrsAccountDto.setUpdateTime(new Date());
							mrsAccountDtoMapper.updateByPrimaryKeySelective(mrsAccountDto);
						}
						// 新增子账户数据
					} else {
						dto.setSubAccountStatus(dto.getOperationType().getValue());
						dto.setCreateTime(new Date());
						mrsSubAccountDtoMapper.insertSelective(dto);
//						index++;
//						// 将json中的临时子账户数据删除
//						for(MrsAduitContentDtoWithBLOBs blob:mrsAduitContentDtos){
//							str = blob.getNewValue();
//							mrsToJsonDto = jsonToDto(str);
//						    List<MrsSubAccountDto> mrsSubAccountDtos = mrsToJsonDto.getMrsSubAccountDtoList();
//						    //保存所有添加子账户数据
//						    mrsSubAccountDtosList.addAll(mrsSubAccountDtos);
//						}
					
						Iterator<MrsSubAccountDto> it = mrsSubAccountDtosList.iterator();
						MrsSubAccountDto accountDto = null;
						while(it.hasNext()){
							accountDto = it.next();
					        if(dto.getId().equals(accountDto.getId())){
					        	it.remove();
					        }
						}
					}
				}
				
				
//				if(mrsSubAccountDtosList.size()==index){
//					mrsAduitContentDtoMapper.deleteByAduitId(mrsAduitInfoDto.getCustId());
//				}else{
					MrsToJsonDto jsonDto = new MrsToJsonDto();
					jsonDto.setMrsSubAccountDtoList(mrsSubAccountDtosList);
					JSONObject json = JSONObject.fromObject(jsonDto);
				    String jsons = json.toString();
				    MrsAduitContentDtoWithBLOBs blob = new MrsAduitContentDtoWithBLOBs();
				    blob.setId(UUID.randomUUID().toString());
				    blob.setAduitId(mrsAduitInfoDto.getCustId());
				    blob.setNewValue(jsons);
				    mrsAduitContentDtoMapper.deleteByAduitId(mrsAduitInfoDto.getCustId());
				    mrsAduitContentDtoMapper.insert(blob);
//				}
				// 若新增子账户类型，则开通资金账户
				if (!CollectionUtil.isEmpty(newJsonDto.getAccountSubsPojos())) {
					actBookAppService.createAccounts(mrsAduitInfoDto.getCustId(), name,
							newJsonDto.getAccountSubsPojos());
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.info(ExceptionProcUtil.getExceptionDesc(e));
				if (e instanceof CbsUncheckedException) {
					throw new CbsUncheckedException(ECbsErrorCode.getByDisplay(e.getMessage()));
				}
				throw new CbsUncheckedException(ECbsErrorCode.ADUIT_0095);
			}

		}

	}

}
