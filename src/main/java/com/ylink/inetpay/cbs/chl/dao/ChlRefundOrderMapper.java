package com.ylink.inetpay.cbs.chl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.channel.dto.TbChlRefundOrderDto;
@MybatisMapper("chlRefundOrderMapper")
public interface ChlRefundOrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(TbChlRefundOrderDto record);

    int insertSelective(TbChlRefundOrderDto record);

    TbChlRefundOrderDto selectByPrimaryKey(String id);
    

    /**
     * 分页查询
     * @param record
     * @return
     */
    List<TbChlRefundOrderDto> findListPage(TbChlRefundOrderDto record);
    
    /**
     * 根据支付流水查找退款信息
     * @param payId
     * @return
     */
    TbChlRefundOrderDto selectByPayId(@Param("payId")String payId);
    /**
     * 根据发往银行流水查询退款订单
     * @param platNo
     * @return
     */
    TbChlRefundOrderDto selectByPlatNo(@Param("platNo")String platNo);

    int updateByPrimaryKeySelective(TbChlRefundOrderDto record);

    int updateByPrimaryKey(TbChlRefundOrderDto record);
}