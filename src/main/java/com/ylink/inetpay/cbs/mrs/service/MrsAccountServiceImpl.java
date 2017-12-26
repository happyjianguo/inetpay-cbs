package com.ylink.inetpay.cbs.mrs.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ylinkpay.framework.core.model.PageData;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ylink.eu.util.tools.CollectionUtil;
import com.ylink.inetpay.cbs.act.dao.ActBusiRefSubDtoMapper;
import com.ylink.inetpay.cbs.bis.service.BisSysParamService;
import com.ylink.inetpay.cbs.common.CbsConstants;
import com.ylink.inetpay.cbs.mrs.constants.MrsConstants;
import com.ylink.inetpay.cbs.mrs.dao.MrsAccountDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsAduitInfoDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsCertAuditDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsCertFileDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsConfAcctDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsPersonDtoMapper;
import com.ylink.inetpay.cbs.mrs.dao.MrsProductDtoMapper;
import com.ylink.inetpay.common.core.SystemParamConstants;
import com.ylink.inetpay.common.core.constant.AaccountType;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.core.constant.MrsAccountStatus;
import com.ylink.inetpay.common.core.constant.MrsCertAuditStatus;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.core.util.StringUtils;
import com.ylink.inetpay.common.project.account.dto.ActBusiRefSubDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitInfoDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsProductDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.dto.AccountMsg;

@Service("mrsAccountService")
public class MrsAccountServiceImpl implements MrsAccountService {

	private static Logger log = LoggerFactory.getLogger(MrsAccountServiceImpl.class);
	
	@Autowired
	private MrsAccountDtoMapper mrsAccountDtoMapper;
	@Autowired
	private MrsPersonDtoMapper mrsPersonDtoMapper;
	@Autowired
	private MrsCertAuditDtoMapper mrsCertAuditDtoMapper;
	@Autowired
	private MrsProductDtoMapper mrsProductDtoMapper;
	@Autowired
	private MrsCertFileDtoMapper mrsCertFileDtoMapper;
	@Autowired
	private MrsAduitInfoDtoMapper mrsAduitInfoDtoMapper;
	@Autowired
	private BisSysParamService bisSysParamService;
	@Autowired
	private MrsConfAcctDtoMapper mrsConfAcctDtoMapper;
	@Autowired
	private ActBusiRefSubDtoMapper actBusiRefSubDtoMapper;
	@Override
	public MrsAccountDto findById(String id) {
		return mrsAccountDtoMapper.selectByPrimaryKey(id);
	}

	@Override
	public MrsAccountDto findByCustId(String custId) {
		return mrsAccountDtoMapper.findByCustId(custId);
	}
	
	@Override
	public MrsAccountDto findByExOrgNo(String exOrgNo) {
		return mrsAccountDtoMapper.findByExOrgNo(exOrgNo);
	}

