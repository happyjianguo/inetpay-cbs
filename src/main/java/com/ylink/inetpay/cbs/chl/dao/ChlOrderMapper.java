package com.ylink.inetpay.cbs.chl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.channel.dto.TbChlPayOrderDto;
@MybatisMapper("chlOrderMapper")
public interface ChlOrderMapper {
    int deleteByPrimaryKey(String id);

    int insert(TbChlPayOrderDto record);

    int insertSelective(TbChlPayOrderDto record);

    TbChlPayOrderDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TbChlPayOrderDto record);

    int updateByPrimaryKey(TbChlPayOrderDto record);
    
    /**
     * 分页查询
     * @param record
     * @return
     */
    List<TbChlPayOrderDto> findListPage(TbChlPayOrderDto record);
    
    /**
     * 根据支付流水查询订单
     * @return
     */
    TbChlPayOrderDto selectByPayId(@Param("payId")String payId);
    /**
     * 根据发往银行流水查询
     * @param platTradeNo
     * @param channelCode
     * @return
     */
    TbChlPayOrderDto selectByPlatTradeNo(@Param("platTradeNo")String platTradeNo);
}