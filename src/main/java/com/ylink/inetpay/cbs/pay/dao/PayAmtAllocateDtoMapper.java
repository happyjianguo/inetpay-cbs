package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.channel.dto.TbChlBank;
import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateDto;

@MybatisMapper("payAmtAllocateDtoMapper")
public interface PayAmtAllocateDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayAmtAllocateDto record);

    int insertSelective(PayAmtAllocateDto record);

    PayAmtAllocateDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayAmtAllocateDto record);

    int updateByPrimaryKey(PayAmtAllocateDto record);
    /**
     * 资金划拨列表
     * @param queryparam
     * @param payeeBankTypeList 
     * @param payerBankTypeLis 
     * @return
     */
	List<PayAmtAllocateDto> list( @Param("queryparam")PayAmtAllocateDto queryparam, @Param("payerBankTypeLis")List<TbChlBank> payerBankTypeLis,@Param("payeeBankTypeList") List<TbChlBank> payeeBankTypeList);
	
	List<PayAmtAllocateDto> listBatchNo(PayAmtAllocateDto queryParam);
	
	List<PayAmtAllocateDto> listForOut(PayAmtAllocateDto queryparam);
	
	List<PayAmtAllocateDto> queryAll(PayAmtAllocateDto payAmtAllocateDto);

	List<PayAmtAllocateDto> findPayAmtAllocateByBatchNo(PayAmtAllocateDto queryParam);
}