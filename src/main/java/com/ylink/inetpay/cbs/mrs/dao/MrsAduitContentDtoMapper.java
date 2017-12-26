package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitContentDto;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitContentDtoWithBLOBs;
@MybatisMapper("mrsAduitContentDtoMapper")
public interface MrsAduitContentDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsAduitContentDtoWithBLOBs record);

    int insertSelective(MrsAduitContentDtoWithBLOBs record);

    MrsAduitContentDtoWithBLOBs selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsAduitContentDtoWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(MrsAduitContentDtoWithBLOBs record);

    int updateByPrimaryKey(MrsAduitContentDto record);
    
    List<MrsAduitContentDtoWithBLOBs> selectByAuditId(String id);
    
    void deleteByAduitId(String id);
}