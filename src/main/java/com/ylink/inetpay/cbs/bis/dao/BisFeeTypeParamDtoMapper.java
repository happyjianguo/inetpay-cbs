package com.ylink.inetpay.cbs.bis.dao;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisFeeTypeParamDto;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

@MybatisMapper("bisFeeTypeParamDtoMapper")
public interface BisFeeTypeParamDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisFeeTypeParamDto record);

    int insertSelective(BisFeeTypeParamDto record);

    BisFeeTypeParamDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisFeeTypeParamDto record);

    int updateByPrimaryKey(BisFeeTypeParamDto record);

	List<BisFeeTypeParamDto> list(BisFeeTypeParamDto queryParam);
}