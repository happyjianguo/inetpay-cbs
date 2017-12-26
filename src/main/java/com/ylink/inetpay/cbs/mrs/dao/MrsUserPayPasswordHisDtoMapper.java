package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordHisDto;
@MybatisMapper("mrsUserPayPasswordHisDtoMapper")
public interface MrsUserPayPasswordHisDtoMapper {
    int insert(MrsUserPayPasswordHisDto record);

    int insertSelective(MrsUserPayPasswordHisDto record);
}