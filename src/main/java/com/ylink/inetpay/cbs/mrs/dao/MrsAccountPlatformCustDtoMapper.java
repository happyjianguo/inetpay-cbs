package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountPlatformCustDto;
@MybatisMapper("mrsAccountPlatformCustDtoMapper")
public interface MrsAccountPlatformCustDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsAccountPlatformCustDto record);

    int insertSelective(MrsAccountPlatformCustDto record);

    MrsAccountPlatformCustDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsAccountPlatformCustDto record);

    int updateByPrimaryKey(MrsAccountPlatformCustDto record);
    /**
	 * 
	 *	根据该记录中一户通编号、请求参数中接入平台编号和平台客户编号查询“一户通客户关系表”是否存在记录
	 * @return
	 */
	List<MrsAccountPlatformCustDto> findActPlatformCust(@Param(value = "custId")String custId, 
			@Param(value = "platformCustCode")String platformCustCode,
			@Param(value = "source")String source);
}