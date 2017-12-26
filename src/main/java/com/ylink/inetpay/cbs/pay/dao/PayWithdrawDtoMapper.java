package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;
import com.ylink.inetpay.common.project.pay.dto.PayWithdrawDto;
@MybatisMapper("payWithdrawDtoMapper")
public interface PayWithdrawDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayWithdrawDto record);

    int insertSelective(PayWithdrawDto record);

    PayWithdrawDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayWithdrawDto record);

    int updateByPrimaryKey(PayWithdrawDto record);
    /**
     * 根据参数查询所有提现订单数据
     * @param PayWithdrawDto
     * @return
     */
    List<PayWithdrawDto> queryAllData(PayWithdrawDto payWithdrawDto);
    /**
     * 根据平台业务订单号查询
     * @param busiId
     * @return
     */
    PayWithdrawDto selectByBusiId(String busiId);
    /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayWithdrawDto payWithdrawDto);
    
    
    public List<ClsTradeStationVo>  queryWithdrawStation(ClsTradeStationVo vo);
}