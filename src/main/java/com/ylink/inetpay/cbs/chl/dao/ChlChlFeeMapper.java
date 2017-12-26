package com.ylink.inetpay.cbs.chl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.channel.dto.ChlChlFeeDto;
@MybatisMapper("chlChlFeeMapper")
public interface ChlChlFeeMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChlChlFeeDto record);

    int insertSelective(ChlChlFeeDto record);

    ChlChlFeeDto selectByPrimaryKey(String id);
    ChlChlFeeDto selectByChlCode(@Param("chlCode")String chlCode,@Param("status")String status);
    
    List<ChlChlFeeDto> selectByStatus(String status);

    int updateByPrimaryKeySelective(ChlChlFeeDto record);

    int updateByPrimaryKey(ChlChlFeeDto record);
    /**
     * 分页查询计费模板
     * @param queryParam
     * @return
     */
	List<ChlChlFeeDto> findAll(ChlChlFeeDto queryParam);

	/**
	 * 查询所有渠道名称
	 * @return
	 */
	List<ChlChlFeeDto> queryAllChannels();
}