package com.ylink.inetpay.cbs.pay.dao;

import com.ylink.inetpay.common.project.pay.dto.PayAmtAllocateBatchDto;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

@MybatisMapper("payAmtAllocateBatchDtoMapper")
public interface PayAmtAllocateBatchDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayAmtAllocateBatchDto record);

    int insertSelective(PayAmtAllocateBatchDto record);

    PayAmtAllocateBatchDto selectByPrimaryKey(String id);
    
    List<PayAmtAllocateBatchDto> listAll(PayAmtAllocateBatchDto record);

    int updateByPrimaryKeySelective(PayAmtAllocateBatchDto record);

    int updateByPrimaryKey(PayAmtAllocateBatchDto record);

    /**
     * 根据批次号和接入放代码查询出批次信息
     * @param batchNo
     * @param accessCode
     * @return
     */
    PayAmtAllocateBatchDto findByBatchNoAndAccessCode(@Param("batchNo") String batchNo, @Param("accessCode") String accessCode);


    /**
     * 累加该批次成功的数量
     * @param batchNo
     * @param amount
     * @param num
     */
    void addSuccessNumAndAmount(@Param("batchNo") String batchNo,@Param("amount")  Long amount, @Param("num") Long num);

    /**
     * 累加该批次的数据
     * @param batchNo
     * @param amount
     * @param num
     */
    void addFailNumAndAmount(@Param("batchNo") String batchNo,@Param("amount")  Long amount, @Param("num") Long num);


    /**
     * 累加失败的数据，减去成功的数据
     *
     * @param batchNo
     * @param amount
     * @param num
     */
    void addFailBySuccess(@Param("batchNo") String batchNo,@Param("amount")  Long amount, @Param("num") Long num);

    
}