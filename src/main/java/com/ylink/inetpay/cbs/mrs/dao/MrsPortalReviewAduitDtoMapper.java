package com.ylink.inetpay.cbs.mrs.dao;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsPortalReviewAduitDto;
@MybatisMapper("mrsPortalReviewAduitDtoMapper")
public interface MrsPortalReviewAduitDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsPortalReviewAduitDto record);

    int insertSelective(MrsPortalReviewAduitDto record);

    MrsPortalReviewAduitDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsPortalReviewAduitDto record);

    int updateByPrimaryKey(MrsPortalReviewAduitDto record);
    
    int updateByRechargeId(MrsPortalReviewAduitDto record);
    
    /**
     * 根据业务编号和业务类型查找实体
     * @param busiNo
     * @param busiType
     * @return
     */
    MrsPortalReviewAduitDto selectByBusiNoAndType(@Param("busiNo")String busiNo,@Param("busiType")String busiType);
    
    /**
     * 根据业务编号和业务类型查找实体（只查待审核）
     * @param busiNo
     * @param busiType
     * @return
     */
    MrsPortalReviewAduitDto selectByBusiNoAndTypeWait(@Param("busiNo")String busiNo,@Param("busiType")String busiType);
}