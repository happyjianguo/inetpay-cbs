package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.constant.bis.EMessageType;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisEmailAdministratorDto;
@MybatisMapper("bisEmailAdministratorDtoMapper")
public interface BisEmailAdministratorDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisEmailAdministratorDto record);

    int insertSelective(BisEmailAdministratorDto record);

    BisEmailAdministratorDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisEmailAdministratorDto record);

    int updateByPrimaryKey(BisEmailAdministratorDto record);
    /**
     * 根据信息类型判断是否已存在该类型的收件人配置
     * @param messageType
     * @return
     */
	long isExistByMessageType(@Param("messageType")EMessageType messageType);
	/**
	 * 根据信息类型删除收件人配置
	 * @param messageType
	 * @return
	 */
	long deleteByMessageType(@Param("messageType")EMessageType messageType);
	/**
	 * 批量保存记录
	 * @param dtos
	 * @return
	 */
	long batchSave(@Param("dtos")List<BisEmailAdministratorDto> dtos);
	/**
	 * 查询列表
	 * @param queryParam
	 * @return
	 */
	List<BisEmailAdministratorDto> list(BisEmailAdministratorDto queryParam);
	/**
	 * 根据用户名获取收件人配置
	 * @param userName
	 * @return
	 */
	List<BisEmailAdministratorDto> findByUserName(String userName);
	/**
	 * 根据邮箱获取收件人配置
	 * @param email
	 * @return
	 */
	List<BisEmailAdministratorDto> findByEmail(String email);
	/**
	 * 根据信息类型获取收件人记录
	 * @param messageType
	 * @return
	 */
	List<BisEmailAdministratorDto> viewByMessageType(@Param("messageType")EMessageType messageType);
}