	@Override
	public List<MrsAccountDto> selectByDto(MrsAccountDto queryparam){
		return mrsAccountDtoMapper.selectByDto(queryparam);
	}
	/**
	 * 保存认证信息
	 * 更新MrsAccountDto认证结果
	 */
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public void doAudit(MrsCertAuditDto dto) {
		MrsAccountDto acountDto = mrsAccountDtoMapper.findByCustId(dto.getCustId());
		if(dto.getStatus().equals(MrsCertAuditStatus.STATUS_1.getValue())) {
			acountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_2.getValue());
			acountDto.setAccountStatus(MrsAccountStatus.ACCOUNT_STATUS_0.getValue());
		} else {
			acountDto.setAuthStatus(MrsAccountAuthStatus.MAAS_3.getValue());
		}
		mrsCertAuditDtoMapper.insert(dto);
		mrsAccountDtoMapper.updateByPrimaryKey(acountDto);
	}

	@Override
	public List<AccountMsg> findPersonAccountMsgByLoginUserId(String loginId) {
		return mrsAccountDtoMapper.findPersonAccountMsgByLoginUserId(loginId);
	}

	@Override
	public List<AccountMsg> findOrganAccountMsgByLoginUserId(String loginId) {
		return mrsAccountDtoMapper.findOrganAccountMsgByLoginUserId(loginId);
	}

	@Override
	public void doModifyAuthStatus(MrsAccountAuthStatus authStatus, String custId) {
		MrsAccountDto dto = mrsAccountDtoMapper.findByCustId(custId);
		dto.setAuthStatus(authStatus.getValue());
		mrsAccountDtoMapper.updateByPrimaryKey(dto);
	}

	@Override
	public MrsCustomerType getCustTypeByLoginId(String id) {
		List<MrsAccountDto> list = mrsAccountDtoMapper.findMrsAccountByLoginUserId(id);
		if(CollectionUtil.isEmpty(list)) {
			return null;
		}
		String customerType = list.get(0).getCustomerType();
		return MrsCustomerType.getEnum(customerType);
	}

	@Override
	public List<MrsAccountDto> findByCustIds(List<String> custIds) {
		return mrsAccountDtoMapper.findByCustIds(custIds);
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public void updatePersonCustomerCode(String custId, String customerCode) throws CbsCheckedException{
		Date date = new Date();
		int row = mrsAccountDtoMapper.updateCustomerCodeByCustId(custId, customerCode, date);
		if(row < 1) {
			log.error("修改数据TB_MRS_ACCOUNT[custId="+custId+"]不存在!");
			throw new CbsCheckedException("修改失败");
		}
		/*row = mrsPersonDtoMapper.updateCustomerCodeByCustId(custId, customerCode, date);
		if(row < 1) {
			log.error("修改数据TB_MRS_PERSON[custId="+custId+"]不存在!");
			throw new CbsCheckedException("修改失败");
		}*/
	}

	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public void updateOrganCustomerCode(String custId, String customerCode) throws CbsCheckedException {
		Date date = new Date();
		int row = mrsAccountDtoMapper.updateCustomerCodeByCustId(custId, customerCode, date);
		if(row < 1) {
			log.error("修改数据TB_MRS_ACCOUNT[custId="+custId+"]不存在!");
			throw new CbsCheckedException("修改失败");
		}
		/*row = mrsOrganDtoMapper.updateCustomerCodeByCustId(custId, customerCode, date);
		if(row < 1) {
			log.error("修改数据TB_MRS_ORGAN[custId="+custId+"]不存在!");
			throw new CbsCheckedException("修改失败");
		}*/
	}
	
	@Override
	@Transactional(value=CbsConstants.TX_MANAGER_MRS)
	public void updateProductCustomerCode(String custId, String customerCode) throws CbsCheckedException {
		Date date = new Date();
		int row = mrsAccountDtoMapper.updateCustomerCodeByCustId(custId, customerCode, date);
		if(row < 1) {
			log.error("修改数据TB_MRS_ACCOUNT[custId="+custId+"]不存在!");
			throw new CbsCheckedException("修改失败");
		}
		/*row = mrsProductDtoMapper.updateCustomerCodeByCustId(custId, customerCode, date);
		if(row < 1) {
			log.error("修改数据TB_MRS_Product[custId="+custId+"]不存在!");
			throw new CbsCheckedException("修改失败");
		}*/
	}
	@Override
	public List<MrsAccountDto> findNameByCode(List<String> merCodes) {
		return mrsAccountDtoMapper.findNameByCode(merCodes);
	}

	@Override
	public List<MrsCertFileDto> queryCertFile(MrsCertFileDto record) {
		return mrsCertFileDtoMapper.queryCertFile(record);
	}

	@Override
	public int insertSelective(MrsAccountDto dto) {
		return mrsAccountDtoMapper.insertSelective(dto);
	}

	@Override
	public String createCustId() {
		String custId = "";
		custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
		custId = MrsConstants.PERSON_ACCOUNT_PREFIX + StringUtils.format(11, custId);
		return custId;
	}

	@Override
	public int updateByPrimaryKey(MrsAccountDto dto) {
		return mrsAccountDtoMapper.updateByPrimaryKey(dto);
	}

//	@Override
//	public List<MrsAccountDto> findAccountDtoBySbjNo2(List<String> subjectNoList) {
//		return mrsAccountDtoMapper.findAccountDtoBySbjNo2(subjectNoList);
//	}
//
//	@Override
//	public List<MrsAccountDto> findAccountDtoBySubAcctTypeAndBusiType(String subAcctType, String busiType) {
//		return mrsAccountDtoMapper.findAccountDtoBySubAcctTypeAndBusiType(subAcctType, busiType);
//	}
//
//	@Override
//	public List<MrsAccountDto> findAccountDtoBySubAcctType(String subAcctType) {
//		return mrsAccountDtoMapper.findAccountDtoBySubAcctType(subAcctType);
//	}
	
	@Override
	public boolean check3ElmentIsMax(String certType, String certNo, String name, String customerType) {
		
		int length = 0;
		int maxLength = 0;
		
		//根据客户类型查询不同的信息查询注销意外的其他状态
		if(MrsCustomerType.MCT_0.getValue().equals(customerType)){
			//个人用户
			List<MrsPersonDto> mrsPersonDtos = mrsPersonDtoMapper.findBy3Element(name, certType, certNo);
			if(!CollectionUtil.isEmpty(mrsPersonDtos)){
				 length = mrsPersonDtos.size();
			}
			
		}else if (MrsCustomerType.MCT_2.getValue().equals(customerType)) {
			//产品客户
			List<MrsProductDto> mrsProductDtos = mrsProductDtoMapper.findBy3ElementAndNoEff(name, certType, certNo);
			if(!CollectionUtil.isEmpty(mrsProductDtos)){
				 length = mrsProductDtos.size();
			}
			
		}else {
			log.error("调用三要素查询上限接口，传入参数不正确！");
			return false;
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
	}

	@Override
	public boolean checkCustId(String custId) {
		try {
			//查询一户通表
			MrsAccountDto account = findByCustId(custId);
			//查询审核主要信息表 审核状态为待审核，审核中的
			List<MrsAduitInfoDto> aduitInfo = mrsAduitInfoDtoMapper.findByCustIdNoFinalStatus(custId);
			
			//判断两个地方都没用到的一户通号，就返回True 否则返回false
			if(account==null && CollectionUtil.isEmpty(aduitInfo)){
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<MrsAccountDto> findByPerson3Element(String customerName, String credentialsType,
			String credentialsNumber) {
		return mrsAccountDtoMapper.findByPerson3Element(customerName, credentialsType, credentialsNumber);
	}
	@Override
	public List<MrsAccountDto> findByPerson3ElementLast(String customerName, String credentialsType,
			String credentialsNumber) {
		return mrsAccountDtoMapper.findByPerson3ElementLast(customerName, credentialsType, credentialsNumber);
	}
	@Override
	public List<MrsAccountDto> findPersonBy3EleAndStatus(String customerName, String type, String number,
			String accountStatus) {
		return mrsAccountDtoMapper.findPersonBy3EleAndStatus(customerName,type,number,accountStatus);
	}

	@Override
	public List<MrsAccountDto> findByOrgan3Element(String name, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence,String organOtherCode) {
		return mrsAccountDtoMapper.findByOrgan3Element(name, socialCreditCode, organizeCode, revenueCode, businessLicence, organOtherCode);
	}

	@Override
	public List<MrsAccountDto> findByOrgan3ElementLast(String name, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence,String organOtherCode) {
		return mrsAccountDtoMapper.findByOrgan3ElementLast(name, socialCreditCode, organizeCode, revenueCode, businessLicence, organOtherCode);
	}

	@Override
	public List<MrsAccountDto> findByOrganAsset3Element(String customerName, String credentialsType,
			String credentialsNumber) {
		return mrsAccountDtoMapper.findByOrganAsset3Element(customerName, credentialsType, credentialsNumber);
	}

	@Override
	public List<MrsAccountDto> findByOrganAsset3ElementLast(String customerName, String credentialsType,
			String credentialsNumber) {
		return mrsAccountDtoMapper.findByOrganAsset3ElementLast(customerName, credentialsType, credentialsNumber);
	}

	@Override
	public List<MrsAccountDto> findByProduct3Element(String customerName, String credentialsType,
			String credentialsNumber) {
		return mrsAccountDtoMapper.findByProduct3Element(customerName, credentialsType, credentialsNumber);
	}

	@Override
	public List<MrsAccountDto> findByProduct3ElementLast(String customerName, String credentialsType,
			String credentialsNumber) {
		return mrsAccountDtoMapper.findByProduct3ElementLast(customerName, credentialsType, credentialsNumber);
	}

	@Override
	public List<MrsAccountDto> findByUpdatePerson3Element(String custId) {
		return mrsAccountDtoMapper.findByUpdatePerson3Element(custId);
	}

	@Override
	public void update(MrsAccountDto dto) {
		mrsAccountDtoMapper.updateByPrimaryKeySelective(dto);
	}

	@Override
	public List<MrsAccountDto> findByUpdateOrgan3Element(String custId) {
		return mrsAccountDtoMapper.findByUpdateOrgan3Element(custId);
	}

	@Override
	public List<MrsAccountDto> findByUpdateProduct3Element(String custId) {
		return mrsAccountDtoMapper.findByUpdateProduct3Element(custId);
	}

	@Override
	public String checkCustTypeReturnId(MrsCustomerType custType) {
		//递归
		String custId = null;
		if( custType != null ){
			if(MrsCustomerType.MCT_0.equals(custType)){
				//个人生成规则
				custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
				custId = MrsConstants.PERSON_ACCOUNT_PREFIX + StringUtils.format(11, custId);
			}else {
				//机构或者产品生成规则
				custId = mrsAccountDtoMapper.getMrsNonPersonSeqVal();
				custId = MrsConstants.ORGAN_ACCOUONT_PREFIX + StringUtils.format(11, custId);
			}
		}else {
			return null;
		}
		boolean checkFlag = checkCustId(custId);
		// 如果返回True表编号可用
		if (!checkFlag) {
			return checkCustTypeReturnId(custType);
		}else{
			return custId;
		}
	}


	@Override
	public String checkCustTypeReturnId(MrsCustomerType custType, AaccountType accountType) {
		
		String custId = null;
		
		if( custType != null ){
			if(MrsCustomerType.MCT_0.equals(custType)){
				//个人生成规则
				custId = mrsAccountDtoMapper.getMrsPersonSeqVal();
				custId = MrsConstants.PERSON_ACCOUNT_PREFIX + StringUtils.format(11, custId);
			}
			else if(MrsCustomerType.MCT_1.equals(custType) && AaccountType.CYRYHT.equals(accountType)){
				// 机构 参与人一户通
				custId = mrsAccountDtoMapper.getMrsOrganAffSeqVal();
			}
			else {
				//机构或者产品生成规则
				custId = mrsAccountDtoMapper.getMrsNonPersonSeqVal();
				custId = MrsConstants.ORGAN_ACCOUONT_PREFIX + StringUtils.format(11, custId);
			}
		}else {
			return null;
		}
		boolean checkFlag = checkCustId(custId);
		// 如果返回True表编号可用
		if (!checkFlag) {
			return checkCustTypeReturnId(custType, accountType);
		}else{
			return custId;
		}
	}

 
	@Override
	public PageData<MrsAccountDto> pageMrsAccountList(PageData<MrsAccountDto> pageData, MrsAccountDto queryParam) {
		PageHelper.startPage(pageData.getPageNumber(), pageData.getPageSize());
		List<MrsAccountDto> items = mrsAccountDtoMapper.list(queryParam);
		Page<MrsAccountDto> page = (Page<MrsAccountDto>)items;
		pageData.setRows(items);
		pageData.setTotal(page.getTotal());
		return pageData;
	}
	@Override
	public List<ActBusiRefSubDto> findActBusiRefSubByCustId(String custId) {
		//先查询一户通号查询的子账户信息
		List<String> acctTypeNos = mrsConfAcctDtoMapper.findSubAcctTypeByCustId(custId);
		if(!CollectionUtil.isEmpty(acctTypeNos)){
			return actBusiRefSubDtoMapper.findByAcctTypeNos(acctTypeNos, EActBusiRefSubBusiType.getTypeEnum());
		}
		return null;
	}

	@Override
	public List<MrsAccountDto> findByWeChatId(String weChatId) {
		return mrsAccountDtoMapper.findByWeChatId(weChatId);
	}

	@Override
	public List<MrsAccountDto> findOrgListByName(String orgName) {
		return mrsAccountDtoMapper.findOrgListByName(orgName);
	}
	
	/**
     *方法描述：根据账户类型编号查询科目编号
     * @param acctTypeNos
     * @return
     */
	@Override
	public List<String> findSub2NoByAcctTypeNos(List<String> acctTypeNos) {
		return actBusiRefSubDtoMapper.findSub2NoByAcctTypeNos(acctTypeNos);
	}

	@Override
	public List<ActBusiRefSubDto> findByAcctTypeNos(List<String> acctTypeNos, List<EActBusiRefSubBusiType> busiTypes) {
		return actBusiRefSubDtoMapper.findByAcctTypeNos(acctTypeNos, EActBusiRefSubBusiType.getTypeEnum());
	}

	@Override
	public List<MrsAccountDto> findByLoginUserId(String id) {
		return mrsAccountDtoMapper.findByLoginUserId(id);
	}
}
