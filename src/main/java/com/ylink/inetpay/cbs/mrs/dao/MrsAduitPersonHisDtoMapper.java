package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonHisDto;
@MybatisMapper("mrsAduitPersonHisDtoMapper")
public interface MrsAduitPersonHisDtoMapper {
    int deleteByPrimaryKey(String id);

    void insert(MrsAduitPersonHisDto record);

    int insertSelective(MrsAduitPersonHisDto record);

    MrsAduitPersonHisDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsAduitPersonHisDto record);

    int updateByPrimaryKey(MrsAduitPersonHisDto record);

    List<MrsAduitPersonHisDto> findListHisPage(MrsAduitPersonHisDto hisDto);
    
    List<MrsAduitPersonHisDto> findListHisAll(MrsAduitPersonHisDto hisDto);
    /**
     * 根据用户主键，查询已审核的数据
     * @param loginId
     * @return
     */
    List<MrsAduitPersonHisDto> findListHisByLoginId(@Param("loginId")String loginId);
    
    MrsAduitPersonHisDto findByAduitIdAndUserId(@Param("aduitId")String aduitId,@Param("userId")String userId);
}