package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitAttachmentDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOrganDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserAccountDto;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsActAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsOrganVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsRemoveAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitProductResponseVo;
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

public interface MrsOrganService {

	/**
	 * 根据三要素查询机构客户信息
	 * @param customerName
	 * @param type
	 * @param number
	 * @return
	 */
	public List<MrsOrganDto> findBy3Element(String customerName, String socialCreditCode, 
			String organizeCode, String revenueCode, String businessLicence);
	/**
	 * 	根据请求参数中社会统一信用代码或组织机构代码或税务登记号码或营业执照编码和一户通状态为非“注销”状态
	 * 关联查询“机构客户信息表”和“一户通账户表”（
	 * @param name
	 * @param socialCreditCode
	 * @param organizeCode
	 * @param revenueCode
	 * @param businessLicence
	 * @param organOtherCode
	 * @return
	 */
	List<MrsOrganDto> findBy3ElementNoEff(String name, String socialCreditCode,
			String organizeCode,String revenueCode,String businessLicence, 
		String organOtherCode);
	/**
	 * 根据条件查询机构客户信息
	 * @param searchDto
	 * @return
	 */
	public List<MrsOrganDto> findOrganByParams(String customerType, String accountStatus);
	
	/**
	 * 客户不存在-开机构账户(创建客户信息和账户信息以及登陆信息)
	 * @param requestVo
	 * @return
	 * @throws Exception 
	 */
	public MrsAccountDto initOpenAcnt(UnitRequestVO requestVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	/**
	 * 更新机构信息
	 * @param requestVo
	 * @return
	 * @throws CodeCheckedException 
	 */
	public AccountMsgRespVo updateOrganInfo(AccountUnitUpdateReqVO reqVo,
			MrsAccountDto account,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	/**
	 * 客户不存在-开机构账户(创建客户信息和账户信息以及登陆信息)资管
	 * @param requestVo
	 * @return
	 * @throws Exception 
	 */
	public MrsAccountDto initOpenAssetAcnt(UnitAssetRequestVo requestVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	/**
	 * 存在客户信息  强制开户(机构)
	 * @param unitAssetRequestVo
	 * @param mrsPlatformDto
	 * @throws CodeCheckedException 
	 */
	public MrsAccountDto forceOpenAcnt(UnitAssetRequestVo unitAssetRequestVo,
			MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	/**
	 * 存在客户信息  强制开户(机构)
	 * @param individualReqVo
	 * @param personDto
	 * @throws CodeCheckedException 
	 */
	public MrsAccountDto forceOpenOrganAcnt(UnitAssetRequestVo unitReqVo, List<MrsOrganDto> organList) 
			throws CodeCheckedException;
	/**
	 * 客户存在-开机构账户（巨灾）
	 * @param requestVo
	 * @return
	 * @throws CodeCheckedException 
	 */
	public MrsAccountDto updateOpenAcnt(UnitRequestVO requestVo, List<MrsAccountDto> organList
			,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	/**
	 * 客户存在-开机构账户（资管）
	 * @param requestVo
	 * @return
	 * @throws CodeCheckedException 
	 */
	public MrsAccountDto updateOpenAcntAsset(UnitAssetRequestVo requestVo, List<MrsAccountDto> organList
			,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	/**
	 * 客户存在-开机构账户（资管）
	 * @param requestVo
	 * @return
	 * @throws CodeCheckedException 
	 */
	public MrsAccountDto initOpenAcnt(UnitAssetRequestVo requestVo, List<MrsOrganDto> organList) throws CodeCheckedException;
	/*
	 * 新增子账户
	 */
	public void addAccount(UnitAssetRequestVo unitReqVo,String custId);
	/**
	 * 查询客户的未启用账户的登陆信息
	 * @param reqVo
	 * @return
	 * @throws CodeCheckedException 
	 */
	public LoginMsgSearchResponseVO findLoginMsg(LoginMsgSearchRequestVO reqVo) throws CodeCheckedException;
	
	/**
	 * 分页查询机构信息
	 * @param pageData
	 * @param searchDto
	 * @return
	 */
	public PageData<MrsOrganDto> findOrgan(PageData<MrsOrganDto> pageData, MrsOrganDto searchDto);
	/**
	 * 分页查询机构信息
	 * @param pageData
	 * @param searchDto
	 * @return
	 */
	public PageData<MrsOrganDto> findOrganByUpdateAudit(PageData<MrsOrganDto> pageData, MrsOrganDto searchDto);

	/**
	 * 根据ID查询机构信息
	 * @param id
	 * @return
	 */
	public MrsOrganDto findById(String id);

	/**
	 * 根据一户通账号查询机构信息
	 * @param id
	 * @return
	 */
	public MrsOrganDto findByCustId(String custId);

    OrganVO findOrganVoByCustId(String custId);
	/**
	 * 更新机构信息
	 * @param dto
	 */
	public void update(MrsOrganDto dto);
	/**
	 * 只更新机构信息
	 * @param dto
	 */
	public void updateByPrimaryKey(MrsOrganDto dto);
	/**
	 * 更新机构信息并同步  
	 * for 接口（变更、可信接口开户）
	 * @param dto
	 */
	public void updateBaseAndSync(MrsOrganDto dto);
	
	/**
	 * 更新机构信息  附件  并 同步
	 * for 主动开户、被动开户认证后  客户信息审核通过生效后  
	 * @param dto
	 */
	public void updateBaseFileAndSync(MrsOrganDto dto);
	
	/**
	/**
	 * 更新机构信息
	 * @param dto
	 */
	public void updateOrgan(MrsOrganDto dto,String loginName);
	
	public MrsOrganDto findByExtOrgId(String extOrgId);

	/**
	 * 
	 * @param custId
	 * @param businessLicenceFileId
	 * @param organizeCodeFileId
	 * @param revenueCodeFileId
	 * @param socialcreditCodeFileId
	 * @param otherFileId
	 * @throws PortalCheckedException 
	 */
	public void updateFileId(String custId, String businessLicenceFileId, String organizeCodeFileId,
			String revenueCodeFileId, String socialcreditCodeFileId, String otherFileId) throws PortalCheckedException;

	public UserCheckVO updateFileId(String custId, List<UploadOrganPojo> uploadPojoList);
	
	public void updateAndSync(MrsOrganDto dto);

	public void updatePicAndSync(MrsOrganDto dto);

	public AccountSearchRespVO findAccountExist(AccountSearchReqVO reqVo) throws CodeCheckedException;
	
	public int saveMrsUserAccountDto(MrsUserAccountDto mrsUserAccountDto);
	/**
	 * 保存机构送审信息
	 *方法描述：
	 * 创建人：ydx
	 * 创建时间：2017年2月24日 下午5:34:01
	 * @param mrsOrganVo
	 * @return
	 */
	public SaveAduitPersonResponseVo saveAduitOrgan(MrsOrganVo mrsOrganVo);
	/**
	 * 
	 *方法描述：根据三要素账户状态查询机构信息
	 * 创建人：ydx
	 * 创建时间：2017年2月25日 下午4:40:39
	 * @param customerName
	 * @param socialCreditCode
	 * @param organizeCode
	 * @param revenueCode
	 * @param businessLicence
	 * @param organOtherCode
	 * @param accountStatus
	 * @return
	 */
	public List<MrsOrganDto> findBy3ElementAndAcountStatus(String customerName, String socialCreditCode, 
			String organizeCode, String revenueCode, String businessLicence,String organOtherCode,String accountStatus);
	/**
	 * 
	 *方法描述：保存机构信息变更送审
	 * 创建人：ydx
	 * 创建时间：2017年2月27日 下午9:45:58
	 * @param vo
	 * @return
	 */
	public SaveAduitPersonResponseVo doUpdateSaveAduit(MrsOrganVo vo);
	
	public SaveAduitProductResponseVo doUpdateAccountStatus(MrsActAccountVo mrsActAccountVo);
	/**
	 * 保存被动开户数据
	 * @param custId
	 * @param loginUserId
	 * @param uploadPojoList
	 * @return
	 */
	public UserCheckVO saveAduitOrganByPortalRest(String custId,String loginUserId,  List<MrsAduitAttachmentDto> mrsAduitAttachmentDtoList) ;
	/**
	  * 
	  *方法描述：根据机构要素查询机构要素强制开户是否达到上限
	  * 创建人：ydx
	  * 创建时间：2017年3月9日 下午4:17:54
	  * @param customerName
	  * @param socialCreditCode
	  * @param organizeCode
	  * @param revenueCode
	  * @param businessLicence
	  * @param organOtherCode
	  * @return
	  */
	public boolean checkOrgan3ElmentIsMax(String customerName, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence, String organOtherCode);
	/**
	 * 
	 *方法描述：根据机构要素一户通号码校验机构一户通是否达到最大开户数量
	 * 创建人：ydx
	 * 创建时间：2017年3月21日 下午4:20:36
	 * @param customerName 机构名称
	 * @param socialCreditCode 社会统一信用代码
	 * @param organizeCode 组织机构号
	 * @param revenueCode 税务登记证代码
	 * @param businessLicence 营业执照号
	 * @param organOtherCode 其他证件号码
	 * @param custId 一户通号码
	 * @return
	 */
	public boolean checkOrgan3ElmentUpdateIsMax(String customerName, String socialCreditCode, String organizeCode,
			String revenueCode, String businessLicence, String organOtherCode,String custId);
	/**
	 * 用户注销送审
	 * @param removeAccountVo
	 * @return
	 */
	public SaveAduitProductResponseVo removeSaveAduit(MrsRemoveAccountVo removeAccountVo);
}
