package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActAccountDto;
import com.ylink.inetpay.common.project.account.dto.ActBillDto;

@MybatisMapper("actBillDtoMapper")
public interface ActBillDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(ActBillDto record);

	int insertSelective(ActBillDto record);

	ActBillDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(ActBillDto record);

	int updateByPrimaryKey(ActBillDto record);
	
	/**
     * 根据参数查询所有记账分录数据
     * @param ActBillDto
     * @return
     */
    List<ActBillDto> queryAllData(ActBillDto actBillDto);
    /**
     * 根据参数查询所有记账分录数据
     * @param ActBillDto
     * @return
     */
    List<ActBillDto> queryAllDataByPortal(@Param(value = "actBillDto") ActBillDto actBillDto,
    		@Param(value = "subjectNoList")List<String> subjectNoList);
    /**
     * 根据记账分录编号查询
     * @param billId
     * @return
     */
    ActBillDto selectByBillId(String billId);
    /**
     * 统计总笔数与总金额
     * @param orderStatus
     * @return
     */
    ReporHeadDto reportSumData(ActBillDto actBillDto);
    /**
     * 渠道账户
     * @param queryParam
     * @param actAccountDtoList 
     * @return
     */
	List<ActBillDto> findAll(@Param("queryParam")ActBillDto queryParam,@Param("actAccountDtoList") List<ActAccountDto> actAccountDtoList);
}