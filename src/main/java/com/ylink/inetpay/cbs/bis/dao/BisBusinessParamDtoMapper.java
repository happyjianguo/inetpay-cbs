package com.ylink.inetpay.cbs.bis.dao;


import com.ylink.inetpay.common.project.cbs.dto.bis.BisBusinessParamDto;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import java.util.ArrayList;
import java.util.List;

@MybatisMapper("bisBusinessParamDtoMapper")
public interface BisBusinessParamDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisBusinessParamDto record);

    int insertSelective(BisBusinessParamDto record);

    BisBusinessParamDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisBusinessParamDto record);

    int updateByPrimaryKey(BisBusinessParamDto record);

    List<BisBusinessParamDto> listByBusiCode(String busiCode);
    /**
     * 批量保存
     * @param bisBusinessParamDtoList
     * @return
     */
    int batchSave(@Param("params")List<BisBusinessParamDto> params);
    /**
     * 修改业务参数
     * @param bisBusinessParamDto
     * @return
     */
	long updateBusinessParam(BisBusinessParamDto bisBusinessParamDto);
 
}