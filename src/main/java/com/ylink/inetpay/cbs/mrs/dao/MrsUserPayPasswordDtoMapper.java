package com.ylink.inetpay.cbs.mrs.dao;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsUserPayPasswordDto;
@MybatisMapper("mrsUserPayPasswordDtoMapper")
public interface MrsUserPayPasswordDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsUserPayPasswordDto record);

    int insertSelective(MrsUserPayPasswordDto record);

    MrsUserPayPasswordDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsUserPayPasswordDto record);

    int updateByPrimaryKey(MrsUserPayPasswordDto record);
    /**
     * 根据一户通Id进行查找
     * @param custId
     * @return
     */
    MrsUserPayPasswordDto selectByCustId(String custId);
    /*
     * 清空锁定时间和次数为0
     */
    int clearLockTime(@Param("id") String id);
}