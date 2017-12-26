package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EAuditResult;
import com.ylink.inetpay.common.core.dto.ExportDto;
import com.ylink.inetpay.common.project.pay.dto.PayAccountAdjustDto;
@MybatisMapper("payAccountAdjustDtoMapper")
public interface PayAccountAdjustDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayAccountAdjustDto record);

    int insertSelective(PayAccountAdjustDto record);

    PayAccountAdjustDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayAccountAdjustDto record);

    int updateByPrimaryKey(PayAccountAdjustDto record);
    /**
     * 审核列表）
     * @param payAccountAdjustDto
     * @return
     */
	List<PayAccountAdjustDto> pageList(PayAccountAdjustDto payAccountAdjustDto);
	/**
	 * 获取调账审核历史
	 * @param payAccountAdjustDto
	 * @param auditPass
	 * @param auditReject
	 * @return
	 */
	List<PayAccountAdjustDto> pageResultList(
			@Param("aDto")PayAccountAdjustDto payAccountAdjustDto, @Param("auditPass")EAuditResult auditPass,
			@Param("auditReject")EAuditResult auditReject);
	
	/**
	 * @方法描述: 查询调账的记录条数(调账审核)
	 * @作者： 1603254
	 * @日期： 2016-6-1-上午11:43:56
	 * @return 
	 * @返回类型： Integer
	*/
	public Integer queryAdjustCountByAdjust();
	
	/**
	 * @方法描述: 查询调账的记录条数(补录审核)
	 * @作者： 1603254
	 * @日期： 2016-6-1-上午11:43:56
	 * @return 
	 * @返回类型： Integer
	 */
	public Integer queryAdjustCountByRecover();
	
	/**
	 * 根据查询条件获取导出统计数据
	 * @param queryParam
	 * @param auditPass 
	 * @return
	 */
	ExportDto export(@Param("aDto")PayAccountAdjustDto queryParam, @Param("auditPass")EAuditResult auditPass);

	/***
	 * 查询单条数据详情
	 * @param id
	 * @return
	 */
	public PayAccountAdjustDto getById(String id);
}