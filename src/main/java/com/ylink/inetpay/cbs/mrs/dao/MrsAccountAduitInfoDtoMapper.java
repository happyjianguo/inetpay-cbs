package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAccountAduitInfoDto;
@MybatisMapper("mrsAccountAduitInfoDtoMapper")
public interface MrsAccountAduitInfoDtoMapper {
    void deleteByPrimaryKey(String id);

    void insert(MrsAccountAduitInfoDto record);

    int insertSelective(MrsAccountAduitInfoDto record);

    MrsAccountAduitInfoDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsAccountAduitInfoDto record);

    void updateByPrimaryKey(MrsAccountAduitInfoDto record);
    
    List<MrsAccountAduitInfoDto> findListPage(MrsAccountAduitInfoDto infoDto);
    
    List <MrsAccountAduitInfoDto> getByIds(MrsAccountAduitInfoDto queryParam);
}