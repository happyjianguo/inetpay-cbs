package com.ylink.inetpay.cbs.mrs.App;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.inetpay.cbs.act.service.ActAccountService;
import com.ylink.inetpay.cbs.mrs.dao.MrsAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.service.MrsAccountService;
import com.ylink.inetpay.cbs.mrs.service.MrsCertAuditService;
import com.ylink.inetpay.cbs.mrs.service.MrsLoginUserService;
import com.ylink.inetpay.cbs.mrs.service.MrsOrganService;
import com.ylink.inetpay.cbs.mrs.service.MrsPlatformService;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.core.constant.MrsCertAuditStatus;
import com.ylink.inetpay.common.core.constant.SubAcctType;
import com.ylink.inetpay.common.core.util.ExceptionProcUtil;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActBusiRefSubDto;
import com.ylink.inetpay.common.project.cbs.app.MrsAccountAppService;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOrganDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.AccountRespVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.ActAccountRespVO;
import com.ylink.inetpay.common.project.portal.vo.accountsearch.ActAccountSbjNo2RespVO;

@Service("mrsAccountAppService")
public class MrsAccountAppServiceImpl implements MrsAccountAppService {

	private static Logger log = LoggerFactory.getLogger(MrsAccountAppServiceImpl.class);
	@Autowired
	private MrsAccountService mrsAccountService;
	@Autowired
	private MrsCertAuditService mrsCertAuditService;
	@Autowired
	private MrsPlatformService mrsPlatformService;
	@Autowired
	private MrsAccountDtoMapper mrsAccountDtoMapper;
	@Autowired
	private ActAccountService actAccountService;
	@Autowired
	private MrsOrganService mrsOrganService;
	@Autowired
	private MrsLoginUserService mrsLoginUserService;

	@Override
	public MrsAccountDto findById(String id) {
		return mrsAccountService.findById(id);
	}

	@Override
	public MrsAccountDto findByCustId(String custId) {
		return mrsAccountService.findByCustId(custId);
	}

	@Override
	public MrsPlatformDto findByPlatformCode(String platformCode) {
		return mrsPlatformService.findByPlatform(platformCode);
	}

	@Override
	public void doAudit(MrsCertAuditDto auditDto) {
		mrsAccountService.doAudit(auditDto);
	}

	@Override
	public MrsAccountDto findByExOrgNo(String exOrgNo) {
		return mrsAccountService.findByExOrgNo(exOrgNo);
	}

	@Override
	public List<MrsAccountDto> selectByDto(MrsAccountDto queryparam) throws CbsCheckedException {
		return mrsAccountService.selectByDto(queryparam);
	}

	public void modifyAuthStatus(MrsAccountAuthStatus authStatus, String custId) {
		mrsAccountService.doModifyAuthStatus(authStatus, custId);
	}

	@Override
	public List<MrsAccountDto> findByCustIds(List<String> custIds) {
		return mrsAccountService.findByCustIds(custIds);
	}

	@Override
	public String findOneByCustId(String custId) {

		try {
			MrsCertAuditDto dto = mrsCertAuditService.findOneByCustId(custId, MrsCertAuditStatus.STATUS_2);
			if (dto == null) {
				log.info("客户[custId = " + custId + "]审核信息不存在...");
				return null;
			}
			return dto.getRemarks();
		} catch (Exception e) {
			log.error("审核原因查询失败：", e);
			return null;
		}

	}

	@Override
	public List<MrsAccountDto> findNameByCode(List<String> merCodes) {
		return mrsAccountService.findNameByCode(merCodes);
	}

	@Override
	public List<MrsCertFileDto> queryCertFile(MrsCertFileDto record) {
		return mrsAccountService.queryCertFile(record);
	}

	@Override
	public int insertSelective(MrsAccountDto dto) {
		return mrsAccountService.insertSelective(dto);

	}

	@Override
	public String createCustId() {
		return mrsAccountService.createCustId();
	}

	@Override
	public int updateByPrimaryKey(MrsAccountDto dto) {
		return mrsAccountService.updateByPrimaryKey(dto);
	}

//	@Override
//	public MrsAccountDto checkNotEffectivePersonBy3Element(String name, String certType, String certNo) {
//		MrsAccountDto accountDto = null;
//		try {
//			log.debug("MrsPersonAppServiceImpl.checkNotEffectivePersonBy3Element run....");
//			//List<MrsPersonDto> mrsPersonDto = mrsPersonService.findBy3Element(name, certType, certNo);
//			if (null != mrsPersonDto && StringUtils.isNotBlank(mrsPersonDto.getCustId())) {
//				accountDto = mrsAccountService.findByCustId(mrsPersonDto.getCustId());
//			}
//
//		} catch (Exception e) {
//			log.error("检查是否存在相同三要素失败:", e);
//			accountDto = new MrsAccountDto();
//
//		}
//		return accountDto;
//	}

