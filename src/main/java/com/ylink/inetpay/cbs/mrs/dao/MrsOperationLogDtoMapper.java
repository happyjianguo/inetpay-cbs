package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;
import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsOperationLogDto;
@MybatisMapper("mrsOperationLogDtoMapper")
public interface MrsOperationLogDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsOperationLogDto record);

    int insertSelective(MrsOperationLogDto record);

    MrsOperationLogDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsOperationLogDto record);

    int updateByPrimaryKey(MrsOperationLogDto record);
    /**
     * 分页查询用户操作日志
     * @param mrsOperationLogDto
     * @return
     */
	List<MrsOperationLogDto> list(MrsOperationLogDto mrsOperationLogDto);
}