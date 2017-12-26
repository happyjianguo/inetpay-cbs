package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EBisBusinessType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisBillingTemplateDto;
@MybatisMapper("bisBillingTemplateDtoMapper")
public interface BisBillingTemplateDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisBillingTemplateDto record);

    int insertSelective(BisBillingTemplateDto record);

    BisBillingTemplateDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisBillingTemplateDto record);

    int updateByPrimaryKey(BisBillingTemplateDto record);

	List<BisBillingTemplateDto> list(BisBillingTemplateDto bisBillingTemplateDto);

	List<BisBillingTemplateDto> selectByTrade(EBisBusinessType businessType);
}