package com.ylink.inetpay.cbs.act.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.dto.ReporHeadDto;
import com.ylink.inetpay.common.project.account.dto.ActBookDto;

@MybatisMapper("actBookDtoMapper")
public interface ActBookDtoMapper {
	int deleteByPrimaryKey(String id);

	int insert(ActBookDto record);

	int insertSelective(ActBookDto record);

	ActBookDto selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(ActBookDto record);

	int updateByPrimaryKey(ActBookDto record);

	/**
	 * 根据参数查询所有记账交易流水数据
	 * 
	 * @param ActBookDto
	 * @return
	 */
	List<ActBookDto> queryAllData(ActBookDto actBookDto);

	/**
	 * 根据记账交易流水编号查询
	 * 
	 * @param billId
	 * @return
	 */
	ActBookDto selectByBookId(String bookId);
	/**
	 * @方法描述:  根据记payId查询
	 * @作者： 1603254
	 * @日期： 2016-8-18-上午9:28:03
	 * @param payId
	 * @return 
	 * @返回类型： ActBookDto
	*/
	ActBookDto selectByPayId(String payId);
	/**
     * 统计总笔数与总金额
     * @param checkStatus
     * @return
     */
    ReporHeadDto reportSumData(ActBookDto actBookDto);
}