package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisMessageNotificationDto;
@MybatisMapper("bisMessageNotificationDtoMapper")
public interface BisMessageNotificationDtoMapper {
    int insert(BisMessageNotificationDto record);

    int insertSelective(BisMessageNotificationDto record);
    
    BisMessageNotificationDto selectByPrimaryKey(String id);
    /**
     * 分页查询短信消息
     * @param bisMessageNotificationDto
     * @return
     */
    List<BisMessageNotificationDto> list(BisMessageNotificationDto bisMessageNotificationDto);
}