package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;
import com.ylink.inetpay.common.project.pay.dto.PayTransferDto;
@MybatisMapper("payTransferDtoMapper")
public interface PayTransferDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayTransferDto record);

    int insertSelective(PayTransferDto record);

    PayTransferDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayTransferDto record);

    int updateByPrimaryKey(PayTransferDto record);
    /**
     * 根据参数查询所有转账订单数据
     * @param PayTransferDto
     * @return
     */
    List<PayTransferDto> queryAllData(PayTransferDto payTransferDto);
    /**
     * 根据平台业务订单号查询
     * @param busiId
     * @return
     */
    PayTransferDto selectByBusiId(String busiId);
    /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayTransferDto payTransferDto);
    
    
    public List<ClsTradeStationVo> queryTransferStation(ClsTradeStationVo vo);
    
}