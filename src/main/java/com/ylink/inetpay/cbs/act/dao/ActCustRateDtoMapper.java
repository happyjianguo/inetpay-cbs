package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.account.dto.ActCustRateDto;

@MybatisMapper("actCustRateDtoMapper")
public interface ActCustRateDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ActCustRateDto record);

    int insertSelective(ActCustRateDto record);

    ActCustRateDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ActCustRateDto record);

    int updateByPrimaryKey(ActCustRateDto record);
    
    List<ActCustRateDto> queryByCustIds(@Param("custIds")List<String> custIds);
    
    List<ActCustRateDto> findBankRate();
    
    List<ActCustRateDto> list(ActCustRateDto queryParam);
    
    ActCustRateDto findByAccountId(String accountId);
    
    ActCustRateDto findByBankCardNo(String bankCardNo);
    
    List<ActCustRateDto> listCustRate(@Param("custRateDto")ActCustRateDto actCustRateDto,@Param("custId")String custId);
    /**
     * 根据银行卡号获取银行计息
     * @param bankAccNos
     * @return
     */
	List<ActCustRateDto> findByBankCardNos(@Param("bankAccNos")List<String> bankAccNos);
	/**
	 * 根据账户编号，获取计息用户或者资金冻结用户
	 * @param accountIds
	 * @param actCustRateDto
	 * @param custId
	 * @return
	 */
	List<ActCustRateDto> listCustRateByAccountIds(@Param("accountIds")List<String> accountIds, 
			@Param("custRateDto")ActCustRateDto actCustRateDto,@Param("custId")String custId);
}