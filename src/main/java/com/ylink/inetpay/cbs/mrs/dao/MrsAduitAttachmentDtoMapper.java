package com.ylink.inetpay.cbs.mrs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import com.ylink.inetpay.common.project.cbs.dto.mrs.MrsAduitAttachmentDto;
@MybatisMapper("mrsAduitAttachmentDtoMapper")
public interface MrsAduitAttachmentDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(MrsAduitAttachmentDto record);

    int insertSelective(MrsAduitAttachmentDto record);

    MrsAduitAttachmentDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MrsAduitAttachmentDto record);

    int updateByPrimaryKey(MrsAduitAttachmentDto record);
    /**
     * 
     *方法描述：批量插入审核信息附件表
     * 创建人：ydx
     * 创建时间：2017年2月23日 下午2:27:38
     * @param list
     */
    void batchInsert(@Param("aduitAttachmentList") List<MrsAduitAttachmentDto> list);
    
    List<String> findMrsAduitAttachmentDtoByAduitId(String aduitId);
}