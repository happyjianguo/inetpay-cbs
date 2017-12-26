package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitPersonDto;
@MybatisMapper("mrsAduitPersonDtoMapper")
public interface MrsAduitPersonDtoMapper {
    void deleteByPrimaryKey(String id);

    void insert(MrsAduitPersonDto record);

    int insertSelective(MrsAduitPersonDto record);

    MrsAduitPersonDto selectByPrimaryKey(String id);

    MrsAduitPersonDto selectByAduitUserName(@Param("aduitUserId")String aduitUserId,@Param("aduitId")String aduitId);
    	
    int updateByPrimaryKeySelective(MrsAduitPersonDto record);

    void updateByPrimaryKey(MrsAduitPersonDto record);
    
    List<MrsAduitPersonDto> findListPerPage(MrsAduitPersonDto perDto);
    
    /**
     * 修改审核人状态
     */
    void updateStatusByKey(String id);
    
    List<String> getByCurrentUserId(String id);
    
    MrsAduitPersonDto selectByAduitId(@Param("aduitId")String aduitId,@Param("aduitUserId")String aduitUserId);
    /**
     * 
     *方法描述：批量插入审核人信息
     * 创建人：ydx
     * 创建时间：2017年2月10日 下午7:45:29
     * @param list
     */
    void batchInsert(@Param("aduitPersonList") List<MrsAduitPersonDto> list);
    
    List<MrsAduitPersonDto> findByAduitId(String aduitId);
}