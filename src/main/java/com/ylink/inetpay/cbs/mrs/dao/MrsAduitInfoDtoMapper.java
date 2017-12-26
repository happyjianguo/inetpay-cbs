package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitInfoDto;
@MybatisMapper("mrsAduitInfoDtoMapper")
public interface MrsAduitInfoDtoMapper {
    void deleteByPrimaryKey(String id);

    int insert(MrsAduitInfoDto record);

    int insertSelective(MrsAduitInfoDto record);

    MrsAduitInfoDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsAduitInfoDto record);

    void updateByPrimaryKey(MrsAduitInfoDto record);
    
    List <MrsAduitInfoDto> findListPage(MrsAduitInfoDto dto);
    
    List <MrsAduitInfoDto> getByIds(MrsAduitInfoDto queryParam);
    
    /**
     * 根据用户主键，查询已审核的数据
     * @param loginId
     * @return
     */
    List<MrsAduitInfoDto> findListByLoginId(@Param("loginId")String loginId);
    /**
     * 
     *方法描述：根据custId 查询审核不是终态的一户通号
     * 创建人：ydx
     * 创建时间：2017年3月9日 下午5:23:58
     * @param custId
     * @return
     */
    List<MrsAduitInfoDto> findByCustIdNoFinalStatus(@Param("custId")String custId );
}