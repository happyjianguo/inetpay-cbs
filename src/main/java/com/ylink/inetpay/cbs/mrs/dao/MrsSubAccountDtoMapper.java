package com.ylink.inetpay.cbs.mrs.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsBankBusiDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsSubAccountDto;

@MybatisMapper("mrsSubAccountDtoMapper")
public interface MrsSubAccountDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsSubAccountDto record);

    int insertSelective(MrsSubAccountDto record);

    MrsSubAccountDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsSubAccountDto record);

    int updateByPrimaryKey(MrsSubAccountDto record);
    /**
     * 根据一户通更新子账户状态
     * @param custId
     * @param subAccountStatus
     * @param updateTime
     * @return
     */
    int updateByCustId(@Param("custId")String custId, @Param("subAccountStatus")String subAccountStatus, @Param("updateTime")Date updateTime);
    /**
     * 根据一户通更新子账户状态
     * @param custId
     * @param subAccountStatus
     * @param updateTime
     * @return
     */
    int updateByCustIdAndSubType(@Param("custId")String custId, 
    		@Param("subAccountType")String subAccountType,
    		@Param("subAccountStatus")String subAccountStatus, 
    		@Param("updateTime")Date updateTime);

	int insertList(@Param("subAccountList")List<MrsSubAccountDto> subAccountList);
    
	List<MrsSubAccountDto> findByCustId(String custId);
	/**
	 * 	根据请求参数中一户通账号和子账户类型和一户通状态为非“注销”状态关联查询“子账户表”和“一户通账户表”
	 * @param mrsSubAccountDto
	 * @return
	 */
	List<MrsSubAccountDto> findByCustIdAndSubAccountType(MrsSubAccountDto mrsSubAccountDto);
	
	/**
	 * 根据卡号、一户通号、绑卡类型查询
	 * @param accNo
	 * @param custId
	 * @param payType 枚举：BankPayType
	 * @return
	 */
	MrsBankBusiDto findByAccnoAndCustidAndPaytype(String accNo,String custId,String payType );
	
	/**
	 * 根据一户通账号和子账户名查询子账户信息
	 * @return
	 */
	MrsSubAccountDto findByAccountNameandCustId(@Param("accountName")String accountName,@Param("custId")String custId);
    
	/**
     * 根据一户通账号查询子账户状态
     * (投保人子账户)
     * @param custId
     * @return
     */
	MrsSubAccountDto findSubAccountStatusByCustIdAndType(String custId);
 
}