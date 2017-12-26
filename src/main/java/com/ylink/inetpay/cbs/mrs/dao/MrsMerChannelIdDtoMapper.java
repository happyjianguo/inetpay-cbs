package com.ylink.inetpay.cbs.mrs.dao;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsMerChannelIdDto;
@MybatisMapper("mrsMerChannelIdDtoMapper")
public interface MrsMerChannelIdDtoMapper {
    int insert(MrsMerChannelIdDto record);

    int insertSelective(MrsMerChannelIdDto record);
    /**
     * 根据一户同账号获取渠道id
     * @param custId
     * @return
     */
	String getMerChannelIdByCustId(String custId);
	/**
	 * 修改渠道id
	 * @param dto
	 * @return
	 */
	int updateMerChannelId(MrsMerChannelIdDto dto);
}