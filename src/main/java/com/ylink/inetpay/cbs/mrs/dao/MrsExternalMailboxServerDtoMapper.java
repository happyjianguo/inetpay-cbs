package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsExternalMailboxServerDto;
@MybatisMapper("mrsExternalMailboxServerDtoMapper")
public interface MrsExternalMailboxServerDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsExternalMailboxServerDto record);

    int insertSelective(MrsExternalMailboxServerDto record);

    MrsExternalMailboxServerDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsExternalMailboxServerDto record);

    int updateByPrimaryKey(MrsExternalMailboxServerDto record);
    /**
     * 根据后缀获取邮箱服务器地址
     * @param sUFFI
     * @return
     */
    String getServerPath(String suffi);
	/**
	 * 查询外部邮箱管理列表
	 * @param mrsExternalMailboxServerDto
	 * @return
	 */
	List<MrsExternalMailboxServerDto> list(
			MrsExternalMailboxServerDto mrsExternalMailboxServerDto);
}