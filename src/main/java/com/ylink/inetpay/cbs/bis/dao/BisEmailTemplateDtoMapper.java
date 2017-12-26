package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EBisEmailTemplateCode;
import com.ylink.inetpay.common.core.constant.ENormalDisabledStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailTemplateDto;
@MybatisMapper("bisEmailTemplateDtoMapper")
public interface BisEmailTemplateDtoMapper {
   // int deleteByPrimaryKey(String id);

 //   int insert(BisEmailTemplateDto record);

 //   int insertSelective(BisEmailTemplateDto record);

    BisEmailTemplateDto selectByPrimaryKey(String id);

//    int updateByPrimaryKeySelective(BisEmailTemplateDto record);

    int updateByPrimaryKey(BisEmailTemplateDto record);
    /**
     * 根据编号获取邮件模板
     * @param templateCode
     * @return
     */
	BisEmailTemplateDto getEmailTempla(@Param("templateCode")EBisEmailTemplateCode templateCode);
	/**
	 * 分页查询邮件模板列表
	 * @param bisEmailTemplateDto
	 * @return
	 */
	List<BisEmailTemplateDto> list(BisEmailTemplateDto bisEmailTemplateDto);
}