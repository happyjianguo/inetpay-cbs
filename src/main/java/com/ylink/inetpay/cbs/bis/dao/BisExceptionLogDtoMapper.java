package com.ylink.inetpay.cbs.bis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.core.constant.EBisExceptionLogNlevel;
import com.ylink.inetpay.common.project.cbs.constant.bis.EIsSend;
import com.ylink.inetpay.common.project.cbs.dto.bis.BisExceptionLogDto;
@MybatisMapper("bisExceptionLogDtoMapper")
public interface BisExceptionLogDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisExceptionLogDto record);

    int insertSelective(BisExceptionLogDto record);

    BisExceptionLogDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisExceptionLogDto record);

    int updateByPrimaryKey(BisExceptionLogDto record);
    /**
     * 分页查询异常日志信息
     * @param bisExceptionLogDto
     * @return
     */
	List<BisExceptionLogDto> list(BisExceptionLogDto bisExceptionLogDto);
	/**
	 * 根据id修改异常日志的发送状态
	 * @param alreadySend
	 * @param id
	 * @return
	 */
	long updateSendStatusById(@Param("sendStatus")EIsSend sendStatus, @Param("id")String id);
	/**
	 * 获取异常级别为"错误"并且未发送的异常记录
	 * @param error
	 * @return
	 */
	List<BisExceptionLogDto> findAbleSendExceptionLog(@Param("nlevel")EBisExceptionLogNlevel nlevel);
}