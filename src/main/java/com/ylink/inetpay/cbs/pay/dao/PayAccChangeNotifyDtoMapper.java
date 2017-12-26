package com.ylink.inetpay.cbs.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayAccChangeNotifyDto;
@MybatisMapper("payAccChangeNotifyDtoMapper")
public interface PayAccChangeNotifyDtoMapper {
    int insert(PayAccChangeNotifyDto record);

    int insertSelective(PayAccChangeNotifyDto record);
    
    List<PayAccChangeNotifyDto> queryList(PayAccChangeNotifyDto param);
    
    PayAccChangeNotifyDto selectByPrimaryKey(String id);
    /**
     * 根据订单号查询可以动账匹配的记录
     * @param accChangeBusiId
     * @return
     */
	long isMovingAccountMatching(String accChangeBusiId);

	PayAccChangeNotifyDto findPayAccChangeNotifyDtoById(@Param("id")String id);
}