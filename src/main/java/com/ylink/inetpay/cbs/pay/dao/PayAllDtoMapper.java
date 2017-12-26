package com.ylink.inetpay.cbs.pay.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayAllDto;
/**
 * 业务订单视图
 * @author huangckl@qq.com
 *
 * 2016年5月25日
 */
@MybatisMapper("payAllDtoMapper")
public interface PayAllDtoMapper {



	PayAllDto selectByBusiId(String id);

	/**
	 * 根据参数查询所有订单数据
	 * 
	 * @param PayPaymentDto
	 * @return
	 */
	List<PayAllDto> queryAllData(PayAllDto payAllDto);

	
}