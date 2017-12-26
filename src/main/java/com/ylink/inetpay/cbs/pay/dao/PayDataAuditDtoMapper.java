package com.ylink.inetpay.cbs.pay.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.pay.PayDataAuditDto;
@MybatisMapper("payDataAuditDtoMapper")
public interface PayDataAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PayDataAuditDto record);

    int insertSelective(PayDataAuditDto record);

    PayDataAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PayDataAuditDto record);

    int updateByPrimaryKey(PayDataAuditDto record);
    /**
	 * 检查是否有待审核数据
	 * @param refId
	 * @return
	 */
    PayDataAuditDto selectByRefId(String refId);
}