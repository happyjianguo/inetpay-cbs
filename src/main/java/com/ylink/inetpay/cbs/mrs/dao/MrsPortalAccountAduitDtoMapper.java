package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalAccountAduitDto;
@MybatisMapper("mrsPortalAccountAduitDtoMapper")
public interface MrsPortalAccountAduitDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsPortalAccountAduitDto record);

    int insertSelective(MrsPortalAccountAduitDto record);

    MrsPortalAccountAduitDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsPortalAccountAduitDto record);

    int updateByPrimaryKey(MrsPortalAccountAduitDto record);
    
    MrsPortalAccountAduitDto selectByAduitId(String aduitId);
}