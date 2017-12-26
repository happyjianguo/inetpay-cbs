package com.ylink.inetpay.cbs.mrs.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.portal.dto.AccountMsg;

@MybatisMapper("mrsAccountDtoMapper")
public interface MrsAccountDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsAccountDto record);

    int insertSelective(MrsAccountDto record);

    MrsAccountDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsAccountDto record);
    
    /**获取需要生产对账文件的商户 **/
    List<MrsAccountDto> selectByDto(MrsAccountDto record);

    int updateByPrimaryKey(MrsAccountDto record);
    
    /**
     * 根据账户编号查询一户通信息
     * @param id
     * @return
     */
    MrsAccountDto findByCustId(@Param("custId")String custId);
    
    /**
     * 根据机构号查询账户信息
     * @param exOrgNo
     * @return
     */
    MrsAccountDto findByExOrgNo(@Param("exOrgNo")String exOrgNo);

	List<MrsAccountDto> findMrsAccountByLoginUserId(@Param("loginId")String loginId);

	List<AccountMsg> findPersonAccountMsgByLoginUserId(String loginId);
	

	List<AccountMsg> findOrganAccountMsgByLoginUserId(String loginId);

	int updateAcntStatusByCustId(@Param("custId")String custId, @Param("accountStatus")String accountStatus, @Param("authStatus")String authStatus, @Param("updateTime")Date date);
	
	MrsAccountDto selectByCustIdAndStatus(@Param("custId")String custId, @Param("accountStatus")String accountStatus);
	
	int updateAuthStatus(@Param("custId")String custId, @Param("authStatus")String authStatus, @Param("updateTime")Date updateTime);
	
	int updateCustomerCodeByCustId(@Param("custId")String custId, @Param("customerCode")String customerCode, @Param("updateTime")Date updateTime);

    /**
     * 根据一户通编号、姓名、证件类型、证件号码等信息查询一户通账户
     * @param custId
     * @param name
     * @param type
     * @param number
     * @return
     */
    List<MrsAccountDto> findByCustIdAnd3Element(@Param("name")String name, 
    		@Param("type")String type, @Param("number")String number );
	
    /**
     * 根据一户通编号、姓名、证件类型、证件号码等信息查询一户通账户
     * 为避免影响新增此方法
     * @param name
     * @param type
     * @param number
     * @return
     */
    List<MrsAccountDto> findByCustIdAnd3Elements(@Param("name")String name, 
    		@Param("type")String type, @Param("number")String number );
    /**
	 * 获取自然人保险一户通账户序列号
	 * @return
	 */
	String getMrsPersonSeqVal();
    /**
	 * 获取非自然人保险一户通账户序列号
	 * @return
	 */
	String getMrsNonPersonSeqVal();
	
	/**
	 * 获取非自然人参与人保险一户通账户序列号
	 * @return
	 */
	String getMrsOrganAffSeqVal();
    /**
	 * 获取自然人保险一户通的子账户序列号
	 * @return
	 */
	String getMrsSubInsureSeqVal();
    /**
	 * 获取非自然人保险一户通的子账户序列号
	 * @return
	 */
	String getMrsSubInsurantSeqVal();
	/**
	 * 根据用户id集合获取用户集合
	 * @param custIds
	 * @return
	 */
	List<MrsAccountDto> findByCustIds(@Param("custIds")List<String> custIds);

    List<MrsAccountDto> findNameByCode(@Param("merCodes")List<String> merCodes);
    
    /**
     * 分页查询用户的信息
     * @param dto
     * @return
     */
    List<MrsAccountDto> pageCust(MrsAccountDto dto);

    /**
	 * 
	 *方法描述：根据三要素查询机构信息
	 * 创建人：ydx
	 * 创建时间：2017年2月27日 下午11:53:04
	 * @param customerName
	 * @param socialCreditCode
	 * @param organizeCode
	 * @param revenueCode
	 * @param businessLicence
	 * @param organOtherCode
	 * @return
	 */
	public List<MrsAccountDto> findOganBy3Element(@Param("customerName")String customerName,@Param("socialCreditCode") String socialCreditCode, 
			@Param("organizeCode")String organizeCode, @Param("revenueCode")String revenueCode, 
			@Param("businessLicence")String businessLicence,@Param("organOtherCode")String organOtherCode);
	/**
	 * 根据二级科目获取子账户配置
	 * @param subjectNoList
	 * @return
	 */
