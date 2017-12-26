package com.ylink.inetpay.cbs.mrs.service;

import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPersonDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsActAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsPersonVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsRemoveAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitPersonResponseVo;
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

/**
 * 个人开户Service接口
 * @author pst11
 *
 */
public interface MrsPersonService {

	/**
	 * 根据三要素查询个人客户信息
	 * @param customerName
	 * @param type
	 * @param number
	 * @return
	 */
	public List<MrsPersonDto> findBy3Element(String customerName, String type, String number);
	/** 
	* 根据三要素查询个人客户信息
	 * @param customerName
	 * @param type
	 * @param number
	 * @return
	 */
	public List<MrsPersonDto> findActPersonBy3Element(String customerName, String type, String number);

	/**
	 * 客户不存在-开个人账户(创建客户和账户以及登陆信息)
	 * @param requestVo
	 * @return
	 * @throws Exception 
	 */
	public MrsAccountDto initOpenAcnt(IndividualRequestVO requestVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	
	/**
	 * 客户存在-开个人账户
	 * @param requestVo
	 * @return
	 * @throws CodeCheckedException 
	 */
	public MrsAccountDto updateOpenAcnt(IndividualRequestVO requestVo, List<MrsAccountDto> actPersonList,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	/**
	 * 更新个人信息
	 * @param requestVo
	 * @return
	 * @throws CodeCheckedException 
	 */
	public AccountMsgRespVo updatePersonInfo(AccountIndividualUpdateReqVO reqVo,
			MrsAccountDto account,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;

	/**
	 * 查询客户的未启用账户的登陆信息
	 * @param reqVo
	 * @return
	 */
	public LoginMsgSearchResponseVO findLoginMsg(LoginMsgSearchRequestVO reqVo) throws CodeCheckedException;
	
	/**
	 * 分页查询个人信息
	 * @param pageData
	 * @param searchDto
	 * @return
	 */
	public PageData<MrsPersonDto> findPerson(PageData<MrsPersonDto> pageData, MrsPersonDto searchDto);
	/**
	 * 分页查询个人信息
	 * @param pageData
	 * @param searchDto
	 * @return
	 */
	public PageData<MrsPersonDto> findPersonByUpdateAudit(PageData<MrsPersonDto> pageData, MrsPersonDto searchDto);
	
	/**
	 * 根据ID查询个人客户信息
	 * @param id
	 * @return
	 */
	public MrsPersonDto findById(String id);
	

	/**
	 * 根据一户通账号查询个人客户信息
	 * @param id
	 * @return
	 */
	public MrsPersonDto findByCustId(String custId);

	public PersonVO findPersonVoByCustId(String custId);
	
	/**
	 * 更新个人客户信息
	 * @param dto
	 */
	public void update(MrsPersonDto dto);
	
	/**
	 * 只更新个人客户信息
	 * @param dto
	 */
	public void updateByPrimaryKey(MrsPersonDto dto);
	
	/**
	 * 更新个人客户信息并同步
	 * @param dto
	 */
	public void updateBaseAndSync(MrsPersonDto dto);
	
	/**
	 * 更新个人客户信息 附件 并 同步
	 * @param dto
	 */
	public void updateBaseFileAndSync(MrsPersonDto dto);
	
	/**
	 * 更新个人客户信息
	 * @param dto
	 */
	public void updatePerson(MrsPersonDto dto,String loginName);
	
	public MrsLoginUserDto restMobile(String custId, String mobile) ;

	public MrsLoginUserDto restEmail(String custId, String email) ;
	
	public MrsLoginUserDto restEmailById(String id, String email) ;

	public void authSuccess(String custId) throws PortalCheckedException;

	public void updateFileId(String custId, String fileId) throws PortalCheckedException;

	public void updateAndSync(MrsPersonDto dto);
	
	public void updatePicAndSync(MrsPersonDto dto);

	public UserCheckVO uploadPojoList(String custId, List<UploadPersonPojo> uploadPojoList);

	/**
	 * 存在客户信息  强制开户(个人)
	 * @param individualReqVo
	 * @param personDto
	 * @throws CodeCheckedException 
	 */
	public MrsAccountDto forceOpenAcnt(IndividualRequestVO individualReqVo,
			MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	
	
	public AccountSearchRespVO findAccountExist(AccountSearchReqVO reqVo) throws CodeCheckedException;
	
	public int insertSelective(MrsPersonDto dto);
	
	/**
	 * 
	 *方法描述：保存个人用户送审信息，转为JSON存储在审核信息里面
	 * 创建人：ydx
	 * 创建时间：2017年2月10日 上午10:56:29
	 * @param personVo
	 */
	public PersonVO saveAduitPersonByPortal(PersonVO personVo,MrsLoginUserDto loginUser);
	/**
	 * 
	 *方法描述：直接先开户，再送审
	 * 创建人：ydx
	 * 创建时间：2017年2月10日 上午10:56:29
	 * @param personVo
	 */
	public PersonVO openAndAduitPersonByPortal(PersonVO personVo,MrsLoginUserDto loginUser);
	/**
	 * 
	 *方法描述：保存个人用户送审信息，转为JSON存储在审核信息里面--用于被动开户
	 * 创建人：ydx
	 * 创建时间：2017年2月10日 上午10:56:29
	 * @param personVo
	 */
	public PersonVO saveAduitPersonByPortalRest(PersonVO personVo,MrsLoginUserDto loginUser);

	/**
	 * 
	 *方法描述：保存个人用户送审信息，转为JSON存储在审核信息里面
	 * 创建人：ydx
	 * 创建时间：2017年2月10日 上午10:56:29
	 * @param personVo
	 */
	public SaveAduitPersonResponseVo saveAduitPerson(MrsPersonVo personVo);
	/**
	 * 
	* @Title: saveAduitPersondobb
	* @Description: 用户信息修改后提交
	* @param @param personVo
	* @param @param id
	* @param @return    参数
	* @return SaveAduitPersonResponseVo    返回类型
	* @throws
	 */
	public SaveAduitPersonResponseVo saveAduitPersondobb(MrsPersonVo personVo,String id);

	/**
	 * 
	 *方法描述：开通一户通账户和资金账户
	 * 创建人：ydx
	 * 创建时间：2017年2月10日 上午10:56:29
	 * @param personVo
	 */
	
	public PersonVO openAccountByPortal(PersonVO personVo,BankBusiReqVO bankBusiReqVO);
	/**
	 * 
	 *方法描述：开通一户通账户和资金账户--直接调用
	 * 创建人：ydx
	 * 创建时间：2017年2月10日 上午10:56:29
	 * @param personVo
	 */
	
	public PersonVO directlyOpenAccount(PersonVO personVo);
	/**
	 * 
	 *方法描述：保存个人客户信息变更并送审
	 * 创建人：ydx
	 * 创建时间：2017年2月21日 下午2:44:46
	 * @param personVo
	 * @return
	 */
	public SaveAduitPersonResponseVo doUpdateSaveAduit(MrsPersonVo personVo);
	/**
	 * 
	 *方法描述：资金账户送审
	 * 创建人：ydx
	 * 创建时间：2017年2月28日 下午11:52:51
	 * @param actAccountVo
	 * @return
	 */
	public SaveAduitPersonResponseVo doUpdateActSaveAduit(MrsActAccountVo actAccountVo);
	/**
	 * 判断接入平台是否存在。根据请求参数中接入平台编号查询“接入平台表”是否存在记录，
	 * 如果不存在或存在记录的状态为非“正常”状态，则记录错误日志并返回处理结果“开通失败”，错误信息“开户渠道不存在”。
	 * @param platformCode
	 * @return
	 * @throws CodeCheckedException
	 */
	public MrsPlatformDto checkPlatform(String platformCode) throws CodeCheckedException ;
	/**
	 * 用户注销送审
	 * @param actAccountVo
	 * @return
	 */
	public SaveAduitPersonResponseVo removeSaveAduit(MrsRemoveAccountVo removeAccountVo);
}
