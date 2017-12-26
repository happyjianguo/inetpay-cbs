package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPayBatchDetailDto;
@MybatisMapper("mrsPayBatchDetailDtoMapper")
public interface MrsPayBatchDetailDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsPayBatchDetailDto record);

    int insertSelective(MrsPayBatchDetailDto record);

    MrsPayBatchDetailDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsPayBatchDetailDto record);

    int updateByPrimaryKey(MrsPayBatchDetailDto record);
    /**
     * 
     *方法描述：批量插入
     * @param list
     */
    void batchInsert(@Param("mrsPayBatchDetailList") List<MrsPayBatchDetailDto> list);
    /**
     * 根据包号查询所有数据
     * @param batchNo
     * @return
     */
    List<MrsPayBatchDetailDto> selectByBatchNo(@Param("batchNo")String batchNo);
    
}