//	List<MrsAccountDto> findAccountDtoBySbjNo2(@Param("subjectNoList")List<String> subjectNoList);
	/**
	 * 
	 *方法描述：根据二级科目 一户通编号 获取子账户配置 
	 * 创建人：ydx
	 * 创建时间：2017年3月24日 下午4:55:39
	 * @param subjectNoList 科目编号
	 * @param custId 一户通编号
	 * @return
	 */
//	List<MrsAccountDto> findAccountDtoBySbjNo2AndCustId(@Param("subjectNoList")List<String> subjectNoList,@Param(value = "custId") String custId);
	/**
	 * 根据一级科目，科目类别，查询资金账号信息
	 * 
	 * @param subAcctType
	 * @param busiType
	 * @return
	 */
//	List<MrsAccountDto> findAccountDtoBySubAcctTypeAndBusiType(@Param(value = "subAcctType") String subAcctType,
//			@Param(value = "busiType") String busiType);
	/**
	 * 根据科目类别，查询资金账号信息
	 * 
	 * @param subAcctType
	 * @return
	 */
//	List<MrsAccountDto> findAccountDtoBySubAcctType(@Param(value = "subAcctType") String subAcctType);
	/**
	 * 客户姓名、证件类型、和证件号码和一户通状态为非“注销”状态关联查询
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByPerson3Element(@Param(value = "customerName") String customerName,
			@Param(value = "credentialsType") String credentialsType,
			@Param(value = "credentialsNumber") String credentialsNumber);
	/**
	 * 客户姓名、证件类型、和证件号码和一户通状态为非“注销”状态关联查询
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByOrganAsset3Element(@Param(value = "customerName") String customerName,
			@Param(value = "credentialsType") String credentialsType,
			@Param(value = "credentialsNumber") String credentialsNumber);
	/**
	 * 客户姓名、证件类型、和证件号码和一户通状态为非“注销”状态关联查询
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByProduct3Element(@Param(value = "customerName") String customerName,
			@Param(value = "credentialsType") String credentialsType,
			@Param(value = "credentialsNumber") String credentialsNumber);
	/**
	 * 	根据请求参数中社会统一信用代码或组织机构代码或税务登记号码或营业执照编码和
	 * 一户通状态为非“注销”状态关联查询“机构客户信息表”和“一户通账户表”
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByOrgan3Element(@Param("name")String name,
			@Param("socialCreditCode") String socialCreditCode,
			@Param("organizeCode")String organizeCode,
			@Param("revenueCode")String revenueCode,
			@Param("businessLicence") String businessLicence,
			@Param("organOtherCode")String organOtherCode);
	/**
	 * 根据个人的三要素获取所有账户数据-那么首先取认证状态为“认证成功”或“无需认证”记录，创建时间为最近的记录。
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByPerson3ElementLast(@Param(value = "customerName") String customerName,
			@Param(value = "credentialsType") String credentialsType,
			@Param(value = "credentialsNumber") String credentialsNumber);
	/**
	 * 	根据请求参数中一户通账号和一户通状态为非“注销”状态关联查询“个人客户信息表”和“一户通账户表”
	 * @param custId
	 * @return
	 */
	List<MrsAccountDto> findByUpdatePerson3Element(@Param(value = "custId") String custId);
	/**
	 * 	根据请求参数中一户通账号和一户通状态为非“注销”状态关联查询“机构客户信息表”和“一户通账户表”
	 * @param custId
	 * @return
	 */
	List<MrsAccountDto> findByUpdateOrgan3Element(@Param(value = "custId") String custId);
	/**
	 * 	根据请求参数中一户通账号和一户通状态为非“注销”状态关联查询“产品客户信息表”和“一户通账户表”
	 * @param custId
	 * @return
	 */
	List<MrsAccountDto> findByUpdateProduct3Element(@Param(value = "custId") String custId);
	/**
	 * 根据机构的三要素获取所有账户数据-那么首先取认证状态为“认证成功”或“无需认证”记录，创建时间为最近的记录。
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByOrganAsset3ElementLast(@Param(value = "customerName") String customerName,
			@Param(value = "credentialsType") String credentialsType,
			@Param(value = "credentialsNumber") String credentialsNumber);
	/**
	 * 根据机构的三要素获取所有账户数据-那么首先取认证状态为“认证成功”或“无需认证”记录，创建时间为最近的记录。
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByProduct3ElementLast(@Param(value = "customerName") String customerName,
			@Param(value = "credentialsType") String credentialsType,
			@Param(value = "credentialsNumber") String credentialsNumber);
	/**
	 * 	根据个人的三要素获取所有账户数据-那么首先取认证状态为“认证成功”或“无需认证”记录，创建时间为最近的记录。
	 * @param customerName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsAccountDto> findByOrgan3ElementLast(@Param("name")String name,
			@Param("socialCreditCode") String socialCreditCode,
			@Param("organizeCode")String organizeCode,
			@Param("revenueCode")String revenueCode,
			@Param("businessLicence") String businessLicence,
			@Param("organOtherCode")String organOtherCode);
	/**
	 * 
	 *方法描述：根据三要素一户通状态，查询个人一户通信息
	 * 创建人：ydx
	 * 创建时间：2017年3月9日 下午9:01:07
	 * @param customerName 名称
	 * @param type 证件类型
	 * @param number 证件号码
	 * @param accountStatus 一户通状态
	 * @return
	 */
	List<MrsAccountDto> findPersonBy3EleAndStatus(@Param(value = "name")String customerName, 
			@Param(value = "certType")String type,
			@Param(value = "certNo")String number,
			@Param(value = "accountStatus")String accountStatus);

	
	
	/**
	 * 根据认证状态获取一户通信息
	 * @param authStatus
	 * @return
	 *//*
	List<MrsAccountDto> findByAuthStatus(String authStatus);*/

	/**
	 * 获取一户机构通账号
	 * @param queryParam 
	 * @方法描述:  根据 一户通类型1 客户类型1 2 9 查询 
	 * @作者： yc
	 * @日期： 2017年4月19日-上午10:24:35
	 * @return 
	 * @返回类型： List<MrsAccountDto>
	 */
	List<MrsAccountDto> list(MrsAccountDto queryParam);
	/**
	 * 
	* @方法描述： 排除个人客户的一户通账户列表
	* @param searchDto
	* @return List<MrsAccountDto> 
	* @作者: SEN_SHAO
	* @创建时间:2017年4月17日 下午4:52:42
	 */
	List<MrsAccountDto> payMrsAccountListPage(MrsAccountDto searchDto);
	/**
	 * 根据认证状态获取一户通信息
	 * @param authStatus
	 * @return
	 *//*
	List<MrsAccountDto> findByAuthStatus(String authStatus);*/
	
	/**
	 * 根据微信号查询一户通信息
	 * @param weChatId
	 * @return
	 */
	List<MrsAccountDto> findByWeChatId(String weChatId);

	List<MrsAccountDto> findOrgListByName(@Param(value ="orgName")String orgName);
	
	/**
     * 根据登陆用户Id查询一互通信息
     * @param id
     * @return
     */
    List<MrsAccountDto> findByLoginUserId(String id);
}