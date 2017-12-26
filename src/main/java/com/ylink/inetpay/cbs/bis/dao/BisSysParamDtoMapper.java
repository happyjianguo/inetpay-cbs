package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisSysParamDto;
@MybatisMapper("bisSysParamDtoMapper")
public interface BisSysParamDtoMapper {
  //  int deleteByPrimaryKey(String id);

    int insert(BisSysParamDto record);

   // int insertSelective(BisSysParamDto record);

	//编辑明细不加缓存
    BisSysParamDto selectByPrimaryKey(String id);

  //  int updateByPrimaryKeySelective(BisSysParamDto record);

    int updateByPrimaryKey(BisSysParamDto record);

    //分页查询不加缓存
	List<BisSysParamDto> list(BisSysParamDto bisSysParamDto);

	//List<BisSysParamDto> mapAllCode();

//	String getValue(String parmCode);
	BisSysParamDto selectByKey(@Param("key")String key);
	/**
	 * 根据组名获取系统参数
	 * @param groupName
	 * @return
	 */
	List<BisSysParamDto> getLimitParams(String groupName);
	/**
	 * 查询多个系统参数信息
	 * 
	 * @param paramNames
	 * @return
	 */
	List<BisSysParamDto> findByParamNames(List<String> paramNames);
}