	@Override
	public PageData<MrsAccountDto> listWidthRolesPage(PageData<MrsAccountDto> pageData, MrsAccountDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsAccountDto> list = mrsAccountDtoMapper.pageCust(searchDto);
		Page<MrsAccountDto> page = (Page<MrsAccountDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public List<ActAccountRespVO> findAccountDtoByCustIdAndSbjNo2(String custId, List<String> subjectNoList) {
		log.info("根据客户编号{}和二级科目获取账户数据", custId);
		List<ActAccountRespVO> actAccountRespVOList = new ArrayList<ActAccountRespVO>();
		// MRS_BASE_ACT("1001","客户基础账户"),
//		ActAccountRespVO base = new ActAccountRespVO();
//		ActAccountSbjNo2RespVO baseAct = new ActAccountSbjNo2RespVO();
//		// MRS_ZG_ACT("1002","客户资管账户"),
//		ActAccountRespVO zg = new ActAccountRespVO();
//		ActAccountSbjNo2RespVO zgAct = new ActAccountSbjNo2RespVO();
//		List<ActAccountDto> actList = actAccountService.findAccountDtoByCustIdAndSbjNo2(custId, subjectNoList);
//		if (actList != null && actList.size() > 0) {
//			for (ActAccountDto actDto : actList) {
//				// 2001001消费备付金
//				if (MrsActSubjectSub.MRS_BASE_ACT_SUB_XF.getValue().equals(actDto.getSubjectNo2())) {
//					baseAct.setXfAmt(actDto.getCashAmount());
//				}
//				// 2001002冻结备付金
//				else if (MrsActSubjectSub.MRS_BASE_ACT_SUB_DJ.getValue().equals(actDto.getSubjectNo2())) {
//					baseAct.setDjAmt(actDto.getCashAmount());
//				}
//				// 2001003业务保证金
//				else if (MrsActSubjectSub.MRS_BASE_ACT_SUB_YW.getValue().equals(actDto.getSubjectNo2())) {
//					baseAct.setBzAmt(actDto.getCashAmount());
//					// "2003004","待结算金额"
//				} else if (MrsActSubjectSub.MRS_BASE_ACT_SUB_JS.getValue().equals(actDto.getSubjectNo2())) {
//					baseAct.setJsAmt(actDto.getCashAmount());
//					// 2002001消费备付金
//				} else if (MrsActSubjectSub.MRS_ZG_ACT_SUB_XF.getValue().equals(actDto.getSubjectNo2())) {
//					zgAct.setXfAmt(actDto.getCashAmount());
//				}
//				// 2002002冻结备付金
//				else if (MrsActSubjectSub.MRS_ZG_ACT_SUB_DJ.getValue().equals(actDto.getSubjectNo2())) {
//					zgAct.setDjAmt(actDto.getCashAmount());
//				}
//				// 2002003业务保证金
//				else if (MrsActSubjectSub.MRS_ZG_ACT_SUB_YW.getValue().equals(actDto.getSubjectNo2())) {
//					zgAct.setBzAmt(actDto.getCashAmount());
//				} else {
//					log.info("二级科目{},没有配置对应的数据", actDto.getSubjectNo2());
//				}
//			}
//		} else {
//			log.info("根据客户编号{}和二级科目没有获取到对应的账户数据", custId);
//		}
//		// 组装数据
//		base.setCustId(custId);
//		base.setSubjectNo1(MrsActSubject.MRS_BASE_ACT.getValue());
//		base.setSubjectNo2(baseAct);
//		zg.setCustId(custId);
//		zg.setSubjectNo1(MrsActSubject.MRS_ZG_ACT.getValue());
//		zg.setSubjectNo2(zgAct);
//		actAccountRespVOList.add(base);
//		actAccountRespVOList.add(zg);
		return actAccountRespVOList;
	}

	@Override
	public ActAccountDto findByCustIdSubject2(String custId, String sub2) {
		List<ActAccountDto> items = actAccountService.findAcctIdByCustIdAndSubjectNo2(custId, sub2);
		if (items != null && items.size() == 1) {
			return items.get(0);
		}
		ActAccountDto respDto = new ActAccountDto();
		respDto.setCashAmount(0l);
		return respDto;
	}

	@Override
	public AccountRespVO findAccountDtoByCustId(String custId) {

		log.info("根据客户编号{}获取账户数据", custId);
		AccountRespVO accountRespVO = new AccountRespVO();
		List<ActAccountRespVO> actAccountRespVOList = new ArrayList<ActAccountRespVO>();
		// "客户基础账户"),
		ActAccountRespVO base = new ActAccountRespVO();
		ActAccountSbjNo2RespVO baseAct = new ActAccountSbjNo2RespVO();
		// "客户资管账户"),
		ActAccountRespVO zg = new ActAccountRespVO();
		ActAccountSbjNo2RespVO zgAct = new ActAccountSbjNo2RespVO();
		// 参与人基础资金
		ActAccountRespVO csr = new ActAccountRespVO();
		ActAccountSbjNo2RespVO csrAct = new ActAccountSbjNo2RespVO();
		List<ActAccountDto> actList;
		List<ActBusiRefSubDto> actSubjectList;
		try {
			// 查询用户账户（根据用户编号）
			actList = actAccountService.getUserAccounts(custId);
			//获取子账户配置
			actSubjectList = mrsAccountService.findActBusiRefSubByCustId(custId);
			if (actList != null && actList.size() > 0
					&&actSubjectList != null && actSubjectList.size() > 0) {
				for (ActAccountDto actDto : actList) {
					for (ActBusiRefSubDto actSubjectDto : actSubjectList) {
						if(actDto.getSubjectNo2().equals(actSubjectDto.getSub2No())){
							// 基础类资金
							if (SubAcctType.BASE.getValue().equals(actSubjectDto.getAcctTypeNo())) {
								accountRespVO.setCheckSubAcctTypeBase(true);
								// 消费备付金账户
								if (EActBusiRefSubBusiType.BALANCE_ACCOUNT.equals(actSubjectDto.getBusiType())) {
                                    baseAct.setAccountId(actDto.getAccountId());
                                    baseAct.setXfAmt(actDto.getCashAmount());
								}
								// 冻结备付金
								else if (EActBusiRefSubBusiType.FROZENABLE_ACCOUNT.equals(actSubjectDto.getBusiType())) {
									baseAct.setDjAmt(actDto.getCashAmount());
								}
								// 业务保证金
								else if (EActBusiRefSubBusiType.DEPOSIT_ACCOUNT.equals(actSubjectDto.getBusiType())) {
									baseAct.setBzAmt(actDto.getCashAmount());
								} 
							}
							// 资管类资金
							 if (SubAcctType.MANAGER.getValue().equals(actSubjectDto.getAcctTypeNo())) {
								accountRespVO.setCheckSubAcctTypeManager(true);
								// 消费备付金
								if (EActBusiRefSubBusiType.BALANCE_ACCOUNT.equals(actSubjectDto.getBusiType())) {
									zgAct.setXfAmt(actDto.getCashAmount());
                                    zgAct.setAccountId(actDto.getAccountId());
                                }
								// 冻结备付金
								else if (EActBusiRefSubBusiType.FROZENABLE_ACCOUNT.equals(actSubjectDto.getBusiType())) {
									zgAct.setDjAmt(actDto.getCashAmount());
								}
								// 业务保证金
								else if (EActBusiRefSubBusiType.DEPOSIT_ACCOUNT.equals(actSubjectDto.getBusiType())) {
									zgAct.setBzAmt(actDto.getCashAmount());
								}
							} 
							// 参与人基础资金
							 if (SubAcctType.PARTICIPANT_BASE.getValue().equals(actSubjectDto.getAcctTypeNo())) {
								accountRespVO.setCheckSubAcctTypeCyr(true);
								// 消费备付金账户
								if (EActBusiRefSubBusiType.BALANCE_ACCOUNT.equals(actSubjectDto.getBusiType())) {
									csrAct.setXfAmt(actDto.getCashAmount());
                                    csrAct.setAccountId(actDto.getAccountId());
                                }
								// 冻结备付金
								else if (EActBusiRefSubBusiType.FROZENABLE_ACCOUNT.equals(actSubjectDto.getBusiType())) {
									csrAct.setDjAmt(actDto.getCashAmount());
								}
								// 业务保证金
								else if (EActBusiRefSubBusiType.DEPOSIT_ACCOUNT.equals(actSubjectDto.getBusiType())) {
									csrAct.setBzAmt(actDto.getCashAmount());
								} 
								// 待结算金额
								else if (EActBusiRefSubBusiType.WAIT_CLEAR.equals(actSubjectDto.getBusiType())) {
									csrAct.setJsAmt(actDto.getCashAmount());
								}
							}
							
						}
					}
				}
			} else {
				log.info("根据客户编号{}和二级科目没有获取到对应的账户数据", custId);
			}
			// 组装数据
			base.setCustId(custId);
			base.setSubjectNo1(SubAcctType.BASE.getValue());
			base.setSubjectNo2(baseAct);
			
			zg.setCustId(custId);
			zg.setSubjectNo1(SubAcctType.MANAGER.getValue());
			zg.setSubjectNo2(zgAct);
			ActAccountSbjNo2RespVO amtVo = zg.getSubjectNo2();
			if (amtVo != null) {
				amtVo.setSumAmt(amtVo.getXfAmt() + amtVo.getJsAmt() + amtVo.getDjAmt() + amtVo.getBzAmt());
				zg.setSubjectNo2(amtVo);
			}

			csr.setCustId(custId);
			csr.setSubjectNo1(SubAcctType.PARTICIPANT_BASE.getValue());
			csr.setSubjectNo2(csrAct);
			
			actAccountRespVOList.add(base);
			actAccountRespVOList.add(zg);
			actAccountRespVOList.add(csr);
			accountRespVO.setActAccountRespVOList(actAccountRespVOList);
		} catch (Exception e) {
			log.error("获取账户数据失败:", ExceptionProcUtil.getExceptionDesc(e));
			return accountRespVO;
		}
		return accountRespVO;

	}

	@Override
	public ActAccountDto findByCustIdSubBusiType(String custId, String subAcctType, String busiType) {
		List<String> acctTypeNos = new ArrayList<String>();
		List<EActBusiRefSubBusiType> busiTypes = new ArrayList<EActBusiRefSubBusiType>();
		acctTypeNos.add(subAcctType);
		busiTypes.add(EActBusiRefSubBusiType.getEnum(busiType));
		List<ActBusiRefSubDto> actList = mrsAccountService.findByAcctTypeNos(acctTypeNos, busiTypes);
		if (actList != null && actList.size() > 0) {
			List<ActAccountDto> accountList = actAccountService.findAcctIdByCustIdAndSubjectNo2(custId, actList.get(0).getSub2No());
			if (accountList != null && accountList.size() > 0) {
				return accountList.get(0);
			}
		}
		ActAccountDto actAccountDto = new ActAccountDto();
		actAccountDto.setCashAmount(0l);
		return actAccountDto;
	}

	@Override
	public MrsAccountDto checkOrganAccountEffBy3Elements(String name, String certType, String certNo,
			String accountStatus) {
		String organizeCode = null;
		String revenueCode = null;
		String businessLicence = null;
		String socialCreditCode = null;
		String organOtherCode = null;
		switch (certType) {
		case "71":
			organizeCode = certNo;
			break;
		case "72":
			revenueCode = certNo;
			break;
		case "73":
			businessLicence = certNo;
			break;
		case "74":
			socialCreditCode = certNo;
			break;
		case "99":
			organOtherCode = certNo;
			break;
		default:
			break;
		}

		List<MrsOrganDto> mrsOrganDtos = mrsOrganService.findBy3ElementAndAcountStatus(name, socialCreditCode,
				organizeCode, revenueCode, businessLicence, organOtherCode, accountStatus);
		if (!CollectionUtil.isEmpty(mrsOrganDtos)) {
			MrsOrganDto organ = mrsOrganDtos.get(0);

			MrsAccountDto account = findByCustId(organ.getCustId());
			return account;
		} else {
			return null;
		}

	}

	@Override
	public MrsUserAccountDto isExistIsMain(String isMain) {
		return mrsLoginUserService.isExistIsMain(isMain);
	}

	@Override
	public boolean check3ElmentIsMax(String certType, String certNo, String name, String customerType) {
		return mrsAccountService.check3ElmentIsMax(certType,certNo,name,customerType);
	}

	@Override
	public boolean checkCustId(String custId) {
		return mrsAccountService.checkCustId(custId);
	}

	@Override
	public List<MrsAccountDto> findPersonBy3EleAndStatus(String customerName, String type, String number,
			String accountStatus) {
		return mrsAccountService.findPersonBy3EleAndStatus(customerName, type, number, accountStatus);
	}
 
	@Override
	public PageData<MrsAccountDto> pageMrsAccountList(PageData<MrsAccountDto> pageData, MrsAccountDto queryParam) {
		 
		return mrsAccountService.pageMrsAccountList(pageData,queryParam);
	}

	@Override
	public PageData<MrsAccountDto> payMrsAccountListPage(PageData<MrsAccountDto> pageData, MrsAccountDto searchDto) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsAccountDto> list = mrsAccountDtoMapper.payMrsAccountListPage(searchDto);
		Page<MrsAccountDto> page = (Page<MrsAccountDto>) list;
		pageData.setTotal(page.getTotal());
		pageData.setRows(list);
		return pageData;
	}

	@Override
	public List<MrsAccountDto> findOrgListByName(String orgName) {
		 
		return mrsAccountService.findOrgListByName(orgName);
	}
}
