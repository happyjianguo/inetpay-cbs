package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalPersonOpenDto;
@MybatisMapper("mrsPortalPersonOpenDtoMapper")
public interface MrsPortalPersonOpenDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsPortalPersonOpenDto record);

    int insertSelective(MrsPortalPersonOpenDto record);

    MrsPortalPersonOpenDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsPortalPersonOpenDto record);

    int updateByPrimaryKey(MrsPortalPersonOpenDto record);
}