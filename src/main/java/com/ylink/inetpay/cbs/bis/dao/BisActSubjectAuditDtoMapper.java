package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.BISAuditStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisActSubjectAuditDto;

@MybatisMapper("bisActSubjectAuditDtoMapper")
public interface BisActSubjectAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisActSubjectAuditDto record);

    int insertSelective(BisActSubjectAuditDto record);

    BisActSubjectAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisActSubjectAuditDto record);

    int updateByPrimaryKeyWithBLOBs(BisActSubjectAuditDto record);

    int updateByPrimaryKey(BisActSubjectAuditDto record);
    
    List<BisActSubjectAuditDto> list(BisActSubjectAuditDto record);
    
    long countWaitAuditBySubjectNo(String subjectNo);
    /**
     * 获取审核记录数
     * @param ids
     * @param auditPass
     * @param uneffective
     * @return
     */
	long getAuditNum(@Param("ids")List<String> ids, @Param("auditPass")BISAuditStatus auditPass);
}