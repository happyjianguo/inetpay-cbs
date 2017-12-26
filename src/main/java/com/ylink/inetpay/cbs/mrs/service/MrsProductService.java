package com.ylink.inetpay.cbs.mrs.service;



import java.util.List;

import org.ylinkpay.framework.core.model.PageData;

import com.ylink.inetpay.common.core.exception.CodeCheckedException;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsLoginUserDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPlatformDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsProductDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsActAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsProductVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.MrsRemoveAccountVo;
import com.ylink.inetpay.common.project.cbs.vo.mrs.SaveAduitProductResponseVo;
import com.ylink.inetpay.common.project.portal.vo.AccountMsgRespVo;
import com.ylink.inetpay.common.project.portal.vo.AccountProductUpdateRequestVO;
import com.ylink.inetpay.common.project.portal.vo.ProductRequestVO;

public interface MrsProductService {

	/**
	 * 根据三要素查询产品客户信息
	 * @param productName 产品名称
	 * @param credentialsType 证件类型
	 * @param credentialsNumber 证件号码
	 * @return
	 */
	public MrsProductDto findBy3Element(String productName, 
    		String credentialsType, 
    		String credentialsNumber);
	/**
	 * 更新产品信息
	 * @param requestVo
	 * @return
	 * @throws CodeCheckedException 
	 */
	public AccountMsgRespVo updateProductInfo(AccountProductUpdateRequestVO reqVo,
			MrsAccountDto account,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	/**
	 * 客户不存在-开产品账户(创建客户信息和账户信息以及登陆信息)
	 * @param requestVo
	 * @return
	 * @throws Exception 
	 */
	public MrsAccountDto initOpenAcnt(ProductRequestVO requestVo,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	
	/**
	 * 强制开户
	 * @param requestVo
	 * @param productDto
	 * @return
	 * @throws CodeCheckedException
	 */
	public MrsAccountDto forceOpenAcnt(ProductRequestVO requestVo, MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	
	/**
	 * 客户存在-开产品账户
	 * @param requestVo
	 * @return
	 * @throws CodeCheckedException 
	 */
	public MrsAccountDto updateOpenAcnt(ProductRequestVO requestVo, List<MrsAccountDto> productList
			,MrsPlatformDto mrsPlatformDto) throws CodeCheckedException;
	
	PageData<MrsProductDto> findListPage(PageData<MrsProductDto> pageData,MrsProductDto proDto);
	
	void updateByPrimaryKey(MrsProductDto record);
	
	public SaveAduitProductResponseVo saveAduitProductVo(MrsProductVo productVo);
    
	MrsProductDto selectByPrimaryKey(String id);
	
	/**
	 * 获取一户通信息
	 * @param custId
	 * @return
	 */
	MrsAccountDto findByCustId(String custId);
	
	/**
	 * 获取子账户信息
	 * @param custId
	 * @return
	 */
	PageData<MrsSubAccountDto> findSubAccountByCustId(PageData<MrsSubAccountDto> pageData,String custId);
	
	/**
	 * 获取登录用户信息
	 * @param custId
	 * @return
	 */
	PageData<MrsLoginUserDto> findLoginUserByCustId(PageData<MrsLoginUserDto> pageData,String custId);
    
	public SaveAduitProductResponseVo updateAduitProduct(MrsProductVo mrsProductVo);
	
	public SaveAduitProductResponseVo doUpdateAcctStatus(MrsActAccountVo mrsActAccountVo);
	/**
     * 根据一户通获取产品信息
     * @param custId
     * @return
     */
    public MrsProductDto findCustId(String custId);
    
    /**
     * 更新基本信息 并 同步 基础信息
     * @param dto
     */
    public void updateBaseAndSync(MrsProductDto dto);
    
    /**
     * 更新基本信息  并  同步 基础信息  附件
     * @param dto
     */
    public void updateBaseFileAndSync(MrsProductDto dto);
    /**
     * 
     *方法描述：根据三要素查询未注销的产品信息
     * 创建人：ydx
     * 创建时间：2017年3月9日 下午7:33:44
     * @param productName
     * @param credentialsType
     * @param credentialsNumber
     * @return
     */
	public List<MrsProductDto> findBy3ElementAndNoEff(String productName, String credentialsType,
			String credentialsNumber);

	public List<MrsProductDto> findBy3ElementAndStatus(String productName,String credentialsType,String credentialsNumber,String accountStatus);
    
	public void updateFileAndSync(MrsProductDto dto);
	/**
     * 用户注销送审
     * @param removeAccountVo
     * @return
     */
	public SaveAduitProductResponseVo removeSaveAduit(MrsRemoveAccountVo removeAccountVo);

}
