package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayExternalHandleDto;
/**
 * 对外支付经办
 * @author pst10
 *
 */
@MybatisMapper("payExternalDtoMapper")
public interface PayExternalHandleDtoMapper {
	
	/**
	 * 分页查询
	 * @param payExternalDto
	 * @return
	 */
	public List<PayExternalHandleDto> getByCond(PayExternalHandleDto payExternalDto);
	
	/**
	 * 新增
	 * @param payExternalDto
	 */
	public void insert(PayExternalHandleDto payExternalDto);
	
	
	
	/**
	 * 修改
	 * @param payExternalDto
	 */
	public void update(PayExternalHandleDto payExternalDto);
	
	
	
	/**
	 * 查询单条
	 * @param id
	 * @return
	 */
	public PayExternalHandleDto details(String id);
}