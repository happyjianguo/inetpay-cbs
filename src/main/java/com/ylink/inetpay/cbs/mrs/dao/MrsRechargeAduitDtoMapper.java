package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsRechargeAduitDto;
@MybatisMapper("mrsRechargeAduitDtoMapper")
public interface MrsRechargeAduitDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsRechargeAduitDto record);

    int insertSelective(MrsRechargeAduitDto record);

    MrsRechargeAduitDto selectByPrimaryKey(String id);
    
    int updateByPrimaryKeySelective(MrsRechargeAduitDto record);
    
    int updateByAuditStatus(MrsRechargeAduitDto record);

    int updateByPrimaryKey(MrsRechargeAduitDto record);
    
    /**
     * 根据参数查询所有充值待审核数据
     * @param mrsRechargeAduitDto
     * @return
     */
    List<MrsRechargeAduitDto> queryAllData(MrsRechargeAduitDto mrsRechargeAduitDto);
}