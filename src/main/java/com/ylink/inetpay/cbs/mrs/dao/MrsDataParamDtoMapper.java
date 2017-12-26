package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsDataParamDto;
import com.ylink.inetpay.common.project.portal.vo.customer.DataParamVO;

@MybatisMapper("mrsDataParamDtoMapper")
public interface MrsDataParamDtoMapper {
   
	List<MrsDataParamDto> list(MrsDataParamDto queryParam);

	MrsDataParamDto findByCodeType(@Param("paramCode")String code, @Param("paramType")String type);

	List<DataParamVO> findByType(@Param("paramType")String type);

}