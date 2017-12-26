package com.ylink.inetpay.cbs.bis.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisActInterestDateAuditDto;

@MybatisMapper("bisActInterestDateAuditDtoMapper")
public interface BisActInterestDateAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisActInterestDateAuditDto record);

    int insertSelective(BisActInterestDateAuditDto record);

    BisActInterestDateAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisActInterestDateAuditDto record);

    int updateByPrimaryKeyWithBLOBs(BisActInterestDateAuditDto record);

    int updateByPrimaryKey(BisActInterestDateAuditDto record);
    
    int countWaitAuditWithAccountId(String accountId);
    
    List<BisActInterestDateAuditDto> list(BisActInterestDateAuditDto queryParam);
    /**
     * 获取待复核或者复核通过未生效的记录
     * @param accountIds
     * @return
     */
	List<BisActInterestDateAuditDto> findWaitAudit(@Param("accountIds")List<String> accountIds);
}