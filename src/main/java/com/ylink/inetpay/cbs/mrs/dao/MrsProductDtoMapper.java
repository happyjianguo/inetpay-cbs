package com.ylink.inetpay.cbs.mrs.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsProductDto;

@MybatisMapper("mrsProductDtoMapper")
public interface MrsProductDtoMapper {
	int deleteByPrimaryKey(String id);

	void insert(MrsProductDto record);

	int insertSelective(MrsProductDto record);

	MrsProductDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(MrsProductDto record);

	void updateByPrimaryKey(MrsProductDto record);

	/**
	 * 根据三要素查询产品客户信息
	 * 
	 * @param productName
	 *            产品名称
	 * @param credentialsType
	 *            证件类型
	 * @param credentialsNumber
	 *            证件号码
	 * @return
	 */
	public List<MrsProductDto> findBy3Element(@Param("productName") String productName,
			@Param("credentialsType") String credentialsType, @Param("credentialsNumber") String credentialsNumber);
	/**
	 * 更新客户编码
	 * @param custId
	 * @param customerCode
	 * @param updateTime
	 * @return
	 */
	
	int updateCustomerCodeByCustId(@Param("custId") String custId, @Param("customerCode") String customerCode,
			@Param("updateTime") Date updateTime);
	
	List<MrsProductDto> findListPage(MrsProductDto proDto);
	
	MrsProductDto findCustId(String custId);
	/**
	 * 
	 *方法描述：根据三要素查询未生效的产品信息
	 * 创建人：ydx
	 * 创建时间：2017年3月9日 下午7:36:00
	 * @param productName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsProductDto> findBy3ElementAndNoEff(@Param("productName") String productName,
			@Param("credentialsType") String credentialsType, @Param("credentialsNumber") String credentialsNumber);
	
	/**
	 * 根据三要素和账户状态查询信息
	 * @param productName
	 * @param credentialsType
	 * @param credentialsNumber
	 * @return
	 */
	List<MrsProductDto> findBy3ElementAndStatus(@Param("productName") String productName,
			@Param("credentialsType") String credentialsType, @Param("credentialsNumber") String credentialsNumber,@Param("accountStatus")String accountStatus);
}