package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisBillHandleDto;

/**
 * 手工记账经办
 * @author pst10
 *
 */
@MybatisMapper("bisBillHandleDtoMapper")
public interface BisBillHandleDtoMapper {
	/**
	 * 查询所有分页
	 * @param params
	 * @return
	 */
	public List<BisBillHandleDto> getByCond(BisBillHandleDto actBillHandleDto);
	
	/**
	 * 新增
	 * @param actBillHandleDto
	 */
	public void insert(BisBillHandleDto actBillHandleDto);
	
	/**
	 * 修改
	 * @param actBillHandleDto
	 */
	public void update(BisBillHandleDto actBillHandleDto);
	
	/**
	 * 单条详情
	 * @param id
	 * @return
	 */
	public BisBillHandleDto details(String id);
	
	/***
	 * 修改支付状态
	 * @param actBillHandleDto
	 */
	public void updatePayStatus(BisBillHandleDto actBillHandleDto);
}