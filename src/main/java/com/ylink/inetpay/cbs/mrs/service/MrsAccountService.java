package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.constant.AaccountType;
import com.ylink.inetpay.common.core.constant.EActBusiRefSubBusiType;
import com.ylink.inetpay.common.core.constant.MrsAccountAuthStatus;
import com.ylink.inetpay.common.core.constant.MrsCustomerType;
import com.ylink.inetpay.common.project.account.dto.ActBusiRefSubDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertAuditDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsCertFileDto;
import com.ylink.inetpay.common.project.cbs.exception.CbsCheckedException;
import com.ylink.inetpay.common.project.portal.dto.AccountMsg;

public interface MrsAccountService {

	public MrsAccountDto findById(String id);

	public MrsAccountDto findByCustId(String custId);

	MrsAccountDto findByExOrgNo(String exOrgNo);

	/**
	 * 认证服务
	 * 
	 * @param dto
	 */
	public void doAudit(MrsCertAuditDto dto);

	public List<AccountMsg> findPersonAccountMsgByLoginUserId(String loginId);

	public List<AccountMsg> findOrganAccountMsgByLoginUserId(String loginId);

	public void doModifyAuthStatus(MrsAccountAuthStatus authStatus, String custId);

	public MrsCustomerType getCustTypeByLoginId(String id);

	public List<MrsAccountDto> findByCustIds(List<String> custIds);

	/** 查找需要生成对账文件的商户 **/
	public List<MrsAccountDto> selectByDto(MrsAccountDto queryparam);

	/**
	 * 根据custId更新个人客户的customerCode
	 * 
	 * @param custId
	 * @param customerCode
	 * @throws CbsCheckedException
	 */
	public void updatePersonCustomerCode(String custId, String customerCode) throws CbsCheckedException;

	/**
	 * 根据custId更新个人客户的customerCode
	 * 
	 * @param custId
	 * @param customerCode
	 */
	public void updateOrganCustomerCode(String custId, String customerCode) throws CbsCheckedException;

	/**
	 * 根据custId更新产品客户的customerCode
	 * 
	 * @param custId
	 * @param customerCode
	 */
	public void updateProductCustomerCode(String custId, String customerCode) throws CbsCheckedException;

	public List<MrsAccountDto> findNameByCode(List<String> merCodes);

	/**
	 * 查询客户上传的文件
	 * 
	 * @param record
	 * @return
	 */
	public List<MrsCertFileDto> queryCertFile(MrsCertFileDto record);

	/**
	 * 保存一户通账户
	 */
	public int insertSelective(MrsAccountDto dto);

	/**
	 * 创建一户通账号custID
	 */
	public String createCustId();

	public int updateByPrimaryKey(MrsAccountDto dto);
	
	public void update(MrsAccountDto dto);
	/**
	 * 根据二级科目获取子账户配置
	 * 
	 * @param subjectNoList
	 * @return
	 */
//	List<MrsAccountDto> findAccountDtoBySbjNo2(List<String> subjectNoList);
	
	/**
	 * 根据一级科目，科目类别，查询资金账号信息
	 * 
	 * @param sub2
	 * @param busiType
	 * @return
	 */
//	List<MrsAccountDto> findAccountDtoBySubAcctTypeAndBusiType(String subAcctType, String busiType);

	/**
	 * 根据科目类别，查询资金账号信息
	 * 
	 * @param subAcctType
	 * @return
	 */
//	List<MrsAccountDto> findAccountDtoBySubAcctType(String subAcctType);

	/**
	 * 
	 * 方法描述：根据三要素校验一户通是否已经达到设置的最大限度， 如果已经达到返回false 否则返回true 创建人：ydx
	 * 创建时间：2017年3月9日 下午3:40:40
	 * 
	 * @param certType
	 *            证件类型
	 * @param certNo
	 *            证件号码
	 * @param name
	 *            名称
	 * @param customerType
	 *            个人，产品
	 * @return
	 */
	public boolean check3ElmentIsMax(String certType, String certNo, String name, String customerType);

	/**
	 * 
	 * 方法描述：校验一户通号码是否可用 查询一户通表审核表 创建人：ydx 创建时间：2017年3月9日 下午3:45:34
	 * 
	 * @param custId
	 *            一户通编号
	 * @return
	 */
	public boolean checkCustId(String custId);

