package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EBisSendStatus;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisSmsDto;
@MybatisMapper("bisSmsDtoMapper")
public interface BisSmsDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisSmsDto record);

    int insertSelective(BisSmsDto record);

    BisSmsDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisSmsDto record);

    int updateByPrimaryKey(BisSmsDto record);
    
	List<BisSmsDto> list(BisSmsDto bisSmsDto);
	/**
	 * 根据时间和发送状态，查询未发送且发送时间范围内的
	 * @param time
	 * @param staySend
	 * @return
	 */
	List<BisSmsDto> getSms(@Param("time")String time, @Param("sendStatus")EBisSendStatus staySend);
	/**
	 * 更新短信发送状态
	 * @param newStatus
	 * @param oldStatus
	 */
	int updateStatus(@Param("newStatus")EBisSendStatus newStatus,
			@Param("oldStatus")EBisSendStatus oldStatus,@Param("id")String id);
}