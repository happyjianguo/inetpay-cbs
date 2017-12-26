package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.PayStatusEnum;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSetCashFundOperDto;
@MybatisMapper("bisSetCashFundOperDtoMapper")
public interface BisSetCashFundOperDtoMapper {
    int insert(BisSetCashFundOperDto record);

    int insertSelective(BisSetCashFundOperDto record);
    /**
     * 根据业务主键获取未知状态的操作记录
     * @param id
     * @param unknown
     * @return
     */
	long findByBusId(@Param("busId")String busId, @Param("unknown")PayStatusEnum unknown);
	/**
	 * 获取所有未知状态的操作记录
	 * @param unknown
	 * @return
	 */
	List<BisSetCashFundOperDto> findListUndown(PayStatusEnum unknown);
	/**
	 * 修改支付状态
	 * @param dto
	 * @return
	 */
	long updatePayStatus(BisSetCashFundOperDto dto);
	/**
	 * 根据业务主键获取所有操作记录
	 * @param id
	 * @return
	 */
	List<BisSetCashFundOperDto> getOperList(String busId);
	/**
	 * 根据业务主键查询操作记录数
	 * @param id
	 * @return
	 */
	long existOperNum(String busiId);
	
}