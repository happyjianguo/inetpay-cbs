package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsConfAuditDto;
@MybatisMapper("mrsConfAuditDtoMapper")
public interface MrsConfAuditDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsConfAuditDto record);

    int insertSelective(MrsConfAuditDto record);

    MrsConfAuditDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsConfAuditDto record);

    int updateByPrimaryKey(MrsConfAuditDto record);
    /**
     * 
     *方法描述：根据业务类型，发起端查询审核配置信息
     * 创建人：ydx
     * 创建时间：2017年2月10日 下午4:06:52
     * @param busiType 业务类型
     * @param sendPort 发起端
     * @return 审核配置对象
     */
    MrsConfAuditDto findByBusiTypeAndSendType(@Param("busiType")String busiType, @Param("sendPort")String sendPort);
    /**
     * 根据参数查询所有数据
     * @author dxd
     * 
     */
    List<MrsConfAuditDto> quaryAllData(MrsConfAuditDto mrsConfAuditDto);
}