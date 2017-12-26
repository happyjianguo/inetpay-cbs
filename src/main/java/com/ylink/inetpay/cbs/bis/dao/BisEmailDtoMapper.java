package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailDto;
@MybatisMapper("bisEmailDtoMapper")
public interface BisEmailDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisEmailDto record);

    int insertSelective(BisEmailDto record);

    BisEmailDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisEmailDto record);

    int updateByPrimaryKey(BisEmailDto record);

	List<BisEmailDto> list(BisEmailDto bisEmailDto);
}