package com.ylink.inetpay.cbs.bis.dao;

import com.ylink.inetpay.common.project.cbs.dto.bis.BisBusinessDto;
import com.ylink.inetpay.common.project.cbs.vo.bis.BisBusinessVO;
import org.apache.ibatis.annotations.Param;
import org.ylinkpay.framework.mybatis.annotation.MybatisMapper;

import java.util.List;

@MybatisMapper("bisBusinessDtoMapper")
public interface BisBusinessDtoMapper {
    int deleteByPrimaryKey(String id);

    int insert(BisBusinessDto record);

    int insertSelective(BisBusinessDto record);

    BisBusinessDto selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BisBusinessDto record);

    int updateByPrimaryKey(BisBusinessDto record);

    List<BisBusinessDto> list(BisBusinessVO vo);

    List<BisBusinessDto> find(@Param("businessCode") String businessCode, @Param("payeeCode") String payeeCode, @Param("status") String status);

	List<BisBusinessDto> queryAccessorCode(String id);
	/**
	 * 根据业务id获取业务dto
	 * @param businessCode
	 * @return
	 */
	BisBusinessDto findByBusinessCode(String businessCode);

	 
}