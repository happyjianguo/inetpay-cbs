package com.ylink.inetpay.cbs.bis.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EBisTemplateCode;
import com.ylink.inetpay.common.core.constant.ENormalDisabledStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsTemplateDto;
@MybatisMapper("bisSmsTemplateDtoMapper")
public interface BisSmsTemplateDtoMapper {
   // int deleteByPrimaryKey(String id);

  //  int insert(BisSmsTemplateDto record);

  //  int insertSelective(BisSmsTemplateDto record);

    BisSmsTemplateDto selectByPrimaryKey(String id);

  //  int updateByPrimaryKeySelective(BisSmsTemplateDto record);

    int updateByPrimaryKey(BisSmsTemplateDto record);
    /*
     * 分页查询短信模板
     */
	List<BisSmsTemplateDto> list(BisSmsTemplateDto bisSmsTemplateDto);
	/**
	 * 根据编码，查询符合要求的短信模板
	 * @param templateCode
	 * @return
	 */
	BisSmsTemplateDto getSmsTempla(@Param("templateCode")EBisTemplateCode templateCode);
}