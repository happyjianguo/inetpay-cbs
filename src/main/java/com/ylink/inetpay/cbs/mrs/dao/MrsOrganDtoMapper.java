package com.ylink.inetpay.cbs.mrs.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOrganDto;
import com.ylink.inetpay.common.project.portal.vo.customer.OrganVO;

@MybatisMapper("mrsOrganDtoMapper")
public interface MrsOrganDtoMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(MrsOrganDto record);

    int insertSelective(MrsOrganDto record);

    MrsOrganDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsOrganDto record);

    int updateByPrimaryKey(MrsOrganDto record);
    
    /**
     * 根据一户通账号查询机构客户信息
     * @param custId
     * @return
     */
    MrsOrganDto findByCustId(@Param("custId")String custId);
    
    OrganVO findOrganVoByCustId(@Param("custId")String custId);
    
    public List<MrsOrganDto> findBy3Element(@Param("name")String name, @Param("socialCreditCode")String socialCreditCode, 
    		@Param("organizeCode")String organizeCode, @Param("revenueCode")String revenueCode, @Param("businessLicence")String businessLicence);
    
    /**
     * 根据条件查询出所有的机构信息
     * @param searchDto
     * @return
     */
	List<MrsOrganDto> findOrganByParams(@Param("customerType")String customerType, @Param("accountStatus")String accountStatus);
	

    /**
     * 根据查询条件查询机构客户信息
     * @param searchDto
     * @return
     */
    List<MrsOrganDto> list(MrsOrganDto searchDto);
    /**
     * 根据查询条件查询机构客户信息
     * @param searchDto
     * @return
     */
    List<MrsOrganDto> updateAuditList(MrsOrganDto searchDto);
    MrsOrganDto findByExOrgNo(@Param("exOrgNo")String exOrgNo);

	int updateFileId(@Param("custId")String custId, @Param("businessLicenceFileId")String businessLicenceFileId, 
			@Param("organizeCodeFileId")String organizeCodeFileId, @Param("revenueCodeFileId")String revenueCodeFileId,
			@Param("socialcreditCodeFileId")String socialcreditCodeFileId, @Param("otherFileId")String otherFileId,
			@Param("updateTime")Date updateTime );
	
	int updateCustomerCodeByCustId(@Param("custId")String custId, @Param("customerCode")String customerCode, @Param("updateTime")Date updateTime);
	 /**
     * 
     *方法描述：根据三要素一户通状态查询机构信息
     * 创建人：ydx
     * 创建时间：2017年2月25日 下午4:43:43
     * @param customerName
     * @param socialCreditCode
     * @param organizeCode
     * @param revenueCode
     * @param businessLicence
     * @param organOtherCode
     * @param accountStatus
     * @return
     */
	List<MrsOrganDto> findBy3ElementAndAcountStatus(@Param("name")String name,@Param("socialCreditCode") String socialCreditCode,
			@Param("organizeCode")String organizeCode,@Param("revenueCode")String revenueCode,@Param("businessLicence") String businessLicence, 
		@Param("organOtherCode")String organOtherCode,@Param("accountStatus") String accountStatus);
	/**
	 * 
	 *方法描述：根据机构名称，多要素查询机构信息
	 * 创建人：ydx
	 * 创建时间：2017年3月9日 下午8:14:58
	 * @param customerName 机构名称
	 * @param socialCreditCode
	 * @param organizeCode
	 * @param revenueCode
	 * @param businessLicence
	 * @param organOtherCode
	 * @return
	 */
	List<MrsOrganDto> findBy3ElementNoEff(@Param("name")String name,@Param("socialCreditCode") String socialCreditCode,
			@Param("organizeCode")String organizeCode,@Param("revenueCode")String revenueCode,@Param("businessLicence") String businessLicence, 
		@Param("organOtherCode")String organOtherCode);
	/**
	 * 
	 *方法描述：根据机构要素，查询不为注销状态，且不等于传入一户通号码的机构信息
	 * 创建人：ydx
	 * 创建时间：2017年3月21日 下午4:30:02
	 * @param name
	 * @param socialCreditCode
	 * @param organizeCode
	 * @param revenueCode
	 * @param businessLicence
	 * @param organOtherCode
	 * @param custId
	 * @return
	 */
	List<MrsOrganDto> findBy3ElementNoCustIdEff(@Param("name")String name,@Param("socialCreditCode") String socialCreditCode,
			@Param("organizeCode")String organizeCode,@Param("revenueCode")String revenueCode,@Param("businessLicence") String businessLicence, 
		@Param("organOtherCode")String organOtherCode,@Param("custId")String custId);
}