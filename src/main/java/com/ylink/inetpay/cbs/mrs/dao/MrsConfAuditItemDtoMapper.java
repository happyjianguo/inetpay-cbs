package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditItemDto;
@MybatisMapper("mrsConfAuditItemDtoMapper")
public interface MrsConfAuditItemDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsConfAuditItemDto record);

    int insertSelective(MrsConfAuditItemDto record);

    MrsConfAuditItemDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsConfAuditItemDto record);

    int updateByPrimaryKey(MrsConfAuditItemDto record);
    /**
     * 
     *方法描述：根据审核配置信息主键查询审核配置明细信息
     * 创建人：ydx
     * 创建时间：2017年2月10日 下午5:41:52
     * @param auditId 审核配置主键
     * @return
     */
    List<MrsConfAuditItemDto> selectByAuditId(String auditId);
    /**
     * 
     *方法描述：根据业务类型，发起端查询审核配置明细信息（联合查询，先查主表信息，再查明细信息）
     * 创建人：ydx
     * 创建时间：2017年2月10日 下午5:52:27
     * @param sendPort 发起段
     * @param busiType 业务类型
     * @return
     */
    List<MrsConfAuditItemDto> selectBySendPortAndBusiTye(String sendPort,String busiType);
    /**
     * 根据审核配置表主键删除审核配置明细信息
     */
    int deleteByAuditId(String auditId);
}