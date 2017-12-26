package com.ylink.inetpay.cbs.mrs.dao;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAssignShieNoDto;

@MybatisMapper("mrsAssignShieNoDtoMapper")
public interface MrsAssignShieNoDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(MrsAssignShieNoDto record);

	int insertSelective(MrsAssignShieNoDto record);

	MrsAssignShieNoDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(MrsAssignShieNoDto record);

	int updateByPrimaryKey(MrsAssignShieNoDto record);
	/**
	 * 根据客户名称、证件类别、证件号码查询系统是否存在历史配号数据
	 * @param custName客户名称
	 * @param certiType证件类别
	 * @param certiNum证件号码
	 * @return
	 */
	MrsAssignShieNoDto findAssignNoBy3Element(@Param("custName")String custName,
			@Param("certiType") String certiType,
			@Param("certiNum")String certiNum);

	/**
	 * 获取序列号
	 * 
	 * @return
	 */
	String getMrsAssignShieNoVal();
	
	/**
	 * 获取序列号
	 * 
	 * @return
	 */
	String getMrsAssignOrganVal();
	
}