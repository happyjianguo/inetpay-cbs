package com.ylink.inetpay.cbs.pay.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EPayLimitBusinessType;
import com.ylink.inetpay.common.project.pay.dto.PayLimitDto;
@MybatisMapper("payLimitDtoMapper")
public interface PayLimitDtoMapper {
   // int deleteByPrimaryKey(String id);

   // int insert(PayLimitDto record);

  //  int insertSelective(PayLimitDto record);

   // PayLimitDto selectByPrimaryKey(String id);

   // int updateByPrimaryKeySelective(PayLimitDto record);

  //  int updateByPrimaryKey(PayLimitDto record);

	List<PayLimitDto> getPayLimits(String custId);
	PayLimitDto findByCustIdAndBusiType(@Param("custId")String custId,
    		@Param("busiType")EPayLimitBusinessType busiType);
}