package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.core.constant.EActRuleType;
import com.ylink.inetpay.common.core.constant.ETradeType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActRuleAuditDto;

@MybatisMapper("bisActRuleAuditDtoMapper")
public interface BisActRuleAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisActRuleAuditDto record);

    int insertSelective(BisActRuleAuditDto record);

    BisActRuleAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisActRuleAuditDto record);

    int updateByPrimaryKeyWithBLOBs(BisActRuleAuditDto record);

    int updateByPrimaryKey(BisActRuleAuditDto record);
    
    List<BisActRuleAuditDto> list(BisActRuleAuditDto queryParam);
    
    long countWaitAuditByTradeTypeAndRuleType(@Param("tradeType")ETradeType tradeType, @Param("ruleType")EActRuleType ruleType);
    /**
     * 获取审核记录数
     * @param ids
     * @param auditPass
     * @param uneffective
     * @return
     */
	long getAuditNum(@Param("ids")List<String> ids, @Param("auditPass")BISAuditStatus auditPass);
}