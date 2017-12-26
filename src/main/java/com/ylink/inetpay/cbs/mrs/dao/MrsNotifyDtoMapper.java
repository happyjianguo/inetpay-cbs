package com.ylink.inetpay.cbs.mrs.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsNotifyDto;

@MybatisMapper("mrsNotifyDtoMapper")
public interface MrsNotifyDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsNotifyDto record);

    int insertSelective(MrsNotifyDto record);

    MrsNotifyDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsNotifyDto record);

    int updateByPrimaryKey(MrsNotifyDto record);
    
    /**
     * 每次查找100条数据
     * @param status
     * @param maxNotifyNum
     * @return
     */
    List<MrsNotifyDto> findByStatusAndMaxNotifyNum(@Param("statusList")List<String> status, @Param("notifyNum")Integer maxNotifyNum);
    
    int updateStatusById(@Param("id")String id, @Param("status")String status, @Param("notifyNum")Integer notifyNum, 
    		@Param("updateTime")Date date);

	MrsNotifyDto findByCustIdStatus(@Param("busiType")String busiType,@Param("custId")String custId, @Param("statusList")List<String> statusList);

	List<MrsNotifyDto> findByCustIdForCustomerCode(@Param("custId")String custId, @Param("busiType")String busiType);
	
	List<MrsNotifyDto> findByCustIdAndBusiType(@Param("custId")String custId, @Param("busiType")String busiType);
	
}