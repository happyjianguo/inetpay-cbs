package com.ylink.inetpay.cbs.pay.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeDetailVo;
import com.ylink.inetpay.common.project.clear.dto.ClsTradeStationVo;
import com.ylink.inetpay.common.project.pay.dto.PayBookDto;

@MybatisMapper("payBookDtoMapper")
public interface PayBookDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(PayBookDto record);

	int insertSelective(PayBookDto record);

	PayBookDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(PayBookDto record);

	int updateByPrimaryKey(PayBookDto record);

	/**
	 * 根据参数查询所有订单数据
	 * 
	 * @param PayBookDto
	 * @return
	 */
	List<PayBookDto> queryAllData(PayBookDto payBookDto);

	/**
	 * 根据平台业务订单号查询
	 * 
	 * @param busiId
	 * @return
	 */
	PayBookDto selectByPayId(String busiId);
	 /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(PayBookDto payBookDto);
    
    
    public List<ClsTradeStationVo> queryActiveUserStation(ClsTradeStationVo vo);
    
    
	 
	public List<ClsTradeDetailVo> queryMerTradeDetail(ClsTradeDetailVo detail);
	
	//结算日期查询（无交易日期查询条件）
	public List<ClsTradeStationVo> queryMerTradeSummary2(ClsTradeStationVo station);
	//交易日期查询（无结算日期查询条件）
	public List<ClsTradeStationVo> queryMerTradeSummary1(ClsTradeStationVo station);
	
	public List<ClsTradeStationVo> queryStation(ClsTradeStationVo station);
	
	public List<ClsTradeStationVo>  queryActiveMer(ClsTradeStationVo vo);
    
	/**
	 * 一户通查询交易明细
	 * 充值、对外支付
	 * @param payBookDto
	 * @return
	 */
	List<PayBookDto> queryAllDataForAccount(PayBookDto payBookDto);
	/**
	 * 根据支付流水号获取平台订单号
	 * @param payIdSet
	 * @return
	 */
	List<PayBookDto> findBusIdByPayId(@Param("payIdSet")Set<String> payIdSet);
}