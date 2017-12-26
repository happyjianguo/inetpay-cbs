package com.ylink.inetpay.cbs.pay.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EPayBusiType;
import com.ylink.inetpay.common.project.pay.dto.PayFeeConfigDto;
@MybatisMapper("payFeeConfigDtoMapper")
public interface PayFeeConfigDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayFeeConfigDto record);

    int insertSelective(PayFeeConfigDto record);

    PayFeeConfigDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayFeeConfigDto record);

    int updateByPrimaryKey(PayFeeConfigDto record);

	PayFeeConfigDto findFeeConfig(@Param("merCode")String merCode, 
			@Param("busiType")EPayBusiType busiType,
			@Param("effectiveDate")String effectiveDate);

	List<PayFeeConfigDto> findByCondition(PayFeeConfigDto queryParam);

	List<PayFeeConfigDto> findByStatusPage(@Param("feeState")String feeState, @Param("effectiveDate")Date effectiveDate);

	List<PayFeeConfigDto> editAjaxcheck(@Param("merCode")String merCode, 
			@Param("busiType")EPayBusiType busiType,
			@Param("effectiveDate")String effectiveDate);

	PayFeeConfigDto queryByMerCodeBusiTypeEffectiveDate(@Param("merCode")String merCode, @Param("busiType")String busiType, @Param("effectiveDate")String effectiveDate,
			@Param("feeState")String feeState);
	
	/**
	 * 获取序列
	 * @return
	 */
	long getPlatTradeNoSeqNo(@Param(value="seqName") String seqName);
	
}