	/**
	 * 根据个人的三要素获取所有账户数据
	 * 
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByPerson3Element(String customerName, String credentialsType, String credentialsNumber);
	/**
	 * 	根据请求参数中一户通账号和一户通状态为非“注销”状态关联查询“个人客户信息表”和“一户通账户表”
	 * @param custId
	 * @return
	 */
	List<MrsAccountDto> findByUpdatePerson3Element(String custId);
	/**
	 * 	根据请求参数中一户通账号和一户通状态为非“注销”状态关联查询“机构客户信息表”和“一户通账户表”
	 * @param custId
	 * @return
	 */
	List<MrsAccountDto> findByUpdateOrgan3Element(String custId);
	/**
	 * 	根据请求参数中一户通账号和一户通状态为非“注销”状态关联查询“产品客户信息表”和“一户通账户表”
	 * @param custId
	 * @return
	 */
	List<MrsAccountDto> findByUpdateProduct3Element(String custId);
	/**
	 * 根据个人的三要素获取所有账户数据-那么首先取认证状态为“认证成功”或“无需认证”记录，创建时间为最近的记录。
	 * 
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByPerson3ElementLast(String customerName, String credentialsType, String credentialsNumber);
	/**
	 * 根据的三要素获取所有账户数据
	 * 
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByOrganAsset3Element(String customerName, String credentialsType, String credentialsNumber);

	/**
	 * 根据机构的三要素获取所有账户数据-那么首先取认证状态为“认证成功”或“无需认证”记录，创建时间为最近的记录。
	 * 
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByOrganAsset3ElementLast(String customerName, String credentialsType, String credentialsNumber);
	/**
	 * 根据产品的三要素获取所有账户数据
	 * 
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByProduct3Element(String customerName, String credentialsType, String credentialsNumber);

	/**
	 * 根据产品的三要素获取所有账户数据-那么首先取认证状态为“认证成功”或“无需认证”记录，创建时间为最近的记录。
	 * 
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByProduct3ElementLast(String customerName, String credentialsType, String credentialsNumber);

	/**
	 * 根据机构的三要素获取所有账户数据
	 * 
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByOrgan3Element(String name,
			String socialCreditCode,
			String organizeCode,
			String revenueCode,
			 String businessLicence,String organOtherCode);

	/**
	 * 根据机构的三要素获取所有账户数据-那么首先取认证状态为“认证成功”或“无需认证”记录，创建时间为最近的记录。
	 * 
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByOrgan3ElementLast(String name,
			String socialCreditCode,
			String organizeCode,
			String revenueCode,
			 String businessLicence,String organOtherCode);

	/**
	 * 
	 *方法描述：根据三要素 一户通状态查询个人一户通信息
	 * 创建人：ydx
	 * 创建时间：2017年3月9日 下午8:51:00
	 * @param customerName
	 * @param type
	 * @param number
	 * @param accountStatus
	 * @return
	 */
	public List<MrsAccountDto> findPersonBy3EleAndStatus(String customerName, String type, String number,String accountStatus);
	/**
	 * 
	 *方法描述：根据客户类型生成一户通号码并返回
	 * 创建人：ydx
	 * 创建时间：2017年3月22日 下午2:56:53
	 * @param custType 客户类型（个人，机构，产品）
	 * @return
	 */
	public String checkCustTypeReturnId(MrsCustomerType custType);
	
	/**
	 * 方法描述：根据客户类型一户通类型生成一户通号码并返回
	 * @param custType  客户类型（个人，机构，产品）
	 * @param accountType  一户通类型（保险一户通， 参与人一户通）
	 * @return
	 */
	public String checkCustTypeReturnId(MrsCustomerType custType, AaccountType accountType);
 
	
 /**
	 * 获取一户机构通账号
	 * @方法描述:  根据 一户通类型1 客户类型1 2 9 查询 
	 * @作者： yc
	 * @日期： 2017年4月19日-上午10:24:35
	 * @return 
	 * @返回类型： List<MrsAccountDto>
	 */				
	 public PageData<MrsAccountDto> pageMrsAccountList(PageData<MrsAccountDto> pageData, MrsAccountDto queryParam);
	 /**
		 * 
		 *方法描述：根据一户通号码查询一户通开通的科目，科目所属的大类，科目属性
		 * 创建人：ydx
		 * 创建时间：2017年4月18日 下午8:36:25
		 * @param custId
		 * @return
		 */
	public List<ActBusiRefSubDto> findActBusiRefSubByCustId( String custId);
	
	/**
	 * 根据微信号查询一户通信息
	 * @param weChatId
	 * @return
	 */
	public List<MrsAccountDto> findByWeChatId(String weChatId);
	

	public List<MrsAccountDto> findOrgListByName(String orgName);
	
	/**
     * 
     *方法描述：根据账户类型编号查询科目编号
     * @param acctTypeNos
     * @return
     */
    List<String> findSub2NoByAcctTypeNos(List<String> acctTypeNos);
    /**
     * 
     *方法描述：根据账户类型编号查询
     * 创建人：ydx
     * 创建时间：2017年4月18日 下午8:14:52
     * @param acctTypeNos
     * @return
     */
    List<ActBusiRefSubDto> findByAcctTypeNos(List<String> acctTypeNos,List<EActBusiRefSubBusiType> busiTypes);
    
    /**
     * 根据登陆用户Id查询一互通信息
     * @param id
     * @return
     */
    List<MrsAccountDto> findByLoginUserId(String id);
}
