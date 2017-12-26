package com.ylink.inetpay.cbs.chl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.channel.dto.TbChlParamDto;
@MybatisMapper("chlParamMapper")
public interface ChlParamMapper {
  //  int deleteByPrimaryKey(String id);

   // int insert(TbChlParamDto record);

 //   int insertSelective(TbChlParamDto record);

    TbChlParamDto selectByPrimaryKey(String id);
    
    TbChlParamDto selectByChannelCodeAndParam(TbChlParamDto record);

 //   int updateByPrimaryKeySelective(TbChlParamDto record);

 //   int updateByPrimaryKey(TbChlParamDto record);
    /**
     * 获取参数列表
     * @param chlParamDto
     * @return
     */
	List<TbChlParamDto> list(TbChlParamDto chlParamDto);
	
	List<TbChlParamDto> listByChannelCodeAndParam(@Param("channelCodeList")List<String> channelCodeList, 
			@Param("paramCodeList")List<String> paramCodeList);
	
}