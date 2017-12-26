package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EPayOverrBusinessType;
import com.ylink.inetpay.common.project.pay.dto.PayOverlimitDto;
@MybatisMapper("payOverlimitDtoMapper")
public interface PayOverlimitDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayOverlimitDto record);

    int insertSelective(PayOverlimitDto record);

    PayOverlimitDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayOverlimitDto record);

    int updateByPrimaryKey(PayOverlimitDto record);
    /***
     * 分页查询指定类型的支付限额记录
     * @param payOverlimitDto
     * @return
     */
	List<PayOverlimitDto> list(PayOverlimitDto payOverlimitDto);
	/**
	 * 查询单笔支付限额记录
	 * @param payOverlimitDto
	 * @param singleLimit
	 * @param singleWarning
	 * @return
	 */
	List<PayOverlimitDto> limitList(@Param("dto")PayOverlimitDto payOverlimitDto,
			@Param("limit")EPayOverrBusinessType singleLimit,
			@Param("warning")EPayOverrBusinessType singleWarning);
}