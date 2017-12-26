package com.ylink.inetpay.cbs.pay.dao;


import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.pay.dto.PayCheckFileDto;
@MybatisMapper("payCheckFileDtoMapper")
public interface PayCheckFileDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayCheckFileDto record);

    int insertSelective(PayCheckFileDto record);

    PayCheckFileDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayCheckFileDto record);

    int updateByPrimaryKey(PayCheckFileDto record);
    
    /**
     * @方法描述: 根据账务日期查询对账监控record
     * @作者： 1603254
     * @日期： 2016-5-27-上午10:56:50
     * @param dto
     * @return 
     * @返回类型： List<PayCheckFileDto>
    */
    List<PayCheckFileDto> findCheckFile(PayCheckFileDto dto);
    
    
    /**
	 * @方法描述: 更新状态为未处理
	 * @作者： 1603254
	 * @日期： 2016-5-27-下午4:48:55
	 * @param id 
	 * @返回类型： void
	*/
    void updateDealStatusToUnProcess(String id);
}